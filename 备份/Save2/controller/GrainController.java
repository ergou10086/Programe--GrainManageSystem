package controller;

import java.io.File;
import java.io.IOException;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.util.List;
import java.util.UUID;

import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;

import entity.Grain;
import exceptions.GetHistoryExceptions;
import exceptions.GrainOrWarehouseServiceExceptions;
import service.IGrainChangeHistoryService;
import service.IGrainInfService;
import service.impl.GrainChangeHistoryServiceImpl;
import util.LogUtil;
import view.GrainInfoView;
import service.impl.GrainInfServiceImpl;
import util.IOFileHelper;
import view.GrainInfoView;

// 粮食信息管理控制器类，负责与视图交互，处理用户操作，调用服务层方法
public class GrainController {
    // 声明视图和服务对象属性
    private final GrainInfoView view;
    private final IGrainInfService grainService;
    private String currentUser;
    private String userRole;
    private final IGrainChangeHistoryService historyService;

    // 构造函数，初始化视图和服务对象
    public GrainController(String username, String role) {
        this.currentUser = username;
        this.userRole = role;
        this.historyService = new GrainChangeHistoryServiceImpl();
        this.grainService = new GrainInfServiceImpl();
        // 创建视图并设置控制器
        this.view = new GrainInfoView(username, role);
        this.view.setController(this);
        initializeListeners();
        loadGrainData();
    }

    // 添加获取视图的方法
    public JPanel getView() {
        return view;
    }

    // 初始化监听器
    private void initializeListeners() {
        // 添加按钮监听器
        view.getBtnAdd().addActionListener(e -> handleAddGrain());
        view.getBtnUpdate().addActionListener(e -> handleUpdateGrain());
        view.getBtnDelete().addActionListener(e -> handleDeleteGrain());
        view.getBtnClear().addActionListener(e -> view.clearFields());
        view.getBtnImport().addActionListener(e -> handleImportGrain());
        view.getBtnExport().addActionListener(e -> handleExportGrain());
        view.getBtnCheck().addActionListener(e -> handleInquireGrain());

        // 表格选择监听器
        view.getGrainTable().getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                handleTableSelection();
            }
        });
    }


    // loadGrainData加载粮食信息的带调试
    public void loadGrainData() {
        try {
            System.out.println("开始加载数据...");
            // 调用服务层的方法获取所有粮食信息，返回一个Grain对象的列表
            List<Grain> grains = grainService.getAllGrains();
            System.out.println("获取到 " + grains.size() + " 条数据");
            // 获取视图中对应的表格数据模型，用于后续操作表格数据
            DefaultTableModel model = view.getTableModel();
            // 先清空表格中的现有数据，为重新加载新数据做准备
            model.setRowCount(0);

            // 遍历每一个获取到的Grain对象
            for (Grain grain : grains) {
                model.addRow(new Object[]{
                        grain.getGrainCode(),
                        grain.getGrainName(),
                        grain.getGrainType(),
                        grain.getGrainPrice(),
                        grain.getGrainShelfLife(),
                        grain.getGrainRemark()
                });
                System.out.println("添加数据行：" + grain.getGrainCode());
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



    // 处理添加粮食
    private void handleAddGrain() {
        try {
            // 从输入中获取Grain对象
            Grain grain = getGrainFromInput();
            // 不空且合法
            if(grain != null && validateInput(grain)) {
                // 生成UUID作为id_grain，因为是数据库主键
                String idGrain = UUID.randomUUID().toString();
                grain.setIdGrain(idGrain);

                // 先添加粮食数据
                boolean addSuccess = grainService.addGrain(grain);

                if (addSuccess) {
                    // 确保粮食数据已经添加成功后，再查询一次确认数据存在
                    Grain addedGrain = grainService.findGrainByCode(grain.getGrainCode());
                    if (addedGrain != null) {
                        try {
                            // 使用查询到的grain的idGrain记录历史
                            historyService.recordGrainChange(
                                    addedGrain.getIdGrain(),  // 使用查询到的idGrain
                                    "添加",
                                    String.format("添加新粮食: %s, 编号: %s, 类型: %s, 价格: %.2f",
                                            addedGrain.getGrainName(),
                                            addedGrain.getGrainCode(),
                                            addedGrain.getGrainType(),
                                            addedGrain.getGrainPrice()),
                                    currentUser
                            );

                            JOptionPane.showMessageDialog(view, "添加成功！");
                            // 清空用户输入的相关字段，方便下次输入
                            view.clearFields();
                            // 刷新界面展示的粮食列表
                            loadGrainData();
                        } catch (Exception ex) {
                            // 如果记录历史出现异常，在控制台输出错误信息
                            System.err.println("记录历史失败: " + ex.getMessage());
                            ex.printStackTrace();
                            // 历史记录失败不影响主流程，仍然提示添加成功
                            JOptionPane.showMessageDialog(view, "添加成功，但历史记录失败！");
                            view.clearFields();
                            loadGrainData();
                            System.err.println("记录历史失败: " + ex.getMessage());
                            throw new GetHistoryExceptions.GetHistoryFailedException(ex.getMessage());
                        }
                    } else {
                        JOptionPane.showMessageDialog(view, "添加成功，但无法获取新添加的记录！");
                        view.clearFields();
                        loadGrainData();
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "添加失败！");
                }
            }
        } catch (Exception e) {
            System.err.println("添加失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "添加失败：" + e.getMessage());
            try {
                throw new GrainOrWarehouseServiceExceptions.AddGrainFailedException(e.getMessage());
            } catch (GrainOrWarehouseServiceExceptions.AddGrainFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }



    // 处理更新粮食
    private void handleUpdateGrain() {
        try{
            // 获取用户输入的Grain对象
            Grain grain = getGrainFromInput();
            // 验证操作
            if (grain != null && validateInput(grain)) {
                // 先获取原有的grain记录，以获取id_grain
                Grain existingGrain = grainService.findGrainByCode(grain.getGrainCode());
                // 原有的grain记录不空，则设置
                if (existingGrain != null) {
                    // 设置id_grain
                    grain.setIdGrain(existingGrain.getIdGrain());

                    // 先更新粮食数据
                    if (grainService.updateGrain(grain)) {
                        try {
                            // 成功更新后再记录历史
                            historyService.recordGrainChange(
                                    existingGrain.getIdGrain(),
                                    "修改",
                                    String.format("修改粮食信息: %s, 编号: %s, 类型: %s, 价格: %.2f",
                                            grain.getGrainName(),
                                            grain.getGrainCode(),
                                            grain.getGrainType(),
                                            grain.getGrainPrice()),
                                    currentUser
                            );

                            JOptionPane.showMessageDialog(view, "更新成功！");
                            // 重新加载表格数据，使表格显示最新状态
                            loadGrainData();
                        } catch (Exception ex) {
                            System.err.println("记录历史失败: " + ex.getMessage());
                            ex.printStackTrace();
                            throw new GetHistoryExceptions.GetHistoryFailedException(ex.getMessage());
                        }
                    }
                }else{
                    JOptionPane.showMessageDialog(view, "未找到要更新的粮食信息！");
                }
            }
        }catch (Exception e) {
            System.err.println("更新失败: " + e.getMessage());
            e.printStackTrace();
            JOptionPane.showMessageDialog(view, "更新失败：" + e.getMessage());
            try {
                throw new GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException(e.getMessage());
            } catch (GrainOrWarehouseServiceExceptions.UpdateWarehouseFailedException ex) {
                throw new RuntimeException(ex);
            }
        }
    }




    // 处理删除粮食
    private void handleDeleteGrain() {
        int row = view.getGrainTable().getSelectedRow();
        if(row >= 0) {
            // 获取 grainCode作为删除依据
            String grainCode = (String) view.getTableModel().getValueAt(row, 0);

            try {
                // 先获取要删除的grain记录
                Grain existingGrain = grainService.findGrainByCode(grainCode);
                if (existingGrain != null) {
                    // 确认框的提示并阻塞
                    int confirm = JOptionPane.showConfirmDialog(view, "确定要删除这条记录吗？", "确认删除", JOptionPane.YES_NO_OPTION);
                    if(confirm == JOptionPane.YES_OPTION) {
                        // 先记录历史（因为删除后可能就找不到记录了）
                        try {
                            historyService.recordGrainChange(
                                    existingGrain.getIdGrain(),
                                    "删除",
                                    String.format("删除粮食: %s, 编号: %s",
                                            existingGrain.getGrainName(),
                                            existingGrain.getGrainCode()),
                                    currentUser
                            );

                            // 然后执行删除操作
                            if (grainService.deleteGrain(grainCode)) {
                                JOptionPane.showMessageDialog(view, "删除成功！");
                                loadGrainData();
                            }
                        } catch (Exception ex) {
                            System.err.println("记录历史失败: " + ex.getMessage());
                            ex.printStackTrace();
                            throw new GetHistoryExceptions.GetHistoryFailedException(ex.getMessage());
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(view, "未找到要删除的粮食信息！");
                }
            } catch (Exception e) {
                System.err.println("删除失败: " + e.getMessage());
                e.printStackTrace();
                JOptionPane.showMessageDialog(view, "删除失败：" + e.getMessage());
                try {
                    throw new GrainOrWarehouseServiceExceptions.DeleteGrainFailedException(e.getMessage());
                } catch (GrainOrWarehouseServiceExceptions.DeleteGrainFailedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }




    // 处理导入粮食信息
    private void handleImportGrain() {
        // 创建一个文件选择器对象，用于让用户选择要导入的文件
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择导入文件");

        // 添加文件过滤器
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            // 确定文件选择器是否应该显示某个文件或目录。
            public boolean accept(File f) {
                // 文件f是一个xlsx或者txt，返回true，应该被显示。
                return f.isDirectory() || f.getName().toLowerCase().endsWith(".xlsx")
                        || f.getName().toLowerCase().endsWith(".txt");
            }
            // 提供文件过滤器的描述
            public String getDescription() {
                return "Excel文件(*.xlsx)或文本文件(*.txt)";
            }
        });

        if (fileChooser.showOpenDialog(view) == JFileChooser.APPROVE_OPTION) {
            try{
                // 获取用户选择的文件路径
                File file = fileChooser.getSelectedFile();
                // 导入得到的粮食信息列表
                List<Grain> importedGrains;
                // 根据文件扩展名选择导入方法
                String fileType = file.getName().toLowerCase().endsWith(".xlsx") ? "Excel" : "TXT";

                // 根据文件扩展名选择导入方法
                if (fileType.equals("Excel")) {
                    try {
                        importedGrains = IOFileHelper.importFromExcel(file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else{
                    try {
                        importedGrains = IOFileHelper.importFromTxt(file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                // 记录不同状态信息的条数
                int successCount = 0;
                int skipCount = 0;
                int errorCount = 0;
                // 错误信息串
                StringBuilder errorMsg = new StringBuilder();
                // 导入信息到数据库
                for (Grain grain : importedGrains) {
                    try {
                        // 检查是否已经存在相同的粮食信息
                        Grain existingGrain = grainService.findGrainByCode(grain.getGrainCode());

                        if (existingGrain != null) {
                            // 如果记录存在，检查是否被软删除
                            boolean isDeleted = grainService.isGrainDeletedCheck(existingGrain.getGrainName());

                            if (isDeleted) {
                                // 如果被软删除，询问是否恢复
                                int choice = JOptionPane.showConfirmDialog(view,
                                    String.format("粮食编号 %s (名称: %s) 被暂时删除，是否恢复？",
                                        grain.getGrainCode(), grain.getGrainName()),
                                    "确认恢复",
                                    JOptionPane.YES_NO_OPTION);

                                if (choice == JOptionPane.YES_OPTION) {
                                    // 恢复记录
                                    grainService.restoreGrainByName(existingGrain.getGrainName());
                                    // 记录恢复操作
                                    historyService.recordGrainChange(
                                        existingGrain.getIdGrain(),
                                        "导入恢复",
                                        String.format("导入恢复粮食: %s (编号: %s)",
                                            existingGrain.getGrainName(),
                                            existingGrain.getGrainCode()),
                                        currentUser
                                    );
                                    successCount++;
                                } else {
                                    skipCount++;
                                }
                            } else {
                                // 如果未被删除，询问是否更新
                                int choice = JOptionPane.showConfirmDialog(view,
                                    String.format("粮食编号 %s (名称: %s) 已存在，是否更新？",
                                        grain.getGrainCode(), grain.getGrainName()),
                                    "确认更新",
                                    JOptionPane.YES_NO_OPTION);

                                if (choice == JOptionPane.YES_OPTION) {
                                    // 设置id_grain以更新正确的记录
                                    grain.setIdGrain(existingGrain.getIdGrain());
                                    boolean updateSuccess = grainService.updateGrain(grain);
                                    if (updateSuccess) {
                                        try {
                                            // 更新成功后再记录历史
                                            historyService.recordGrainChange(
                                                grain.getIdGrain(),
                                                "导入更新",
                                                String.format("更新粮食信息: %s (编号: %s)",
                                                    grain.getGrainName(),
                                                    grain.getGrainCode()),
                                                currentUser
                                            );
                                            successCount++;
                                        } catch (Exception historyEx) {
                                            System.err.println("记录历史失败: " + historyEx.getMessage());
                                            // 更新成功但记录历史失败，仍然计入成功
                                            successCount++;
                                        }
                                    }
                                } else {
                                    skipCount++;
                                }
                            }
                        } else {
                            // 如果不存在，则添加新记录
                            String idGrain = UUID.randomUUID().toString();
                            grain.setIdGrain(idGrain);
                            boolean addSuccess = grainService.addGrain(grain);
                            if (addSuccess) {
                                try {
                                    // 添加成功后再记录历史
                                    historyService.recordGrainChange(
                                        idGrain,
                                        "导入新增",
                                        String.format("导入新粮食: %s (编号: %s)",
                                            grain.getGrainName(),
                                            grain.getGrainCode()),
                                        currentUser
                                    );
                                    successCount++;
                                } catch (Exception historyEx) {
                                    System.err.println("记录历史失败: " + historyEx.getMessage());
                                    // 添加成功但记录历史失败，仍然计入成功
                                    successCount++;
                                }
                            }
                        }
                        // 在每次成功导入后记录日志
                        if (successCount > 0) {
                            LogUtil.recordLog(
                                    currentUser,
                                    "导入粮食信息",
                                    String.format("从%s文件导入粮食信息：成功%d条，跳过%d条，失败%d条",
                                            fileType,
                                            successCount,
                                            skipCount,
                                            errorCount)
                            );
                        }
                    } catch (Exception e) {
                        errorCount++;
                        String errorMessage;
                        if (e instanceof GrainOrWarehouseServiceExceptions.AddGrainFailedException) {
                            errorMessage = String.format("导入记录失败 (编号: %s): 粮食编号已存在",
                                grain.getGrainCode());
                        } else {
                            errorMessage = String.format("导入记录失败 (编号: %s): %s",
                                grain.getGrainCode(),
                                e.getMessage());
                        }
                        errorMsg.append(errorMessage).append("\n");
                        System.err.println(errorMessage);
                        e.printStackTrace();
                        try {
                            throw new GrainOrWarehouseServiceExceptions.ImportGrainFailedException(e.getMessage());
                        } catch (GrainOrWarehouseServiceExceptions.ImportGrainFailedException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }

                // 记录导入结果的日志
                LogUtil.recordLog(currentUser, "从%s导入粮食信息",
                        String.format("导入结果：成功 %d 条，跳过 %d 条，失败 %d 条",
                                file.getName(), successCount, skipCount, errorCount));

                // 显示导入结果
                String resultMessage = String.format(
                    "导入完成：\n成功：%d 条\n跳过：%d 条\n失败：%d 条\n%s",
                    successCount,
                    skipCount,
                    errorCount,
                    errorCount > 0 ? "\n错误详情：\n" + errorMsg.toString() : ""
                );

                JOptionPane.showMessageDialog(view, resultMessage, "导入结果", errorCount > 0 ? JOptionPane.WARNING_MESSAGE : JOptionPane.INFORMATION_MESSAGE);
                loadGrainData(); // 刷新表格
            }catch (RuntimeException e) {
                //LogUtil.recordLog(currentUser, "导入失败", "导入失败：" + e.getMessage());
                JOptionPane.showMessageDialog(view, "导入失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                try {
                    throw new GrainOrWarehouseServiceExceptions.ImportGrainFailedException(e.getMessage());
                } catch (GrainOrWarehouseServiceExceptions.ImportGrainFailedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }



    // 处理导出粮食信息
    private void handleExportGrain() {
        // 创建一个文件选择器对象，用于让用户选择保存导出文件的位置
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("选择保存位置");

        // 添加文件类型选择
        String[] options = {"Excel文件(*.xlsx)", "文本文件(*.txt)"};
        String fileType = (String) JOptionPane.showInputDialog(view,
                "请选择导出文件格式：",
                "选择格式",
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);

        if (fileType == null) return;

        // 设置文件扩展名
        String extension = fileType.contains("Excel") ? ".xlsx" : ".txt";
        fileChooser.setSelectedFile(new File("粮食信息" + extension));

        if (fileChooser.showSaveDialog(view) == JFileChooser.APPROVE_OPTION) {
            try {
                // 获取用户选择的文件路径
                String filePath = fileChooser.getSelectedFile().getPath();
                // 如果用户没有选择文件扩展名，则自动添加
                if (!filePath.toLowerCase().endsWith(extension)) {
                    filePath += extension;
                }

                // 获取所有粮食数据
                List<Grain> grains = grainService.getAllGrains();

                // 根据选择的格式导出
                if (extension.equals(".xlsx")) {
                    IOFileHelper.exportToExcel(grains, filePath);
                } else {
                    IOFileHelper.exportToTxt(grains, filePath);
                }

                // 记录导出操作到日志表
                LogUtil.recordLog(
                        currentUser,
                        "导出粮食信息",
                        String.format("导出%d条粮食信息到%s文件: %s",
                                grains.size(),
                                extension.equals(".xlsx") ? "Excel" : "TXT",
                                filePath)
                );

                JOptionPane.showMessageDialog(view, "导出成功！");
            } catch (Exception e) {
                // LogUtil.recordLog(currentUser, "导出失败", "导出失败：" + e.getMessage());
                JOptionPane.showMessageDialog(view, "导出失败：" + e.getMessage(), "错误", JOptionPane.ERROR_MESSAGE);
                try {
                    throw new GrainOrWarehouseServiceExceptions.ExportGrainFailedException(e.getMessage());
                } catch (GrainOrWarehouseServiceExceptions.ExportGrainFailedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }
    }



    // 处理搜索粮食
    private void handleInquireGrain() {
        try{
            // 获取搜索关键字
            String keyword = JOptionPane.showInputDialog(
                    view,
                    "请输入要搜索的粮食编号、名称或类型：\n(留空显示所有记录)",
                    "搜索粮食",
                    JOptionPane.QUESTION_MESSAGE
            );
            List<Grain> grains;

            // 用户点击取消按钮
            if (keyword == null) {
                return;
            }

            if(keyword.trim().isEmpty()){
                // 如果搜索框为空，显示所有粮食信息
                grains = grainService.getAllGrains();
            }else{
                grains = grainService.searchGrains(keyword.trim());
            }

            // 更新表格
            DefaultTableModel model = view.getTableModel();
            model.setRowCount(0); // 清空表格

            if(grains.isEmpty()){
                JOptionPane.showMessageDialog(view, "未找到匹配的粮食信息", "搜索结果", JOptionPane.INFORMATION_MESSAGE);
            }else{
                // 添加搜索结果
                for (Grain grain : grains) {
                    model.addRow(new Object[]{
                            grain.getGrainCode(),
                            grain.getGrainName(),
                            grain.getGrainType(),
                            grain.getGrainPrice(),
                            grain.getGrainShelfLife(),
                            grain.getGrainRemark()
                    });
                }
                // 显示搜索结果
                JOptionPane.showMessageDialog(view, String.format("找到 %d 条匹配的记录", grains.size()), "搜索结果", JOptionPane.INFORMATION_MESSAGE);
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



    // 处理表格选择
    private void handleTableSelection() {
        // 获取表格中当前选中行的索引
        int row = view.getGrainTable().getSelectedRow();
        if(row >= 0){
            // 获取表格对应的数据模型
            DefaultTableModel model = view.getTableModel();
            // 将选中的每一列设置
            view.getTxtGrainCode().setText((String) model.getValueAt(row, 0));    // 粮食编号
            view.getTxtGrainName().setText((String) model.getValueAt(row, 1));    // 粮食名称
            view.getTxtGrainType().setText((String) model.getValueAt(row, 2));    // 粮食类型
            view.getTxtPrice().setText(String.valueOf(model.getValueAt(row, 3)));    // 粮食每斤单价
            view.getTxtShelfLife().setText(String.valueOf(model.getValueAt(row, 4)));   // 粮食保质期
            view.getTxtRemark().setText((String) model.getValueAt(row, 5));      // 粮食备注
        }
    }




    // 从输入框获取粮食信息
    private Grain getGrainFromInput() {
        try {
            // 获取输入的文本，去除首位空格
            String code = view.getTxtGrainCode().getText().trim();  // 粮食编号
            String name = view.getTxtGrainName().getText().trim();   // 粮食名称
            String type = view.getTxtGrainType().getText().trim();   // 粮食类型
            double price = Double.parseDouble(view.getTxtPrice().getText().trim());  // 粮食价格
            double shelfLife = Double.parseDouble(view.getTxtShelfLife().getText().trim());   // 粮食保质期
            String remark = view.getTxtRemark().getText().trim();    // 粮食备注

            return new Grain(code, name, type, price, shelfLife, remark);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "请输入有效的数字！");
            return null;
        }
    }



    // 验证输入
    private boolean validateInput(Grain grain) {
        // 字段填写完整度验证
        if (grain.getGrainCode() == null || grain.getGrainCode().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "粮食编号不能为空！");
            return false;
        }

        if (grain.getGrainName() == null || grain.getGrainName().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "粮食名称不能为空！");
            return false;
        }

        if (grain.getGrainType() == null || grain.getGrainType().trim().isEmpty()) {
            JOptionPane.showMessageDialog(view, "粮食类型不能为空！");
            return false;
        }

        // 价格和保质期正确性验证
        if(grain.getGrainPrice() <= 0) {
            JOptionPane.showMessageDialog(view, "请填写正确的价格！");
            return false;
        }

        if(grain.getGrainShelfLife() <= 0) {
            JOptionPane.showMessageDialog(view, "请填写正确的保质期！");
            return false;
        }

        return true;
    }
}