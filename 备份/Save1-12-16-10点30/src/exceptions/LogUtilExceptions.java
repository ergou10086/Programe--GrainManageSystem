package exceptions;

public class LogUtilExceptions extends Exception {
    // 构造方法，难以描述异常
    public LogUtilExceptions(String message) {
        super(message);
    }


    // 日志加载失败或阻断
    public static class LogLoadException extends LogUtilExceptions {
        public LogLoadException(String message) {
            super(message);
        }
    }


    // 删除日志失败
    public static class LogDeleteException extends LogUtilExceptions {
        public LogDeleteException(String message) {
            super(message);
        }
    }
}
