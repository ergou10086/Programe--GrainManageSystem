package exceptions;

public class RegisterExceptions extends Exception{
    // 用户名已存在异常
    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {

            super(message);
        }
    }


    // 无效密码异常
    public static class InvalidPasswordException extends RuntimeException {
        public InvalidPasswordException(String message) {

            super(message);
        }
    }


    // 确认密码和密码不一样异常
    public static class ConfirmErrorPasswordException extends RuntimeException {
        public ConfirmErrorPasswordException(String message) {

            super(message);
        }
    }


    // 其他注册异常
    public static class OtherRegisterExceptions extends RuntimeException {
        public OtherRegisterExceptions(String message) {
            super(message);
        }
    }


    // 查询用户信息异常
    public static class QueryUserException extends RuntimeException {
        public QueryUserException(String message) {
            super(message);
        }
    }

    // 修改账号信息失败
    public static class UpdateUserException extends RuntimeException {
        public UpdateUserException(String message) {
            super(message);
        }
    }
}
