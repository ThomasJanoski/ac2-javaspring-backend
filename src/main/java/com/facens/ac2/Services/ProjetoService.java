package com.facens.ac2.Services;

import com.facens.ac2.DTOs.DadosProjetoDTO;

import java.time.LocalDate;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface ProjetoService {

    @Transactional
    void create(DadosProjetoDTO projetoDTO);

    @Transactional
    void update(Integer id, DadosProjetoDTO updatedProjetoDTO);

    @Transactional
    void delete(Integer id);

    DadosProjetoDTO getByIdWithFuncionarios(Integer id);

    List<DadosProjetoDTO> getByDataInicioBetween(LocalDate start, LocalDate end);

    List<DadosProjetoDTO> getAll();

    @Transactional
    void addFuncionarioToProjeto(Integer projetoId, Integer funcionarioId);
}