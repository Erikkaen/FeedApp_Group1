package FeedApp.FeedApp.model;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

@Component
public class PollManager {
    private final Map<String, User> users = new HashMap<>();
    private final Map<String, Poll> polls = new HashMap<>();
    private final Map<String, Vote> votes = new HashMap<>();

    public Map<String, User> getUsers() {
        return this.users;
    }

    public Map<String, Poll> getPolls() {
        return this.polls;
    }

    public Map<String, Vote> getVotes() {
        return this.votes;
    }

    // user methods
    public void addUser(String username, User user) {
        this.users.put(username, user);
    }
    public User getUser(String username) {
        return this.users.get(username);
    }
    public boolean removeUser(String username) {
        return users.remove(username) != null;
    }

    // poll methods
    public void addPoll(String pollId, Poll poll) {
        this.polls.put(pollId, poll);
    }

    public Poll getPoll(String pollId) {
        return this.polls.get(pollId);
    }

    public boolean removePoll(String pollId) {
        return polls.remove(pollId) != null;
    }

    // vote methods
    public void addVote(String pollId, Vote vote, String username) {
        this.votes.put(pollId + "-" + username, vote);
    }

    public void updateVote(String pollId, Vote vote, String username) {
        this.votes.put(pollId + "-" + username, vote);
    }

    public Vote getVote(String pollId, String username) {
        return this.votes.get(pollId + "-" + username);
    }

    public boolean removeVote(String pollId, String username) {
        String key = pollId + "-" + username;
        return votes.remove(key) != null;
    }

    public void removeVotes(String pollId) {
        this.votes.keySet().removeIf(key -> key.startsWith(pollId + "-"));
    }
}
