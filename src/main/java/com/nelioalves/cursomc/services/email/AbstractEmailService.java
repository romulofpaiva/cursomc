package com.nelioalves.cursomc.services.email;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.cursomc.domains.Pedido;

public abstract class AbstractEmailService implements EmailService {
	
	@Value("${default.sender}")
	private String sender;

	public void sendOrderConfirmationEmail( Pedido obj ) {
		SimpleMailMessage msg = prepareSimpleMailMessageFromPedido( obj );
		sendEmail( msg );
	}
	
	protected SimpleMailMessage prepareSimpleMailMessageFromPedido( Pedido obj) {
		SimpleMailMessage msg = new SimpleMailMessage( );
		msg.setFrom(sender);
		msg.setTo( obj.getCliente().getEmail() );
		msg.setSubject("Pedido confirmado! Código: " + obj.getId());
		msg.setSentDate(new Date( System.currentTimeMillis() ));
		msg.setText(obj.toString());
		
		return msg;
	}
}
