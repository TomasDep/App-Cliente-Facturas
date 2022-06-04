package com.dev.springboot.app.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.dev.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends CrudRepository<Usuario, Long> {
	@Query("select u from Usuario u where u.username=?1")
	public Usuario findByUsuario(String username);
}
