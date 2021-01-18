package com.nelioalves.cursomc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domains.Categoria;
import com.nelioalves.cursomc.domains.Produto;
import com.nelioalves.cursomc.repositories.CategoriaRepository;
import com.nelioalves.cursomc.repositories.ProdutoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	public Produto find( Integer id ) {
		return produtoRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException( 
				"Objeto não encontrato! Id: " + id + ", Tipo: " + Produto.class.getName() ) );
	}
	
	public Page<Produto> search( String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction ) {
		List<Categoria> categorias = categoriaRepository.findAllById( ids );
		return produtoRepository.search( nome, categorias, PageRequest.of( page, linesPerPage, Direction.valueOf( direction ), orderBy ) );
	}
}
