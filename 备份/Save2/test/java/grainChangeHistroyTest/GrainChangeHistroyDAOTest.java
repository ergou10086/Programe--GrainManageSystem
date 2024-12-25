package grainChangeHistroyTest;

import controller.GrainController;
import dao.impl.GrainChangeHistoryDAOimpl;
import dao.impl.GrainDataDAOImpl;
import exceptions.DAOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.opentest4j.AssertionFailedError;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GrainChangeHistoryDAOTest {
    private static GrainChangeHistoryDAOimpl dao;
    private static GrainDataDAOImpl grainDataDAO;
    private static GrainController grainController;

    @BeforeAll
    static void setUp() {
        dao = new GrainChangeHistoryDAOimpl();
        grainDataDAO = new GrainDataDAOImpl();
        // 测试用粮食
        grainController = new GrainController("ErgouTree","系统管理员");

    }


    // 插入记录测试
    // 正常情况
    @Test
    @Order(1)
    void testRecordChange_Success() {
        assertDoesNotThrow(() -> {
            dao.recordChange("SVC001", "添加", "测试添加记录", "testUser");
        });
    }
    // 非正常情况，使用参数化
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @Order(2)
    void testRecordChange_InvalidInput(String input) {
        assertThrows(SQLException.class, () -> {
            dao.recordChange(input, input, input, input);
        });
    }
    // 错误情况测试
    @Test
    @Order(3)
    void testRecordChange_Fail() {
        assertThrows(SQLException.class, () -> {
            dao.recordChange("TEST10086", "添加", "测试添加记录", "testUser");
        });
    }


    // 时间范围查询测试
    // 正常情况
    @Test
    @Order(4)
    void testGetChangeHistoryByTimeRange() {
        assertDoesNotThrow(() -> {
            List<Map<String, Object>> result = dao.getChangeHistoryByTimeRange("2022-01-01", "2022-12-31");
            assertNotNull(result);
        });
    }
    // 不正常情况
    @Test
    @Order(5)
    void testGetChangeHistoryByTimeRange_InvalidInput() {
        assertThrows(SQLException.class, () -> {
           List<Map<String, Object>> result = dao.getChangeHistoryByTimeRange("", "");
           assertNull(result);
        });
    }
    // 错误情况测试
    @Test
    @Order(6)
    void testGetChangeHistoryByTimeRange_Fail() {
        assertThrows(AssertionFailedError.class, () -> {
            List<Map<String, Object>> result = dao.getChangeHistoryByTimeRange("6666-6-6", "6676-6-6");
            assertNull(result);
        });
    }



    // 获取指定粮食变更记录的测试
    // 正常情况
    @Test
    @Order(7)
    void testGetChangeHistoryByGrainId() {
        assertDoesNotThrow(() -> {
            List<Map<String, Object>> result = dao.getChangeHistory("SVC001");
            assertNotNull(result);
        });
    }
    // 错误情况测试
    @Test
    @Order(8)
    void testGetChangeHistoryByGrainId_Fail() {
        assertThrows(SQLException.class, () -> {
            List<Map<String, Object>> result = dao.getChangeHistory("NotExistGrainId");
            assertNull(result);
        });
    }
    // 不正常情况测试
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @Order(9)
    void testGetChangeHistoryByGrainId_InvalidInput(String input) {
        assertThrows(SQLException.class, () -> {
            List<Map<String, Object>> result = dao.getChangeHistory(input);
            assertNull(result);
        });
    }




    // 测试按操作者查询
    @ParameterizedTest
    @MethodSource("provideOperators")
    void testGetChangeHistoryByOperator(String operator, boolean shouldSucceed) {
        if (shouldSucceed) {
            assertDoesNotThrow(() -> {
                List<Map<String, Object>> results = dao.getChangeHistoryByOperator(operator);
                assertNotNull(results);
            });
        } else {
            assertDoesNotThrow(() -> {
                List<Map<String, Object>> results = dao.getChangeHistoryByOperator(operator);
                assertNull(results);
            });
        }
    }

    static Stream<Arguments> provideOperators() {
        return Stream.of(
                Arguments.of("testUser", true),
                Arguments.of("", false),
                Arguments.of(null, false)
        );
    }


    // 删除历史记录测试
    // 测试删除历史记录
    @ParameterizedTest
    @MethodSource("provideDeleteDates")
    void testDeleteOldRecords(String beforeTime, boolean shouldSucceed) {
        if (shouldSucceed) {
            assertDoesNotThrow(() -> {
                int result = dao.deleteOldRecords(beforeTime);
                assertTrue(result >= 0);
            });
        } else {
            assertThrows(SQLException.class, () -> {
                dao.deleteOldRecords(beforeTime);
            });
        }
    }

    static Stream<Arguments> provideDeleteDates() {
        return Stream.of(
                Arguments.of("2024-01-01", true),
                Arguments.of("invalid-date", false),
                Arguments.of(null, false)
        );
    }


    // 获取全部历史记录测试
    @Test
    @Order(8)
    void testGetAllChangeHistory() {

    }
}