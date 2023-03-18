package managers.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import managers.file.ManagerSaveException;
import managers.task.TaskManager;
import managers.util.Managers;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.time.format.DateTimeFormatter;

public class HttpTaskServer {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final int PORT = 8080;
    private final HttpServer httpServer;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm, dd.MM.yy");
    static String path = "resources/data.csv";

    public HttpTaskServer() throws IOException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        try {
            httpServer.createContext("/tasks/task", new TasksHandler(Managers.FileManager(Path.of(path))));
            httpServer.createContext("/tasks/subtask", new SubtasksHandler(Managers.FileManager(Path.of(path))));
            httpServer.createContext("/tasks/epic", new EpicsHandler(Managers.FileManager(Path.of(path))));
            httpServer.createContext("/tasks/history", new HistoryHandler(Managers.FileManager(Path.of(path))));
            httpServer.createContext("/tasks", new PrioritizedTasksHandler(Managers.FileManager(Path.of(path))));
        } catch (ManagerSaveException e) {
            throw new RuntimeException(e);
        }
    }

    static class PrioritizedTasksHandler implements HttpHandler {
        private final TaskManager taskManager;
        private final Gson gson;

        public PrioritizedTasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
            this.gson = new Gson();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                if (taskManager.getPrioritizedTasks().isEmpty()) {
                    writeResponse(exchange, "", 204);
                    return;
                }

                writeResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);


            } else {
                writeResponse(exchange, "", 400);
            }
        }
    }

    static class HistoryHandler implements HttpHandler {
        private final TaskManager taskManager;
        private final Gson gson;

        public HistoryHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
            this.gson = new Gson();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                if (taskManager.getHistory().isEmpty()) {
                    writeResponse(exchange, "", 204);
                    return;
                }

                writeResponse(exchange, gson.toJson(taskManager.getHistory()), 200);
            } else {
                writeResponse(exchange, "", 400);
            }
        }
    }

    static class EpicsHandler implements HttpHandler {
        private final TaskManager taskManager;
        private final Gson gson;

        public EpicsHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
            this.gson = new Gson();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getEpic(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, gson.toJson(taskManager.getEpic(id)), 200);
                } else {
                    if (taskManager.getListAllEpics().isEmpty()) {
                        writeResponse(exchange, "", 204);
                        return;
                    }
                    writeResponse(exchange, gson.toJson(taskManager.getListAllEpics()), 200);
                }
            } else if ("DELETE".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getEpic(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, "", 204);
                    taskManager.removeEpicById(id);
                } else {
                    if (taskManager.getListAllEpics().isEmpty()) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, "", 204);
                    taskManager.deleteAllEpics();
                }
            } else if ("POST".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getEpic(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Epic epic = gson.fromJson(body, Epic.class);
                    taskManager.updateEpic(id, epic);
                    writeResponse(exchange, "", 201);
                } else {
                    System.out.println(new String(exchange.getRequestBody().readAllBytes()));
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Epic epic = gson.fromJson(body, Epic.class);
                    String duration = epic.getStringDuration().substring(2, epic.getStringDuration().length() - 1);

                    writeResponse(exchange, "", 201);
                    taskManager.createEpic(epic.getName(), epic.getDescription(), epic.getStatus(),
                            epic.getStartTime().format(DATE_TIME_FORMATTER), duration);
                }
            }
        }
    }

    static class SubtasksHandler implements HttpHandler {
        private final TaskManager taskManager;
        private final Gson gson;

        public SubtasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
            this.gson = new Gson();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getSubtask(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, gson.toJson(taskManager.getSubtask(id)), 200);
                } else {
                    if (taskManager.getListAllSubtasks().isEmpty()) {
                        writeResponse(exchange, "", 204);
                        return;
                    }
                    writeResponse(exchange, gson.toJson(taskManager.getListAllSubtasks()), 200);
                }
            } else if ("DELETE".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getSubtask(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, "", 204);
                    taskManager.removeSubtaskById(id);
                } else {
                    if (taskManager.getListAllSubtasks().isEmpty()) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, "", 204);
                    taskManager.deleteAllSubtasks();
                }
            } else if ("POST".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getSubtask(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    taskManager.updateSubtask(id, subtask);
                    writeResponse(exchange, "", 201);
                } else {
                    System.out.println(new String(exchange.getRequestBody().readAllBytes()));
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Subtask subtask = gson.fromJson(body, Subtask.class);
                    String duration = subtask.getStringDuration().substring(2, subtask.getStringDuration().length() - 1);

                    writeResponse(exchange, "", 201);
                    taskManager.createSubtask(subtask.getName(), subtask.getDescription(), subtask.getStatus(), subtask.getEpicId(),
                            subtask.getStartTime().format(DATE_TIME_FORMATTER), duration);
                }
            }
        }
    }

    static class TasksHandler implements HttpHandler {
        private final TaskManager taskManager;
        private final Gson gson;

        public TasksHandler(TaskManager taskManager) {
            this.taskManager = taskManager;
            this.gson = new Gson();
        }

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestMethod = exchange.getRequestMethod();

            if ("GET".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getTask(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, gson.toJson(taskManager.getTask(id)), 200);
                } else {
                    if (taskManager.getListAllTasks().isEmpty()) {
                        writeResponse(exchange, "", 204);
                        return;
                    }
                    writeResponse(exchange, gson.toJson(taskManager.getListAllTasks()), 200);
                }
            } else if ("DELETE".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getTask(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, "", 204);
                    taskManager.removeTaskById(id);
                } else {
                    if (taskManager.getListAllTasks().isEmpty()) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    writeResponse(exchange, "", 204);
                    taskManager.deleteAllTasks();
                }
            } else if ("POST".equals(requestMethod)) {
                if (exchange.getRequestURI().toString().contains("?id=")) {
                    String[] path = exchange.getRequestURI().toString().split("/");
                    int id = Integer.parseInt(path[path.length - 1].substring(4));

                    if (taskManager.getTask(id) == null) {
                        writeResponse(exchange, "", 404);
                        return;
                    }

                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Task task = gson.fromJson(body, Task.class);
                    taskManager.updateTask(id, task);
                    writeResponse(exchange, "", 201);
                } else {
                    System.out.println(new String(exchange.getRequestBody().readAllBytes()));
                    String body = new String(exchange.getRequestBody().readAllBytes());
                    Task task = gson.fromJson(body, Task.class);
                    String duration = task.getStringDuration().substring(2, task.getStringDuration().length() - 1);

                    writeResponse(exchange, "", 201);
                    taskManager.createTask(task.getName(), task.getDescription(), task.getStatus(),
                            task.getStartTime().format(DATE_TIME_FORMATTER), duration);
                }
            }
        }
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        httpServer.start();
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановлен сервер на порту " + PORT);
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
}
