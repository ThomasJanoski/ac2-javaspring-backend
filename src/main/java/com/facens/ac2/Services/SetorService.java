package com.facens.ac2.Services;

import com.facens.ac2.DTOs.DadosSetorDTO;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface SetorService {

    @Transactional
    void create(DadosSetorDTO setorDTO);

    @Transactional
    void update(Integer id, DadosSetorDTO updatedSetorDTO);

    @Transactional
    void delete(Integer id);

    DadosSetorDTO getByIdWithFuncionarios(Integer id);

    List<DadosSetorDTO> getAllWithFuncionarios();

    List<DadosSetorDTO> getAll();
}