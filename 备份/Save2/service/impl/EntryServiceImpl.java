package service.impl;

import dao.EntryDAO;
import dao.impl.EntryDAOimpl;
import service.EntryService;

import java.sql.SQLException;
import java.util.Map;

public class EntryServiceImpl implements EntryService {
    private EntryDAO entryDAO = new EntryDAOimpl();
    
    @Override
    public boolean processEntry(Map<String, Object> entryData) throws SQLException {
        String warehouseName = (String) entryData.get("warehouse");
        String cropName = (String) entryData.get("cropName");
        double weight = Double.parseDouble((String) entryData.get("grossWeight"));
        try {
            // 更新仓库库存
            if (!entryDAO.updateWarehouseStock(warehouseName, cropName, weight)) {
                return false;
            }
            
            // 添加入库记录
            return entryDAO.addEntryRecord(entryData);
        } catch (SQLException e) {
            throw e;
        }
    }
}
