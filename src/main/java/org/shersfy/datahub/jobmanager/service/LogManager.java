package org.shersfy.datahub.jobmanager.service;

import org.shersfy.datahub.commons.meta.MessageData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LogManager {
    
    private Logger logger = LoggerFactory.getLogger(getClass());
    
    public void sendMsg(MessageData msg) {
        logger.info(msg.toString());
    }

}
