package kma.topic8.springrestsample.dto;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data(staticConstructor = "of")
public class LoginResponseDto {

    private final String login;
    private final String successMessage;

}
