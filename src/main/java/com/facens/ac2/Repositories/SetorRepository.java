package com.facens.ac2.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.facens.ac2.Models.Setor;

@Repository
public interface SetorRepository extends JpaRepository<Setor, Integer> {
    @Query("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios")
    List<Setor> findAllWithFuncionarios();

    @Query("SELECT s FROM Setor s WHERE s.nome = :nome")
    Optional<Setor> findByNome(@Param("nome") String nome);

    @Query("SELECT s FROM Setor s WHERE s.id = :id")
    @NonNull
    Optional<Setor> findById(@Param("id") @NonNull Integer id);

    @Query("SELECT s FROM Setor s LEFT JOIN FETCH s.funcionarios WHERE s.id = :id")
    Setor findByIdWithFuncionarios(@Param("id") Integer id);
}
