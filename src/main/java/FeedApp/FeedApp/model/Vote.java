package FeedApp.FeedApp.model;

import java.time.Instant;

import jakarta.persistence.*;

@Entity
@Table(name = "votes")
public class Vote {
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private Instant publishedAt;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    private VoteOption votesOn;

    public Vote() {}

    public String getId() {
        return this.id; 
    }

    public Instant getPublishedAt() {
        return this.publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public VoteOption getVotesOn() {
        return this.votesOn;
    }

    public void setVotesOn(VoteOption votesOn) {
        this.votesOn = votesOn;
    }
}
