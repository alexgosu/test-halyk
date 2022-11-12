package kz.example.halykfinance.service;

import kz.example.halykfinance.dto.Client;
import kz.example.halykfinance.persistence.entity.ClientEntity;

public interface ClientService {

    ClientEntity createClient(Client client);

    Client findClient(Long id);
}
