package FeedApp.FeedApp.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


import com.fasterxml.jackson.annotation.JsonBackReference;

import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    @JsonManagedReference
    private List<VoteOption> options = new ArrayList<>();

    public Poll() {}

    public String getId() {
        return this.id; 
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<VoteOption> getOptions() {
        return this.options;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }
    public void setPublishedAt(Instant publishedAt) {
      this.publishedAt = publishedAt;
    }
    public String getQuestion() { return question; }
    public void setOptions(List<VoteOption> options) { this.options = options; }
}
