package contacloud.dpgatewayservice.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class TokenDto {
    private String token;
    private String rol; // ← este es esencial
}
