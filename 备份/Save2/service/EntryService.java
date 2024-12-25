package service;

import java.sql.SQLException;
import java.util.Map;

public interface EntryService {
    //处理入库逻辑
    boolean processEntry(Map<String, Object> entryData) throws SQLException;
}