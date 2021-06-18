package pl.wat.wcy.server.service;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.wat.wcy.server.dao.*;
import pl.wat.wcy.server.dto.*;
import pl.wat.wcy.server.repository.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
public class ReservationService {
    private final AuthService authService;

    private final ReservationRepository reservationRepository;
    private final AttachmentRepository attachmentRepository;
    private final RoomRepository roomRepository;
    private final OpinionRepository opinionRepository;

    public ResponseEntity<List<ReservationListItem>> getReservations() {
        if (!authService.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        User currentUser = authService.getCurrentUser();

        List<Reservation> reservations = reservationRepository.findByUser_IdOrderByDateFromAsc(currentUser.getId());
        List<ReservationListItem> reservationListItems = new LinkedList<>();
        for (Reservation reservation: reservations) {
            List<Attachment> attachments = attachmentRepository.findByRoom_Id(reservation.getRoom().getId());

            File file = null;
            if (attachments != null && attachments.size() >= 1) {
                Attachment attachment = attachments.get(0);
                file = new File();
                file.setFile(attachment.getFile());
                file.setName(attachment.getName());
            }

            ReservationListItem reservationListItem = new ReservationListItem();
            reservationListItem.setId(reservation.getId());
            reservationListItem.setRoomName(reservation.getRoom().getName());
            reservationListItem.setDateFrom(reservation.getDateFrom());
            reservationListItem.setDateTo(reservationListItem.getDateTo());
            reservationListItem.setAttachment(file);

            reservationListItems.add(reservationListItem);
        }

        return new ResponseEntity<>(reservationListItems, HttpStatus.OK);
    }

    public ResponseEntity<ReservationDTO> getReservation(Long reservationId) {
        if (!authService.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);

        if (optionalReservation.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Reservation reservation = optionalReservation.get();
        Room room = reservation.getRoom();
        Hotel hotel = room.getHotel();

        List<Attachment> attachments = attachmentRepository.findByRoom_Id(room.getId());
        List<File> files = new LinkedList<>();
        for (Attachment attachment: attachments) {
            File file = new File();
            file.setName(attachment.getName());
            file.setFile(attachment.getFile());
            files.add(file);
        }

        List<Opinion> opinions = opinionRepository.findByHotel_Id(hotel.getId());
        List<OpinionDTO> opinionDTOS = new LinkedList<>();
        for (Opinion opinion: opinions) {
            OpinionDTO opinionDTO = new OpinionDTO();
            opinionDTO.setUsername(opinion.getUser().getName());
            opinionDTO.setComment(opinion.getComment());
            opinionDTO.setRate(opinion.getRate());

            opinionDTOS.add(opinionDTO);
        }

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(reservation.getId());
        reservationDTO.setName(room.getName());
        reservationDTO.setDescription(room.getDescription());
        reservationDTO.setCapacity(room.getCapacity());
        reservationDTO.setFeatures(room.getFeatures());
        reservationDTO.setAttachments(files);
        reservationDTO.setDateFrom(reservation.getDateFrom());
        reservationDTO.setDateTo(reservation.getDateTo());
        reservationDTO.setTotalPrice(reservation.getPrice());
        reservationDTO.setOpinions(opinionDTOS);
        reservationDTO.setHotelName(hotel.getName());
        reservationDTO.setRating(hotel.getRating());
        reservationDTO.setCity(hotel.getCity());
        reservationDTO.setStreet(hotel.getStreet());
        reservationDTO.setPhoneNumber(hotel.getPhoneNumber());
        reservationDTO.setEmail(hotel.getEmail());

        return new ResponseEntity<>(reservationDTO, HttpStatus.OK);
    }

    public ResponseEntity<ReservationDTO> addReservation(ReservationRequest reservationRequest) {
        if (!authService.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Room> optionalRoom = roomRepository.findById(reservationRequest.getRoomId());
        if (optionalRoom.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Room room = optionalRoom.get();
        Hotel hotel = room.getHotel();
        User currentUser = authService.getCurrentUser();

        Reservation reservation = new Reservation();
        reservation.setUser(currentUser);
        reservation.setDateFrom(reservation.getDateFrom());
        reservation.setDateTo(reservation.getDateTo());
        reservation.setRoom(room);
        reservation.setPrice(reservationRequest.getPrice());
        Reservation savedReservation = reservationRepository.save(reservation);

        List<Attachment> attachments = attachmentRepository.findByRoom_Id(room.getId());
        List<File> files = new LinkedList<>();
        for (Attachment attachment: attachments) {
            File file = new File();
            file.setName(attachment.getName());
            file.setFile(attachment.getFile());
            files.add(file);
        }

        List<Opinion> opinions = opinionRepository.findByHotel_Id(hotel.getId());
        List<OpinionDTO> opinionDTOS = new LinkedList<>();
        for (Opinion opinion: opinions) {
            OpinionDTO opinionDTO = new OpinionDTO();
            opinionDTO.setUsername(opinion.getUser().getName());
            opinionDTO.setComment(opinion.getComment());
            opinionDTO.setRate(opinion.getRate());

            opinionDTOS.add(opinionDTO);
        }

        ReservationDTO reservationDTO = new ReservationDTO();
        reservationDTO.setId(savedReservation.getId());
        reservationDTO.setName(room.getName());
        reservationDTO.setDescription(room.getDescription());
        reservationDTO.setCapacity(room.getCapacity());
        reservationDTO.setFeatures(room.getFeatures());
        reservationDTO.setAttachments(files);
        reservationDTO.setDateFrom(savedReservation.getDateFrom());
        reservationDTO.setDateTo(savedReservation.getDateTo());
        reservationDTO.setTotalPrice(savedReservation.getPrice());
        reservationDTO.setOpinions(opinionDTOS);
        reservationDTO.setHotelName(hotel.getName());
        reservationDTO.setRating(hotel.getRating());
        reservationDTO.setCity(hotel.getCity());
        reservationDTO.setStreet(hotel.getStreet());
        reservationDTO.setPhoneNumber(hotel.getPhoneNumber());
        reservationDTO.setEmail(hotel.getEmail());

        return new ResponseEntity<>(reservationDTO, HttpStatus.OK);
    }

    public ResponseEntity<Long> deleteReservation(Long reservationId) {
        if (!authService.isLoggedIn()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!reservationRepository.existsById(reservationId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        long removedItemCount = reservationRepository.removeById(reservationId);

        return new ResponseEntity<>(removedItemCount, HttpStatus.OK);
    }
}
