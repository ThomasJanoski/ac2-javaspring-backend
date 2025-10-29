package com.facens.ac2.Services.Impl;

import com.facens.ac2.DTOs.DadosFuncionarioDTO;
import com.facens.ac2.DTOs.DadosProjetoDTO;
import com.facens.ac2.DTOs.DadosSetorDTO;
import com.facens.ac2.DTOs.RegraNegocioException;
import com.facens.ac2.Models.Funcionario;
import com.facens.ac2.Models.Projeto;
import com.facens.ac2.Repositories.FuncionarioRepository;
import com.facens.ac2.Repositories.ProjetoRepository;
import com.facens.ac2.Services.ProjetoService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProjetoServiceImpl implements ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Override
    @Transactional
    public void create(DadosProjetoDTO projetoDTO) {
        validateDescricao(projetoDTO.getDescricao());
        validateDatas(projetoDTO.getDataInicio(), projetoDTO.getDataFim());

        Projeto projeto = new Projeto();
        projeto.setDescricao(projetoDTO.getDescricao());
        projeto.setDataInicio(projetoDTO.getDataInicio());
        projeto.setDataFim(projetoDTO.getDataFim());

        List<Funcionario> funcionarios = fetchFuncionarios(projetoDTO.getFuncionarios());
        if (!funcionarios.isEmpty()) {
            for (Funcionario f : funcionarios) {
                if (!f.getProjetos().contains(projeto)) {
                    f.getProjetos().add(projeto);
                }
            }
        }
        projeto.setFuncionarios(funcionarios);

        projetoRepository.save(projeto);
        funcionarioRepository.saveAll(funcionarios);
    }

    @Override
    @Transactional
    public void update(Integer id, DadosProjetoDTO updatedProjetoDTO) {
        Projeto existing = getById(id);
        if (updatedProjetoDTO.getDescricao() != null) {
            validateDescricao(updatedProjetoDTO.getDescricao());
            existing.setDescricao(updatedProjetoDTO.getDescricao());
        }
        if (updatedProjetoDTO.getDataInicio() != null && updatedProjetoDTO.getDataFim() != null) {
            validateDatas(updatedProjetoDTO.getDataInicio(), updatedProjetoDTO.getDataFim());
            existing.setDataInicio(updatedProjetoDTO.getDataInicio());
            existing.setDataFim(updatedProjetoDTO.getDataFim());
        }
        if (updatedProjetoDTO.getFuncionarios() != null) {
            List<Funcionario> novosFuncionarios = fetchFuncionarios(updatedProjetoDTO.getFuncionarios());

            List<Funcionario> antigosFuncionarios = new ArrayList<>(existing.getFuncionarios());

            existing.getFuncionarios().clear();
            existing.getFuncionarios().addAll(novosFuncionarios);

            for (Funcionario f : novosFuncionarios) {
                if (!f.getProjetos().contains(existing)) {
                    f.getProjetos().add(existing);
                }
            }

            for (Funcionario f : antigosFuncionarios) {
                if (!novosFuncionarios.contains(f)) {
                    f.getProjetos().remove(existing);
                }
            }
        }

        projetoRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Projeto projeto = getById(id);
        if (projeto.getFuncionarios() != null && !projeto.getFuncionarios().isEmpty()) {
            throw new RegraNegocioException("Não pode deletar projeto com funcionários vinculados.");
        }
        projetoRepository.delete(projeto);
    }

    @Override
    public DadosProjetoDTO getByIdWithFuncionarios(Integer id) {
        Projeto projeto = projetoRepository.findByIdWithFuncionarios(id);
        if (projeto == null) {
            throw new EntityNotFoundException("Projeto não encontrado com ID: " + id);
        }
        return mapToDadosProjetoDTO(projeto);
    }

    @Override
    public List<DadosProjetoDTO> getByDataInicioBetween(LocalDate start, LocalDate end) {
        return projetoRepository.findByDataInicioBetween(start, end).stream()
                .map(this::mapToDadosProjetoDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<DadosProjetoDTO> getAll() {
        return projetoRepository.findAll().stream()
                .map(this::mapToDadosProjetoDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addFuncionarioToProjeto(Integer projetoId, Integer funcionarioId) {
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));

        Funcionario funcionario = funcionarioRepository.findById(funcionarioId)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado."));

        if (!funcionario.getProjetos().contains(projeto)) {
            funcionario.getProjetos().add(projeto);
            funcionarioRepository.save(funcionario);
        }

        if (!projeto.getFuncionarios().contains(funcionario)) {
            projeto.getFuncionarios().add(funcionario);
        }
    }

    private List<Funcionario> fetchFuncionarios(List<DadosFuncionarioDTO> funcionarioDTOs) {
        if (funcionarioDTOs == null || funcionarioDTOs.isEmpty()) {
            return List.of();
        }
        return funcionarioDTOs.stream()
                .map(dto -> {
                    if (dto.getId() == null) {
                        throw new RegraNegocioException("ID do funcionário não pode ser nulo.");
                    }
                    return funcionarioRepository.findById(dto.getId())
                            .orElseThrow(() -> new RegraNegocioException(
                                    "Funcionário não encontrado com ID: " + dto.getId()));
                })
                .collect(Collectors.toList());
    }

    private DadosProjetoDTO mapToDadosProjetoDTO(Projeto projeto) {
        List<DadosFuncionarioDTO> funcDTOs = projeto.getFuncionarios().stream()
                .map(f -> DadosFuncionarioDTO.builder()
                        .id(f.getId())
                        .nome(f.getNome())
                        .setor(f.getSetor() != null ? DadosSetorDTO.builder()
                                .id(f.getSetor().getId())
                                .nome(f.getSetor().getNome())
                                .build() : null)
                        .build())
                .collect(Collectors.toList());
        return DadosProjetoDTO.builder()
                .id(projeto.getId())
                .descricao(projeto.getDescricao())
                .dataInicio(projeto.getDataInicio())
                .dataFim(projeto.getDataFim())
                .funcionarios(funcDTOs)
                .build();
    }

    private void validateDescricao(String descricao) {
        if (descricao == null || descricao.trim().isEmpty()) {
            throw new RegraNegocioException("Descrição do projeto não pode ser vazia.");
        }
    }

    private void validateDatas(LocalDate inicio, LocalDate fim) {
        if (inicio == null || fim == null) {
            throw new RegraNegocioException("Datas de início e fim são obrigatórias.");
        }
        if (inicio.isAfter(fim)) {
            throw new RegraNegocioException("Data de início deve ser antes da data de fim.");
        }
    }

    private Projeto getById(Integer id) {
        return projetoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado com ID: " + id));
    }
}