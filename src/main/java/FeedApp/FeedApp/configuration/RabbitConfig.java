package FeedApp.FeedApp.configuration;

import FeedApp.FeedApp.services.ConsumerService;
import FeedApp.FeedApp.model.PollManager;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

@Configuration
public class RabbitConfig {

  private static final String EXCHANGE_NAME = "pollTopic";

//  @Bean
//  Channel connectionFactory(ConsumerService consumerService) throws IOException, TimeoutException {
//    System.out.println("ConnectionFACTORY");
//    ConnectionFactory factory = new ConnectionFactory();
//    System.out.println(" new ConnectionFACTORY");
//    factory.setHost("rabbitmq");
//    Connection connection = factory.newConnection();
//    System.out.println("Connection");
//    Channel channel = connection.createChannel();
//    System.out.println("Channel");
//
//    channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
//    System.out.println("exchangeDeclare");
//    String queue = channel.queueDeclare().getQueue();
//    System.out.println("Queue");
//    channel.queueBind(queue, EXCHANGE_NAME, "");
//
//    System.out.println("Waiting for messages. To exit press CTRL+C");
//
//    DeliverCallback callback = ((consumerTag, message) -> {
//      String result = new String(message.getBody(), StandardCharsets.UTF_8);
//      System.out.println(" [x] Received '" + result + "'");
//      consumerService.votePersist(result);
//      channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
//    });
//    channel.basicConsume(queue, false,  callback, consumerTag -> {});
//
//    return channel;
//  }

  @Bean
  public Connection rabbitConnection() throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setHost("rabbitmq");  // Change if needed
    return factory.newConnection();
  }

  @Bean(name = "producerChannel")
  public Channel producerChannel(Connection connection) throws Exception {
    Channel channel = connection.createChannel();
    return channel;
  }

}