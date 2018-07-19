// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package codeu.model.data;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import codeu.model.store.basic.ConversationStore;

/** Class representing a registered user. */
public class User {
  private final UUID id;
  private final String name;
  private final String passwordHash;
  private final Instant creation;
  private List<Conversation> conversations = ConversationStore.getInstance().getAllConversations();
  private List<Conversation> unfollowedConversations = new ArrayList<Conversation>();

  /**
   * Constructs a new User.
   *
   * @param id the ID of this User
   * @param name the username of this User
   * @param passwordHash the password of this User
   * @param creation the creation time of this User
   */
  public User(UUID id, String name, String passwordHash, Instant creation) {
    this.id = id;
    this.name = name;
    this.passwordHash = passwordHash;
    this.creation = creation;
  }

  /** Returns the ID of this User. */
  public UUID getId() {
    return id;
  }

  /** Returns the username of this User. */
  public String getName() {
    return name;
  }
  
  /** Returns the password hash of this User. */
  public String getPasswordHash() {
    return passwordHash;
  }

  /** Returns the creation time of this User. */
  public Instant getCreationTime() {
    return creation;
  }
  
  /** Set the current conversations that the user has followed. */
  public void addConversation(Conversation conversation) {
    unfollowedConversations.add(conversation);
  }
  
  /** Access the current conversations that the user has followed. */
  public List<Conversation> getConversations() {
    return conversations;
  }
  
  /** Access the current conversations that the user has unfollowed. */
  public List<Conversation> getUnfollowedConversations() {
    return unfollowedConversations;
  }
  
  /** Delete conversation from current set of conversations being followed. */
  public void deleteConversation(Conversation conversation) {
    conversations.remove(conversation);
  }
  
  /** Checks if the input conversation is being followed by the user. */
  public boolean isConversationFollowed(Conversation conv) {
    for (Conversation conversation : conversations) {
      if ( conversation == conv)
        return true;
    }
    return false;
  }
  
  /** Checks if the input conversation is being unfollowed by the user. */
  public boolean isConversationUnfollowed(Conversation conv) {
    if (unfollowedConversations.isEmpty())
      return false;
    
    for (Conversation conversation : unfollowedConversations) {
      if ( conversation == conv)
        return true;
    }
    return false;
  }
}
