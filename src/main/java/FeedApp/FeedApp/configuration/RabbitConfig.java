package FeedApp.FeedApp.configuration;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

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