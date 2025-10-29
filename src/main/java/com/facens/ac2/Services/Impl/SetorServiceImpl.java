package com.facens.ac2.Services.Impl;

import com.facens.ac2.DTOs.DadosFuncionarioDTO;
import com.facens.ac2.DTOs.DadosSetorDTO;
import com.facens.ac2.DTOs.RegraNegocioException;
import com.facens.ac2.Models.Setor;
import com.facens.ac2.Repositories.SetorRepository;
import com.facens.ac2.Services.SetorService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetorServiceImpl implements SetorService {

    @Autowired
    private SetorRepository setorRepository;

    @Override
    @Transactional
    public void create(DadosSetorDTO setorDTO) {
        validateNome(setorDTO.getNome());

        if (setorRepository.findByNome(setorDTO.getNome()).isPresent()) {
            throw new RegraNegocioException("Setor com esse nome já existe.");
        }

        Setor setor = new Setor();
        setor.setNome(setorDTO.getNome());
        setorRepository.save(setor);
    }

    @Override
    @Transactional
    public void update(Integer id, DadosSetorDTO updatedSetorDTO) {
        Setor existing = getById(id);
        if (updatedSetorDTO.getNome() != null) {
            validateNome(updatedSetorDTO.getNome());
            if (setorRepository.findByNome(updatedSetorDTO.getNome()).isPresent() &&
                    !setorRepository.findByNome(updatedSetorDTO.getNome()).get().getId().equals(id)) {
                throw new RegraNegocioException("Setor com esse nome já existe.");
            }
            existing.setNome(updatedSetorDTO.getNome());
        }
        setorRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Setor setor = getById(id);
        if (!setor.getFuncionarios().isEmpty()) {
            throw new RegraNegocioException("Não pode deletar setor com funcionários vinculados.");
        }
        setorRepository.delete(setor);
    }

    @Override
    public DadosSetorDTO getByIdWithFuncionarios(Integer id) {
        Setor setor = setorRepository.findByIdWithFuncionarios(id);
        if (setor == null) {
            throw new EntityNotFoundException("Setor não encontrado com ID: " + id);
        }
        return mapToDadosSetorDTO(setor);
    }

    @Override
    public List<DadosSetorDTO> getAllWithFuncionarios() {
        return setorRepository.findAllWithFuncionarios().stream()
                .map(this::mapToDadosSetorDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DadosSetorDTO> getAll() {
        return setorRepository.findAll().stream()
                .map(this::mapToDadosSetorDTO)
                .collect(Collectors.toList());
    }

    private DadosSetorDTO mapToDadosSetorDTO(Setor setor) {
        List<DadosFuncionarioDTO> funcDTOs = setor.getFuncionarios().stream()
                .map(f -> DadosFuncionarioDTO.builder()
                        .id(f.getId())
                        .nome(f.getNome())
                        .setor(DadosSetorDTO.builder()
                                .id(setor.getId())
                                .nome(setor.getNome())
                                .build())
                        .build())
                .collect(Collectors.toList());
        return DadosSetorDTO.builder()
                .id(setor.getId())
                .nome(setor.getNome())
                .funcionarios(funcDTOs)
                .build();
    }

    private void validateNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("Nome do setor não pode ser vazio.");
        }
    }

    private Setor getById(Integer id) {
        return setorRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Setor não encontrado com ID: " + id));
    }
}