package view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PrintfEntryView extends JFrame {
    private JTable table;
    private DefaultTableModel tableModel;

    public PrintfEntryView(){
        init();
    }

    public void init(){
        setTitle("打印入库信息");
        setSize(700, 500);
        setLocationRelativeTo(null);

        String[] columnNames = {"作物名称","仓库名称","毛重","皮重","净重","水分含量","司机名称","手机号","车牌号","入库时间"};
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

    public void addDataToTable(String[] rowData) {
        // 将传入的一行数据添加到表格模型中，从而在表格中显示新的数据行
        tableModel.addRow(rowData);
        // 更新表格的显示，确保界面上能及时看到新添加的数据
        table.updateUI();
    }
}
