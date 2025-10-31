package FeedApp.FeedApp.model;

import java.time.Instant;
import java.util.List;

public class Poll {
    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private List<VoteOption> options;

    public Poll() {}

    public String getQuestion() {
        return this.question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }

    public Instant getPublishedAt() {
        return this.publishedAt;
    }
    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public Instant getValidUntil() {
        return this.validUntil;
    }
    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }

    public List<VoteOption> getOptions() {
        return this.options;
    }
    public void setOptions(List<VoteOption> options) {
        this.options = options;
    }
}
