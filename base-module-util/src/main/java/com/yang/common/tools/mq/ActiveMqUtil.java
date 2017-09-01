package com.yang.common.tools.mq;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

public class ActiveMqUtil {
	
	private JmsTemplate jmsTemplate;
	
	public void send(String destinationName, final String messageString){
		jmsTemplate.send(destinationName, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
            	// 消息内容
            	TextMessage message = session.createTextMessage(messageString);
                return message;
            }
        });
	}

	public JmsTemplate getJmsTemplate() {
		return jmsTemplate;
	}

	public void setJmsTemplate(JmsTemplate jmsTemplate) {
		this.jmsTemplate = jmsTemplate;
	}
	
}
