package grainInfManageTest;

import entity.Grain;
import exceptions.GrainOrWarehouseServiceExceptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import service.impl.GrainInfServiceImpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class GrainInfServiceImplTest {
    private static GrainInfServiceImpl grainService;

    @BeforeEach
    void setUp() {
        grainService = new GrainInfServiceImpl();
    }



    // 提供添加粮食方法的参数数据（正常、已存在、空值等多种情况）
    private static Stream<Arguments> addGrainParams() {
        Grain validGrain = new Grain("SVC001", "服务测试粮食", "测试类型", 1.5, 12, "服务测试");
        Grain emptyGrain = new Grain("", "", "", 0, 0, "");
        return Stream.of(
                Arguments.of(validGrain, true),
                Arguments.of(validGrain, false),
                Arguments.of(emptyGrain, false)
        );
    }
    // 测试添加粮食的方法 - 参数化测试，覆盖多种情况
    @ParameterizedTest
    @MethodSource("addGrainParams")
    void testAddGrain(Grain grain, boolean expectedResult) {
        if (expectedResult) {
            assertDoesNotThrow(() -> {
                boolean result = grainService.addGrain(grain);
                assertTrue(result);
            });
        } else {
            assertThrows(GrainOrWarehouseServiceExceptions.class, () -> {
                boolean result = grainService.addGrain(grain);
                assertFalse(result);
            });
        }
    }



    // 测试更新粮食的方法 - 覆盖更新成功、更新不存在的粮食等情况
    @Test
    void testUpdateGrain() {
        Grain grainToAdd = new Grain("SVC002", "更新测试粮食", "测试类型", 1.0, 10, "更新测试");
        assertDoesNotThrow(() -> {
            grainService.addGrain(grainToAdd);
            grainToAdd.setGrainPrice(2.0);
            boolean updateResult = grainService.updateGrain(grainToAdd);
            assertTrue(updateResult);
        });

        Grain nonExistentGrain = new Grain("NON_EXIST", "不存在粮食", "测试类型", 1.0, 10, "不存在测试");
        assertThrows(GrainOrWarehouseServiceExceptions.class, () -> {
            grainService.updateGrain(nonExistentGrain);
        });
    }



    // 提供查找粮食方法的参数数据（存在、不存在、空关键字等情况）
    private static Stream<Arguments> searchGrainsParams() {
        Grain existingGrain = new Grain("SVC003", "查找测试粮食", "测试类型", 1.5, 12, "查找测试");
        assertDoesNotThrow(() -> grainService.addGrain(existingGrain));
        return Stream.of(
                Arguments.of("查找测试", Optional.of(existingGrain)),
                Arguments.of("不存在关键字", Optional.empty()),
                Arguments.of(null, Optional.empty())
        );
    }
    // 测试查找粮食的方法 - 参数化测试，覆盖多种查找情况
    @ParameterizedTest
    @MethodSource("searchGrainsParams")
    void testSearchGrains(String keyword, Optional<Grain> expectedResult) {
        assertDoesNotThrow(() -> {
            List<Grain> results = grainService.searchGrains(keyword);
            if (expectedResult.isPresent()) {
                assertFalse(results.isEmpty());
                assertTrue(results.stream().anyMatch(g -> g.getGrainCode().equals(expectedResult.get().getGrainCode())));
            } else {
                assertTrue(results.isEmpty());
            }
        });
    }



    // 测试删除粮食的方法 - 覆盖删除成功、删除不存在的粮食等情况
    @Test
    void testDeleteGrain() {
        Grain grainToAdd = new Grain("SVC004", "删除测试粮食", "测试类型", 1.0, 10, "删除测试");
        assertDoesNotThrow(() -> {
            grainService.addGrain(grainToAdd);
            boolean deleteResult = grainService.deleteGrain(grainToAdd.getGrainCode());
            assertTrue(deleteResult);
            Grain deletedGrain = grainService.findGrainByCode(grainToAdd.getGrainCode());
            assertNull(deletedGrain);
        });

        String nonExistentCode = "NON_EXIST_CODE";
        assertThrows(GrainOrWarehouseServiceExceptions.class, () -> {
            grainService.deleteGrain(nonExistentCode);
        });
    }



    // 测试获取所有粮食信息的方法
    @Test
    void testGetAllGrains() {
        assertDoesNotThrow(() -> {
            List<Grain> grains = grainService.getAllGrains();
            assertNotNull(grains);
        });
    }



    // 测试彻底删除粮食信息的方法 - 这里假设彻底删除不存在的粮食会抛异常，可根据实际情况调整
    @Test
    void testDeleteGrainsPermanently() {
        String existingGrainName = "服务测试粮食";
        assertDoesNotThrow(() -> {
            Grain grain = new Grain("SVC005", existingGrainName, "测试类型", 1.0, 10, "测试");
            grainService.addGrain(grain);
            grainService.deleteGrainsPermanently(existingGrainName);
            // 可根据实际情况进一步验证是否真的彻底删除了，比如尝试查找等操作
        });

        String nonExistentGrainName = "不存在粮食名";
        assertThrows(RuntimeException.class, () -> {
            grainService.deleteGrainsPermanently(nonExistentGrainName);
        });
    }



    // 测试恢复粮食信息的方法 - 覆盖恢复存在、恢复不存在等情况，假设调用恢复不存在的会抛异常
    @Test
    void testRestoreGrain() {
        String existingGrainName = "恢复测试粮食";
        Grain grain = new Grain("SVC006", existingGrainName, "测试类型", 1.0, 10, "恢复测试");
        assertDoesNotThrow(() -> {
            grainService.addGrain(grain);
            grainService.deleteGrain(grain.getGrainCode());
            grainService.restoreGrain(existingGrainName);
            Grain restoredGrain = grainService.findGrainByCode(grain.getGrainCode());
            assertNotNull(restoredGrain);
        });

        String nonExistentGrainName = "不存在恢复粮食名";
        assertThrows(RuntimeException.class, () -> {
            grainService.restoreGrain(nonExistentGrainName);
        });
    }



    // 测试通过名称获取id的方法 - 这里简单验证不抛异常以及返回结果非空（可根据实际更严格验证返回内容是否正确）
    @Test
    void testGetIdGrainsByName() {
        String existingGrainName = "获取id测试粮食";
        Grain grain = new Grain("SVC007", existingGrainName, "测试类型", 1.0, 10, "获取id测试");
        assertDoesNotThrow(() -> {
            grainService.addGrain(grain);
            List<String> ids = grainService.getIdGrainsByName(existingGrainName);
            assertNotNull(ids);
        });
    }



    // 测试检查粮食是否被软删除的方法 - 覆盖已删除、未删除、不存在等情况
    @Test
    void testIsGrainDeletedCheck() {
        Grain grainToAdd = new Grain("SVC008", "软删除测试粮食", "测试类型", 1.0, 10, "软删除测试");
        assertDoesNotThrow(() -> {
            grainService.addGrain(grainToAdd);
            boolean isDeleted = grainService.isGrainDeletedCheck(grainToAdd.getGrainCode());
            assertFalse(isDeleted);
            grainService.deleteGrain(grainToAdd.getGrainCode());
            isDeleted = grainService.isGrainDeletedCheck(grainToAdd.getGrainCode());
            assertTrue(isDeleted);
        });

        String nonExistentCode = "NON_EXIST_CODE";
        boolean isDeleted = grainService.isGrainDeletedCheck(nonExistentCode);
        assertFalse(isDeleted);
    }



    // 测试恢复被软删除的粮食信息的方法 - 类似前面恢复方法，覆盖不同情况
    @Test
    void testRestoreGrainByName() {
        String existingGrainName = "软恢复测试粮食";
        Grain grain = new Grain("SVC009", existingGrainName, "测试类型", 1.0, 10, "软恢复测试");
        assertDoesNotThrow(() -> {
            grainService.addGrain(grain);
            grainService.deleteGrain(grain.getGrainCode());
            grainService.restoreGrainByName(existingGrainName);
            Grain restoredGrain = grainService.findGrainByCode(grain.getGrainCode());
            assertNotNull(restoredGrain);
        });

        String nonExistentGrainName = "不存在软恢复粮食名";
        assertThrows(RuntimeException.class, () -> {
            grainService.restoreGrainByName(nonExistentGrainName);
        });
    }
}