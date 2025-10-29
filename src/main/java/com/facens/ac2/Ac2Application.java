package com.facens.ac2;

import java.time.LocalDate;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.facens.ac2.Models.Funcionario;
import com.facens.ac2.Models.Projeto;
import com.facens.ac2.Models.Setor;
import com.facens.ac2.Repositories.FuncionarioRepository;
import com.facens.ac2.Repositories.ProjetoRepository;
import com.facens.ac2.Repositories.SetorRepository;

@SpringBootApplication
public class Ac2Application {
	public static void main(String[] args) {
		SpringApplication.run(Ac2Application.class, args);
	}

	@Autowired
	private SetorRepository setorRepository;

	@Autowired
	private FuncionarioRepository funcionarioRepository;

	@Autowired
	private ProjetoRepository projetoRepository;

	@Bean
	public CommandLineRunner initData() {
		return args -> {
			// Criar Setores
			Setor setor1 = Setor.builder()
					.nome("Cibersegurança e privacidade")
					.build();
			Setor setor2 = Setor.builder()
					.nome("Desenvolvimento de software e aplicações")
					.build();
			Setor setor3 = Setor.builder()
					.nome("Marketing e mídia")
					.build();

			setorRepository.save(setor1);
			setorRepository.save(setor2);
			setorRepository.save(setor3);

			// Criar Projetos
			Projeto projeto1 = Projeto.builder()
					.descricao(
							"SolarLink é um serviço desenvolvido que visa fazer a mediação entre fornecedores de energia solar e clientes interessados em adquirir o produto.")
					.dataInicio(LocalDate.of(2025, 2, 1))
					.dataFim(LocalDate.of(2025, 6, 30))
					.build();

			Projeto projeto2 = Projeto.builder()
					.descricao(
							"AcquaMonitor serve como um app que monitora o consumo de água em uma residência com finalidade de demonstrar padrões e ajudar na economia familiar.")
					.dataInicio(LocalDate.of(2024, 8, 1))
					.dataFim(LocalDate.of(2024, 12, 20))
					.build();

			Projeto projeto3 = Projeto.builder()
					.descricao(
							"O site Vias e Vozes permite a participação da comunidade na criação de tickets que são postados no portal com pedidos de melhorias urbanísticas estruturais, entre outros problemas. Funciona como o Reclame Aqui de um município para chamar a atenção do poder público para reclamações dos munícipes.")
					.dataInicio(LocalDate.of(2024, 2, 1))
					.dataFim(LocalDate.of(2024, 6, 30))
					.build();

			projetoRepository.save(projeto1);
			projetoRepository.save(projeto2);
			projetoRepository.save(projeto3);

			// Criar Funcionários
			Funcionario func1 = Funcionario.builder()
					.nome("Wesley")
					.setor(setor1)
					.projetos(Arrays.asList(projeto1))
					.build();
			Funcionario func2 = Funcionario.builder()
					.nome("Lucas")
					.setor(setor1)
					.projetos(Arrays.asList(projeto1))
					.build();
			Funcionario func3 = Funcionario.builder()
					.nome("Thomas")
					.setor(setor2)
					.projetos(Arrays.asList(projeto2))
					.build();
			Funcionario func4 = Funcionario.builder()
					.nome("Guilherme")
					.setor(setor2)
					.projetos(Arrays.asList(projeto2))
					.build();
			Funcionario func5 = Funcionario.builder()
					.nome("Milena")
					.setor(setor3)
					.projetos(Arrays.asList(projeto3))
					.build();
			Funcionario func6 = Funcionario.builder()
					.nome("Gabriel")
					.setor(setor3)
					.projetos(Arrays.asList(projeto3))
					.build();

			funcionarioRepository.save(func1);
			funcionarioRepository.save(func2);
			funcionarioRepository.save(func3);
			funcionarioRepository.save(func4);
			funcionarioRepository.save(func5);
			funcionarioRepository.save(func6);
		};
	}
}
