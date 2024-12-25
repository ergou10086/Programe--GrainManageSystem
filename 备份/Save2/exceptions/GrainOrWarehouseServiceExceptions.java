package exceptions;

import java.sql.SQLException;

public class GrainOrWarehouseServiceExceptions extends Exception{
    public GrainOrWarehouseServiceExceptions(){}
    public GrainOrWarehouseServiceExceptions(String message){
        super(message);
    }

    // 粮食或粮库信息格式出现错误
    public static class GrainOrWarehouseFormatException extends Exception{
        public GrainOrWarehouseFormatException(String message, Throwable cause){
            super(message, cause);
        }
    }

    // 粮食或粮库信息不存在
    public static class GrainOrWarehouseNotFoundException extends Exception{
        public GrainOrWarehouseNotFoundException(String message){
            super(message);
        }
    }

    // 加载粮食信息失败
    public static class LoadGrainFailedException extends Exception{
        public LoadGrainFailedException(String message){
            super(message);
        }
    }

    // 添加粮食信息失败
    public static class AddGrainFailedException extends Exception{
        public AddGrainFailedException(String message, ServiceException e){
            super(message);
        }

        public AddGrainFailedException(Exception e) {
        }

        public AddGrainFailedException(String 添加粮食信息失败, SQLException e) {
        }

        public AddGrainFailedException(String s) {
        }
    }


    // 更新粮食信息失败
    public static class UpdateWarehouseFailedException extends Exception{
        public UpdateWarehouseFailedException(String message, DAOException e){
            super(message, e);
        }

        public UpdateWarehouseFailedException(String 更新粮食信息失败, SQLException e) {}

        public UpdateWarehouseFailedException(String message) {
            super(message);
        }
    }


    // 软删除粮食信息失败
    public static class SoftDeleteGrainFailedException extends Exception{
        public SoftDeleteGrainFailedException(String message, ServiceException e){
            super(message, e);
        }

        public SoftDeleteGrainFailedException(String 软删除粮食信息失败, DAOException e) {
        }

        public SoftDeleteGrainFailedException(String 软删除粮食信息失败, SQLException e) {
        }
    }


    // 查找粮食信息失败
    public static class FindWarehouseFailedException extends Exception{
        public FindWarehouseFailedException(String message, ServiceException e){
            super(message, e);
        }

        public FindWarehouseFailedException(String 查询粮食信息失败, DAOException e) {}

        public FindWarehouseFailedException(String 查询粮食信息失败, SQLException e) {
        }
    }
    
    // 返回所有粮食信息出现失败
    public static class GetAllGrainFailedException extends Exception{
        public GetAllGrainFailedException(String message, RuntimeException e){
            super(message, e);
        }

        public GetAllGrainFailedException(String 获取所有粮食信息失败, DAOException e) {
        }

        public GetAllGrainFailedException(String 获取所有粮食信息失败, SQLException e) {
        }
    }

    //彻底删除粮食记录异常
    public static class DeleteGrainFailedException extends Exception{
        public DeleteGrainFailedException(String message, ServiceException e){
            super(message, e);
        }
        public DeleteGrainFailedException(String message) {
            super(message);
        }
    }


    // 导入信息失败
    public static class ImportGrainFailedException extends Exception{
        public ImportGrainFailedException(String message, ServiceException e){
            super(message, e);
        }
        public ImportGrainFailedException(String message) {
            super(message);
        }
    }


    // 导出信息失败
    public static class ExportGrainFailedException extends Exception{
        public ExportGrainFailedException(String message, ServiceException e){
            super(message, e);
        }
        public ExportGrainFailedException(String message) {
            super(message);
        }
    }


    // 搜索出现非正常情况
    public static class SearchGrainFailedException extends Exception{
        public SearchGrainFailedException(String message, ServiceException e){
            super(message, e);
        }
        public SearchGrainFailedException(String message) {
            super(message);
        }
    }

    // 恢复粮食信息异常
    public static class RecoverGrainFailedException extends Exception{
        public RecoverGrainFailedException(String message, ServiceException e){
            super(message, e);
        }
        public RecoverGrainFailedException(String message) {
            super(message);
        }
    }
}
