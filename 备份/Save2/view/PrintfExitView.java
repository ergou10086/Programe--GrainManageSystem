package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrintfExitView extends JFrame {
    private JTable table;//用于展示表格组件
    private DefaultTableModel tableModel;//用于管理表格模型

    public PrintfExitView(){
        init();
    }

    private void init(){
        setTitle("打印入库信息");
        setSize(800, 500);
        setLocationRelativeTo(null);

        String[] columnNames = {
                "公司名称", "负责人", "联系方式", "仓库","作物名称",
                "重量(kg)", "水分(%)", "单价(元/kg)", "总金额(元)",
                "司机姓名", "司机电话", "车牌号","出库时间"
        };

        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            // 重写isCellEditable方法，设置表格单元格不可编辑
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);//创建表格，与tableModel相关联

        setContentPane(createContentPane());
    }

    public JPanel createContentPane(){
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new EmptyBorder(8, 8, 8, 8));

        JScrollPane scrollPane = new JScrollPane(table);//创建滚动面板,将table数据过多时可滚动
        panel.add(scrollPane, BorderLayout.CENTER);

        // 创建底部按钮面板
        JPanel buttonPanel = new JPanel();

        // 创建导出到文件按钮
        JButton addButton = new JButton("确认");
        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(addButton);

        // 创建关闭按钮
        JButton closeButton = new JButton("关闭");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(closeButton);

        panel.add(buttonPanel, BorderLayout.SOUTH);

        return panel;
    }

    // 用于向表格中添加数据的方法
    public void addDataToTable(String[] rowData) {
        // 将传入的一行数据添加到表格模型中，从而在表格中显示新的数据行
        tableModel.addRow(rowData);
        // 更新表格的显示，确保界面上能及时看到新添加的数据
        table.updateUI();
    }
}
