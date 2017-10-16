import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Main {
    public static void main(String[] args) {
        int port = Constants.DEFAULT_PORT;
        int threads = Constants.THREADS;
        String root = Constants.DEFAULT_ROOT;

        try {
            final Properties props = new Properties();
            props.load(new FileInputStream(new File(Constants.CONFIG_PATH)));
            final String portString = props.getProperty("listen");
            final String cpuString = props.getProperty("cpu_limit");
            final String rootString = props.getProperty("document_root");
            port = Integer.valueOf(parseAgrument(portString));
            threads = Integer.valueOf(parseAgrument(cpuString));
            root = parseAgrument(rootString);
        } catch (IOException e) {
            System.out.println("Error reading config file!");
        }

        final Server server = new Server(port, threads, root);
        System.out.println("Server started!");
        System.out.println("Port: " + port);
        System.out.println("Threads: " + threads);
        System.out.println("Root: " + root);
        server.start();
    }

    private static String parseAgrument(String rawString) {
        String result = rawString;
        final int spaceIndex = rawString.indexOf(' ');
        final int commentIndex = rawString.indexOf('#');
        if (spaceIndex != -1) {
            result = rawString.substring(0, spaceIndex);
        } else if (commentIndex != -1) {
            result = rawString.substring(0, commentIndex);
        }
        return result;
    }
}
