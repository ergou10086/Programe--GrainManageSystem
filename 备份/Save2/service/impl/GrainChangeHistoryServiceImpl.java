package service.impl;

import dao.impl.GrainChangeHistoryDAOimpl;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import service.IGrainChangeHistoryService;

public class GrainChangeHistoryServiceImpl implements IGrainChangeHistoryService {
    private final GrainChangeHistoryDAOimpl grainChangeHistoryDAO;

    // 构造方法，初始化grainChangeHistoryDAO的实现
    public GrainChangeHistoryServiceImpl() {
        this.grainChangeHistoryDAO = new GrainChangeHistoryDAOimpl();
    }


    // 记录变更
    public void recordGrainChange(String grainId, String changeType, String changeDetail, String operator) throws SQLException {
        grainChangeHistoryDAO.recordChange(grainId, changeType, changeDetail, operator);
    }



    // 获取指定粮食的历史记录
    public List<Map<String, Object>> getGrainHistory(String grainId) throws SQLException {
        return grainChangeHistoryDAO.getChangeHistory(grainId);
    }


    // 获取时间范围内的历史记录
    public List<Map<String, Object>> getHistoryByTimeRange(String startTime, String endTime) throws SQLException {
        return grainChangeHistoryDAO.getChangeHistoryByTimeRange(startTime, endTime);
    }

    // 获取指定操作者的历史记录
    public List<Map<String, Object>> getHistoryByOperator(String operator) throws SQLException {
        return grainChangeHistoryDAO.getChangeHistoryByOperator(operator);
    }

    // 删除指定时间的历史记录的方法,带调试日志
    public boolean deleteHistoryRecordByTime(String changeTime) throws SQLException {
        try {
            System.out.println("Service层正在删除记录，时间：" + changeTime);
            int result = grainChangeHistoryDAO.deleteOldRecords(changeTime);
            return result > 0;
        } catch (SQLException e) {
            System.err.println("删除历史记录失败：" + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
