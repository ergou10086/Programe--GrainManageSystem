package grainInfManageTest;

import controller.GrainController;
import entity.Grain;
import org.junit.jupiter.api.*;
import service.impl.GrainInfServiceImpl;
import view.GrainInfoView;
import javax.swing.table.DefaultTableModel;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GrainIntegrationTest {
    private static GrainController controller;
    private static GrainInfServiceImpl service;
    private static GrainInfoView view;

    @BeforeAll
    static void setUp() {
        controller = new GrainController("testUser", "管理员");
        service = new GrainInfServiceImpl();
        view = (GrainInfoView) controller.getView();
    }

    // 测试完整的粮食添加
    @Test
    @Order(1)
    void testAddGrainFlow() {
        // 模拟用户输入
        view.getTxtGrainCode().setText("INT001");
        view.getTxtGrainName().setText("集成测试粮食");
        view.getTxtGrainType().setText("测试类型");
        view.getTxtPrice().setText("3.0");
        view.getTxtShelfLife().setText("12");
        view.getTxtRemark().setText("集成测试备注");

        // 触发添加操作
        view.getBtnAdd().doClick();

        // 验证结果
        DefaultTableModel model = view.getTableModel();
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals("INT001")) {
                found = true;
                assertEquals("集成测试粮食", model.getValueAt(i, 1));
                break;
            }
        }
        assertTrue(found);
    }


    // 测试完整的粮食更新
    @Test
    @Order(2)
    void testUpdateGrainFlow() {
        // 选择要更新的记录
        int rowIndex = -1;
        DefaultTableModel model = view.getTableModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals("INT001")) {
                rowIndex = i;
                break;
            }
        }
        assertTrue(rowIndex >= 0);

        // 选择表格行
        view.getGrainTable().setRowSelectionInterval(rowIndex, rowIndex);

        // 修改价格
        view.getTxtPrice().setText("3.5");

        // 触发更新操作
        view.getBtnUpdate().doClick();

        // 验证更新结果
        Grain updated = service.findGrainByCode("INT001");
        assertNotNull(updated);
        assertEquals(3.5, updated.getGrainPrice());
    }


    // 测试完整的搜索
    @Test
    @Order(3)
    void testSearchGrainFlow() {
        // 触发搜索操作
        view.getBtnCheck().doClick();

        // 在弹出的对话框中输入搜索关键字
        // 注意：这部分在实际测试中可能需要使用UI自动化测试框架

        // 验证搜索结果
        DefaultTableModel model = view.getTableModel();
        boolean found = false;
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals("INT001")) {
                found = true;
                break;
            }
        }
        assertTrue(found);
    }


    // 测试完整的粮食软删除
    @Test
    @Order(4)
    void testDeleteGrainFlow() {
        // 选择要删除的记录
        int rowIndex = -1;
        DefaultTableModel model = view.getTableModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).equals("INT001")) {
                rowIndex = i;
                break;
            }
        }
        assertTrue(rowIndex >= 0);

        // 选择表格行
        view.getGrainTable().setRowSelectionInterval(rowIndex, rowIndex);

        // 触发删除操作
        view.getBtnDelete().doClick();

        // 验证删除结果
        Grain deleted = service.findGrainByCode("INT001");
        assertNull(deleted);
    }
}
