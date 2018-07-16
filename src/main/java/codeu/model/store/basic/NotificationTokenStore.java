package codeu.model.store.basic;

import codeu.model.data.*;
import codeu.model.store.persistence.PersistentStorageAgent;

import java.time.Instant;
import java.util.*;

public class NotificationTokenStore {
  /** Singleton instance of NotificationTokenStore. */
  private static NotificationTokenStore instance;

  private String messagingAPIKey;

  /**
   * Returns the singleton instance of NotificationTokenStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static NotificationTokenStore getInstance() {
    if (instance == null) {
      instance = new NotificationTokenStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static NotificationTokenStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new NotificationTokenStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading NotificationTokens from and saving NotificationTokens to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Users. */
  private Hashtable<UUID, String> notificationTokens;


  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private NotificationTokenStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    notificationTokens = new Hashtable<UUID, String>();
  }


  /**
   * Access the Notification Token with the given UUID.
   *
   * @return null if the UUID does not have a corresponding Notification Token.
   */
  public String getNotificationToken(UUID id) {
    return notificationTokens.get(id);
  }

  /**
   * Add a new Notification Token to the current set of Notification Tokens known to the application.
   *
   * This can be called to add or update a Notification Token corresponding to a user.
   */
  public void addNotificationToken(UUID id, String token) {
    notificationTokens.put(id, token);
    persistentStorageAgent.writeThrough(id, token);
  }

  /**
   * Sets the HashTable of Notification Tokens stored by this NotificationTokenStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setNotificationTokens(Hashtable<UUID, String> notificationTokens) {
    this.notificationTokens = notificationTokens;
  }

  /** Access the current set of NotificationTokens known to the application. */
  public Collection<String> getAllNotificationTokens() {
    return new ArrayList<>(notificationTokens.values());
  }

  public void setMessagingAPIKey(String key){
    this.messagingAPIKey = key;
  }

  public String getMessagingAPIKey(){
    return messagingAPIKey;
  }

}
