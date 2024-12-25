package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import com.toedter.calendar.JDateChooser;

// 负责粮库信息管理的视图类，实现系统和应用设置的ui
public class WarehouseInfoView extends JPanel {
    // 表单组件
    private JTextField warehouseCodeField;
    private JTextField warehouseNameField;
    private JTextField companyNameField;
    private JTextField managerNameField;
    private JTextField locationField;
    private JTextField capacityField;
    private JComboBox<String> structureTypeCombo;
    private JComboBox<String> warehouseTypeCombo;
    private JTextField rentField;
    private JDateChooser leaseStartDate;
    private JDateChooser leaseEndDate;
    private JTextArea remarksArea;
    
    // 按钮
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton uploadButton;
    
    // 表格
    private JTable warehouseTable;
    private JScrollPane tableScrollPane;

    public WarehouseInfoView() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createEtchedBorder(), "粮库信息",
            TitledBorder.LEFT, TitledBorder.TOP));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // 初始化组件
        warehouseCodeField = new JTextField(15);
        warehouseNameField = new JTextField(15);
        companyNameField = new JTextField(15);
        managerNameField = new JTextField(15);
        locationField = new JTextField(15);
        capacityField = new JTextField(15);
        
        structureTypeCombo = new JComboBox<>(new String[]{"钢筋混凝土", "砖混", "钢结构"});
        warehouseTypeCombo = new JComboBox<>(new String[]{"粮库", "烘干塔", "晾晒场"});
        
        rentField = new JTextField(15);
        leaseStartDate = new JDateChooser();
        leaseEndDate = new JDateChooser();
        remarksArea = new JTextArea(3, 20);
        
        // 添加组件到表单
        addFormField(formPanel, "仓库编号:", warehouseCodeField, gbc, 0);
        addFormField(formPanel, "仓库名称:", warehouseNameField, gbc, 1);
        addFormField(formPanel, "公司名称:", companyNameField, gbc, 2);
        addFormField(formPanel, "负责人:", managerNameField, gbc, 3);
        addFormField(formPanel, "位置:", locationField, gbc, 4);
        addFormField(formPanel, "库容(吨):", capacityField, gbc, 5);
        addFormField(formPanel, "库结构:", structureTypeCombo, gbc, 6);
        addFormField(formPanel, "仓库类型:", warehouseTypeCombo, gbc, 7);
        addFormField(formPanel, "租金(元/年):", rentField, gbc, 8);
        addFormField(formPanel, "租赁开始:", leaseStartDate, gbc, 9);
        addFormField(formPanel, "租赁结束:", leaseEndDate, gbc, 10);
        
        // 备注文本区域
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("备注:"), gbc);
        
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(new JScrollPane(remarksArea), gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
        addButton = new JButton("添加");
        updateButton = new JButton("修改");
        deleteButton = new JButton("删除");
        searchButton = new JButton("查询");
        uploadButton = new JButton("上传附件");
        
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(uploadButton);

        // 表格
        String[] columnNames = {"仓库编号", "仓库名称", "公司名称", "负责人", "位置", "库容", "类型", "租赁到期"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        warehouseTable = new JTable(model);
        tableScrollPane = new JScrollPane(warehouseTable);

        // 布局
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加到主面板
        add(leftPanel, BorderLayout.WEST);
        add(tableScrollPane, BorderLayout.CENTER);

        // 确保按钮默认是启用的
        addButton.setEnabled(true);
        updateButton.setEnabled(true);
        deleteButton.setEnabled(true);
        uploadButton.setEnabled(true);
    }

    private void addFormField(JPanel panel, String label, JComponent field, 
                            GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        panel.add(field, gbc);
    }


    // Getters
    public JTextField getWarehouseCodeField() { return warehouseCodeField; }
    public JTextField getWarehouseNameField() { return warehouseNameField; }
    public JTextField getCompanyNameField() { return companyNameField; }
    public JTextField getManagerNameField() { return managerNameField; }
    public JTextField getLocationField() { return locationField; }
    public JTextField getCapacityField() { return capacityField; }
    public JComboBox<String> getStructureTypeCombo() { return structureTypeCombo; }
    public JComboBox<String> getWarehouseTypeCombo() { return warehouseTypeCombo; }
    public JTextField getRentField() { return rentField; }
    public JDateChooser getLeaseStartDate() { return leaseStartDate; }
    public JDateChooser getLeaseEndDate() { return leaseEndDate; }
    public JTextArea getRemarksArea() { return remarksArea; }
    public JButton getAddButton() { return addButton; }
    public JButton getUpdateButton() { return updateButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getUploadButton() { return uploadButton; }
    public JTable getWarehouseTable() { return warehouseTable; }
}
