package hw1.util;


import generated.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.TextMessage;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.StringReader;
import java.io.StringWriter;

public class MessageConverter {

    @Autowired
    Jaxb2Marshaller marshaller;

    public String convertCardToXml(Card card) {
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        marshaller.marshal(card, result);
        return writer.toString();
    }

    public Card convertXmlToCard(Message message) throws JMSException {

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            return (Card) marshaller.unmarshal(new StreamSource(new StringReader(textMessage.getText())));
        }
        return null;
    }
}
