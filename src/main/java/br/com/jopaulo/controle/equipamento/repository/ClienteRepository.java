package br.com.jopaulo.controle.equipamento.repository;

import java.util.List;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.jopaulo.controle.equipamento.model.Cliente;
import br.com.jopaulo.controle.equipamento.model.Situacao;

@Repository
@Transactional
public interface ClienteRepository extends JpaRepository<Cliente, Long>{

	@Query("select c from Cliente c where lower(c.nome) like lower('%'||?1||'%')")
	List<Cliente> findByNameContainingIgnoreCase(String nome);
	
	@Query("select c from Cliente c where c.situacao = ?1")
	List<Cliente> findBySituacao(String situacao);
	
	@Query("select c from Cliente c where c.nome like %?1% and c.situacao = ?2")
	List<Cliente> findByNameSituacaoContainingIgnoreCase(String nome, String situacao);
	
	default Page<Cliente> findClienteByNamePage(String nome, Pageable pageable) {
		
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Cliente> example = Example.of(cliente, exampleMatcher);
		
		Page<Cliente> clientes = findAll(example, pageable);
		
		return clientes;
	}
	
	default Page<Cliente> findClienteBySituacaoPage(String nome, String situacao, Pageable pageable) {
		
		Cliente cliente = new Cliente();
		cliente.setNome(nome);
		cliente.setSituacao(Situacao.valueOf(situacao));
		
		ExampleMatcher exampleMatcher = ExampleMatcher.matchingAny()
				.withMatcher("nome", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
				.withMatcher("situacao", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
		
		Example<Cliente> example = Example.of(cliente, exampleMatcher);
		
		Page<Cliente> clientes = findAll(example, pageable);
		
		return clientes;
	}
}
