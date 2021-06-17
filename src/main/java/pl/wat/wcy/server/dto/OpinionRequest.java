package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OpinionRequest {
    private Double rate;
    private String comment;
}
