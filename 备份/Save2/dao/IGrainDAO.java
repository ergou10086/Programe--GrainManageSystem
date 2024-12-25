package dao;

import entity.Grain;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;

import java.util.List;

// 谷物数据访问对象接口
public interface IGrainDAO {
    /**
     * 添加谷物
     * @param grain 谷物对象
     * @return 添加成功返回true，否则返回false
     * @throws DAOException 如果添加失败
     */
    boolean addGrain(Grain grain) throws DAOException, GrainOrWarehouseServiceExceptions, Exception;


    /**
     * 更新谷物信息
     * @param grain 谷物对象
     * @return 更新成功返回true，否则返回false
     * @throws DAOException 如果更新失败
     */
    boolean updateGrain(Grain grain) throws DAOException;


    /**
     * 删除谷物
     * @param grainCode 谷物编码
     * @return 删除成功返回true，否则返回false
     * @throws DAOException 如果删除失败
     */
    boolean deleteGrain(String grainCode) throws DAOException;


    /**
     * 根据谷物编码查找谷物
     * @param grainCode 谷物编码
     * @return 找到的谷物对象，如果没有找到则返回null
     * @throws DAOException 如果查找失败
     */
    Grain findGrainByCode(String grainCode) throws DAOException;


    /**
     * 获取所有谷物
     * @return 所有谷物对象列表
     * @throws DAOException 如果获取失败
     */
    List<Grain> getAllGrains() throws DAOException;

    /**
     * 根据关键字模糊查询谷物
     * @param keyword 关键字
     * @return 模糊查询结果列表
     * @throws DAOException 如果查询失败
     */
    List<Grain> searchGrains(String keyword) throws DAOException;

    /**
     * 从数据库中彻底删除is_deleted字段为1的数据
     * @return 是否成功删除
     * @throws DAOException
     */
    void deleteGrainsPermanently(String grainName) throws DAOException;


    /**
     * 恢复is_deleted字段为1的数据，置为0
     * @param grainName
     * @throws DAOException
     */
    void restoreGrainByName(String grainName) throws DAOException;


    /**
     * 根据粮食名称获取id的方法
     * @param grainName
     * @return
     * @throws DAOException
     */
    List<String> getIdGrainsByName(String grainName) throws DAOException;



    /**
     * 根据粮食名称判断是否被删除
     * @param grainName
     * @return
     * @throws DAOException
     */
    boolean isGrainDeletedCheck(String grainName) throws DAOException;
    List<Grain> search(String key) throws DAOException;
    List<Grain> set1(String notice,String code)throws DAOException;
    List<Grain> delete(String code)throws DAOException;
}