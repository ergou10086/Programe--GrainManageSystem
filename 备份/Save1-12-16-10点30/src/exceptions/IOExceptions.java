package exceptions;

import java.io.IOException;

public class IOExceptions extends Exception{

    public IOExceptions(String message) {}

    // 图片加载异常
    public static class ImageLoadException extends IOException {
        public ImageLoadException(String message){
            super(message);
        }
    }


    // 导出信息失败
    public static class ExportException extends IOException {
        public ExportException(String message){
            super(message);
        }
    }
}
