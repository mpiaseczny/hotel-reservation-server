package pl.wat.wcy.server.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.wat.wcy.server.dao.UserType;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthenticationResponse {
    private Long id;
    private String authenticationToken;
    private String name;
    private String email;
    private UserType type;
}
