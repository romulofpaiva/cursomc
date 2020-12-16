package com.nelioalves.cursomc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domains.Pedido;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository repo;
	
	public Pedido find( Integer id ) {
		return repo.findById(id).orElseThrow( () -> new ObjectNotFoundException( 
				"Objeto não encontrato! Id: " + id + ", Tipo: " + Pedido.class.getName() ) );
	}

}
