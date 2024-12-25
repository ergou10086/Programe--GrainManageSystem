package dao.impl;

import dao.IGrainDAO;
import entity.Grain;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;
import util.DatabaseHelper;

import java.sql.*;
import java.util.*;

// 实现了IGrainDAO接口，用于对数据库中的graindata表进行增删改查操作。
public class GrainDataDAOImpl implements IGrainDAO {

    @Override
    // 添加一个新的粮食信息
    public boolean addGrain(Grain grain) throws DAOException {
        // 构建sql语句
        String sql = "INSERT INTO graindata (id_grain, grain_code, grain_name, grain_type, grain_price, grain_remark, grainShelfLife) " +
                "VALUES (?,?,?,?,?,?,?)";

        try {
            // 生成一个随机的UUID作为id_grain，确保唯一性
            String id = UUID.randomUUID().toString();
            // 执行插入操作，将id_grain作为第一个参数传入，受影响的行数大于0则成功
            return DatabaseHelper.executeUpdate(sql,
                    id,
                    grain.getGrainCode(),
                    grain.getGrainName(),
                    grain.getGrainType(),
                    grain.getGrainPrice(),
                    grain.getGrainRemark(),
                    grain.getGrainShelfLife()) > 0;
        } catch (SQLException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.AddGrainFailedException("添加粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.AddGrainFailedException ex) {
                throw new DAOException(String.valueOf(ex));
            }
        }
    }

    @Override
    // 更新一个已存在的粮食信息
    public boolean updateGrain(Grain grain) throws DAOException {
        // 构建sql语句，将grain_code作为条件
        String sql = "UPDATE graindata SET grain_name =?, grain_type =?, " +
                "grain_price =?, grain_remark =?, grainShelfLife =? WHERE grain_code =? AND is_deleted = 0";
        try {
            return DatabaseHelper.executeUpdate(sql,
                    grain.getGrainName(),
                    grain.getGrainType(),
                    grain.getGrainPrice(),
                    grain.getGrainRemark(),
                    grain.getGrainShelfLife(),
                    grain.getGrainCode()) > 0;
        } catch (SQLException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException("更新粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException ex) {
                throw new DAOException(String.valueOf(ex));
            }
        }
    }


    @Override
    // 软删除一个已存在的粮食信息
    public boolean deleteGrain(String grainCode) throws DAOException {
        // 构建sql语句，将grain_code作为条件
        String sql = "UPDATE graindata SET is_deleted = 1 WHERE grain_code =?";
        try {
            return DatabaseHelper.executeUpdate(sql, grainCode) > 0;
        } catch (SQLException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException("软删除粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException ex) {
                throw new DAOException(String.valueOf(ex));
            }
        }
    }


    // 查找一个已存在的粮食信息，只返回第一个匹配的结果
    @Override
    public Grain findGrainByCode(String grainCode) throws DAOException {
        // 构建sql语句，将grain_code作为条件
        String sql = "SELECT * FROM graindata WHERE grain_code =? AND is_deleted = 0";
        try {
            // 执行查询操作，返回结果集List<Map<String, Object>>包含了多行数据的键值对，其中的键是列名，值是对应的值。
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, grainCode);
            // 如果结果集不为空，则将结果集的第一行数据转换为Grain对象并返回
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                Grain grain = new Grain();
                grain.setIdGrain((String) row.get("id_grain"));
                grain.setGrainCode((String) row.get("grain_code"));
                grain.setGrainName((String) row.get("grain_name"));
                grain.setGrainType((String) row.get("grain_type"));
                grain.setCapacity((String) row.get("grain_capacity"));
                grain.setNotice((String) row.get("grain_notice"));
                grain.setGrainPrice((Double) row.get("grain_price"));
                grain.setGrainRemark((String) row.get("grain_remark"));
                grain.setGrainShelfLife((Double) row.get("grainShelfLife"));
                return grain;
            }
            return null;
        } catch (SQLException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.FindWarehouseFailedException("查询粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.FindWarehouseFailedException ex) {
                throw new DAOException(String.valueOf(ex));
            }
        }
    }


    @Override
    // 获取所有已存在的粮食信息，返回一个List<Grain>对象，其中包含了所有的粮食信息
    public List<Grain> getAllGrains() throws DAOException {
        String sql = "SELECT * FROM graindata WHERE is_deleted = 0";
        try {
            // 执行查询操作，返回结果集List<Map<String, Object>>包含了多行数据的键值对，其中的键是列名，值是对应的值。
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql);
            // 空的List<Grain>对象，用于存储查询到的所有粮食信息
            return getGrains(results);
        } catch (SQLException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.GetAllGrainFailedException("获取所有粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.GetAllGrainFailedException ex) {
                throw new DAOException(String.valueOf(ex));
            }
        }
    }

    private List<Grain> getGrains(List<Map<String, Object>> results) {
        List<Grain> grains = new ArrayList<>();

        for (Map<String, Object> row : results) {
            Grain grain = new Grain();
            grain.setIdGrain((String) row.get("id_grain"));
            grain.setGrainCode((String) row.get("grain_code"));
            grain.setGrainName((String) row.get("grain_name"));
            grain.setNotice((String) row.get("grain_notice"));
            grain.setCapacity((String) row.get("grain_capacity"));
            grain.setGrainType((String) row.get("grain_type"));
            grain.setGrainPrice((Double) row.get("grain_price"));
            grain.setGrainRemark((String) row.get("grain_remark"));
            grain.setGrainShelfLife((Double) row.get("grainShelfLife"));
            grains.add(grain);
        }
        return grains;
    }

    @Override
    // 关键字模糊查询粮食相关的数据记录
    public List<Grain> searchGrains(String keyword) throws DAOException {
        // 使用LIKE关键字进行模糊查询，持按编号和名称搜索，并且只搜索未删除的记录
        String sql = "SELECT * FROM graindata WHERE is_deleted = 0 AND " +
                "(grain_code LIKE ? OR grain_name LIKE ? OR grain_type LIKE ?)";
        // 构建了一个searchPattern字符串，通过在传入的关键字前后添加%符号，形成了符合LIKE操作符进行模糊匹配的模式
        String searchPattern = "%" + keyword + "%";

        try {
            // 两个searchPattern参数，分别对应grain_name和grain_type字段的模糊匹配占位符
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, searchPattern, searchPattern, searchPattern);
            return getGrains(results);
        } catch (SQLException e) {
            try {
                throw new GrainOrWarehouseServiceExceptions.FindWarehouseFailedException("搜索粮食信息失败", e);
            } catch (GrainOrWarehouseServiceExceptions.FindWarehouseFailedException ex) {
                throw new DAOException(String.valueOf(ex));
            }
        }
    }




    @Override
    // 彻底删除数据库中is_deleted为1的字段
    public void deleteGrainsPermanently(String grainName) throws DAOException {
        // 新建立连接
        Connection conn = null;
        try {
            conn = DatabaseHelper.getConnection();
            conn.setAutoCommit(false); // 开启事务
            
            // 执行事务的多表连接查询，获取要删除的记录的 id_grain
            String selectSql = "SELECT id_grain FROM graindata WHERE grain_name = ? AND is_deleted = 1";
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(selectSql, grainName);
            
            if (results.isEmpty()) {
                throw new DAOException("找不到要删除的记录或记录未被标记为删除");
            }
            
            // 获取所有相关的 id_grain
            List<String> idGrains = new ArrayList<>();
            for (Map<String, Object> row : results) {
                idGrains.add((String) row.get("id_grain"));
            }
            
            // 对每个 id_grain 执行删除操作
            for (String idGrain : idGrains) {
                // 先删除历史记录
                String deleteHistorySql = "DELETE FROM grain_change_history WHERE grain_id = ?";
                DatabaseHelper.executeUpdate(conn, deleteHistorySql, idGrain);
                
                // 再删除粮食记录
                String deleteGrainSql = "DELETE FROM graindata WHERE id_grain = ? AND is_deleted = 1";
                DatabaseHelper.executeUpdate(conn, deleteGrainSql, idGrain);
            }
            
            conn.commit(); // 提交事务
        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback(); // 发生错误时回滚事务
                } catch (SQLException ex) {
                    throw new DAOException("回滚事务失败: " + ex.getMessage(), ex);
                }
            }
            throw new DAOException("彻底删除粮食信息失败: " + e.getMessage(), e);
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true); // 恢复自动提交
                    conn.close();
                } catch (SQLException e) {
                    throw new DAOException("关闭数据库连接失败: " + e.getMessage(), e);
                }
            }
        }
    }



    // 恢复被软删除的信息
    @Override
    public void restoreGrainByName(String grainName) throws DAOException {
        String sql = "UPDATE graindata SET is_deleted = 0 WHERE grain_name = ? AND is_deleted = 1";
        try {
            int result = DatabaseHelper.executeUpdate(sql, grainName);
            if (result == 0) {
                throw new DAOException("未找到要恢复的记录或记录未被标记为删除");
            }
        } catch (SQLException e) {
            throw new DAOException("恢复粮食信息失败: " + e.getMessage(), e);
        }
    }




    // 通过名称获取id的方法
    @Override
    public List<String> getIdGrainsByName(String grainName) throws DAOException {
        String sql = "SELECT id_grain FROM graindata WHERE grain_name = ?";
        try {
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, grainName);
            List<String> idList = new ArrayList<>();
            for (Map<String, Object> row : results) {
                idList.add((String) row.get("id_grain"));
            }
            return idList;
        } catch (SQLException e) {
            throw new DAOException("获取id_grain失败: " + e.getMessage(), e);
        }
    }



    // 查询一个粮食记录是否被软删除
    @Override
    public boolean isGrainDeletedCheck(String grainName) throws DAOException {
        String sql = "SELECT is_deleted FROM graindata WHERE grain_name = ?";
        try (Connection connection = DatabaseHelper.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            // 设置SQL语句中的参数（这里对应? 占位符）
            preparedStatement.setString(1, grainName);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // 获取 is_deleted 字段的值
                    int isDeletedValue = resultSet.getInt("is_deleted");
                    return isDeletedValue == 1;
                }
                // 如果没有查询到对应的记录，返回 false，表示未被标记为软删除（按照通常逻辑理解没有记录就不存在软删除情况）
                return false;
            }
        } catch (SQLException e) {
            throw new DAOException("查询软删除情况失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<Grain> search(String key) throws DAOException {
        List<Grain> grain2List2=new ArrayList<>();
        List<Grain> grain2List=getAllGrains();
        for (Grain grain2 :grain2List){
            if(grain2.getGrainCode().contains(key)||grain2.getGrainName().contains(key)||grain2.getGrainType().contains(key)||grain2.getCapacity().contains(key)||grain2.getNotice().contains(key)){
                grain2List2.add(grain2);
            }
        }
        return grain2List2;
    }

    @Override
    public List<Grain> set1(String notice,String code) throws DAOException {
            String sql="update graindata set grain_notice=? where grain_code =?";
            List<Object>parms=new ArrayList<>();
            parms.add(notice);
            parms.add(code);
            DatabaseHelper.save(sql,parms);
            List<Grain> grain2List=getAllGrains();
            return grain2List;
    }

    @Override
    public List<Grain> delete(String code) throws DAOException {
        //String sql="delete from grain2 where grain_id = ?";
        String sql = "UPDATE graindata SET is_deleted = 1 WHERE grain_code =?";
        List<Object> parms=new ArrayList<>();
        parms.add(code);
        DatabaseHelper.save(sql,parms);
        List<Grain> grain2List=getAllGrains();
        return grain2List;
    }
}