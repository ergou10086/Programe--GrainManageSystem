package view;

import exceptions.IOExceptions;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.border.EmptyBorder;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

// 登录视图类，实现登录的视图界面
public class LoginView extends JFrame {
    private JTextField usernameField;    // 用户名文本框
    private JPasswordField passwordField; // 密码文本框
    private JButton loginButton;    // 登录按钮
    private JButton registerButton;  // 注册按钮
    private Image backgroundImage; // 背景图片
    // 记住密码和7天内无需登录的单选框和选项组
    private JRadioButton rememberPasswordRadioButton;
    private JRadioButton noLoginRequiredRadioButton;

    // 构造方法
    public LoginView() {
        initComponents();  // 初始化组件
        setupLayout();     // 设置布局
        setupWindow();     // 设置窗口
    }

    // 设置窗口的属性
    private void setupWindow() {
        this.setTitle("登录 - 粮库管理系统");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);    // 启用窗口调整大小
        // 设置固定的初始窗口大小
        this.setSize(800, 450);
        // 将窗口居中显示
        this.setLocationRelativeTo(null);
    }

    // 初始化组件
    private void initComponents() {
        // 初始化文本框和按钮
        usernameField = new JTextField(30);
        passwordField = new JPasswordField(30);
        loginButton = new JButton("登录");
        registerButton = new JButton("注册新用户");

        // 设置字体样式
        Font labelFont = new Font("微软雅黑", Font.PLAIN, 16);
        Font buttonFont = new Font("微软雅黑", Font.BOLD, 16);

        // 设置字体颜色
        Color labelColor = new Color(0, 0, 0); // 黑色字体
        Color buttonColor = Color.WHITE; // 按钮文字为白色

        // 设置字体样式
        usernameField.setFont(labelFont);
        passwordField.setFont(labelFont);
        loginButton.setFont(buttonFont);
        registerButton.setFont(buttonFont);

        // 设置文本框和按钮的字体颜色
        usernameField.setForeground(labelColor);
        passwordField.setForeground(labelColor);

        loginButton.setForeground(buttonColor);
        registerButton.setForeground(new Color(66, 133, 244));

        loginButton.setBackground(new Color(66, 133, 244));
        loginButton.setFocusPainted(false);

        registerButton.setBackground(Color.WHITE);
        registerButton.setFocusPainted(false);

        usernameField.setPreferredSize(new Dimension(200, 30));  // 设置用户名文本框的首选大小
        passwordField.setPreferredSize(new Dimension(200, 30));  // 设置密码文本框的首选大小

        // 加载背景图片
        try {
            backgroundImage = ImageIO.read(new File("各种其他内容存放(非程序必要内容)/1.jpg"));
        } catch (IOException e) {
            try {
                throw new IOExceptions.ImageLoadException("无法加载背景图片！");
            } catch (IOExceptions.ImageLoadException ex) {
                throw new RuntimeException(ex);
            }
        }

        // 初始化单选框
        rememberPasswordRadioButton = new JRadioButton("记住密码");
        noLoginRequiredRadioButton = new JRadioButton("7天内无需登录");

        // 设置单选框字体和颜色
        rememberPasswordRadioButton.setFont(labelFont);
        noLoginRequiredRadioButton.setFont(labelFont);
        rememberPasswordRadioButton.setForeground(labelColor);
        noLoginRequiredRadioButton.setForeground(labelColor);
    }

    // 设置布局
    private void setupLayout() {
        // 创建主面板，设置布局为 BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout(20, 20));
        mainPanel.setBorder(new EmptyBorder(20, 30, 50, 30));
        mainPanel.setOpaque(false); // 设置主面板为透明

        // 创建并设置标题面板
        JPanel titlePanel = createTitlePanel();
        titlePanel.setOpaque(false); // 设置标题面板为透明
        mainPanel.add(titlePanel, BorderLayout.NORTH);

        // 创建并设置功能面板
        JPanel functionPanel = createFunctionPanel();
        functionPanel.setOpaque(false); // 设置功能面板为透明
        mainPanel.add(functionPanel, BorderLayout.CENTER);

        // 创建并设置按钮面板
        JPanel buttonPanel = createButtonPanel();
        buttonPanel.setOpaque(false); // 设置按钮面板为透明
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        // 添加背景面板
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    Graphics2D g2d = (Graphics2D) g.create();
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.65f)); // 调整背景图片透明度
                    g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                    g2d.dispose();
                }
            }
        };
        backgroundPanel.setLayout(new BorderLayout());
        backgroundPanel.add(mainPanel, BorderLayout.CENTER);

        // 设置窗口的内容面板
        this.setContentPane(backgroundPanel);
    }


    // 创建标题面板
    private JPanel createTitlePanel() {
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("欢迎登陆粮库管理系统");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        titleLabel.setForeground(new Color(50, 50, 50)); // 设置标题字体颜色为深灰色
        titlePanel.add(titleLabel);
        return titlePanel;
    }

    // 创建功能面板（包括用户名和密码输入框，单选框）
    private JPanel createFunctionPanel() {
        JPanel functionPanel = new JPanel(new GridBagLayout());    // 网格包布局
        functionPanel.setOpaque(false); // 设置面板透明

        // 设置网格包布局的约束条件
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 创建标签并设置不透明
        JLabel userLabel = new JLabel("用户名：");
        JLabel passLabel = new JLabel("密码：");
        userLabel.setForeground(new Color(0, 0, 0));
        passLabel.setForeground(new Color(0, 0, 0));

        // 用户名输入框
        gbc.gridx = 0; gbc.gridy = 0;
        functionPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        functionPanel.add(usernameField, gbc);

        // 密码输入框
        gbc.gridx = 0; gbc.gridy = 1;
        functionPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        functionPanel.add(passwordField, gbc);

        // 添加单选框
        gbc.gridx = 0; gbc.gridy = 2;
        functionPanel.add(rememberPasswordRadioButton, gbc);
        gbc.gridx = 1;
        functionPanel.add(noLoginRequiredRadioButton, gbc);

        return functionPanel;
    }

    // 创建按钮面板
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        return buttonPanel;
    }

    // 为登录按钮和注册按钮添加事件监听器
    public void addLoginListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    // 提供方法让控制器为输入框添加键盘监听器
    public void addKeyListenerToFields(KeyAdapter keyAdapter) {
        usernameField.addKeyListener(keyAdapter);
        passwordField.addKeyListener(keyAdapter);
    }

    // 为单选框添加事件监听器
    public void addRememberPasswordListener(ActionListener listener) {
        rememberPasswordRadioButton.addActionListener(listener);
    }

    public void addNoLoginRequiredListener(ActionListener listener) {
        noLoginRequiredRadioButton.addActionListener(listener);
    }


    // 获取输入的用户名和密码
    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }


    // 显示错误消息
    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE);
    }

    // 显示成功消息
    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "成功", JOptionPane.ERROR_MESSAGE);
    }

    // 清除输入
    public void clearInputs() {
        usernameField.setText("");
        passwordField.setText("");
    }

    // 清除密码输入
    public void clearPassword() {
        passwordField.setText("");
    }


    // getter 和 setter 方法
    public JButton getLoginButton() {
        return loginButton;
    }

    public JButton getRegisterButton() {
        return registerButton;
    }

    public JTextField getUsernameField() {
        return usernameField;
    }

    public JPasswordField getPasswordField() {
        return passwordField;
    }
}
