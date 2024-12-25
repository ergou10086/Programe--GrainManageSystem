package view;

import com.mysql.cj.protocol.a.result.ResultsetRowsStatic;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.*;
import javax.swing.border.*;

// 主界面类，实现主菜单到其他功能的切换的界面
public class MainMenuView extends JFrame {
    // UI组件
    private JPanel mainPanel;           // 主面板
    private JPanel leftPanel;           // 左侧导航面板
    private JPanel rightPanel;          // 右侧内容面板
    private JPanel headerPanel;         // 顶部面板
    private JLabel userInfoLabel;       // 用户信息标签
    private JLabel titleLabel;          // 标题标签

    // 左侧导航按钮
    private JButton grainManageBtn;     // 粮食信息管理
    private JButton warehouseManageBtn; // 粮库管理
    private JButton outboundManageBtn;   // 出库管理
    private JButton enStorageManageBtn;     // 入库管理
    private JButton inventoryManageBtn; // 库存管理
    private JButton historyRecordBtn;    // 历史记录查询
    private JButton systemManageBtn;    // 系统管理
    private JButton switchAccountBtn;
    private JButton inspectionBtn;

    // 菜单栏组件，暂时先放着，后面再写
    private JMenuBar menuBar;
    private JMenu grainMenu;        // 粮食信息管理
    private JMenu warehouseMenu;    // 粮库管理
    private JMenu storageMenu;      // 出入库管理
    private JMenu inventoryMenu;    // 库存管理
    private JMenu systemMenu;       // 系统设置

    // 定义配色方案
    private final Color PRIMARY_COLOR = new Color(165, 222, 229);      // 主色调
    private final Color ACCENT_COLOR = new Color(255, 207, 223);         // 强调色调
    private final Color SECONDARY_COLOR = new Color(254, 253, 202);       // 次要色
    private final Color BACKGROUND_COLOR = new Color(224, 249, 181, 255);  // 背景色
    private final Color TEXT_COLOR = new Color(255, 255, 255);           // 文本颜色

    public MainMenuView(String username, String role) {
        // 设置窗口基本属性
        setTitle("粮库管理系统");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化组件
        initComponents(username, role);
        // 设置布局
        setupLayout();

        // 设置窗口背景色
        getContentPane().setBackground(BACKGROUND_COLOR);
    }

    private void initComponents(String username, String role) {
        // 初始化面板
        mainPanel = new JPanel(new BorderLayout(15, 0));
        leftPanel = new JPanel();
        GridLayout gridLayout = new GridLayout(0, 1, 0, 10); // 单列，行间距10像素
        leftPanel.setLayout(gridLayout);
        rightPanel = new JPanel(new BorderLayout());
        headerPanel = new JPanel(new BorderLayout());

        // 设置主面板样式
        mainPanel.setBackground(BACKGROUND_COLOR);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // 设置左边栏按钮面板样式
        leftPanel.setBackground(PRIMARY_COLOR);
        leftPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 0, 0, 30), 1, true),
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        ));


        // 顶部面板样式
        headerPanel.setBackground(PRIMARY_COLOR);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // 标题样式
        titleLabel = new JLabel("粮库管理系统", SwingConstants.CENTER);
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        titleLabel.setForeground(TEXT_COLOR);

        // 用户信息样式
        userInfoLabel = new JLabel("当前用户: " + username + " (" + role + ")", SwingConstants.RIGHT);
        userInfoLabel.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        userInfoLabel.setForeground(TEXT_COLOR);

        // 初始化导航按钮
        grainManageBtn = createNavButton("粮食信息管理");
        warehouseManageBtn = createNavButton("粮库管理");
        enStorageManageBtn = createNavButton("入库管理");
        outboundManageBtn = createNavButton("出库管理");
        inventoryManageBtn = createNavButton("库存管理");
        inspectionBtn = createNavButton("巡库管理");
        historyRecordBtn = createNavButton("历史记录查询");
        systemManageBtn = createNavButton("系统管理");
        inspectionBtn=createNavButton("巡库管理");
        switchAccountBtn = createNavButton("切换账号");
    }

    // 创建按钮特性的方法
    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("微软雅黑", Font.PLAIN, 18));
        button.setForeground(TEXT_COLOR);
        button.setBackground(PRIMARY_COLOR);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        // 添加鼠标事件监听器，实现拖拽功能
        button.addMouseMotionListener(new MouseMotionAdapter() {
            private Point dragStart;
            private int startIndex;

            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart == null) {
                    dragStart = e.getPoint();
                    startIndex = getButtonIndex(button);
                }

                Point current = e.getLocationOnScreen();
                int newIndex = getInsertIndex(current.y);

                if (newIndex != startIndex && newIndex >= 0 && newIndex < leftPanel.getComponentCount()) {
                    // 移动按钮到新位置
                    leftPanel.remove(button);
                    leftPanel.add(button, newIndex);
                    startIndex = newIndex;
                    leftPanel.revalidate();
                    leftPanel.repaint();
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                dragStart = null;
            }
        });


        // 设置按钮悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(ACCENT_COLOR);
                button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
                button.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
            }

            @Override
            public void mousePressed(MouseEvent e) {
                button.setBackground(PRIMARY_COLOR);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                button.setBackground(SECONDARY_COLOR);
            }
        });

        return button;
    }


    private int getButtonIndex(JButton button) {
        Component[] components = leftPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            if (components[i] == button) {
                return i;
            }
        }
        return -1;
    }

    private int getInsertIndex(int screenY) {
        Component[] components = leftPanel.getComponents();
        for (int i = 0; i < components.length; i++) {
            Rectangle bounds = components[i].getBounds();
            Point location = new Point(bounds.x, bounds.y);
            SwingUtilities.convertPointToScreen(location, leftPanel);

            if (screenY < location.y + bounds.height / 2) {
                return i;
            }
        }
        return components.length - 1;
    }


    private void setupLayout() {
        // 设置主面板布局
        setLayout(new BorderLayout());

        // 设置顶部面板
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(userInfoLabel, BorderLayout.EAST);

        // 添加导航按钮到左侧面板
        leftPanel.add(grainManageBtn);
        leftPanel.add(warehouseManageBtn);
        leftPanel.add(enStorageManageBtn);
        leftPanel.add(outboundManageBtn);
        leftPanel.add(inventoryManageBtn);
        leftPanel.add(inspectionBtn);
        leftPanel.add(historyRecordBtn);
        leftPanel.add(systemManageBtn);
        leftPanel.add(switchAccountBtn);

        // 设置右侧面板
        rightPanel.setBackground(TEXT_COLOR);
        // 给右侧面板设置边框
        rightPanel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(0, 0, 0, 20), 1, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));

        // 添加欢迎信息到右侧面板
        JLabel welcomeLabel = new JLabel("欢迎使用粮库管理系统", SwingConstants.CENTER);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 28));
        welcomeLabel.setForeground(PRIMARY_COLOR);
        rightPanel.add(welcomeLabel, BorderLayout.CENTER);

        // 组装主面板
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);

        // 添加到窗口
        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        // 添加组件监听器来处理窗口大小变化
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // 获取左侧面板当前的组件数量
                int componentCount = leftPanel.getComponentCount();
                if (componentCount > 0) {
                    // 计算每个按钮应该的高度
                    int availableHeight = leftPanel.getHeight() - (componentCount - 1) * 10; // 减去间距的总和
                    int buttonHeight = availableHeight / componentCount;

                    
                    // 更新 GridLayout 的行间距
                    GridLayout layout = (GridLayout) leftPanel.getLayout();
                    layout.setVgap(10);
                    
                    // 重新验证布局
                    leftPanel.revalidate();
                    leftPanel.repaint();
                }
            }
        });
    }

    // getter，主要是按钮
    public JButton getGrainManageBtn() {
        return grainManageBtn;
    }

    public JButton getWarehouseManageBtn() {
        return warehouseManageBtn;
    }

    public JButton getOutboundManageBtn() {
        return outboundManageBtn;
    }

    public JButton getEnStorageManageBtn() {
        return enStorageManageBtn;
    }

    public JButton getInventoryManageBtn() {
        return inventoryManageBtn;
    }

    public JButton getSystemManageBtn() {
        return systemManageBtn;
    }

    public JButton getHistoryRecordBtn() {
        return historyRecordBtn;
    }

    public JButton getSwitchAccountBtn() {
        return switchAccountBtn;
    }

    public JButton getInspectionBtn(){return inspectionBtn;}


    public void showContentPanel(JPanel panel) {
        // 假设主界面使用BorderLayout，并且内容区域在CENTER位置
        // 移除当前显示的面板
        rightPanel.removeAll();
        // 添加新的面板
        rightPanel.add(panel, BorderLayout.CENTER);
        // 重新验证和重绘
        rightPanel.revalidate();
        rightPanel.repaint();
    }

    // 添加新方法用于动态添加按钮
    public void addNavigationButton(JButton button) {
        leftPanel.add(button);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    // 添加新方法用于动态移除按钮
    public void removeNavigationButton(JButton button) {
        leftPanel.remove(button);
        leftPanel.revalidate();
        leftPanel.repaint();
    }
}