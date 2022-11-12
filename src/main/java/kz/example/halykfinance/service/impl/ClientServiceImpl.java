package kz.example.halykfinance.service.impl;

import kz.example.halykfinance.dto.Client;
import kz.example.halykfinance.exception.ClientException;
import kz.example.halykfinance.exception.ResourceNotFoundException;
import kz.example.halykfinance.mapper.ClientMapper;
import kz.example.halykfinance.persistence.dao.ClientDao;
import kz.example.halykfinance.persistence.entity.ClientEntity;
import kz.example.halykfinance.service.ClientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;
    private final ClientMapper clientMapper = Mappers.getMapper(ClientMapper.class);

    @Override
    public ClientEntity createClient(Client client) {
        try {
            return clientDao.save(clientMapper.toEntity(client));
        } catch (DataIntegrityViolationException e) {
            log.error("Client already exists", e);
            throw new ClientException("Client already exists");
        }
    }

    @Override
    public Client findClient(Long id) {
        return clientDao
                .findById(id)
                .map(clientMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Client not found"));
    }
}
