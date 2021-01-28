package com.nelioalves.cursomc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import com.nelioalves.cursomc.domains.PagamentoComBoleto;

@Service
public class BoletoService {
	
	public void preencherPagamentoComBoleto( PagamentoComBoleto pagamento, Date instanteDoPedido ) {
		Calendar cal = Calendar.getInstance();
		cal.setTime( instanteDoPedido );
		cal.add( Calendar.DAY_OF_MONTH, 7 );
		pagamento.setDataVencimento( cal.getTime() );
	}

}