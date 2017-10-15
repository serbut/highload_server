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

    Server() {
        this.port = Constants.PORT;
        executor = Executors.newFixedThreadPool(Constants.THREADS);
    }

    public void start() {
        try (final ServerSocket socket = new ServerSocket(port)) {
            //noinspection InfiniteLoopStatement
            while (true) {
                executor.execute(new Task(socket.accept()));
            }
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
        }
    }
}
