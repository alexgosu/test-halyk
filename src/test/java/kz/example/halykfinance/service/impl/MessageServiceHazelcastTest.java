package kz.example.halykfinance.service.impl;

import kz.example.halykfinance.dto.Message;
import kz.example.halykfinance.persistence.dao.ClientDao;
import kz.example.halykfinance.persistence.dao.MessageDao;
import kz.example.halykfinance.persistence.entity.ClientEntity;
import kz.example.halykfinance.persistence.entity.MessageEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MessageServiceHazelcastTest {

    @Mock
    private ClientDao clientDao;

    @Mock
    private MessageDao messageDao;

    @InjectMocks
    private MessageServiceHazelcast messageService;

    @Captor
    ArgumentCaptor<MessageEntity> messageEntityAC;

    @Test
    void addToQueue_allOk() {
        // given
        var queue = new LinkedBlockingQueue<Message>();
        ReflectionTestUtils.setField(messageService, "queue", queue);

        // when
        var response = messageService.addToQueue(new Message("theLogin", "some text"));

        // then
        assertTrue(response);
        assertNotNull(queue);
        assertEquals(1, queue.size());
    }

    @Test
    void processAllMessages_allOk() {
        // given
        var queue = new LinkedBlockingQueue<Message>();
        queue.add(new Message("login_1", "message_1"));
        queue.add(new Message("login_2", "message_2"));
        queue.add(new Message("login_3", "message_3"));
        ReflectionTestUtils.setField(messageService, "queue", queue);
        ReflectionTestUtils.setField(messageService, "lock", new ReentrantLock());

        when(clientDao.findByLogin("login_1"))
                .thenReturn(Optional.of(getSampleClientEntity(1L)));
        when(clientDao.findByLogin("login_2"))
                .thenReturn(Optional.of(getSampleClientEntity(2L)));
        when(clientDao.findByLogin("login_3"))
                .thenReturn(Optional.empty());
        when(messageDao.save(any()))
                .thenReturn(null);

        // when
        messageService.processAllMessages();

        // then
        verify(clientDao, times(3)).findByLogin(any());
        verify(messageDao, times(2)).save(messageEntityAC.capture());

        List<MessageEntity> allValues = messageEntityAC.getAllValues();
        assertEquals(1L, allValues.get(0).getClient().getId());
        assertEquals("message_1", allValues.get(0).getBody());
        assertEquals(2L, allValues.get(1).getClient().getId());
        assertEquals("message_2", allValues.get(1).getBody());

    }

    private ClientEntity getSampleClientEntity(Long id) {
        var client = new ClientEntity();
        client.setId(id);
        return client;
    }
}