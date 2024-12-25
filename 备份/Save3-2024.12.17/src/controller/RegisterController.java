package controller;

import exceptions.RegisterExceptions;
import exceptions.ServiceException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import service.IUserService;
import service.impl.UserServiceImpl;
import view.RegisterView;

// 注册页面对应的控制器类
public class RegisterController {
    // 属性
    private RegisterView registerView;
    private IUserService registerService;

    // 构造方法
    public RegisterController() {
        this.registerView = new RegisterView();
        this.registerService = new UserServiceImpl();
        initializeListeners();
    }

    private void initializeListeners() {
        registerView.addRegisterListener(new RegisterListener());
        registerView.addBackListener(new BackListener());
    }

    // 显示注册窗口
    public void showRegisterView() {
        registerView.setVisible(true);
    }

    // 注册按钮监听器
    private class RegisterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // 获取用户输入
            String username = registerView.getUsername();
            String password = registerView.getPassword();
            String confirmPassword = registerView.getConfirmPassword();
            String role = registerView.getSelectedRole();

            try {
                // 输入验证
                if (!validateInput(username, password, confirmPassword)) {
                    return;
                }

                // 执行注册
                if (registerService.register(username, password, role)) {
                    JOptionPane.showMessageDialog(registerView, "注册成功，欢迎您，仓库的"+role+"！");
                    registerView.clearInputs();
                    registerView.dispose();
                    new LoginController();
                }
            } catch (ServiceException ex) {
                registerView.showError("注册失败：" + ex.getMessage());
                throw new RegisterExceptions.OtherRegisterExceptions(ex.getMessage());
            }
        }

        // 输入验证方法
        private boolean validateInput(String username, String password, String confirmPassword) {
            if (username.trim().isEmpty() || password.trim().isEmpty()) {
                registerView.showError("用户名和密码不能为空！");
                return false;
            }
            if (!password.equals(confirmPassword)) {
                registerView.showError("两次输入的密码不一致！");
                return false;
            }
            if (password.length() < 6 || password.length() > 16) {
                registerView.showError("密码长度必须在6-16位之间！");
                return false;
            }
            return true;
        }
    }

    // 返回按钮监听器
    private class BackListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            registerView.dispose();
            new LoginController();
        }
    }
}
