package view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebView;
import javafx.scene.layout.StackPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import exceptions.JavaFxExceptions;

// 关于项目的自定义浏览器类，继承自JPanel，作为单独javafx面板显示网页
public class BrowserPanel extends JPanel {
    private JFXPanel jfxPanel;
    private WebView webView;
    private JTextField urlField;
    private JButton goButton;
    private JButton backButton;
    private JButton forwardButton;
    private JButton refreshButton;
    private volatile boolean initialized = false;

    public BrowserPanel() {
        try {
            setLayout(new BorderLayout());
            initComponents();

            // 添加组件大小变化监听器
            addComponentListener(new ComponentAdapter() {
                @Override
                public void componentResized(ComponentEvent e) {
                    if (jfxPanel != null) {
                        jfxPanel.setPreferredSize(getSize());
                        revalidate();
                        repaint();
                    }
                }
            });

            // 确保JavaFX线程已启动
            if (!initialized) {
                Platform.setImplicitExit(false);
                initFX();
                initialized = true;
            }
        } catch (Exception e) {
            handleInitializationError(e);
        }
    }

    private void initComponents() {
        // 创建工具栏
        JPanel toolBar = createToolBar();

        // 创建JavaFX面板
        jfxPanel = new JFXPanel();
        jfxPanel.setBackground(new java.awt.Color(248, 248, 248));

        // 添加组件
        add(toolBar, BorderLayout.NORTH);
        add(jfxPanel, BorderLayout.CENTER);
    }

    private JPanel createToolBar() {
        JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEADING));
        toolBar.setBackground(new java.awt.Color(198, 255, 146));

        backButton = createNavigationButton("←");
        forwardButton = createNavigationButton("→");
        refreshButton = createNavigationButton("刷新");
        urlField = createURLField();
        goButton = createNavigationButton("转到");

        toolBar.add(backButton);
        toolBar.add(forwardButton);
        toolBar.add(refreshButton);
        toolBar.add(urlField);
        toolBar.add(goButton);

        return toolBar;
    }

    private JButton createNavigationButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(new java.awt.Color(66, 133, 244));
        button.setForeground(new java.awt.Color(248, 248, 248));
        button.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        return button;
    }

    private JTextField createURLField() {
        JTextField field = new JTextField(30);
        field.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                field.getBorder(),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        return field;
    }

    private void initFX() {
        Platform.runLater(() -> {
            try {
                StackPane root = new StackPane();
                Scene scene = new Scene(root);
                webView = new WebView();
                root.getChildren().add(webView);

                // 设置WebView属性
                webView.setContextMenuEnabled(false);
                webView.getEngine().setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36");

                // 设置场景
                jfxPanel.setScene(scene);

                // 添加事件监听器
                setupEventListeners();

                // 加载初始页面
                loadURL("https://devops.aliyun.com/workbench?_userId=66f0d10d6b386908ddbcc4e7&timestamp=1733217718104&mode=redirect&sign=d197eddecb1fa3e22c9c2a6725ddce22");
            } catch (Exception e) {
                handleInitializationError(e);
            }
        });
    }

    // 初始化事件监听器
    private void setupEventListeners() {
        // 导航按钮事件
        backButton.addActionListener(e -> Platform.runLater(() -> {
            if (webView.getEngine().getHistory().getCurrentIndex() > 0) {
                webView.getEngine().getHistory().go(-1);
            }
        }));

        forwardButton.addActionListener(e -> Platform.runLater(() -> {
            if (webView.getEngine().getHistory().getCurrentIndex() < webView.getEngine().getHistory().getMaxSize() - 1) {
                webView.getEngine().getHistory().go(1);
            }
        }));

        refreshButton.addActionListener(e -> Platform.runLater(() ->
                webView.getEngine().reload()
        ));

        goButton.addActionListener(e -> loadURL(urlField.getText()));

        // URL变化监听
        webView.getEngine().locationProperty().addListener((obs, oldUrl, newUrl) -> {
            if (newUrl != null) {
                SwingUtilities.invokeLater(() -> urlField.setText(newUrl));
            }
        });
    }

    public void loadURL(String url) {
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "https://" + url;
        }
        final String finalUrl = url;
        Platform.runLater(() -> {
            try {
                webView.getEngine().load(finalUrl);
            } catch (Exception e) {
                handleError("加载页面失败: " + e.getMessage());
            }
        });
    }

    private void handleInitializationError(Exception e) {
        System.err.println("初始化浏览器失败: " + e.getMessage());
        SwingUtilities.invokeLater(() -> {
            removeAll();
            add(new JLabel("浏览器初始化失败: " + e.getMessage(), SwingConstants.CENTER));
            revalidate();
            repaint();
        });
        try {
            throw new JavaFxExceptions.webViewInitError(e.getMessage());
        } catch (JavaFxExceptions.webViewInitError ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleError(String message) {
        System.err.println(message);
        SwingUtilities.invokeLater(() ->
                JOptionPane.showMessageDialog(this, message, "错误", JOptionPane.ERROR_MESSAGE)
        );
        try {
            throw new JavaFxExceptions.webViewPanelError(message);
        } catch (JavaFxExceptions.webViewPanelError e) {
            throw new RuntimeException(e);
        }
    }

    public void cleanup() {
        Platform.runLater(() -> {
            if (webView != null) {
                webView.getEngine().load(null);
            }
        });
    }

    @Override
    public void removeNotify() {
        cleanup();
        super.removeNotify();
    }
}