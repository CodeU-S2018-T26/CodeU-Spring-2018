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
import java.util.UUID;
import java.util.ArrayList;

import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

import com.google.appengine.api.images.Image;
import com.google.appengine.api.datastore.Blob;


/** Class representing a message. Messages are sent by a User in a Conversation. */
public class Message {

  private final UUID id;
  private final UUID conversation;
  private final UUID author;
  private final String content;
  private final Instant creation;
  private final Blob image;
  private ArrayList <Blob> emojis;

  /**
   * Constructs a new Message.
   *
   * @param id the ID of this Message
   * @param conversation the ID of the Conversation this Message belongs to
   * @param author the ID of the User who sent this Message
   * @param content the text content of this Message
   * @param creation the creation time of this Message
   * @param image an image to send with this message
   * @param emojis
   */
   public Message(UUID id, UUID conversation, UUID author, String content, Instant creation, Blob image, ArrayList <Blob> emojis) {
     this.id = id;
     this.conversation = conversation;
     this.author = author;
     this.content = content;
     this.creation = creation;
     this.image = image;
     this.emojis = emojis;
   }

  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation, Blob image) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.content = content;
    this.creation = creation;
    this.image = image;
    this.emojis = null;
  }

  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation, ArrayList <Blob> emojis) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.content = content;
    this.creation = creation;
    this.image = null;
    this.emojis = emojis;
  }

  public Message(UUID id, UUID conversation, UUID author, String content, Instant creation) {
    this.id = id;
    this.conversation = conversation;
    this.author = author;
    this.content = content;
    this.creation = creation;
    this.image = null;
    this.emojis = null;
  }


  /** Returns the ID of this Message. */
  public UUID getId() {
    return id;
  }

  /** Returns the ID of the Conversation this Message belongs to. */
  public UUID getConversationId() {
    return conversation;
  }

  /** Returns the ID of the User who sent this Message. */
  public UUID getAuthorId() {
    return author;
  }

  /** Returns the text content of this Message. */
  public String getContent() {
    return content;
  }

  /** Returns the length of this Message. */
  public int getMessageLength(){
    return content.length();
  }

  /** Returns the creation time of this Message. */
  public Instant getCreationTime() {
    return creation;
  }

  /** Returns image associated with this Message */
  public Blob getImage(){
    return this.image;
  }

  public boolean imageExists(){
    return this.image != null;
  }

  public ArrayList <Blob> getEmojis(){
    return this.emojis;
  }

  public boolean emojisExist(){
    return this.emojis != null;
  }
}
