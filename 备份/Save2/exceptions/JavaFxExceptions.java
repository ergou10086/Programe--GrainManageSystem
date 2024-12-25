package exceptions;

public class JavaFxExceptions extends Exception{
    // 构造方法，难以描述异常
    public JavaFxExceptions(String message) {
        super(message);
    }
    public JavaFxExceptions(String message, Throwable cause) {
        super(message, cause);
    }


    public static class webViewPanelError extends JavaFxExceptions {
        public webViewPanelError(String message) {
            super(message);
        }
    }

    public static class webViewInitError extends JavaFxExceptions{
        public webViewInitError(String message) {
            super(message);
        }
    }
}
