package com.facens.ac2.Controllers;

import com.facens.ac2.DTOs.DadosFuncionarioDTO;
import com.facens.ac2.DTOs.DadosProjetoDTO;
import com.facens.ac2.Services.FuncionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@CrossOrigin("*")
public class FuncionarioController {

    @Autowired
    private FuncionarioService funcionarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosFuncionarioDTO create(@RequestBody DadosFuncionarioDTO funcionarioDTO) {
        funcionarioService.create(funcionarioDTO);
        return funcionarioDTO;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosFuncionarioDTO update(@PathVariable Integer id, @RequestBody DadosFuncionarioDTO funcionarioDTO) {
        funcionarioService.update(id, funcionarioDTO);
        return funcionarioDTO;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        funcionarioService.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosFuncionarioDTO getById(@PathVariable Integer id) {
        return funcionarioService.getById(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DadosFuncionarioDTO> getAll() {
        return funcionarioService.getAll();
    }

    @PostMapping("/{funcionarioId}/projetos/{projetoId}")
    @ResponseStatus(HttpStatus.OK)
    public void addProjetoToFuncionario(@PathVariable Integer funcionarioId, @PathVariable Integer projetoId) {
        funcionarioService.addProjetoToFuncionario(funcionarioId, projetoId);
    }

    @GetMapping("/{id}/projetos")
    @ResponseStatus(HttpStatus.OK)
    public List<DadosProjetoDTO> getProjetosByFuncionarioId(@PathVariable Integer id) {
        return funcionarioService.getProjetosByFuncionarioId(id);
    }
}