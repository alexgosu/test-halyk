package kz.example.halykfinance.persistence.dao;

import kz.example.halykfinance.persistence.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageDao extends JpaRepository<MessageEntity, Long> {
}
