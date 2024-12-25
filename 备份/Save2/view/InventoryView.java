package view;

import controller.InventoryController;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class InventoryView extends JPanel {
    private JTextField search;
    private JTable table;
    private JButton set1;
    private JButton set2;
    private JButton write;
    private JTextField set;
    private JButton search2;
    private JButton delete;
    private JComboBox role;
    private JTextField number1;
    private JButton jButton;
    private InventoryController inventoryController;
    DefaultTableModel dtm;
    JScrollPane scrollPane;
    public InventoryController getInventoryController() {
        return inventoryController;
    }

    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
        loaddata();
    }
    private void loaddata() {
        inventoryController.loadGrainData();
    }

    public InventoryView(){
        setLayout(new BorderLayout(10, 10));
        createCenterPane();
    }
    private void createCenterPane(){
        JPanel panel=new JPanel(new BorderLayout(5,5));
        setBorder(new EmptyBorder(8,28,8,28));
        add(creatnorth(),BorderLayout.NORTH);
        add(createmiddle(),BorderLayout.CENTER);
        add(createnorthmiddle(),BorderLayout.SOUTH);
    }
    private JPanel creatnorth(){
        JPanel panel=new JPanel(new GridLayout(1,1,2,2));
        panel.add(creatnorthmiddle2());
        return panel;
    }
    private JPanel creatnorthmiddle2(){
        JPanel panel =new JPanel(new GridLayout(1,3,5,2));
         search=new JTextField();
        panel.add(search);
        panel.add(createbutton());
        return panel;
    }
    private JPanel createbutton(){
        JPanel panel=new JPanel();
        search2=new JButton("搜索");
        delete = new JButton("删除");
        panel.add(search2);
        panel.add(delete);
        return panel;
    }
    private JPanel createnorthmiddle(){
        JPanel panel=new JPanel( new GridLayout(2,2,6,2));
        jButton=new JButton("设置");
        set=new JTextField();
        panel.add(create1());
        panel.add(jButton);
        panel.add(set);
        panel.add(create2());
        return panel;
    }
    private JPanel create1(){
        JPanel panel=new JPanel(new GridLayout(1,3,2,2));
        JLabel jLabel=new JLabel("预警规则设置：",JLabel.CENTER);
        number1=new JTextField();
        role=new JComboBox<>();
        role.addItem("库存小于预警阈值");
        role.addItem("库存低于一定数值(输入该值)");
        role.addItem("仓库状态异常");
        role.setSelectedIndex(0);
        panel.add(jLabel);
        panel.add(role);
        panel.add(number1);
        return panel;
    }
    private JPanel create2(){
        JPanel panel=new JPanel(new GridLayout(1,2,2,2));
        set1=new JButton("设置");
        set2=new JButton("批量设置");
        write=new JButton("预警记录");
        panel.add(set1);
        panel.add(set2);
        panel.add(write);
        return panel;
    }
    private JScrollPane createmiddle(){
        //JPanel panel=new JPanel();
        String[] head={"编号","仓库编号","仓库名称","仓库地址","仓库类型","仓库状态","库存","预警阈值"};
        dtm =new DefaultTableModel();
        for (int j=0;j<head.length;j++){
            dtm.addColumn(head[j]);
        }
        for(int i=0;i<10;i++){
            dtm.addRow(new Object[]{i+1,i+2,i+3,i+4});
        }
        table=new JTable(dtm);
        scrollPane=new JScrollPane(table);
        return scrollPane;
    }
    public DefaultTableModel getDtm(){
        return dtm;
    }
    public JButton getsearch(){
        return search2;
    }
    public String getsearchcontent(){
        return search.getText();
    }
    public JTable getTable(){
        return table;
    }
    public JButton getDelete(){
        return delete;
    }
    public String getSet(){
        return set.getText();
    }
    public JButton getSet1(){
        return set1;
    }
    public JComboBox getcombobox(){
        return role;
    }
    public String getroleset(){
        return number1.getText();
    }
    public JButton getjButton(){
        return jButton;
    }
    public JButton getSet2(){
        return set2;
    }
    public JButton getWrite(){
        return write;
    }
}
