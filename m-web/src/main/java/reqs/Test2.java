package reqs;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.JMSException;
import javax.naming.NamingException;

public class Test2 {
    private static Logger logger = LoggerFactory.getLogger(Test2.class);
    public static void test() throws JMSException, NamingException {

        logger.info("HYI");

//        mqConnectionFactory.setTransportType(1);
//        mqConnectionFactory.setChannel("qwerty");
//        mqConnectionFactory.setQueueManager("qManager");
//        mqConnectionFactory.setHostName("localhost");
//        mqConnectionFactory.setPort(1414);
//
//        MQConnection connection = (MQConnection) mqConnectionFactory.createConnection();
//        connection.start();
//        MQSession mqSession = (MQSession) connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
//
//        MQQueue queue = (MQQueue) mqSession.createQueue("INPUT");
//
//        MQMessageProducer mqMessageProducer = (MQMessageProducer) mqSession.createProducer(queue);
//
//
//        TextMessage textMessage = mqSession.createTextMessage();
//        textMessage.setText("HELLO");
//        mqMessageProducer.send(textMessage);
//        mqSession.close(); connection.close();
//
//
//        MQConnection connection2 = (MQConnection) mqConnectionFactory.createConnection();
//        connection2.start();
//        MQSession mqSession2 = (MQSession) connection2.createSession(false, Session.AUTO_ACKNOWLEDGE);
//        MQMessageConsumer mqMessageConsumer = (MQMessageConsumer) mqSession2.createConsumer(queue);
//
//        TextMessage textMessage1 = (TextMessage) mqMessageConsumer.receive(1000);
//        System.out.println(textMessage1.getText());
//
//        mqSession2.close();
//        connection2.close();
    }

    public static void main(String[] args) {

    }
}
