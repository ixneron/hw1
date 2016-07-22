package hw1.controller;

import generated.Card;
import hw1.senders.MessageSenderViaQueue;
import hw1.senders.MessageSenderViaTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.DefaultMessageListenerContainer;

import javax.xml.bind.JAXBException;
import java.io.IOException;


public class Test {

    private static Logger logger = LoggerFactory.getLogger(Test.class);

    public static void test(MessageSenderViaQueue messageSenderViaQueue,
                            MessageSenderViaTopic messageSenderViaTopic,
                            DefaultMessageListenerContainer topicListenerFirst,
                            DefaultMessageListenerContainer topicListenerSecond,
                            DefaultMessageListenerContainer topicListenerNonDur) throws JAXBException, InterruptedException, IOException {

        logger.error("===================================================================================================================");
        logger.error("тестируем первый сценарий (отправка в очередь и чтение + отправка в топик и чтение (2 подписчика)");
        logger.error("===================================================================================================================");

        testProducerAndConsumer(messageSenderViaQueue, messageSenderViaTopic);

        Thread.sleep(2000);

        logger.error("===================================================================================================================");
        logger.error("тестируем второй сценарий (отправка в топик, когда клиенты оффлайн (потеря сообщений)");
        logger.error("===================================================================================================================");
        testOfflineConsumer(messageSenderViaQueue, messageSenderViaTopic, topicListenerFirst, topicListenerSecond, topicListenerNonDur);

        Thread.sleep(2000);

        logger.error("===================================================================================================================");
        logger.error("старые сообщение :");
        logger.error("===================================================================================================================");
        topicListenerFirst.initialize();
        topicListenerFirst.start();
        topicListenerSecond.initialize();
        topicListenerSecond.start();
        Thread.sleep(1500);
        logger.error("===================================================================================================================");

        Thread.sleep(2000);

        logger.error("тестируем третий сценарий (отправка в топик, когда клиент оффлайн и получение сообщения, когда клиент залогинился");
        logger.error("=================================================================================================================== ");
        testDurableConsumer(messageSenderViaQueue, messageSenderViaTopic, topicListenerFirst, topicListenerSecond, topicListenerNonDur);

        Thread.sleep(2000);

        logger.error("===================================================================================================================");
        logger.error("тестируем четвертый сценарий (отправка сообщение в очередь А, сразу после получения сообщения из очереди B");
        logger.error("===================================================================================================================");
        testResendFromQToQ(messageSenderViaQueue);

        Thread.sleep(2000);

        logger.error("===================================================================================================================");
        logger.error("тестируем пятый сценарий (откат транзакции)");
        logger.error("===================================================================================================================");
        testTransact(messageSenderViaQueue);
    }

    public static void testProducerAndConsumer(MessageSenderViaQueue messageSenderViaQueue, MessageSenderViaTopic messageSenderViaTopic) {
        Card queueCard = new Card();
        queueCard.setCardOwner("Alex - Queue");
        queueCard.setCardStatus("inactive");
        queueCard.setCardLimit(0);

        Card topicCard = new Card();
        topicCard.setCardOwner("Valera - Topic");
        topicCard.setCardStatus("inactive");
        topicCard.setCardLimit(0);

        logger.error("отправка запроса на чтение (queue)");
        messageSenderViaQueue.sendRequestToActivation(queueCard);

        logger.error("отправка запроса на чтение (topic)");
        messageSenderViaTopic.sendRequestToActivation(topicCard);
    }

    public static void testDurableConsumer(MessageSenderViaQueue messageSenderViaQueue,
                                           MessageSenderViaTopic messageSenderViaTopic,
                                           DefaultMessageListenerContainer topicListenerFirst,
                                           DefaultMessageListenerContainer topicListenerSecond,
                                           DefaultMessageListenerContainer topicListenerNonDur) throws InterruptedException, IOException {
        Card topicCard = new Card();
        topicCard.setCardOwner("Alena - Topic");
        topicCard.setCardStatus("inactive");
        topicCard.setCardLimit(0);

        topicListenerFirst.stop();
        topicListenerFirst.shutdown();
        topicListenerSecond.stop();
        topicListenerSecond.shutdown();
        topicListenerNonDur.stop();
        topicListenerNonDur.shutdown();
        logger.error("отключаем клиентов");

        Thread.sleep(1000);

        logger.error("отправляем сообщение в топик");
        messageSenderViaTopic.sendRequestToActivation(topicCard);

        topicListenerFirst.initialize();
        topicListenerFirst.start();
        topicListenerSecond.initialize();
        topicListenerSecond.start();
        topicListenerNonDur.initialize();
        topicListenerNonDur.start();

        Thread.sleep(1500);

        logger.error("2 из 3 подписчиков получили сообщения.");
    }

    public static void testOfflineConsumer(MessageSenderViaQueue messageSenderViaQueue,
                                           MessageSenderViaTopic messageSenderViaTopic,
                                           DefaultMessageListenerContainer topicListenerFirst,
                                           DefaultMessageListenerContainer topicListenerSecond,
                                           DefaultMessageListenerContainer topicListenerNonDur) throws InterruptedException, IOException {

        Card topicCard = new Card();
        topicCard.setCardOwner("Sasha - Topic");
        topicCard.setCardStatus("inactive");
        topicCard.setCardLimit(0);

        logger.error("отключаем клиентов");

        topicListenerFirst.stop();
        topicListenerFirst.shutdown();
        topicListenerSecond.stop();
        topicListenerSecond.shutdown();
        topicListenerNonDur.stop();
        topicListenerNonDur.shutdown();

        logger.error("отправляем сообщение");
        messageSenderViaTopic.sendRequestToActivation(topicCard);

        Thread.sleep(2000);


        topicListenerNonDur.initialize();
        topicListenerNonDur.start();
        logger.error("вновь подключенные подписчики не получили сообщений");
    }

    public static void testResendFromQToQ(MessageSenderViaQueue messageSenderViaQueue) {

        Card resendCard = new Card();
        resendCard.setCardOwner("roma - resender");
        resendCard.setCardStatus("inactive");
        resendCard.setCardLimit(0);

        messageSenderViaQueue.sendRequestToActivation(resendCard);

    }

    public static void testTransact(MessageSenderViaQueue messageSenderViaQueue) throws InterruptedException {

        Card trCard = new Card();
        trCard.setCardOwner("katya-transaction");
        trCard.setCardStatus("inactive");
        trCard.setCardLimit(0);

        messageSenderViaQueue.sendRequestToActivation(trCard);
    }
}
