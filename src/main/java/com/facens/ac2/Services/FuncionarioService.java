package com.facens.ac2.Services;

import com.facens.ac2.DTOs.DadosFuncionarioDTO;
import com.facens.ac2.DTOs.DadosProjetoDTO;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface FuncionarioService {

    @Transactional
    void create(DadosFuncionarioDTO funcionarioDTO);

    @Transactional
    void update(Integer id, DadosFuncionarioDTO updatedFuncionarioDTO);

    @Transactional
    void delete(Integer id);

    DadosFuncionarioDTO getById(Integer id);

    List<DadosFuncionarioDTO> getAll();

    @Transactional
    void addProjetoToFuncionario(Integer funcionarioId, Integer projetoId);

    List<DadosProjetoDTO> getProjetosByFuncionarioId(Integer id);
}