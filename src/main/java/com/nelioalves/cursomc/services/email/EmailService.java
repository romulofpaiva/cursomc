package com.nelioalves.cursomc.services.email;

import org.springframework.mail.SimpleMailMessage;

import com.nelioalves.cursomc.domains.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail( Pedido obj );
	
	void sendEmail( SimpleMailMessage msg );
}
