package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import entity.WareHouse;
import exceptions.DAOException;
import exceptions.GetHistoryExceptions;
import exceptions.GrainOrWarehouseServiceExceptions;
import service.IWarehouseInfService;
import service.impl.GrainChangeHistoryServiceImpl;
import service.impl.GrainInfServiceImpl;
import service.impl.WarehouseInfServiceImpl;
import view.HistoryRecordView;


// 历史信息记录页面的控制器类，连接视图和服务还有数据库
public class HistoryRecordController {
    private HistoryRecordView view;
    private String currentUser;
    private String userRole;
    private GrainChangeHistoryServiceImpl grainHistoryService;
    private GrainInfServiceImpl grainInfService;
    private IWarehouseInfService iWarehouseInfService;


    // 构造函数初始化
    public HistoryRecordController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        this.view = new HistoryRecordView();
        this.grainHistoryService = new GrainChangeHistoryServiceImpl();
        this.grainInfService = new GrainInfServiceImpl();
        this.iWarehouseInfService=new WarehouseInfServiceImpl();
        initializeListeners();
        loadHistoryData();
        // 根据用户角色设置权限
        setupPermissions();
    }

    // 初始化各种监听器，用于监听界面上不同组件（如查询按钮、恢复/删除菜单项等）的用户操作事件，并做出相应的处理逻辑响应
    private void initializeListeners() {
        // 为每个面板的查询按钮添加监听器
        view.getSearchButtons().forEach((panelName, button) -> button.addActionListener(e -> {
            String startDate = view.getStartDateFromDC(panelName); // 使用DateChooser获取日期
            String endDate = view.getEndDateFromDC(panelName);

            // 验证日期输入
            if (startDate.isEmpty() || endDate.isEmpty()) {
                JOptionPane.showMessageDialog(view, "请选择开始和结束日期", "提示", JOptionPane.WARNING_MESSAGE);
                return;
            }

            searchHistory(panelName, startDate, endDate);
        }));

        // 为查询全部按钮添加监听器
        view.getSearchAllButtons().forEach((panelName, button) -> button.addActionListener(e -> {
            if (panelName.equals("storage")) {
                try {
                    // 使用一个较大的时间范围来获取所有记录
                    List<Map<String, Object>> entryResults = searchEntryHistory("1970-01-01", "2099-12-31");
                    List<Map<String, Object>> exitResults = searchExitHistory("1970-01-01", "2099-12-31");
                    updateStorageHistoryTable(entryResults, exitResults);
                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(view, "加载出入库历史记录失败：" + ex.getMessage(),
                            "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else if (panelName.equals("grain")) {
                loadGrainHistory();
            }else if(panelName.equals("inventory")){
                loadInspectionHistory();
            }
            // 可以添加其他面板的处理逻辑
        }));

        // 添加恢复记录的监听器
        view.getRestoreMenuItem().addActionListener(e -> handleRestore());

        // 添加彻底删除的监听器
        view.getDeleteMenuItem().addActionListener(e -> handlePermanentDelete());

        // 添加删除按钮的监听器，并添加调试输出
        view.getDeleteRecordBtn().addActionListener(e -> {
            System.out.println("Delete button clicked in controller"); // 调试输出
            handleDeleteRecord();
        });
    }



    // 根据指定的面板名称（panelName）以及时间范围来查询相应的历史记录
    private void searchHistory(String panelName, String startDate, String endDate) {
        try {
            if (panelName.equals("grain")) {
                List<Map<String, Object>> results = grainHistoryService.getHistoryByTimeRange(startDate, endDate);
                updateGrainHistoryTable(results);
            } else if (panelName.equals("storage")) {
                // 查询出入库历史记录，获取入库和出库历史记录数据，然后更新出入库历史记录表格显示
                List<Map<String, Object>> entryResults = searchEntryHistory(startDate, endDate);
                List<Map<String, Object>> exitResults = searchExitHistory(startDate, endDate);
                updateStorageHistoryTable(entryResults, exitResults);
            }else if(panelName.equals("inventory")){
                try {
                    DefaultTableModel model=view.getInspectionModel();
                    model.setRowCount(0);
                   List<WareHouse> wareHouses= iWarehouseInfService.search3(startDate,endDate);
                   for(WareHouse wareHouse :wareHouses){
                       model.addRow(new Object[]{
                          wareHouse.getSpectiontime(),
                               wareHouse.getWarehouseCode(),
                               wareHouse.getWarehouseName(),
                               wareHouse.getWarehouseLocation(),
                               wareHouse.getProblem()
                       });
                   }
                } catch (DAOException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(view, "查询历史记录失败：" + ex.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }



    // 权限设置方法
    private void setupPermissions() {
        boolean hasPermission = "仓库管理员".equals(userRole) || "系统管理员".equals(userRole);

        // 设置菜单选项的可用性
        view.getRestoreMenuItem().setEnabled(hasPermission);
        view.getDeleteMenuItem().setEnabled(hasPermission);

        // 如果没有权限，添加提示信息
        if (!hasPermission) {
            view.getRestoreMenuItem().setToolTipText("只有仓库管理员和系统管理员可以执行此操作");
            view.getDeleteMenuItem().setToolTipText("只有仓库管理员和系统管理员可以执行此操作");
        }
    }



    private void loadHistoryData() {
        // 加载粮食信息历史记录
        loadGrainHistory();

        // 加载出入库历史记录
        loadStorageHistory();

        // TODO: 加载其他历史记录
        loadWarehouseHistory();
        loadInspectionHistory();
    }


    // 从数据库加载粮食信息历史记录
    private void loadGrainHistory() {
        try {
            // 加载粮食信息历史记录
            List<Map<String, Object>> grainHistory = grainHistoryService.getHistoryByTimeRange(
                    "1970-01-01", // 开始时间设置得足够早
                    "2099-12-31"  // 结束时间设置得足够晚
            );
            updateGrainHistoryTable(grainHistory);
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "加载历史记录失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            try {
                throw new GetHistoryExceptions.LoadHistoryFailedException(e.getMessage());
            } catch (GetHistoryExceptions.LoadHistoryFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    private void loadWarehouseHistory() {
        // 从数据库加载粮库信息历史记录
        DefaultTableModel model = view.getWarehouseModel();
        model.setRowCount(0);
        // TODO: 添加数据库查询代码
    }


    private void loadStorageHistory() {
        try {
            // 使用一个较大的时间范围来获取所有记录
            List<Map<String, Object>> entryResults = searchEntryHistory("1970-01-01", "2099-12-31");
            List<Map<String, Object>> exitResults = searchExitHistory("1970-01-01", "2099-12-31");
            updateStorageHistoryTable(entryResults, exitResults);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "加载出入库历史记录失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void loadInspectionHistory() {
        // 从数据库加载巡检历史记录
        DefaultTableModel model = view.getInspectionModel();
        model.setRowCount(0);
        try {
            DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime ldt2 = LocalDateTime.now();
            String str2 = ldt2.format(dtf3);
            List<WareHouse> wareHouses = iWarehouseInfService.getdata3();
            for (WareHouse wareHouse : wareHouses) {
                model.addRow(new Object[]{
                        wareHouse.getSpectiontime(),
                        wareHouse.getWarehouseCode(),
                        wareHouse.getWarehouseName(),
                        wareHouse.getWarehouseLocation(),
                        wareHouse.getProblem()
                });
            }}catch(Exception e){
                try {
                    JOptionPane.showMessageDialog(view, "加载数据失败：" + e.getMessage());
                    throw new GrainOrWarehouseServiceExceptions.LoadGrainFailedException(e.getMessage());
                } catch (GrainOrWarehouseServiceExceptions.LoadGrainFailedException ex) {
                    throw new RuntimeException(ex);
                }
            }
    }


    public HistoryRecordView getView() {
        return view;
    }

    public IWarehouseInfService getiWarehouseInfService() {
        return iWarehouseInfService;
    }

    // 更新粮食历史记录信息的表格
    private void updateGrainHistoryTable(List<Map<String, Object>> records) {
        DefaultTableModel model = view.getGrainModel();
        model.setRowCount(0);  // 先清空数据

        for(Map<String, Object> record : records) {
            model.addRow(new Object[]{
                    record.get("change_time"),
                    record.get("change_type"),
                    record.get("grain_id"),
                    record.get("grain_name"),
                    record.get("operator"),
                    record.get("change_detail")
            });
        }
    }


    // 删除历史记录的监听器
    private void handleDeleteRecord(){
        // 首先检查权限
        if (!checkPermission()) {
            JOptionPane.showMessageDialog(view, "只有仓库管理员和系统管理员可以执行此操作", "权限不足", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 获取选中的行
        int selectedRow = view.getGrainHistoryTable().getSelectedRow();
        System.out.println("Selected row: " + selectedRow); // 调试输出
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "请先选择要删除的记录", "提示", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            // 获取记录的时间并转换格式
            Object timeValue = view.getGrainModel().getValueAt(selectedRow, 0);
            String changeTime;
            if (timeValue instanceof java.time.LocalDateTime) {
                java.time.LocalDateTime dateTime = (java.time.LocalDateTime) timeValue;
                changeTime = dateTime.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            } else {
                changeTime = timeValue.toString();
            }

            System.out.println("Attempting to delete record with time: " + changeTime); // 调试输出

            // 确认删除
            int confirm = JOptionPane.showConfirmDialog(view, "确定要删除这条历史记录吗？此操作不可恢复。\n时间：" + changeTime, "确认删除", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    boolean success = grainHistoryService.deleteHistoryRecordByTime(changeTime);
                    if (success) {
                        // 刷新表格数据
                        loadHistoryData();
                        JOptionPane.showMessageDialog(view, "记录删除成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(view, "删除记录失败：未找到匹配的记录", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception ex) {
                    System.err.println("删除记录失败：" + ex.getMessage());
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(view, "删除记录失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (Exception e) {
            System.err.println("处理时间格式时出错：" + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "处理时间格式时出错：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }


    // 处理恢复粮食
    private void handleRestore() {
        // 首先检查权限
        if (!checkPermission()) {JOptionPane.showMessageDialog(view, "只有仓库管理员和系统管理员可以执行此操作", "权限不足", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = view.getGrainHistoryTable().getSelectedRow();
        if (row >= 0) {
            try {
                // 从历史记录表格中获取相关信息
                String grainName = (String) view.getGrainModel().getValueAt(row, 3); // 粮食名称
                String changeType = (String) view.getGrainModel().getValueAt(row, 1); // 操作类型

                // 检查是否是删除的记录
                if (!"删除".equals(changeType)) {
                    JOptionPane.showMessageDialog(view, "只能恢复已删除的记录！");
                    return;
                }

                // 恢复记录
                grainInfService.restoreGrain(grainName);

                // 获取所相关的grain_id并记录历史
                List<String> idGrains = grainInfService.getIdGrainsByName(grainName);
                for (String idGrain : idGrains) {
                    grainHistoryService.recordGrainChange(
                            idGrain,
                            "恢复",
                            String.format("恢复已删除的粮食: %s", grainName),
                            currentUser
                    );
                }

                JOptionPane.showMessageDialog(view, "记录已恢复！");
                loadHistoryData(); // 刷新历史记录表格
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "恢复失败：" + ex.getMessage());
                ex.printStackTrace();
                try {
                    throw new GrainOrWarehouseServiceExceptions.RecoverGrainFailedException(ex.getMessage());
                } catch (GrainOrWarehouseServiceExceptions.RecoverGrainFailedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    // 处理彻底删除粮食，并留下记录，此过程不可逆
    private void handlePermanentDelete() {
        // 首先检查权限
        if (!checkPermission()) {JOptionPane.showMessageDialog(view, "只有仓库管理员和系统管理员可以执行此操作", "权限不足", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int row = view.getGrainHistoryTable().getSelectedRow();
        if (row >= 0) {
            try {
                // 从历史记录表格中获取相关信息
                String grainName = (String) view.getGrainModel().getValueAt(row, 3); // 粮食名称
                String changeType = (String) view.getGrainModel().getValueAt(row, 1); // 操作类型

                // 检查是否是删除的记录
                if (!"删除".equals(changeType)) {
                    JOptionPane.showMessageDialog(view, "只能彻底删除已标记为删除的记录！");
                    return;
                }

                int confirm = JOptionPane.showConfirmDialog(view,
                        "此操作将永久删除该记录及其所有历史记录，确定要继续吗？",
                        "确认删除",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        // 彻底删除记录
                        grainInfService.deleteGrainsPermanently(grainName);

                        JOptionPane.showMessageDialog(view, "记录已永久删除！");
                        loadHistoryData(); // 刷新历史记录表格
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(view,
                                "删除失败：" + e.getMessage(),
                                "错误",
                                JOptionPane.ERROR_MESSAGE);
                        e.printStackTrace();
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(view, "操作失败：" + ex.getMessage());
                ex.printStackTrace();
                try {
                    throw new GrainOrWarehouseServiceExceptions.DeleteGrainFailedException(ex.getMessage());
                } catch (GrainOrWarehouseServiceExceptions.DeleteGrainFailedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    // 权限检查方法
    private boolean checkPermission() {
        return "仓库管理员".equals(userRole) || "系统管理员".equals(userRole);
    }


    // 添加查询入库历史记录的方法
    private List<Map<String, Object>> searchEntryHistory(String startDate, String endDate) throws SQLException {
        String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "zjm10086";

        Connection conn = null;
        PreparedStatement ps = null;

        // 用于存储查询结果的列表，每个元素是一个Map，Map的键值对表示一条入库历史记录的各个字段及其对应的值
        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT entryDate, cropName, warehouse, driverName, phone,grossWeigh " +
                "FROM entry_data WHERE STR_TO_DATE(entryDate, '%Y-%m-%d %H:%i:%s')" +
                " BETWEEN ? AND ? ORDER BY STR_TO_DATE(entryDate, '%Y-%m-%d %H:%i:%s') DESC";
        //STR_TO_DATE()是 MySQL 中的一个函数，用于将一个字符串按照指定的格式转换为日期时间类型。'%Y-%m-%d %H:%i:%s' 是一个格式化字符串
        //BETWEEN ? AND ?作用为将查询的结果确定为startDate + " 00:00:00"与endDate + " 23:59:59"之间
        //ORDER BY 是 SQL 中用于对查询结果进行排序的关键字，DESC将排序设置为降序

        try{
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = conn.prepareStatement(sql);

            // 设置日期参数
            ps.setString(1, startDate + " 00:00:00");//传入为第一个问号的值
            ps.setString(2, endDate + " 23:59:59");//传入为第二个问号的值

            ResultSet rs = ps.executeQuery();//获取查找的结果
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("operation_time", rs.getString("entryDate"));
                record.put("operation_type", "入库");
                record.put("crop_name", rs.getString("cropName"));
                record.put("weight", rs.getString("grossWeigh"));
                record.put("warehouse_name", rs.getString("warehouse"));
                record.put("driver_name", rs.getString("driverName"));
                record.put("driver_phone", rs.getString("phone"));
                results.add(record);
            }
        } catch (SQLException e) {
            e.printStackTrace(); // 添加这行来打印详细的错误信息
            JOptionPane.showMessageDialog(view, "查询入库记录失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
        return results;
    }


    // 添加查询出库历史记录的方法
    private List<Map<String, Object>> searchExitHistory(String startDate, String endDate) throws SQLException {
        String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "zjm10086";

        Connection conn = null;
        PreparedStatement ps = null;

        List<Map<String, Object>> results = new ArrayList<>();
        String sql = "SELECT data, cropName, wareHouse, driverName, driverPhone,weight " +
                "FROM exit_data " +
                "WHERE STR_TO_DATE(data, '%Y-%m-%d %H:%i:%s') BETWEEN ? AND ? " +
                "ORDER BY STR_TO_DATE(data, '%Y-%m-%d %H:%i:%s') DESC";

        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            ps = conn.prepareStatement(sql);
            // 设置日期参数
            ps.setString(1, startDate + " 00:00:00");
            ps.setString(2, endDate + " 23:59:59");

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<String, Object> record = new HashMap<>();
                record.put("operation_time", rs.getString("data"));
                record.put("operation_type", "出库");
                record.put("crop_name", rs.getString("cropName"));
                record.put("weight", rs.getString("weight"));
                record.put("warehouse_name", rs.getString("wareHouse"));
                record.put("driver_name", rs.getString("driverName"));
                record.put("driver_phone", rs.getString("driverPhone"));
                results.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace(); // 添加这行来打印详细的错误信息
            JOptionPane.showMessageDialog(view, "查询出库记录失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            throw e;
        }
        return results;
    }


    // 更新出入库历史记录表格的方法
    private void updateStorageHistoryTable(List<Map<String, Object>> entryResults,
                                           List<Map<String, Object>> exitResults) {
        // 获取出入库历史记录对应的表格模型，用于后续对表格数据的操作（如清空、添加行等）
        DefaultTableModel model = view.getStorageModel();
        // 先清空现有表格中的数据，确保表格处于初始状态
        model.setRowCount(0);

        // 合并入库和出库记录
        List<Map<String, Object>> allResults = new ArrayList<>();
        allResults.addAll(entryResults);
        allResults.addAll(exitResults);

        // 添加所有记录到表格
        //// 遍历 allResults 列表
        for (Map<String, Object> record : allResults) {
            // 调用 model 的 addRow 方法向表格模型中添加一行数据
            model.addRow(new Object[]{
                    record.get("operation_time"),    // 操作时间
                    record.get("operation_type"),    // 操作类型（出库/入库）
                    record.get("crop_name"),         // 粮食名称
                    record.get("weight"),            //粮食重量
                    record.get("warehouse_name"),    // 仓库
                    record.get("driver_name"),       // 司机
                    record.get("driver_phone")       // 司机联系方式
            });
        }
    }
}