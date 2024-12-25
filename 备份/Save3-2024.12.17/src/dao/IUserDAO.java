package dao;

import entity.User;
import exceptions.DAOException;

import java.util.List;

// IUserDAO接口定义了与用户数据访问相关的操作方法。
public interface IUserDAO {
    /**
     * 根据用户名查找对应的用户信息。
     *
     * @param username 要查找的用户的用户名，为一个字符串类型的参数。
     * @return 返回查找到的User对象
     * @throws DAOException 如果在数据访问过程中出现错误（例如数据库连接问题、查询语句执行出错等），则抛出此异常。
     */
    User findByUsername(String username) throws DAOException;


    /**
     * 向数据源（比如数据库）中插入一个新的用户信息。
     *
     * @param user 要插入的用户对象，包含了用户的各种属性信息，如用户名、密码等。
     * @return 如果插入操作成功，则返回true；否则返回false
     * @throws DAOException 如果在数据插入过程中出现错误，则抛出此异常。
     */
    boolean insert(User user) throws DAOException;


    /**
     * 根据传入的用户对象，更新数据源中对应的用户信息。用于日后修改密码和用户名使用
     *
     * @param user 包含更新后信息的用户对象，其关键属性（例如用于定位该用户的主键属性等）用于确定要更新的具体记录。
     * @return 如果更新操作成功，则返回true；否则返回false（例如要更新的记录不存在等情况导致更新失败）。
     * @throws DAOException 如果在数据更新过程中出现错误（例如数据库更新语句执行失败、违反约束等），则抛出此异常。
     */
    boolean update(User user) throws DAOException;



    /**
     * 检查指定的用户名在数据源中是否已经存在。
     *
     * @param username 要检查是否存在的用户名，为一个字符串类型的参数。
     * @return 如果用户名已经存在，则返回true；否则返回false。
     * @throws DAOException 如果在检查过程中出现错误（例如数据库查询语句执行出错等），则抛出此异常。
     */
    boolean isUsernameExists(String username) throws DAOException;



    /**
     * 删除用户操作，危险操作
     * @param username
     * @return
     * @throws DAOException
     */
    boolean deleteUser(String username) throws DAOException;




    /**
     * 获取所有用户信息
     * @return 用户列表
     * @throws DAOException 如果查询失败
     */
    List<User> getAllUsers() throws DAOException;

    /**
     * 更新用户信息
     * @param username 当前用户名
     * @param newUsername 新用户名
     * @param newRole 新角色
     * @return 更新是否成功
     * @throws DAOException 如果更新失败
     */
    boolean updateUserInfo(String username, String newUsername, String newRole) throws DAOException;

    /**
     * 更新用户密码
     * @param username 用户名
     * @param newPassword 新密码
     * @return 更新是否成功
     * @throws DAOException 如果更新失败
     */
    boolean updatePassword(String username, String newPassword) throws DAOException;

    /**
     * 验证用户密码
     * @param username 用户名
     * @param password 密码
     * @return 密码是否正确
     * @throws DAOException 如果验证失败
     */
    boolean validatePassword(String username, String password) throws DAOException;



    /**
     * 根据关键字搜索用户
     * @param keyword 搜索关键字
     * @return 匹配的用户列表
     * @throws DAOException 如果搜索失败
     */
    List<User> searchUsers(String keyword) throws DAOException;
}