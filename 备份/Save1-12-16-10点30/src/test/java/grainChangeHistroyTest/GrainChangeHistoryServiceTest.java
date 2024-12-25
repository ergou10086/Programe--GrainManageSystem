package grainChangeHistroyTest;

import entity.Grain;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import service.impl.GrainChangeHistoryServiceImpl;
import service.impl.GrainInfServiceImpl;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class GrainChangeHistoryServiceTest {
    private static GrainChangeHistoryServiceImpl service;
    private static GrainInfServiceImpl grainService;
    private static String testGrainId;
    private static final String TEST_USER = "testUser";

    @BeforeAll
    static void setUp() {
        service = new GrainChangeHistoryServiceImpl();
        grainService = new GrainInfServiceImpl();

        /*
        // 创建测试用粮食数据
        testGrainId = UUID.randomUUID().toString();
        String testGrainCode = "TEST10086";

        Grain testGrain = new Grain();
        testGrain.setIdGrain(testGrainId);
        testGrain.setGrainCode(testGrainCode);
        testGrain.setGrainName("测试粮食");
        testGrain.setGrainType("测试类型");
        testGrain.setGrainPrice(1.0);
        testGrain.setGrainShelfLife(6.0);
        testGrain.setGrainRemark("测试备注");

        try {
            grainService.addGrain(testGrain);
        } catch (Exception e) {
            fail("测试数据准备失败: " + e.getMessage());
        }
         */
    }

    // 添加记录测试
    // 正常情况测试
    @Test
    @Order(1)
    void testRecordGrainChange_Success() {
        String id = String.valueOf(grainService.getIdGrainsByName("TEST10086"));
        assertDoesNotThrow(() -> {
            service.recordGrainChange(id, "添加", "测试添加记录", "testUser");
        });
    }
    // 不正常情况测试
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void testRecordGrainChange_InvalidInput(String input) {
        assertThrows(SQLException.class, () -> {
            service.recordGrainChange(input, input, input, input);
        });
    }
    // 错误情况测试
    @Test
    void testRecordGrainChange_Error() {
        assertDoesNotThrow(() -> {
            service.recordGrainChange(null, "添加", "测试记录", "testUser");
        });
    }



    // 获取指定粮食的历史记录查询
    @Test
    @Order(2)
    void testGetGrainChangeHistory() {
        assertDoesNotThrow(() -> {
            assertNotNull(service.getGrainHistory("TEST10086"));
        });
    }
    // 不正常情况测试
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    void testGetGrainChangeHistory_InvalidInput(String input) {
        assertThrows(SQLException.class, () -> {
            service.getGrainHistory(input);
        });
    }
    // 错误情况测试
    @Test
    void testGetGrainChangeHistory_Error() {
        assertDoesNotThrow(()->{
            service.getGrainHistory("NotExistGrain");
        });
    }
}