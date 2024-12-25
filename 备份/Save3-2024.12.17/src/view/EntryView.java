package view;
import com.toedter.calendar.JDateChooser;
import controller.EntryController;

import com.toedter.calendar.JDateChooser;
import controller.EntryController;
import dao.impl.EntryDAOimpl;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 入库视图类，在这里实现入库管理的视图实现
public class EntryView extends JPanel{
    private JTextField cropNameField;// 用于输入作物名称的文本字段
    private JComboBox<String> warehouseCombo;// 用于选择入库仓库的下拉列表框
    private JTextField grossWeightField;// 用于输入毛重的文本字段
    private JTextField tareWeightField;// 用于输入皮重的文本字段
    private JTextField netWeightField;// 用于输入净重的文本字段
    private JTextField moistureField;// 用于输入水分含量的文本字段
    private JTextField driverNameField;// 用于输入司机姓名的文本字段
    private JTextField phoneField;// 用于输入司机姓名的文本字段
    private JTextField plateNumberField;// 用于输入车牌号的文本字段
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton printfButton;
    private JDateChooser leaseDate;//获取时间
    private PrintfEntryView printfEntryView;
    private EntryController entryController;
    private EntryDAOimpl entryDAOimpl;

    public EntryView(){}

    public EntryView(EntryController controller) {
        this.entryController = controller;
        init();
    }

    public void init(){
        setLayout(new BorderLayout());
        add(createCenterPane(), BorderLayout.CENTER);
    }

    public JPanel createCenterPane(){
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        // 添加整体边距
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 左侧面板
        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createBasicPane());
        leftPanel.add(Box.createVerticalStrut(15));
        leftPanel.add(createWeightPane());
        leftPanel.add(Box.createVerticalGlue());

        // 右侧面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(createDriverPane());
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(createTimePane());
        rightPanel.add(Box.createVerticalStrut(15));
        rightPanel.add(createButtonPane());
        rightPanel.add(Box.createVerticalGlue());

        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    public JPanel createBasicPane(){
        JPanel panel = createStandardPanel("基本信息");
        panel.setMinimumSize(new Dimension(300, 120));

        panel.add(createFormRow("作物名称：", cropNameField = new JTextField(20)));
        panel.add(Box.createVerticalStrut(10));

        JPanel warehousePanel = createFormRow("仓库名称：", warehouseCombo = new JComboBox<>(entryController.getWarehouse()));
        warehouseCombo.setPreferredSize(new Dimension(200, 25));
        panel.add(warehousePanel);

        return panel;
    }

    public JPanel createWeightPane() {
        JPanel panel = createStandardPanel("重量信息");
        panel.setMinimumSize(new Dimension(300, 200));

        // 毛重行
        JPanel grossWeightRow = createFormRow("毛重：", grossWeightField = new JTextField(20));
        JLabel hairWeightUnit = new JLabel("(kg)");
        hairWeightUnit.setForeground(Color.GRAY);
        grossWeightRow.add(hairWeightUnit); // 直接添加到行面板中
        panel.add(grossWeightRow);
        panel.add(Box.createVerticalStrut(5));

        // 皮重行
        JPanel tareWeightRow = createFormRow("皮重：", tareWeightField = new JTextField(20));
        JLabel skinWeightUnit = new JLabel("(kg)");
        skinWeightUnit.setForeground(Color.GRAY);
        tareWeightRow.add(skinWeightUnit);
        panel.add(tareWeightRow);
        panel.add(Box.createVerticalStrut(5));

        // 净重行
        JPanel netWeightRow = createFormRow("净重：", netWeightField = new JTextField(20));
        JLabel netWeightUnit = new JLabel("(kg)");
        netWeightUnit.setForeground(Color.GRAY);
        netWeightRow.add(netWeightUnit);
        netWeightField.setEditable(false);

        // 添加焦点监听器
        // 这里使用了匿名内部类的方式实现 FocusAdapter 抽象类，并重写了 focusLost 方法
        grossWeightField.addFocusListener(new java.awt.event.FocusAdapter() {
            // 当 grossWeightField 失去焦点时会触发此方法
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculateNetWeight();
            }
        });

        tareWeightField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculateNetWeight();
            }
        });

        panel.add(netWeightRow);
        panel.add(Box.createVerticalStrut(5));

        // 水分含量行
        JPanel moistureRow = createFormRow("水分含量：", moistureField = new JTextField(20));
        JLabel moistureUnit = new JLabel("(%)");
        moistureUnit.setForeground(Color.GRAY);
        moistureRow.add(moistureUnit);
        panel.add(moistureRow);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    private void calculateNetWeight() {
        try {
            String tareWeightText = tareWeightField.getText().trim();
            String grossWeightText = grossWeightField.getText().trim();

            if (tareWeightText.isEmpty() || grossWeightText.isEmpty()) {
                netWeightField.setText("");
                return;
            }

            double doubleTareWeight = Double.parseDouble(tareWeightText);
            double doubleGrossWeight = Double.parseDouble(grossWeightText);

            if (doubleGrossWeight < doubleTareWeight) {
                JOptionPane.showMessageDialog(null, "毛重不能小于皮重", "错误", JOptionPane.ERROR_MESSAGE);
                netWeightField.setText("");
                return;
            }

            double netWeight = doubleGrossWeight - doubleTareWeight;
            netWeightField.setText(String.format("%.2f", netWeight));
        } catch (NumberFormatException e) {
            netWeightField.setText("");
            JOptionPane.showMessageDialog(null, "请输入有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    public JPanel createDriverPane(){
        JPanel panel = createStandardPanel("司机信息");
        panel.setMinimumSize(new Dimension(250, 150));

        panel.add(createFormRow("司机名称：",driverNameField=new JTextField(20)));
        panel.add(Box.createVerticalStrut(5));
        panel.add(createFormRow("手机号：",phoneField=new JTextField(20)));
        panel.add(Box.createVerticalStrut(5));
        panel.add(createFormRow("车牌号：",plateNumberField=new JTextField(20)));

        return panel;
    }

    public JPanel createTimePane(){
        JPanel panel = createStandardPanel("时间信息");
        panel.setMinimumSize(new Dimension(250, 80));
        panel.add(createFormRow("入库时间：", leaseDate = new JDateChooser()));

        return panel;
    }
    public JPanel createButtonPane() {
        JPanel panel = createStandardPanel("操作");
        // 先初始化按钮
        confirmButton = new JButton("确认");
        cancelButton = new JButton("重置");
        printfButton = new JButton("打印");
        // 设置按钮大小
        confirmButton.setPreferredSize(new Dimension(70, 35));
        cancelButton.setPreferredSize(new Dimension(70, 35));
        printfButton.setPreferredSize(new Dimension(70, 35));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 验证输入
                if (!validateInputs()) {
                    return;
                }

                if(entryController.addWarehouse()){
                    entryController.addData();
                }
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cropNameField.setText("");// 用于输入作物名称的文本字段
                warehouseCombo.setSelectedIndex(0);// 用于选择入库仓库的下拉列表框
                grossWeightField.setText("");// 用于输入毛重的文本字段
                tareWeightField.setText("");// 用于输入皮重的文本字段
                netWeightField.setText("");// 用于输入净重的文本字段
                moistureField.setText("");// 用于输入水分含量的文本字段
                driverNameField.setText("");// 用于输入司机姓名的文本字段
                phoneField.setText("");// 用于输入司机姓名的文本字段
                plateNumberField.setText("");// 用于输入车牌号的文本字段
                leaseDate.setDate(null);
            }
        });

        printfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 获取JDateChooser中的日期并格式化
                String dateStr = "";
                if (leaseDate.getDate() != null) {
                    dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(leaseDate.getDate());
                } else {
                    // 如果没有选择日期，使用当前时间
                    dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new java.util.Date());
                }
                String[] rowData = {
                        cropNameField.getText(),
                        (String) warehouseCombo.getSelectedItem(),
                        grossWeightField.getText(),
                        tareWeightField.getText(),
                        netWeightField.getText(),
                        moistureField.getText(),
                        driverNameField.getText(),
                        phoneField.getText(),
                        plateNumberField.getText(),
                        dateStr
                };
                printfEntryView = new PrintfEntryView();
                printfEntryView.addDataToTable(rowData);
                printfEntryView.setVisible(true);
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cropNameField.setText("");
                grossWeightField.setText("");
                tareWeightField.setText("");
                netWeightField.setText("");
                moistureField.setText("");
                driverNameField.setText("");
                phoneField.setText("");
                plateNumberField.setText("");
                // 重置下拉列表框选择项为默认（这里假设第一个选项是默认选项，索引为0）
                warehouseCombo.setSelectedIndex(0);
                // 重置日期选择组件为未选择日期状态（设置为null）
                leaseDate.setDate(null);
            }
        });

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        // 添加按钮到面板
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(printfButton);
        panel.add(buttonPanel);
        return panel;
    }
    private JPanel createStandardPanel(String title) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(200, 200, 200)),
                        title
                ),
                new EmptyBorder(15, 15, 15, 15)
        ));
        return panel;
    }
    private JPanel createFormRow(String label, JComponent field) {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel jLabel = new JLabel(label);
        jLabel.setPreferredSize(new Dimension(100, 30));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(0, 5, 0, 10);
        panel.add(jLabel, gbc);
        gbc.gridx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        if (field instanceof JTextField) {
            ((JTextField) field).setPreferredSize(new Dimension(200, 30));
        }
        panel.add(field, gbc);
        return panel;
    }

    public String getCropNameField(){
        return cropNameField.getText();
    }
    public String getGrossWeightField(){
        return grossWeightField.getText();
    }
    public String getTareWeightField(){
        return tareWeightField.getText();
    }
    public String getNetWeightField(){
        return netWeightField.getText();
    }
    public String getMoistureField(){
        return  moistureField.getText();
    }
    public String getDriverNameField(){
        return driverNameField.getText();
    }
    public String getPhoneField(){
        return phoneField.getText();
    }
    public String getPlateNumberField(){
        return plateNumberField.getText();
    }
    public String getWareHouse(){
        return (String) warehouseCombo.getSelectedItem();
    }
    public String getData(){
        String dateStr = "";
        if (leaseDate.getDate() != null) {
            dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(leaseDate.getDate());
        } else {
            // 如果没有选择日期，使用当前时间
            dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                    .format(new java.util.Date());
        }
        return dateStr;
    }

    // 添加对于非法输入的判定方法
    private boolean validateInputs() {
        // 验证作物名称
        if (cropNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "作物名称不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证水分含量
        try {
            if (moistureField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "水分不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            String moistureText = moistureField.getText().trim();
            //moistureText.isEmpty()检验是否为空，空返回true,否则返回false
            if (!moistureText.isEmpty()) {
                double moisture = Double.parseDouble(moistureText);
                if (moisture < 0 || moisture > 100) {
                    JOptionPane.showMessageDialog(this, "水分含量必须在0-100%之间", "输入错误", JOptionPane.ERROR_MESSAGE);
                    moistureField.setText("");
                    return false;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "水分含量必须是有效的数字", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证重量
        try {
            String grossText = grossWeightField.getText().trim();
            String tareText = tareWeightField.getText().trim();

            if (grossText.isEmpty() || tareText.isEmpty()) {
                return false;
            }

            double grossWeight = Double.parseDouble(grossText);
            double tareWeight = Double.parseDouble(tareText);

            if (grossWeight <= 0 || tareWeight <= 0) {
                grossWeightField.setText("");
                tareWeightField.setText("");
                return false;
            }

            if (grossWeight <= tareWeight) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }

        // 验证手机号
        String phone = phoneField.getText().trim();
        if (!phone.isEmpty()) {
            if (phone.length() == 11) {
                //^：表示匹配字符串的开始位置，确保从开头进行匹配。
                //1：要求手机号必须以数字 1 开头,[3-9]：表示第二个数字必须是 3 到 9 中的任意一个数字
                //\\d{9}：\\d 表示匹配任意一个数字共9个
                if (!phone.matches("^1[3-9]\\d{9}$")) {
                    JOptionPane.showMessageDialog(this, "手机号格式不正确，请输入正确的11位手机号", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "手机号长度必须为11位", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // 验证车牌号
        String plateNumber = plateNumberField.getText().trim();
        //[A-Z0-9]{5,6}：规定了车牌号后续部分由 5个字符组成
        if (!plateNumber.isEmpty() && !plateNumber.matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{5}$")) {
            JOptionPane.showMessageDialog(this, "请输入有效的车牌号", "输入错误", JOptionPane.ERROR_MESSAGE);
            plateNumberField.setText("");
            return false;
        }

        return true;
    }
}