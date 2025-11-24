package FeedApp.FeedApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "vote_options")
public class VoteOption {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

  @Column(nullable = false)
    private String caption;

  @Column(nullable = false)
    private int presentationOrder;

    @Column(nullable = false)
    private int voteCount = 0;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "poll_id", nullable = false)
    @JsonBackReference
    private Poll poll;


    public VoteOption() {}

    public String getId() {
        return this.id; 
    }
    
    public int getVoteCount() {
        return this.voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Poll getPoll() {
        return poll;
    }

    public void setPoll(Poll poll) {
        this.poll = poll;
    }
    public String getCaption() {
      return caption;
    }
    public void setPresentationOrder(int presentationOrder) {
      this.presentationOrder = presentationOrder;
    }
    public int getPresentationOrder() {
      return presentationOrder;
    }

}
