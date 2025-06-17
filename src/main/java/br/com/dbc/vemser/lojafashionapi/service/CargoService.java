package br.com.dbc.vemser.lojafashionapi.service;

import br.com.dbc.vemser.lojafashionapi.entity.CargoEntity;
import br.com.dbc.vemser.lojafashionapi.repository.CargoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CargoService {
    private final CargoRepository cargoRepository;

    public CargoEntity findByNome(String nome) {
        return cargoRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Cargo não encontrado: " + nome));
    }
}