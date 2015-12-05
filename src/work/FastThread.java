package work;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FastThread {
    public static void main(String args[]) throws IOException {
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 1; i++) {
            executorService.submit(new HTTPClient("http://localhost:9377?date=2015-11-27&uuid=eba6d836-7784-4e8b-b222-6751251170f4"));
        }
    }
}

class HTTPClient implements Runnable {
    private final String url;
    private final boolean useGET = true;

    HTTPClient(String url) {
        this.url = url;
    }

    public void run() {
        if (!useGET) {
            makePostRequest();
        } else {
            try {
                makeGetRequest();
            } catch (IOException e) {
                System.out.println(e);
            }
        }
    }

    private void  makeGetRequest() throws IOException {
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(0);
        String response = null;
        if (con.getResponseCode() == 200) {
            response = getResponseAsString(con);
        }
        HttpResponse httpResponse = new HttpResponse(response, con.getResponseCode());
        System.out.println(new Gson().toJson(httpResponse, HttpResponse.class));
    }

    private void makePostRequest() {
        try {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("guid", "");
            hashMap.put("mobile", "+919920533241");
            hashMap.put("update", "false");
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            System.out.println("sent");
            String response = null;
            BasicHttpEntityEnclosingRequest request = new BasicHttpEntityEnclosingRequest("POST", url);
            request.setEntity(new StringEntity(new Gson().toJson(hashMap)));
            if (con.getResponseCode() == 200) {
                response = getResponseAsString(con);
            }
            final HttpResponse httpResponse = new HttpResponse(response, con.getResponseCode());
            System.out.println(url + "\n" + new Gson().toJson(httpResponse));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getResponseAsString(HttpURLConnection con) throws IOException {
        final InputStream inputStream = con.getInputStream();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(inputStream));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return new String(response);
    }

    public static class ClothoCallStatusResponse {
        @SerializedName("end_time")
        private Long endTime;
        @SerializedName("talk_start")
        private Long talkStart;
        @SerializedName("call_start")
        private Long callStart;
        @SerializedName("duration")
        private Double duration;
        @SerializedName("billed_time")
        private Double billedTime;

        public Double getDuration() {
            return duration;
        }

        public Double getBilledTime() {
            return billedTime;
        }

        public Long getEndTime() {
            return endTime;
        }

        public Long getTalkStart() {
            return talkStart;
        }

        @Override
        public String toString() {
            return "ClothoCallStatusResponse{" +
                    "endTime=" + endTime +
                    ", talkStart=" + talkStart +
                    ", callStart=" + callStart +
                    ", duration=" + duration +
                    ", billedTime=" + billedTime +
                    '}';
        }

        public Long getCallStart() {
            return callStart;
        }
    }

    public static class HttpResponse {
        private String responseBody;
        private int statusCode;

        public HttpResponse(String responseBody, int statusCode) {
            this.responseBody = responseBody;
            this.statusCode = statusCode;
        }

        public String getResponseBody() {
            return responseBody;
        }

        public int getStatusCode() {
            return statusCode;
        }
    }
}