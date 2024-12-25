package service;

import entity.User;
import exceptions.ServiceException;

import java.util.List;

// 定义用户服务接口，提供用户登录、注册和检查用户名是否存在的功能的接口
public interface IUserService {
    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return 登录成功返回用户对象，否则返回null
     * @throws ServiceException 如果登录失败
     */
    User login(String username, String password) throws ServiceException;


    /**
     * 用户注册
     * @param username 用户名
     * @param password 密码
     * @param role     用户角色
     * @return 注册成功返回true，否则返回false
     * @throws ServiceException 如果注册失败
     */
    boolean register(String username, String password, String role) throws ServiceException;


    /**
     * 检查用户名是否存在
     * @param username 用户名
     * @return 用户名存在返回true，否则返回false
     * @throws ServiceException 如果查询失败
     */
    boolean isUsernameExists(String username) throws ServiceException;



    /**
     * 删除用户信息方法
     * @param username
     * @return
     * @throws ServiceException
     */
    boolean deleteUser(String username, String role) throws ServiceException;


    /**
     * 获取所有用户信息
     * @return 用户列表
     * @throws ServiceException 如果查询失败
     */
    List<User> getAllUsers() throws ServiceException;

    /**
     * 更新用户信息
     * @param username 当前用户名
     * @param newUsername 新用户名
     * @param newRole 新角色
     * @return 更新是否成功
     * @throws ServiceException 如果更新失败
     */
    boolean updateUserInfo(String username, String role, String newUsername, String newRole) throws ServiceException;

    /**
     * 更新用户密码
     * @param username 用户名
     * @param oldPassword 原密码
     * @param newPassword 新密码
     * @return 更新是否成功
     * @throws ServiceException 如果更新失败
     */
    boolean updatePassword(String username, String oldPassword, String newPassword) throws ServiceException;



    /**
     * 搜索用户
     * @param keyword 搜索关键字
     * @return 匹配的用户列表
     * @throws ServiceException 如果搜索失败
     */
    List<User> searchUsers(String keyword) throws ServiceException;
}