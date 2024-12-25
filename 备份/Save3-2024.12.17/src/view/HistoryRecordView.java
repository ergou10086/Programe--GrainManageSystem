package view;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

import javax.swing.table.DefaultTableModel;
import javax.swing.ImageIcon;

import com.toedter.calendar.JDateChooser;
import java.text.SimpleDateFormat;
import java.util.Date;

// 用于展示各种历史记录相关的界面组件
public class HistoryRecordView extends JPanel {
    // 用于存放不同选项卡的面板，实现多页面切换展示不同历史记录的功能
    private JTabbedPane tabbedPane;
    // 粮食信息历史记录对应的表格
    private JTable grainHistoryTable;
    // 粮库信息历史记录对应的表格
    private JTable warehouseHistoryTable;
    // 出入库历史记录对应的表格
    private JTable storageHistoryTable;
    // 巡检历史记录对应的表格
    private JTable inspectionHistoryTable;
    // 粮食信息历史记录对应的表格模型，用于管理表格的数据、结构等
    private DefaultTableModel grainModel;
    // 粮库信息历史记录对应的表格模型
    private DefaultTableModel warehouseModel;
    // 出入库历史记录对应的表格模型
    private DefaultTableModel storageModel;
    // 巡检历史记录对应的表格模型
    private DefaultTableModel inspectionModel;
    // 右键菜单
    private JPopupMenu popupMenu;
    // 恢复选项
    private JMenuItem restoreMenuItem;
    // 删除选项
    private JMenuItem deleteMenuItem;

    // 控件组
    private Map<String, JTextField> startDateFields = new HashMap<>();
    private Map<String, JTextField> endDateFields = new HashMap<>();
    private Map<String, JButton> searchButtons = new HashMap<String, JButton>();
    private Map<String, JButton> searchAllButtons= new HashMap<String, JButton>();

    // 修改日期选择器相关的字段
    private Map<String, JDateChooser> startDateChoosers = new HashMap<String, JDateChooser>();
    private Map<String, JDateChooser> endDateChoosers = new HashMap<String, JDateChooser>();

    // 日期格式化器
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");;

    // 单独的删除按钮
    private JButton deleteRecordBtn = new JButton("删除记录");;

    // 构造函数
    public HistoryRecordView() {
        // 布局为BorderLayout
        setLayout(new BorderLayout());
        // 调用初始化组件的方法
        initComponents();
    }


    // 初始化界面组件
    private void initComponents() {
        tabbedPane = new JTabbedPane();

        // 粮食信息历史记录相关的面板
        JPanel grainHistoryPanel = createHistoryPanel("粮食信息历史记录",
                new String[]{"操作时间", "操作类型", "粮食编号", "粮食名称", "操作人", "详细信息"});

        // 粮库信息历史记录相关的面板
        JPanel warehouseHistoryPanel = createHistoryPanel("粮库信息历史记录",
                new String[]{"操作时间", "操作类型", "仓库编号", "仓库名称", "操作人", "详细信息"});

        // 出入库历史记录相关的面板
        JPanel storageHistoryPanel = createHistoryPanel("出入库历史记录",
                new String[]{"操作时间", "操作类型", "粮食名称","重量","仓库", "司机","司机联系方式"});

        JPanel inventoryHistoryPanel = createHistoryPanel("预警历史记录",
                new String[]{"预警时间", "仓库编号", "仓库编号","仓库地址", "问题描述"});
        // 巡检历史记录相关的面板

        // 将查看各项历史记录面板添加到选项卡面板中
        tabbedPane.addTab("粮食信息历史", grainHistoryPanel);
        tabbedPane.addTab("粮库信息历史", warehouseHistoryPanel);
        tabbedPane.addTab("出入库历史", storageHistoryPanel);
        tabbedPane.addTab("预警历史",inventoryHistoryPanel);

        // 将包含各个选项卡的tabbedPane添加到当前面板的中心位置（BorderLayout布局中的CENTER区域）
        add(tabbedPane, BorderLayout.CENTER);
    }


    // 用于创建一个带有特定标题和表格列名的历史记录面板的私有方法，内部会创建表格、搜索面板等组件并进行布局设置
    private JPanel createHistoryPanel(String title, String[] columns) {
        JPanel panel = new JPanel(new BorderLayout());

        // 创建一个默认的表格模型，传入列名数组和初始行数（这里初始行数为0，表示先没有数据）
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        // 根据创建的表格模型创建对应的JTable表格对象
        JTable table = new JTable(model);

        // 根据传入的标题判断是哪种类型的历史记录，将对应的表格和表格模型进行保存引用，方便后续在外部获取和操作
        if (title.contains("粮食信息")) {
            grainHistoryTable = table;
            grainModel = model;
        } else if (title.contains("粮库信息")) {
            warehouseHistoryTable = table;
            warehouseModel = model;
        } else if (title.contains("出入库")) {
            storageHistoryTable = table;
            storageModel = model;
        } else if (title.contains("预警")) {
            inspectionHistoryTable = table;
            inspectionModel = model;
        }

        // 表格添加到滚动面板中
        JScrollPane scrollPane = new JScrollPane(table);

        // 创建日期选择器
        JDateChooser startDateChooser = new JDateChooser();
        JDateChooser endDateChooser = new JDateChooser();

        // 设置日期选择器大小和格式
        Dimension dateSize = new Dimension(120, 25);
        startDateChooser.setPreferredSize(dateSize);
        endDateChooser.setPreferredSize(dateSize);
        startDateChooser.setDateFormatString("yyyy-MM-dd");
        endDateChooser.setDateFormatString("yyyy-MM-dd");

        // 获取面板标识
        String panelKey = getPanelKey(title);

        // 将日期选择器存入Map
        startDateChoosers.put(panelKey, startDateChooser);
        endDateChoosers.put(panelKey, endDateChooser);

        // 创建搜索按钮
        JButton searchBtn = new JButton("查询");
        JButton searchAllBtn = new JButton("查询全部信息");

        // 将按钮存入Map
        searchButtons.put(panelKey, searchBtn);
        searchAllButtons.put(panelKey, searchAllBtn);

        // 创建搜索面板并添加组件
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(new Color(240, 240, 240));
        searchPanel.add(new JLabel("开始日期："));
        searchPanel.add(startDateChooser);
        searchPanel.add(new JLabel("结束日期："));
        searchPanel.add(endDateChooser);
        searchPanel.add(searchBtn);
        searchPanel.add(searchAllBtn);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        deleteRecordBtn.setBackground(new Color(255, 99, 71));
        deleteRecordBtn.setForeground(Color.WHITE);
        buttonPanel.add(deleteRecordBtn); // 确保按钮被添加到面板
        // 将按钮面板添加到主面板
        add(buttonPanel, BorderLayout.SOUTH);


        // 添加面板
        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);


        // 创建右键菜单
        popupMenu = new JPopupMenu();
        restoreMenuItem = new JMenuItem("恢复记录");
        deleteMenuItem = new JMenuItem("彻底删除");

        // 添加工具提示
        restoreMenuItem.setToolTipText("恢复已删除的记录");
        deleteMenuItem.setToolTipText("永久删除记录及其历史");

        popupMenu.add(restoreMenuItem);
        popupMenu.add(deleteMenuItem);

        return panel;
    }


    // 删除恢复相关信息菜单弹出的鼠标监听器
    private void addTableMouseListener(JTable table) {
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) { // 检测右键点击
                    int row = table.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        table.setRowSelectionInterval(row, row);
                        // 只有当操作类型为"删除"时才显示菜单
                        String changeType = (String) table.getValueAt(row, 1);
                        if ("删除".equals(changeType)) {
                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            }
        });
    }


    // 获取面板标题
    private String getPanelKey(String title) {
        if (title.contains("粮食信息")) return "grain";
        if (title.contains("粮库信息")) return "warehouse";
        if (title.contains("出入库")) return "storage";
        if (title.contains("预警")) return "inventory";
        return "";
    }

    // 获取日期输入值的方法
    public String getStartDate(String panelName) {
        JDateChooser chooser = startDateChoosers.get(panelName);
        if (chooser != null && chooser.getDate() != null) {
            return dateFormat.format(chooser.getDate());
        }
        return "";
    }

    public String getEndDate(String panelName) {
        JDateChooser chooser = endDateChoosers.get(panelName);
        if (chooser != null && chooser.getDate() != null) {
            return dateFormat.format(chooser.getDate());
        }
        return "";
    }

    // 获取按钮的方法
    public Map<String, JButton> getSearchButtons() {
        return searchButtons;
    }

    public Map<String, JButton> getSearchAllButtons() {
        return searchAllButtons;
    }


    // 修改获取日期的方法
    public String getStartDateFromDC(String panelName) {
        JDateChooser chooser = startDateChoosers.get(panelName);
        if (chooser != null && chooser.getDate() != null) {
            return dateFormat.format(chooser.getDate());
        }
        return "";
    }

    public String getEndDateFromDC(String panelName) {
        JDateChooser chooser = endDateChoosers.get(panelName);
        if (chooser != null && chooser.getDate() != null) {
            return dateFormat.format(chooser.getDate());
        }
        return "";
    }


    // 添加设置菜单项可用性的方法
    public void setMenuItemsEnabled(boolean enabled) {
        restoreMenuItem.setEnabled(enabled);
        deleteMenuItem.setEnabled(enabled);
    }

    // 添加设置菜单项工具提示的方法
    public void setMenuItemsTooltip(String tooltip) {
        restoreMenuItem.setToolTipText(tooltip);
        deleteMenuItem.setToolTipText(tooltip);
    }


    // getter
    public JTable getGrainHistoryTable() { return grainHistoryTable; }
    public JTable getWarehouseHistoryTable() { return warehouseHistoryTable; }
    public JTable getStorageHistoryTable() { return storageHistoryTable; }
    public JTable getInspectionHistoryTable() { return inspectionHistoryTable; }

    public DefaultTableModel getGrainModel() { return grainModel; }
    public DefaultTableModel getWarehouseModel() { return warehouseModel; }
    public DefaultTableModel getStorageModel() { return storageModel; }
    public DefaultTableModel getInspectionModel() { return inspectionModel; }

    public JMenuItem getRestoreMenuItem() {
        return restoreMenuItem;
    }

    public JMenuItem getDeleteMenuItem() {
        return deleteMenuItem;
    }

    // 添加获取删除按钮的方法
    public JButton getDeleteRecordBtn() {
        return deleteRecordBtn;
    }
}