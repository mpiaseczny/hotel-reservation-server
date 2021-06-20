package pl.wat.wcy.server.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.wcy.server.dao.*;
import pl.wat.wcy.server.dto.File;
import pl.wat.wcy.server.dto.HotelDTO;
import pl.wat.wcy.server.dto.OpinionDTO;
import pl.wat.wcy.server.dto.OpinionRequest;
import pl.wat.wcy.server.repository.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class HotelService {
    private final AuthService authService;

    private final HotelRepository hotelRepository;
    private final AttachmentRepository attachmentRepository;
    private final RoomRepository roomRepository;
    private final OpinionRepository opinionRepository;
    private final ReservationRepository reservationRepository;

    public ResponseEntity<List<HotelDTO>> getHotels(String city) {
        List<HotelDTO> hotelsResponseList = new LinkedList<>();

        long hotelCount = hotelRepository.count();
        if (hotelCount == 0) {
            return new ResponseEntity<>(hotelsResponseList, HttpStatus.OK);
        }

        List<Hotel> hotels = hotelRepository.findAll();
        if (city != null && !city.isEmpty()) {
            hotels = hotels.stream().filter(hotel -> hotel.getCity().startsWith(city)).collect(Collectors.toList());
        }

        for (Hotel hotel: hotels) {
            File file = null;
            Optional<Attachment> attachment = attachmentRepository.findByHotel_Id(hotel.getId());
            if (attachment.isPresent()) {
                file = new File();
                file.setName(attachment.get().getName());
                file.setFile(attachment.get().getFile());
            }

            HotelDTO hotelDTO = HotelDTO.builder()
                    .id(hotel.getId())
                    .name(hotel.getName())
                    .rating(hotel.getRating())
                    .city(hotel.getCity())
                    .street(hotel.getStreet())
                    .phoneNumber(hotel.getPhoneNumber())
                    .email(hotel.getEmail())
                    .attachment(file)
                    .build();

            hotelsResponseList.add(hotelDTO);
        }

        return new ResponseEntity<>(hotelsResponseList, HttpStatus.OK);
    }

    public ResponseEntity<HotelDTO> addHotel(HotelDTO hotelDTO) {
        if (!authService.isCurrentUserAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (hasHotelDTONullParam(hotelDTO)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Hotel hotel = new Hotel();
        hotel.setName(hotelDTO.getName());
        hotel.setRating(hotelDTO.getRating());
        hotel.setCity(hotelDTO.getCity());
        hotel.setStreet(hotelDTO.getStreet());
        hotel.setPhoneNumber(hotelDTO.getPhoneNumber());
        hotel.setEmail(hotelDTO.getEmail());

        Hotel savedHotel = hotelRepository.save(hotel);
        hotelDTO.setId(savedHotel.getId());

        File hotelPhoto = hotelDTO.getAttachment();
        if (hotelPhoto != null) {
            Attachment attachment = new Attachment();
            attachment.setFile(hotelPhoto.getFile());
            attachment.setName(hotelPhoto.getName());
            attachment.setHotel(savedHotel);

            attachmentRepository.save(attachment);
        }

        return new ResponseEntity<>(hotelDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<HotelDTO> updateHotel(Long hotelId, HotelDTO hotelDTO) {
        if (!authService.isCurrentUserAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!hotelRepository.existsById(hotelId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (hasHotelDTONullParam(hotelDTO)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Hotel hotel = hotelRepository.getById(hotelId);
        hotel.setName(hotelDTO.getName());
        hotel.setRating(hotel.getRating());
        hotel.setCity(hotelDTO.getCity());
        hotel.setStreet(hotelDTO.getStreet());
        hotel.setPhoneNumber(hotel.getPhoneNumber());
        hotel.setEmail(hotel.getEmail());

        Hotel savedHotel = hotelRepository.save(hotel);
        hotelDTO.setId(savedHotel.getId());

        attachmentRepository.deleteByHotel_Id(hotel.getId());
        File hotelPhoto = hotelDTO.getAttachment();
        if (hotelPhoto != null) {
            Attachment attachment = new Attachment();
            attachment.setFile(hotelPhoto.getFile());
            attachment.setName(hotelPhoto.getName());
            attachment.setHotel(savedHotel);

            attachmentRepository.save(attachment);
        }

        return new ResponseEntity<>(hotelDTO, HttpStatus.OK);
    }

    public ResponseEntity<Long> deleteHotel(Long hotelId) {
        if (!authService.isCurrentUserAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!hotelRepository.existsById(hotelId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        attachmentRepository.deleteByHotel_Id(hotelId);
        reservationRepository.deleteByRoom_Hotel_Id(hotelId);
        roomRepository.deleteByHotel_Id(hotelId);
        long removedItemCount = hotelRepository.removeById(hotelId);

        return new ResponseEntity<>(removedItemCount, HttpStatus.OK);
    }

    public ResponseEntity<OpinionDTO> addOpinion(Long hotelId, OpinionRequest opinionRequest) {
        if (opinionRequest.getRate() == null || opinionRequest.getComment() == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!authService.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        User currentUser = authService.getCurrentUser();

        Optional<Hotel> optionalHotel = hotelRepository.findById(hotelId);
        if (optionalHotel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Opinion opinion = new Opinion();
        opinion.setComment(opinionRequest.getComment());
        opinion.setRate(opinionRequest.getRate());
        opinion.setHotel(optionalHotel.get());
        opinion.setUser(currentUser);
        Opinion savedOpinion = opinionRepository.save(opinion);

        OpinionDTO opinionDTO = new OpinionDTO();
        opinionDTO.setComment(savedOpinion.getComment());
        opinionDTO.setRate(savedOpinion.getRate());
        opinionDTO.setUsername(savedOpinion.getUser().getName());

        return new ResponseEntity<>(opinionDTO, HttpStatus.CREATED);
    }

    private boolean hasHotelDTONullParam(HotelDTO hotelDTO) {
        return hotelDTO.getName() == null ||
                hotelDTO.getRating() == null ||
                hotelDTO.getCity() == null ||
                hotelDTO.getStreet() == null ||
                hotelDTO.getPhoneNumber() == null ||
                hotelDTO.getEmail() == null;
    }
}
