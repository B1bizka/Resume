package org.libin.externalms.util;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;

import java.nio.charset.StandardCharsets;

public class MessageUtil {
    public static Message buildMessage(String operationType, Object value){
        MessageProperties props = MessagePropertiesBuilder
                .newInstance()
                .setContentType(MessageProperties.CONTENT_TYPE_BYTES)
                .build();
        props.setHeader(operationType, value);
        return new Message("1".getBytes(StandardCharsets.UTF_8), props);
    }

}
