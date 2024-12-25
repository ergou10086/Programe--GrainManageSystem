package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import exceptions.DAOException;
import exceptions.DataBaseExceptions;

// 数据库操作的工具类，提供了连接数据库、执行更新和查询操作的方法
public class DatabaseHelper {
    // 数据库连接URL，指定了连接的MySQL主机地址、端口、数据库名称等连接参数，如不使用SSL且设置时区为UTC
    private static final String URL = "jdbc:mysql://47.94.74.232:3306/graininfdb?useSSL=false&serverTimezone=UTC";
    // 用户名，根据项目成员的配置情况进行修改
    private static final String USER = "root";
    // 密码，根据项目成员的配置情况进行修改
    private static final String PASSWORD = "zjm10086";

    // 静态代码块，用于加载MySQL的JDBC驱动程序
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            try {
                throw new DataBaseExceptions.DriverLoadFailedException("无法加载MySQL JDBC驱动程序");
            } catch (DataBaseExceptions.DriverLoadFailedException ex) {
                try {
                    throw new SQLException(ex);
                } catch (SQLException exc) {
                    throw new RuntimeException(exc);
                }
            }
        }
    }


    // 获取数据库连接的方法，返回一个Connection对象，用于与数据库建立连接
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    // 关闭数据库连接的方法，用于释放数据库连接资源
    public static void closeConnection(Connection conn) throws SQLException {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException exc) {
            throw new DataBaseExceptions.CloseFailedException("无法关闭数据库连接");
        }
    }


    // 通用的更新的查询方法，用于执行数据库的增删改操作（如INSERT、UPDATE、DELETE语句）的方法
    // 参数sql是要执行的SQL语句，params是可变参数，用于传递SQL语句中需要的参数（占位符对应的值）
    public static int executeUpdate(String sql, Object... params) throws SQLException {
        // // 通过getConnection方法获取数据库连接
        try (Connection conn = getConnection();
             // 创建一个预编译的Statement对象
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 将参数设置到pstmt中对应的占位符位置
            setParameters(pstmt, params);
            // 执行成功后返回受影响的行数
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL执行失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    // 通用查询方法，参数意义和上面的一样
    // List<Map<String, Object>>形式表示，每个Map对应一行数据，键为列名，值为对应列的值
    public static List<Map<String, Object>> executeQuery(String sql, Object... params) throws SQLException {
        // 通过getConnection方法获取数据库连接
        try (Connection conn = getConnection();
             // 创建一个预编译的Statement对象，设置参数和执行查询
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // 将传入的参数设置到预编译的Statement对象中对应的占位符位置
            setParameters(pstmt, params);
            // 获取结果集ResultSet对象
            try (ResultSet rs = pstmt.executeQuery()) {
                return resultSetToList(rs);
            }
        } catch (SQLException e) {
            System.err.println("SQL执行失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }


    // 将传入的参数设置到预编译的Statement对象对应的占位符位置上
    private static void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        try {
            for (int i = 0; i < params.length; i++) {
                pstmt.setObject(i + 1, params[i]);
            }
        } catch (SQLException e) {
            throw new DAOException.ParameterSettingException(e.getMessage());
        }
    }


    // 将查询得到的ResultSet结果集转换为List<Map<String, Object>>形式
    // 返回值是一个List，其中每个元素是一个Map，表示一行数据，键为列名，值为对应列的值
    private static List<Map<String, Object>> resultSetToList(ResultSet rs) throws SQLException {
        // 存放转换后结果
        List<Map<String, Object>> list = new ArrayList<>();
        // 结果集的元数据，包含了列的信息
        ResultSetMetaData metaData = rs.getMetaData();
        // 获取结果集中的列数
        int columnCount = metaData.getColumnCount();

        // 遍历结果集的每一行数据
        while (rs.next()) {
            // 存放当前行的数据，键为列名，值为对应列的值
            Map<String, Object> row = new HashMap<>();
            // 遍历当前行的每一列
            for (int i = 1; i <= columnCount; i++) {
                String columnName = metaData.getColumnName(i);
                Object value = rs.getObject(i);
                row.put(columnName, value);
            }
            list.add(row);
        }
        return list;
    }

    // 添加支持事务的执行更新方法
    public static int executeUpdate(Connection conn, String sql, Object... params) throws SQLException {
        // 创建一个预编译的Statement对象，设置参数和执行查询
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, params);
            return pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("SQL执行失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    // 添加支持事务的执行查询方法
    public static List<Map<String, Object>> executeQuery(Connection conn, String sql, Object... params) throws SQLException {
        // 创建一个预编译的Statement对象，设置参数和执行查询
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            setParameters(pstmt, params);
            try (ResultSet rs = pstmt.executeQuery()) {
                return resultSetToList(rs);
            }
        } catch (SQLException e) {
            System.err.println("SQL执行失败: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
//-----------------------------------------------------------------------------
    public static Connection getConnection1() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }
    public static void close(Connection conn) {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<Map<String, Object>> getResult(
            String sql, List<Object> params, List<String> columns) {
        Connection conn = getConnection1();
        List<Map<String, Object>> result = new ArrayList<>();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
            }
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {// 行
                Map<String, Object> row = new HashMap<>();
                for (String column : columns) {// 列
                    row.put(column, rs.getObject(column));
                }
                result.add(row);
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn);
        }
        return result;
    }
    public static void save(String sql, List<Object> params) {
        Connection conn = getConnection1();
        try {
            PreparedStatement ps = conn.prepareStatement(sql);
            if (params != null) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
            }
            ps.executeUpdate();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            close(conn);
        }
    }
}