package controller;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import entity.User;
import exceptions.*;
import service.IUserService;
import service.impl.UserServiceImpl;
import util.IOFileHelper;
import util.LogUtil;
import view.BrowserPanel;
import view.SystemManageView;

// 系统管理控制器类, 负责系统管理的逻辑处理
public class SystemManageController {
    private SystemManageView systemManageView;
    private String currentUser;
    private String userRole;
    private IUserService userService;
    private MainMenuController mainMenuController;   // 用于控制主窗口

    public SystemManageController(String username, String role, MainMenuController mainMenuController) {
        this.currentUser = username;
        this.userRole = role;
        this.mainMenuController = mainMenuController;
        this.userService = new UserServiceImpl();

        // 初始化视图
        systemManageView = new SystemManageView(role);

        // 添加按钮监听器
        initializeListeners();

        // 权限设置
        setupPermissions();

        // 显示窗口
        systemManageView.setVisible(true);
    }



    // 初始化按钮的控制器
    private void initializeListeners() {
        // 切换页面
        systemManageView.addUserManageListener(e -> showUserManage());
        systemManageView.addBackupListener(e -> showBackup());
        systemManageView.addSettingsListener(e -> showSettings());
        systemManageView.addLogManageListener(e -> showLogManage());

        // 添加注销账号监听器
        systemManageView.addDeleteAccountListener(e -> handleDeleteAccount());

        // 添加查询用户监听器
        systemManageView.addSearchUserListener(e -> handleSearchUsers());

        // 添加修改账号信息和修改密码的监听器
        systemManageView.addModifyAccountListener(e -> handleModifyAccount());
        systemManageView.addModifyPasswordListener(e -> handleModifyPassword());

        // 添加项目链接监听器
        systemManageView.addProjectLinkListener(e -> showProjectLink());

        // 添加导出日志按钮事件
        systemManageView.addExportLogButtonListener(e -> handleExportLog());
        // 添加删除日志按钮事件
        systemManageView.addDeleteLogButtonListener(e -> handleDeleteLog());
    }


    // 注销账号监听器方法
    private void handleDeleteAccount() {
        int confirm = JOptionPane.showConfirmDialog(systemManageView,
        "确定要注销账号吗？此操作不可恢复！",
                "确认注销",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if(confirm == JOptionPane.YES_OPTION) {
            try{
                if(userService.deleteUser(currentUser, userRole)){
                    // 记录日志（暂时注释掉，等创建 LogUtil 类后再启用）
                    // LogUtil.recordLog(currentUser, "账号注销", "用户" + currentUser + "注销了账号");
                    
                    JOptionPane.showMessageDialog(systemManageView, "账号已注销，系统将退出", "注销成功", JOptionPane.INFORMATION_MESSAGE);
                    
                    // 关闭系统管理窗口
                    systemManageView.dispose();
                    
                    // 调用主菜单控制器的登出方法
                    mainMenuController.logout();
                }else{
                    JOptionPane.showMessageDialog(systemManageView, "注销账号失败,请咨询李鹏飞", "错误", JOptionPane.ERROR_MESSAGE);
                }
            }catch(Exception ex){
                JOptionPane.showMessageDialog(systemManageView, "注销账号时发生错误：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                try {
                    throw new LoginException.LogoutException(ex.getMessage());
                } catch (LoginException.LogoutException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }



    // 查询用户监听器方法
    private void handleSearchUsers() {
        if(!"系统管理员".equals(userRole)){
            JOptionPane.showMessageDialog(systemManageView,
                    "只有系统管理员可以查询用户信息",
                    "权限不足",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        try{
            String keyword = systemManageView.getSearchKeyword();
            List<User> users;

            // 空输入显示全部
            if(keyword == null || keyword.trim().isEmpty()) {
                users = userService.getAllUsers();
            }else{
                // 执行搜索
                users = userService.searchUsers(keyword);
            }

            if (users.isEmpty()) {
                JOptionPane.showMessageDialog(systemManageView, "未找到匹配的用户", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            }

            systemManageView.updateUserTable(users);
        }catch (Exception e) {
            JOptionPane.showMessageDialog(systemManageView, "查询用户信息失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            throw new RegisterExceptions.QueryUserException(e.getMessage());
        }
    }


    // 检查权限设置
    private void setupPermissions() {
        boolean isAdmin = "系统管理员".equals(userRole);
        systemManageView.setSearchUserButtonEnabled(isAdmin);
        if (!isAdmin) {
            systemManageView.setSearchUserButtonTooltip("只有系统管理员可以查询用户信息");
        }
    }

    private void showUserManage() {
        if (systemManageView.getCurrentPanel().equals("browser")) {
            // 如果当前是浏览器面板，需要特殊处理
            systemManageView.getRightPanel().removeAll();
            systemManageView.getRightPanel().setLayout(new BorderLayout());
        }
        systemManageView.showPanel("用户信息管理");
        systemManageView.setCurrentPanel("user");
    }

    private void showBackup() {
        systemManageView.showPanel("数据管理");
    }

    private void showSettings() {
        systemManageView.showPanel("系统设置");
    }

    private void showLogManage() {
        systemManageView.showPanel("日志管理");

        try{
            // 获取最近的日志记录
            List<Map<String, Object>> logs = LogUtil.getLogsByTimeRange(
                    "1970-01-01 00:00:00",  // 从最早时间开始
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) // 到现在
            );
            // 更新日志表格
            updateLogTable(logs);
        }catch (SQLException e) {
            JOptionPane.showMessageDialog(systemManageView, "加载日志失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            try {
                throw new LogUtilExceptions.LogLoadException(e.getMessage());
            } catch (LogUtilExceptions.LogLoadException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    // 处理修改账号信息
    private void handleModifyAccount() {
        // 创建一个自定义对话框
        JDialog dialog = new JDialog(systemManageView, "修改账号信息", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(systemManageView);

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 用户名输入框
        JTextField usernameField = new JTextField(20);
        usernameField.setText(currentUser);
        
        // 角色选择下拉框
        String[] roles = {"普通用户", "系统管理员", "巡检员", "仓库管理员"};
        JComboBox<String> roleCombo = new JComboBox<>(roles);
        roleCombo.setSelectedItem(userRole);

        // 添加组件到表单
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("新用户名:"), gbc);
        gbc.gridx = 1;
        formPanel.add(usernameField, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("新角色:"), gbc);
        gbc.gridx = 1;
        formPanel.add(roleCombo, gbc);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");

        // 添加按钮事件
        confirmButton.addActionListener(e -> {
            String newUsername = usernameField.getText().trim();
            String newRole = (String) roleCombo.getSelectedItem();
            
            try {
                // 检查新用户名是否已存在
                if (!newUsername.equals(currentUser) && userService.isUsernameExists(newUsername)) {
                    JOptionPane.showMessageDialog(dialog, "用户名已存在！", "错误", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                if (userService.updateUserInfo(currentUser, userRole, newUsername, newRole)) {
                    JOptionPane.showMessageDialog(dialog, "修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    currentUser = newUsername; // 更新当前用户名
                    userRole = newRole; // 更新当前角色
                    dialog.dispose();
                    handleSearchUsers(); // 刷新用户列表
                } else {
                    JOptionPane.showMessageDialog(dialog, "修改失败", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "修改失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                throw new RegisterExceptions.UpdateUserException(ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        // 组装对话框
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }

    // 处理修改密码
    private void handleModifyPassword() {
        JDialog dialog = new JDialog(systemManageView, "修改密码", true);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(systemManageView);

        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JPasswordField oldPasswordField = new JPasswordField(20);
        JPasswordField newPasswordField = new JPasswordField(20);
        JPasswordField confirmPasswordField = new JPasswordField(20);

        // 添加组件到表单
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("原密码:"), gbc);
        gbc.gridx = 1;
        formPanel.add(oldPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("新密码:"), gbc);
        gbc.gridx = 1;
        formPanel.add(newPasswordField, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("确认新密码:"), gbc);
        gbc.gridx = 1;
        formPanel.add(confirmPasswordField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton confirmButton = new JButton("确认");
        JButton cancelButton = new JButton("取消");

        confirmButton.addActionListener(e -> {
            String oldPassword = new String(oldPasswordField.getPassword());
            String newPassword = new String(newPasswordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());

            // 验证输入
            if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "所有字段都必须填写！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!newPassword.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(dialog, "两次输入的新密码不一致！", "错误", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                if (userService.updatePassword(currentUser, oldPassword, newPassword)) {
                    JOptionPane.showMessageDialog(dialog, "密码修改成功！", "成功", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                }
            } catch (ServiceException ex) {
                throw new RuntimeException(ex);
            }
        });

        cancelButton.addActionListener(e -> dialog.dispose());

        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);

        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        dialog.setVisible(true);
    }


    // 关于项目按钮的监听器
    private void showProjectLink() {
        try {
            // 创建浏览器面板
            BrowserPanel browserPanel = new BrowserPanel();
            
            // 清空右侧面板内容
            systemManageView.getRightPanel().removeAll();
            
            // 将浏览器面板添加到右侧内容区域
            systemManageView.getRightPanel().setLayout(new BorderLayout());
            systemManageView.getRightPanel().add(browserPanel, BorderLayout.CENTER);

            // 记录当前面板为浏览器面板
            systemManageView.setCurrentPanel("browser");
            
            // 刷新显示
            systemManageView.getRightPanel().revalidate();
            systemManageView.getRightPanel().repaint();
        } catch (Exception e) {
            System.err.println("加载浏览器面板失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(systemManageView, "加载浏览器失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            try {
                throw new JavaFxExceptions.webViewInitError(e.getMessage());
            } catch (JavaFxExceptions.webViewInitError ex) {
                throw new RuntimeException(ex);
            }
        }
    }





    // 处理导出日志监听器
    private void handleExportLog() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择保存位置");

        // 添加文件类型选择
        String[] options = {"Excel文件(*.xlsx)", "文本文件(*.txt)"};
        String fileType = (String) JOptionPane.showInputDialog(systemManageView,
                "请选择导出文件格式：",
                "选择格式",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (fileType == null) return;

        // 设置文件扩展名
        String extension = fileType.contains("Excel") ? ".xlsx" : ".txt";
        fileChooser.setSelectedFile(new File("日志信息" + extension));

        if(fileChooser.showSaveDialog(systemManageView) == JFileChooser.APPROVE_OPTION) {
            try {
                // 获取用户选择的文件路径
                String filePath = fileChooser.getSelectedFile().getPath();
                // 如果用户没有选择文件扩展名，则自动添加
                if (!filePath.toLowerCase().endsWith(extension)) {
                    filePath += extension;
                }
                // 根据选择的格式导出
                if (extension.equals(".xlsx")) {
                    IOFileHelper.exportLogsToExcel(filePath);
                } else {
                    IOFileHelper.exportLogsToTxt(filePath);
                }
                JOptionPane.showMessageDialog(systemManageView, "日志导出成功！");
            }catch (Exception ex) {
                JOptionPane.showMessageDialog(systemManageView, "导出日志失败：" + ex.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                try {
                    throw new IOExceptions.ExportException(ex.getMessage());
                } catch (IOExceptions.ExportException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    // 添加删除日志的事件监听器方法
    private void handleDeleteLog() {
        if (!("系统管理员".equals(userRole) || "仓库管理员".equals(userRole))) {
            JOptionPane.showMessageDialog(systemManageView,
                    "只有系统管理员和仓库管理员可以删除日志",
                    "权限不足",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 获取选中的行
        int selectedRow = systemManageView.getLogTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(systemManageView,
                    "请先选择要删除的日志记录",
                    "提示",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        try {
            int logId = LogUtil.getLogId(selectedRow, systemManageView.getLogTableModel());

            int confirm = JOptionPane.showConfirmDialog(systemManageView,
                    "确定要删除ID为 " + logId + " 的日志记录吗？",
                    "确认删除",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (LogUtil.deleteLog(logId)) {
                        JOptionPane.showMessageDialog(systemManageView, "日志删除成功！");
                        // 刷新日志表格
                        LogUtil.updateLogTable(systemManageView.getLogTableModel());
                    } else {
                        JOptionPane.showMessageDialog(systemManageView, "删除失败：未找到指定的日志记录", "错误", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } catch (Exception e) {
            JOptionPane.showMessageDialog(systemManageView, "删除日志失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            try {
                throw new LogUtilExceptions.LogLoadException(e.getMessage());
            } catch (LogUtilExceptions.LogLoadException ex) {
                throw new RuntimeException(ex);
            }
        }
    }


    // 更新日志表格
    private void updateLogTable(List<Map<String, Object>> logs) {
        DefaultTableModel model = systemManageView.getLogTableModel();
        model.setRowCount(0);
        for (Map<String, Object> log : logs) {
            model.addRow(new Object[]{
                    ((Number) log.get("log_id")).intValue(),  // 第一列显示log_id
                    log.get("log_time"),                      // 第二列显示时间
                    log.get("username"),                      // 第三列显示用户名
                    log.get("operation"),                     // 第四列显示操作类型
                    log.get("detail")                         // 第五列显示详情
            });
        }
    }
}