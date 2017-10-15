import java.io.*;
import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Request {

    final String method;
    final String httpVersion;
    final boolean isIndexFileRequested;
    String uri;

    Request(String request) {
        final StringTokenizer stringTokenizer = new StringTokenizer(request, " \r\n");
        method = stringTokenizer.nextToken();
        uri = stringTokenizer.nextToken();
        try {
            uri = URLDecoder.decode(uri, Constants.CHARSET);
        } catch (UnsupportedEncodingException e) {
            System.err.println(e.getLocalizedMessage());
        }
        httpVersion = stringTokenizer.nextToken();
        uri = "/var/www/html" + uri;

        cutAnchor();
        cutParameters();

        isIndexFileRequested = uri.endsWith("/") && !uri.contains(".");
        updateDirectoryUri();

        System.out.println(uri);
    }

    String getFileExtension() {
        return uri.substring(uri.lastIndexOf('.') + 1, uri.length());
    }

    private void cutAnchor() {
        final int anchorIndex = uri.indexOf('#');
        if (anchorIndex != -1) uri = uri.substring(0, anchorIndex);
    }

    private void cutParameters() {
        final int parameterIndex = uri.indexOf('?');
        if (parameterIndex != -1) uri = uri.substring(0, parameterIndex);
    }

    private void updateDirectoryUri() {
        if (uri.endsWith("/")) {
            uri += "index.html";
        }
    }

    boolean isSupportedMethod() {
        return method.equals("GET") || method.equals("HEAD");
    }

    boolean isRootEscaping() {
        return uri.contains("../");
    }
}
