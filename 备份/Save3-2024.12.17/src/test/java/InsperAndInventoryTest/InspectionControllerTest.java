package InsperAndInventoryTest;

import controller.InspectionController;
import org.junit.jupiter.api.*;
import view.InspectionView;
import view.ProductionreportView;
import view.TrackproblemView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InspectionControllerTest {
    private static InspectionController controller;
    private static InspectionView view;
    private static ProductionreportView productionView;
    private static TrackproblemView trackView;

    @BeforeAll
    static void setUp() {
        SwingUtilities.invokeLater(() -> {
            try {
                controller = new InspectionController("testUser", "管理员");

                // 通过反射获取视图实例
                Field viewField = InspectionController.class.getDeclaredField("view");
                Field productionViewField = InspectionController.class.getDeclaredField("productionreportView");
                Field trackViewField = InspectionController.class.getDeclaredField("trackproblemView");

                viewField.setAccessible(true);
                productionViewField.setAccessible(true);
                trackViewField.setAccessible(true);

                view = (InspectionView) viewField.get(controller);
                productionView = (ProductionreportView) productionViewField.get(controller);
                trackView = (TrackproblemView) trackViewField.get(controller);
            } catch (Exception e) {
                fail("Test setup failed: " + e.getMessage());
            }
        });

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nested
    @Order(1)
    class DataOperationTests {
        @Test
        void testLoadData() {
            assertDoesNotThrow(() -> {
                controller.loaddata();
                DefaultTableModel model = view.getDefaultTableModel();
                assertNotNull(model);
            });
        }

        @Test
        void testSearch() {
            assertDoesNotThrow(() -> {
                controller.search();
                DefaultTableModel model = view.getDefaultTableModel();
                assertNotNull(model);
            });
        }

        @Test
        void testAdd() {
            assertDoesNotThrow(() -> {
                setTestInputData("WH001", "测试仓库", "2024-01-20", "25℃",
                        "60%", "无问题", "正常", "张三");
                controller.add();
            });
        }
    }

    @Nested
    @Order(2)
    class TableOperationTests {
        @Test
        void testHandleProduce() {
            assertDoesNotThrow(() -> {
                setupTableSelection(0);
                controller.handleproduce();
                assertTrue(productionView.isVisible());
            });
        }

        @Test
        void testHandleTrack() {
            assertDoesNotThrow(() -> {
                setupTableSelection(0);
                controller.handletrack();
                assertTrue(trackView.isVisible());
            });
        }
    }

    @Nested
    @Order(3)
    class NoticeTests {
        @Test
        void testNotice() {
            assertDoesNotThrow(() -> {
                Method noticeMethod = InspectionController.class.getDeclaredMethod("notice");
                noticeMethod.setAccessible(true);
                setupTableSelection(0);
                noticeMethod.invoke(controller);
            });
        }
    }

    @Nested
    @Order(4)
    class HandleDealTests {
        @Test
        void testHandleDeal() {
            assertDoesNotThrow(() -> {
                Method handleDealMethod = InspectionController.class.getDeclaredMethod("handledeal");
                handleDealMethod.setAccessible(true);
                setupTableSelection(0);
                handleDealMethod.invoke(controller);
            });
        }
    }

    // 辅助方法
    private void setTestInputData(String code, String name, String snowtime,
                                  String temper, String humidity, String problem,
                                  String deal, String inspector) throws Exception {
        Method setCodeMethod = InspectionView.class.getDeclaredMethod("setcode", String.class);
        Method setNameMethod = InspectionView.class.getDeclaredMethod("setname", String.class);
        // ... 其他字段的setter方法

        setCodeMethod.setAccessible(true);
        setNameMethod.setAccessible(true);
        // ... 设置其他字段的访问权限

        setCodeMethod.invoke(view, code);
        setNameMethod.invoke(view, name);
        // ... 设置其他字段的值
    }

    private void setupTableSelection(int row) {
        SwingUtilities.invokeLater(() -> {
            JTable table = view.getWarehouseTable();
            if (table.getRowCount() > row) {
                table.setRowSelectionInterval(row, row);
            }
        });
    }

    @AfterEach
    void cleanUp() {
        SwingUtilities.invokeLater(() -> {
            // 清理测试数据和状态
            DefaultTableModel model = view.getDefaultTableModel();
            if (model != null) {
                model.setRowCount(0);
            }
        });
    }
}