package controller;
import view.EntryView;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

// 入库管理页面对应的控制器类
public class EntryController {
    private EntryView view;
    private String currentUser;
    private String userRole;
    private int length;

    public EntryController(){
        view = new EntryView(this);
    }
    public EntryController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        this.view = new EntryView(this);
    }
    public JPanel getView(){
        return view;
    }
    public void addData() {
        String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "zjm10086";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            //COUNT 是一个聚合函数，用于对数据进行计数。* 在这里表示统计所有的行，也就是会计算 entry_data 表中所有记录的条数。
            String sql ="SELECT COUNT(*) FROM entry_data";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                length = resultSet.getInt(1);
            }
            resultSet.close();
            statement.close();
            connection.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 从视图获取数据
            String sql = "INSERT INTO entry_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            // 从视图中获取实际的值
            ps.setString(1, String.valueOf(length+1));
            ps.setString(2, view.getCropNameField());
            ps.setString(3, view.getWareHouse());
            ps.setString(4, view.getGrossWeightField());
            ps.setString(5, view.getTareWeightField());
            ps.setString(6, view.getNetWeightField());
            ps.setString(7, view.getMoistureField());
            ps.setString(8, view.getDriverNameField());
            ps.setString(9, view.getPhoneField());
            ps.setString(10, view.getPlateNumberField());
            ps.setString(11, view.getData());
            int result = ps.executeUpdate();//操作的返回值，根据操作不同返回值不同
            if (result > 0) {
                JOptionPane.showMessageDialog(view, "入库添加成功！");
            } else {
                JOptionPane.showMessageDialog(view, "入库添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(view, "数据库驱动加载失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "数据库操作失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean addWarehouse(){
        String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "zjm10086";
        Connection conn = null;
        PreparedStatement ps = null;

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            // 获取入库界面的仓库名称、作物名称和重量
            String warehouseName = view.getWareHouse();
            String cropName = view.getCropNameField();
            double weight = Double.parseDouble(view.getGrossWeightField());//获取毛重，转换为double类型

            // 查询仓库中的作物名称
            //查询表中warehouse_name与view.getWareHouse()相同的一行中的cropname, cropweight
            String sql = "SELECT cropname, cropweight FROM warehouse WHERE warehouse_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, warehouseName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                // 从结果集中获取当前行的作物名称字段值
                String existingCrop = rs.getString("cropname");
                // 从结果集中获取当前行的作物重量字段值
                double existingWeight = rs.getDouble("cropweight");

                // 判断作物名称是否匹配
                if(existingCrop.equals(cropName)) {
                    // 更新仓库重量
                    // 更新warehouse表中的数据
                    String updateSql = "UPDATE warehouse SET cropweight = ? WHERE warehouse_name = ?";
                    ps = conn.prepareStatement(updateSql);
                    //对于updateSql中的第一个问号，existingWeight + weight为更改后的值
                    ps.setDouble(1, existingWeight + weight);
                    //对于updateSql中的第二个问号
                    ps.setString(2, warehouseName);

                    int result = ps.executeUpdate();
                    if(result > 0) {
                    } else {
                        return false;
                    }
                }else if (existingCrop == null || existingCrop.equals("")) {
                    // 更新仓库重量
                    // 更新warehouse表中的数据
                    String updateSql = "UPDATE warehouse SET cropweight = ?, cropname = ? WHERE warehouse_name = ?";
                    ps = conn.prepareStatement(updateSql);
                    //对于updateSql中的第一个问号，weight为更改后的值
                    ps.setDouble(1, weight);
                    //对于updateSql中的第二个问号
                    ps.setString(2, cropName);
                    ps.setString(3, warehouseName);

                    int result = ps.executeUpdate();
                    if(result <= 0) {
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "入库作物与该仓库存储的作物类型不符！", "错误", JOptionPane.ERROR_MESSAGE);
                    return false;
                }
            }

            if(rs != null) rs.close();

        }catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(view, "数据库驱动加载失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "数据库操作失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return true;
    }


    public String[] getWarehouse(){
        String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "zjm10086";
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<String> list = new ArrayList<>();

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 指定结果集类型为可滚动（这里选择了对更新不敏感的可滚动类型，根据实际需求也可选择其他合适类型）
            String sql = "SELECT warehouse_name FROM warehouse";
            //ResultSet.TYPE_SCROLL_INSENSITIVE指定结果集具有可滚动的特性，并且对其他并发事务或操作导致的数据更新不敏感。
            //ResultSet.CONCUR_READ_ONLY表示结果集是只读的，不能通过这个结果集去修改数据库中的数据。
            ps = conn.prepareStatement(sql, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);;
            ResultSet resultSet = ps.executeQuery();

            // 遍历结果集，将warehouse_name添加到ArrayList中
            while (resultSet.next()) {
                list.add(resultSet.getString("warehouse_name"));
            }

        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(view, "数据库驱动加载失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(view, "数据库操作失败：" + e.getMessage(),
                    "错误", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }finally {
            try {
                if (ps != null) ps.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return list.toArray(new String[0]);
    }
}