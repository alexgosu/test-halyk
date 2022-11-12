package kz.example.halykfinance.persistence.dao;

import kz.example.halykfinance.persistence.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClientDao extends JpaRepository<ClientEntity, Long> {

    Optional<ClientEntity> findByLogin(String login);
}
