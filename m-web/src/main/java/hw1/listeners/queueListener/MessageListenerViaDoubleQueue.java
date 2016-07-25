package hw1.listeners.queueListener;

import generated.Card;
import hw1.util.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;

public class MessageListenerViaDoubleQueue implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerViaDoubleQueue.class);

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public void onMessage(Message message) {
        try {
            Card card = messageConverter.convertXmlToCard(message);
            logger.error("получено сообщение || владелец карты : " + card.getCardOwner() + "|| лимит : " + card.getCardLimit() + "|| статус : " + card.getCardStatus() + " || ");
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
