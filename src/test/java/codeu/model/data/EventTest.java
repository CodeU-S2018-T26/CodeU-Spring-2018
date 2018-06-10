package codeu.model.data;

import java.time.Instant;
import java.util.UUID;
import org.junit.Assert;
import org.junit.Test;

public class EventTest {

  @Test
  public void testCreate() {
    UUID id = UUID.randomUUID();
    String type = "Event_Type";
    UUID user = UUID.randomUUID();
    UUID conversation = UUID.randomUUID();
    UUID message = UUID.randomUUID();
    Instant creation = Instant.now();

    Event event = new Event(id, type, user, conversation, message, creation);

    Assert.assertEquals(id, event.getId());
    Assert.assertEquals(type, event.getEventType());
    Assert.assertEquals(user, event.getUserId());
    Assert.assertEquals(conversation, event.getConversationId());
    Assert.assertEquals(message, event.getMessageId());
    Assert.assertEquals(creation, event.getCreationTime());
  }
}
