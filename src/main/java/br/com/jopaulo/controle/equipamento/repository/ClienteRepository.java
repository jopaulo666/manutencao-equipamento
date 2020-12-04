package br.com.jopaulo.controle.equipamento.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.jopaulo.controle.equipamento.model.Cliente;

@Repository
@Transactional
public interface ClienteRepository extends CrudRepository<Cliente, Long>{

	@Query("select c from Cliente c where c.nome like %?1%")
	List<Cliente> findByNomeContainingIgnoreCase(String nome);
}
