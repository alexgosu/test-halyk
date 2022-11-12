package kz.example.halykfinance.mapper;

import kz.example.halykfinance.dto.Client;
import kz.example.halykfinance.persistence.entity.ClientEntity;
import org.mapstruct.Mapper;

@Mapper
public interface ClientMapper {
    ClientEntity toEntity(Client client);

    Client toDto(ClientEntity clientEntity);
}
