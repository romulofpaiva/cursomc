package com.nelioalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domains.Cliente;
import com.nelioalves.cursomc.repositories.ClienteRepository;
import com.nelioalves.cursomc.repositories.EnderecoRepository;
import com.nelioalves.cursomc.services.exceptions.DataIntegrityException;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente find(Integer id)	{
		return repository.findById(id).orElseThrow( () -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName() ) );
	}
	
	public List<Cliente> findAll( ) {
		return repository.findAll();
	}
	
	public Page<Cliente> findPage( Integer page, Integer linesPerPage, String orderBy, String direction ) {
		return repository.findAll( PageRequest.of(page, linesPerPage, Direction.valueOf( direction ), orderBy ) );
	}
	
	public Cliente insert( Cliente cli ) {
		cli = repository.save( cli );
		enderecoRepository.saveAll( cli.getEnderecos() );
		return cli;
	}
	
	public Cliente update( Cliente obj ) {
		return repository.save( obj );
	}
	
	public void delete( Integer id ) {
		find( id );
		
		try {
			repository.deleteById( id );
		} catch( DataIntegrityViolationException e ) {
			throw new DataIntegrityException( "Não é possível excluir pois há pedidos relacionados." );
		}
	}
}
