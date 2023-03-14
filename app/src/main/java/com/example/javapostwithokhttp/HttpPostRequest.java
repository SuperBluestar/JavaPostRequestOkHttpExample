package com.example.javapostwithokhttp;

import java.io.*;
import java.net.*;

public class HttpPostRequest {

    public interface HttpPostCallback {
        void onSuccess(String response);
        void onError(Exception e);
    }

    public static void sendHttpPostRequest(String url, String urlParameters, final HttpPostCallback callback) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL obj = new URL(url);
                    HttpURLConnection con = (HttpURLConnection) obj.openConnection();
                    con.setRequestMethod("POST");
                    con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    con.setDoOutput(true);
                    con.setDoInput(true);

                    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                    wr.writeBytes(urlParameters);
                    wr.flush();
                    wr.close();

                    int responseCode = con.getResponseCode();

                    BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    callback.onSuccess(response.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    callback.onError(e);
                }
            }
        });

        thread.start();
    }
}
