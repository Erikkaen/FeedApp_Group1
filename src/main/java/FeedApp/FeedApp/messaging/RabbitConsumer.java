package FeedApp.FeedApp.messaging;

import FeedApp.FeedApp.services.ConsumerService;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Component;

@Component
public class RabbitConsumer {

  private final Connection connection;
  private final ConsumerService consumerService;
  private static final String EXCHANGE_NAME = "pollTopic";

  public RabbitConsumer(Connection connection, ConsumerService consumerService) {
    this.connection = connection;
    this.consumerService = consumerService;
  }

  public void createConsumerForPoll(String pollId) throws Exception {
    Channel channel = connection.createChannel();
    channel.exchangeDeclare(EXCHANGE_NAME, "topic", true);


    String queueName = "pollQueue_" + pollId;
    boolean durable = true;
    boolean exclusive = false;
    boolean autoDelete = false;

    channel.queueDeclare(queueName, durable, exclusive, autoDelete, null);
    channel.queueBind(queueName, EXCHANGE_NAME, pollId);

    DeliverCallback callback = (consumerTag, message) -> {
      String body = new String(message.getBody());
      consumerService.votePersist(body);
      channel.basicAck(message.getEnvelope().getDeliveryTag(), false);
    };

    channel.basicConsume(queueName, false, callback, consumerTag -> {});
    System.out.println("Consumer bound to pollId = " + pollId);
  }
}
