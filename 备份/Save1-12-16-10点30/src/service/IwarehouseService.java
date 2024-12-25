package service;

import entity.WareHouse;
import exceptions.GrainOrWarehouseServiceExceptions;
import java.io.File;
import java.util.List;

public interface IwarehouseService {
    // 添加仓库
    void addWarehouse(WareHouse warehouse) throws GrainOrWarehouseServiceExceptions.AddGrainFailedException;
    
    // 更新仓库信息
    void updateWarehouse(WareHouse warehouse) throws GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException;
    
    // 删除仓库
    void deleteWarehouse(String warehouseCode) throws GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException;
    
    // 获取所有仓库
    List<WareHouse> getAllWarehouses() throws GrainOrWarehouseServiceExceptions.GetAllGrainFailedException;
    
    // 搜索仓库
    List<WareHouse> searchWarehouses(String searchTerm) throws GrainOrWarehouseServiceExceptions.FindWarehouseFailedException;
    
    // 导入仓库信息
    void importFromFile(File file) throws GrainOrWarehouseServiceExceptions.ImportGrainFailedException;
    
    // 根据编码获取仓库
    WareHouse getWarehouseByCode(String warehouseCode) throws GrainOrWarehouseServiceExceptions.FindWarehouseFailedException;
}
