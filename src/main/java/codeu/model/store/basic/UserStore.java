// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.store.basic;

import codeu.model.data.Conversation;
import codeu.model.data.Message;
import codeu.model.data.User;
import codeu.model.store.persistence.PersistentStorageAgent;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Store class that uses in-memory data structures to hold values and automatically loads from and
 * saves to PersistentStorageAgent. It's a singleton so all servlet classes can access the same
 * instance.
 */
public class UserStore {

  /** Singleton instance of UserStore. */
  private static UserStore instance;

  /**
   * Returns the singleton instance of UserStore that should be shared between all servlet classes.
   * Do not call this function from a test; use getTestInstance() instead.
   */
  public static UserStore getInstance() {
    if (instance == null) {
      instance = new UserStore(PersistentStorageAgent.getInstance());
    }
    return instance;
  }

  /**
   * Instance getter function used for testing. Supply a mock for PersistentStorageAgent.
   *
   * @param persistentStorageAgent a mock used for testing
   */
  public static UserStore getTestInstance(PersistentStorageAgent persistentStorageAgent) {
    return new UserStore(persistentStorageAgent);
  }

  /**
   * The PersistentStorageAgent responsible for loading Users from and saving Users to Datastore.
   */
  private PersistentStorageAgent persistentStorageAgent;

  /** The in-memory list of Users. */
  private List<User> users;

  /** The in-memory list of Instants of Events. */
  ArrayList<Instant> eventsInstantsSorted = new ArrayList<Instant>();
  HashMap<Instant, HashMap<UUID, String>> builtEventsMap =
      new HashMap<Instant, HashMap<UUID, String>>();

  /** This class is a singleton, so its constructor is private. Call getInstance() instead. */
  private UserStore(PersistentStorageAgent persistentStorageAgent) {
    this.persistentStorageAgent = persistentStorageAgent;
    users = new ArrayList<>();
  }

  /**
   * Access the User object with the given name.
   *
   * @return null if username does not match any existing User.
   */
  public User getUser(String username) {
    // This approach will be pretty slow if we have many users.
    for (User user : users) {
      if (user.getName().equals(username)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Access the User object with the given UUID.
   *
   * @return null if the UUID does not match any existing User.
   */
  public User getUser(UUID id) {
    for (User user : users) {
      if (user.getId().equals(id)) {
        return user;
      }
    }
    return null;
  }

  /**
   * Add a new user to the current set of users known to the application. This should only be called
   * to add a new user, not to update an existing user.
   */
  public void addUser(User user) {
    users.add(user);
    persistentStorageAgent.writeThrough(user);
  }

  /**
   * Update an existing User.
   */
  public void updateUser(User user) {
    persistentStorageAgent.writeThrough(user);
  }

  /** Return true if the given username is known to the application. */
  public boolean isUserRegistered(String username) {
    for (User user : users) {
      if (user.getName().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Sets the List of Users stored by this UserStore. This should only be called once, when the data
   * is loaded from Datastore.
   */
  public void setUsers(List<User> users) {
    this.users = users;
  }

  /** Access the current set of users known to the application. */
  public List<User> getAllUsers() {
    return users;
  }

  Instant findEarliestInstant(HashMap<Instant, HashMap<UUID, String>> hm) {
    Instant earliestInstant = null;
    for (Map.Entry<Instant, HashMap<UUID, String>> m : hm.entrySet()) {
      earliestInstant = m.getKey();
      break;
    }
    for (Map.Entry<Instant, HashMap<UUID, String>> m : hm.entrySet()) {
      Instant toCmpInstant = m.getKey();
      if (earliestInstant.isBefore(toCmpInstant)) {
        earliestInstant = toCmpInstant;
      }
    }
    return earliestInstant;
  }

  /**
   * This function takes in lists of conversations,users and messages and returns a HashMap of
   * Instants mapping to an inner HashMap of UUIDs mapping to a data store string: User,Conversation
   * or Message
   * 
   * @param conversations
   * @param users
   * @param messages
   * @return built HashMap for each Event
   */
  public HashMap<Instant, HashMap<UUID, String>> buildEventsMap(List<User> users,
      List<Conversation> conversations, List<Message> messages) {
    builtEventsMap = new HashMap<Instant, HashMap<UUID, String>>();

    for (User user : users) {
      HashMap<UUID, String> innerhm = new HashMap<UUID, String>();
      innerhm.put(user.getId(), "user");
      builtEventsMap.put(user.getCreationTime(), innerhm);

    }

    for (Conversation conversation : conversations) {
      HashMap<UUID, String> innerhm = new HashMap<UUID, String>();
      innerhm.put(conversation.getId(), "conversation");
      builtEventsMap.put(conversation.getCreationTime(), innerhm);

    }
    for (Message message : messages) {
      HashMap<UUID, String> innerhm = new HashMap<UUID, String>();
      innerhm.put(message.getId(), "message");
      builtEventsMap.put(message.getCreationTime(), innerhm);
    }
    return builtEventsMap;
  }

  public HashMap<Instant, HashMap<UUID, String>> sortEventsMap(
      HashMap<Instant, HashMap<UUID, String>> hm) {
    Instant earlier = null;
    int size = hm.size();
    eventsInstantsSorted = new ArrayList<Instant>();
    HashMap<Instant, HashMap<UUID, String>> sortedhm =
        new HashMap<Instant, HashMap<UUID, String>>();
    for (int i = 0; i < size; i++) {
      earlier = findEarliestInstant(hm);
      eventsInstantsSorted.add(earlier);
      HashMap<UUID, String> innersm = hm.get(earlier);
      sortedhm.put(earlier, innersm);
      hm.remove(earlier);
    }
    return sortedhm;
  }

  public ArrayList<Instant> getAllEventsInstants() {
    return eventsInstantsSorted;
  }

}

