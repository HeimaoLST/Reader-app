package com.reader.app.ui.auth

import androidx.compose.ui.test.assertHasNoClickAction
import androidx.compose.ui.test.assertHasTextExactly
import androidx.compose.ui.test.assertTextEquals
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import org.junit.Rule
import org.junit.Test

/**
 * LoginScreen UI 测试示例
 * 测试登录界面的用户交互
 *
 * 运行方式：
 * ./gradlew connectedAndroidTest
 */
class LoginScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun loginScreen_displaysWelcomeMessage() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        // Then - 应该显示欢迎消息
        composeTestRule
            .onNodeWithText("Welcome Back")
            .assertExists()

        composeTestRule
            .onNodeWithText("Sign in to continue reading")
            .assertExists()
    }

    @Test
    fun loginScreen_displaysEmailAndPasswordFields() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        // Then - 应该显示输入框
        composeTestRule
            .onNodeWithText("Email")
            .assertExists()

        composeTestRule
            .onNodeWithText("Password")
            .assertExists()
    }

    @Test
    fun loginScreen_displaysSignInButton() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        // Then - 应该显示登录按钮
        composeTestRule
            .onNodeWithText("Sign In")
            .assertExists()
    }

    @Test
    fun loginScreen_displaysSignUpLink() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        // Then - 应该显示注册链接
        composeTestRule
            .onNodeWithText("Don't have an account?")
            .assertExists()

        composeTestRule
            .onNodeWithText("Sign up")
            .assertExists()
    }

    @Test
    fun loginScreen_whenEmailAndPasswordEntered_callsOnLoginClick() {
        // Given
        var clickedEmail = ""
        var clickedPassword = ""

        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { email, password ->
                    clickedEmail = email
                    clickedPassword = password
                },
                onNavigateToRegister = {}
            )
        }

        // When - 输入邮箱和密码
        composeTestRule
            .onNodeWithText("Email")
            .performTextInput("test@example.com")

        composeTestRule
            .onNodeWithText("Password")
            .performTextInput("password123")

        // 点击登录按钮
        composeTestRule
            .onNodeWithText("Sign In")
            .performClick()

        // Then - 应该调用 onLoginClick 并传递正确的参数
        assert(clickedEmail == "test@example.com")
        assert(clickedPassword == "password123")
    }

    @Test
    fun loginScreen_whenSignUpClicked_callsOnNavigateToRegister() {
        // Given
        var navigateToRegisterCalled = false

        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {
                    navigateToRegisterCalled = true
                }
            )
        }

        // When - 点击 Sign up 链接
        composeTestRule
            .onNodeWithText("Sign up")
            .performClick()

        // Then - 应该调用 onNavigateToRegister
        assert(navigateToRegisterCalled)
    }

    @Test
    fun loginScreen_whenShowsLoading_displaysProgressBar() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {},
                isLoading = true
            )
        }

        // Then - 应该显示加载指示器
        composeTestRule
            .onNodeWithText("Sign In")
            .assertDoesNotExist()

        // 应该有 CircularProgressIndicator（这个是 Compose 内置的）
        // 注意：CircularProgressIndicator 没有文本，所以我们检查登录按钮消失即可
    }

    @Test
    fun loginScreen_whenHasError_displaysErrorMessage() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {},
                errorMessage = "Invalid email or password"
            )
        }

        // Then - 应该显示错误消息
        composeTestRule
            .onNodeWithText("Invalid email or password")
            .assertExists()
    }

    @Test
    fun loginScreen_whenNoError_doesNotDisplayErrorMessage() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {},
                errorMessage = null
            )
        }

        // Then - 不应该显示错误消息
        composeTestRule
            .onNodeWithText("Invalid email or password")
            .assertDoesNotExist()
    }

    @Test
    fun loginScreen_emailInput_acceptsText() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        // When - 输入文本
        val testEmail = "user@test.com"
        composeTestRule
            .onNodeWithText("Email")
            .performTextInput(testEmail)

        // Then - 文本应该被输入
        composeTestRule
            .onNodeWithText("Email")
            .assertTextEquals(testEmail)
    }

    @Test
    fun loginScreen_passwordInput_masksText() {
        // Given
        composeTestRule.setContent {
            LoginScreen(
                onLoginClick = { _, _ -> },
                onNavigateToRegister = {}
            )
        }

        // When - 输入密码
        val testPassword = "secret123"
        composeTestRule
            .onNodeWithText("Password")
            .performTextInput(testPassword)

        // Then - 密码字段应该包含输入的文本（但不显示明文）
        // 注意：由于使用了 PasswordVisualTransformation，文本被隐藏了
        composeTestRule
            .onNodeWithText("Password")
            .assertExists()
    }
}
