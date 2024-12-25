package service;

import java.sql.SQLException;
import java.util.*;

public interface IGrainChangeHistoryService {
    /**
     * 记录变更操作
     * @param grainId
     * @param changeType
     * @param changeDetail
     * @param operator
     * @throws SQLException
     */
    void recordGrainChange(String grainId, String changeType, String changeDetail, String operator) throws SQLException;


    /**
     * 获取指定粮食的历史记录
     * @param grainId
     * @return 记录历史记录的列表
     * @throws SQLException
     */
    List<Map<String, Object>> getGrainHistory(String grainId) throws SQLException;


    /**
     * 获取时间范围内的历史记录
     * @param startTime
     * @param endTime
     * @return 记录历史记录的列表
     * @throws SQLException
     */
    List<Map<String, Object>> getHistoryByTimeRange(String startTime, String endTime) throws SQLException;


    /**
     * 获取指定操作者的历史记录
     * @param operator
     * @return 记录历史记录的列表
     * @throws SQLException
     */
    List<Map<String, Object>> getHistoryByOperator(String operator) throws SQLException;
}
