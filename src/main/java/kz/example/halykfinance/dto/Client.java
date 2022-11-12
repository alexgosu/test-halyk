package kz.example.halykfinance.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@Getter
@Setter
public class Client {

    @NotBlank
    private String login;
    private String lastName;
    private String firstName;
    private String patronymic;
    private LocalDate birthDate;
}
