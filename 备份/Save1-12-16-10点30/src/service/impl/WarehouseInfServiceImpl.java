package service.impl;

import dao.WarehouseDataDAO;
import dao.impl.WarehouseDataDAOImpl;
import entity.WareHouse;
import exceptions.DAOException;
import service.IWarehouseInfService;
import exceptions.GrainOrWarehouseServiceExceptions;
import java.io.File;
import java.sql.SQLException;
import java.util.List;



import java.util.List;

public class WarehouseInfServiceImpl implements IWarehouseInfService {
    private WarehouseDataDAO warehouseDataDAO;
    public WarehouseInfServiceImpl(){
        this.warehouseDataDAO=new WarehouseDataDAOImpl();
    }
    @Override
    public List<WareHouse> getdata1()throws DAOException {
        return warehouseDataDAO.getdata1();
    }

    @Override
    public List<WareHouse> search1(String key) throws DAOException {
        return warehouseDataDAO.search(key);
    }

    @Override
    public List<WareHouse> add1(String code, String name, String snowtime, String temper, String humidity, String problem, String deal, String spectionpeople, String spectiontime) throws DAOException {
        return warehouseDataDAO.add1(code,name,snowtime,temper,humidity,problem,deal,spectionpeople,spectiontime);
    }

    @Override
    public List<WareHouse> search2(String key) throws DAOException {
        return warehouseDataDAO.search2(key);
    }

    @Override
    public List<WareHouse> delete1(String code) throws DAOException {
        return warehouseDataDAO.delete(code);
    }

    @Override
    public WareHouse findBycode(String code) throws DAOException {
        return warehouseDataDAO.findBycode(code);
    }

    @Override
    public WareHouse set2(String code, String problems, String deals, String chooose, String content) throws DAOException {
        return warehouseDataDAO.set2( code, problems, deals, chooose, content);
    }

    @Override
    public List<WareHouse> getdata2() throws DAOException {
        return warehouseDataDAO.getdata2();
    }

    @Override
    public List<WareHouse> set3(String notice, String code) throws DAOException {
        return warehouseDataDAO.set3(notice,code);
    }

    @Override
    public void notice(String code) throws DAOException {
        warehouseDataDAO.notice(code);
    }

    @Override
    public List<WareHouse> getdata3() throws DAOException {
        return warehouseDataDAO.getdata3();
    }

    @Override
    public WareHouse findbycode(String code, String problem) throws DAOException {
        return warehouseDataDAO.findBycode2(code,problem);
    }

    @Override
    public void add2(WareHouse wareHouse) throws DAOException {
        warehouseDataDAO.add(wareHouse);
    }

    @Override
    public List<WareHouse> search3(String qidata, String modata) throws DAOException {
        return warehouseDataDAO.search3(qidata,modata);
    }

    @Override
    public void addWarehouse(WareHouse warehouse) throws GrainOrWarehouseServiceExceptions.AddGrainFailedException {
        try {
            if (warehouse == null || warehouse.getWarehouseCode() == null || warehouse.getWarehouseName() == null) {
                throw new GrainOrWarehouseServiceExceptions.AddGrainFailedException("仓库信息不完整");
            }
            warehouseDataDAO.addWarehouse(warehouse);
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.AddGrainFailedException("添加仓库信息失败: " + e.getMessage());
        }
    }

    @Override
    public void updateWarehouse(WareHouse warehouse) throws GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException {
        try {
            if (warehouse == null || warehouse.getWarehouseCode() == null || warehouse.getWarehouseName() == null) {
                throw new GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException("仓库信息不完整");
            }
            warehouseDataDAO.updateWarehouse(warehouse);
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException("更新仓库信息失败: " + e.getMessage());
        }
    }

    @Override
    public void deleteWarehouse(String warehouseCode) throws GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException {
        try {
            if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
                throw new GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException("仓库编码不能为空", new SQLException());
            }
            warehouseDataDAO.deleteWarehouse(warehouseCode);
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException("删除仓库信息失败", new SQLException(e.getMessage()));
        }
    }

    @Override
    public List<WareHouse> getAllWarehouses() throws GrainOrWarehouseServiceExceptions.GetAllGrainFailedException {
        try {
            return warehouseDataDAO.getAllWarehouses();
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.GetAllGrainFailedException("获取所有仓库信息失败", new SQLException(e.getMessage()));
        }
    }

    @Override
    public List<WareHouse> searchWarehouses(String searchTerm) throws GrainOrWarehouseServiceExceptions.FindWarehouseFailedException {
        try {
            if (searchTerm == null || searchTerm.trim().isEmpty()) {
                return getAllWarehouses();
            }
            return warehouseDataDAO.searchWarehouses(searchTerm);
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.FindWarehouseFailedException("查询仓库信息失败", new SQLException(e.getMessage()));
        }
    }

    @Override
    public void importFromFile(File file) throws GrainOrWarehouseServiceExceptions.ImportGrainFailedException {
        try {
            if (file == null || !file.exists()) {
                throw new GrainOrWarehouseServiceExceptions.ImportGrainFailedException("文件不存在");
            }
            warehouseDataDAO.importFromFile(file);
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.ImportGrainFailedException("导入仓库信息失败: " + e.getMessage());
        }
    }

    @Override
    public WareHouse getWarehouseByCode(String warehouseCode) throws GrainOrWarehouseServiceExceptions.FindWarehouseFailedException {
        try {
            if (warehouseCode == null || warehouseCode.trim().isEmpty()) {
                throw new GrainOrWarehouseServiceExceptions.FindWarehouseFailedException("仓库编码不能为空", new SQLException());
            }
            return warehouseDataDAO.getWarehouseByCode(warehouseCode);
        } catch (Exception e) {
            throw new GrainOrWarehouseServiceExceptions.FindWarehouseFailedException("获取仓库信息失败: " ,new SQLException(e.getMessage()));
        }
    }
}
