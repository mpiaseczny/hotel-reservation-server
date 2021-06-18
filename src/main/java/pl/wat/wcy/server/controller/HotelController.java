package pl.wat.wcy.server.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.wat.wcy.server.dto.HotelDTO;
import pl.wat.wcy.server.dto.OpinionDTO;
import pl.wat.wcy.server.dto.OpinionRequest;
import pl.wat.wcy.server.service.HotelService;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
@AllArgsConstructor
public class HotelController {
    private final HotelService hotelService;

    @GetMapping()
    public ResponseEntity<List<HotelDTO>> getHotels(@RequestParam(defaultValue = "", required = false) String city) {
        return hotelService.getHotels(city);
    }

    @PostMapping()
    public ResponseEntity<HotelDTO> addHotel(@RequestBody HotelDTO hotelDTO) {
        return hotelService.addHotel(hotelDTO);
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDTO> updateHotel(@PathVariable Long hotelId, @RequestBody HotelDTO hotelDTO) {
        return hotelService.updateHotel(hotelId, hotelDTO);
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<Long> deleteHotel(@PathVariable Long hotelId) {
        return hotelService.deleteHotel(hotelId);
    }

    @PostMapping("/{hotelId}/opinions")
    public ResponseEntity<OpinionDTO> addOpinion(@PathVariable Long hotelId, @RequestBody OpinionRequest opinionRequest) {
        return hotelService.addOpinion(hotelId, opinionRequest);
    }
}
