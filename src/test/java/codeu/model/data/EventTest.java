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

    Event event = new Event(id, type);

    Assert.assertEquals(id, event.getId());
    Assert.assertEquals(type, event.getEventType());
  }
}