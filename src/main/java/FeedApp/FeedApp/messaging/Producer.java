package FeedApp.FeedApp.messaging;

import com.rabbitmq.client.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Console;
import java.io.IOException;

@Component
public class Producer {

  private static final String EXCHANGE_NAME = "pollTopic";
  private final Channel producerChannel;

  public Producer(@Qualifier("producerChannel") Channel producerChannel) {
    this.producerChannel = producerChannel;
  }


  public void Produce(String message, String pollId) {
    try {
      producerChannel.basicPublish( EXCHANGE_NAME, pollId, null, message.getBytes());

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
