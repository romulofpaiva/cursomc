package com.nelioalves.cursomc.resources;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

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

import com.nelioalves.cursomc.domains.Cidade;
import com.nelioalves.cursomc.domains.Cliente;
import com.nelioalves.cursomc.domains.Endereco;
import com.nelioalves.cursomc.domains.enums.TipoCliente;
import com.nelioalves.cursomc.dto.ClienteDTO;
import com.nelioalves.cursomc.dto.ClienteNewDTO;
import com.nelioalves.cursomc.repositories.CidadeRepository;
import com.nelioalves.cursomc.services.ClienteService;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@RestController
@RequestMapping("/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@Autowired
	private CidadeRepository cidadeRepository;
	
	@GetMapping("/{id}")
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		return ResponseEntity.ok( service.find(id) );
	}
	
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> findAll( ) {
		return ResponseEntity.ok( service.findAll( ).stream().map( cli -> new ClienteDTO( cli ) ).collect( Collectors.toList() ) );
	}
	
	@GetMapping("/page")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(value="page", defaultValue="0") Integer page,
			@RequestParam(value="linesPerPage", defaultValue="24") Integer linesPerPage,
			@RequestParam(value="orderBy", defaultValue="nome") String orderBy,
			@RequestParam(value="direction", defaultValue="ASC") String direction ) {
		
		Page<ClienteDTO> list = service.findPage( page, linesPerPage, orderBy, direction ).map( cli -> new ClienteDTO( cli ) );
		
		return ResponseEntity.ok( list );
	}
	
	@PostMapping
	public ResponseEntity<Void> insert( @Valid @RequestBody ClienteNewDTO dto ) {
		Cliente cli = new Cliente( null, dto.getNome(), dto.getEmail(), dto.getCpfOuCnpj(), TipoCliente.toEnum( dto.getTipo() ) );
		
		Cidade cid = cidadeRepository.findById( dto.getCidadeId() ).orElseThrow( () -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + dto.getCidadeId() + ", Tipo: " + Cidade.class.getName() ) );
		
		Endereco end = new Endereco( null, dto.getLogradouro(), dto.getNumero(), dto.getComplemento(), dto.getBairro(), dto.getCep(), cid, cli );
		
		cli.getEnderecos().add( end );
		
		cli.getTelefones().addAll( Arrays.asList( dto.getTelefone1(), dto.getTelefone2(), dto.getTelefone3() ) );
		
		cli = service.insert( cli );
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand( cli.getId() ).toUri();
		
		return ResponseEntity.created( uri ).build( );
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<Cliente> update( @PathVariable Integer id, @Valid @RequestBody ClienteDTO dto ) {
		Cliente cli = service.find( id );
		cli.setNome( dto.getNome() );
		cli.setEmail( dto.getEmail() );
		
		cli = service.update( cli );
		
		return ResponseEntity.ok( cli );
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete( @PathVariable Integer id ) {
		service.delete( id );
		
		return ResponseEntity.ok( ).build();
	}
}
