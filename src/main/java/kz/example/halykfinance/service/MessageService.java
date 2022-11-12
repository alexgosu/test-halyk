package kz.example.halykfinance.service;

import kz.example.halykfinance.dto.Message;

public interface MessageService {
    boolean addToQueue(Message message);
    void processAllMessages();
}
