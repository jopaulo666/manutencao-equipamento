package br.com.jopaulo.controle.equipamento.repository;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.jopaulo.controle.equipamento.model.Usuario;

@Repository
@Transactional
public interface UsuarioRepository extends CrudRepository<Usuario, Long>{

	@Query("select u from Usuario u where u.login = ?1")
	Usuario findUserByLogin(String login);
}
