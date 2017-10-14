import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;

/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Request {

    final String method;
    String uri;
    final String httpVersion;

    Request(InputStream inputStream) {
        final String request = getInputString(inputStream);
        final StringTokenizer stringTokenizer = new StringTokenizer(request, " \r\n");
        method = stringTokenizer.nextToken();
        uri = stringTokenizer.nextToken();
        try {
            uri = URLDecoder.decode(uri, Constants.CHARSET);
        } catch (UnsupportedEncodingException e) {
            System.out.println(e.getLocalizedMessage());
        }
        httpVersion = stringTokenizer.nextToken();
        uri = "ROOT" + uri;

        cutAnchor();
        cutParameters();
    }

    String getInputString(InputStream is) {
        try(final ByteArrayOutputStream result = new ByteArrayOutputStream()) {
            final byte[] buffer = new byte[Constants.INPUT_BUFFER_SIZE];
            final int length = is.read(buffer);
            result.write(buffer, 0, length);
            return result.toString(Constants.CHARSET);
        } catch (IOException e) {
            System.out.println(e.getLocalizedMessage());
            return "";
        }
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
}
