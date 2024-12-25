package service;

import entity.Grain;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;

import java.util.List;

public interface IGrainInfService {
    /**
     * 添加粮食信息的方法。
     * @param grain 要添加的粮食对象
     * @return 添加操作是否成功
     * @throws GrainOrWarehouseServiceExceptions  添加粮食信息的过程中出现了服务层相关的异常情况，使用 AddGrainFailedException
     */
    boolean addGrain(Grain grain) throws GrainOrWarehouseServiceExceptions;

    /**
     * 更新粮食信息的方法。
     * @param grain  要更新的粮食对象
     * @return  更新操作是否成功
     * @throws GrainOrWarehouseServiceExceptions 更新粮食信息过程中出现服务层相关异常情况，使用 UpdateWarehouseFailedException
     */
    boolean updateGrain(Grain grain) throws GrainOrWarehouseServiceExceptions;

    /**
     * 删除粮食信息的方法。
     * @param grainCode 标识要删除的粮食的编码
     * @return 对应粮食记录是否已从存储中成功删除
     * @throws GrainOrWarehouseServiceExceptions 在删除粮食信息的过程中出现服务层相关的异常情况，使用 DeleteGrainFailedException
     */
    boolean deleteGrain(String grainCode) throws GrainOrWarehouseServiceExceptions;

    /**
     * 根据粮食编码查找特定粮食信息的方法
     * @param grainCode 唯一标识要查找的粮食的编码
     * @return 返回查找到的粮食对象
     * @throws GrainOrWarehouseServiceExceptions 在查找过程中出现服务层相关的异常情况，使用 FindWarehouseFailedException
     */
    Grain findGrainByCode(String grainCode) throws GrainOrWarehouseServiceExceptions;

    /**
     * 获取所有粮食信息的方法
     * @return 返回一个包含所有粮食对象的列表，列表中的每个 Grain 对象代表一种粮食，包含了其各自的属性信息。
     * @throws GrainOrWarehouseServiceExceptions 出现服务层相关的异常,使用 GetAllGrainFailedException
     */
    List<Grain> getAllGrains() throws GrainOrWarehouseServiceExceptions;


    /**
     * 删除粮食信息，彻底删除
     * @throws DAOException
     */
    void deleteGrainsPermanently(String grainName) throws DAOException;


    /**
     * 恢复粮食信息，把is_delete字段为1的字段信息恢复
     * @throws DAOException
     */
    void restoreGrain(String grainName) throws DAOException;


    /**
     * 通过名称获取id
     * @param grainName
     * @return
     */
    List<String> getIdGrainsByName(String grainName);


    /**
     *
     * @param grainName
     * @return
     */
    boolean isGrainDeletedCheck(String grainName);


    /**
     * 根据名称恢复粮食信息
     * @param grainName
     */
    void restoreGrainByName(String grainName);

    /**
     * 根据粮食名称模糊查询粮食信息
     * @param keyword
     * @return
     */
    List<Grain> searchGrains(String keyword);
    List<Grain> search1(String key) throws DAOException;
    List<Grain> set1(String notice,String code)throws DAOException;
    List<Grain> delete1(String code)throws DAOException;
}