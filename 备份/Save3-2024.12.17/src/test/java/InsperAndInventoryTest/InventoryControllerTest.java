package InsperAndInventoryTest;

import controller.InventoryController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import view.InventoryView;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InventoryControllerTest {
    private static InventoryController controller;
    private static InventoryView view;

    @BeforeAll
    static void setUp() {
        SwingUtilities.invokeLater(() -> {
            try {
                controller = new InventoryController("testUser", "管理员");
                Field viewField = InventoryController.class.getDeclaredField("view");
                viewField.setAccessible(true);
                view = (InventoryView) viewField.get(controller);
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
    class DataLoadingTests {
        @Test
        void testLoadGrainData() {
            assertDoesNotThrow(() -> {
                controller.loadGrainData();
                DefaultTableModel model = view.getDtm();
                assertNotNull(model);
            });
        }
    }

    @Nested
    @Order(2)
    class SearchTests {
        @Test
        void testSearch() {
            assertDoesNotThrow(() -> {
                setSearchField("测试仓库");
                controller.search();
                DefaultTableModel model = view.getDtm();
                assertNotNull(model);
            });
        }
    }

    @Nested
    @Order(3)
    class NoticeTests {
        @ParameterizedTest
        @ValueSource(strings = {"100", "200", "300"})
        void testStr1(String value) {
            assertDoesNotThrow(() -> {
                List<Integer> result = controller.str1(value);
                assertNotNull(result);
            });
        }

        @Test
        void testStr2() {
            assertDoesNotThrow(() -> {
                List<Integer> result = controller.str2();
                assertNotNull(result);
            });
        }

        @Test
        void testNotice() {
            assertDoesNotThrow(() -> {
                controller.notice();
            });
        }
    }

    @Nested
    @Order(4)
    class SetTests {
        @Test
        void testSet() {
            assertDoesNotThrow(() -> {
                setupTableSelection(0);
                setNoticeValue("50");
                controller.set();
            });
        }

        @Test
        void testManyset() {
            assertDoesNotThrow(() -> {
                setupMultipleTableSelection(new int[]{0, 1});
                setNoticeValue("100");
                controller.manyset();
            });
        }
    }

    @Nested
    @Order(5)
    class DeleteTests {
        @Test
        void testDelete() {
            assertDoesNotThrow(() -> {
                setupTableSelection(0);
                controller.delete();
            });
        }
    }

    @Nested
    @Order(6)
    class BackgroundColorTests {
        @Test
        void testSetOneRowBackgroundColor() {
            assertDoesNotThrow(() -> {
                controller.setOneRowBackgroundColor();
            });
        }

        @Test
        void testSetOneRowBackgroundColor1() {
            assertDoesNotThrow(() -> {
                controller.setOneRowBackgroundColor1();
            });
        }

        @Test
        void testSetOneRowBackgroundColor2() {
            assertDoesNotThrow(() -> {
                controller.setOneRowBackgroundColor2();
            });
        }
    }

    // 辅助方法
    private void setSearchField(String value) throws Exception {
        Method setSearchContentMethod = InventoryView.class.getDeclaredMethod("setsearchcontent", String.class);
        setSearchContentMethod.setAccessible(true);
        setSearchContentMethod.invoke(view, value);
    }

    private void setNoticeValue(String value) throws Exception {
        Method setNoticeMethod = InventoryView.class.getDeclaredMethod("setSet", String.class);
        setNoticeMethod.setAccessible(true);
        setNoticeMethod.invoke(view, value);
    }

    private void setupTableSelection(int row) {
        SwingUtilities.invokeLater(() -> {
            JTable table = view.getTable();
            if (table.getRowCount() > row) {
                table.setRowSelectionInterval(row, row);
            }
        });
    }

    private void setupMultipleTableSelection(int[] rows) {
        SwingUtilities.invokeLater(() -> {
            JTable table = view.getTable();
            for (int row : rows) {
                if (table.getRowCount() > row) {
                    table.addRowSelectionInterval(row, row);
                }
            }
        });
    }

    @AfterEach
    void cleanup() {
        SwingUtilities.invokeLater(() -> {
            DefaultTableModel model = view.getDtm();
            if (model != null) {
                model.setRowCount(0);
            }
        });
    }
}
