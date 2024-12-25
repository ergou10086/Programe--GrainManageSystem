package dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface IGrainChangeHistoryDAO {

    /**
     * 记录粮食信息变更历史
     *
     * @param grainId      粮食ID
     * @param changeType   变更类型（出库/入库/售完/补充）
     * @param changeDetail 变更详情
     * @param operator     操作人
     */
    void recordChange(String grainId, String changeType, String changeDetail, String operator) throws SQLException;


    /**
     * 获取指定粮食的所有变更历史记录
     *
     * @param grainId 粮食ID
     * @return 变更历史记录列表
     */
    List<Map<String, Object>> getChangeHistory(String grainId) throws SQLException;

    /**
     * 获取指定时间段内的变更历史记录
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 变更历史记录列表
     */
    List<Map<String, Object>> getChangeHistoryByTimeRange(String startTime, String endTime) throws SQLException;


    /**
     * 获取指定操作者的历史信息
     *
     * @param operator
     * @return 变更历史记录列表
     * @throws SQLException
     */
    List<Map<String, Object>> getChangeHistoryByOperator(String operator) throws SQLException;


    /**
     * 获取指定变更类型的历史记录
     *
     * @param changeType
     * @return
     * @throws SQLException
     */
    List<Map<String, Object>> getChangeHistoryByType(String changeType) throws SQLException;


    /**
     * 获取指定日期内变更的历史记录
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws SQLException
     */
    Map<String, Integer> getChangeTypeStatistics(String startTime, String endTime) throws SQLException;


    /**
     * 删除指定日期之前的历史记录
     *
     * @param beforeTime
     * @return
     * @throws SQLException
     */
    int deleteOldRecords(String beforeTime) throws SQLException;


}


