package dao.impl;

import dao.WarehouseDataDAO;
import entity.WareHouse;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;
import util.DatabaseHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class WarehouseDataDAOImpl implements WarehouseDataDAO {

    @Override
    public List<WareHouse> getdata1() throws DAOException {
        String sql = "SELECT * FROM inspection WHERE is_deleted = 0";
        try {
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql);
            return getwarehouses(results);
        } catch (SQLException e) {
            throw new DAOException("获取巡检数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<WareHouse> getdata2() throws DAOException {
        String sql = "SELECT * FROM warehouse WHERE is_deleted=0";
        try {
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql);
            return getwarehouse2(results);
        } catch (SQLException e) {
            throw new DAOException("获取仓库数据失败: " + e.getMessage());
        }
    }

    @Override
    public List<WareHouse> search(String key) throws DAOException {
        List<WareHouse> wareHouseList = new ArrayList<>();
        List<WareHouse> wareHouseList1 = getdata1();
        for (WareHouse wareHouse : wareHouseList1) {
            if (wareHouse.getWarehouseCode().contains(key) || wareHouse.getWarehouseName().contains(key) ||
                wareHouse.getTemper().contains(key) || wareHouse.getHumidity().contains(key) ||
                wareHouse.getProblem().contains(key) || wareHouse.getSnowtime().contains(key) ||
                wareHouse.getDeal().contains(key) || wareHouse.getSpectionpeople().contains(key) ||
                wareHouse.getSpectiontime().contains(key)) {
                wareHouseList.add(wareHouse);
            }
        }
        return wareHouseList;
    }

    @Override
    public List<WareHouse> search2(String key) throws DAOException {
        List<WareHouse> wareHouseList = new ArrayList<>();
        List<WareHouse> wareHouseList1 = getdata2();
        for (WareHouse wareHouse : wareHouseList1) {
            if (wareHouse.getWarehouseCode().contains(key) || wareHouse.getWarehouseName().contains(key) ||
                wareHouse.getId().contains(key) || wareHouse.getWarehouseType().contains(key) ||
                wareHouse.getWarehouseLocation().contains(key) || wareHouse.getNotice().contains(key) ||
                wareHouse.getStatus().contains(key)) {
                wareHouseList.add(wareHouse);
            }
        }
        return wareHouseList;
    }

    @Override
    public List<WareHouse> add1(String code, String name, String snowtime, String temper, String humidity,
                                String problem, String deal, String spectionpeople, String spectiontime) throws DAOException {
        String sql = "INSERT INTO inspection (warehouse_id, warehouse_code, warehouse_name, warehouse_snowtime, " +
                     "warehouse_temper, warehouse_humidity, warehouse_problem, warehouse_deal, " +
                     "warehouse_spectionpeople, warehouse_spectiontime) VALUES (?,?,?,?,?,?,?,?,?,?)";
        String id = UUID.randomUUID().toString();
        List<Object> parms = new ArrayList<>();
        parms.add(id);
        parms.add(code);
        parms.add(name);
        parms.add(snowtime);
        parms.add(temper);
        parms.add(humidity);
        parms.add(problem);
        parms.add(deal);
        parms.add(spectionpeople);
        parms.add(spectiontime);
        DatabaseHelper.save(sql, parms);
        return getdata1();
    }

    @Override
    public List<WareHouse> delete(String code) throws DAOException {
        String sql = "UPDATE inspection SET is_deleted = 1 WHERE warehouse_code = ?";
        List<Object> parms = new ArrayList<>();
        parms.add(code);
        DatabaseHelper.save(sql, parms);
        return getdata1();
    }

    @Override
    public WareHouse findBycode(String code) throws DAOException {
        String sql = "SELECT * FROM inspection WHERE warehouse_code = ?";
        List<Object> parm = new ArrayList<>();
        parm.add(code);
        List<String> stringList = new ArrayList<>();
        stringList.add("warehouse_problem");
        stringList.add("warehouse_deal");
        stringList.add("warehouse_spectiontime");
        stringList.add("warehouse_problem2");
        stringList.add("warehouse_deal2");
        stringList.add("warehouse_problem3");
        stringList.add("warehouse_deal3");
        List<Map<String, Object>> results = DatabaseHelper.getResult(sql, parm, stringList);
        return getrow2(results);
    }

    @Override
    public WareHouse set2(String code, String problems, String deals, String choose, String content) throws DAOException {
        String sql = "UPDATE inspection SET " + choose + " = ? WHERE warehouse_code = ?";
        String sql2 = "UPDATE inspection SET " + deals + " = ? WHERE warehouse_code = ?";
        List<Object> parms = new ArrayList<>();
        List<Object> parms2 = new ArrayList<>();
        parms.add(problems);
        parms.add(code);
        parms2.add(content);
        parms2.add(code);
        DatabaseHelper.save(sql, parms);
        DatabaseHelper.save(sql2, parms2);
        return findBycode(code);
    }

    @Override
    public List<WareHouse> set3(String notice, String code) throws DAOException {
        String sql = "UPDATE warehouse SET warehouse_notice = ? WHERE warehouse_code = ?";
        List<Object> parms = new ArrayList<>();
        parms.add(notice);
        parms.add(code);
        DatabaseHelper.save(sql, parms);
        return getdata2();
    }

    @Override
    public void notice(String code) throws DAOException {
        String sql = "UPDATE warehouse SET status = '异常' WHERE warehouse_code = ?";
        List<Object> parms = new ArrayList<>();
        parms.add(code);
        DatabaseHelper.save(sql, parms);
    }

    @Override
    public List<WareHouse> getdata3() throws DAOException {
        String sql = "SELECT * FROM notice";
        try {
            List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql);
            return getwarehouse3(results);
        } catch (SQLException e) {
            throw new DAOException("获取通知数据失败: " + e.getMessage());
        }
    }

    @Override
    public WareHouse findBycode2(String code, String problem) throws DAOException {
        String sql = "SELECT * FROM warehouse WHERE warehouse_code = ?";
        List<Object> parm = new ArrayList<>();
        parm.add(code);
        List<String> stringList = new ArrayList<>();
        stringList.add("id");
        stringList.add("warehouse_code");
        stringList.add("warehouse_name");
        stringList.add("warehouse_address");
        List<Map<String, Object>> results = DatabaseHelper.getResult(sql, parm, stringList);
        return getrow3(results, problem);
    }

    @Override
    public void add(WareHouse wareHouse) throws DAOException {
        String sql = "INSERT INTO notice (notice_random, notice_id, notice_time, notice_code, notice_name, " +
                     "notice_location, notice_problem) VALUES (?,?,?,?,?,?,?)";
        List<Object> parms = new ArrayList<>();
        String id = UUID.randomUUID().toString();
        parms.add(id);
        parms.add(wareHouse.getId());
        parms.add(wareHouse.getSpectiontime());
        parms.add(wareHouse.getWarehouseCode());
        parms.add(wareHouse.getWarehouseName());
        parms.add(wareHouse.getWarehouseLocation());
        parms.add(wareHouse.getProblem());
        DatabaseHelper.save(sql, parms);
    }

    @Override
    public List<WareHouse> search3(String qidata, String modata) throws DAOException {
        List<WareHouse> wareHouseList = getdata3();
        List<WareHouse> wareHouseList1 = new ArrayList<>();
        for (WareHouse wareHouse : wareHouseList) {
            if (wareHouse.getSpectiontime().contains(qidata) || wareHouse.getSpectiontime().contains(modata)) {
                wareHouseList1.add(wareHouse);
            }
        }
        return wareHouseList1;
    }

    @Override
    public List<WareHouse> delete2(String code) throws DAOException {
            String sql = "UPDATE warehouse SET is_deleted = 1 WHERE warehouse_code =?";
            String sql2 = "UPDATE warehouse SET status = '已删除' WHERE warehouse_code =?";
            List<Object> parms=new ArrayList<>();
            parms.add(code);
            DatabaseHelper.save(sql,parms);
            DatabaseHelper.save(sql2,parms);
            List<WareHouse> wareHouses=getdata2();
            return wareHouses;
    }

    @Override
    public void addWarehouse(WareHouse warehouse) throws Exception {
        String sql = "INSERT INTO warehouse (id, warehouse_code, warehouse_name, company_name, " +
                     "responsible_person, warehouse_address, warehouse_capacity, structure_type, warehouse_type, " +
                     "lease_amount, lease_start_date, lease_end_date, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, '正常')";
        String id = UUID.randomUUID().toString();
        DatabaseHelper.executeUpdate(sql,
                id,
                warehouse.getWarehouseCode(),
                warehouse.getWarehouseName(),
                warehouse.getCompanyName(),
                warehouse.getResponsiblePerson(),
                warehouse.getWarehouseLocation(),
                warehouse.getCapacity(),
                warehouse.getStructure(),
                warehouse.getWarehouseType(),
                warehouse.getLeaseAmount(),
                warehouse.getLeaseStartDate(),
                warehouse.getLeaseEndDate()
        );
    }

    @Override
    public void updateWarehouse(WareHouse warehouse) throws Exception {
        String sql = "UPDATE warehouse SET warehouse_name = ?, company_name = ?, " +
                     "responsible_person = ?, warehouse_address = ?, warehouse_capacity = ?, " +
                     "structure_type = ?, warehouse_type = ?, lease_amount = ?, " +
                     "lease_start_date = ?, lease_end_date = ? WHERE warehouse_code = ?";
        DatabaseHelper.executeUpdate(sql,
                warehouse.getWarehouseName(),
                warehouse.getCompanyName(),
                warehouse.getResponsiblePerson(),
                warehouse.getWarehouseLocation(),
                warehouse.getCapacity(),
                warehouse.getStructure(),
                warehouse.getWarehouseType(),
                warehouse.getLeaseAmount(),
                warehouse.getLeaseStartDate(),
                warehouse.getLeaseEndDate(),
                warehouse.getWarehouseCode()
        );
    }

    @Override
    public void deleteWarehouse(String warehouseCode) throws Exception {
        String sql = "UPDATE warehouse SET status = '已删除' WHERE warehouse_code = ?";
        DatabaseHelper.executeUpdate(sql, warehouseCode);
    }

    @Override
    public List<WareHouse> getAllWarehouses() throws Exception {
        String sql = "SELECT * FROM warehouse WHERE status != '已删除'";
        List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql);
        return convertToWarehouses(results);
    }

    @Override
    public List<WareHouse> searchWarehouses(String searchTerm) throws Exception {
        String sql = "SELECT * FROM warehouse WHERE status != '已删除' AND " +
                     "(warehouse_code LIKE ? OR warehouse_name LIKE ? OR company_name LIKE ?)";
        String pattern = "%" + searchTerm + "%";
        List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, pattern, pattern, pattern);
        return convertToWarehouses(results);
    }

    @Override
    public WareHouse getWarehouseByCode(String warehouseCode) throws Exception {
        String sql = "SELECT * FROM warehouse WHERE warehouse_code = ? AND status != '已删除'";
        List<Map<String, Object>> results = DatabaseHelper.executeQuery(sql, warehouseCode);
        if (results.isEmpty()) {
            return null;
        }
        return convertToWarehouse(results.get(0));
    }

    @Override
    public void importFromFile(File file) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 8) {
                    throw new DAOException("文件格式不正确");
                }

                WareHouse warehouse = new WareHouse();
                warehouse.setWarehouseCode(data[0].trim());
                warehouse.setWarehouseName(data[1].trim());
                warehouse.setCompanyName(data[2].trim());
                warehouse.setResponsiblePerson(data[3].trim());
                warehouse.setWarehouseLocation(data[4].trim());
                warehouse.setCapacity(new BigDecimal(data[5].trim()));
                warehouse.setStructure(data[6].trim());
                warehouse.setWarehouseType(data[7].trim());

                addWarehouse(warehouse);
            }
        } catch (Exception e) {
            throw new DAOException("导入文件失败: " + e.getMessage());
        }
    }

    private List<WareHouse> convertToWarehouses(List<Map<String, Object>> results) {
        List<WareHouse> warehouses = new ArrayList<>();
        for (Map<String, Object> row : results) {
            warehouses.add(convertToWarehouse(row));
        }
        return warehouses;
    }

    private WareHouse convertToWarehouse(Map<String, Object> row) {
        WareHouse warehouse = new WareHouse();
        warehouse.setId((String) row.get("id"));
        warehouse.setWarehouseCode((String) row.get("warehouse_code"));
        warehouse.setWarehouseName((String) row.get("warehouse_name"));
        warehouse.setCompanyName((String) row.get("company_name"));
        warehouse.setResponsiblePerson((String) row.get("responsible_person"));
        warehouse.setWarehouseLocation((String) row.get("warehouse_address"));
        warehouse.setCapacity(new BigDecimal(row.get("warehouse_capacity").toString()));
        warehouse.setStructure((String) row.get("structure_type"));
        warehouse.setWarehouseType((String) row.get("warehouse_type"));

        if (row.get("lease_amount") != null) {
            warehouse.setLeaseAmount(new BigDecimal(row.get("lease_amount").toString()));
        }

        if (row.get("lease_start_date") != null) {
            warehouse.setLeaseStartDate(((java.sql.Date) row.get("lease_start_date")).toLocalDate());
        }
        if (row.get("lease_end_date") != null) {
            warehouse.setLeaseEndDate(((java.sql.Date) row.get("lease_end_date")).toLocalDate());
        }

        return warehouse;
    }

    // Existing helper methods
    public List<WareHouse> getwarehouse2(List<Map<String, Object>> results) {
        List<WareHouse> wareHouseList = new ArrayList<>();
        for (Map<String, Object> row : results) {
            WareHouse wareHouse = new WareHouse();
            wareHouse.setWarehouseCode((String) row.get("warehouse_code"));
            wareHouse.setId((String) row.get("id"));
            wareHouse.setWarehouseName((String) row.get("warehouse_name"));
            wareHouse.setStatus((String) row.get("status"));
            wareHouse.setWarehouseType((String) row.get("warehouse_type"));
            wareHouse.setWarehouseLocation((String) row.get("warehouse_address"));
            wareHouse.setCapacity((BigDecimal) row.get("warehouse_capacity"));
            wareHouse.setNotice((String) row.get("warehouse_notice"));
            wareHouseList.add(wareHouse);
        }
        return wareHouseList;
    }

    public WareHouse getrow2(List<Map<String, Object>> results) {
        WareHouse wareHouse = new WareHouse();
        for (Map<String, Object> row : results) {
            wareHouse.setProblem((String) row.get("warehouse_problem"));
            wareHouse.setDeal((String) row.get("warehouse_deal"));
            wareHouse.setSpectiontime((String) row.get("warehouse_spectiontime"));
            wareHouse.setProblem2((String) row.get("warehouse_problem2"));
            wareHouse.setDeal2((String) row.get("warehouse_deal2"));
            wareHouse.setProblem3((String) row.get("warehouse_problem3"));
            wareHouse.setDeal3((String) row.get("warehouse_deal3"));
        }
        return wareHouse;
    }

    public WareHouse getrow3(List<Map<String, Object>> results, String problem) {
        WareHouse wareHouse = new WareHouse();
        for (Map<String, Object> row : results) {
            DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime ldt2 = LocalDateTime.now();
            String str2 = ldt2.format(dtf3);
            wareHouse.setProblem(problem);
            wareHouse.setWarehouseLocation((String) row.get("warehouse_address"));
            wareHouse.setWarehouseName((String) row.get("warehouse_name"));
            wareHouse.setWarehouseCode((String) row.get("warehouse_code"));
            wareHouse.setId((String) row.get("id"));
            wareHouse.setSpectiontime(str2);
        }
        return wareHouse;
    }

    public List<WareHouse> getwarehouse3(List<Map<String, Object>> results) {
        List<WareHouse> wareHouseList = new ArrayList<>();
        for (Map<String, Object> row : results) {
            WareHouse wareHouse = new WareHouse();
            wareHouse.setWarehouseCode((String) row.get("notice_code"));
            wareHouse.setSpectiontime((String) row.get("notice_time"));
            wareHouse.setId((String) row.get("notice_id"));
            wareHouse.setWarehouseName((String) row.get("notice_name"));
            wareHouse.setWarehouseLocation((String) row.get("notice_location"));
            wareHouse.setProblem((String) row.get("notice_problem"));
            wareHouseList.add(wareHouse);
        }
        return wareHouseList;
    }

    private List<WareHouse> getwarehouses(List<Map<String, Object>> results) {
        List<WareHouse> wareHouseList = new ArrayList<>();
        for (Map<String, Object> row : results) {
            WareHouse wareHouse = new WareHouse();
            wareHouse.setWarehouseCode((String) row.get("warehouse_code"));
            wareHouse.setId((String) row.get("warehouse_id"));
            wareHouse.setWarehouseName((String) row.get("warehouse_name"));
            wareHouse.setSnowtime((String) row.get("warehouse_snowtime"));
            wareHouse.setTemper((String) row.get("warehouse_temper"));
            wareHouse.setHumidity((String) row.get("warehouse_humidity"));
            wareHouse.setProblem((String) row.get("warehouse_problem"));
            wareHouse.setDeal((String) row.get("warehouse_deal"));
            wareHouse.setSpectionpeople((String) row.get("warehouse_spectionpeople"));
            wareHouse.setSpectiontime((String) row.get("warehouse_spectiontime"));
            wareHouseList.add(wareHouse);
        }
        return wareHouseList;
    }
}
