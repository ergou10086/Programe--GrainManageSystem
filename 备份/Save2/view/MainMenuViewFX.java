package view;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.embed.swing.SwingNode;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

// 主菜单视图类，使用javaFx，用于构建粮库管理系统的图形化主菜单界面
public class MainMenuViewFX extends JFrame {
    // JavaFX面板，用于承载JavaFX场景并展示在Swing框架中
    private JFXPanel fxPanel;
    // 存储导航按钮的映射，键为按钮文本，值为对应的按钮对象
    private Map<String, Button> navButtons = new HashMap<>();
    // 主布局，采用BorderPane来划分界面不同区域（顶部、左侧、中心等）
    private BorderPane mainLayout;
    // 左侧导航面板，以垂直布局（VBox）来排列导航按钮
    private VBox leftPanel;
    // 内容面板，采用StackPane布局，用于展示不同的功能内容等
    private StackPane contentPanel;
    // 当前应用的主题名称，初始值为"default"
    private String currentTheme = "default";
    // 初始化完成的回调接口实例，用于在界面初始化完成后执行特定逻辑
    private InitializationCallback initCallback;

    private static final int USE_COMPUTED_SIZE = 35;

    // 主题配置类
    public static class ThemeConfig {
        public String primaryColor = "#ffc057";
        public String secondaryColor = "#d6f7ad";
        public String backgroundColor = "#95e1d3";
        public String textColor = "#2C3E50";         // 深灰文本色
        public String buttonTextColor = "#323232";    // 白色按钮文本
        public double buttonWidth = 200;              // 按钮宽度
        public double buttonHeight = 50;              // 按钮高度
        public double buttonSpacing = 10;             // 按钮间距
        public String fontFamily = "Microsoft YaHei"; // 字体
        
        // 右侧面板颜色
        public String contentPanelColor = "#FFFFFF";     // 内容面板背景色
        public String contentHeaderColor = "#F5F5F5";    // 内容面板头部颜色
        public String contentTextColor = "#333333";      // 内容���本颜色
        
        // 添加渐变色支持
        public String gradientStartColor = "#c0ffc2";    // 渐变开始颜色
        public String gradientEndColor = "#46b7b9";      // 渐变结束颜色
        
        // 添加新的配置项
        public double minButtonHeight = 45;    // 按钮最小高度
        public double maxButtonHeight = 80;    // 按钮最大高度
        public boolean autoResizeButtons = true; // 是否自动调整按钮大小
        public String accentColor;
    }

    // 当前应用的主题配置实例，初始化时采用默认配置
    private ThemeConfig currentThemeConfig = new ThemeConfig();

    // 定义初始化完成的回调接口，只包含一个方法onInitialized，供外部实现特定逻辑
    public interface InitializationCallback {
        void onInitialized();
    }

    // 构造函数，用于初始化主菜单视图框架相关属性，并启动JavaFX界面的初始化
    public MainMenuViewFX(String username, String role) {
        // 简单的初始设置
        setTitle("粮库管理系统");
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 初始化JavaFX面板
        fxPanel = new JFXPanel();
        // 将JavaFX面板添加到当前JFrame中
        add(fxPanel);

        // 在JavaFX应用线程中执行界面初始化方法，传入用户名和角色信息
        Platform.runLater(() -> initFX(username, role));
    }

    // 设置初始化完成后的回调方法
    public void setInitializationCallback(InitializationCallback callback) {
        this.initCallback = callback;
    }

    // 初始化JavaFX界面的具体方法
    private void initFX(String username, String role) {
        // 创建主布局
        mainLayout = new BorderPane();

        // 创建顶部面板
        HBox headerPanel = createHeader(username, role);
        mainLayout.setTop(headerPanel);

        // 创建左侧导航面板
        leftPanel = createNavPanel();
        mainLayout.setLeft(leftPanel);

        // 创建内容面板
        contentPanel = createContentPanel();
        mainLayout.setCenter(contentPanel);

        // 应用主题
        applyTheme();

        // 创建场景，设置到JavaFX面板上
        Scene scene = new Scene(mainLayout);
        fxPanel.setScene(scene);

        // 如果初始化回调接口实例不为空，则调用其onInitialized方法通知初始化完成
        if (initCallback != null) {
            initCallback.onInitialized();
        }
    }

    // 创建顶部面板的方法，用于显示系统标题、当前用户信息等内容，并进行布局和样式设置
    private HBox createHeader(String username, String role) {
        HBox header = new HBox();
        header.setAlignment(Pos.CENTER);  // 居中对齐
        header.setPadding(new Insets(15));   // 设置面板内边距为15像素
        header.setSpacing(20);    // 设置组件之间的水平间距为20像素

        // 创建标题标签
        Label titleLabel = new Label("粮库管理系统");
        titleLabel.setAlignment(Pos.CENTER);
        titleLabel.setFont(Font.font(currentThemeConfig.fontFamily, FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.web(currentThemeConfig.textColor));

        // 创建用户信息标签
        Label userInfoLabel = new Label("当前用户: " + username + " (" + role + ")");
        userInfoLabel.setFont(Font.font(currentThemeConfig.fontFamily, FontWeight.NORMAL, 12));
        userInfoLabel.setTextFill(Color.web(currentThemeConfig.textColor));

        // 创建左右两个占位区域，实现标题居中和用户信息右对齐
        Region leftSpacer = new Region();
        Region rightSpacer = new Region();
        HBox.setHgrow(leftSpacer, Priority.ALWAYS);
        HBox.setHgrow(rightSpacer, Priority.ALWAYS);

        // 创建用户信息容器，使其位于右下角
        VBox userInfoContainer = new VBox();
        userInfoContainer.setAlignment(Pos.BOTTOM_RIGHT);
        userInfoContainer.getChildren().add(userInfoLabel);

        // 按顺序添加组件：左占位符、标题、右占位符、用户信息
        header.getChildren().addAll(
                leftSpacer,
                titleLabel,
                rightSpacer,
                userInfoContainer
        );

        // 添加底部边框和背景色
        header.setStyle(String.format(
                "-fx-border-color: %s; " +
                        "-fx-border-width: 0 0 1 0; " +
                        "-fx-background-color: %s;",
                currentThemeConfig.secondaryColor,
                currentThemeConfig.backgroundColor
        ));

        return header;
    }


    // 创建左侧导航面板的方法，添加多个导航按钮，并设置按钮样式、间距以及可拖拽功能
    private VBox createNavPanel() {
        VBox nav = new VBox();
        // 设置导航面板的内边距
        nav.setPadding(new Insets(20, 10, 20, 10));
        nav.setSpacing(currentThemeConfig.buttonSpacing);
        nav.setPrefWidth(currentThemeConfig.buttonWidth + 20);
        // 设置最小宽度和高度
        nav.setMinWidth(200);
        nav.setMinHeight(USE_COMPUTED_SIZE);
        // 设置VBox填充整个可用空间
        VBox.setVgrow(nav, Priority.ALWAYS);
        
        // 设置背景色和边框
        nav.setStyle(String.format(
            "-fx-background-color: %s; " +
            "-fx-border-color: %s; " +
            "-fx-border-width: 0 1 0 0;",
            currentThemeConfig.primaryColor,
            currentThemeConfig.secondaryColor
        ));

        String[] buttonNames = {
            "粮食信息管理", "粮库管理", "入库管理", "出库管理",
            "库存预警", "历史记录查询", "系统管理", "切换账号"
        };

        // 创建按钮列表
        for (String name : buttonNames) {
            Button button = createNavButton(name);
            navButtons.put(name, button);
            
            // 设置按钮填充整个可用宽度
            button.setMaxWidth(Double.MAX_VALUE);
            VBox.setVgrow(button, Priority.ALWAYS);
            
            // 添加按钮到导航栏
            nav.getChildren().add(button);

            // 添加拖拽功能
            setupDragAndDrop(button, nav);
        }

        return nav;
    }

    // 添加拖拽功能的辅助方法
    private void setupDragAndDrop(Button button, VBox nav) {
        button.setOnDragDetected(event -> {
            button.startFullDrag();
            event.consume();
        });

        button.setOnMouseDragged(event -> {
            double mouseY = event.getSceneY();
            double buttonHeight = button.getHeight();
            int currentIndex = nav.getChildren().indexOf(button);
            int newIndex = (int)((mouseY - nav.localToScene(0, 0).getY()) / buttonHeight);
            
            // 确保新索引在有效范围内
            newIndex = Math.max(0, Math.min(newIndex, nav.getChildren().size() - 1));
            
            if (newIndex != currentIndex) {
                nav.getChildren().remove(button);
                nav.getChildren().add(newIndex, button);
                
                // 重新计算所有按钮的大小
                Platform.runLater(() -> redistributeButtonSizes(nav));
            }
        });
    }

    // 重新分配按钮大小的方法
    private void redistributeButtonSizes(VBox nav) {
        double availableHeight = nav.getHeight() - (nav.getPadding().getTop() + nav.getPadding().getBottom());
        double buttonHeight = availableHeight / nav.getChildren().size();
        
        nav.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button btn = (Button) node;
                btn.setPrefHeight(buttonHeight);
                btn.setMinHeight(buttonHeight);
            }
        });
    }

    // 修改 createNavButton 方法
    private Button createNavButton(String text) {
        Button button = new Button(text);
        
        // 设置按钮大小
        button.setPrefWidth(currentThemeConfig.buttonWidth);
        button.setMaxWidth(Double.MAX_VALUE);
        
        // 设置字体
        button.setFont(Font.font(currentThemeConfig.fontFamily, FontWeight.NORMAL, 16));
        
        // 设置按钮样式
        String buttonStyle = String.format(
            "-fx-background-color: %s; " +
            "-fx-text-fill: %s; " +
            "-fx-font-size: 16px; " +
            "-fx-background-radius: 5; " +
            "-fx-border-radius: 5; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 10 15;",
            currentThemeConfig.primaryColor,
            currentThemeConfig.buttonTextColor
        );
        
        button.setStyle(buttonStyle);

        // 添加悬停效果
        button.setOnMouseEntered(e -> 
            button.setStyle(buttonStyle + 
                "-fx-background-color: " + currentThemeConfig.accentColor + ";"
            )
        );
        
        button.setOnMouseExited(e -> 
            button.setStyle(buttonStyle)
        );

        return button;
    }

    // 添加方法：动态添加新按钮
    public void addNavButton(String text) {
        Button newButton = createNavButton(text);
        navButtons.put(text, newButton);
        
        Platform.runLater(() -> {
            leftPanel.getChildren().add(newButton);
            redistributeButtonSizes(leftPanel);
        });
    }

    // 添加方法：动态移除按钮
    public void removeNavButton(String text) {
        Button button = navButtons.get(text);
        if (button != null) {
            Platform.runLater(() -> {
                leftPanel.getChildren().remove(button);
                navButtons.remove(text);
                redistributeButtonSizes(leftPanel);
            });
        }
    }

    private StackPane createContentPanel() {
        StackPane content = new StackPane();
        content.setPadding(new Insets(20));
        
        // 设置渐变背景
        String style = String.format(
            "-fx-background-color: linear-gradient(to bottom right, %s, %s);",
            currentThemeConfig.gradientStartColor,
            currentThemeConfig.gradientEndColor
        );
        content.setStyle(style);

        // 添加欢迎信息
        Label welcomeLabel = new Label("欢迎使用粮库管理系统");
        welcomeLabel.setFont(Font.font(currentThemeConfig.fontFamily, FontWeight.BOLD, 28));
        welcomeLabel.setTextFill(Color.web(currentThemeConfig.contentTextColor));
        
        // 添加阴影效果
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.rgb(0, 0, 0, 0.3));
        dropShadow.setRadius(5.0);
        welcomeLabel.setEffect(dropShadow);

        content.getChildren().add(welcomeLabel);
        return content;
    }

    public void setTheme(ThemeConfig newTheme) {
        this.currentThemeConfig = newTheme;
        Platform.runLater(this::applyTheme);
    }

    private void applyTheme() {
        // 更新导航按钮样式
        navButtons.forEach((text, button) -> {
            String buttonColor = "#fff8b5";
            button.setStyle(String.format(
                "-fx-background-color: %s; -fx-text-fill: %s;",
                buttonColor,
                currentThemeConfig.buttonTextColor
            ));
        });

        // 更新内容面板样式
        if (contentPanel != null) {
            contentPanel.setStyle(String.format(
                "-fx-background-color: linear-gradient(to bottom right, %s, %s);",
                currentThemeConfig.gradientStartColor,
                currentThemeConfig.gradientEndColor
            ));
        }
    }

    // 获取按钮的方法
    public Button getButton(String name) {
        return navButtons.get(name);
    }

    // 显示内容面板的方法
    public void showContent(JPanel panel) {
        if (Platform.isFxApplicationThread()) {
            updateContent(panel);
        } else {
            Platform.runLater(() -> updateContent(panel));
        }
    }


    // 处理内容更新
    private void updateContent(JPanel panel) {
        if (contentPanel != null) {
            contentPanel.getChildren().clear();
            if (panel != null) {
                SwingNode swingNode = new SwingNode();
                swingNode.setContent(panel);
                contentPanel.getChildren().add(swingNode);
            }
        }
    }
} 