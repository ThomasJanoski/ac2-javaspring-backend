package com.facens.ac2.Controllers;

import com.facens.ac2.DTOs.DadosSetorDTO;
import com.facens.ac2.Services.SetorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/setores")
public class SetorController {

    @Autowired
    private SetorService setorService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DadosSetorDTO create(@RequestBody DadosSetorDTO setorDTO) {
        setorService.create(setorDTO);
        return setorDTO;
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosSetorDTO update(@PathVariable Integer id, @RequestBody DadosSetorDTO setorDTO) {
        setorService.update(id, setorDTO);
        return setorDTO;
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        setorService.delete(id);
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public DadosSetorDTO getByIdWithFuncionarios(@PathVariable Integer id) {
        return setorService.getByIdWithFuncionarios(id);
    }

    @GetMapping("/todos")
    @ResponseStatus(HttpStatus.OK)
    public List<DadosSetorDTO> getAllWithFuncionarios() {
        return setorService.getAllWithFuncionarios();
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DadosSetorDTO> getAll() {
        return setorService.getAll();
    }
}