package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Class representing an event
 */
public class Event {
  public final UUID id;
  public final String type;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Event
   * @param type the type of this Event
   */
  public Event(UUID id, String type) {
    this.id = id;
    this.type = type;
  }

  /** Returns the ID of this Event. */
  public UUID getId() {
    return id;
  }

  /** Returns the Event type. */
  public String getEventType() {
    return type;
  }
}
