package FeedApp.FeedApp.dto;

import java.time.Instant;

public class VoteRequest {
  public String optionId;
  public Instant publishedAt;
  public String userIdOrGuestId;
  public String optionCaption;

  public String getUserIdOrGuestId() { return userIdOrGuestId; }
  public void setUserIdOrGuestId(String userIdOrGuestId) { this.userIdOrGuestId = userIdOrGuestId; }
  public String getOptionId() {
    return optionId;
  }
}
