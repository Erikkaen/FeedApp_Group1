package FeedApp.FeedApp.controllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import FeedApp.FeedApp.model.User;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//public class UserControllerTest {
//
//    @LocalServerPort
//    private int port;
//
//    private RestTemplate client;
//    private User user;
//
//    private String baseUrl() {
//        return "http://localhost:" + port + "/users";
//    }
//
//    @BeforeEach
//    public void setup() {
//        client = new RestTemplate();
//
//        user = new User();
//        user.setUsername("bob");
//        user.setEmail("bob@example.com");
//
//        client.postForObject(baseUrl(), user, String.class);
//    }
//
//    @Test
//    public void testGetUser() {
//        User userFetched = client.getForObject(baseUrl() + "/bob", User.class);
//        assertNotNull(userFetched);
//        assertEquals("bob", userFetched.getUsername());
//        assertEquals("bob@example.com", userFetched.getEmail());
//    }
//
//    @Test
//    public void testUpdateUser() {
//        user.setEmail("bob@newEmail.com");
//        client.put(baseUrl() + "/bob", user);
//
//        User updatedUser = client.getForObject(baseUrl() + "/bob", User.class);
//        assertEquals("bob@newEmail.com", updatedUser.getEmail());
//    }
//
//    @Test
//    public void testDeleteUser() {
//        client.delete(baseUrl() + "/bob");
//
//        try {
//            client.getForEntity(baseUrl() + "/bob", User.class);
//            fail("Expected exception for non-existent user");
//        } catch (RestClientResponseException e) {
//            assertEquals(404, e.getStatusCode().value());
//        }
//    }
//}
