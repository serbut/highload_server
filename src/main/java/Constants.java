/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Constants {
    static final int PORT = 80;
    static final int THREADS = Runtime.getRuntime().availableProcessors();
    public enum Codes {
        @SuppressWarnings("EnumeratedConstantNamingConvention")
        OK("200 OK"),
        FORBIDDEN("403 Forbidden"),
        NOT_FOUND("404 Not Found"),
        NOT_ALLOWED("405 Method Not Allowed");

        private final String code;
        Codes(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static final int RESPONSE_BUFFER_SIZE = 512 * 1024;
    public static final String CHARSET = "UTF-8";
}
