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
import FeedApp.FeedApp.model.Vote;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class VoteControllerTest {

    @LocalServerPort
    private int port;

    private RestTemplate client;
    private String pollId;
    private Poll poll;
    private Vote vote;

    private String pollBaseUrl() {
        return "http://localhost:" + port + "/polls";
    }

    private String voteBaseUrl() {
        return "http://localhost:" + port + "/votes";
    }

    private Poll createSamplePoll() {
        Poll poll = new Poll();
        poll.setQuestion("What's your favorite color?");
        poll.setPublishedAt(Instant.now());
        poll.setValidUntil(Instant.now().plusSeconds(3600));

        VoteOption option1 = new VoteOption();
        option1.setCaption("Red");
        option1.setPresentationOrder(1);

        VoteOption option2 = new VoteOption();
        option2.setCaption("Blue");
        option2.setPresentationOrder(2);

        poll.setOptions(List.of(option1, option2));
        return poll;
    }

    @BeforeEach
    public void setup() {
        client = new RestTemplate();

        poll = createSamplePoll();
        pollId = client.postForObject(pollBaseUrl(), poll, String.class);
        assertNotNull(pollId);

        vote = new Vote();
        vote.setPublishedAt(Instant.now());
        vote.setVotesOn(poll.getOptions().get(0));

        client.postForObject(voteBaseUrl() + "/" + pollId + "/bob", vote, Void.class);
    }

    @Test
    public void testGetVote() {
        Vote fetched = client.getForObject(voteBaseUrl() + "/" + pollId + "/bob", Vote.class);
        assertNotNull(fetched);
        assertEquals("Red", fetched.getVotesOn().getCaption());
    }

    @Test
    public void testUpdateVote() {
        Vote updatedVote = new Vote();
        updatedVote.setPublishedAt(Instant.now());
        updatedVote.setVotesOn(poll.getOptions().get(1));

        client.put(voteBaseUrl() + "/" + pollId + "/bob", updatedVote);

        Vote fetched = client.getForObject(voteBaseUrl() + "/" + pollId + "/bob", Vote.class);
        assertEquals("Blue", fetched.getVotesOn().getCaption());
    }

    @Test
    public void testDeleteVote() {
        client.postForEntity(voteBaseUrl() + "/" + pollId + "/bob", vote, Void.class);
        client.delete(voteBaseUrl() + "/" + pollId + "/bob");

        try {
            client.getForEntity(voteBaseUrl() + "/" + pollId + "/bob", Vote.class);
            fail("Expected exception for non-existent vote");
        } catch (RestClientResponseException e) {
            assertEquals(404, e.getStatusCode().value());
        }
    }
}
