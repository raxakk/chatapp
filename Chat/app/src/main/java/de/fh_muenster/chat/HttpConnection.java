package de.fh_muenster.chat;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;

public class HttpConnection {

    private final String USER_AGENT = "Mozilla/5.0";
    private final String SERVER = "http://10.60.70.2";
    private final String SERVER1 = "10.60.70.2";
    private static final String TAG = HttpConnection.class.getName();

    // HTTP GET request (Wird für diesen Server nicht benötigt)
    public String sendGet(String param_url) throws Exception {

        String url = SERVER + param_url;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            return "";
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        return response.toString();
    }

    /**
     * HTTP POST Request
     * @param param_url
     * @param param_body
     * @return
     * @throws Exception
     */
    public String sendPost(String param_url, String param_body) throws Exception {

        String body = param_body;
        String url = SERVER + param_url;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        //add request header
        con.setRequestMethod( "POST" );
        con.setDoInput(true);
        con.setDoOutput(true);
        con.setUseCaches(false);
        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty( "Content-Length", String.valueOf(body.length()) );
        Log.d(TAG, "SEND HTTP POST TO: " + url);

        // Send post request
        OutputStreamWriter writer = new OutputStreamWriter( con.getOutputStream() );
        writer.write( body );
        writer.flush();

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            return "";
        }

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        writer.close();
        in.close();

        return response.toString();
    }

    /**
     * Custom HTTP GET Request (mit Body)
     * @param param_url
     * @param value
     * @return
     * @throws IOException
     */
    public String sendGetWithBody(String param_url, String value) throws IOException {
        String modifiedSentence;
        Socket clientSocket = new Socket(SERVER1, 80);
        DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
        BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String content = value;
        outToServer.writeBytes("GET " + param_url + " HTTP/1.1" + "\n" +
                        "Host: " + SERVER1 + "\n" +
                        "Content-Length: " + content.length() + "\n" +
                        "\n" +
                        content
        );
        Log.d(TAG, "SEND HTTP GET TO: " + SERVER1 + param_url);
        StringBuffer response = new StringBuffer();

        while ((modifiedSentence = inFromServer.readLine()) != null) {
            if(modifiedSentence.startsWith("[") || modifiedSentence.startsWith("{")) {
                response.append(modifiedSentence);
            }
        }

        inFromServer.close();

        clientSocket.close();

        return response.toString();
    }
}
