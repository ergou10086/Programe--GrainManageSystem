package grainInfManageTest;

import entity.Grain;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

// 针对Grain的实体类的测试
public class GrainTest {
    private Grain grain;

    @BeforeEach
    public void setUp() {
        grain = new Grain("G001", "小麦", "粮食", 2.5, 12, "正常情况的测试");
    }

    // 对Grain的实体类中构造函数和getter的测试
    // 正常情况的测试
    @Test
    void testConstructorAndGetters() {
        assertEquals("G001", grain.getGrainCode());
        assertEquals("小麦", grain.getGrainName());
        assertEquals("粮食", grain.getGrainType());
        assertEquals(2.5, grain.getGrainPrice());
        assertEquals(12, grain.getGrainShelfLife());
        assertEquals("正常情况的测试", grain.getGrainRemark());
    }

    // 非正常情况，空字符串
    @Test
    void testConstructorWithEmptyString() {
        grain = new Grain("", "", "", 0, 0, "");
        assertEquals("", grain.getGrainCode());
        assertEquals("", grain.getGrainName());
        assertEquals("", grain.getGrainType());
        assertEquals(0, grain.getGrainPrice());
        assertEquals(0, grain.getGrainShelfLife());
        assertEquals("", grain.getGrainRemark());
    }

    // 测试获得粮食id情况
    @Test
    void testIdGrain() {
        String id = "test-id";
        grain.setIdGrain(id);
        assertEquals(id, grain.getIdGrain());
    }

    // 空字符串情况
    @Test
    void testIdGrainWithEmptyString() {
        grain.setIdGrain("");
        assertEquals("", grain.getIdGrain());
    }


    // 测试setter
    @Test
    void testSetters() {
        grain.setGrainCode("G002");
        grain.setGrainName("玉米");
        grain.setGrainType("谷物");
        grain.setGrainPrice(3.0);
        grain.setGrainShelfLife(24);
        grain.setGrainRemark("新备注，正常情况");

        assertEquals("G002", grain.getGrainCode());
        assertEquals("玉米", grain.getGrainName());
        assertEquals("谷物", grain.getGrainType());
        assertEquals(3.0, grain.getGrainPrice());
        assertEquals(24, grain.getGrainShelfLife());
        assertEquals("新备注，正常情况", grain.getGrainRemark());
    }

    // 空串和null情况
    @Test
    void testSettersWithEmptyStringAndNull() {
        grain.setGrainCode("");
        grain.setGrainName(null);
        grain.setGrainType("");
        grain.setGrainPrice(0);
        grain.setGrainShelfLife(0);
        grain.setGrainRemark(null);
        assertEquals("", grain.getGrainCode());
        assertNull(grain.getGrainName());
        assertEquals("", grain.getGrainType());
        assertEquals(0, grain.getGrainPrice());
        assertEquals(0, grain.getGrainShelfLife());
        assertNull(grain.getGrainRemark());
    }
}
