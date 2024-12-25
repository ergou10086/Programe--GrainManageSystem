package service.impl;

import dao.ExitDAO;
import dao.impl.ExitDAOimpl;
import service.ExitService;

import java.sql.SQLException;
import java.util.Map;

public class ExitServiceImpl implements ExitService {
    private ExitDAO exitDAO = new ExitDAOimpl();
    
    @Override
    public boolean processExit(Map<String, Object> exitData) throws SQLException {
        String warehouseName = (String) exitData.get("warehouse");
        String cropName = (String) exitData.get("cropName");
        double weight = Double.parseDouble((String) exitData.get("weight"));

        try {
            // 更新仓库库存
            if (!exitDAO.updateWarehouseStock(warehouseName, cropName, weight)) {
                return false;
            }
            
            // 添加出库记录
            return exitDAO.addExitRecord(exitData);
        } catch (SQLException e) {
            throw e;
        }
    }
}
