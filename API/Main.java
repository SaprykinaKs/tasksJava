// cкомпилить и запустить с библиотекой gson:
// javac -cp .:gson-2.10.1.jar User.java Todo.java Main.java
// java -cp .:gson-2.10.1.jar Main

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String usersApi = "https://fake-json-api.mock.beeceptor.com/users";
        String todosApi = "https://dummy-json.mock.beeceptor.com/todos";

        try {
            String usersResponse = sendHttp(usersApi);
            List<User> users = parse(usersResponse, new TypeToken<List<User>>() {}.getType());
            System.out.println("Users:");
            users.forEach(System.out::println);

            String todosResponse = sendHttp(todosApi);
            List<Todo> todos = parse(todosResponse, new TypeToken<List<Todo>>() {}.getType());
            System.out.println("\nTodos:");
            todos.forEach(System.out::println);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String sendHttp(String apiUrl) throws Exception {
        URI uri = new URI(apiUrl);
        URL url = uri.toURL();

        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod("GET");
        connect.setRequestProperty("Accept", "application/json");

        BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        return response.toString();
    }

    private static <T> T parse(String jsonResponse, Type type) {
        Gson gson = new Gson();
        return gson.fromJson(jsonResponse, type);
    }
}