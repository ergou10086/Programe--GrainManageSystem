package view;

import com.toedter.calendar.JDateChooser;
import controller.ExitController;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.nio.file.Files;

// 出库视图类，在这里实现出库管理的视图实现
public class ExitView extends JPanel{
    private JTextField companyName;
    private JTextField contactPerson;
    private JTextField contactPhone;
    private JComboBox<String> warehouseCombo;
    private JTextField cropName;
    private JTextField weight;
    private JTextField moisture;
    private JTextField price;
    private JTextField driverName;
    private JTextField driverPhone;
    private JTextField licensePlate;
    private JDateChooser leaseDate;
    private JButton photoButton;
    private JTextField totalAmount;
    private JButton confirmButton;
    private JButton cancelButton;
    private JButton printfButton;
    private double total;
    private PrintfExitView printfExitView;
    private JLabel photoPreview; // 添加照片预览标签
    private ExitController exitController;
    private byte[] currentImageData;// 添加成员变量存储当前图片数据

    public ExitView(){}
    public ExitView(ExitController exitController) {
        this.exitController = exitController;
        init();
    }

    public void init(){
        setLayout(new BorderLayout(10, 10));
        // 将创建的面板添加到ExitView中
        add(createCenterPane(), BorderLayout.CENTER);
    }
    public JPanel createCenterPane(){
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        // 添加整体边距
        panel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // 创建左侧面板存放主要信息
        JPanel leftPanel = new JPanel();
        //为左侧面板设置布局管理器为BoxLayout，且布局方向为垂直方向（Y_AXIS）
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
        leftPanel.add(createBuyPane());
        leftPanel.add(Box.createVerticalStrut(10)); // 添加垂直间距
        leftPanel.add(createGrainPane());
        leftPanel.add(Box.createVerticalStrut(10));
        //在垂直方向上尽可能地伸展，填充其leftPanel在垂直方向上剩余的可用空间。
        leftPanel.add(createTransport());

        // 创建右侧面板
        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.add(createPhotoPane());
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createAmountPane());
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(createButtonPane());

        panel.add(leftPanel, BorderLayout.CENTER);
        panel.add(rightPanel, BorderLayout.EAST);
        return panel;
    }

    public JPanel createBuyPane(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));//垂直布局

        //添加边框
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("收购方信息"),//边框标题为收购方信息
                new EmptyBorder(5,5,5,5)));//内边距为5

        //创建文本框
        panel.add(createFormRow("公司名称：",companyName=new JTextField(20)));
        panel.add(Box.createVerticalStrut(5));//添加垂直间距为5
        panel.add(createFormRow("负责人：",contactPerson=new JTextField(20)));
        panel.add(Box.createVerticalStrut(5));//添加垂直间距为5
        panel.add(createFormRow("联系方式：",contactPhone=new JTextField(20)));
        return panel;
    }

    public JPanel createGrainPane(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel,BoxLayout.Y_AXIS));//垂直布局
        JPanel warehousePanel = createFormRow("仓库名称：", warehouseCombo = new JComboBox<>(exitController.getWarehouse()));
        warehouseCombo.setPreferredSize(new Dimension(200, 25));
        panel.add(warehousePanel);
        //添加边框
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("粮食信息："),//边框标题为收购方信息
                new EmptyBorder(5,5,5,5)));//内边距为5

        panel.add(createFormRow("作物名称：",cropName=new JTextField(20)));
        panel.add(Box.createVerticalStrut(5));//添加垂直间距为5

        JPanel weightRow = createFormRow("重量数据：",weight=new JTextField(20));
        JLabel weightUnit = new JLabel("(kg)");
        weightUnit.setForeground(Color.GRAY);//设置为灰色字体
        weightRow.add(weightUnit);//添加到重量数据面板中
        panel.add(weightRow);
        panel.add(Box.createVerticalStrut(5));//添加垂直间距为5


        JPanel moistureRow = createFormRow("水分：",moisture=new JTextField(20));
        JLabel moistureUnit = new JLabel("(%)");
        moistureUnit.setForeground(Color.GRAY);//设置为灰色字体
        moistureRow.add(moistureUnit);//添加到重量数据面板中
        panel.add(moistureRow);
        panel.add(Box.createVerticalStrut(5));//添加垂直间距为5

        JPanel priceRow = createFormRow("单价：",price=new JTextField(20));
        JLabel priceUnit = new JLabel("(元/kg)");
        priceUnit.setForeground(Color.GRAY);//设置为灰色字体
        priceRow.add(priceUnit);//添加到重量数据面板中
        panel.add(priceRow);

        // 添加焦点监听器
        // 这里使用了匿名内部类的方式实现 FocusAdapter 抽象类，并重写了 focusLost 方法
        weight.addFocusListener(new java.awt.event.FocusAdapter() {
            // 当 grossWeightField 失去焦点时会触发此方法
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculateNetWeight();
            }
        });

        price.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                calculateNetWeight();
            }
        });
        return panel;
    }


    public JPanel createTransport(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("运输信息"),
                new EmptyBorder(5, 5, 5, 5)
        ));
        panel.add(createFormRow("出库时间：",  leaseDate = new JDateChooser()));
        panel.add(Box.createVerticalStrut(3));
        panel.add(createFormRow("司机姓名：", driverName = new JTextField(20)));
        panel.add(Box.createVerticalStrut(3));
        panel.add(createFormRow("联系方式：", driverPhone = new JTextField(20)));
        panel.add(Box.createVerticalStrut(3));
        panel.add(createFormRow("车牌号：", licensePlate = new JTextField(20)));

        return panel;
    }

    public JPanel createPhotoPane(){
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("照片记录"),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // 添加预览区域
        JPanel previewPanel = new JPanel();
        previewPanel.setPreferredSize(new Dimension(200, 200));
        previewPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));//边框设置为灰色
        previewPanel.setBackground(Color.WHITE);//背景设置为白色

        photoPreview = new JLabel();//创建一个JLable用于显示图片预览
        photoPreview.setPreferredSize(new Dimension(190,190));
        photoPreview.setHorizontalAlignment(JLabel.CENTER);//居中对齐
        previewPanel.add(photoPreview);

        photoButton = new JButton("上传图片");
        photoButton.setAlignmentX(Component.CENTER_ALIGNMENT);//居中对齐
        photoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();//创建一个文件选择器对象

                //设置文件过滤器，只允许选择特点扩展名的文件
                FileNameExtensionFilter filter = new FileNameExtensionFilter("图片文件","jpg","jpeg","png","gif");
                fileChooser.setFileFilter(filter);

                //显示文件选择界面，获取用户选择的结果
                int result = fileChooser.showOpenDialog(panel);
                //如果用户点击确认按钮
                if(result==JFileChooser.APPROVE_OPTION){
                    File selextedFile = fileChooser.getSelectedFile();//获取用户选则的图片
                    try{
                        currentImageData = Files.readAllBytes(selextedFile.toPath());//读取图片文件为字节数组
                        //创建一个对象用于表示原始图片
                        ImageIcon origialIcon = new ImageIcon(selextedFile.getPath());
                        Image origialImage = origialIcon.getImage();//获取origialIcon中的图片
                        //对图片进行放缩操作
                        Image scaledImage = origialImage.getScaledInstance(
                                190,190,Image.SCALE_SMOOTH);

                        ImageIcon scaledIcon = new ImageIcon(scaledImage);//放缩后的图片创建一个对象
                        photoPreview.setIcon(scaledIcon);//在预览区域中显示
                    }catch (Exception a){
                        // 如果在加载图片过程中出现异常，弹出一个错误消息对话框告知用户无法加载图片
                        JOptionPane.showMessageDialog(panel,
                                "无法加载图片", "错误",
                                JOptionPane.ERROR_MESSAGE);
                        currentImageData = null;
                    }
                }
            }
        });
        panel.add(previewPanel);
        panel.add(Box.createVerticalStrut(10));
        panel.add(photoButton);
        panel.add(Box.createVerticalStrut(5));

        return panel;
    }

    // 创建createFormRow方法来实现更好的对齐
    // 该方法用于创建包含标签和文本框（或其他组件）的一行信息面板
    //JComponent 有许多重要的子类JButton，JTextField等
    private JPanel createFormRow(String label, JComponent field) {
        JPanel panel = new JPanel(new GridBagLayout());//使用GridBagLayout布局
        // GridBagLayout 中的组件可以占据一个或多个连续的网格单元，并且可以在这些单元中具有不同的对齐方式、拉伸行为等。

        GridBagConstraints gbc = new GridBagConstraints();//创建了一个 GridBagConstraints 类型的对象 gbc。

        JLabel jLabel=new JLabel(label);//获取标签
        //设置标签的大小
        jLabel.setPreferredSize(new Dimension(80,25));//宽为80高为25
        //gbc.gridx 和 gbc.gridy 属性用于指定组件在网格中的横坐标和纵坐标。
        gbc.gridx=0;
        gbc.gridy=0;
        gbc.anchor = GridBagConstraints.WEST;//左对齐
        //设置左右间距为5
        gbc.insets = new Insets(0,5,0,5);//用于指定组件与网格单元的上、下、左、右边界之间的间距。
        panel.add(jLabel,gbc);

        //文本框设置
        gbc.gridx=1;
        gbc.fill=GridBagConstraints.HORIZONTAL;//设置组件的填充方式为水平填充
        gbc.weightx=1.0;//让文本占据剩余空间
        if(field instanceof  JTextField){
            ((JTextField)field).setPreferredSize(new Dimension(200,25));
        }
        panel.add(field,gbc);
        return panel;
    }

    // 添加金额计算面板
    private JPanel createAmountPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("金额计算"),
                new EmptyBorder(5, 5, 5, 5)
        ));
        panel.setPreferredSize(new Dimension(200, 80));

        // 总金额显示
        JPanel amountPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        amountPanel.add(new JLabel("总金额(/元)："));
        totalAmount = new JTextField(12);
        totalAmount.setEditable(false);//使文本框不可修改
        totalAmount.setBackground(Color.WHITE);
        amountPanel.add(totalAmount);

        panel.add(amountPanel);

        return panel;
    }

    private void calculateNetWeight(){
        try {
            String weightText = weight.getText().trim();
            String priceText = price.getText().trim();

            if (weightText.isEmpty() || priceText.isEmpty()) {
                totalAmount.setText("");
                return;
            }

            double doubleWeightText = Double.parseDouble(weightText);
            double doublePriceText = Double.parseDouble(priceText);

            total = doublePriceText*doubleWeightText;
            totalAmount.setText(String.format("%.2f", total));
        } catch (NumberFormatException e) {
            totalAmount.setText("");
            JOptionPane.showMessageDialog(null, "请输入有效的数字", "错误", JOptionPane.ERROR_MESSAGE);
        }
    }

    // 添加确认按钮面板
    private JPanel createButtonPane() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder("操作"),
                new EmptyBorder(5, 5, 5, 5)
        ));
        panel.setPreferredSize(new Dimension(200, 80));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        confirmButton = new JButton("确认");
        confirmButton.setPreferredSize(new Dimension(80, 25));
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // 验证输入
                if (!validateInputs()) {
                    return;
                }

                if(exitController.addWarehouse()){
                    exitController.addData();
                }
            }
        });
        buttonPanel.add(confirmButton);

        cancelButton = new JButton("重置");
        cancelButton.setPreferredSize(new Dimension(80, 25));
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                companyName.setText("");
                contactPerson.setText("");
                contactPhone.setText("");
                cropName.setText("");
                weight.setText("");
                moisture.setText("");
                price.setText("");
                driverName.setText("");
                driverPhone.setText("");
                licensePlate.setText("");
                totalAmount.setText("");
                leaseDate.setDate(null);
                // 重置下拉列表框选择项为默认（这里假设第一个选项是默认选项，索引为0）
                warehouseCombo.setSelectedIndex(0);
                photoPreview.setIcon(null); // 清除照片预览
            }
        });

        buttonPanel.add(cancelButton);

        printfButton = new JButton("打印");
        printfButton.setPreferredSize(new Dimension(80, 25));
        printfButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String dateStr = "";
                if (leaseDate.getDate() != null) {
                    dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(leaseDate.getDate());
                } else {
                    // 如果没有选择日期，使用当前时间
                    dateStr = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                            .format(new java.util.Date());
                }
                // 收集当前视图中的所有数据
                String[] rowData = {
                        companyName.getText(),
                        contactPerson.getText(),
                        contactPhone.getText(),
                        (String) warehouseCombo.getSelectedItem(),
                        cropName.getText(),
                        weight.getText(),
                        moisture.getText(),
                        price.getText(),
                        totalAmount.getText(),
                        driverName.getText(),
                        driverPhone.getText(),
                        licensePlate.getText(),
                        dateStr
                };

                // 创建PrintfExitView实例并传递数据
                printfExitView = new PrintfExitView();
                printfExitView.addDataToTable(rowData);
                printfExitView.setVisible(true);
            }
        });
        buttonPanel.add(printfButton);

        panel.add(buttonPanel);

        return panel;
    }

    public String getcompanyName(){
        //取输入框的值
        return companyName.getText();
    }

    public String getcontactPerson(){
        //取输入框的值
        return contactPerson.getText();
    }

    public String getcontactPhone(){
        //取输入框的值
        return contactPhone.getText();
    }

    public String getcropName(){
        //取输入框的值
        return cropName.getText();
    }

    public String getweight(){
        //取输入框的值
        return weight.getText();
    }

    public String getmoisture(){
        //取输入框的值
        return moisture.getText();
    }

    public String getprice(){
        //取输入框的值
        return price.getText();
    }

    public String getdriverName(){
        //取输入框的值
        return driverName.getText();
    }

    public String getdriverPhone(){
        //取输入框的值
        return driverPhone.getText();
    }

    public String getlicensePlate(){
        //取输入框的值
        return licensePlate.getText();
    }
    public String getTotal(){
        return totalAmount.getText();
    }
    public String getWareHouse(){
        return (String) warehouseCombo.getSelectedItem();
    }
    // 添加获取图片数据的方法
    public byte[] getImageData() {
        return currentImageData;
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
        // 验证公司名称
        if (companyName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "公司名称不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证负责人
        if (contactPerson.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "负责人不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证联系方式
        String phone = contactPhone.getText().trim();
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

        // 验证作物名称
        if (cropName.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "作物名称不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证重量
        try {
            String weightText = weight.getText().trim();
            if (weightText.isEmpty()) {
                return false;
            }

            double weightValue = Double.parseDouble(weightText);
            if (weightValue <= 0) {
                return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }

        // 验证水分含量
        try {
            String moistureText = moisture.getText().trim();
            if (!moistureText.isEmpty()) {
                double moistureValue = Double.parseDouble(moistureText);
                if (moistureValue < 0 || moistureValue > 100) {
                    JOptionPane.showMessageDialog(this, "水分含量必须在0-100%之间", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "水分含量必须是有效的数字", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证单价
        try {
            String priceText = price.getText().trim();
            if (priceText.isEmpty()) {
                JOptionPane.showMessageDialog(this, "单价不能为空", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }

            double priceValue = Double.parseDouble(priceText);
            if (priceValue <= 0) {
                JOptionPane.showMessageDialog(this, "单价必须大于0", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "单价必须是有效的数字", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        // 验证司机手机号
        String driverPhoneText = driverPhone.getText().trim();
        if (!driverPhoneText.isEmpty()) {
            if (driverPhoneText.length() == 11) {
                if (!driverPhoneText.matches("^1[3-9]\\d{9}$")) {
                    JOptionPane.showMessageDialog(this, "司机手机号格式不正确，请输入正确的11位手机号", "输入错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            } else {
                JOptionPane.showMessageDialog(this, "司机手机号长度必须为11位", "输入错误", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }

        // 验证车牌号
        String plateNumberText = licensePlate.getText().trim();
        if (!plateNumberText.isEmpty() && !plateNumberText.matches("^[京津沪渝冀豫云辽黑湘皖鲁新苏浙赣鄂桂甘晋蒙陕吉闽贵粤青藏川宁琼使领][A-Z][A-Z0-9]{5,6}$")) {
            JOptionPane.showMessageDialog(this, "请输入有效的车牌号", "输入错误", JOptionPane.ERROR_MESSAGE);
            return false;
        }

        return true;
    }
}