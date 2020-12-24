package com.nelioalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domains.Categoria;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria find(Integer id) {
		return repo.findById(id).orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName() ));
	}
	
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	public Categoria save( Categoria cat ) {
		return repo.save( cat );
	}
	
	public Categoria update( Categoria cat ) {
		find( cat.getId() );
		return repo.save( cat );
	}
	
	public void delete( Integer id ) {
		find( id );
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException( "Não é possível excluir uma categoria que possui produtos." );
		}
	}
}
