import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Server {
    private final int port;
    private final Executor executor;
    private final String root;

    Server(int port, int threads, String root) {
        this.port = port;
        executor = Executors.newFixedThreadPool(threads);
        this.root = root;
    }

    public void start() {
        try (final ServerSocket socket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                executor.execute(new Task(socket.accept(), root));
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
