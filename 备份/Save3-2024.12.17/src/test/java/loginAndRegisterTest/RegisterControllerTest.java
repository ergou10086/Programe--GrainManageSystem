package loginAndRegisterTest;

import controller.RegisterController;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import view.RegisterView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RegisterControllerTest {
    private static RegisterController controller;
    private static RegisterView view;
    private static Method validateInputMethod;

    @BeforeAll
    static void setUp() {
        SwingUtilities.invokeLater(() -> {
            try {
                // 创建控制器
                controller = new RegisterController();

                // 获取RegisterView实例
                java.lang.reflect.Field viewField = RegisterController.class
                        .getDeclaredField("registerView");
                viewField.setAccessible(true);
                view = (RegisterView) viewField.get(controller);

                // 获取validateInput方法
                Class<?> registerListenerClass = Class.forName(
                        "controller.RegisterController$RegisterListener");
                validateInputMethod = registerListenerClass
                        .getDeclaredMethod("validateInput", String.class, String.class, String.class);
                validateInputMethod.setAccessible(true);
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
    class ValidationTests {
        @Test
        void testValidInput() {
            SwingUtilities.invokeLater(() -> {
                try {
                    Object registerListener = getRegisterListener();
                    boolean result = (boolean) validateInputMethod.invoke(
                            registerListener,
                            "validUser",
                            "password123",
                            "password123"
                    );
                    assertTrue(result);
                } catch (Exception e) {
                    fail("Validation test failed: " + e.getMessage());
                }
            });
        }

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {" ", "\t", "\n"})
        void testEmptyUsername(String username) {
            SwingUtilities.invokeLater(() -> {
                try {
                    Object registerListener = getRegisterListener();
                    boolean result = (boolean) validateInputMethod.invoke(
                            registerListener,
                            username,
                            "password123",
                            "password123"
                    );
                    assertFalse(result);
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
                    Object registerListener = getRegisterListener();
                    boolean result = (boolean) validateInputMethod.invoke(
                            registerListener,
                            "testUser",
                            password,
                            password
                    );
                    assertFalse(result);
                } catch (Exception e) {
                    fail("Empty password test failed: " + e.getMessage());
                }
            });
        }

        @Test
        void testPasswordMismatch() {
            SwingUtilities.invokeLater(() -> {
                try {
                    Object registerListener = getRegisterListener();
                    boolean result = (boolean) validateInputMethod.invoke(
                            registerListener,
                            "testUser",
                            "password123",
                            "password456"
                    );
                    assertFalse(result);
                } catch (Exception e) {
                    fail("Password mismatch test failed: " + e.getMessage());
                }
            });
        }

        @ParameterizedTest
        @ValueSource(strings = {"12345", "12345678901234567"})
        void testInvalidPasswordLength(String password) {
            SwingUtilities.invokeLater(() -> {
                try {
                    Object registerListener = getRegisterListener();
                    boolean result = (boolean) validateInputMethod.invoke(
                            registerListener,
                            "testUser",
                            password,
                            password
                    );
                    assertFalse(result);
                } catch (Exception e) {
                    fail("Password length test failed: " + e.getMessage());
                }
            });
        }
    }

    @Nested
    @Order(2)
    class RegisterTests {
        @Test
        void testSuccessfulRegister() {
            SwingUtilities.invokeLater(() -> {
                try {
                    setInputFields("newUser", "password123", "password123", "普通用户");
                    simulateRegisterAction();
                    assertFalse(view.isVisible());
                } catch (Exception e) {
                    fail("Register test failed: " + e.getMessage());
                }
            });
        }

        @Test
        void testRegisterWithExistingUsername() {
            SwingUtilities.invokeLater(() -> {
                try {
                    setInputFields("existingUser", "password123", "password123", "普通用户");
                    simulateRegisterAction();
                    assertTrue(view.isVisible());
                } catch (Exception e) {
                    fail("Existing username test failed: " + e.getMessage());
                }
            });
        }
    }

    // 辅助方法
    private static Object getRegisterListener() throws Exception {
        java.lang.reflect.Field listenerField = RegisterView.class
                .getDeclaredField("registerListener");
        listenerField.setAccessible(true);
        return listenerField.get(view);
    }

    private void setInputFields(String username, String password,
                                String confirmPassword, String role) throws Exception {
        java.lang.reflect.Method setUsernameMethod = RegisterView.class
                .getDeclaredMethod("setUsername", String.class);
        java.lang.reflect.Method setPasswordMethod = RegisterView.class
                .getDeclaredMethod("setPassword", String.class);
        java.lang.reflect.Method setConfirmPasswordMethod = RegisterView.class
                .getDeclaredMethod("setConfirmPassword", String.class);
        java.lang.reflect.Method setRoleMethod = RegisterView.class
                .getDeclaredMethod("setSelectedRole", String.class);

        setUsernameMethod.setAccessible(true);
        setPasswordMethod.setAccessible(true);
        setConfirmPasswordMethod.setAccessible(true);
        setRoleMethod.setAccessible(true);

        setUsernameMethod.invoke(view, username);
        setPasswordMethod.invoke(view, password);
        setConfirmPasswordMethod.invoke(view, confirmPassword);
        setRoleMethod.invoke(view, role);
    }

    private void simulateRegisterAction() throws Exception {
        Object registerListener = getRegisterListener();
        ActionEvent event = new ActionEvent(view, ActionEvent.ACTION_PERFORMED, "register");
        registerListener.getClass().getMethod("actionPerformed", ActionEvent.class)
                .invoke(registerListener, event);
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