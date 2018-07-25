package codeu.model.store.basic;

import codeu.model.data.*;
import codeu.model.store.persistence.PersistentStorageAgent;

import com.google.appengine.api.datastore.Blob;


import java.time.Instant;
import java.util.*;

public class EmojiStore {
  /** Singleton instance of EmojiStore. */
  private static EmojiStore instance;

  // private String messagingAPIKey;

  /**
   * Returns the singleton instance of EmojiStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static EmojiStore getInstance() {
    if (instance == null) {
      instance = new EmojiStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static EmojiStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new EmojiStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Emojis from and saving Emojis to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory table of emojis. */
  private Hashtable<String, Blob> emojis;


  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private EmojiStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    emojis = new Hashtable<String, Blob>();
  }


  /**
   * Access the Emoji with the given shortcode.
   *
   * @return null if the Shortcode does not have a corresponding emoji.
   */
  public Blob getEmoji(String shortcode) {
    return emojis.get(shortcode);
  }

  public Hashtable<String, Blob> getEmojiTable(){
    return emojis;
  }

  /**
   * Add a new custom emoji to the current set of emojis known to the application.
   *
   * This can be called to add or update a custom emoji corresponding to a user.
   */
  public void addEmoji(String shortcode, Blob image) {
    emojis.put(shortcode, image);
    persistentStorageAgent.writeThrough(shortcode, image);
  }

  /**
   * Sets the HashTable of Notification Tokens stored by this EmojiStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setEmojis(Hashtable<String, Blob> emojis) {
    this.emojis = emojis;
  }

  /** Access the current set of Emojis known to the application. */
  public Collection<Blob> getAllEmojis() {
    return new ArrayList<>(emojis.values());
  }

  // public void setMessagingAPIKey(String key){
  //   this.messagingAPIKey = key;
  // }

  // public String getMessagingAPIKey(){
  //   return messagingAPIKey;
  // }

}
