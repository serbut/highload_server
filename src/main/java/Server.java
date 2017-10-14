import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Server {
    private final int port;
    private Executor executor;

    Server(int port, int threads) {
        this.port = port;
        executor = Executors.newFixedThreadPool(threads);
    }
    public void start() {
        try (final ServerSocket socket = new ServerSocket(port)) {
            while (true) {
                executor.execute(new Task(socket.accept()));
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
