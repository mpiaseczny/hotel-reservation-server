package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {
    private Long roomId;
    private Long dateFrom;
    private Long dateTo;
    private Double price;
}
