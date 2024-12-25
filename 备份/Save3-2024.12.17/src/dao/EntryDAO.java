package dao;

import java.sql.SQLException;
import java.util.Map;

public interface EntryDAO {

    //添加入库记录的方法。
    boolean addEntryRecord(Map<String, Object> entryData) throws SQLException;

    //更新仓库库存的方法。
    boolean updateWarehouseStock(String warehouseName, String cropName, double weight) throws SQLException;

    //更新仓库库存的方法2。
    boolean updateWarehouseStockTwo(String warehouseName, String cropName, double weight) throws SQLException;

    //获取指定仓库当前重量的方法。
    double getWarehouseCurrentWeight(String warehouseName) throws SQLException;

    //获取指定仓库当前存储的作物名称的方法。
    String getWarehouseCropName(String warehouseName) throws SQLException;
}
