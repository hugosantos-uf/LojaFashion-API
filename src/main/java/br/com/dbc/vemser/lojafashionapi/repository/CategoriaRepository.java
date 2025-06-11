package br.com.dbc.vemser.lojafashionapi.repository;

import br.com.dbc.vemser.lojafashionapi.entity.CategoriaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoriaRepository extends JpaRepository<CategoriaEntity, Integer> {
    Optional<CategoriaEntity> findByNome(String nome);
}
