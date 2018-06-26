package codeu.controller;
import org.json.simple.JSONObject;

import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class SendNotification{

  public static void sendMsg() throws IOException
  {
    String url = "https://fcm.googleapis.com/fcm/send";
    String API_ACCESS_KEY="AAAAQ8CY2j0:APA91bHKNT-HNdbxyO-eD671RuQlkZOgMS7VTz66uwtxVr8kDfUDcGCLojY1hgRsXK9IfyE1LmTYarOO6gq_4CThif_6bjgmvh6JIikM28HZTAQs-u7jtEkEUokDHrpWvZ9jsI6Bmfyl";
    String registrationIds="cQL5eOLym-0:APA91bHXAoSpRFN0spvpS4dOpfDc0iisZv2i5NeA50iUCqSlERo4ZKFcCk5XMFD3It3qQD-0oH_FdtAhaCre6sJJQKJ_fRgbwhBmS4o9GBnkgvLowmFQICEu-vFopR6CVI8-PY8ACX_0";

    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

    con.setRequestMethod("POST");
    con.setRequestProperty("Content-Type", "application/json");
    con.setRequestProperty("Authorization", "key="+API_ACCESS_KEY);

    JSONObject msg=new JSONObject();
    msg.put("message","test");

    JSONObject parent=new JSONObject();

    parent.put("to", registrationIds);
    parent.put("data", msg);

    con.setDoOutput(true);
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