package com.facens.ac2.Services.Impl;

import com.facens.ac2.DTOs.*;
import com.facens.ac2.Models.Funcionario;
import com.facens.ac2.Models.Projeto;
import com.facens.ac2.Models.Setor;
import com.facens.ac2.Repositories.FuncionarioRepository;
import com.facens.ac2.Repositories.ProjetoRepository;
import com.facens.ac2.Repositories.SetorRepository;
import com.facens.ac2.Services.FuncionarioService;
import jakarta.persistence.EntityNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FuncionarioServiceImpl implements FuncionarioService {

    @Autowired
    private FuncionarioRepository funcionarioRepository;

    @Autowired
    private SetorRepository setorRepository;

    @Autowired
    private ProjetoRepository projetoRepository;

    @Override
    @Transactional
    public void create(DadosFuncionarioDTO funcionarioDTO) {
        validateNome(funcionarioDTO.getNome());

        Setor setor = setorRepository.findById(funcionarioDTO.getSetor().getId())
                .orElseThrow(() -> new RegraNegocioException(
                        "Setor não encontrado com ID: " + funcionarioDTO.getSetor().getId()));

        if (funcionarioRepository.findByNome(funcionarioDTO.getNome()).isPresent()) {
            throw new RegraNegocioException("Funcionário com esse nome já existe.");
        }

        Funcionario funcionario = new Funcionario();
        funcionario.setNome(funcionarioDTO.getNome());
        funcionario.setSetor(setor);

        List<Projeto> projetos = fetchProjetos(funcionarioDTO.getProjetos());
        funcionario.setProjetos(projetos);

        for (Projeto p : projetos) {
            p.getFuncionarios().add(funcionario);
        }

        funcionarioRepository.save(funcionario);
    }

    @Override
    @Transactional
    public void update(Integer id, DadosFuncionarioDTO updatedFuncionarioDTO) {
        Funcionario existing = funcionarioRepository.findByIdWithProjetosAndSetor(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com ID: " + id));

        if (updatedFuncionarioDTO.getNome() != null) {
            validateNome(updatedFuncionarioDTO.getNome());
            existing.setNome(updatedFuncionarioDTO.getNome());
        }

        if (updatedFuncionarioDTO.getSetor() != null && updatedFuncionarioDTO.getSetor().getId() != null) {
            Setor setor = setorRepository.findById(updatedFuncionarioDTO.getSetor().getId())
                    .orElseThrow(() -> new RegraNegocioException(
                            "Setor não encontrado com ID: " + updatedFuncionarioDTO.getSetor().getId()));
            existing.setSetor(setor);
        }

        if (updatedFuncionarioDTO.getProjetos() != null) {
            List<Projeto> projetos = fetchProjetos(updatedFuncionarioDTO.getProjetos());
            existing.getProjetos().clear();
            existing.getProjetos().addAll(projetos);
        }

        funcionarioRepository.save(existing);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Funcionario funcionario = funcionarioRepository.findByIdWithProjetosAndSetor(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com ID: " + id));
        if (!funcionario.getProjetos().isEmpty()) {
            throw new RegraNegocioException("Não pode deletar funcionário vinculado a projetos.");
        }
        funcionarioRepository.delete(funcionario);
    }

    @Override
    public DadosFuncionarioDTO getById(Integer id) {
        Funcionario funcionario = funcionarioRepository.findByIdWithProjetosAndSetor(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com ID: " + id));
        return mapToDadosFuncionarioDTO(funcionario);
    }

    @Override
    public List<DadosFuncionarioDTO> getAll() {
        return funcionarioRepository.findAllWithProjetosAndSetor().stream()
                .map(this::mapToDadosFuncionarioDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void addProjetoToFuncionario(Integer funcionarioId, Integer projetoId) {
        Funcionario funcionario = getByIdEntity(funcionarioId);
        Projeto projeto = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto não encontrado."));
        if (funcionario.getProjetos().contains(projeto)) {
            throw new RegraNegocioException("Projeto já vinculado ao funcionário.");
        }
        funcionario.getProjetos().add(projeto);
        funcionarioRepository.save(funcionario);
    }

    @Override
    public List<DadosProjetoDTO> getProjetosByFuncionarioId(Integer id) {
        List<Projeto> projetos = funcionarioRepository.findProjetosByFuncionarioId(id);
        return projetos.stream()
                .map(this::mapToDadosProjetoDTO)
                .collect(Collectors.toList());
    }

    private List<Projeto> fetchProjetos(List<DadosProjetoDTO> projetoDTOs) {
        if (projetoDTOs == null || projetoDTOs.isEmpty())
            return List.of();
        return projetoDTOs.stream()
                .map(dto -> projetoRepository.findById(dto.getId())
                        .orElseThrow(() -> new RegraNegocioException("Projeto não encontrado com ID: " + dto.getId())))
                .collect(Collectors.toList());
    }

    private DadosFuncionarioDTO mapToDadosFuncionarioDTO(Funcionario f) {
        DadosSetorDTO setorDTO = f.getSetor() != null
                ? DadosSetorDTO.builder().id(f.getSetor().getId()).nome(f.getSetor().getNome()).build()
                : null;

        List<DadosProjetoDTO> projetosDTO = f.getProjetos().stream()
                .map(p -> DadosProjetoDTO.builder()
                        .id(p.getId())
                        .descricao(p.getDescricao())
                        .dataInicio(p.getDataInicio())
                        .dataFim(p.getDataFim())
                        .build())
                .collect(Collectors.toList());

        return DadosFuncionarioDTO.builder()
                .id(f.getId())
                .nome(f.getNome())
                .setor(setorDTO)
                .projetos(projetosDTO)
                .build();
    }

    private DadosProjetoDTO mapToDadosProjetoDTO(Projeto p) {
        List<DadosFuncionarioDTO> funcDTOs = p.getFuncionarios().stream()
                .map(f -> DadosFuncionarioDTO.builder()
                        .id(f.getId())
                        .nome(f.getNome())
                        .setor(f.getSetor() != null
                                ? DadosSetorDTO.builder().id(f.getSetor().getId()).nome(f.getSetor().getNome()).build()
                                : null)
                        .build())
                .collect(Collectors.toList());

        return DadosProjetoDTO.builder()
                .id(p.getId())
                .descricao(p.getDescricao())
                .dataInicio(p.getDataInicio())
                .dataFim(p.getDataFim())
                .funcionarios(funcDTOs)
                .build();
    }

    private void validateNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("Nome do funcionário não pode ser vazio.");
        }
    }

    private Funcionario getByIdEntity(Integer id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Funcionário não encontrado com ID: " + id));
    }
}