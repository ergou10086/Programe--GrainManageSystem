package service;

import entity.WareHouse;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;

import java.io.File;
import java.util.List;

public interface IWarehouseInfService {
     List<WareHouse> getdata1() throws DAOException;
     List<WareHouse> search1(String key)throws DAOException;
     List<WareHouse> add1(String code,String name,String snowtime,String temper,String humidity,String problem,String deal,String spectionpeople,String spectiontime)throws DAOException;
     List<WareHouse>search2(String key)throws DAOException;
     List<WareHouse> delete1(String code) throws DAOException;
     WareHouse findBycode(String code)throws DAOException;
     WareHouse set2(String code,String problems,String deals,String chooose,String content) throws DAOException;
     List<WareHouse>getdata2()throws DAOException;
     List<WareHouse>set3(String notice,String code)throws DAOException;
     void notice(String code)throws DAOException;
     List<WareHouse> getdata3()throws DAOException;
     WareHouse findbycode(String code,String problem)throws DAOException;
     void add2(WareHouse wareHouse)throws DAOException;
     List<WareHouse> search3(String qidata,String modata)throws DAOException;
     List<WareHouse> delete2(String code)throws DAOException;
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
