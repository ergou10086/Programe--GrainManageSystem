package loginAndRegisterTest;

import controller.LoginController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import view.LoginView;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class LoginControllerTest {
    private static LoginController controller;
    private static LoginView view;
    private static Method handleLoginMethod;
    private static Method handleRegisterMethod;

    @BeforeAll
    static void setUp() {
        SwingUtilities.invokeLater(() -> {
            try {
                // 创建控制器
                controller = new LoginController();

                // 获取LoginView实例
                Field viewField = LoginController.class.getDeclaredField("loginView");
                viewField.setAccessible(true);
                view = (LoginView) viewField.get(controller);

                // 获取私有方法
                handleLoginMethod = LoginController.class.getDeclaredMethod("handleLogin");
                handleRegisterMethod = LoginController.class.getDeclaredMethod("handleRegister");
                handleLoginMethod.setAccessible(true);
                handleRegisterMethod.setAccessible(true);
            } catch (Exception e) {
                fail("Test setup failed: " + e.getMessage());
            }
        });

        try {
            Thread.sleep(100); // 等待EDT初始化完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nested
    @Order(1)
    class LoginTests {
        @Test
        void testValidLogin() {
            SwingUtilities.invokeLater(() -> {
                try {
                    setLoginFields("validUser", "validPass");
                    handleLoginMethod.invoke(controller);
                    assertFalse(view.isVisible());
                } catch (Exception e) {
                    fail("Valid login test failed: " + e.getMessage());
                }
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n"})
        void testEmptyUsername(String username) {
            SwingUtilities.invokeLater(() -> {
                try {
                    setLoginFields(username, "validPass");
                    handleLoginMethod.invoke(controller);
                    assertTrue(view.isVisible());
                } catch (Exception e) {
                    fail("Empty username test failed: " + e.getMessage());
                }
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n"})
        void testEmptyPassword(String password) {
            SwingUtilities.invokeLater(() -> {
                try {
                    setLoginFields("validUser", password);
                    handleLoginMethod.invoke(controller);
                    assertTrue(view.isVisible());
                } catch (Exception e) {
                    fail("Empty password test failed: " + e.getMessage());
                }
            });
        }

        @Test
        void testInvalidCredentials() {
            SwingUtilities.invokeLater(() -> {
                try {
                    setLoginFields("invalidUser", "invalidPass");
                    handleLoginMethod.invoke(controller);
                    assertTrue(view.isVisible());
                } catch (Exception e) {
                    fail("Invalid credentials test failed: " + e.getMessage());
                }
            });
        }

        @Test
        void testEnterKeyPress() {
            SwingUtilities.invokeLater(() -> {
                try {
                    setLoginFields("validUser", "validPass");
                    KeyEvent enterKeyEvent = new KeyEvent(
                            view,
                            KeyEvent.KEY_PRESSED,
                            System.currentTimeMillis(),
                            0,
                            KeyEvent.VK_ENTER,
                            KeyEvent.CHAR_UNDEFINED
                    );
                    view.dispatchEvent(enterKeyEvent);
                } catch (Exception e) {
                    fail("Enter key test failed: " + e.getMessage());
                }
            });
        }
    }

    @Nested
    @Order(2)
    class RegisterNavigationTests {
        @Test
        void testRegisterNavigation() {
            SwingUtilities.invokeLater(() -> {
                try {
                    handleRegisterMethod.invoke(controller);
                    assertFalse(view.isVisible());
                } catch (Exception e) {
                    fail("Register navigation test failed: " + e.getMessage());
                }
            });
        }
    }

    @Nested
    @Order(3)
    class LoginSuccessListenerTests {
        @Test
        void testLoginSuccessListener() {
            SwingUtilities.invokeLater(() -> {
                final boolean[] listenerCalled = {false};
                controller.addLoginSuccessListener((username, role) ->
                        listenerCalled[0] = true
                );

                try {
                    setLoginFields("validUser", "validPass");
                    handleLoginMethod.invoke(controller);
                    assertTrue(listenerCalled[0]);
                } catch (Exception e) {
                    fail("Login success listener test failed: " + e.getMessage());
                }
            });
        }
    }

    // 辅助方法
    private void setLoginFields(String username, String password) throws Exception {
        Method setUsernameMethod = LoginView.class.getDeclaredMethod("setUsername", String.class);
        Method setPasswordMethod = LoginView.class.getDeclaredMethod("setPassword", String.class);

        setUsernameMethod.setAccessible(true);
        setPasswordMethod.setAccessible(true);

        setUsernameMethod.invoke(view, username);
        setPasswordMethod.invoke(view, password);
    }

    @AfterAll
    static void tearDown() {
        SwingUtilities.invokeLater(() -> {
            if (view != null) {
                view.dispose();
            }
        });
    }
}
