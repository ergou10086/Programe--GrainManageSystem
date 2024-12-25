package loginAndRegisterTest;

import dao.impl.UserDAOImpl;
import entity.User;
import exceptions.DAOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {
    private static UserDAOImpl userDAO;
    private static User testUser;

    @BeforeAll
    static void setUp() {
        userDAO = new UserDAOImpl();
        testUser = new User();
        testUser.setUsername("testUser");
        testUser.setPassword("testPass123");
        testUser.setRole("普通用户");
    }

    // 测试插入用户 - 正常情况
    @Test
    @Order(1)
    void testInsert() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.insert(testUser);
            assertTrue(result, "用户插入应该成功");
        });
    }

    // 测试插入用户 - 异常情况
    @Test
    @Order(2)
    void testInsertDuplicate() {
        User duplicateUser = new User("testUser", "anotherPass", "普通用户");
        assertThrows(DAOException.class, () -> userDAO.insert(duplicateUser));
    }

    // 测试查找用户 - 参数化测试
    @ParameterizedTest
    @ValueSource(strings = {"testUser", "nonExistentUser"})
    @Order(3)
    void testFindByUsername(String username) {
        assertDoesNotThrow(() -> {
            User user = userDAO.findByUsername(username);
            if (username.equals("testUser")) {
                assertNotNull(user);
                assertEquals(username, user.getUsername());
            } else {
                assertNull(user);
            }
        });
    }

    // 测试查找用户 - 无效输入
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @Order(4)
    void testFindByUsernameInvalid(String username) {
        assertThrows(DAOException.class, () -> userDAO.findByUsername(username));
    }

    // 测试更新用户信息
    @Test
    @Order(5)
    void testUpdate() {
        assertDoesNotThrow(() -> {
            User user = userDAO.findByUsername("testUser");
            assertNotNull(user);
            user.setPassword("newPass123");
            user.setRole("管理员");
            boolean result = userDAO.update(user);
            assertTrue(result);

            User updatedUser = userDAO.findByUsername("testUser");
            assertEquals("newPass123", updatedUser.getPassword());
            assertEquals("管理员", updatedUser.getRole());
        });
    }

    // 测试用户名存在检查
    @ParameterizedTest
    @MethodSource("usernameExistsParams")
    @Order(6)
    void testIsUsernameExists(String username, boolean expected) {
        assertDoesNotThrow(() -> {
            boolean exists = userDAO.isUsernameExists(username);
            assertEquals(expected, exists);
        });
    }

    private static Stream<Arguments> usernameExistsParams() {
        return Stream.of(
                Arguments.of("testUser", true),
                Arguments.of("nonExistentUser", false)
        );
    }

    // 测试获取所有用户
    @Test
    @Order(7)
    void testGetAllUsers() {
        assertDoesNotThrow(() -> {
            List<User> users = userDAO.getAllUsers();
            assertNotNull(users);
            assertFalse(users.isEmpty());
            assertTrue(users.stream()
                    .anyMatch(user -> user.getUsername().equals("testUser")));
        });
    }

    // 测试更新用户信息
    @Test
    @Order(8)
    void testUpdateUserInfo() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.updateUserInfo("testUser", "updatedTestUser", "高级用户");
            assertTrue(result);

            User updatedUser = userDAO.findByUsername("updatedTestUser");
            assertNotNull(updatedUser);
            assertEquals("高级用户", updatedUser.getRole());
        });
    }

    // 测试密码验证
    @Test
    @Order(9)
    void testValidatePassword() {
        assertDoesNotThrow(() -> {
            assertTrue(userDAO.validatePassword("updatedTestUser", "newPass123"));
            assertFalse(userDAO.validatePassword("updatedTestUser", "wrongPass"));
        });
    }

    // 测试搜索用户
    @Test
    @Order(10)
    void testSearchUsers() {
        assertDoesNotThrow(() -> {
            List<User> results = userDAO.searchUsers("updated");
            assertNotNull(results);
            assertFalse(results.isEmpty());
            assertTrue(results.stream()
                    .anyMatch(user -> user.getUsername().equals("updatedTestUser")));
        });
    }

    // 测试搜索用户 - 边界情况
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @Order(11)
    void testSearchUsersInvalid(String keyword) {
        assertDoesNotThrow(() -> {
            List<User> results = userDAO.searchUsers(keyword);
            assertNotNull(results);
        });
    }

    // 测试删除用户
    @Test
    @Order(12)
    void testDeleteUser() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.deleteUser("updatedTestUser");
            assertTrue(result);
            assertNull(userDAO.findByUsername("updatedTestUser"));
        });
    }

    // 测试删除不存在的用户
    @Test
    @Order(13)
    void testDeleteNonExistentUser() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.deleteUser("nonExistentUser");
            assertFalse(result);
        });
    }
}
