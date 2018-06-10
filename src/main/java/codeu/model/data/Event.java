package codeu.model.data;

import java.time.Instant;
import java.util.UUID;

/**
 * Class representing an event
 */
public class Event {
  public final UUID id;
  public final String type;
  public final UUID user;
  public final UUID conversation;
  public final UUID message;
  public final Instant creation;

  /**
   * Constructs a new Conversation.
   *
   * @param id the ID of this Event
   * @param type the type of this Event
   * @param user the ID of the User who created this Event
   * @param conversation the ID of the conversation
   * @param message the ID of the message
   * @param creation the creation time of this Conversation
   */
  public Event(UUID id, String type, UUID user, UUID conversation, UUID message, Instant creation) {
    this.id = id;
    this.type = type;
    this.user = user;
    this.conversation = conversation;
    this.message = message;
    this.creation = creation;
  }

  /** Returns the ID of this Event. */
  public UUID getId() {
    return id;
  }

  /** Returns the Event type. */
  public String getEventType() {
    return type;
  }

  /** Returns the ID of the User who created this Event. */
  public UUID getUserId() {
    return user;
  }

  /** Returns the ID of the Conversation in this Event. */
  public UUID getConversationId() {
    return conversation;
  }

  /** Returns the ID of the Message in this Event. */
  public UUID getMessageId() {
    return message;
  }

  /** Returns the creation time of this Event. */
  public Instant getCreationTime() {
    return creation;
  }
}
