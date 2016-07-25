package hw1.listeners.queueListener;

import generated.Card;
import hw1.util.MessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;

public class MessageListenerViaQueue implements MessageListener {

    private static final Logger logger = LoggerFactory.getLogger(MessageListenerViaQueue.class);

    @Qualifier("jms2QueueTemplate")
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private MessageConverter messageConverter;

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {
                if (textMessage.getText().contains("roma - resender")) { //отправка из A в B
                    logger.error("получено сообщение из первой очереди, оформляем посылку во вторую очередь + активируем");
                    sendToDoubleQueue(textMessage);

                } else if (textMessage.getText().contains("katya-transaction")) { //создает эксепшн для теста 5 сценария
                    generateException4Test();

                } else {
                    try {
                        Card card = messageConverter.convertXmlToCard(message);
                        logger.error("получено сообщение || владелец карты : " + card.getCardOwner() + "|| лимит : " + card.getCardLimit() + "|| статус : " + card.getCardStatus() + " || ");
                    } catch (JMSException e) {
                        e.printStackTrace();
                    }
                };

            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    private void sendToDoubleQueue(final TextMessage textMessage) {

        jmsTemplate.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                String temp = textMessage.getText();
                temp = temp.replaceFirst("<cardStatus>inactive</cardStatus>", "<cardStatus>active</cardStatus>");
                temp = temp.replaceFirst("<cardLimit>0</cardLimit>", "<cardLimit>1000</cardLimit>");

                TextMessage newMessage = session.createTextMessage();
                newMessage.setText(temp);

                return newMessage;
            }
        });
    }

    private void generateException4Test() {
        if (true) {
            int[] mas = new int[5];
            mas[6] = 10;
        }
    }
}
