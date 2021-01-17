package com.rabbitmq.mqclient.controller;


import com.rabbitmq.mqclient.model.MQModel;
import com.rabbitmq.mqclient.model.rabbitConn;
import org.springframework.amqp.rabbit.connection.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Type;

@RestController
@RequestMapping("/api")
public class MQController {

    // Create a new Note
    @PostMapping("/send")
    public serviceResult send(@RequestBody rabbitConn conn)
    {
        MQModel mm = new MQModel(new RabbitTemplate());
        serviceResult sr = new serviceResult();
        if (mm.getRabbitTemplate().getConnectionFactory() == null) {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory(conn.getHost());
            connectionFactory.setUsername(conn.getUser());
            connectionFactory.setPassword(conn.getPass());

            Connection connection = connectionFactory.createConnection();

            mm.getRabbitTemplate().setConnectionFactory(connectionFactory);


        }
            sr.returnCode=0;
        sr.returnMessage = "Ok";

        //try
  //      {
            mm.getRabbitTemplate().convertAndSend(conn.getTopicExchangeName(),
                    conn.getRoutingKey(), conn.getMessage());
 /*       }
/        catch (Exception ex)
        {
            sr.setReturnCode(1);
            System.out.println(ex );
            sr.setReturnMessage(ex.getMessage());
        }
*/
        return sr;
    }

    @GetMapping("/Message/{queue}")
    public serviceResult message (@PathVariable(value = "queue") String queueName) {
        MQModel mm = new MQModel(new RabbitTemplate());
        serviceResult sr = new serviceResult();

        if (mm.getRabbitTemplate().getConnectionFactory() == null) {
            CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");

            Connection connection = connectionFactory.createConnection();

            mm.getRabbitTemplate().setConnectionFactory(connectionFactory);

            /*
            sr.returnCode = 1;
            sr.returnMessage = "No host defined ";
            */

        }


            Object message = mm.getRabbitTemplate().receiveAndConvert(queueName);
        /*
                    ,
                    new ParameterizedTypeReference<String>() {
                        @Override
                        public Type getType() {
                            return super.getType();
                        }
                    } );
        */


            sr.returnCode = 0;
            sr.returnMessage = "Message is :" + message;
        


        return sr;
    }
}
