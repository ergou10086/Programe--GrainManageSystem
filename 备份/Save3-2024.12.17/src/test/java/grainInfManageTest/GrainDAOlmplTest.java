package grainInfManageTest;


import dao.impl.GrainDataDAOImpl;
import entity.Grain;
import org.junit.jupiter.api.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class GrainDAOlmplTest {
    private static GrainDataDAOImpl grainDAO;
    private static Grain testGrain;

    // 预先加载一个正常情况，用于正常情况的测试
    @BeforeAll
    static void setUp() {
        grainDAO = new GrainDataDAOImpl();
        testGrain = new Grain("TEST001", "测试粮食", "测试类型", 1.0, 6, "测试备注");
        testGrain = new Grain("TEST003", "测试粮食2", "测试类型", 1.50, 5, "测试备注");
        testGrain = new Grain("TEST040", "测试粮食3", "测试类型", 1.60, 65, "测试备注");
    }


    // 测试添加粮食

    // 正常情况
    @Test
    @Order(1)
    void testAddGrain() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.addGrain(testGrain);
            assertTrue(result);
        });
    }
    // 不正常情况，添加的是空串
    @Test
    @Order(2)
    void testAddGrainNull() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.addGrain(null);
            assertFalse(result);
        });
    }
    // 错误情况，添加的是重复的粮食
    @Test
    @Order(3)
    void testAddGrainDuplicate() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.addGrain(testGrain);
            assertFalse(result);
        });
    }

    // 参数化测试
    // 提供测试添加粮食方法的参数数据（正常、空值、重复情况）
    private static Stream<Arguments> addGrainParams() {
        return Stream.of(
                Arguments.of(testGrain, true),
                Arguments.of(null, false),
                Arguments.of(testGrain, false)
        );
    }
    // 测试添加粮食 - 参数化测试
    @ParameterizedTest
    @MethodSource("addGrainParams")
    @Order(1)
    void testAddGrain(Grain grain, boolean expectedResult) {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.addGrain(grain);
            assertEquals(expectedResult, result);
        });
    }


    // 测试查找粮食方法
    // 正常情况
    @Test
    @Order(4)
    void testFindGrainByCode() {
        assertDoesNotThrow(() -> {
            Grain found = grainDAO.findGrainByCode("TEST001");
            assertNotNull(found);
            assertEquals(testGrain.getGrainName(), found.getGrainName());
        });
    }
    // 错误情况，查找一个不存在的粮食
    @Test
    @Order(5)
    void testFindGrainByCodeNotFound() {
        assertDoesNotThrow(() -> {
            Grain found = grainDAO.findGrainByCode("NOTEXIST");
            assertNull(found);
        });
    }
    // 不正常情况，查找一个空串
    @Test
    @Order(6)
    void testFindGrainByCodeNull() {
        assertDoesNotThrow(() -> {
            Grain found = grainDAO.findGrainByCode(null);
            assertNull(found);
        });
    }

    //参数化测试
    // 提供查找粮食方法的参数数据（正常、不存在、空值情况）
    private static Stream<Arguments> findGrainByCodeParams() {
        return Stream.of(
                Arguments.of("TEST001", Optional.of(testGrain)),
                Arguments.of("NOTEXIST", Optional.empty()),
                Arguments.of(null, Optional.empty())
        );
    }

    // 测试查找粮食方法 - 参数化测试
    @ParameterizedTest
    @MethodSource("findGrainByCodeParams")
    @Order(2)
    void testFindGrainByCode(String code, Optional<Grain> expectedResult) {
        assertDoesNotThrow(() -> {
            Optional<Grain> found = Optional.ofNullable(grainDAO.findGrainByCode(code));
            assertEquals(expectedResult, found);
        });
    }




    // 测试更新粮食方法
    // 正常情况
    @Test
    @Order(7)
    void testUpdateGrain() {
        testGrain.setGrainPrice(2.0);
        testGrain.setIdGrain("1165");
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.updateGrain(testGrain);
            assertTrue(result);

            Grain updated = grainDAO.findGrainByCode("TEST001");
            assertEquals(2.0, updated.getGrainPrice());

        });
    }
    // 错误情况，更新一个不存在的粮食
    @Test
    @Order(8)
    void testUpdateGrainNotFound() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.updateGrain(new Grain("NOTEXIST", "测试粮食", "测试类型", 1.0, 6, "测试备注"));
            assertFalse(result);
        });
    }
    // 不正常情况，更新的是空串
    @Test
    @Order(9)
    void testUpdateGrainNull() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.updateGrain(new Grain(null, "", "", 0, 0, ""));
            assertFalse(result);
        });
    }

    // 参数化测试
    // 提供更新粮食方法的参数数据（正常、不存在、空值情况）
    private static Stream<Arguments> updateGrainParams() {
        Grain updatedGrain = new Grain("TEST001", "测试粮食", "测试类型", 2.0, 6, "测试备注");
        return Stream.of(
                Arguments.of(updatedGrain, true),
                Arguments.of(new Grain("NOTEXIST", "测试粮食", "测试类型", 1.0, 6, "测试备注"), false),
                Arguments.of(new Grain(null, "", "", 0, 0, ""), false)
        );
    }

    // 测试更新粮食方法 - 参数化测试
    @ParameterizedTest
    @MethodSource("updateGrainParams")
    @Order(3)
    void testUpdateGrain(Grain grain, boolean expectedResult) {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.updateGrain(grain);
            assertEquals(expectedResult, result);
            if (expectedResult && grain.getGrainCode()!= null) {
                Grain updated = grainDAO.findGrainByCode(grain.getGrainCode());
                assertEquals(grain.getGrainPrice(), updated.getGrainPrice());
            }
        });
    }



    // 测试软删除粮食方法
    // 正常情况
    @Test
    @Order(10)
    void testDeleteGrain() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.deleteGrain("TEST001");
            assertTrue(result);

            Grain deleted = grainDAO.findGrainByCode("TEST001");
            assertNull(deleted);
        });
    }
    // 错误情况，删除一个不存在的粮食
    @Test
    @Order(11)
    void testDeleteGrainNotFound() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.deleteGrain("NOTEXIST");
            assertFalse(result);
        });
    }

    // 参数化测试
    // 提供软删除粮食方法的参数数据（正常、不存在情况）
    private static Stream<Arguments> deleteGrainParams() {
        return Stream.of(
                Arguments.of("TEST001", true),
                Arguments.of("NOTEXIST", false)
        );
    }

    // 测试软删除粮食方法 - 参数化测试
    @ParameterizedTest
    @MethodSource("deleteGrainParams")
    @Order(4)
    void testDeleteGrain(String code, boolean expectedResult) {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.deleteGrain(code);
            assertEquals(expectedResult, result);
            if (expectedResult) {
                Grain deleted = grainDAO.findGrainByCode(code);
                assertNull(deleted);
            }
        });
    }




    // 测试获取所有已存在的粮食信息
    // 正常情况
    @Test
    @Order(12)
    void testGetAllGrain() {
        assertDoesNotThrow(() -> {
            List<Grain> grains = grainDAO.getAllGrains();
            assertNotNull(grains);
        });
    }




    // 测试关键字模糊查询粮食相关的数据记录
    // 正常情况
    @Test
    @Order(13)
    void searchGrains(){
        assertDoesNotThrow(() -> {
            List<Grain> results = grainDAO.searchGrains("测试粮食2");
            assertFalse(results.isEmpty());
            assertTrue(results.stream().anyMatch(g -> g.getGrainCode().equals("TEST003")));
        });
    }
    // 错误情况，查询一个不存在的关键字
    @Test
    @Order(14)
    void searchGrainsNotFound(){
        assertDoesNotThrow(() -> {
            List<Grain> results = grainDAO.searchGrains("NOTEXIST");
            assertTrue(results.isEmpty());
        });
    }
    // 不正常情况，查询空串
    @Test
    @Order(15)
    void searchGrainsNull(){
        assertDoesNotThrow(() -> {
            List<Grain> results = grainDAO.searchGrains(null);
            assertTrue(results.isEmpty());
        });
    }

    // 参数化测试
    // 提供关键字模糊查询粮食相关的数据记录的参数数据（正常、不存在、空值情况）
    private static Stream<Arguments> searchGrainsParams() {
        return Stream.of(
                Arguments.of("测试粮食2", false),
                Arguments.of("NOTEXIST", true),
                Arguments.of(null, true)
        );
    }

    // 测试关键字模糊查询粮食相关的数据记录 - 参数化测试
    @ParameterizedTest
    @MethodSource("searchGrainsParams")
    @Order(6)
    void searchGrains(String keyword, boolean expectedEmpty) {
        assertDoesNotThrow(() -> {
            List<Grain> results = grainDAO.searchGrains(keyword);
            assertEquals(expectedEmpty, results.isEmpty());
            if (!expectedEmpty) {
                assertTrue(results.stream().anyMatch(g -> g.getGrainCode().equals("TEST003")));
            }
        });
    }


    // 测试粮食信息是否被软删除
    // 正常情况
    @Test
    @Order(16)
    void testIsGrainDeletedCheck() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.isGrainDeletedCheck("TEST003");
            assertFalse(result);
        });
    }
    // 错误情况，查询一个不存在的粮食
    @Test
    @Order(17)
    void testIsGrainDeletedCheckNotFound() {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.isGrainDeletedCheck("NOTEXIST");
            assertFalse(result);
        });
    }

    // 参数化测试
    // 提供测试粮食信息是否被软删除的参数数据（正常、不存在情况）
    private static Stream<Arguments> isGrainDeletedCheckParams() {
        return Stream.of(
                Arguments.of("TEST003", false),
                Arguments.of("NOTEXIST", false)
        );
    }

    // 测试粮食信息是否被软删除 - 参数化测试
    @ParameterizedTest
    @MethodSource("isGrainDeletedCheckParams")
    @Order(7)
    void testIsGrainDeletedCheck(String code, boolean expectedResult) {
        assertDoesNotThrow(() -> {
            boolean result = grainDAO.isGrainDeletedCheck(code);
            assertEquals(expectedResult, result);
        });
    }
}
