package managers.server;

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
    HttpClient client;
    private String apiToken;
    //private static final int PORT = KVServer.PORT;

    public KVTaskClient(URL url) throws URISyntaxException, IOException, InterruptedException {
        this.url = url;
        client = HttpClient.newHttpClient();
        HttpRequest registrationRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();
        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(registrationRequest, handler);
        this.apiToken = response.body();
    }

    public void put(String key, String json) {

    }

    public String load(String key) throws IOException, InterruptedException {
        URI uriLoad = URI.create(url + "/load/"+ key + "?API_TOKEN=" + apiToken);

        HttpRequest loadRequest = HttpRequest.newBuilder()
                .uri(uriLoad)
                .GET()
                .build();
        System.out.println(uriLoad);

        HttpResponse.BodyHandler<String> handler = HttpResponse.BodyHandlers.ofString();
        HttpResponse<String> response = client.send(loadRequest, handler);

        return response.body();
        //return null;
    }
}
