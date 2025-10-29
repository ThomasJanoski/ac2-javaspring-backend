package com.facens.ac2.Repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.facens.ac2.Models.Funcionario;
import com.facens.ac2.Models.Projeto;

@Repository
public interface FuncionarioRepository extends JpaRepository<Funcionario, Integer> {

    @Query("SELECT f FROM Funcionario f " +
            "LEFT JOIN FETCH f.setor " +
            "LEFT JOIN FETCH f.projetos " +
            "WHERE f.id = :id")
    Optional<Funcionario> findByIdWithProjetosAndSetor(@Param("id") Integer id);

    @Query("SELECT f FROM Funcionario f " +
            "LEFT JOIN FETCH f.setor " +
            "LEFT JOIN FETCH f.projetos")
    List<Funcionario> findAllWithProjetosAndSetor();

    @Query("SELECT f.projetos FROM Funcionario f WHERE f.id = :id")
    List<Projeto> findProjetosByFuncionarioId(@Param("id") Integer id);

    @Query("SELECT f FROM Funcionario f WHERE f.nome = :nome")
    Optional<Funcionario> findByNome(@Param("nome") String nome);
}
