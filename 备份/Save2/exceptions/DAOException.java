package exceptions;

import java.sql.SQLException;

// 数据访问对象异常类,处理一些DAO的通用异常
public class DAOException extends Exception {
    // 各种构造函数
    public DAOException(String message) {
        super(message);
    }

    public DAOException(String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(QueryFailedException ex) {
    }

    public DAOException(UpdateFailedException ex) {
    }


    // 查询出现问题异常
    public static class QueryFailedException extends DataBaseExceptions.DataBaseException {
        public QueryFailedException(String message, SQLException e) {
            super(message);
        }

        public QueryFailedException(String message) {
            super(message);
        }
    }

    // 更新失败的未知异常
    public static class UpdateFailedException extends DataBaseExceptions.DataBaseException {
        public UpdateFailedException(String message, SQLException e) {
            super(message);
        }
    }

    // 插入失败的未知异常
    public static class InsertFailedException extends DataBaseExceptions.DataBaseException {
        public InsertFailedException(String message) {
            super(message);
        }
    }

    // 删除失败的未知异常
    public static class DeleteFailedException extends DataBaseExceptions.DataBaseException {
        public DeleteFailedException(String message) {
            super(message);
        }
    }


    // 参数设置到预编译的Statement对象的jdbc异常
    public static class ParameterSettingException extends DataBaseExceptions.DataBaseException {
        public ParameterSettingException(String message) {
            super(message);
        }
    }


    // 结果集转换错误
    public static class ResultSetConversionException extends DataBaseExceptions.DataBaseException {
        public ResultSetConversionException(String message) {
            super(message);
        }
    }
} 