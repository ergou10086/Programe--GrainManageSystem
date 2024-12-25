package view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

import controller.GrainController;

// 粮食基础信息的管理视图类，负责实现粮食基础信息的管理视图界面
public class GrainInfoView extends JPanel {
    // 用户信息
    private String username;
    private String role;
    // 文本框
    private JTextField txtGrainCode;     // 输入粮食编号的文本框
    private JTextField txtGrainName;     // 输入粮食名称的文本框
    private JTextField txtGrainType;     // 输入粮食类型的文本框
    private JTextField txtPrice;         // 输入粮食价格的文本框
    private JTextField txtShelfLife;     // 输入粮食保质期的文本框
    private JTextArea txtRemark;         // 输入粮食备注的文本域
    // 表格
    private JTable grainTable;           // 显示粮食信息的表格
    private DefaultTableModel tableModel; // 表格模型
    private GrainController controller;   // 控制器对象实例化
    // 按钮
    private JButton btnAdd = new JButton("添加");
    private JButton btnUpdate = new JButton("修改");
    private JButton btnClear = new JButton("清空");
    private JButton btnImport = new JButton("导入");
    private JButton btnExport = new JButton("导出");
    private JButton btnDelete = new JButton("删除");
    private JButton btnCheck = new JButton("查询");

    // 构造方法，初始化视图组件
    public GrainInfoView(String username, String role) {
        this.username = username;
        this.role = role;
        // 初始化界面组件
        initComponents();
    }


    // 初始化页面
    private void initComponents(){
        // 移除窗口相关设置
        setLayout(new BorderLayout(10, 20));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 主面板
        JPanel mainPanel = new JPanel(new BorderLayout(10, 20));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 输入面板，放置输入粮食信息的文本框和文本区域等组件
        JPanel inputPanel = createInputPanel();
        // 将输入面板添加到主面板的北
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // 创建表格面板，用于展示粮食信息的表格
        JPanel tablePanel = createTablePanel();
        // 将表格面板添加到主面板的中
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        // 创建按钮面板，用于放置各种操作按钮
        JPanel buttonPanel = createButtonPanel();
        // 将按钮面板添加到主面板的南
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 将主面板添加到当前面板
        add(mainPanel);
    }


    // 设置控制器的方法
    public void setController(GrainController controller) {
        this.controller = controller;
        loadGrainData();
    }


    // 创建输入面板，使用GridBagLayout布局
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 第一行组件布局
        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("粮食编号:"), gbc);

        gbc.gridx = 1;
        txtGrainCode = new JTextField(15);   // 宽度为15的文本框用于输入粮食编号
        panel.add(txtGrainCode, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("粮食名称:"), gbc);

        gbc.gridx = 3;
        txtGrainName = new JTextField(15);  // 宽度为15列的文本框用于输入粮食名称
        panel.add(txtGrainName, gbc);


        // 第二行组件布局
        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("粮食种类:"), gbc);

        gbc.gridx = 1;
        txtGrainType = new JTextField(15);   // 一个宽度为15列的文本框用于输入粮食种类
        panel.add(txtGrainType, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("单价(元/斤):"), gbc);

        gbc.gridx = 3;
        txtPrice = new JTextField(15);    // 宽度为15列的文本框用于输入粮食单价
        panel.add(txtPrice, gbc);


        // 第三行组件布局
        gbc.gridy = 2;
        gbc.gridx = 0;
        panel.add(new JLabel("保质期(月):"), gbc);

        gbc.gridx = 1;
        txtShelfLife = new JTextField(15);   // 宽度为15列的文本框用于输入粮食保质期
        panel.add(txtShelfLife, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel("备注:"), gbc);

        gbc.gridx = 3;
        // 创建一个行数为2，宽度为15列的文本区域用于输入粮食备注信息，设置自动换行
        txtRemark = new JTextArea(2, 15);
        txtRemark.setLineWrap(true);
        panel.add(new JScrollPane(txtRemark), gbc);   // 滚动面板

        return panel;
    }



    // 创建用于展示粮食信息的表格面板
    private JPanel createTablePanel() {
        // 定义表格的列名数组
        String[] columnNames = {"编号", "名称", "种类", "单价", "保质期", "备注"};
        // 创建表格模型
        tableModel = new DefaultTableModel(columnNames, 0);
        // 创建表格
        grainTable = new JTable(tableModel);
        // 创建滚动面板并添加表格
        JScrollPane scrollPane = new JScrollPane(grainTable);

        // 创建面板
        JPanel panel = new JPanel(new BorderLayout());
        // 将滚动面板添加到面板中
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }



    // 创建包含操作按钮的按钮面板，并为各个按钮添加对应的事件监听器
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        // 将各个按钮添加到按钮面板上
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnClear);
        panel.add(btnImport);
        panel.add(btnExport);
        panel.add(btnCheck);

        return panel;
    }

    // 清空各个输入框和文本区域中的内容
    public void clearFields() {
        txtGrainCode.setText("");
        txtGrainName.setText("");
        txtGrainType.setText("");
        txtPrice.setText("");
        txtShelfLife.setText("");
        txtRemark.setText("");
    }


    // 调用控制器的loadGrainData方法，加载粮食数据到表格中显示
    public void loadGrainData() {
        if (controller != null) {
            controller.loadGrainData();
        }
    }



    // getter
    public JTextField getTxtGrainCode() {
        return txtGrainCode;
    }

    public JTextField getTxtGrainName() {
        return txtGrainName;
    }

    public JTextField getTxtGrainType() {
        return txtGrainType;
    }

    public JTextField getTxtPrice() {
        return txtPrice;
    }

    public JTextField getTxtShelfLife() {
        return txtShelfLife;
    }

    public JTextArea getTxtRemark() {
        return txtRemark;
    }

    public JTable getGrainTable() {
        return grainTable;
    }

    public DefaultTableModel getTableModel() {
        return tableModel;
    }

    public GrainController getController() {
        return controller;
    }


    public JButton getBtnAdd() {
        return btnAdd;
    }

    public JButton getBtnUpdate() {
        return btnUpdate;
    }

    public JButton getBtnClear() {
        return btnClear;
    }

    public JButton getBtnImport() {
        return btnImport;
    }

    public JButton getBtnCheck() { return btnCheck;}

    public JButton getBtnExport() {
        return btnExport;
    }

    public JButton getBtnDelete() {
        return btnDelete;
    }

    public String getUsername() {
        return username;
    }

    public String getRole() {
        return role;
    }
}
