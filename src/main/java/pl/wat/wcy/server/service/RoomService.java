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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class RoomService {
    private final AuthService authService;

    private final RoomRepository roomRepository;
    private final ReservationRepository reservationRepository;
    private final AttachmentRepository attachmentRepository;
    private final OpinionRepository opinionRepository;
    private final HotelRepository hotelRepository;

    public ResponseEntity<List<RoomListItem>> getRooms(String nameOrCity, Long dateFrom, Long dateTo, Integer people) {
        List<RoomListItem> roomsResponseList = new LinkedList<>();

        long roomCount = roomRepository.count();
        if (roomCount == 0) {
            return new ResponseEntity<>(roomsResponseList, HttpStatus.OK);
        }

        List<Room> rooms = roomRepository.findDistinctByNameOrHotel_CityAllIgnoreCase(nameOrCity, nameOrCity);
        if (dateFrom != null && dateTo != null) {
            rooms = rooms.stream().filter(room -> {
                List<TimeInterval> timeIntervals = getReservationsTimesForRoom(room.getId());
                for (TimeInterval timeInterval: timeIntervals) {
                    if (
                            (dateFrom <= timeInterval.getDateTo() && dateFrom >= timeInterval.getDateFrom()) ||
                                    (dateTo <= timeInterval.getDateTo() && dateTo >= timeInterval.getDateFrom())) {
                        return false;
                    }
                }
                return true;
            }).collect(Collectors.toList());
        }

        if (people != null) {
            rooms = rooms.stream().filter(room -> room.getCapacity() >= people).collect(Collectors.toList());
        }

        for (Room room: rooms) {
            List<Attachment> attachments = attachmentRepository.findByRoom_Id(room.getId());

            List<File> files = new LinkedList<>();
            for (Attachment attachment: attachments) {
                File file = new File();
                file.setName(attachment.getName());
                file.setFile(attachment.getFile());

                files.add(file);
            }

            RoomListItem roomListItem = new RoomListItem();
            roomListItem.setId(room.getId());
            roomListItem.setName(room.getName());
            roomListItem.setCapacity(room.getCapacity());
            roomListItem.setDescription(room.getDescription());
            roomListItem.setFeatures(room.getFeatures());
            roomListItem.setAttachments(files);

            roomsResponseList.add(roomListItem);
        }

        return new ResponseEntity<>(roomsResponseList, HttpStatus.OK);
    }

    public ResponseEntity<RoomDTO> getRoom(Long roomId) {
        Optional<Room> optionalRoom = roomRepository.findById(roomId);

        if (optionalRoom.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Room room = optionalRoom.get();
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

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(room.getId());
        roomDTO.setName(room.getName());
        roomDTO.setDescription(room.getDescription());
        roomDTO.setPrice(room.getPrice());
        roomDTO.setCapacity(room.getCapacity());
        roomDTO.setFeatures(room.getFeatures());
        roomDTO.setAttachments(files);
        roomDTO.setOpinions(opinionDTOS);
        roomDTO.setHotelName(hotel.getName());
        roomDTO.setRating(hotel.getRating());
        roomDTO.setCity(hotel.getCity());
        roomDTO.setStreet(hotel.getStreet());
        roomDTO.setPhoneNumber(hotel.getPhoneNumber());
        roomDTO.setEmail(hotel.getEmail());

        return new ResponseEntity<>(roomDTO, HttpStatus.OK);
    }

    public ResponseEntity<List<TimeInterval>> getReservationTimes(Long roomId) {
        if (!roomRepository.existsById(roomId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        List<TimeInterval> reservationTimes = getReservationsTimesForRoom(roomId);

        return new ResponseEntity<>(reservationTimes, HttpStatus.OK);
    }

    public ResponseEntity<RoomDTO> addRoom(RoomRequest roomRequest) {
        if (!authService.isCurrentUserAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Hotel> optionalHotel = hotelRepository.findById(roomRequest.getHotelId());
        if (optionalHotel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Hotel hotel = optionalHotel.get();
        Room room = new Room();
        room.setHotel(hotel);
        room.setName(roomRequest.getName());
        room.setDescription(roomRequest.getDescription());
        room.setPrice(roomRequest.getPrice());
        room.setCapacity(roomRequest.getCapacity());
        room.setFeatures(roomRequest.getFeatures());

        Room savedRoom = roomRepository.save(room);

        for (File file: roomRequest.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setRoom(savedRoom);
            attachment.setName(file.getName());
            attachment.setFile(file.getFile());

            attachmentRepository.save(attachment);
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

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(savedRoom.getId());
        roomDTO.setName(savedRoom.getName());
        roomDTO.setDescription(savedRoom.getDescription());
        roomDTO.setPrice(savedRoom.getPrice());
        roomDTO.setCapacity(savedRoom.getCapacity());
        roomDTO.setFeatures(savedRoom.getFeatures());
        roomDTO.setAttachments(roomRequest.getAttachments());
        roomDTO.setOpinions(opinionDTOS);
        roomDTO.setHotelName(hotel.getName());
        roomDTO.setRating(hotel.getRating());
        roomDTO.setCity(hotel.getCity());
        roomDTO.setStreet(hotel.getStreet());
        roomDTO.setPhoneNumber(hotel.getPhoneNumber());
        roomDTO.setEmail(hotel.getEmail());

        return new ResponseEntity<>(roomDTO, HttpStatus.CREATED);
    }

    public ResponseEntity<RoomDTO> updateRoom(Long roomId, RoomRequest roomRequest) {
        if (!authService.isCurrentUserAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        Optional<Room> optionalRoom = roomRepository.findById(roomId);
        if (optionalRoom.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Optional<Hotel> optionalHotel = hotelRepository.findById(roomRequest.getHotelId());
        if (optionalHotel.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Room room = optionalRoom.get();
        Hotel hotel = optionalHotel.get();
        room.setName(roomRequest.getName());
        room.setDescription(roomRequest.getDescription());
        room.setPrice(roomRequest.getPrice());
        room.setCapacity(roomRequest.getCapacity());
        room.setFeatures(roomRequest.getFeatures());
        Room savedRoom = roomRepository.save(room);

        attachmentRepository.deleteByRoom_Id(savedRoom.getId());
        for (File file: roomRequest.getAttachments()) {
            Attachment attachment = new Attachment();
            attachment.setRoom(savedRoom);
            attachment.setName(file.getName());
            attachment.setFile(file.getFile());
            attachmentRepository.save(attachment);
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

        RoomDTO roomDTO = new RoomDTO();
        roomDTO.setId(savedRoom.getId());
        roomDTO.setName(savedRoom.getName());
        roomDTO.setDescription(savedRoom.getDescription());
        roomDTO.setPrice(savedRoom.getPrice());
        roomDTO.setCapacity(savedRoom.getCapacity());
        roomDTO.setFeatures(savedRoom.getFeatures());
        roomDTO.setAttachments(roomRequest.getAttachments());
        roomDTO.setOpinions(opinionDTOS);
        roomDTO.setHotelName(hotel.getName());
        roomDTO.setRating(hotel.getRating());
        roomDTO.setCity(hotel.getCity());
        roomDTO.setStreet(hotel.getStreet());
        roomDTO.setPhoneNumber(hotel.getPhoneNumber());
        roomDTO.setEmail(hotel.getEmail());

        return new ResponseEntity<>(roomDTO, HttpStatus.OK);
    }

    public ResponseEntity<Long> deleteRoom(Long roomId) {
        if (!authService.isCurrentUserAdmin()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        if (!roomRepository.existsById(roomId)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        long removedItemCount = roomRepository.removeById(roomId);
        attachmentRepository.deleteByRoom_Id(roomId);
        reservationRepository.deleteByRoom_Id(roomId);

        return new ResponseEntity<>(removedItemCount, HttpStatus.OK);
    }

    private List<TimeInterval> getReservationsTimesForRoom(Long roomId) {
        List<TimeInterval> reservationsTimes = new LinkedList<>();

        List<Reservation> reservations = reservationRepository.findByRoom_IdOrderByDateFromAsc(roomId);
        if (reservations.isEmpty()) {
            return reservationsTimes;
        }

        for (Reservation reservation: reservations) {
            TimeInterval timeInterval = new TimeInterval();
            timeInterval.setDateFrom(reservation.getDateFrom());
            timeInterval.setDateTo(reservation.getDateTo());

            reservationsTimes.add(timeInterval);
        }

        return reservationsTimes;
    }
}
