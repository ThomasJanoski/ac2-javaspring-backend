package com.facens.ac2.DTOs;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class DadosFuncionarioDTO {
    private Integer id;
    private String nome;

    private DadosSetorDTO setor;

    private List<DadosProjetoDTO> projetos;
}
