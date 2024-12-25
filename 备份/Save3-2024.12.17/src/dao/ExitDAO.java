package dao;

import java.sql.SQLException;
import java.util.Map;

/**
 * ExitDAO接口定义了与仓库货物出库相关操作的数据访问方法，涵盖了出库记录添加、仓库库存更新、获取仓库当前重量以及获取仓库存储作物名称等功能。
 */
public interface ExitDAO {

    //添加货物出库记录的方法。
    boolean addExitRecord(Map<String, Object> exitData) throws SQLException;
    //更新仓库库存的方法。
    boolean updateWarehouseStock(String warehouseName, String cropName, double weight) throws SQLException;

    //获取指定仓库当前重量的方法。
    double getWarehouseCurrentWeight(String warehouseName) throws SQLException;

    //获取指定仓库当前存储的作物名称的方法。
    String getWarehouseCropName(String warehouseName) throws SQLException;
}