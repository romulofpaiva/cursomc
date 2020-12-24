package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domains.Categoria;
import com.nelioalves.cursomc.dto.CategoriaDTO;
import com.nelioalves.cursomc.services.CategoriaService;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService categoriaService;

	@GetMapping("/{id}")
	public ResponseEntity<Categoria> find(@PathVariable Integer id) {
		Categoria cat = categoriaService.find(id);

		return ResponseEntity.ok(cat);
	}
	
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> findAll( ) {
		List<CategoriaDTO> list = categoriaService.findAll( ).stream().map( cat -> new CategoriaDTO( cat ) ).collect(Collectors.toList());
		
		return ResponseEntity.ok( list );
	}
	
	@GetMapping(value="/page")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction ) {
		Page<CategoriaDTO> list = categoriaService.findPage(page, linesPerPage, orderBy, direction).map( cat -> new CategoriaDTO( cat ));
		
		return ResponseEntity.ok( list );
	}
	
	@PostMapping
	public ResponseEntity<Void> save( @RequestBody Categoria cat ) {
		cat = categoriaService.save( cat );
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequestUri().path("/{id}").buildAndExpand( cat.getId() ).toUri();
		
		return ResponseEntity.created( uri ).build();
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Categoria> update( @PathVariable Integer id, @RequestBody Categoria cat ) {
		cat.setId( id );
		cat = categoriaService.update( cat );
		
		return ResponseEntity.ok( cat );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete( @PathVariable Integer id ) {
		categoriaService.delete( id );
		return ResponseEntity.ok( ).build();
	}
}
