package dao.impl;

import dao.IUserDAO;
import entity.User;
import exceptions.DAOException;
import java.sql.*;
import util.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// 用户数据访问对象实现类，用于对数据库中的用户数据进行增删改查操作
public class UserDAOImpl implements IUserDAO {

    // 根据用户名查找用户信息，返回User对象
    @Override
    public User findByUsername(String username) throws DAOException {
        String sql = "SELECT * FROM userdata WHERE username = ?";
        try {
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, username);
            if (!results.isEmpty()) {
                Map<String, Object> row = results.get(0);
                User user = new User();
                user.setUserId(((Number) row.get("user_id")).intValue());
                user.setUsername((String) row.get("username"));
                user.setPassword((String) row.get("password"));
                user.setRole((String) row.get("role"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            System.err.println("查询用户失败：" + e.getMessage());
            e.printStackTrace();
            throw new DAOException("查询用户信息失败", e);
        }
    }

    // 新增用户信息，用于注册使用
    @Override
    public boolean insert(User user) throws DAOException {
        String sql = "INSERT INTO userdata (username, password, role) VALUES (?, ?, ?)";
        try {
            System.out.println("正在插入用户：" + user.getUsername());
            int result = DatabaseHelper.executeUpdate(sql, 
                user.getUsername(),
                user.getPassword(),
                user.getRole());
            System.out.println("插入结果：" + result);
            return result > 0;
        } catch (SQLException e) {
            if (e.getErrorCode() == 1062) { // MySQL唯一键冲突错误码
                throw new DAOException("用户名已存在", e);
            }
            throw new DAOException("创建用户失败", e);
        }
    }

    // 更新用户信息，用于修改密码等操作
    @Override
    public boolean update(User user) throws DAOException {
        // 构建SQL语句，更新用户信息
        String sql = "UPDATE userdata SET password = ?, role = ? WHERE user_id = ?";
        try {
            return DatabaseHelper.executeUpdate(sql,
                user.getPassword(),
                user.getRole(),
                user.getUserId()) > 0;
        } catch (SQLException e) {
            try {
                throw new DAOException.UpdateFailedException("更新用户信息失败", e);
            } catch (DAOException.UpdateFailedException ex) {
                throw new DAOException(ex);
            }
        }
    }

    // 根据用户ID检查用户是否存在
    @Override
    public boolean isUsernameExists(String username) throws DAOException {
        // 构建SQL语句，检查用户名是否存在
        String sql = "SELECT COUNT(*) as count FROM userdata WHERE username = ?";
        try {
            // 执行查询操作，返回结果集
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, username);
            if (!results.isEmpty()) {
                // 从结果集中获取用户数量，判断是否存在该用户
                return ((Number) results.get(0).get("count")).intValue() > 0;
            }
            return false;
        } catch (SQLException e) {
            throw new DAOException("检查用户名是否存在失败", e);
        }
    }




    // 删除用户操作，危险操作
    @Override
    public boolean deleteUser(String username) throws DAOException {
        try {
            // 先检查用户是否存在
            if (isUsernameExists(username)) {
                String sql = "DELETE FROM userdata WHERE username = ?";
                int result = DatabaseHelper.executeUpdate(sql, username);
                return result > 0;
            }
            return false;
        } catch (SQLException e) {
            System.err.println("删除用户失败：" + e.getMessage());
            e.printStackTrace();
            throw new DAOException("删除用户信息失败", e);
        }
    }




    // 获取所有用户信息的方法
    @Override
    public List<User> getAllUsers() throws DAOException {
        String sql = "SELECT * FROM userdata";
        try{
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql);
            List<User> users = new ArrayList<>();
            for(Map<String, Object> row : results){
                User user = new User();
                user.setUserId(((Number) row.get("user_id")).intValue());
                user.setUsername((String) row.get("username"));
                user.setPassword((String) row.get("password"));
                user.setRole((String) row.get("role"));
                users.add(user);
            }
            return users;
        }catch (SQLException e){
            throw new DAOException("获取用户列表失败", e);
        }
    }


    // 更新用户信息
    @Override
    public boolean updateUserInfo(String username, String newUsername, String newRole) throws DAOException {
        String sql = "UPDATE userdata SET username = ?, role = ? WHERE username = ?";
        try {
            return DatabaseHelper.executeUpdate(sql, newUsername, newRole, username) > 0;
        } catch (SQLException e) {
            throw new DAOException("更新用户信息失败", e);
        }
    }


    // 更新用户密码
    @Override
    public boolean updatePassword(String username, String newPassword) throws DAOException {
        String sql = "UPDATE userdata SET password = ? WHERE username = ?";
        try {
            int result = DatabaseHelper.executeUpdate(sql, newPassword, username);
            return result > 0;
        } catch (SQLException e) {
            throw new DAOException("更新密码失败", e);
        }
    }



    // 验证密码
    @Override
    public boolean validatePassword(String username, String password) throws DAOException {
        try {
            User user = findByUsername(username);
            if (user != null) {
                return password.equals(user.getPassword());
            }
            return false;
        } catch (Exception e) {
            throw new DAOException("验证密码失败", e);
        }
    }



    // 模糊搜索用户
    @Override
    public List<User> searchUsers(String keyword) throws DAOException {
        String sql = "SELECT * FROM userdata WHERE username LIKE ? OR role LIKE ?";
        String searchPattern = "%" + keyword + "%";

        try {
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, searchPattern, searchPattern);
            List<User> users = new ArrayList<>();

            for (Map<String, Object> row : results) {
                User user = new User();
                user.setUserId(((Number) row.get("user_id")).intValue());
                user.setUsername((String) row.get("username"));
                user.setPassword((String) row.get("password"));
                user.setRole((String) row.get("role"));
                users.add(user);
            }

            return users;
        } catch (SQLException e) {
            throw new DAOException("搜索用户失败", e);
        }
    }
} 