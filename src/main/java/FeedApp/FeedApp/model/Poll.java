package FeedApp.FeedApp.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "polls")
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String question;
    
    private Instant publishedAt;
//    private Instant validUntil;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private User createdBy;
    
    @OneToMany( cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    private List<VoteOption> options = new ArrayList<>();

    public Poll() {}

    public String getId() {
        return this.id; 
    }


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

//    public Instant getValidUntil() {
//        return this.validUntil;
//    }
//    public void setValidUntil(Instant validUntil) {
//        this.validUntil = validUntil;
//    }

    public List<VoteOption> getOptions() {
        return this.options;
    }
    public void setOptions(List<VoteOption> options) {
        this.options = options;
    }

    public User getCreatedBy() {
        return this.createdBy; 
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }


    public Poll(String question) {
      this.question = question;
      this.publishedAt = Instant.now();
      this.createdBy = createdBy;
    }

    /**
     *
     * Adds a new option to this Poll and returns the respective
     * VoteOption object with the given caption.
     * The value of the presentationOrder field gets determined
     * by the size of the currently existing VoteOptions for this Poll.
     * I.e. the first added VoteOption has presentationOrder=0, the secondly
     * registered VoteOption has presentationOrder=1 and so on.
     */
    public VoteOption addVoteOption(String caption) {
        VoteOption option = new VoteOption();
        option.setCaption(caption);
        option.setPresentationOrder(this.options.size());
//        option.setPoll(this);
        this.options.add(option);
        return option;
    }
}
