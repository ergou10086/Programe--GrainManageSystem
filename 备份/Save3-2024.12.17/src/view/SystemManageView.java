package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.io.File;
import java.sql.SQLException;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import entity.User;
import util.LogUtil;
import util.IOFileHelper;

// 负责系统管理的视图类，实现系统和应用设置的ui
public class SystemManageView extends JFrame {
    // 定义颜色方案
    private final Color PRIMARY_COLOR = new Color(246, 174, 90);      // 主色调
    private final Color BACKGROUND_COLOR = new Color(255, 255, 255);  // 背景色
    private final Color BUTTON_COLOR = new Color(66, 133, 244);       // 按钮色
    private final Color TEXT_COLOR = new Color(51, 51, 51);          // 文字色

    // 主要面板
    private JPanel mainPanel;
    private JPanel leftPanel;    // 左侧导航面板
    private JPanel rightPanel;   // 右侧内容面板
    private CardLayout cardLayout;

    // 功能按钮
    private JButton userManageBtn;
    private JButton dataMangementBtn;
    private JButton settingsBtn;
    private JButton logManageBtn;
    private JButton projectLinkBtn;
    private JButton exportLogButton;
    private JButton deleteLogButton;

    // 内容面板
    private JPanel userManagePanel;
    private JPanel backupPanel;
    private JPanel settingsPanel;
    private JPanel logManagePanel;

    // 有关用户信息管理的一些组件
    private JButton deleteAccountBtn;
    private JButton searchUserBtn;
    private JButton modifyAccountInfBtn;
    private JButton modifyPasswordBtn;
    private JTextField searchUserTxt;   // 搜索用户文本框
    private DefaultTableModel userTableModel;
    private JTable userTable;

    // 有关日志表格相关的属性
    private DefaultTableModel logTableModel;
    private JTable logTable;

    // 用户信息
    private String userRole;

    // 添加字段记录当前面板
    private String currentPanel = "";

    // 构造方法
    public SystemManageView(String role) {
        this.userRole = role;
        // 设置窗口基本属性
        setTitle("系统管理");
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        initComponents();
        setupLayout();
    }

    private void initComponents() {
        // 初始化主面板
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(BACKGROUND_COLOR);

        // 初始化左侧导航面板
        leftPanel = new JPanel(new GridLayout(8, 1, 0, 10));
        leftPanel.setBackground(PRIMARY_COLOR);
        leftPanel.setBorder(new EmptyBorder(20, 10, 20, 10));
        leftPanel.setPreferredSize(new Dimension(200, getHeight()));

        // 初始化右侧内容面板
        cardLayout = new CardLayout();
        rightPanel = new JPanel(cardLayout);
        rightPanel.setBackground(BACKGROUND_COLOR);
        rightPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // 初始化按钮
        initButtons();

        // 初始化各个功能面板
        initAccountInfManagePanel();
        initInfoManagerPanel();
        initSettingsPanel();
        initLogManagePanel();

        // 添加面板到卡片布局
        rightPanel.add(userManagePanel, "用户信息管理");
        rightPanel.add(backupPanel, "数据管理");
        rightPanel.add(settingsPanel, "系统设置");
        rightPanel.add(logManagePanel, "日志管理");
    }


    // 创建并设置按钮的初始化方法
    private void initButtons() {
        userManageBtn = createMenuButton("用户信息管理");
        dataMangementBtn = createMenuButton("数据管理");
        settingsBtn = createMenuButton("系统设置");
        logManageBtn = createMenuButton("日志管理");
        projectLinkBtn = createMenuButton("关于项目");

        // 添加按钮到左侧面板
        leftPanel.add(userManageBtn);
        leftPanel.add(dataMangementBtn);
        leftPanel.add(settingsBtn);
        leftPanel.add(logManageBtn);
        leftPanel.add(projectLinkBtn);
    }

    // 创建按钮的方案方法
    private JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(PRIMARY_COLOR);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setHorizontalAlignment(SwingConstants.LEFT);

        // 添加鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_COLOR);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
            }
        });

        return button;
    }


    // 初始化用户信息管理面板
    private void initAccountInfManagePanel() {
        userManagePanel = new JPanel(new BorderLayout(10, 10));
        userManagePanel.setBackground(BACKGROUND_COLOR);

        // 创建功能操作区域
        JPanel operationPanel = new JPanel(new GridBagLayout());
        operationPanel.setBackground(BACKGROUND_COLOR);
        operationPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 创建按钮
        modifyAccountInfBtn = createStyledButton("修改账号信息", new Color(66, 133, 244));
        modifyPasswordBtn = createStyledButton("修改密码", new Color(128, 128, 128));
        deleteAccountBtn = createStyledButton("注销个人账号", new Color(220, 53, 69));
        deleteAccountBtn.setToolTipText("永久删除当前账号并退出系统");

        // 添加按钮到操作面板
        gbc.gridx = 0; gbc.gridy = 0;
        operationPanel.add(modifyAccountInfBtn, gbc);
        gbc.gridx = 1;
        operationPanel.add(modifyPasswordBtn, gbc);
        gbc.gridx = 2;
        operationPanel.add(deleteAccountBtn, gbc);

        // 创建用户名查询区域
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(BACKGROUND_COLOR);
        searchUserBtn = createStyledButton("查询用户", BUTTON_COLOR);
        searchUserBtn.setToolTipText("查询所有用户信息（仅系统管理员可用）");
        searchUserTxt = new JTextField(14);
        searchPanel.add(new JLabel("搜索用户："));
        searchPanel.add(searchUserTxt);
        searchPanel.add(searchUserBtn);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.gridwidth = 3;
        operationPanel.add(searchPanel, gbc);


        // 创建用户表格
        String[] columnNames = {"用户ID", "用户名", "角色", "创建时间"};
        userTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 使表格不可编辑
            }
        };
        userTable = new JTable(userTableModel);
        userTable.setRowHeight(30);
        userTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));

        // 设置操作列的渲染器和编辑器
        //userTable.getColumnModel().getColumn(4).setCellRenderer(new ButtonRenderer());
        //userTable.getColumnModel().getColumn(4).setCellEditor(new ButtonEditor(new JCheckBox()));

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        userManagePanel.add(operationPanel, BorderLayout.NORTH);
        userManagePanel.add(scrollPane, BorderLayout.CENTER);
    }



    // 初始化数据管理面板
    private void initInfoManagerPanel() {
        backupPanel = new JPanel(new BorderLayout(10, 10));
        backupPanel.setBackground(BACKGROUND_COLOR);

        // 创建功能按钮面板
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(BACKGROUND_COLOR);
        buttonPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 创建功能按钮
        JButton[] buttons = {
                createDataButton("数据库备份", "备份整个系统数据", "backup.sql"),
                createDataButton("导出粮食信息", "导出粮食数据表", "grain_info.xlsx"),
                createDataButton("导出仓库信息", "导出仓库数据表", "warehouse_info.xlsx"),
                createDataButton("导出巡库信息", "导出巡库记录表", "inspection_info.xlsx")
        };

        // 布局按钮
        for (int i = 0; i < buttons.length; i++) {
            gbc.gridx = i % 2;
            gbc.gridy = i / 2;
            buttonPanel.add(buttons[i], gbc);
        }

        // 创建操作反馈区域
        JTextArea feedbackArea = new JTextArea();
        feedbackArea.setEditable(false);
        feedbackArea.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        feedbackArea.setBorder(BorderFactory.createTitledBorder("操作反馈"));
        JScrollPane feedbackScroll = new JScrollPane(feedbackArea);

        backupPanel.add(buttonPanel, BorderLayout.NORTH);
        backupPanel.add(feedbackScroll, BorderLayout.CENTER);
    }



    // 初始化系统设置面板
    private void initSettingsPanel() {
        settingsPanel = new JPanel(new GridBagLayout());
        settingsPanel.setBackground(BACKGROUND_COLOR);
        settingsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.gridwidth = GridBagConstraints.REMAINDER;

        // 主题设置
        JPanel themePanel = createSettingsSection("主题设置");
        String[] themes = {"浅色主题", "深色主题", "高对比度"};
        JComboBox<String> themeCombo = new JComboBox<>(themes);
        themePanel.add(themeCombo);

        // 语言设置
        JPanel languagePanel = createSettingsSection("语言设置");
        String[] languages = {"简体中文", "English", "日本語"};
        JComboBox<String> languageCombo = new JComboBox<>(languages);
        languagePanel.add(languageCombo);

        // 显示设置
        JPanel displayPanel = createSettingsSection("显示设置");
        String[] resolutions = {"1920x1080", "1600x900", "1366x768"};
        JComboBox<String> resolutionCombo = new JComboBox<>(resolutions);
        displayPanel.add(new JLabel("分辨率："));
        displayPanel.add(resolutionCombo);

        // 字体设置
        JPanel fontPanel = createSettingsSection("字体设置");
        String[] fontSizes = {"小", "中", "大"};
        JComboBox<String> fontCombo = new JComboBox<>(fontSizes);
        fontPanel.add(new JLabel("字体大小："));
        fontPanel.add(fontCombo);

        // 添加所有设置面板
        gbc.gridy = 0; settingsPanel.add(themePanel, gbc);
        gbc.gridy = 1; settingsPanel.add(languagePanel, gbc);
        gbc.gridy = 2; settingsPanel.add(displayPanel, gbc);
        gbc.gridy = 3; settingsPanel.add(fontPanel, gbc);

        // 添加保存按钮
        JButton saveButton = createStyledButton("保存设置", BUTTON_COLOR);
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        settingsPanel.add(saveButton, gbc);
    }



    // 初始化日志管理面板
    private void initLogManagePanel() {
        logManagePanel = new JPanel(new BorderLayout(10, 10));
        logManagePanel.setBackground(BACKGROUND_COLOR);

        // 创建工具栏
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        toolBar.setBackground(BACKGROUND_COLOR);

        exportLogButton = createStyledButton("导出日志", BUTTON_COLOR);
        deleteLogButton = createStyledButton("删除日志", new Color(220, 53, 69));
        deleteLogButton.setToolTipText("删除指定的日志记录（仅系统管理员和仓库管理员可用）");

        toolBar.add(exportLogButton);
        toolBar.add(deleteLogButton);

        // 创建日志表格
        String[] columnNames = {"日志ID", "时间", "用户", "操作", "详情"};
        logTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // 使表格不可编辑
            }
            // 获取表格ID列的数据
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 0) return Integer.class;
                return String.class;
            }
        };

        // 设置表格形式的数据
        logTable = new JTable(logTableModel);
        logTable.setRowHeight(30);
        logTable.getTableHeader().setFont(new Font("微软雅黑", Font.BOLD, 14));


        // 设置表格的滚动面板
        JScrollPane scrollPane = new JScrollPane(logTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // 安放工具栏
        logManagePanel.add(toolBar, BorderLayout.NORTH);
        logManagePanel.add(scrollPane, BorderLayout.CENTER);
    }


    // 组装
    private void setupLayout() {
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
        setContentPane(mainPanel);
    }


    // 辅助方法，创建数据管理页面的样式化按钮
    private JButton createDataButton(String text, String tooltip, String fileFormat) {
        JButton button = new JButton(text);
        button.setToolTipText(tooltip);
        button.setBackground(BUTTON_COLOR);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        button.addActionListener(e -> showExportDialog(text, fileFormat));
        return button;
    }


    // 显示导出位置对话框的辅助方法，需要修改
    private void showExportDialog(String operation, String fileFormat) {
        // 获取当前用户的主目录,在应用程序中指定默认的文件下载或保存位置。
        String defaultPath = System.getProperty("user.home") + "/Downloads/";
        String message = String.format("数据将导出到：%s\n文件格式：%s", defaultPath, fileFormat);
        JOptionPane.showMessageDialog(this, message, operation, JOptionPane.INFORMATION_MESSAGE);
    }


    // 这里是切换右侧面板的功能类
    private JPanel createSettingsSection(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setBackground(BACKGROUND_COLOR);
        panel.setBorder(BorderFactory.createTitledBorder(title));
        return panel;
    }


    // 创建样式化按钮的辅助方法
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setPreferredSize(new Dimension(120, 35));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor.darker());
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }


    // 添加获取搜索文本的方法
    public String getSearchKeyword() {
        return searchUserTxt.getText();
    }

    // 添加按钮监听器
    public void addUserManageListener(ActionListener listener) {
        userManageBtn.addActionListener(listener);
    }
    public void addBackupListener(ActionListener listener) {
        dataMangementBtn.addActionListener(listener);
    }
    public void addSettingsListener(ActionListener listener) {
        settingsBtn.addActionListener(listener);
    }
    public void addLogManageListener(ActionListener listener) {
        logManageBtn.addActionListener(listener);
    }
    public void addModifyAccountListener(ActionListener listener) {
        modifyAccountInfBtn.addActionListener(listener);
    }

    public void addModifyPasswordListener(ActionListener listener) {
        modifyPasswordBtn.addActionListener(listener);
    }
    public void addProjectLinkListener(ActionListener listener) {
        projectLinkBtn.addActionListener(listener);
    }
    public void addExportLogButtonListener(ActionListener listener) {
        // 添加导出按钮事件
        exportLogButton.addActionListener(listener);
    }
    public void addDeleteLogButtonListener(ActionListener listener) {
        // 添加删除按钮事件
        deleteLogButton.addActionListener(listener);
    }



    // 切换面板方法
    public void showPanel(String panelName) {
        if ("关于项目".equals(panelName)) {
            // 如果是显示关于项目面板，直接返回，因为这里是控制器直接处理
            return;
        }

        if ("browser".equals(currentPanel)) {
            // 如果当前是浏览器面板，需要重新设置布局
            rightPanel.removeAll();
            rightPanel.setLayout(cardLayout);
            // 重新添加所有面板
            rightPanel.add(userManagePanel, "用户信息管理");
            rightPanel.add(backupPanel, "数据管理");
            rightPanel.add(settingsPanel, "系统设置");
            rightPanel.add(logManagePanel, "日志管理");
        }
        cardLayout.show(rightPanel, panelName);
        currentPanel = panelName;
    }


    // 为按钮添加监听器和设置动效
    public void addDeleteAccountListener(ActionListener listener) {
        deleteAccountBtn.addActionListener(listener);
    }
    public void addSearchUserListener(ActionListener listener) {
        searchUserBtn.addActionListener(listener);
    }
    public void setSearchUserButtonEnabled(boolean enabled) {
        searchUserBtn.setEnabled(enabled);
    }
    // 添加鼠标悬停提示信息的功能
    public void setSearchUserButtonTooltip(String tooltip) {
        searchUserBtn.setToolTipText(tooltip);
    }


    // 更新用户表格的方法
    public void updateUserTable(List<User> users) {
        userTableModel.setRowCount(0); // 清空表格
        for (User user : users) {
            userTableModel.addRow(new Object[]{
                    user.getUserId(),
                    user.getUsername(),
                    user.getRole(),
                    ""   // 显示创建时间，可以添加相应的字段
            });
        }
    }



    // 添加获取日志表格模型的方法
    public DefaultTableModel getLogTableModel() {
        return logTableModel;
    }

    // 添加获取日志表格的方法
    public JTable getLogTable() {
        return logTable;
    }

    // 添加获取右侧面板的方法
    public JPanel getRightPanel() {
        return rightPanel;
    }

    // 添加 getter 和 setter
    public String getCurrentPanel() {
        return currentPanel;
    }

    public void setCurrentPanel(String panel) {
        this.currentPanel = panel;
    }

    public void setLogTableModel(DefaultTableModel logTableModel) {
        this.logTableModel = logTableModel;
    }

    public void setLogTable(JTable logTable) {
        this.logTable = logTable;
    }
}