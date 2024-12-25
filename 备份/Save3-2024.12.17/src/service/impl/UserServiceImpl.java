package service.impl;

import dao.IUserDAO;
import dao.impl.UserDAOImpl;
import entity.User;
import exceptions.DAOException;
import exceptions.ServiceException;
import exceptions.LoginException;
import service.IUserService;
import util.LogUtil;

import java.util.List;

// UserServiceImpl 类实现了 IUserService 接口，提供了用户登录所需要的各种功能
public class UserServiceImpl implements IUserService {
    // 声明一个用户数据访问对象
    private final IUserDAO userDAO;

    // 构造函数，初始化用户数据访问对象
    public UserServiceImpl() {
        this.userDAO = new UserDAOImpl();
    }


    // 登录方法，根据用户名和密码进行登录验证
    @Override
    public User login(String username, String password) throws ServiceException {
        try {
            System.out.println("尝试登录用户：" + username);
            User user = userDAO.findByUsername(username.trim());
            if (user != null) {
                System.out.println("找到用户：" + user.getUsername());
                if (password.equals(user.getPassword())) {
                    System.out.println("密码验证成功");
                    return user;
                }
                System.out.println("密码验证失败");
            } else {
                System.out.println("未找到用户");
            }
            throw new ServiceException("用户名或密码错误");
        } catch (DAOException e) {
            System.err.println("登录失败：" + e.getMessage());
            e.printStackTrace();
            throw new ServiceException("登录失败: " + e.getMessage(), e);
        }
    }


    // 注册方法，根据用户名、密码和角色进行用户注册
    @Override
    public boolean register(String username, String password, String role) throws ServiceException {
        try {
            // 输入验证
            if (username == null || username.trim().isEmpty()) {
                throw new LoginException.DataEmptyException("用户名不能为空");
            }
            if (password == null || password.trim().isEmpty()) {
                throw new LoginException.DataEmptyException("密码不能为空");
            }
            if (role == null || role.trim().isEmpty()) {
                throw new LoginException.DataEmptyException("角色不能为空");
            }

            // 检查用户名是否已存在
            if (userDAO.isUsernameExists(username.trim())) {
                throw new LoginException.InsertFailedException("用户名已存在");
            }

            // 创建新用户
            User user = new User(username.trim(), password, role.trim());
            boolean result = userDAO.insert(user);
            if(result){
                LogUtil.recordLog(username, "注册", "注册为" + role);
            }
            return result;
        } catch (DAOException e) {
            throw new ServiceException("注册失败: " + e.getMessage(), e);
        } catch (LoginException.DataEmptyException | LoginException.InsertFailedException e) {
            throw new RuntimeException(e);
        }
    }


    // 检查用户名是否存在方法
    @Override
    public boolean isUsernameExists(String username) throws ServiceException {
        try {
            return userDAO.isUsernameExists(username.trim());
        } catch (DAOException e) {
            throw new ServiceException("检查用户名失败: " + e.getMessage(), e);
        }
    }



    // 删除用户信息方法
    @Override
    public boolean deleteUser(String username, String role) throws ServiceException {
        try{
            boolean result = userDAO.deleteUser(username.trim());
            if(result){
                LogUtil.recordLog(username, "注销", role + username + "注销了账号");
            }
            return result;
        }catch (DAOException e){
            throw new ServiceException("删除用户信息失败: " + e.getMessage(), e);
        }
    }



    // 获取所有用户信息的方法
    @Override
    public List<User> getAllUsers() throws ServiceException {
        try {
            return userDAO.getAllUsers();
        } catch (DAOException e) {
            throw new ServiceException("获取用户列表失败: " + e.getMessage(), e);
        }
    }


    // 更新用户名方法
    @Override
    public boolean updateUserInfo(String username, String role, String newUsername, String newRole) throws ServiceException {
        try {
            boolean result = userDAO.updateUserInfo(username, newUsername, newRole);
            if(result){
                LogUtil.recordLog(username, "修改用户信息", role + username + "更改为" + newRole + newUsername);
            }
            return result;
        } catch (DAOException e) {
            throw new ServiceException("更新用户信息失败: " + e.getMessage(), e);
        }
    }


    // 修改密码
    @Override
    public boolean updatePassword(String username, String oldPassword, String newPassword) throws ServiceException {
        try {
            // 先获取用户信息
            User user = userDAO.findByUsername(username.trim());
            if (user == null) {
                throw new ServiceException("用户不存在");
            }
            
            // 验证原密码
            if (!oldPassword.equals(user.getPassword())) {
                throw new ServiceException("原密码错误");
            }

            boolean result = userDAO.updatePassword(username, newPassword);
            if (result) {
                LogUtil.recordLog(username, "修改密码", String.format("用户 %s 修改了密码", username));
            }
            return result;
        } catch (DAOException e) {
            throw new ServiceException("更新密码失败: " + e.getMessage(), e);
        }
    }




    // 查找用户，支持模糊查找
    @Override
    public List<User> searchUsers(String keyword) throws ServiceException {
        try {
            if (keyword == null || keyword.trim().isEmpty()) {
                return userDAO.getAllUsers();
            }
            return userDAO.searchUsers(keyword.trim());
        } catch (DAOException e) {
            throw new ServiceException("搜索用户失败: " + e.getMessage(), e);
        }
    }
} 