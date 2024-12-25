package controller;

import view.MainMenuView;
import view.HomePanel;
import javax.swing.JOptionPane;

// 主页面对应的控制器
public class MainMenuController {
    private MainMenuView mainView;
    private String username;
    private String role;

    // 构造函数，接收用户名和角色信息
    public MainMenuController(String username, String role) {
        this.username = username;
        this.role = role;

        // 初始化主视图
        mainView = new MainMenuView(username, role);

        // 添加按钮监听器
        initializeButtonListeners();

        // 初始化主页面
        initializeHomePanel();

        // 显示主窗口
        mainView.setVisible(true);
    }

    // 初始化按钮监听器
    private void initializeButtonListeners() {
        // 粮食信息管理
        mainView.getGrainManageBtn().addActionListener(e -> handleGrainManagement());
        // 粮库管理
        mainView.getWarehouseManageBtn().addActionListener(e -> handleWarehouseManagement());
        // 入库管理
        mainView.getEnStorageManageBtn().addActionListener(e -> handleStorageOneManagement());
        // 出库管理
        mainView.getOutboundManageBtn().addActionListener(e -> handleStorageTwoManagement());
        // 库存管理
        mainView.getInventoryManageBtn().addActionListener(e -> handleInventoryManagement());
        // 历史记录
        mainView.getHistoryRecordBtn().addActionListener(e -> handleHistoryRecord());
        // 系统管理
        mainView.getSystemManageBtn().addActionListener(e -> handleSystemManagement());
        // 切换账号
        mainView.getSwitchAccountBtn().addActionListener(e -> handleSwitchAccount());
        mainView.getInspectionBtn().addActionListener(e -> handleInspection());
    }


    public void logout() {
        try {
            // 关闭主窗口
            if (mainView != null) {
                mainView.dispose();
            }

            // 显示登录窗口
            LoginController loginController = new LoginController();
            loginController.showLoginView();
        } catch (Exception e) {
            System.err.println("切换账号时发生错误: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    mainView,
                    "切换账号失败: " + e.getMessage(),
                    "错误",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    // 处理菜单项点击事件，实现页面的跳转

    // 跳转到粮食信息管理页面
    private void handleGrainManagement() {
        GrainController controller = new GrainController(username, role);
        mainView.showContentPanel(controller.getView());
    }

    // 跳转显示粮库管理面板
    private void handleWarehouseManagement() {
        WarehouseController controller = new WarehouseController(username, role);
        mainView.showContentPanel(controller.getView());
    }

    private void handleStorageOneManagement() {
        EntryController controller = new EntryController(username,role);
        mainView.showContentPanel(controller.getView());
    }

    private void handleStorageTwoManagement() {
        ExitController controller = new ExitController(username,role);
        mainView.showContentPanel(controller.getView());
    }

    private void handleInventoryManagement() {
        InventoryController controller=new InventoryController(username,role);
        mainView.showContentPanel(controller.getView());
        //InspectionController controller=new InspectionController(username,role);
        //mainView.showContent(controller.getView());
    }
    private void handleInspection(){
        InspectionController controller=new InspectionController(username,role);
        mainView.showContentPanel(controller.getView());
    }
    // 跳转查询历史信息面板
    private void handleHistoryRecord() {
        HistoryRecordController controller = new HistoryRecordController(username, role);
        mainView.showContentPanel(controller.getView());
    }

    // 创建系统管理控制器，它会自动创建并显示新窗口
    private void handleSystemManagement() {
        new SystemManageController(username, role, this);
    }



    // 处理切换账号的方法
    private void handleSwitchAccount() {
        int confirm = JOptionPane.showConfirmDialog(
                mainView,
                "确定要切换账号吗？",
                "确认切换账号",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            logout();  // 调用已有的登出方法
        }
    }



    // 初始化主页面
    private void initializeHomePanel() {
        HomePanel homePanel = new HomePanel(username, role);
        mainView.showContentPanel(homePanel);
    }
}
