package codeu.controller;
import codeu.model.store.persistence.PersistentDataStore;
import org.json.simple.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class SendNotification{

  public void sendMsg(String messageContent) throws IOException
  {
    String url = "https://fcm.googleapis.com/fcm/send";
    String API_ACCESS_KEY= PersistentDataStore.getFirebaseKey();

    //hard coded for prototype
    String registrationIds="dyE91zYSwQY:APA91bGwEMZ__rHyLBfHhQWbp5_E6loByepDD9q1xFq3u9lbf3wpcUL1atF4qTCiRY9WQC79HlhGb6A8r85FETYRh7FMrdEYysUon7j1dolr88z-1ocuN-OWljMaRbLC2hvDTff2t9he";

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Authorization", "key="+API_ACCESS_KEY);

    //customize notification
    JSONObject data=new JSONObject();
    data.put("title", "CodeU Chat App");
    data.put("body",messageContent);

    JSONObject parent=new JSONObject();

    parent.put("to", registrationIds);
    parent.put("data", data);

    con.setDoOutput(true);

    OutputStreamWriter os = new OutputStreamWriter(con.getOutputStream());
    os.write(parent.toString());
    os.flush();
    os.close();

    int responseCode = con.getResponseCode();
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + parent.toString());
    System.out.println("Response Code : " + responseCode+" "+con.getResponseMessage());

  }
}