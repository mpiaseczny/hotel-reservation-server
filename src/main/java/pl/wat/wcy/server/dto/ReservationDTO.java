package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDTO {
    private Long id;
    private String name;
    private String description;
    private Integer capacity;
    private Set<String> features;
    private List<File> attachments;
    private Long dateFrom;
    private Long dateTo;
    private Double totalPrice;
    private List<OpinionDTO> opinions;
    private String hotelName;
    private Integer rating;
    private String city;
    private String street;
    private String phoneNumber;
    private String email;
}
