
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.Socket;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Task implements Runnable {

    private OutputStream outputStream;
    private BufferedReader bufferedReader;
    private String root;

    Task(Socket clientSocket, String root) {
        try {
            final InputStream inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
            this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            this.root = root;
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        final String requestString = getRequestString();
        if (requestString == null) {
            return;
        }
        final Request request = new Request(requestString, root);
        final Response response;
        if (request.isSupportedMethod()) {
            final File file = new File(request.uri);
            if (request.isRootEscaping() || request.isIndexFileRequested && !file.exists()) {
                response = new Response(request.method, Constants.Codes.FORBIDDEN, request.httpVersion);
            } else if (!file.exists()) {
                response = new Response(request.method, Constants.Codes.NOT_FOUND, request.httpVersion);
            } else {
                response = new Response(request.method, request.httpVersion, file, request.getFileExtension());
            }
        } else {
            response = new Response(request.method, Constants.Codes.NOT_ALLOWED, request.httpVersion);
        }
        try {
            response.write(outputStream);
            bufferedReader.close();
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    @Nullable
    private String getRequestString() {
        try {
            final String request = bufferedReader.readLine();
            if (request == null || request.isEmpty()) {
                return null;
            }
            return request;
        } catch (IOException e) {
            System.err.println(e.getLocalizedMessage());
            return null;
        }
    }
}
