package grababouquet.com.android.http;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Sivly on 1/20/2015.
 */
public class HttpManager {

    public static String getData(RequestPackage p) {

        BufferedReader reader = null;
        String uri = p.getUri();
        if (p.getMethod().equals("GET")) {
            uri += "?" + p.getEncodedParams();
        }

        Log.d("HttpManager", uri);

        try {
            URL url = new URL(uri);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod(p.getMethod());

            if (p.getMethod().equals("POST")) {
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write(p.getEncodedParams());
                writer.flush();
            }

            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }

    }
}
