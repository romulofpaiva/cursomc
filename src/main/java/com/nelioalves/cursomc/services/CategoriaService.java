package com.nelioalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domains.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repository;

	public Categoria find(Integer id) {
		return repository.findById(id).orElseThrow(() -> new ObjectNotFoundException( 
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName() ));
	}
	
	public List<Categoria> findAll() {
		return repository.findAll();
	}
	
	public Page<Categoria> findPage( Integer page, Integer linesPerPage, String orderBy, String direction ) {
		return repository.findAll( PageRequest.of( page, linesPerPage, Direction.valueOf( direction ), orderBy ) );
	}
	
	public Categoria insert( Categoria cat ) {
		return repository.save( cat );
	}
	
	public Categoria update( Categoria cat ) {
		find( cat.getId() );
		return repository.save( cat );
	}
	
	public void delete( Integer id ) {
		find( id );
		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException( "Não é possível excluir uma categoria que possui produtos." );
		}
	}
	
	public Categoria fromDTO( CategoriaDTO dto ) {
		return new Categoria( dto.getId(), dto.getNome() );
	}
}
