package controller;
import view.ExitView;
import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;

// 出库管理的控制器类
public class ExitController {
    private ExitView view;
    private String currentUser;
    private String userRole;
    private double total;
    private int length;
    private JTextField totalAmount; // 添加totalAmount字段

    public ExitController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        this.view = new ExitView(this);
    }
    public JPanel getView(){
        return view;
    }
    public void addData(){
        String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
        String USER = "root";
        String PASSWORD = "zjm10086";
        Connection conn = null;
        PreparedStatement ps = null;

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


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            // 从视图获取数据
            String sql = "INSERT INTO exit_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?)";
            ps = conn.prepareStatement(sql);
            // 从视图中获取实际的值
            ps.setString(1, String.valueOf(length+1));
            ps.setString(2, view.getcompanyName());
            ps.setString(3, view.getcontactPerson());
            ps.setString(4, view.getcontactPhone());
            ps.setString(5, view.getWareHouse());
            ps.setString(6, view.getcropName());
            ps.setString(7, view.getweight());
            ps.setString(8, view.getmoisture());
            ps.setString(9, view.getprice());
            ps.setString(10, view.getdriverName());
            ps.setString(11, view.getdriverPhone());
            ps.setString(12, view.getlicensePlate());
            ps.setString(13, view.getData());
            ps.setString(14, view.getTotal());
            // 设置图片数据
            byte[] imageData = view.getImageData();
            if (imageData != null && imageData.length > 0) {
                ps.setBytes(15, imageData);  // 使用setBytes存储图片数据
            } else {
                ps.setNull(15, Types.BLOB);  // 如果没有图片则存储NULL
            }
            int result = ps.executeUpdate();//操作的返回值，根据操作不同返回值不同
            if (result > 0) {
                JOptionPane.showMessageDialog(view, "出库添加成功！");
            } else {
                JOptionPane.showMessageDialog(view, "出库添加失败！", "错误", JOptionPane.ERROR_MESSAGE);
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
            String cropName = view.getcropName();
            double weight = Double.parseDouble(view.getweight());

            // 查询仓库中的作物名称
            String sql = "SELECT cropname, cropweight FROM warehouse WHERE warehouse_name = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, warehouseName);
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String existingCrop = rs.getString("cropname");
                double existingWeight = rs.getDouble("cropweight");

                // 判断作物名称是否匹配
                if(existingCrop.equals(cropName)) {
                    // 判断出库后的库存是否小于0
                    if(existingWeight - weight < 0) {
                        JOptionPane.showMessageDialog(view, "出库重量超过库存量，无法出库！", "错误", JOptionPane.ERROR_MESSAGE);
                        return false;
                    }

                    // 更新仓库重量
                    String updateSql = "UPDATE warehouse SET cropweight = ? WHERE warehouse_name = ?";
                    ps = conn.prepareStatement(updateSql);
                    ps.setDouble(1, existingWeight - weight);
                    ps.setString(2, warehouseName);

                    int result = ps.executeUpdate();
                    if(result > 0) {
                    } else {
                        return false;
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "出库作物与该仓库存储的作物类型不符！", "错误", JOptionPane.ERROR_MESSAGE);
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
            String sql = "SELECT warehouse_name FROM warehouse";
            // 指定结果集类型为可滚动
            //ResultSet.TYPE_SCROLL_INSENSITIVETYPE 表示创建的结果集是可滚动的，
            // 也就是可以在结果集中向前、向后移动游标，随意定位到不同的行，并且该结果集对底层数据库数据的变化不敏感。
            // 例如，即使在获取结果集之后，数据库中对应的数据被其他操作修改了，这个结果集中的数据不会随之改变。
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
        //将一个长度为0的字符串数组传给toArray中
        // toArray方法会根据列表中元素的数量创建一个新的String类型且大小合适的数组
        // 然后将列表中的元素复制到这个新创建的数组中，并返回该数组。
    }
}




