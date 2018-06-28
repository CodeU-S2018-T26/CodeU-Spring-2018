package codeu.controller;
import org.json.simple.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class SendNotification{

  public void sendMsg(String messageContent) throws IOException
  {
    String url = "https://fcm.googleapis.com/fcm/send";
    String API_ACCESS_KEY="AAAAQ8CY2j0:APA91bHKNT-HNdbxyO-eD671RuQlkZOgMS7VTz66uwtxVr8kDfUDcGCLojY1hgRsXK9IfyE1LmTYarOO6gq_4CThif_6bjgmvh6JIikM28HZTAQs-u7jtEkEUokDHrpWvZ9jsI6Bmfyl";
    //hard coded for prototype
    String registrationIds="f8UTjXXWES4:APA91bGoOfAAhkzN2uQLhCOf0Mxy9AIBFC_AG8km0_1mSAGCiHcAvyzPtKTjqSWKaYdkld7-aNwO7cCgu7Eya6sMeVZF4plMdpxikoh9Vd8pDg9Yd1lycVQp2VParHriMrIDGk_96IzM";

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