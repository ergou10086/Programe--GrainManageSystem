package dao;

import entity.WareHouse;
import exceptions.DAOException;

import java.util.List;

import entity.WareHouse;
import java.io.File;
import java.util.List;

public interface WarehouseDataDAO {
    List<WareHouse>getdata1() throws DAOException;
    List<WareHouse>getdata2()throws    DAOException;
    List<WareHouse>search(String key)throws DAOException;
    List<WareHouse>search2(String key)throws DAOException;
    List<WareHouse>add1(String code,String name,String snowtime,String temper,String humidity,String problem,String deal,String spectionpeople,String spectiontime)throws DAOException;
    List<WareHouse>delete(String code) throws DAOException;
    WareHouse findBycode(String code)throws DAOException;
    WareHouse set2(String code,String problems,String deals,String chooose,String content) throws DAOException;
    List<WareHouse>set3(String notice,String code)throws DAOException;
    void notice(String code)throws DAOException;
    List<WareHouse>getdata3()throws DAOException;
    WareHouse findBycode2(String code,String problem)throws DAOException;
    void add(WareHouse wareHouse)throws DAOException;
    List<WareHouse>search3(String qidata,String modata) throws DAOException;
    List<WareHouse>delete2(String code)throws DAOException;

    // 添加仓库
    void addWarehouse(WareHouse warehouse) throws Exception;
    // 更新仓库信息
    void updateWarehouse(WareHouse warehouse) throws Exception;
    // 根据仓库编码删除仓库
    void deleteWarehouse(String warehouseCode) throws Exception;
    // 获取所有仓库信息
    List<WareHouse> getAllWarehouses() throws Exception;
    // 搜索仓库
    List<WareHouse> searchWarehouses(String searchTerm) throws Exception;
    // 从文件导入仓库信息
    void importFromFile(File file) throws Exception;
    // 根据仓库编码获取仓库信息
    WareHouse getWarehouseByCode(String warehouseCode) throws Exception;

}
