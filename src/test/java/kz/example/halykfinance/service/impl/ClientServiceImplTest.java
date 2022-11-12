package kz.example.halykfinance.service.impl;

import kz.example.halykfinance.dto.Client;
import kz.example.halykfinance.exception.ClientException;
import kz.example.halykfinance.exception.ResourceNotFoundException;
import kz.example.halykfinance.persistence.dao.ClientDao;
import kz.example.halykfinance.persistence.entity.ClientEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private ClientDao clientDao;

    @InjectMocks
    private ClientServiceImpl clientService;

    @Captor
    private ArgumentCaptor<ClientEntity> clientEntityAC;

    @Test
    void createClient_allOk() {
        // given
        var client = getSampleClient();
        when(clientDao.save(any()))
                .thenReturn(new ClientEntity());

        // when
        clientService.createClient(client);

        // then
        verify(clientDao).save(clientEntityAC.capture());
        var actualEntity = clientEntityAC.getValue();
        assertEquals(client.getLogin(), actualEntity.getLogin());
        assertEquals(client.getFirstName(), actualEntity.getFirstName());
        assertEquals(client.getLastName(), actualEntity.getLastName());
        assertEquals(client.getPatronymic(), actualEntity.getPatronymic());
        assertEquals(client.getBirthDate(), actualEntity.getBirthDate());
    }

    @Test
    void createClient_duplicateLoginError() {
        // given
        var client = getSampleClient();
        when(clientDao.save(any()))
                .thenThrow(new DataIntegrityViolationException("No matter"));

        // when + then
        assertThrows(ClientException.class, () -> clientService.createClient(client));
    }

    @Test
    void findClient_allOk() {
        // given
        var clientEntity = getSampleClientEntity();
        when(clientDao.findById(any()))
                .thenReturn(Optional.of(clientEntity));

        // when
        var client = clientService.findClient(10L);

        // then
        verify(clientDao).findById(10L);
        assertEquals(clientEntity.getLogin(), client.getLogin());
        assertEquals(clientEntity.getFirstName(), client.getFirstName());
        assertEquals(clientEntity.getLastName(), client.getLastName());
        assertEquals(clientEntity.getPatronymic(), client.getPatronymic());
        assertEquals(clientEntity.getBirthDate(), client.getBirthDate());
    }

    @Test
    void findClient_notFoundError() {
        // given
        when(clientDao.findById(any()))
                .thenReturn(Optional.empty());

        // when + then
        assertThrows(ResourceNotFoundException.class, () -> clientService.findClient(10L));
    }

    private Client getSampleClient() {
        var client = new Client();
        client.setLogin("login");
        client.setFirstName("John");
        client.setLastName("Smith");
        client.setPatronymic("");
        client.setBirthDate(LocalDate.of(1990, 1, 15));
        return client;
    }

    private ClientEntity getSampleClientEntity() {
        var client = new ClientEntity();
        client.setLogin("login");
        client.setFirstName("John");
        client.setLastName("Smith");
        client.setPatronymic("");
        client.setBirthDate(LocalDate.of(1990, 1, 15));
        return client;
    }
}