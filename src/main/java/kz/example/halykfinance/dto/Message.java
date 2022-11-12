package kz.example.halykfinance.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class Message {

    @NotBlank
    private String login;
    @NotBlank
    private String body;
}
