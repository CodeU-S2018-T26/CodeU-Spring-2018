package codeu.controller;
import org.json.simple.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;


public class SendNotification{

  public void sendMsg(String messageContent, String token, String APIKey) throws IOException
  {
    String url = "https://fcm.googleapis.com/fcm/send";
    String API_ACCESS_KEY= APIKey;

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

    parent.put("to", token);
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