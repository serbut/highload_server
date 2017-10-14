/**
 * Created by sergeybutorin on 13/10/2017.
 */
public class Constants {
    public enum Codes {
        OK("200 OK"),
        FORBIDDEN("403 Forbidden"),
        NOT_FOUND("404 Not Found"),
        NOT_ALLOWED("405 Method Not Allowed");

        private String code;
        Codes(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }
    }

    public static final int INPUT_BUFFER_SIZE = 1024;
    public static final int RESPONSE_BUFFER_SIZE = 512 * 1024;
    public static final String CHARSET = "UTF-8";
}
