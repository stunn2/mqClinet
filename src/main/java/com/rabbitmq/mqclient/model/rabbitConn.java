package com.rabbitmq.mqclient.model;

import lombok.Data;

@Data
public class rabbitConn {
    private String topicExchangeName;
    private String routingKey;
    private String message;
    private String user;
    private String pass;
    private String host;
}
