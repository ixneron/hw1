package hw1.listeners.queueListener;

import hw1.listeners.Magic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.jms.*;

public class MessageListenerViaQueue implements MessageListener {

    private static Logger logger = LoggerFactory.getLogger(MessageListenerViaQueue.class);

    @Qualifier("jms2QueueTemplate")
    @Autowired
    private JmsTemplate jmsTemplate;

    @Autowired
    private Jaxb2Marshaller marshaller;

    @Override
    public void onMessage(Message message) {

        if (message instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) message;
            try {

                if (textMessage.getText().contains("roma - resender")) {
                    logger.error("получено сообщение из первой очереди, оформляем посылку во вторую очередь + активируем");
                    sendToDoubleQueue(textMessage);
                } else if (textMessage.getText().contains("katya-transaction")) { //создает эксепшн для теста
                    if (true) {
                        int[] mas = new int[5];
                        mas[6] = 10;
                    }
                } else Magic.createMagic(message, logger, marshaller);

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
}
