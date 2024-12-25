package exceptions;

public class ServiceException extends Exception {
    public ServiceException(String message) {
        super(message);
    }

    public ServiceException(String message, Throwable cause) {super(message, cause);}

    // 其他异常抛出时候的需要的构造函数
    public ServiceException(GrainOrWarehouseServiceExceptions.GetAllGrainFailedException ex) {}

    public ServiceException(LoginException.DataEmptyException e) {
    }

    public ServiceException(LoginException.UserVerifyFailedException e) {
    }


    // 数据加载异常
    public static class ServiceDataLoadExceptions {
        public ServiceDataLoadExceptions(String message) {
            super();
        }
    }
}