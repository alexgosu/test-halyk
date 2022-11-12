package kz.example.halykfinance.controller;

import io.swagger.v3.oas.annotations.Operation;
import kz.example.halykfinance.dto.Client;
import kz.example.halykfinance.dto.Message;
import kz.example.halykfinance.persistence.entity.ClientEntity;
import kz.example.halykfinance.service.ClientService;
import kz.example.halykfinance.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class ApiController {

    private final ClientService clientService;
    private final MessageService messageService;

    @Operation(summary = "Создать клиента")
    @PostMapping("/clients")
    public ResponseEntity<Void> createClient(@RequestBody @Valid Client client) {
        ClientEntity clientEntity = clientService.createClient(client);
        return ResponseEntity.created(URI.create("/clients/" + clientEntity.getId())).build();
    }

    @Operation(summary = "Инфо по клиенту")
    @GetMapping("/clients/{id}")
    public Client getClient(@PathVariable Long id) {
        return clientService.findClient(id);
    }

    @Operation(summary = "Отправить сообщение клиенту")
    @PostMapping("/messages")
    public ResponseEntity<Void> sendMessage(@RequestBody @Valid Message message) {
        messageService.addToQueue(message);
        return ResponseEntity.accepted().build();
    }
}
