package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

public class HomePanel extends JPanel {
    private JLabel timeLabel;
    private JLabel dateLabel;
    private JLabel welcomeLabel;
    private Timer timer;

    public HomePanel(String username, String role) {
        setLayout(new BorderLayout());
        initComponents(username, role);
        startClock();
    }

    private void initComponents(String username, String role) {
        // 欢迎面板
        JPanel welcomePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        welcomeLabel = new JLabel("欢迎使用粮库管理系统，" + role + username);
        welcomeLabel.setFont(new Font("微软雅黑", Font.BOLD, 24));
        welcomePanel.add(welcomeLabel);

        // 时钟面板
        JPanel clockPanel = new JPanel(new GridBagLayout());
        clockPanel.setBackground(new Color(162, 243, 176));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridwidth = GridBagConstraints.REMAINDER;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 10, 10);

        // 时间标签
        timeLabel = new JLabel();
        timeLabel.setFont(new Font("微软雅黑", Font.BOLD, 48));
        clockPanel.add(timeLabel, gbc);

        // 日期标签
        dateLabel = new JLabel();
        dateLabel.setFont(new Font("微软雅黑", Font.PLAIN, 24));
        clockPanel.add(dateLabel, gbc);

        // 系统信息面板
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // 添加一些系统信息
        infoPanel.add(createInfoLabel("系统版本: v1.1.05"));    // 上传了几次））））
        infoPanel.add(createInfoLabel("上次登录: " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())));
        infoPanel.add(createInfoLabel("系统状态: 正常运行中"));

        // 布局
        add(welcomePanel, BorderLayout.NORTH);
        add(clockPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);
    }

    private JLabel createInfoLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.PLAIN, 16));
        return label;
    }

    private void startClock() {
        timer = new Timer(1000, e -> updateTime());
        timer.start();
    }

    private void updateTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 EEEE");
        
        timeLabel.setText(timeFormat.format(new Date()));
        dateLabel.setText(dateFormat.format(new Date()));
    }

    public void stopClock() {
        if (timer != null) {
            timer.stop();
        }
    }
} 