package managers.server;

import com.google.gson.Gson;
import org.apiguardian.api.API;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {
    private final URL url;
    private Gson gson;
    private HttpClient client;
    private String apiToken;

    public KVTaskClient(URL url) throws URISyntaxException, IOException, InterruptedException {
        this.gson = new Gson();
        this.url = url;
        client = HttpClient.newHttpClient();
        HttpRequest registrationRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();

        //HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(registrationRequest, HttpResponse.BodyHandlers.ofString());
        this.apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uriLoad = URI.create(url + "/save/"+ key + "?API_TOKEN=" + apiToken);

        HttpRequest saveRequest = HttpRequest.newBuilder()
                .uri(uriLoad)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        //HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        //HttpResponse<String> response = client.send(saveRequest, handler);
        client.send(saveRequest, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uriLoad = URI.create(url + "/load/"+ key + "?API_TOKEN=" + apiToken);

        HttpRequest loadRequest = HttpRequest.newBuilder()
                .uri(uriLoad)
                .GET()
                .build();

        //HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        //HttpResponse<String> response = client.send(loadRequest, handler);

        return client.send(loadRequest, HttpResponse.BodyHandlers.ofString()).body();
        //return client.send(loadRequest, handler).body();
        //return response.body();
    }
}
