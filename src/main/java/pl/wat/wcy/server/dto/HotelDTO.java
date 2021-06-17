package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HotelDTO {
    private Long id;
    private String name;
    private Integer rating;
    private String city;
    private String street;
    private String phoneNumber;
    private String email;
    private File attachment;
}
