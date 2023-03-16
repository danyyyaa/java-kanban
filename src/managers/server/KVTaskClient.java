package managers.server;

import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;

public class KVTaskClient {
    private final URL url;
    private final HttpClient client;

    public HttpClient getClient() {
        return client;
    }

    public String getApiToken() {
        return apiToken;
    }

    private final String apiToken;

    public KVTaskClient(URL url) throws IOException, InterruptedException {
        this.url = url;
        client = HttpClient.newHttpClient();
        HttpRequest registrationRequest = HttpRequest.newBuilder()
                .uri(URI.create(url + "/register"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(registrationRequest, HttpResponse.BodyHandlers.ofString());
        this.apiToken = response.body();
    }

    public void put(String key, String json) throws IOException, InterruptedException {
        URI uriSave = URI.create(url + "/save/"+ key + "?API_TOKEN=" + apiToken);

        HttpRequest saveRequest = HttpRequest.newBuilder()
                .uri(uriSave)
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.send(saveRequest, HttpResponse.BodyHandlers.ofString());
    }

    public String load(String key) throws IOException, InterruptedException {
        URI uriLoad = URI.create(url + "/load/"+ key + "?API_TOKEN=" + apiToken);

        HttpRequest loadRequest = HttpRequest.newBuilder()
                .uri(uriLoad)
                .GET()
                .build();

        return client.send(loadRequest, HttpResponse.BodyHandlers.ofString()).body();
    }
}
