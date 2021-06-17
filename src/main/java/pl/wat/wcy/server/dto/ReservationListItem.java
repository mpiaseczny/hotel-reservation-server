package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationListItem {
    private Long id;
    private String roomName;
    private Long dateFrom;
    private Long dateTo;
    private Double price;
    private File attachment;
}
