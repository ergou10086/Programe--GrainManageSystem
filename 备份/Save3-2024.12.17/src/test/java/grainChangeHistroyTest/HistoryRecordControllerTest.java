package grainChangeHistroyTest;

import controller.HistoryRecordController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import view.HistoryRecordView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class HistoryRecordControllerTest {
    private static HistoryRecordController controller;
    private static HistoryRecordView view;

    @BeforeAll
    static void setUp() {
        SwingUtilities.invokeLater(() -> {
            try {
                controller = new HistoryRecordController("testUser", "系统管理员");
                Field viewField = HistoryRecordController.class.getDeclaredField("view");
                viewField.setAccessible(true);
                view = (HistoryRecordView) viewField.get(controller);
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
    class SearchHistoryTests {
        @Test
        void testSearchHistory() {
            assertDoesNotThrow(() -> {
                Method searchHistoryMethod = HistoryRecordController.class.getDeclaredMethod(
                        "searchHistory", String.class, String.class, String.class);
                searchHistoryMethod.setAccessible(true);

                String startDate = "2023-01-01";
                String endDate = "2023-12-31";

                // 测试不同类型的历史记录搜索
                searchHistoryMethod.invoke(controller, "grain", startDate, endDate);
                searchHistoryMethod.invoke(controller, "storage", startDate, endDate);
                searchHistoryMethod.invoke(controller, "inventory", startDate, endDate);
            });
        }
    }

    @Nested
    @Order(2)
    class LoadHistoryTests {
        @Test
        void testLoadGrainHistory() {
            assertDoesNotThrow(() -> {
                Method loadGrainHistoryMethod = HistoryRecordController.class
                        .getDeclaredMethod("loadGrainHistory");
                loadGrainHistoryMethod.setAccessible(true);
                loadGrainHistoryMethod.invoke(controller);

                DefaultTableModel model = view.getGrainModel();
                assertNotNull(model);
            });
        }

        @Test
        void testLoadStorageHistory() {
            assertDoesNotThrow(() -> {
                Method loadStorageHistoryMethod = HistoryRecordController.class
                        .getDeclaredMethod("loadStorageHistory");
                loadStorageHistoryMethod.setAccessible(true);
                loadStorageHistoryMethod.invoke(controller);

                DefaultTableModel model = view.getStorageModel();
                assertNotNull(model);
            });
        }
    }

    @Nested
    @Order(3)
    class DeleteRestoreTests {
        @Test
        void testHandleDeleteRecord() {
            assertDoesNotThrow(() -> {
                Method handleDeleteMethod = HistoryRecordController.class
                        .getDeclaredMethod("handleDeleteRecord");
                handleDeleteMethod.setAccessible(true);

                setupTableSelection(0);
                handleDeleteMethod.invoke(controller);
            });
        }

        @Test
        void testHandleRestore() {
            assertDoesNotThrow(() -> {
                Method handleRestoreMethod = HistoryRecordController.class
                        .getDeclaredMethod("handleRestore");
                handleRestoreMethod.setAccessible(true);

                setupTableSelection(0);
                handleRestoreMethod.invoke(controller);
            });
        }
    }

    @Nested
    @Order(4)
    class SearchEntryExitTests {
        @ParameterizedTest
        @ValueSource(strings = {"2023-01-01", "2023-06-01", "2023-12-31"})
        void testSearchEntryHistory(String date) {
            assertDoesNotThrow(() -> {
                Method searchEntryHistoryMethod = HistoryRecordController.class
                        .getDeclaredMethod("searchEntryHistory", String.class, String.class);
                searchEntryHistoryMethod.setAccessible(true);

                List<Map<String, Object>> results = (List<Map<String, Object>>)
                        searchEntryHistoryMethod.invoke(controller, date, date);
                assertNotNull(results);
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"2023-01-01", "2023-06-01", "2023-12-31"})
        void testSearchExitHistory(String date) {
            assertDoesNotThrow(() -> {
                Method searchExitHistoryMethod = HistoryRecordController.class
                        .getDeclaredMethod("searchExitHistory", String.class, String.class);
                searchExitHistoryMethod.setAccessible(true);

                List<Map<String, Object>> results = (List<Map<String, Object>>)
                        searchExitHistoryMethod.invoke(controller, date, date);
                assertNotNull(results);
            });
        }
    }

    // 辅助方法
    private void setupTableSelection(int row) {
        SwingUtilities.invokeLater(() -> {
            JTable table = view.getGrainHistoryTable();
            if (table.getRowCount() > row) {
                table.setRowSelectionInterval(row, row);
            }
        });
    }

    @AfterEach
    void cleanup() {
        SwingUtilities.invokeLater(() -> {
            // 清理测试数据
            view.getGrainModel().setRowCount(0);
            view.getStorageModel().setRowCount(0);
            view.getInspectionModel().setRowCount(0);
        });
    }
}
