package managers.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import managers.task.TaskManager;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8080;
    private static final Gson gson = new Gson();
    private final HttpServer httpServer;
    private static FileBackedTasksManager fileManager;

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler(new FileBackedTasksManager()));
        fileManager = new FileBackedTasksManager();
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT);
    }

    public static class TasksHandler implements HttpHandler {
        private TaskManager taskManager;
        public TasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
        }
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

            switch (endpoint) {
                case GET_ALL_TASKS:
                    handleGetAllTasks(exchange);
                    break;
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
                if (pathParts.length == 3 && pathParts[2].equals("task")) {
                    System.out.println("GET_ALL_TASKS");
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

        if (pathParts.length == 3 && pathParts[2].equals("tasks") && requestMethod.equals("GET")) {
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
        }
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
        System.out.println("fileManager.getListAllTasks() = " + fileManager.getListAllTasks());
        List<Task> bytes = fileManager.getListAllTasks();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);


        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*private static void handleGetAllTasks(HttpExchange exchange) throws IOException {
        List<Task> bytes = fileManager.getListAllTasks();
        byte[] response = gson.toJson(bytes).getBytes();

        exchange.sendResponseHeaders(200, 0);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(response);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }*/

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
