package FeedApp.FeedApp.model;

import java.time.Instant;

public class Vote {
    private Instant publishedAt;
    private VoteOption option;

    public Vote() {}

    public Instant getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public VoteOption getOption() {
        return option;
    }

    public void setOption(VoteOption option) {
        this.option = option;
    }
}
