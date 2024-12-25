package view;

import controller.InspectionController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TrackproblemView extends JFrame {
    private JTextArea textAreanorth;
    private JTextArea textmiddle1;
    private JTextArea textmiddle2;
    private JTextArea textmiddle3;
    private JButton confirm;
    private JComboBox jComboBox;
    private JButton no2;
    private JTextField jTextField;
    private InspectionController inspectionController;

    public void setInspectionController(InspectionController inspectionController) {
        this.inspectionController = inspectionController;
    }

    public TrackproblemView(){
        init();
    }
    private void init(){
        setTitle("问题追踪");
        setSize(600,400);
        setLocationRelativeTo(null);
        setContentPane(createCenterPane());
    }
    private JPanel createCenterPane(){
        JPanel panel=new JPanel(new BorderLayout(5,5));
        panel.setBorder(new EmptyBorder(8,28,8,28));
        panel.add(createnorth(),BorderLayout.NORTH);
        panel.add(createmiddle(),BorderLayout.CENTER);
        panel.add(createsouth(),BorderLayout.SOUTH);
        return panel;
    }
    private JPanel createnorth(){
        JPanel panel=new JPanel();
        textAreanorth=new JTextArea();
        panel.add(textAreanorth);
        return panel;
    }
    private JPanel createmiddle(){
        JPanel panel=new JPanel(new GridLayout(3,1,4,4));
        textmiddle1=new JTextArea();
        textmiddle2=new JTextArea();
        textmiddle3=new JTextArea();
        panel.add(textmiddle1);
        panel.add(textmiddle2);
        panel.add(textmiddle3);
        return panel;
    }
    private JPanel createsouth(){
        JPanel panel=new JPanel();
         jComboBox=new JComboBox<>();
         jComboBox.addItem("处理中");
         jComboBox.addItem("处理完成");
          jTextField=new JTextField(20);
         JLabel jLabel=new JLabel("处理措施",JLabel.CENTER);
        confirm =new JButton("记录");
        no2=new JButton("确认");
        panel.add(jComboBox);
        panel.add(jLabel);
        panel.add(jTextField);
        panel.add(confirm);
        panel.add(no2);
        return panel;
    }

    public JTextArea getTextAreanorth() {
        return textAreanorth;
    }

    public JTextArea getTextmiddle1() {
        return textmiddle1;
    }

    public JTextArea getTextmiddle2() {
        return textmiddle2;
    }

    public JTextArea getTextmiddle3() {
        return textmiddle3;
    }

    public JButton getConfirm() {
        return confirm;
    }

    public JButton getNo2() {
        return no2;
    }

    public JComboBox getjComboBox() {
        return jComboBox;
    }

    public String getjTextField() {
        return jTextField.getText();
    }
}
