package exceptions;

import java.sql.SQLException;

// 登录情况的自定义异常类，继承自 Exception
public class LoginException extends Exception {
    // 默认构造函数
    public LoginException() {
        super("登录出现错误");
    }

    // 带消息的构造函数
    public LoginException(String message) {
        super(message);
    }

    // 带消息和原因的构造函数
    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }

    // 插入用户数据异常
    public static class InsertFailedException extends SQLException {
        public InsertFailedException(String message) {
            super(message);
        }
    }


    // 填写数据不能为空异常
    public static class DataEmptyException extends Exception {
        public DataEmptyException(String message) {
            super(message);
        }
    }


    // 接口处发生的用户验证失败异常
    public static class UserVerifyFailedException extends Exception {
        public UserVerifyFailedException(String message) {
            super(message);
        }
    }


    // 切换账号异常
    public static class SwitchAccountException extends Exception {
        public SwitchAccountException(String message) {
            super(message);
        }
    }

    // 注销账号异常
    public static class LogoutException extends Exception {
        public LogoutException(String message) {
            super(message);
        }
    }
}
