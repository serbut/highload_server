import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Response {

    final String method;
    final Constants.Codes responseCode;
    final String httpVersion;
    File file;
    String contentType;

    Response(String method, Constants.Codes responseCode, String httpVersion) {
        this.method = method;
        this.responseCode = responseCode;
        this.httpVersion = httpVersion;
    }

    Response(String method, String httpVersion, File file, String extension) {
        this.method = method;
        this.responseCode = Constants.Codes.OK;
        this.httpVersion = httpVersion;
        this.file = file;
        this.contentType = getContentType(extension);
    }

    String buildResponse() {
        final Date currentDateTime = new Date(System.currentTimeMillis());
        final StringBuilder response = new StringBuilder();
        response.append(httpVersion).append(' ').append(responseCode.getCode()).append("\r\n");
        response.append("Date: ").append(new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.US).format(currentDateTime)).append("\r\n");
        response.append("Server: Highload server\r\n");
        if (file != null) {
            response.append("Content-Length: ").append(file.length()).append("\r\n");
            response.append("Content-Type: ").append(contentType).append("\r\n");
        }
        if (responseCode == Constants.Codes.NOT_ALLOWED) {
            response.append("Allow: GET, HEAD");
        }
        response.append("Connection: Close\r\n");
        response.append("\r\n");

        return response.toString();
    }

    void write(OutputStream outputStream) throws IOException {
        outputStream.write(buildResponse().getBytes());
        if (file != null && !method.equals("HEAD")) {
            try (final InputStream fileInputStream = new FileInputStream(file)) {
                final byte [] buffer = new byte[Constants.RESPONSE_BUFFER_SIZE];
                int read;
                while ((read = fileInputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, read);
                }
            }
        }
        outputStream.flush();
        outputStream.close();
    }

    @Nullable
    String getContentType(String extension) {
        switch (extension) {
            case "html":
                return "text/html";
            case "css":
                return "text/css";
            case "js":
                return "text/javascript";
            case "jpg":
            case "jpeg":
                return "image/jpeg";
            case "png":
                return "image/png";
            case "gif":
                return "image/gif";
            case "swf":
                return "application/x-shockwave-flash";
            default:
                return null;
        }
    }
}
