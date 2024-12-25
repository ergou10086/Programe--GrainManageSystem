package service.impl;

import dao.IGrainDAO;
import dao.impl.GrainDataDAOImpl;
import entity.Grain;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;
import exceptions.ServiceException;
import service.IGrainInfService;

import java.util.List;

// 实现了 IGrainService 接口，通过DAO层与controller层，用于对粮食信息进行增删改查操作。
public class GrainInfServiceImpl implements IGrainInfService {
    private final IGrainDAO grainDAO;

    // 构造方法，初始化 grainDAO 字段
    public GrainInfServiceImpl() {
        this.grainDAO = new GrainDataDAOImpl();
    }


    // 重写 addGrain 方法
    @Override
    public boolean addGrain(Grain grain) {
        try {
            // 检查是否存在相同编号的粮食
            if (grainDAO.findGrainByCode(grain.getGrainCode()) != null) {
                throw new GrainOrWarehouseServiceExceptions.AddGrainFailedException("该粮食编号已存在！");
            }
            // 调用 DAO 层的方法添加对应粮食信息
            return grainDAO.addGrain(grain);
        } catch (Exception e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.AddGrainFailedException(e);
            } catch (GrainOrWarehouseServiceExceptions.AddGrainFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    //  重写 updateGrain 方法
    @Override
    public boolean updateGrain(Grain grain) {
        try {
            //  调用 DAO 层的方法更新对应粮食信息
            return grainDAO.updateGrain(grain);
        } catch (DAOException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException("更新粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    // 重写 deleteGrain 方法,
    @Override
    public boolean deleteGrain(String grainCode) {
        try {
            // 调用 DAO 层的方法删除对应粮食信息
            return grainDAO.deleteGrain(grainCode);
        } catch (DAOException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException("删除粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    // 重写 findGrainByCode 方法, 调用 DAO 层的方法获取对应粮食信息
    @Override
    public Grain findGrainByCode(String grainCode) {
        try {
            //  调用 DAO 层的方法获取对应粮食信息
            return grainDAO.findGrainByCode(grainCode);
        } catch (DAOException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.FindWarehouseFailedException("查询粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.FindWarehouseFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    //  重写 getAllGrains 方法, 调用 DAO 层的方法获取所有粮食信息
    @Override
    public List<Grain> getAllGrains() {
        try {
            //  调用 DAO 层的方法获取所有粮食信息
            return grainDAO.getAllGrains();
        } catch (DAOException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.GetAllGrainFailedException("获取所有粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.GetAllGrainFailedException ex) {
                try {
                    throw new ServiceException(ex);
                } catch (ServiceException exc) {
                    throw new RuntimeException(exc);
                }
            }
        }
    }



    // 调用grainDAO彻底删除的方法
    @Override
    public void deleteGrainsPermanently(String grainName) {
        try {
            ((GrainDataDAOImpl)grainDAO).deleteGrainsPermanently(grainName);
        } catch (DAOException e) {
            throw new RuntimeException("彻底删除粮食信息失败: " + e.getMessage(), e);
        }
    }



    // 调用grainDAO中恢复还没有被彻底删除的信息
    @Override
    public void restoreGrain(String grainName) {
        try {
            ((GrainDataDAOImpl)grainDAO).restoreGrainByName(grainName);
        } catch (DAOException e) {
            throw new RuntimeException("恢复粮食信息失败: " + e.getMessage(), e);
        }
    }



    // 通过名称获取id的方法
    public List<String> getIdGrainsByName(String grainName) {
        try {
            return ((GrainDataDAOImpl)grainDAO).getIdGrainsByName(grainName);
        } catch (DAOException e) {
            throw new RuntimeException("获取id_grain失败", e);
        }
    }


    // 检查粮食是否被软删除
    @Override
    public boolean isGrainDeletedCheck(String grainName) {
        try {
            return ((GrainDataDAOImpl)grainDAO).isGrainDeletedCheck(grainName);
        }catch (DAOException e) {
            throw new RuntimeException("检查粮食是否被删除失败", e);
        }
    }



    // 恢复被软删除的粮食信息
    @Override
    public void restoreGrainByName(String grainName){
        try {
            ((GrainDataDAOImpl)grainDAO).restoreGrainByName(grainName);
        }catch (DAOException e) {
            throw new RuntimeException("恢复粮食信息失败", e);
        }
    }



    // 粮食信息的模糊查询
    @Override
    public List<Grain> searchGrains(String keyword) {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return grainDAO.getAllGrains();
            }
            return grainDAO.searchGrains(keyword.trim());
        } catch (DAOException e) {
            throw new RuntimeException("搜索粮食信息失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Grain> search1(String key) throws DAOException {
        return grainDAO.search(key);
    }

    @Override
    public List<Grain> set1(String notice, String code)throws DAOException {
        return grainDAO.set1(notice,code);
    }

    @Override
    public List<Grain> delete1(String code) throws DAOException {
        return grainDAO.delete(code);
    }

}