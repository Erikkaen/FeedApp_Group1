package FeedApp.FeedApp.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.Instant;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import FeedApp.FeedApp.model.Poll;
import FeedApp.FeedApp.model.VoteOption;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class PollControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    private RestTemplate client;
//    private String pollId;
//    private Poll poll;
//
//    private String baseUrl() {
//        return "http://localhost:" + port + "/polls";
//    }
//
//    private Poll createSamplePoll() {
//        Poll poll = new Poll();
//        poll.setQuestion("What's your favorite color?");
//        poll.setPublishedAt(Instant.now());
//        poll.setValidUntil(Instant.now().plusSeconds(3600));
//
//        VoteOption option1 = new VoteOption();
//        option1.setCaption("Red");
//        option1.setPresentationOrder(1);
//
//        VoteOption option2 = new VoteOption();
//        option2.setCaption("Blue");
//        option2.setPresentationOrder(2);
//
//        poll.setOptions(List.of(option1, option2));
//        return poll;
//    }
//
//    @BeforeEach
//    public void setup() {
//        client = new RestTemplate();
//
//        poll = createSamplePoll();
//        pollId = client.postForObject(baseUrl(), poll, String.class);
//        assertNotNull(pollId);
//    }
//
//    @Test
//    public void testGetPoll() {
//        Poll pollFetched = client.getForObject(baseUrl() + "/" + pollId, Poll.class);
//        assertNotNull(pollFetched);
//        assertEquals("What's your favorite color?", pollFetched.getQuestion());
//        assertEquals(2, pollFetched.getOptions().size());
//    }
//
//    @Test
//    public void testUpdatePoll() {
//        poll.setQuestion("What's your favorite fruit?");
//        client.put(baseUrl() + "/" + pollId, poll);
//
//        Poll updated = client.getForObject(baseUrl() + "/" + pollId, Poll.class);
//        assertEquals("What's your favorite fruit?", updated.getQuestion());
//    }
//
//    @Test
//    public void testDeletePoll() {
//        client.delete(baseUrl() + "/" + pollId);
//
//        try {
//            client.getForEntity(baseUrl() + "/" + pollId, Poll.class);
//            fail("Expected exception for non-existent poll");
//        } catch (RestClientResponseException e) {
//            assertEquals(404, e.getStatusCode().value());
//        }
//    }
//}
