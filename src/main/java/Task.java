import java.io.*;
import java.net.Socket;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Task implements Runnable {

    private InputStream inputStream;
    private OutputStream outputStream;

    Task(Socket clientSocket) {
        try {
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        try {
            final Request request = new Request(inputStream);
            final Response response;
            if (!request.method.equals( "GET") && !request.method.equals( "HEAD")) {
                response = new Response(request.method, Constants.Codes.NOT_ALLOWED, request.httpVersion);
            } else {
                final File file;
                if (request.uri.endsWith("/")) {
                    file = new File(request.uri + "index.html");
                } else {
                    file = new File(request.uri);
                }
                if (request.uri.contains("../") || (request.uri.endsWith("/") && !file.exists() && !request.uri.contains("."))) { // change this
                    response = new Response(request.method, Constants.Codes.FORBIDDEN, request.httpVersion);
                } else if (!file.exists()) {
                    response = new Response(request.method, Constants.Codes.NOT_FOUND, request.httpVersion);
                } else {
                    response = new Response(request.method, Constants.Codes.OK, request.httpVersion, file, request.getFileExtension());
                }
            }
            response.write(outputStream);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
        }
    }
}
