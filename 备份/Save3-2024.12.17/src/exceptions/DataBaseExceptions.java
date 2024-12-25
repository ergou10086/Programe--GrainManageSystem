package exceptions;

import java.sql.SQLException;

public class DataBaseExceptions extends Exception{
    // 数据库本身错误各种异常
    public  static class DataBaseException extends SQLException {
        public DataBaseException(String message) {
            super(message);
        }
    }



    // 数据库连接失败异常
    public static class ConnectionFailedException extends DataBaseException {
        public ConnectionFailedException(String message) {
            super(message);
        }
    }



    // 数据库关闭失败异常
    public static class CloseFailedException extends DataBaseException {
        public CloseFailedException(String message) {
            super(message);
        }
    }



    // 数据库驱动加载失败
    public static class DriverLoadFailedException extends DataBaseException {
        public DriverLoadFailedException(String message) {
            super(message);
        }
    }


    // 数据库修改出现异常
    public static class UpdateFailedException extends SQLException {
        public UpdateFailedException(String message) {
            super(message);
        }
    }



}
