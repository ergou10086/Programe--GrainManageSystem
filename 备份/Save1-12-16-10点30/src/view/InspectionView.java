package view;

import java.awt.*;

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
import controller.InspectionController;


public class InspectionView extends JPanel {
    // 表单组件
    private JTextField warehouseCodeField;
    private JTextField warehouseNameField;
    private JTextField companyNameField;
    private JTextField managerNameField;
    private JTextField locationField;
    private DefaultTableModel defaultTableModel;
    private JComboBox<String> structureTypeCombo;
    private JButton notice;
    private JTextArea remarksArea;
    private JTextField number;
    private JButton zhui;

    // 按钮
    private JButton addButton;
    private JButton deleteButton;
    private JButton searchButton;
    private JButton uploadButton;

    // 表格
    private JTable warehouseTable;
    private JScrollPane tableScrollPane;
    private InspectionController inspectionController;

    public void setInspectionController(InspectionController inspectionController) {
        this.inspectionController = inspectionController;
        loaddata();
    }
    public void loaddata(){
        inspectionController.loaddata();
    }

    public InspectionView() {
        setLayout(new BorderLayout(10, 10));
        initComponents();
    }

    private void initComponents() {
        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(), "巡库管理",
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
        number=new JTextField(15);

        structureTypeCombo = new JComboBox<>(new String[]{"害虫", "结霜", "其他问题"});

        remarksArea = new JTextArea(3, 20);

        // 添加组件到表单
        addFormField(formPanel, "仓库编号:",number , gbc, 0);
        addFormField(formPanel, "仓库名称:", warehouseCodeField, gbc, 1);
        addFormField(formPanel, "检查人员:", warehouseNameField, gbc, 2);
        addFormField(formPanel, "风雪时间:", companyNameField, gbc, 3);
        addFormField(formPanel, "粮温:", managerNameField, gbc, 4);
        addFormField(formPanel, "湿度:", locationField, gbc, 5);
        addFormField(formPanel, "问题记录:", structureTypeCombo, gbc, 6);

        // 备注文本区域
        gbc.gridx = 0;
        gbc.gridy = 11;
        gbc.gridwidth = 1;
        formPanel.add(new JLabel("处理措施:"), gbc);

        gbc.gridx = 1;
        gbc.gridwidth = 3;
        formPanel.add(new JScrollPane(remarksArea), gbc);

        // 按钮面板
        JPanel buttonPanel = new JPanel(new GridLayout(2,3,2,2));
        addButton = new JButton("添加");
        deleteButton = new JButton("删除");
        searchButton = new JButton("查询");
        uploadButton = new JButton("生成报告");
        zhui=new JButton("问题追踪");
        notice=new JButton("异常预警");

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(searchButton);
        buttonPanel.add(notice);
        buttonPanel.add(uploadButton);
        buttonPanel.add(zhui);
        // 表格
        String[] columnNames = {"仓库编号","仓库名称","风雪时间", "良温", "湿度", "问题记录","处理措施","检查人员","检查日期"};
        defaultTableModel=new DefaultTableModel();
        for(int j=0;j<columnNames.length;j++){
            defaultTableModel.addColumn(columnNames[j]);
        }
        warehouseTable=new JTable(defaultTableModel);
        tableScrollPane = new JScrollPane(warehouseTable);

        // 布局
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.add(formPanel, BorderLayout.CENTER);
        leftPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加到主面板
        add(leftPanel, BorderLayout.WEST);
        add(tableScrollPane, BorderLayout.CENTER);
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
    public JComboBox<String> getStructureTypeCombo() { return structureTypeCombo; }
    public JTextArea getRemarksArea() { return remarksArea; }
    public JButton getAddButton() { return addButton; }
    public JButton getDeleteButton() { return deleteButton; }
    public JButton getSearchButton() { return searchButton; }
    public JButton getUploadButton() { return uploadButton; }
    public JTable getWarehouseTable() { return warehouseTable; }
    public DefaultTableModel getDefaultTableModel(){return defaultTableModel;}
    public String getcode(){
        return number.getText();
    }
    public String getname(){
        return warehouseCodeField.getText();
    }
    public String getsnowtime(){
        return companyNameField.getText();
    }
    public String gettemper(){
        return managerNameField.getText();
    }
    public String gethumdity(){
        return locationField.getText();
    }
    public String getproblem(){
        return structureTypeCombo.getSelectedItem().toString();
    }
    public String getdeal(){
        return remarksArea.getText();
    }
    public String getspectionpeople(){
        return warehouseNameField.getText();
    }

    public JButton getZhui() {
        return zhui;
    }
    public JButton getNotice(){
        return notice;
    }
}

