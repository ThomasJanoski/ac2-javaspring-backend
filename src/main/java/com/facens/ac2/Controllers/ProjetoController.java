package com.facens.ac2.Controllers;

import com.facens.ac2.DTOs.DadosProjetoDTO;
import com.facens.ac2.Services.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosProjetoDTO create(@RequestBody DadosProjetoDTO projetoDTO) {
        projetoService.create(projetoDTO);
        return projetoDTO;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosProjetoDTO update(@PathVariable Integer id, @RequestBody DadosProjetoDTO projetoDTO) {
        projetoService.update(id, projetoDTO);
        return projetoDTO;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        projetoService.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosProjetoDTO getByIdWithFuncionarios(@PathVariable Integer id) {
        return projetoService.getByIdWithFuncionarios(id);
    }

    @GetMapping("/periodo")
    @ResponseStatus(HttpStatus.OK)
    public List<DadosProjetoDTO> getByDataInicioBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {
        return projetoService.getByDataInicioBetween(start, end);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DadosProjetoDTO> getAll() {
        return projetoService.getAll();
    }

    @PostMapping("/{projetoId}/funcionarios/{funcionarioId}")
    @ResponseStatus(HttpStatus.OK)
    public void addFuncionarioToProjeto(@PathVariable Integer projetoId, @PathVariable Integer funcionarioId) {
        projetoService.addFuncionarioToProjeto(projetoId, funcionarioId);
    }
}