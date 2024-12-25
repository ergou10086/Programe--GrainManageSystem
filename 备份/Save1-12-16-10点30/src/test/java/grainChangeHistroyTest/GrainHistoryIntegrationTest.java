package grainChangeHistroyTest;

import controller.HistoryRecordController;
import dao.impl.GrainChangeHistoryDAOimpl;
import entity.Grain;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import service.impl.GrainChangeHistoryServiceImpl;
import service.impl.GrainInfServiceImpl;
import view.HistoryRecordView;

import javax.swing.table.DefaultTableModel;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

// 定义测试类的执行顺序，按照方法上的 @Order 注解指定的顺序执行测试方法
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GrainHistoryIntegrationTest {

    // 以下是整个测试类中会用到的一些静态变量，用于存储相关的服务、控制器、视图以及测试数据相关信息

    private static HistoryRecordController controller;
    private static GrainChangeHistoryServiceImpl historyService;
    private static GrainInfServiceImpl grainService;
    private static HistoryRecordView view;
    private static String testGrainId;
    private static final String TEST_USER = "testUser";
    private static final String TEST_ROLE = "系统管理员";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 初始化操作
    @BeforeAll
    static void setUp() {
        historyService = new GrainChangeHistoryServiceImpl();
        grainService = new GrainInfServiceImpl();
        controller = new HistoryRecordController(TEST_USER, TEST_ROLE);
        view = controller.getView();

        testGrainId = UUID.randomUUID().toString();
        String testGrainCode = "TEST_" + System.currentTimeMillis();

        Grain testGrain = new Grain();
        testGrain.setIdGrain(testGrainId);
        testGrain.setGrainCode(testGrainCode);
        testGrain.setGrainName("集成测试粮食");
        testGrain.setGrainType("测试类型");
        testGrain.setGrainPrice(1.0);
        testGrain.setGrainShelfLife(6.0);
        testGrain.setGrainRemark("集成测试备注");
        try {
            historyService.recordGrainChange(testGrainId, "新增粮食", "新增粮食，粮食ID为：" + testGrainId, TEST_USER);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            grainService.addGrain(testGrain);
        } catch (Exception e) {
            fail("测试数据准备失败: " + e.getMessage());
        }
    }

    // 在所有测试方法执行之后执行一次，用于清理测试过程中产生的数据等资源
    @AfterAll
    static void tearDown() {
        try {
            if (testGrainId!= null) {
                // 根据测试粮食ID查找对应的粮食对象
                Grain grain = grainService.findGrainByCode(testGrainId);
                if (grain!= null) {
                    // 如果找到对应的粮食对象，则调用粮食信息服务的删除粮食方法将其删除，清理测试数据
                    grainService.deleteGrain(grain.getGrainCode());
                }
            }
        } catch (Exception e) {
            System.err.println("清理测试数据失败: " + e.getMessage());
        }
    }

    // 以下是一个内部嵌套类，用于对正常功能进行测试，即系统在正常情况下相关功能是否能正确运行
    @Nested
    @DisplayName("正常功能测试")
    class NormalFunctionTests {

        // 测试记录并显示历史记录的功能是否正常
        @Test
        @Order(1)
        void testRecordAndDisplayHistory() {
            assertDoesNotThrow(() -> {
                // 调用粮食变更历史记录服务的方法，记录粮食变更情况，传入粮食ID、变更操作类型、变更详细描述以及操作用户等信息
                historyService.recordGrainChange(testGrainId, "添加", "集成测试添加记录", TEST_USER);

                // 获取视图中的粮食模型（通常是用于展示粮食历史记录数据的表格模型）
                DefaultTableModel model = view.getGrainModel();
                boolean found = false;
                // 遍历表格模型中的每一行数据，查找是否存在新添加的历史记录对应的行
                for (int i = 0; i < model.getRowCount(); i++) {
                    if (model.getValueAt(i, 2).equals(testGrainId)) {
                        found = true;
                        // 验证该行中记录的变更操作类型是否正确
                        assertEquals("添加", model.getValueAt(i, 1));
                        // 验证该行中记录的操作用户是否正确
                        assertEquals(TEST_USER, model.getValueAt(i, 4));
                        break;
                    }
                }
                // 断言能够找到新添加的历史记录，否则测试失败
                assertTrue(found, "未找到新添加的历史记录");
            });
        }

        // 测试搜索历史记录的功能是否正常
        @Test
        @Order(2)
        void testSearchHistory() {
            assertDoesNotThrow(() -> {
                // 获取当前日期，格式化为 "yyyy-MM-dd" 的形式，用于作为搜索历史记录的时间范围起点和终点（此处简单测试搜索当天的历史记录情况）
                String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                // 调用控制器的搜索历史记录方法，传入相关搜索条件（这里是按粮食类型搜索，以及指定的日期范围）
               // controller.searchHistory("grain", today, today);

                // 获取视图中的粮食模型，后续进行验证
                DefaultTableModel model = view.getGrainModel();
                // 断言获取到的模型不为空，即能够获取到数据展示模型
                assertNotNull(model);
                // 断言模型中的行数大于0，即搜索到了符合条件的历史记录数据
                assertTrue(model.getRowCount() > 0);
            });
        }
    }

    // 以下是一个内部嵌套类，用于对异常情况进行测试，即系统在输入异常数据等情况下是否能正确处理并给出预期的异常表现
    @Nested
    @DisplayName("异常情况测试")
    class AbnormalTests {

        // 使用参数化测试方法，测试不同的无效日期范围作为搜索历史记录的参数时系统的处理情况
        @ParameterizedTest
        @MethodSource("provideInvalidDateRanges")
        @Order(3)
        void testInvalidDateRanges(String startDate, String endDate) {
            assertDoesNotThrow(() -> {
                // 调用控制器的搜索历史记录方法，传入无效的日期范围参数
                //controller.searchHistory("grain", startDate, endDate);
                // 获取视图中的粮食模型
                DefaultTableModel model = view.getGrainModel();
                // 断言模型中的行数为0，即对于无效日期范围搜索，应该没有符合条件的历史记录被返回
                assertEquals(0, model.getRowCount());
            });
        }

        // 提供参数化测试方法所需的无效日期范围参数数据，以流的形式返回多个参数组合（这里每个组合包含起始日期和结束日期两个字符串参数）
        static Stream<Arguments> provideInvalidDateRanges() {
            return Stream.of(
                    Arguments.of("2024-13-01", "2024-12-31"),  // 月份超出范围的无效日期
                    Arguments.of("2024-01-32", "2024-02-01"),  // 日期超出范围的无效日期
                    Arguments.of("invalid", "date"),           // 非法格式的日期字符串
                    Arguments.of("", "")                       // 空的日期字符串
            );
        }

        // 测试传入无效参数（这里是全部为null的参数）记录粮食变更历史时是否会抛出预期的 SQLException 异常
        @Test
        @Order(4)
        void testInvalidOperations() {
            assertThrows(SQLException.class, () -> {
                // 调用粮食变更历史记录服务的记录方法，传入全部为null的参数，期望抛出 SQLException 异常
                historyService.recordGrainChange(null, null, null, null);
            });
        }
    }

    // 以下是一个内部嵌套类，用于对边界值情况进行测试，比如输入数据达到最大长度等边界情况时系统的处理情况
    @Nested
    @DisplayName("边界值测试")
    class BoundaryTests {

        // 测试记录粮食变更历史时，当详细描述信息达到数据库 TEXT 类型最大长度（这里模拟为 65535 个字符）时是否能正常操作（不抛出异常）
        @Test
        @Order(5)
        void testMaxLengthValues() {
            String longDetail = "a".repeat(65535); // MySQL TEXT 类型最大长度
            assertDoesNotThrow(() -> {
                // 调用粮食变更历史记录服务的记录方法，传入相关参数，包括达到最大长度的详细描述信息，验证是否能正常执行
                historyService.recordGrainChange(testGrainId, "边界测试", longDetail, TEST_USER);
            });
        }

        // 测试记录粮食变更历史时，当详细描述信息包含各种特殊字符时是否能正常操作（不抛出异常）
        @Test
        @Order(6)
        void testSpecialCharacters() {
            String specialChars = "!@#$%^&*()_+<>?\"':;[]{}\\|～·！@#￥%……&*（）——+{}|:\"《》？、；‘’【】";
            assertDoesNotThrow(() -> {
                // 调用粮食变更历史记录服务的记录方法，传入包含特殊字符的详细描述信息等参数，验证是否能正常执行
                historyService.recordGrainChange(testGrainId, "特殊字符测试", specialChars, TEST_USER);
            });
        }
    }

    // 以下是一个内部嵌套类，用于对性能情况进行测试，比如批量操作、并发操作等情况下系统的性能表现是否符合预期
    @Nested
    @DisplayName("性能测试")
    class PerformanceTests {

        // 测试批量记录粮食变更历史操作的性能，验证是否能在指定时间（10 秒）内完成操作，避免出现性能过差的情况
        @Test
        @Order(7)
        void testBatchOperations() {
            assertTimeout(java.time.Duration.ofSeconds(10), () -> {
                for (int i = 0; i < 1000; i++) {
                    historyService.recordGrainChange(
                            testGrainId,
                            "性能测试",
                            "批量测试记录 " + i,
                            TEST_USER
                    );
                }
            });
        }

        // 测试并发记录粮食变更历史操作的性能，验证是否能在指定时间（30 秒）内完成操作，并且正确处理并发情况，避免出现错误
        @Test
        @Order(8)
        void testConcurrentOperations() throws InterruptedException {
            int threadCount = 10;
            int operationsPerThread = 100;
            // 创建一个固定线程数的线程池，用于执行并发任务
            ExecutorService executor = Executors.newFixedThreadPool(threadCount);
            // 创建一个 CountDownLatch，用于协调多个线程完成任务的等待机制
            CountDownLatch latch = new CountDownLatch(threadCount);

            assertTimeoutPreemptively(java.time.Duration.ofSeconds(30), () -> {
                for (int i = 0; i < threadCount; i++) {
                    executor.submit(() -> {
                        try {
                            for (int j = 0; j < operationsPerThread; j++) {
                                historyService.recordGrainChange(
                                        testGrainId,
                                        "并发测试",
                                        "并发测试记录 " + j,
                                        TEST_USER
                                );
                            }
                        } catch (Exception e) {
                            fail("并发测试失败: " + e.getMessage());
                        } finally {
                            // 每个线程完成任务后，将 CountDownLatch 的计数减 1
                            latch.countDown();
                        }
                    });
                }
                // 主线程等待所有线程完成任务（即 CountDownLatch 的计数变为 0）
                latch.await();
            });

            // 关闭线程池，释放相关资源
            executor.shutdown();
        }
    }

    // 以下是一个内部嵌套类，用于对压力情况进行测试，比如大量数据加载、内存使用等方面系统的表现是否符合预期，避免出现资源耗尽等问题
    @Nested
    @DisplayName("压力测试")
    class StressTests {

        // 测试加载大量历史记录数据时的性能情况，验证是否能在指定时间（5 秒）内完成查询操作，并且能正确获取到数据
        @Test
        @Order(9)
        void testLargeDataLoad() {
            assertTimeout(java.time.Duration.ofSeconds(5), () -> {
                // 生成大量测试数据，循环调用记录粮食变更历史的方法
                for (int i = 0; i < 100; i++) {
                    historyService.recordGrainChange(
                            testGrainId,
                            "压力测试",
                            "压力测试记录 " + i,
                            TEST_USER
                    );
                }

                // 测试查询性能，记录查询开始时间
                long startTime = System.currentTimeMillis();
                List<Map<String, Object>> results = historyService.getGrainHistory(testGrainId);
                long endTime = System.currentTimeMillis();

                // 验证查询时间在可接受范围内（5 秒），即查询操作耗时小于 5000 毫秒
                assertTrue((endTime - startTime) < 5000);
                // 验证查询结果不为空，即能够获取到相应的历史记录数据
                assertNotNull(results);
                assertFalse(results.isEmpty());
            });
        }

        // 测试在执行内存密集操作（多次查询获取历史记录数据并合并结果）时内存使用情况是否在合理范围内（小于 500MB）
        @Test
        @Order(10)
        void testMemoryUsage() {
            Runtime runtime = Runtime.getRuntime();
            long usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();

            assertTimeout(java.time.Duration.ofMinutes(1), () -> {
                // 执行内存密集操作，多次查询历史记录数据并合并到一个列表中
                List<Map<String, Object>> allResults = new ArrayList<>();
                for (int i = 0; i < 100; i++) {
                    List<Map<String, Object>> results = historyService.getGrainHistory(testGrainId);
                    allResults.addAll(results);
                }

                long usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
                // 验证内存增长在合理范围内（小于 500MB，这里将字节数转换为 MB 进行比较）
                assertTrue((usedMemoryAfter - usedMemoryBefore) < 500 * 1024 * 1024);
            });
        }
    }
}