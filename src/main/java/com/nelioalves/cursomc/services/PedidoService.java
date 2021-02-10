package com.nelioalves.cursomc.services;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nelioalves.cursomc.domains.ItemPedido;
import com.nelioalves.cursomc.domains.PagamentoComBoleto;
import com.nelioalves.cursomc.domains.Pedido;
import com.nelioalves.cursomc.domains.enums.EstadoPagamento;
import com.nelioalves.cursomc.repositories.ItemPedidoRepository;
import com.nelioalves.cursomc.repositories.PagamentoRepository;
import com.nelioalves.cursomc.repositories.PedidoRepository;
import com.nelioalves.cursomc.services.email.EmailService;
import com.nelioalves.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	public Pedido find( Integer id ) {
		return pedidoRepository.findById(id).orElseThrow( () -> new ObjectNotFoundException( 
				"Objeto não encontrato! Id: " + id + ", Tipo: " + Pedido.class.getName() ) );
	}
	
	@Transactional
	public Pedido insert( Pedido pedido ) {
		pedido.setId( null );
		pedido.setInstante( new Date());
		pedido.setCliente(clienteService.find(pedido.getCliente().getId()));
		pedido.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		pedido.getPagamento().setPedido(pedido);
		
		if( pedido.getPagamento() instanceof PagamentoComBoleto )
			boletoService.preencherPagamentoComBoleto( (PagamentoComBoleto)pedido.getPagamento(), pedido.getInstante() );
		
		pedido = pedidoRepository.save( pedido );
		
		pagamentoRepository.save( pedido.getPagamento() );
		
		for( ItemPedido itemPedido : pedido.getItens() ) {
			itemPedido.setProduto(produtoService.find(itemPedido.getProduto().getId()));
			itemPedido.setDesconto(0.0);
			itemPedido.setPreco( itemPedido.getProduto().getPreco() );
			itemPedido.setPedido( pedido );
		}
		
		itemPedidoRepository.saveAll( pedido.getItens() );
		
		emailService.sendOrderConfirmationEmail(pedido);
		
		return pedido;
	}

}
