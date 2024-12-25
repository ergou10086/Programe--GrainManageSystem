package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

// 注册页面视图类
public class RegisterView extends JFrame {
    // 属性
    private JTextField usernameField;  // 用户名输入框
    private JPasswordField passwordField;   // 密码输入框
    private JPasswordField confirmPasswordField;   // 确认密码输入框
    private JComboBox<String> roleComboBox;    // 注册人员类别多选框
    private JButton registerButton;  // 注册按钮
    private JButton backButton;    // 返回按钮

    // 构造方法
    public RegisterView() {
        initComponents();  // 初始化组件
        setupLayout();     // 布局设置
        setupWindow();     // 窗口设置
    }

    private void initComponents() {
        // 初始化组件
        usernameField = new JTextField(20);
        passwordField = new JPasswordField(20);
        confirmPasswordField = new JPasswordField(20);
        String[] roles = {"仓库管理员", "系统管理员", "巡检员", "普通用户"};
        roleComboBox = new JComboBox<>(roles);
        registerButton = new JButton("注册");
        backButton = new JButton("返回登录");

        // 设置组件字体样式
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 14);
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 14);

        // 设置组件字体和大小
        Dimension fieldSize = new Dimension(250, 30);
        usernameField.setPreferredSize(fieldSize);
        passwordField.setPreferredSize(fieldSize);
        confirmPasswordField.setPreferredSize(fieldSize);
        roleComboBox.setPreferredSize(fieldSize);

        // 设置组件字体
        usernameField.setFont(labelFont);
        passwordField.setFont(labelFont);
        confirmPasswordField.setFont(labelFont);
        roleComboBox.setFont(labelFont);
        registerButton.setFont(buttonFont);
        backButton.setFont(buttonFont);

        // 设置注册按钮样式
        registerButton.setBackground(new Color(66, 133, 244));
        registerButton.setForeground(Color.WHITE);
        registerButton.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        registerButton.setFocusPainted(false);

        // 设置返回按钮样式
        backButton.setBackground(new Color(230, 131, 131));
        backButton.setForeground(new Color(66, 133, 244));
        backButton.setBorder(BorderFactory.createLineBorder(new Color(66, 133, 244), 1));
        backButton.setFocusPainted(false);

        // 添加按钮悬停效果
        registerButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(4, 75, 253));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                registerButton.setBackground(new Color(66, 133, 244));
            }
        });

        backButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                backButton.setBackground(new Color(242, 242, 242));
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                backButton.setBackground(Color.WHITE);
            }
        });
    }

    private void setupLayout() {
        // 创建主面板
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // 创建标题面板
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(162, 243, 176));
        JLabel titleLabel = new JLabel("用户注册");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(33, 33, 33));
        titlePanel.add(titleLabel);

        // 创建表单面板
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(209, 244, 155));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 创建标签并设置样式
        JLabel userLabel = createStyledLabel("用户名：");
        JLabel passLabel = createStyledLabel("密码：");
        JLabel confirmLabel = createStyledLabel("确认密码：");
        JLabel roleLabel = createStyledLabel("用户角色：");

        // 添加组件到表单
        addFormField(formPanel, userLabel, usernameField, gbc, 0);
        addFormField(formPanel, passLabel, passwordField, gbc, 1);
        addFormField(formPanel, confirmLabel, confirmPasswordField, gbc, 2);
        addFormField(formPanel, roleLabel, roleComboBox, gbc, 3);

        // 创建按钮面板
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(registerButton);
        buttonPanel.add(backButton);

        // 组装主面板
        mainPanel.add(titlePanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 设置内容面板
        this.setContentPane(mainPanel);
    }


    // 创建面板类
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        label.setForeground(new Color(66, 66, 66));
        return label;
    }


    // 添加表单字段的方法
    private void addFormField(JPanel panel, JLabel label, JComponent field, GridBagConstraints gbc, int row) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.anchor = GridBagConstraints.EAST;
        panel.add(label, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        panel.add(field, gbc);
    }

    // 设置窗口初始化必要方法
    private void setupWindow() {
        // 设置窗口属性
        this.setTitle("注册 - 粮库管理系统");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();
        this.setLocationRelativeTo(null);
    }


    // 添加事件监听器的方法，为按钮添加监听器
    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public void addBackListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }


    // getter和setter方法
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public String getConfirmPassword() {
        return new String(confirmPasswordField.getPassword());
    }

    public String getSelectedRole() {
        return (String) roleComboBox.getSelectedItem();
    }


    // 显示错误消息
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }


    // 清除输入
    public void clearInputs() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }
}