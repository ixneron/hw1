package hw1.senders;

import generated.Card;
import hw1.util.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;


public class MessageSenderViaTopic {

    @Qualifier("jmsTopicTemplate")
    @Autowired
    private  JmsTemplate jmsTemplate;

    @Autowired
    private MessageConverter messageConverter;

    public void sendRequestToActivation(final Card card) {

        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(messageConverter.convertCardToXml(card));
                return textMessage;
            }
        });
    }

}
