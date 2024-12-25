package exceptions;

public class GetHistoryExceptions extends Exception {

    // 获取历史记录失败异常
    public static class GetHistoryFailedException extends Exception {
        public GetHistoryFailedException(String message) {
            super(message);
        }
    }


    // 统计历史记录情况失败
    public static class CountHistoryFailedException extends Exception {
        public CountHistoryFailedException(String message) {
            super(message);
        }
    }


    // 删除历史记录失败或者被拒绝
    public static class DeleteHistoryFailedException extends Exception {
        public DeleteHistoryFailedException(String message) {
            super(message);
        }
    }

    // 加载历史记录失败或者被拒绝
    public static class LoadHistoryFailedException extends Exception {
        public LoadHistoryFailedException(String message) {
            super(message);
        }
    }
}
