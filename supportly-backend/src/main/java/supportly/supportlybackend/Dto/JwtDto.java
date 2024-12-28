package supportly.supportlybackend.Dto;

import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
public class JwtDto {

    private String token;
    private String type = "Bearer";
    private Long individualId;
    private String username;
    private String email;
    private List<String> roles;

    public JwtDto(String accessToken, Long individualId, String username, String email, List<String> roles) {
        this.token = accessToken;
        this.individualId = individualId;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

}
