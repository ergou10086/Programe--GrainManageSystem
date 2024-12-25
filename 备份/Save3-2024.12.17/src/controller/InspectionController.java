package controller;

import entity.WareHouse;
import exceptions.DAOException;
import exceptions.GrainOrWarehouseServiceExceptions;
import service.IWarehouseInfService;
import service.impl.WarehouseInfServiceImpl;
import view.InspectionView;
import view.ProductionreportView;
import view.TrackproblemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

// 巡库管理页面对应的控制器类，实现巡库信息的记录
public class InspectionController {
    private InspectionView view;
    private ProductionreportView productionreportView;
    private TrackproblemView trackproblemView;
    private String currentUser;
    private String userRole;
    private IWarehouseInfService iWarehouseInfService;

    public IWarehouseInfService getiWarehouseInfService() {
        return iWarehouseInfService;
    }

    public InspectionController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        this.view = new InspectionView();
        productionreportView=new ProductionreportView();
        trackproblemView=new TrackproblemView();
        trackproblemView.setInspectionController(this);
        productionreportView.setInspectionController(this);
        initializeListeners();
        this.iWarehouseInfService=new WarehouseInfServiceImpl();
        view.setInspectionController(this);
//        loadWarehouseData();
//        setupPermissions();
    }
    public void loaddata(){
        try {
            System.out.println("开始加载数据...");
            // 调用服务层的方法获取所有粮食信息，返回一个Grain对象的列表
            List<WareHouse> wareHouses = iWarehouseInfService.getdata1();
            System.out.println("获取到 " + wareHouses.size() + " 条数据");
            // 获取视图中对应的表格数据模型，用于后续操作表格数据
            DefaultTableModel model = view.getDefaultTableModel();
            // 先清空表格中的现有数据，为重新加载新数据做准备
            model.setRowCount(0);

            // 遍历每一个获取到的Grain对象
            for (WareHouse wareHouse : wareHouses) {
                model.addRow(new Object[]{
                        wareHouse.getWarehouseCode(),
                        wareHouse.getWarehouseName(),
                        wareHouse.getSnowtime(),
                        wareHouse.getTemper(),
                        wareHouse.getHumidity(),
                        wareHouse.getProblem(),
                        wareHouse.getDeal(),
                        wareHouse.getSpectionpeople(),
                        wareHouse.getSpectiontime()
                });
                System.out.println("添加数据行：" + wareHouse.getWarehouseCode());
            }
            System.out.println("数据加载完成");
        } catch (Exception e) {
            try {
                JOptionPane.showMessageDialog(view, "加载数据失败：" + e.getMessage());
                throw new GrainOrWarehouseServiceExceptions.LoadGrainFailedException(e.getMessage());
            } catch (GrainOrWarehouseServiceExceptions.LoadGrainFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void search(){
        try{
            // 获取搜索关键字
            String keyword = JOptionPane.showInputDialog(
                    view,
                    "请输入要搜索的仓库编号、名称或类型：\n(留空显示所有记录)",
                    "搜索巡库记录",
                    JOptionPane.QUESTION_MESSAGE
            );
            List<WareHouse> wareHouses;

            // 用户点击取消按钮
            if (keyword == null) {
                return;
            }

            if(keyword.trim().isEmpty()){
                // 如果搜索框为空，显示所有粮食信息
                wareHouses = iWarehouseInfService.getdata1();
            }else{
                wareHouses = iWarehouseInfService.search1(keyword.trim());
            }

            // 更新表格
            DefaultTableModel model = view.getDefaultTableModel();
            model.setRowCount(0); // 清空表格

            if(wareHouses.isEmpty()){
                JOptionPane.showMessageDialog(view, "未找到匹配的粮食信息", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            }else{
                // 添加搜索结果
                for (WareHouse wareHouse : wareHouses) {
                    model.addRow(new Object[]{
                            wareHouse.getWarehouseCode(),
                            wareHouse.getWarehouseName(),
                            wareHouse.getSnowtime(),
                            wareHouse.getTemper(),
                            wareHouse.getHumidity(),
                            wareHouse.getProblem(),
                            wareHouse.getDeal(),
                            wareHouse.getSpectionpeople(),
                            wareHouse.getSpectiontime()
                    });
                }
                // 显示搜索结果
                JOptionPane.showMessageDialog(view, String.format("找到 %d 条匹配的记录", wareHouses.size()), "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (Exception e){
            System.err.println("搜索失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "搜索失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
            try {
                throw new GrainOrWarehouseServiceExceptions.SearchGrainFailedException(e.getMessage());
            } catch (GrainOrWarehouseServiceExceptions.SearchGrainFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public void add(){
        try {
            String code=view.getcode();
            String name=view.getname();
            String snowtime=view.getsnowtime();
            String temper=view.gettemper();
            String humdity=view.gethumdity();
            String problem = view.getproblem();
            String deal=view.getdeal();
            String spectionpeople=view.getspectionpeople();
            DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime ldt2 = LocalDateTime.now();
            String str2 = ldt2.format(dtf3);
            if(!code.equals("")&&!name.equals("")&&!snowtime.equals("")&&!temper.equals("")&&!humdity.equals("")&&!problem.equals("")&&!deal.equals("")&&!spectionpeople.equals("")) {
                List<WareHouse> wareHouses = iWarehouseInfService.add1(code, name, snowtime, temper, humdity, problem, deal, spectionpeople, str2);
                DefaultTableModel model = view.getDefaultTableModel();
                model.setRowCount(0);
                for (WareHouse wareHouse : wareHouses) {
                    model.addRow(new Object[]{
                            wareHouse.getWarehouseCode(),
                            wareHouse.getWarehouseName(),
                            wareHouse.getSnowtime(),
                            wareHouse.getTemper(),
                            wareHouse.getHumidity(),
                            wareHouse.getProblem(),
                            wareHouse.getDeal(),
                            wareHouse.getSpectionpeople(),
                            wareHouse.getSpectiontime()
                    });
                }
                JOptionPane.showMessageDialog(view, String.format("添加成功"), "添加结果", JOptionPane.INFORMATION_MESSAGE);
            }
            else {
                JOptionPane.showMessageDialog(view, String.format("请输入全部信息"), "添加结果", JOptionPane.INFORMATION_MESSAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void delete(){
        int row=view.getWarehouseTable().getSelectedRow();
        if(row<0){JOptionPane.showMessageDialog(view, String.format("请选择要删除的记录"), "删除记录", JOptionPane.INFORMATION_MESSAGE);}
        if(row>=0) {
            try {
                String grainCode = (String) view.getDefaultTableModel().getValueAt(row, 0);
                int confirm = JOptionPane.showConfirmDialog(view, "确定要删除这条记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                if (confirm==JOptionPane.YES_OPTION){
                    List<WareHouse> wareHouses = iWarehouseInfService.delete1(grainCode);
                    DefaultTableModel model = view.getDefaultTableModel();
                    model.setRowCount(0);
                    for (WareHouse wareHouse : wareHouses) {
                        model.addRow(new Object[]{
                                wareHouse.getWarehouseCode(),
                                wareHouse.getWarehouseName(),
                                wareHouse.getSnowtime(),
                                wareHouse.getTemper(),
                                wareHouse.getHumidity(),
                                wareHouse.getProblem(),
                                wareHouse.getDeal(),
                                wareHouse.getSpectionpeople(),
                                wareHouse.getSpectiontime()
                        });
                    }}
            }catch (Exception e){
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "删除数据失败");
            }
        }
    }
    public void handleproduce(){
        int row=view.getWarehouseTable().getSelectedRow();
        if(row>=0){
            List<Object>content=new ArrayList<>();
            JTable table=view.getWarehouseTable();
        productionreportView.setVisible(true);
        for (int i=0;i<view.getWarehouseTable().getColumnCount();i++){
            content.add(table.getValueAt(row,i));
        }
        WareHouse wareHouse=new WareHouse();
            wareHouse.setWarehouseCode((String) content.get(0));
            wareHouse.setWarehouseName((String) content.get(1));
            wareHouse.setSnowtime((String) content.get(2));
            wareHouse.setTemper((String) content.get(3));
            wareHouse.setHumidity((String) content.get(4));
            wareHouse.setProblem((String) content.get(5));
            wareHouse.setDeal((String) content.get(6));
            wareHouse.setSpectionpeople((String) content.get(7));
            wareHouse.setSpectiontime((String)content.get(8));
            JTextArea jTextArea=productionreportView.getTextArea();
            jTextArea.setText(wareHouse.getSpectionpeople()+"于"+wareHouse.getSpectiontime()+"\n"+"在"+wareHouse.getWarehouseName()+
            "(编号："+wareHouse.getWarehouseCode()+")"+"得到如下巡库报告:\n"+"风雪时间："+wareHouse.getSnowtime()+"\n"+"温度："+wareHouse.getTemper()+"\n"+
                    "湿度："+wareHouse.getHumidity()+"\n"+"问题："+wareHouse.getProblem()+"\n"+"解决措施："+wareHouse.getDeal());
        }
    }
    private void handlebutton(){
        productionreportView.setVisible(false);
        trackproblemView.setVisible(false);
    }
    public void handletrack() throws DAOException {
        int row=view.getWarehouseTable().getSelectedRow();
        if(row>=0){
        trackproblemView.setVisible(true);
            List<Object>content=new ArrayList<>();
            JTable table=view.getWarehouseTable();
            String code=table.getValueAt(row,0).toString();
            WareHouse wareHouse=iWarehouseInfService.findBycode(code);
            if(wareHouse.getProblem2()==null){wareHouse.setProblem2("无");}
            if(wareHouse.getDeal2()==null){wareHouse.setDeal2("无");}
            if(wareHouse.getProblem3()==null){wareHouse.setProblem3("无");}
            if(wareHouse.getDeal3()==null){wareHouse.setDeal3("无");}
            JTextArea textAreanorth=trackproblemView.getTextAreanorth();
            textAreanorth.setText("问题记录："+wareHouse.getProblem());
            JTextArea textAreamiddle1=trackproblemView.getTextmiddle1();
            textAreamiddle1.setText("问题初发现\n"+"日期:"+wareHouse.getSpectiontime()+"\n"+"处理措施:"+wareHouse.getDeal());
            JTextArea textAreamiddle2=trackproblemView.getTextmiddle2();
            textAreamiddle2.setText("问题处理中\n"+"日期:"+wareHouse.getProblem2()+"\n"+"处理措施:"+wareHouse.getDeal2());
            JTextArea textAreamiddle3=trackproblemView.getTextmiddle3();
            textAreamiddle3.setText("问题处理完成\n"+"日期:"+wareHouse.getProblem3()+"\n"+"处理措施:"+wareHouse.getDeal3());
        }
    }
    private void notice(){
        int row=view.getWarehouseTable().getSelectedRow();
        try {
            if(row<0){JOptionPane.showMessageDialog(view, String.format("请选择要预警的记录"), "预警记录", JOptionPane.INFORMATION_MESSAGE);}
            else {
                String code=view.getWarehouseTable().getValueAt(row,0).toString();
                int confirm = JOptionPane.showConfirmDialog(view, "确定要预警这条记录吗？", "确认预警", JOptionPane.YES_NO_OPTION);
                if(confirm==JOptionPane.YES_OPTION){
                    iWarehouseInfService.notice(code);
                    JOptionPane.showMessageDialog(view, String.format("预警成功"), "预警记录", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private void handledeal(){
        int row=view.getWarehouseTable().getSelectedRow();
        JTable table=view.getWarehouseTable();
        String choose;
        String deal;
        String code=table.getValueAt(row,0).toString();
    try {
        String problems=trackproblemView.getjComboBox().getSelectedItem().toString();
        if (problems.equals("处理中"));{
         choose="warehouse_problem2";
         deal="warehouse_deal2";
        }
        if (problems.equals("处理完成")){
            choose="warehouse_problem3";
            deal="warehouse_deal3";
        }
        DateTimeFormatter dtf3 = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime ldt2 = LocalDateTime.now();
        String str2 = ldt2.format(dtf3);
        String problem2=str2;
        String content=trackproblemView.getjTextField();
        if(content.equals("")){JOptionPane.showMessageDialog(trackproblemView, "请输入处理措施");return;}
        iWarehouseInfService.set2(code,problem2,deal,choose,content);
        JOptionPane.showMessageDialog(trackproblemView, "记录成功");
        handletrack();
    }catch (Exception e){
        e.printStackTrace();
    }
    }
    public JPanel getView(){
        return view;
    }
    public void initializeListeners(){
        view.getSearchButton().addActionListener(e -> search());
        view.getAddButton().addActionListener(e -> add());
        view.getDeleteButton().addActionListener(e -> delete());
        view.getUploadButton().addActionListener(e -> handleproduce());
        view.getZhui().addActionListener(e -> {
            try {
                handletrack();
            } catch (DAOException ex) {
                throw new RuntimeException(ex);
            }
        });
        productionreportView.getConfirm().addActionListener(e ->handlebutton() );
        productionreportView.getNo1().addActionListener(e -> handlebutton());
        trackproblemView.getConfirm().addActionListener(e -> handledeal());
        trackproblemView.getNo2().addActionListener(e -> handlebutton());
        view.getNotice().addActionListener(e -> notice());
    }
}
