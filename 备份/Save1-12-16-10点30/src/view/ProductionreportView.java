package view;

import controller.InspectionController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

// 生成报告界面的类
public class ProductionreportView extends JFrame {
    private JTable table;
    private DefaultTableModel defaultTableModel;
    private JButton confirm;
    private JButton no1;
    private JTextArea textArea;
    private InspectionController inspectionController;

    public void setInspectionController(InspectionController inspectionController) {
        this.inspectionController = inspectionController;
    }

    public ProductionreportView (){
        init();
    }
    private void init(){
        setTitle("生成报告");
        setSize(600,400);
        setLocationRelativeTo(null);
        setContentPane(createCenterPane());
    }

    private JPanel createCenterPane(){
        JPanel panel=new JPanel(new BorderLayout(5,5));
        panel.setBorder(new EmptyBorder(8,28,8,28));
        textArea=new JTextArea();
        panel.add(textArea,BorderLayout.CENTER);
        panel.add(createsouth(),BorderLayout.SOUTH);
        return panel;
    }


    private JPanel createsouth(){
        JPanel panel=new JPanel(new BorderLayout(2,2));
        panel.add(createbutton(),BorderLayout.CENTER);
        return panel;
    }

    private JPanel createbutton(){
        JPanel panel=new JPanel();
        confirm=new JButton("确认");
        no1= new JButton("取消");
        panel.add(confirm);
        panel.add(no1);
        return panel;
    }

    public JTextArea getTextArea(){
        return textArea;
    }
    public JButton getConfirm(){
        return confirm;
    }
    public JButton getNo1(){
        return no1;
    }
}
