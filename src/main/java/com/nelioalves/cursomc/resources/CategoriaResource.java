package com.nelioalves.cursomc.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.nelioalves.cursomc.domains.Categoria;
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
