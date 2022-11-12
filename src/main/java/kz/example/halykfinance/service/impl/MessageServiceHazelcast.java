package kz.example.halykfinance.service.impl;

import com.hazelcast.core.HazelcastInstance;
import kz.example.halykfinance.dto.Message;
import kz.example.halykfinance.persistence.dao.ClientDao;
import kz.example.halykfinance.persistence.dao.MessageDao;
import kz.example.halykfinance.persistence.entity.ClientEntity;
import kz.example.halykfinance.persistence.entity.MessageEntity;
import kz.example.halykfinance.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Lock;

@Service
@Slf4j
@RequiredArgsConstructor
public class MessageServiceHazelcast implements MessageService {

    private final ClientDao clientDao;
    private final MessageDao messageDao;
    private final HazelcastInstance hazelcastInstanceClient;
    private BlockingQueue<Message> queue;
    private Lock lock;

    @PostConstruct
    private void init() {
        queue = hazelcastInstanceClient.getQueue("messagesQueue");
        lock = hazelcastInstanceClient.getCPSubsystem().getLock("messagesLock");
    }


    @Override
    public boolean addToQueue(Message message) {
        log.info("Add new message {}", message.getBody());
        return queue.add(message);
    }

    @Override
    public void processAllMessages() {
        if (lock.tryLock()) {
            try {
                Message message;
                while ((message = getMessage()) != null) {
                    log.info("Processing message body={}", message.getBody());
                    Optional<ClientEntity> client = clientDao.findByLogin(message.getLogin());
                    if (client.isPresent()) {
                        var messageEntity = new MessageEntity();
                        messageEntity.setBody(message.getBody());
                        messageEntity.setClient(client.get());
                        messageDao.save(messageEntity);
                    } else {
                        log.warn("Client not found, message dropped, login={}", message.getLogin());
                    }
                }
            } finally {
                lock.unlock();
            }
        } else {
            log.info("Thread is locked by other process");
        }
    }

    private Message getMessage() {
        return queue.poll();
    }
}
