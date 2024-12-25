package controller;

import entity.User;
import exceptions.ServiceException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.*;
import service.IUserService;
import service.impl.UserServiceImpl;
import view.LoginView;
import java.util.ArrayList;
import java.util.List;

//登录控制界面
public class LoginController {
    private final LoginView loginView;
    private final IUserService userService;
    private List<LoginSuccessListener> loginSuccessListeners = new ArrayList<>();

    // 添加登录成功监听器接口
    public interface LoginSuccessListener {
        void onLoginSuccess(String username, String role);
    }

    public void addLoginSuccessListener(LoginSuccessListener listener) {
        loginSuccessListeners.add(listener);
    }

    public LoginController() {
        this.loginView = new LoginView();
        this.userService = new UserServiceImpl();
        initializeListeners();
        loginView.setVisible(true);
    }

    private void initializeListeners() {
        loginView.addLoginListener(e -> handleLogin());
        loginView.addRegisterListener(e -> handleRegister());
        loginView.addKeyListenerToFields(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    handleLogin();
                }
            }
        });
    }

    private void handleLogin() {
        String username = loginView.getUsername();
        String password = loginView.getPassword();

        try {
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                loginView.showError("用户名和密码不能为空！");
                return;
            }

            User user = userService.login(username, password);
            if (user != null) {
                JOptionPane.showMessageDialog(loginView, "登录成功！");
                loginView.dispose();
                SwingUtilities.invokeLater(() -> {
                    try {
                        new MainMenuController(username, user.getRole());
                    } catch (Exception e) {
                        System.err.println("创建主菜单时发生错误: " + e.getMessage());
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(loginView, "启动主界面失败: " + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                });
            }
        } catch (ServiceException e) {
            loginView.showError("登录失败：" + e.getMessage());
            loginView.clearPassword();
        } catch (Exception e) {
            System.err.println("登录时发生错误: " + e.getMessage());
            e.printStackTrace();
            loginView.showError("登录失败：发生未知错误");
            loginView.clearPassword();
        }
    }

    private void handleRegister() {
        loginView.setVisible(false);
        new RegisterController().showRegisterView();
    }


    public void showLoginView(){
        loginView.setVisible(true);
    }
}