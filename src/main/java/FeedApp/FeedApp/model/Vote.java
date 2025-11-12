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
    private VoteOption option;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String guestId;

    public Vote() {}

    public String getId() {return this.id;}

    public Instant getPublishedAt() {return this.publishedAt;}
    public void setPublishedAt(Instant publishedAt) {this.publishedAt = publishedAt;}

    public VoteOption getVotesOn() {return this.option;}
    public void setVotesOn(VoteOption votesOn) {this.option = votesOn;}

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getGuestId() { return guestId; }
    public void setGuestId(String guestId) { this.guestId = guestId; }
}
