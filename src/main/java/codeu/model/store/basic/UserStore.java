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
import codeu.model.data.Event;
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

  /** The in-memory eventsMap and list of Instants of Events. */
  ArrayList<Instant> eventsInstantsSorted = new ArrayList<Instant>();
  HashMap<Instant, Event> eventsMap = new HashMap<Instant, Event>();

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

  /**
   * This function takes in the EventsMap and finds the latest Instant from its keys
   * 
   * @param hm
   * @return the earliest instant
   */
  Instant findLatestInstant(HashMap<Instant, Event> hm) {
    Instant earliestInstant = null;
    for (Map.Entry<Instant, Event> m : hm.entrySet()) {
      earliestInstant = m.getKey();
      break;
    }
    for (Map.Entry<Instant, Event> m : hm.entrySet()) {
      Instant toCmpInstant = m.getKey();
      if (earliestInstant.isBefore(toCmpInstant)) {
        earliestInstant = toCmpInstant;
      }
    }
    return earliestInstant;
  }

  /**
   * This function takes in lists of conversations,users and messages and returns a HashMap of
   * Instants mapping to an Event Object
   * 
   * @param conversations
   * @param users
   * @param messages
   * @return eventsMap
   */
  public HashMap<Instant, Event> buildEventsMap(List<User> users, List<Conversation> conversations,
      List<Message> messages) {
    eventsMap = new HashMap<Instant, Event>();
    for (User user : users) {
      Event event = new Event(user.getId(), "user");
      eventsMap.put(user.getCreationTime(), event);
    }

    for (Conversation conversation : conversations) {
      Event event = new Event(conversation.getId(), "conversation");
      eventsMap.put(conversation.getCreationTime(), event);

    }
    for (Message message : messages) {
      Event event = new Event(message.getId(), "message");
      eventsMap.put(message.getCreationTime(), event);
    }
    return eventsMap;
  }

  /**
   * This function takes in a HashMap and sorts the instants from latest to oldest
   * 
   * Stores sorted Instants in an ArrayList
   * 
   * @param eventsMap
   * @return sortedEventsMap
   */
  public HashMap<Instant, Event> sortEventsMap(HashMap<Instant, Event> eventsMap) {
    Instant earlier = null;
    int size = eventsMap.size();
    eventsInstantsSorted = new ArrayList<Instant>();
    HashMap<Instant, Event> sortedEventsMap = new HashMap<Instant, Event>();
    for (int i = 0; i < size; i++) {
      earlier = findLatestInstant(eventsMap);
      eventsInstantsSorted.add(earlier);
      Event event = eventsMap.get(earlier);
      sortedEventsMap.put(earlier, event);
      eventsMap.remove(earlier);
    }
    return sortedEventsMap;
  }

  /** Access the current set of events known to the application. */
  public ArrayList<Instant> getAllEventsInstants() {
    return eventsInstantsSorted;
  }

  /** Access the current eventsMap. */
  public HashMap<Instant, Event> getEventsMap() {
    return eventsMap;
  }

}

