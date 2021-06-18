package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomRequest {
    private Long hotelId;
    private String name;
    private String description;
    private Double price;
    private Integer capacity;
    private Set<String> features;
    private List<File> attachments;
}
