package FeedApp.FeedApp.messaging;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class Producer {

  private final Channel producerChannel;

  public Producer(@Qualifier("producerChannel") Channel producerChannel) {
    this.producerChannel = producerChannel;
  }

  public void createTopicForPoll(String exchangeName) throws Exception {
    producerChannel.exchangeDeclare(exchangeName, "topic", true);
  }


  public void Produce(String message, String pollId) {
    String exchangeName = "Poll_" + pollId;
    AMQP.BasicProperties props = new AMQP.BasicProperties.Builder().deliveryMode(2).build();
    try {
      producerChannel.basicPublish(exchangeName, "", props, message.getBytes());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
