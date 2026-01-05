# Android 应用测试指南

本文档提供了完整的测试说明，帮助你运行和编写测试。

## 测试概览

### 已创建的测试

#### 1. 单元测试 (Unit Tests)
位置: `app/src/test/java/com/reader/app/`

- `utils/DateUtilsTest.kt` - 日期工具类测试
- `ui/home/HomeViewModelTest.kt` - 首页 ViewModel 测试

#### 2. 集成测试 (Integration Tests)
位置: `app/src/androidTest/java/com/reader/app/`

- `data/local/dao/UrlDaoTest.kt` - 数据库 DAO 测试

#### 3. UI 测试 (UI Tests)
位置: `app/src/androidTest/java/com/reader/app/`

- `ui/auth/LoginScreenTest.kt` - 登录界面 UI 测试

## 运行测试

### 前置条件

首先需要生成 Gradle wrapper（如果还没有）：

```bash
# 在项目根目录
gradle wrapper --gradle-version 8.0
```

或者使用 Android Studio：
- 打开 Android Studio
- File → Settings → Build, Execution, Deployment → Gradle
- 选择 "Use Gradle from": Gradle wrapper

### 1. 运行所有单元测试

单元测试不需要 Android 设备或模拟器，可以在 JVM 上运行：

```bash
./gradlew test
# 或者只运行 debug 版本的测试
./gradlew testDebugUnitTest

# 查看测试报告
open app/build/reports/tests/testDebugUnitTest/index.html
```

### 2. 运行集成测试

集成测试需要 Android 设备或模拟器：

```bash
# 首先启动模拟器或连接设备
adb devices

# 运行集成测试
./gradlew connectedAndroidTest
# 或者
./gradlew connectedDebugAndroidTest

# 查看测试报告
open app/build/reports/androidTests/connected/index.html
```

### 3. 运行 UI 测试

UI 测试包含在集成测试中：

```bash
./gradlew connectedDebugAndroidTest
```

### 4. 运行特定的测试类

```bash
# 单元测试
./gradlew test --tests "com.reader.app.utils.DateUtilsTest"

# 集成测试
./gradlew connectedDebugAndroidTest --tests "com.reader.app.ui.auth.LoginScreenTest"
```

### 5. 在 Android Studio 中运行测试

1. **运行单个测试方法**:
   - 打开测试文件
   - 点击方法名旁边的绿色运行按钮

2. **运行整个测试类**:
   - 打开测试文件
   - 点击类名旁边的绿色运行按钮

3. **运行所有测试**:
   - 右键点击测试目录
   - 选择 "Run Tests in..."

## 测试覆盖率

### 生成覆盖率报告

```bash
# 添加 JaCoCo 插件到 build.gradle.kts
./gradlew testDebugUnitTestCoverage
```

然后在 Android Studio 中查看：
- Run → Show Coverage

## 手动测试

### 构建 APK

```bash
# Debug 版本
./gradlew assembleDebug

# Release 版本
./gradlew assembleRelease

# APK 位置
ls app/build/outputs/apk/debug/app-debug.apk
ls app/build/outputs/apk/release/app-release.apk
```

### 安装到设备

```bash
# 安装 debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# 或者直接运行
./gradlew installDebug
```

### 手动测试清单

#### 核心功能
- [ ] 用户注册
- [ ] 用户登录
- [ ] 添加 URL
- [ ] 查看 URL 列表
- [ ] 打开阅读器
- [ ] 标记已读/未读
- [ ] 收藏/取消收藏
- [ ] 归档/删除 URL
- [ ] 搜索功能

#### 边界情况
- [ ] 网络错误处理
- [ ] 无效 URL 输入
- [ ] 空 URL 列表状态
- [ ] 登录失败
- [ ] Token 过期

#### 性能测试
- [ ] 大量 URL 时的列表性能
- [ ] 阅读器加载速度
- [ ] 图片加载性能
- [ ] 内存使用情况

## 测试最佳实践

### 编写单元测试

```kotlin
@Test
fun `test name should describe what is being tested`() {
    // Given - 准备测试数据
    val input = "some input"

    // When - 执行被测试的操作
    val result = functionUnderTest(input)

    // Then - 验证结果
    assertEquals("expected", result)
}
```

### 编写 UI 测试

```kotlin
@Test
fun uiComponent_performsAction() {
    // Given - 设置 UI
    composeTestRule.setContent {
        MyComponent()
    }

    // When - 执行 UI 操作
    composeTestRule.onNodeWithText("Button")
        .performClick()

    // Then - 验证结果
    composeTestRule.onNodeWithText("Success")
        .assertExists()
}
```

## 持续集成

### GitHub Actions 示例

```yaml
name: Android CI

on: [push, pull_request]

jobs:
  test:
    runs-on: macos-latest
    steps:
      - uses: actions/checkout@v3
      - name: Set up JDK
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      - name: Run unit tests
        run: ./gradlew testDebugUnitTest
      - name: Run instrumented tests
        uses: reactivecircus/android-emulator-runner@v2
        with:
          api-level: 29
          script: ./gradlew connectedAndroidTest
```

## 常见问题

### 1. 测试失败：No tests found

确保测试文件位于正确的目录：
- 单元测试: `app/src/test/`
- 集成测试: `app/src/androidTest/`

### 2. 测试运行超时

对于 UI 测试，确保模拟器性能足够或使用真机。

### 3. MockK 错误

确保使用正确的 MockK 版本：
```kotlin
testImplementation("io.mockk:mockk:1.13.8")
```

### 4. Kotlin Coroutines 测试

使用 `UnconfinedTestDispatcher` 或 `runTest`:
```kotlin
@Test
fun myTest() = runTest {
    // 测试代码
}
```

## 下一步

1. ✅ 单元测试已完成
2. ✅ 集成测试已完成
3. ✅ UI 测试已完成
4. ⏳ 运行所有测试
5. ⏳ 构建 APK 进行手动测试
6. 📝 根据需要添加更多测试

## 资源链接

- [Android Testing Guide](https://developer.android.com/training/testing)
- [Jetpack Compose Testing](https://developer.android.com/jetpack/compose/testing)
- [MockK Documentation](https://mockk.io/)
- [Turbine (Flow Testing)](https://cashapp.github.io/turbine/)
