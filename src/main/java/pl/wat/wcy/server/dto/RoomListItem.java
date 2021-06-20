package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RoomListItem {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private Integer capacity;
    private Set<String> features;
    private File attachment;
}
