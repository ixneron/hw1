package hw1.senders;

import generated.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;


public class MessageSenderViaQueue {

    @Qualifier("jmsQueueTemplate")
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Transactional(propagation = Propagation.REQUIRED)
    public void sendRequestToActivation(final Card card) {
        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage();
                textMessage.setText(MessageSenderViaQueue.this.convertObjToXML(card));
                return textMessage;
            }
        });
    }

    private String convertObjToXML(Card card) {

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        marshaller.marshal(card, result);
        return writer.toString();
    }
}
