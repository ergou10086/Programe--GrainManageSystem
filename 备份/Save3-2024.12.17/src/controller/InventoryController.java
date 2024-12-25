package controller;

import entity.Grain;
import entity.WareHouse;
import exceptions.DAOException;
import service.IGrainInfService;
import service.IWarehouseInfService;
import service.impl.GrainInfServiceImpl;
import service.impl.WarehouseInfServiceImpl;
import view.InventoryView;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

// 库存管理页面对应的控制器类
public class InventoryController {
    private InventoryView view;
    private String currentUser;
    private String userRole;
    //private GrainTest graintest;
    private IGrainInfService iGrainInfService;
    private IWarehouseInfService iWarehouseInfService;
    private List<String>getnotice;
    private String getproblem;

    public IWarehouseInfService getiWarehouseInfService() {
        return iWarehouseInfService;
    }

    public IGrainInfService getiGrainInfService() {
        return iGrainInfService;
    }

    public List<Integer> str(){
         List<Integer> integers=new ArrayList<>();
         JTable table=view.getTable();
         DefaultTableModel model=view.getDtm();
         getnotice=new ArrayList<>();
         getproblem="库存小于预警阈值";
         for (int i=0;i<table.getRowCount();i++){
             if(Double.parseDouble(model.getValueAt(i,6).toString())<Double.parseDouble(model.getValueAt(i,7).toString())){
                 getnotice.add(table.getValueAt(i,1).toString());
                 integers.add(i);
             }
         }
         return integers;
    }
    public List<Integer> str1(String j){
        List<Integer> integers=new ArrayList<>();
        JTable table=view.getTable();
        DefaultTableModel model=view.getDtm();
        getproblem="库存小于一定值";
        getnotice=new ArrayList<>();
        for (int i=0;i<table.getRowCount();i++){
            if(Double.parseDouble(model.getValueAt(i,6).toString())<Double.parseDouble(j.toString())){
                getnotice.add(table.getValueAt(i,1).toString());
                integers.add(i);
            }
        }
        return integers;
    }
    public List<Integer> str2(){
        List<Integer> integers=new ArrayList<>();
        JTable table=view.getTable();
        DefaultTableModel model=view.getDtm();
        getproblem="仓库状态异常";
        getnotice=new ArrayList<>();
        for (int i=0;i<table.getRowCount();i++){
            if(model.getValueAt(i,5).toString().equals("异常")){
                getnotice.add(table.getValueAt(i,1).toString());
                integers.add(i);
            }
        }
        return integers;
    }
    public InventoryController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        this.view = new InventoryView();
        this.iGrainInfService=new GrainInfServiceImpl();
        this.iWarehouseInfService=new WarehouseInfServiceImpl();
        this.view.setInventoryController(this);
        initializeListeners();
//        loadWarehouseData();
//        setupPermissions();
    }


    public JPanel getView(){
        return view;
    }
    //删除
    public void delete(){
        int row=view.getTable().getSelectedRow();
        if(row>=0) {
            try {
                String grainCode = (String) view.getDtm().getValueAt(row, 1);
                int confirm = JOptionPane.showConfirmDialog(view, "确定要删除这条记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm==JOptionPane.YES_OPTION){
                List<WareHouse> wareHouses = iWarehouseInfService.delete2(grainCode);
                DefaultTableModel model = view.getDtm();
                model.setRowCount(0);
                    for (WareHouse wareHouse : wareHouses) {
                        if(wareHouse.getNotice()==null ||wareHouse.getNotice().equals("")){wareHouse.setNotice("0");}
                        model.addRow(new Object[]{
                                wareHouse.getId(),
                                wareHouse.getWarehouseCode(),
                                wareHouse.getWarehouseName(),
                                wareHouse.getWarehouseLocation(),
                                wareHouse.getWarehouseType(),
                                wareHouse.getStatus(),
                                wareHouse.getCapacity(),
                                wareHouse.getNotice(),
                        });
                    }
                    notice();
                }
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "删除数据失败");
            }
        }
    }
    public void loadGrainData() {
        try {
            System.out.println("开始加载数据...");
            // 调用服务层的方法获取所有粮食信息，返回一个Grain对象的列表
            List<WareHouse> wareHouses = iWarehouseInfService.getdata2();
            System.out.println("获取到 " + wareHouses.size() + " 条数据");
            // 获取视图中对应的表格数据模型，用于后续操作表格数据
            DefaultTableModel model = view.getDtm();
            JTable table=view.getTable();
            model.setRowCount(0);
            //setOneRowBackgroundColor(table,1,Color.red);
            for (WareHouse wareHouse : wareHouses) {
                if(wareHouse.getNotice()==null||wareHouse.getNotice().equals("")){wareHouse.setNotice("0");}
                model.addRow(new Object[]{
                        wareHouse.getId(),
                        wareHouse.getWarehouseCode(),
                        wareHouse.getWarehouseName(),
                        wareHouse.getWarehouseLocation(),
                        wareHouse.getWarehouseType(),
                        wareHouse.getStatus(),
                        wareHouse.getCapacity(),
                        wareHouse.getNotice(),
                });
            }
            notice();
            System.out.println(getnotice.size());
            System.out.println("数据加载完成");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "加载数据失败：" + e.getMessage());
        }
    }
    public void search(){
        try {
            int row=view.getTable().getSelectedRow();
            System.out.println(view.getTable().getSelectedRow());
            System.out.println("开始加载数据...");
            String key=view.getsearchcontent();
            List<WareHouse> wareHouses = iWarehouseInfService.search2(key);
            System.out.println("获取到 " + wareHouses.size() + " 条数据");
            DefaultTableModel dtm = view.getDtm();
            dtm.setRowCount(0);
            for (WareHouse wareHouse : wareHouses) {
                if(wareHouse.getNotice()==null ||wareHouse.getNotice().equals("")){wareHouse.setNotice("0");}
                dtm.addRow(new Object[]{
                        wareHouse.getId(),
                        wareHouse.getWarehouseCode(),
                        wareHouse.getWarehouseName(),
                        wareHouse.getWarehouseLocation(),
                        wareHouse.getWarehouseType(),
                        wareHouse.getStatus(),
                        wareHouse.getCapacity(),
                        wareHouse.getNotice(),
                });
            }
            notice();
            System.out.println("数据加载完成");
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "加载数据失败：" + e.getMessage());
        }
    }
    public void set(){
        try {
            int row = view.getTable().getSelectedRow();
            String notice = view.getSet();
            if(notice.equals("")){notice="0";}
            if (row >= 0) {
                String code = (String) view.getDtm().getValueAt(row, 1);
                List<WareHouse> wareHouses = iWarehouseInfService.set3(notice, code);
                DefaultTableModel dtm = view.getDtm();
                dtm.setRowCount(0);
                for (WareHouse wareHouse : wareHouses) {
                    if(wareHouse.getNotice()==null ||wareHouse.getNotice().equals("")){wareHouse.setNotice("0");}
                    dtm.addRow(new Object[]{
                            wareHouse.getId(),
                            wareHouse.getWarehouseCode(),
                            wareHouse.getWarehouseName(),
                            wareHouse.getWarehouseLocation(),
                            wareHouse.getWarehouseType(),
                            wareHouse.getStatus(),
                            wareHouse.getCapacity(),
                            wareHouse.getNotice(),
                    });
                }
                notice();
            }
        }catch (DAOException e){
            e.printStackTrace();
        }
    }
    public void manyset(){
        try {
            int[] rows = view.getTable().getSelectedRows();
            String notice = view.getSet();
            if(notice.equals("")){notice="0";}
            for (int row : rows) {
                if (row >= 0) {
                    String code = (String) view.getDtm().getValueAt(row, 1);
                    List<WareHouse> wareHouses = iWarehouseInfService.set3(notice, code);
                    DefaultTableModel dtm = view.getDtm();
                    dtm.setRowCount(0);
                    for (WareHouse wareHouse : wareHouses) {
                        if(wareHouse.getNotice()==null ||wareHouse.getNotice().equals("")){wareHouse.setNotice("0");}
                        dtm.addRow(new Object[]{
                                wareHouse.getId(),
                                wareHouse.getWarehouseCode(),
                                wareHouse.getWarehouseName(),
                                wareHouse.getWarehouseLocation(),
                                wareHouse.getWarehouseType(),
                                wareHouse.getStatus(),
                                wareHouse.getCapacity(),
                                wareHouse.getNotice(),
                        });
                    }
                    notice();
                }
            }
        }catch (DAOException e){
            e.printStackTrace();
        }
    }
    public  void setOneRowBackgroundColor() {
        JTable table=view.getTable();
        List<Integer> integerList=str();
        try {
            DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable table,
                                                               Object value, boolean isSelected, boolean hasFocus,
                                                               int row, int column) {
                    for (Integer i :integerList) {
                        if(row==i)
                        {
                            setBackground(Color.RED);
                            break;
                        }else{
                            setBackground(table.getBackground());
                        }
                    }
                    return super.getTableCellRendererComponent(table, value,
                            isSelected, hasFocus, row, column);
                }
            };
            int columnCount = table.getColumnCount();
            for (int i = 0; i < columnCount; i++) {
                table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    public  void setOneRowBackgroundColor1() {
        JTable table=view.getTable();
        String j=view.getroleset();
        if(!j.equals("")) {
            List<Integer> integerList = str1(j);
            try {
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(JTable table,
                                                                   Object value, boolean isSelected, boolean hasFocus,
                                                                   int row, int column) {
                        for (Integer i : integerList) {
                            if (row == i) {
                                setBackground(Color.RED);
                                break;
                            } else {
                                setBackground(table.getBackground());
                            }
                        }
                        return super.getTableCellRendererComponent(table, value,
                                isSelected, hasFocus, row, column);
                    }
                };
                int columnCount = table.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
    public  void setOneRowBackgroundColor2() {
        JTable table=view.getTable();
        String j=view.getroleset();
            List<Integer> integerList = str2();
            try {
                DefaultTableCellRenderer tcr = new DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(JTable table,
                                                                   Object value, boolean isSelected, boolean hasFocus,
                                                                   int row, int column) {
                        for (Integer i : integerList) {
                            if (row == i) {
                                setBackground(Color.RED);
                                break;
                            } else {
                                setBackground(table.getBackground());
                            }
                        }
                        return super.getTableCellRendererComponent(table, value,
                                isSelected, hasFocus, row, column);
                    }
                };
                int columnCount = table.getColumnCount();
                for (int i = 0; i < columnCount; i++) {
                    table.getColumn(table.getColumnName(i)).setCellRenderer(tcr);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
    }
    public void inseart(){
        try {
            int confirm = JOptionPane.showConfirmDialog(view, "要进行预警记录吗", "预警记录", JOptionPane.YES_NO_OPTION);
            if(confirm==JOptionPane.YES_OPTION){
        for(String i :getnotice){
            WareHouse wareHouse=iWarehouseInfService.findbycode(i,getproblem);
            iWarehouseInfService.add2(wareHouse);
        }
                JOptionPane.showMessageDialog(view, "记录成功");
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void notice(){
        JTable table=view.getTable();
        DefaultTableModel model=view.getDtm();
        String role1=view.getroleset();
        JComboBox role=view.getcombobox();
        try {
            int i=role.getSelectedIndex();
            if(i==0){
                setOneRowBackgroundColor();
            }
            if(i==1){
                    setOneRowBackgroundColor1();
                }
            if(i==2){
                setOneRowBackgroundColor2();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void rollset(){
    loadGrainData();
    }

    private void initializeListeners() {
        view.getsearch().addActionListener(e -> search());
        view.getDelete().addActionListener(e -> delete());
        view.getSet1().addActionListener(e -> set());
        view.getjButton().addActionListener(e -> rollset());
        view.getSet2().addActionListener(e -> manyset());
        view.getWrite().addActionListener(e -> inseart());
    }
}
