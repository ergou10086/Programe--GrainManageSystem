package controller;

import entity.WareHouse;
import view.WarehouseInfoView;
import service.IwarehouseService;
import service.impl.WarehouseInfServiceImpl;
import exceptions.GrainOrWarehouseServiceExceptions;
import service.IWarehouseInfService;

import javax.swing.*;
import java.io.File;
import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class WarehouseController {
    private WarehouseInfoView view;
    private String currentUser;
    private String userRole;
    private IWarehouseInfService warehouseService;

    public WarehouseController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        System.out.println("Current user: " + username + ", role: " + role);
        this.view = new WarehouseInfoView();
        this.warehouseService = new WarehouseInfServiceImpl();

        initializeListeners();
        loadWarehouseData();
        //setupPermissions();
    }

    private void initializeListeners() {
        view.getAddButton().addActionListener(e -> handleAdd());
        view.getUpdateButton().addActionListener(e -> handleUpdate());
        view.getDeleteButton().addActionListener(e -> handleDelete());
        view.getSearchButton().addActionListener(e -> handleSearch());
        view.getUploadButton().addActionListener(e -> handleUpload());
    }

    private void loadWarehouseData() {
        try {
            List<WareHouse> warehouses = warehouseService.getAllWarehouses();
            updateTable(warehouses);
        } catch (GrainOrWarehouseServiceExceptions.GetAllGrainFailedException e) {
            JOptionPane.showMessageDialog(view, "加载数据失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void enableAllButtons() {
        view.getAddButton().setEnabled(true);
        view.getUpdateButton().setEnabled(true);
        view.getDeleteButton().setEnabled(true);
        view.getUploadButton().setEnabled(true);
    }

    private void enableBasicButtons() {
        view.getAddButton().setEnabled(true);
        view.getUpdateButton().setEnabled(true);
        view.getDeleteButton().setEnabled(false);
        view.getUploadButton().setEnabled(false);
    }

    private void enableViewOnlyButtons() {
        view.getAddButton().setEnabled(false);
        view.getUpdateButton().setEnabled(false);
        view.getDeleteButton().setEnabled(false);
        view.getUploadButton().setEnabled(false);
    }

    private void disableAllButtons() {
        view.getAddButton().setEnabled(false);
        view.getUpdateButton().setEnabled(false);
        view.getDeleteButton().setEnabled(false);
        view.getUploadButton().setEnabled(false);
    }

    private void handleAdd() {
        try {
            WareHouse warehouse = getWarehouseFromFields();
            if (validateWarehouse(warehouse)) {
                warehouseService.addWarehouse(warehouse);
                JOptionPane.showMessageDialog(view, "添加成功！");
                loadWarehouseData();
                clearFields();
            }
        } catch (GrainOrWarehouseServiceExceptions.AddGrainFailedException e) {
            JOptionPane.showMessageDialog(view, "添加失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpdate() {
        try {
            int selectedRow = view.getWarehouseTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, "请先选择要更新的记录！");
                return;
            }

            WareHouse warehouse = getWarehouseFromFields();
            if (validateWarehouse(warehouse)) {
                warehouseService.updateWarehouse(warehouse);
                JOptionPane.showMessageDialog(view, "更新成功！");
                loadWarehouseData();
                clearFields();
            }
        } catch (GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException e) {
            JOptionPane.showMessageDialog(view, "更新失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleDelete() {
        try {
            int selectedRow = view.getWarehouseTable().getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(view, "请先选择要删除的记录！");
                return;
            }

            String warehouseCode = (String) view.getWarehouseTable().getValueAt(selectedRow, 0);
            int confirm = JOptionPane.showConfirmDialog(view, "确定要删除该记录吗？", "确认删除",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                warehouseService.deleteWarehouse(warehouseCode);
                JOptionPane.showMessageDialog(view, "删除成功！");
                loadWarehouseData();
                clearFields();
            }
        } catch (GrainOrWarehouseServiceExceptions.SoftDeleteGrainFailedException e) {
            JOptionPane.showMessageDialog(view, "删除失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleSearch() {
        try {
            String searchTerm = JOptionPane.showInputDialog(view, "请输入搜索关键字：");
            if (searchTerm != null && !searchTerm.trim().isEmpty()) {
                List<WareHouse> results = warehouseService.searchWarehouses(searchTerm);
                updateTable(results);
            }
        } catch (GrainOrWarehouseServiceExceptions.FindWarehouseFailedException e) {
            JOptionPane.showMessageDialog(view, "搜索失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void handleUpload() {
        try {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                warehouseService.importFromFile(selectedFile);
                JOptionPane.showMessageDialog(view, "文件导入成功！");
                loadWarehouseData();
            }
        } catch (GrainOrWarehouseServiceExceptions.ImportGrainFailedException e) {
            JOptionPane.showMessageDialog(view, "文件导入失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    private WareHouse getWarehouseFromFields() {
        WareHouse warehouse = new WareHouse();

        // 基本信息
        warehouse.setWarehouseCode(view.getWarehouseCodeField().getText().trim());
        warehouse.setWarehouseName(view.getWarehouseNameField().getText().trim());
        warehouse.setCompanyName(view.getCompanyNameField().getText().trim());
        warehouse.setResponsiblePerson(view.getManagerNameField().getText().trim());
        warehouse.setWarehouseLocation(view.getLocationField().getText().trim());

        // 容量
        try {
            String capacityStr = view.getCapacityField().getText().trim();
            if (!capacityStr.isEmpty()) {
                warehouse.setCapacity(new BigDecimal(capacityStr));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "库容量必须是有效的数字！");
        }

        // 结构和类型
        warehouse.setStructure(view.getStructureTypeCombo().getSelectedItem().toString());
        warehouse.setWarehouseType(view.getWarehouseTypeCombo().getSelectedItem().toString());

        // 租赁信息
        try {
            String rentStr = view.getRentField().getText().trim();
            if (!rentStr.isEmpty()) {
                warehouse.setLeaseAmount(new BigDecimal(rentStr));
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "租金必须是有效的数字！");
        }

        // 租赁日期
        warehouse.setLeaseStartDate(view.getLeaseStartDate().getDate() != null ?
                view.getLeaseStartDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);
        warehouse.setLeaseEndDate(view.getLeaseEndDate().getDate() != null ?
                view.getLeaseEndDate().getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate() : null);

        return warehouse;
    }

    private boolean validateWarehouse(WareHouse warehouse) {
        if (warehouse.getWarehouseCode().isEmpty() || warehouse.getWarehouseName().isEmpty()) {
            JOptionPane.showMessageDialog(view, "仓库编号和名称为必填项！");
            return false;
        }
        return true;
    }

    private void clearFields() {
        // 基本信息字段
        view.getWarehouseCodeField().setText("");
        view.getWarehouseNameField().setText("");
        view.getCompanyNameField().setText("");
        view.getManagerNameField().setText("");
        view.getLocationField().setText("");

        // 仓库属性字段
        view.getCapacityField().setText("");
        view.getStructureTypeCombo().setSelectedIndex(0);
        view.getWarehouseTypeCombo().setSelectedIndex(0);

        // 租赁相关字段
        view.getRentField().setText("");
        view.getLeaseStartDate().setDate(null);
        view.getLeaseEndDate().setDate(null);

        // 如果有备注字段
        if (view.getRemarksArea() != null) {
            view.getRemarksArea().setText("");
        }

        // 刷新表格选择
        if (view.getWarehouseTable() != null) {
            view.getWarehouseTable().clearSelection();
        }
    }

    private void updateTable(List<WareHouse> warehouses) {
        try {
            // 获取表格模型
            if (!(view.getWarehouseTable().getModel() instanceof DefaultTableModel)) {
                throw new IllegalStateException("表格模型不是 DefaultTableModel 类型");
            }
            
            DefaultTableModel model = (DefaultTableModel) view.getWarehouseTable().getModel();
            
            // 清空现有数据
            model.setRowCount(0);

            // 添加新数据
            for (WareHouse warehouse : warehouses) {
                model.addRow(new Object[]{
                        warehouse.getWarehouseCode(),      // 仓库编号
                        warehouse.getWarehouseName(),      // 仓库名称
                        warehouse.getCompanyName(),        // 公司名称
                        warehouse.getResponsiblePerson(),  // 负责人
                        warehouse.getWarehouseLocation(),  // 位置
                        warehouse.getCapacity(),           // 库容
                        warehouse.getWarehouseType(),      // 仓库类型
                        warehouse.getLeaseEndDate()        // 租赁到期日期
                });
            }

            // 刷新表格显示
            view.getWarehouseTable().repaint();
        } catch (IllegalStateException e) {
            JOptionPane.showMessageDialog(view, "更新表格失败：" + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, "更新表格失败：" + e.getMessage());
        }
    }

    public JPanel getView() {
        return view;
    }
}
