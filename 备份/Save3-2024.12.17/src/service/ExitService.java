package service;

import java.sql.SQLException;
import java.util.Map;

public interface ExitService {
    //处理出库逻辑
    boolean processExit(Map<String, Object> exitData) throws SQLException;
}