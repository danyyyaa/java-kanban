package managers.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import managers.file.FileBackedTasksManager;
import managers.task.TaskManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    static FileBackedTasksManager fileManager;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    private static final int PORT = 8080;
    private static final Gson gson = new Gson();
    private HttpServer httpServer;
    private TaskManager taskManager;

    /*private static final Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) -> {
        Instant instant = Instant.ofEpochMilli(json.getAsJsonPrimitive().getAsLong());
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }).create();*/


    /*public HttpTaskServer() throws IOException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler(new FileBackedTasksManager()));
        httpServer.start();
    }*/

    public HttpTaskServer() {
        try {
            httpServer = HttpServer.create();
            httpServer.bind(new InetSocketAddress(PORT), 0);
            httpServer.createContext("/tasks", new TasksHandler());
            httpServer.start();

            System.out.println("HTTP-сервер запущен на " + PORT + " порту!\n");
        } catch (IOException e) {
            System.out.println("Ошибка создания сервера.");
        }
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT);
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_TASKS:
                    handleGetAllTasks(exchange);
                    System.out.println("getAllTasks");
                    break;
                case GET_ALL_SUBTASKS:
                    handleGetAllSubtasks(exchange);
                    System.out.println("getAllSubtasks");
                    break;
                case GET_ALL_EPICS:
                    handleGetAllEpics(exchange);
                    System.out.println("getAllEpics");
                    break;
                case GET_TASK:
                    System.out.println("getTask");
                    break;
                case GET_SUBTASK:
                    System.out.println("getSubtask");
                    break;
                case GET_EPIC:
                    System.out.println("getEpic");
                    break;
                case GET_HISTORY:
                    handleGetHistory(exchange);
                    System.out.println("getHistory");
                case GET_PRIORITIZED_TASK:
                    handleGetPrioritizedTasks(exchange);
                    System.out.println("getPrioritizedTasks");
                    break;
                default:
                    writeResponse(exchange, "Такого эндпоинта не существует", 404);
            }

        }
    }

    private static Endpoint getEndpoint(String requestPath, String requestMethod) {
        String[] pathParts = requestPath.split("[/?]");

        /*System.out.println(requestPath);
        for (int i = 0; i < pathParts.length; i++) {
            System.out.println(i + " " + pathParts[i]);
        }*/

        switch (requestMethod) {
            case "GET":
                if (pathParts.length == 3 && pathParts[2].equals("tasks")) {
                    return Endpoint.GET_ALL_TASKS;
                }

                if (pathParts.length == 3 && pathParts[2].equals("subtasks")) {
                    return Endpoint.GET_ALL_SUBTASKS;
                }

                if (pathParts.length == 3 && pathParts[2].equals("epics")) {
                    return Endpoint.GET_ALL_EPICS;
                }

                if (pathParts.length == 3 && pathParts[2].equals("history")) {
                    return Endpoint.GET_HISTORY;
                }

                if (pathParts.length == 3 && pathParts[2].equals("prioritizedtasks")) {
                    return Endpoint.GET_PRIORITIZED_TASK;
                }
                break;
            case "POST":

                break;
            case "DELETE":

                break;
        }

        /*if (pathParts.length == 3 && pathParts[2].equals("tasks") && requestMethod.equals("GET")) {
            return Endpoint.GET_ALL_TASKS;
        }

        if (pathParts.length == 3 && pathParts[2].equals("subtasks") && requestMethod.equals("GET")) {
            return Endpoint.GET_ALL_SUBTASKS;
        }

        if (pathParts.length == 3 && pathParts[2].equals("epics") && requestMethod.equals("GET")) {
            return Endpoint.GET_ALL_EPICS;
        }

        if (pathParts.length == 3 && pathParts[2].equals("history") && requestMethod.equals("GET")) {
            return Endpoint.GET_HISTORY;
        }

        if (pathParts.length == 3 && pathParts[2].equals("prioritizedtasks") && requestMethod.equals("GET")) {
            return Endpoint.GET_PRIORITIZED_TASK;
        }
*/

        return Endpoint.UNKNOWN;
    }

    private static void handleGetPrioritizedTasks(HttpExchange exchange) throws IOException {
        List<Task> bytes = fileManager.getPrioritizedTasks();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        } /*finally {
            exchange.close();
        }*/
    }

    private static void handleGetHistory(HttpExchange exchange) throws IOException {
        List<Task> bytes = fileManager.getHistory();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> bytes = fileManager.getListAllTasks();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetAllSubtasks(HttpExchange exchange) throws IOException {
        List<Subtask> bytes = fileManager.getListAllSubtasks();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleGetAllEpics(HttpExchange exchange) throws IOException {
        List<Epic> bytes = fileManager.getListAllEpics();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private static void writeResponse(HttpExchange exchange,
                                      String responseString,
                                      int responseCode) throws IOException {
        if (responseString.isBlank()) {
            exchange.sendResponseHeaders(responseCode, 0);
        } else {
            byte[] bytes = responseString.getBytes(DEFAULT_CHARSET);
            exchange.sendResponseHeaders(responseCode, bytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        exchange.close();
    }

    protected void sendText(HttpExchange exchange, String text) throws IOException {
        byte[] resp = text.getBytes(UTF_8);
        exchange.getResponseHeaders().add("Content-Type", "application/json");
        exchange.sendResponseHeaders(200, resp.length);
        exchange.getResponseBody().write(resp);
    }

    enum Endpoint {
        GET_ALL_TASKS, // done
        GET_ALL_SUBTASKS, //
        GET_ALL_EPICS, //
        GET_TASK,
        GET_SUBTASK,
        GET_EPIC,
        GET_HISTORY,
        GET_PRIORITIZED_TASK,
        UNKNOWN
    }
}
