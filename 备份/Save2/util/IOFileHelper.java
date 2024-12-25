package util;

import entity.Grain;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import util.LogUtil;

import java.io.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

// IO流文件操作工具类，在这里完成对文件的操作方法的实现
public class IOFileHelper {

    // Excel粮食信息的导出
    public static void exportToExcel(List<Grain> grains, String filePath) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建一个新的XSSFWorkbook对象,为excel表格
            Sheet sheet = workbook.createSheet("粮食信息");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] columns = {"编号", "名称", "种类", "单价", "保质期", "备注"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // 写入数据
            int rowNum = 1;
            for (Grain grain : grains) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(grain.getGrainCode());
                row.createCell(1).setCellValue(grain.getGrainName());
                row.createCell(2).setCellValue(grain.getGrainType());
                row.createCell(3).setCellValue(grain.getGrainPrice());
                row.createCell(4).setCellValue(grain.getGrainShelfLife());
                row.createCell(5).setCellValue(grain.getGrainRemark());
            }

            // 自动调整列宽
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }


    // TXT粮食信息的导出
    public static void exportToTxt(List<Grain> grains, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 写入标题行
            writer.write("编号\t名称\t种类\t单价\t保质期\t备注\n");

            // 写入数据
            for (Grain grain : grains) {
                writer.write(String.format("%s\t%s\t%s\t%.2f\t%.2f\t%s\n",
                        grain.getGrainCode(),
                        grain.getGrainName(),
                        grain.getGrainType(),
                        grain.getGrainPrice(),
                        grain.getGrainShelfLife(),
                        grain.getGrainRemark()));
            }
        }
    }



    // Excel粮食信息的导入
    public static List<Grain> importFromExcel(String filePath) throws IOException {
        List<Grain> grains = new ArrayList<>();

        try (Workbook workbook = WorkbookFactory.create(new File(filePath))) {
            Sheet sheet = workbook.getSheetAt(0);

            // 从第二行开始读取（跳过标题行）
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row != null) {
                    Grain grain = new Grain();
                    grain.setGrainCode(getStringCellValue(row.getCell(0)));
                    grain.setGrainName(getStringCellValue(row.getCell(1)));
                    grain.setGrainType(getStringCellValue(row.getCell(2)));
                    grain.setGrainPrice(getNumericCellValue(row.getCell(3)));
                    grain.setGrainShelfLife(getNumericCellValue(row.getCell(4)));
                    grain.setGrainRemark(getStringCellValue(row.getCell(5)));
                    grains.add(grain);
                }
            }
        }
        return grains;
    }



    // TXT粮食信息的导入
    public static List<Grain> importFromTxt(String filePath) throws IOException {
        List<Grain> grains = new ArrayList<>();

        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))){
            // 跳过标题行
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t");
                if (parts.length >= 6) {
                    Grain grain = new Grain();
                    grain.setGrainCode(parts[0]);
                    grain.setGrainName(parts[1]);
                    grain.setGrainType(parts[2]);
                    grain.setGrainPrice(Double.parseDouble(parts[3]));
                    grain.setGrainShelfLife(Double.parseDouble(parts[4]));
                    grain.setGrainRemark(parts[5]);
                    grains.add(grain);
                }
            }
        }
        return grains;
    }



    // 辅助方法：获取单元格字符串值
    private static String getStringCellValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue();
            case NUMERIC: return String.valueOf((int)cell.getNumericCellValue());
            default: return "";
        }
    }

    // 辅助方法：获取单元格数值
    private static double getNumericCellValue(Cell cell) {
        if (cell == null) return 0.0;
        switch (cell.getCellType()) {
            case NUMERIC: return cell.getNumericCellValue();
            case STRING:
                try {
                    return Double.parseDouble(cell.getStringCellValue());
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            default: return 0.0;
        }
    }


    // 日志导出到Excel
    public static void exportLogsToExcel(String filePath) throws SQLException, IOException {
        List<Map<String, Object>> logs = LogUtil.getLogsByTimeRange(
                "1970-01-01 00:00:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("系统日志");

            // 创建标题行
            Row headerRow = sheet.createRow(0);
            String[] columns = {"日志ID", "时间", "用户", "操作", "详情"};
            for (int i = 0; i < columns.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(columns[i]);
            }

            // 写入数据
            int rowNum = 1;
            for (Map<String, Object> log : logs) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(((Number) log.get("log_id")).intValue());
                row.createCell(1).setCellValue(log.get("log_time").toString());
                row.createCell(2).setCellValue((String) log.get("username"));
                row.createCell(3).setCellValue((String) log.get("operation"));
                row.createCell(4).setCellValue((String) log.get("detail"));
            }

            // 自动调整列宽
            for (int i = 0; i < columns.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入文件
            try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
                workbook.write(fileOut);
            }
        }
    }


    // 日志导出到Txt
    public static void exportLogsToTxt(String filePath) throws SQLException, IOException {
        List<Map<String, Object>> logs = LogUtil.getLogsByTimeRange(
                "1970-01-01 00:00:00",
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date())
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // 写入标题行
            writer.write("日志ID\t时间\t用户\t操作\t详情\n");

            // 写入数据行
            for (Map<String, Object> log : logs) {
                writer.write(String.format("%d\t%s\t%s\t%s\t%s\n",
                        ((Number) log.get("log_id")).intValue(),
                        log.get("log_time").toString(),
                        log.get("username"),
                        log.get("operation"),
                        log.get("detail")
                ));
            }
        }
    }
}
