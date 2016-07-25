package hw1.controller;

import hw1.senders.MessageSenderViaQueue;
import hw1.senders.MessageSenderViaTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.xml.bind.JAXBException;
import java.io.IOException;

@Controller
@RequestMapping("/")
public class startController {

    private static Logger logger = LoggerFactory.getLogger(startController.class);

    @Autowired
    private MessageSenderViaTopic messageSenderViaTopic;

    @Autowired
    private MessageSenderViaQueue messageSenderViaQueue;

    @Qualifier(value = "topicListenerFirst")
    @Autowired
    private DefaultMessageListenerContainer topicListenerFirst;

    @Qualifier(value = "topicListenerSecond")
    @Autowired
    private DefaultMessageListenerContainer topicListenerSecond;

    @Qualifier(value = "topicListenerNonDur")
    @Autowired
    private DefaultMessageListenerContainer  topicListenerNonDur;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String indexPage() {
        return "index";
    }

    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainPage() {

        try {
            Test.test(messageSenderViaQueue,messageSenderViaTopic,topicListenerFirst,topicListenerSecond,topicListenerNonDur);
        } catch (JAXBException | IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return "main";
    }
}
