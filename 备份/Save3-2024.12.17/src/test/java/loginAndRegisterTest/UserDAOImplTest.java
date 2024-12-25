package loginAndRegisterTest;

import dao.impl.UserDAOImpl;
import entity.User;
import exceptions.DAOException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOImplTest {
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

    // 测试插入用户
    @Test
    @Order(1)
    void testInsert() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.insert(testUser);
            assertTrue(result, "用户插入应该成功");
        });
    }

    // 测试插入重复用户名
    @Test
    @Order(2)
    void testInsertDuplicate() {
        User duplicateUser = new User();
        duplicateUser.setUsername("testUser");
        duplicateUser.setPassword("anotherPass");
        duplicateUser.setRole("普通用户");

        Exception exception = assertThrows(DAOException.class, () ->
                userDAO.insert(duplicateUser)
        );
        assertTrue(exception.getMessage().contains("用户名已存在"));
    }

    // 测试根据用户名查找用户
    @Test
    @Order(3)
    void testFindByUsername() {
        assertDoesNotThrow(() -> {
            User foundUser = userDAO.findByUsername("testUser");
            assertNotNull(foundUser, "应该能找到插入的用户");
            assertEquals(testUser.getUsername(), foundUser.getUsername(), "用户名应该匹配");
            assertEquals(testUser.getPassword(), foundUser.getPassword(), "密码应该匹配");
            assertEquals(testUser.getRole(), foundUser.getRole(), "角色应该匹配");
        });
    }

    // 测试查找不存在的用户
    @Test
    @Order(4)
    void testFindNonExistentUser() {
        assertDoesNotThrow(() -> {
            User foundUser = userDAO.findByUsername("nonExistentUser");
            assertNull(foundUser, "不应该找到不存在的用户");
        });
    }

    // 参数化测试用户名存在检查
    @ParameterizedTest
    @Order(5)
    @ValueSource(strings = {"testUser", "nonExistentUser"})
    void testIsUsernameExists(String username) {
        assertDoesNotThrow(() -> {
            boolean exists = userDAO.isUsernameExists(username);
            if (username.equals("testUser")) {
                assertTrue(exists, "用户名应该存在");
            } else {
                assertFalse(exists, "用户名不应该存在");
            }
        });
    }

    // 测试更新用户信息
    @Test
    @Order(6)
    void testUpdateUserInfo() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.updateUserInfo("testUser", "updatedTestUser", "管理员");
            assertTrue(result, "用户信息更新应该成功");

            User updatedUser = userDAO.findByUsername("updatedTestUser");
            assertNotNull(updatedUser, "应该能找到更新后的用户");
            assertEquals("管理员", updatedUser.getRole(), "角色应该已更新");

            // 将用户名改回来以便后续测试
            userDAO.updateUserInfo("updatedTestUser", "testUser", "管理员");
        });
    }

    // 测试更新密码
    @Test
    @Order(7)
    void testUpdatePassword() {
        assertDoesNotThrow(() -> {
            String newPassword = "newPass456";
            boolean result = userDAO.updatePassword("testUser", newPassword);
            assertTrue(result, "密码更新应该成功");

            User updatedUser = userDAO.findByUsername("testUser");
            assertNotNull(updatedUser, "应该能找到用户");
            assertEquals(newPassword, updatedUser.getPassword(), "密码应该已更新");
        });
    }

    // 测试密码验证
    @Test
    @Order(8)
    void testValidatePassword() {
        assertDoesNotThrow(() -> {
            boolean validResult = userDAO.validatePassword("testUser", "newPass456");
            assertTrue(validResult, "正确密码应该验证通过");

            boolean invalidResult = userDAO.validatePassword("testUser", "wrongPass");
            assertFalse(invalidResult, "错误密码不应该验证通过");
        });
    }

    // 测试搜索用户
    @Test
    @Order(9)
    void testSearchUsers() {
        assertDoesNotThrow(() -> {
            List<User> searchResults = userDAO.searchUsers("test");
            assertFalse(searchResults.isEmpty(), "搜索结果不应该为空");
            assertTrue(searchResults.stream()
                            .anyMatch(user -> user.getUsername().equals("testUser")),
                    "应该能在搜索结果中找到测试用户");
        });
    }

    // 测试空或null关键字搜索
    @ParameterizedTest
    @Order(10)
    @NullAndEmptySource
    void testSearchUsersWithInvalidInput(String keyword) {
        assertDoesNotThrow(() -> {
            List<User> results = userDAO.searchUsers(keyword);
            assertNotNull(results, "搜索结果列表不应该为null");
        });
    }

    // 测试删除用户
    @Test
    @Order(11)
    void testDeleteUser() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.deleteUser("testUser");
            assertTrue(result, "用户删除应该成功");

            User deletedUser = userDAO.findByUsername("testUser");
            assertNull(deletedUser, "删除后应该找不到用户");
        });
    }

    // 测试删除不存在的用户
    @Test
    @Order(12)
    void testDeleteNonExistentUser() {
        assertDoesNotThrow(() -> {
            boolean result = userDAO.deleteUser("nonExistentUser");
            assertFalse(result, "删除不存在的用户应该返回false");
        });
    }
}
