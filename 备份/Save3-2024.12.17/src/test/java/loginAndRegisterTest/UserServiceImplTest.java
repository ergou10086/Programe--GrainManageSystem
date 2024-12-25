package loginAndRegisterTest;

import entity.User;
import exceptions.ServiceException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import service.impl.UserServiceImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserServiceImplTest {
    private static UserServiceImpl userService;
    private static final String TEST_USERNAME = "testUser";
    private static final String TEST_PASSWORD = "testPass123";
    private static final String TEST_ROLE = "普通用户";

    @BeforeAll
    static void setUp() {
        userService = new UserServiceImpl();
    }

    // 注册测试组
    @Nested
    @Order(1)
    class RegisterTests {
        @Test
        void testRegisterSuccess() {
            assertDoesNotThrow(() -> {
                boolean result = userService.register(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE);
                assertTrue(result, "用户注册应该成功");
            });
        }

        @Test
        void testRegisterDuplicateUsername() {
            assertThrows(RuntimeException.class, () ->
                            userService.register(TEST_USERNAME, TEST_PASSWORD, TEST_ROLE),
                    "重复注册应该抛出异常"
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        void testRegisterInvalidUsername(String username) {
            assertThrows(RuntimeException.class, () ->
                            userService.register(username, TEST_PASSWORD, TEST_ROLE),
                    "无效用户名应该抛出异常"
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void testRegisterInvalidPassword(String password) {
            assertThrows(RuntimeException.class, () ->
                            userService.register(TEST_USERNAME + "1", password, TEST_ROLE),
                    "无效密码应该抛出异常"
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        void testRegisterInvalidRole(String role) {
            assertThrows(RuntimeException.class, () ->
                            userService.register(TEST_USERNAME + "2", TEST_PASSWORD, role),
                    "无效角色应该抛出异常"
            );
        }
    }

    // 登录测试组
    @Nested
    @Order(2)
    class LoginTests {
        @Test
        void testLoginSuccess() {
            assertDoesNotThrow(() -> {
                User user = userService.login(TEST_USERNAME, TEST_PASSWORD);
                assertNotNull(user, "登录应该成功");
                assertEquals(TEST_USERNAME, user.getUsername());
                assertEquals(TEST_ROLE, user.getRole());
            });
        }

        @Test
        void testLoginWrongPassword() {
            assertThrows(ServiceException.class, () ->
                            userService.login(TEST_USERNAME, "wrongPassword"),
                    "错误密码应该抛出异常"
            );
        }

        @Test
        void testLoginNonexistentUser() {
            assertThrows(ServiceException.class, () ->
                            userService.login("nonexistentUser", TEST_PASSWORD),
                    "不存在的用户应该抛出异常"
            );
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   ", "\t", "\n"})
        void testLoginInvalidUsername(String username) {
            assertThrows(ServiceException.class, () ->
                            userService.login(username, TEST_PASSWORD),
                    "无效用户名应该抛出异常"
            );
        }
    }

    // 用户信息更新测试组
    @Nested
    @Order(3)
    class UpdateTests {
        @Test
        void testUpdateUserInfoSuccess() {
            assertDoesNotThrow(() -> {
                boolean result = userService.updateUserInfo(
                        TEST_USERNAME,
                        TEST_ROLE,
                        "newTestUser",
                        "高级用户"
                );
                assertTrue(result, "更新用户信息应该成功");
            });
        }

        @Test
        void testUpdatePasswordSuccess() {
            String newPassword = "newPass123";
            assertDoesNotThrow(() -> {
                boolean result = userService.updatePassword(
                        "newTestUser",
                        TEST_PASSWORD,
                        newPassword
                );
                assertTrue(result, "更新密码应该成功");
            });
        }

        @Test
        void testUpdatePasswordWrongOld() {
            assertThrows(ServiceException.class, () ->
                            userService.updatePassword(
                                    "newTestUser",
                                    "wrongPassword",
                                    "newPass"
                            ),
                    "错误的原密码应该抛出异常"
            );
        }
    }

    // 查询测试组
    @Nested
    @Order(4)
    class QueryTests {
        @Test
        void testGetAllUsers() {
            assertDoesNotThrow(() -> {
                List<User> users = userService.getAllUsers();
                assertNotNull(users, "用户列表不应为空");
                assertFalse(users.isEmpty(), "用户列表应该包含用户");
            });
        }

        @Test
        void testSearchUsersWithKeyword() {
            assertDoesNotThrow(() -> {
                List<User> users = userService.searchUsers("new");
                assertNotNull(users, "搜索结果不应为空");
                assertTrue(users.stream()
                                .anyMatch(user -> user.getUsername().equals("newTestUser")),
                        "搜索结果应该包含目标用户");
            });
        }

        @Test
        void testSearchUsersEmptyKeyword() {
            assertDoesNotThrow(() -> {
                List<User> users = userService.searchUsers("");
                assertNotNull(users, "空关键字搜索结果不应为空");
                assertFalse(users.isEmpty(), "空关键字应返回所有用户");
            });
        }
    }

    // 删除测试组
    @Nested
    @Order(5)
    class DeleteTests {
        @Test
        void testDeleteUserSuccess() {
            assertDoesNotThrow(() -> {
                boolean result = userService.deleteUser("newTestUser", "高级用户");
                assertTrue(result, "删除用户应该成功");
            });
        }

        @Test
        void testDeleteNonexistentUser() {
            assertDoesNotThrow(() -> {
                boolean result = userService.deleteUser("nonexistentUser", TEST_ROLE);
                assertFalse(result, "删除不存在的用户应该返回false");
            });
        }
    }

    // 用户名存在检查测试组
    @Nested
    @Order(6)
    class ExistenceTests {
        @Test
        void testIsUsernameExists() {
            assertDoesNotThrow(() -> {
                assertFalse(userService.isUsernameExists("newTestUser"),
                        "已删除的用户名不应存在");
                assertFalse(userService.isUsernameExists("nonexistentUser"),
                        "不存在的用户名应返回false");
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        void testIsUsernameExistsInvalid(String username) {
            assertThrows(ServiceException.class, () ->
                            userService.isUsernameExists(username),
                    "无效用户名应该抛出异常"
            );
        }
    }
}
