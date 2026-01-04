# App 端功能与设计文档

## 项目概述

智能 URL 管理应用的移动端（iOS/Android）原生开发设计文档，基于 Go 后端 API 实现跨平台的链接收集、管理和阅读功能。

**后端 API 基础 URL**: `https://api.yourdomain.com/api/v1`

---

## 一、核心功能列表

### 1.1 用户认证
- 邮箱密码注册/登录
- Google/GitHub OAuth 登录
- Token 自动刷新机制
- 用户信息管理

### 1.2 URL 收集
- **剪贴板监听**: 后台自动检测剪贴板中的 URL
- **手动添加**: 输入框或粘贴板添加
- **批量添加**: 支持一次添加多个链接
- **快速操作**: 从其他应用分享链接到 App

### 1.3 URL 管理
- 查看 URL 列表（全部/未读/收藏/归档）
- 标记已读/未读
- 收藏/取消收藏
- 归档/删除
- 编辑标题和描述
- 添加标签和分类

### 1.4 阅读器模式
- **优质阅读体验**: 清理后的 HTML 内容
- **图片支持**: 保留所有图片，懒加载优化
- **自定义阅读设置**:
  - 字体大小调节（16px - 24px）
  - 深色/浅色主题切换
  - 字体类型选择
- **阅读进度保存**: 自动同步滚动位置

### 1.5 智能分类
- **自动分类**: 按域名/公司自动聚合
- **收藏夹管理**: 创建自定义收藏夹
- **Timeline 视图**: 按时间线展示
- **标签系统**: 手动添加标签

### 1.6 多端同步
- **增量同步**: 基于时间戳的高效同步
- **冲突解决**: 服务端优先策略
- **离线支持**: 本地缓存，离线可浏览

### 1.7 搜索功能
- **全文搜索**: 标题、描述、URL
- **过滤器**: 按域名、分类、标签筛选
- **搜索历史**: 保存最近搜索

---

## 二、主要页面/模块设计

### 2.1 应用结构

```
App/
├── 认证模块
│   ├── 登录页 (Login)
│   ├── 注册页 (Register)
│   └── OAuth 回调页
│
├── 主页面 (Tab Bar)
│   ├── 首页 (Timeline)
│   ├── 收藏夹 (Collections)
│   ├── 搜索 (Search)
│   └── 设置 (Settings)
│
├── URL 列表页
│   ├── 全部 URLs
│   ├── 未读列表
│   ├── 收藏列表
│   └── 归档列表
│
├── 阅读器页面
│   ├── 文章内容展示
│   ├── 工具栏 (收藏、分享、归档)
│   └── 阅读设置面板
│
├── 收藏夹管理
│   ├── 收藏夹列表
│   ├── 收藏夹详情
│   └── 创建/编辑收藏夹
│
├── 剪贴板监听服务
│   └── 后台监听和提示
│
└── 设置页面
    ├── 账户信息
    ├── 同步设置
    └── 阅读偏好设置
```

### 2.2 核心页面设计

#### 页面 1: 首页 (Timeline)
**功能**: 展示按时间排序的所有链接

**UI 元素**:
- 顶部搜索栏
- 筛选器（全部/未读/收藏）
- URL 列表卡片
  - 标题
  - 域名/公司标识
  - 摘要
  - 收藏时间
  - 已读/未读状态标识
  - 收藏星标
- 底部 Tab Bar
- 悬浮添加按钮（FAB）

**交互**:
- 下拉刷新同步
- 上拉加载更多
- 左滑删除/归档
- 点击进入阅读器
- 长按显示操作菜单

#### 页面 2: 阅读器 (Reader)
**功能**: 沉浸式阅读体验

**UI 元素**:
- 顶部导航栏
  - 返回按钮
  - 分享按钮
  - 更多操作（收藏、归档、删除）
- 文章元信息
  - 标题
  - 作者、来源网站
  - 发布时间
  - 阅读时长
- 文章正文（HTML 渲染）
  - 图片（懒加载）
  - 代码块
  - 引用块
- 底部工具栏
  - 字体大小调节
  - 主题切换
  - 阅读进度条

**交互**:
- 滚动保存阅读进度
- 图片点击放大查看
- 长按文章选择文本
- 双击顶部/底部快速滚动

#### 页面 3: 收藏夹列表 (Collections)
**功能**: 管理分类收藏夹

**UI 元素**:
- 收藏夹网格/列表
  - 图标
  - 名称
  - URL 数量
  - 类型标识（手动/自动）
- 新建收藏夹按钮

**交互**:
- 点击进入收藏夹详情
- 长按编辑/删除
- 拖拽排序

#### 页面 4: 收藏夹详情 (Collection Detail)
**功能**: 查看收藏夹内的所有 URL

**UI 元素**:
- 收藏夹信息（名称、描述）
- URL 列表（同首页）
- 底部添加 URL 按钮

**交互**:
- 从其他页面添加 URL 到收藏夹
- 长按移除 URL

#### 页面 5: 搜索页 (Search)
**功能**: 全文搜索和高级筛选

**UI 元素**:
- 搜索输入框
- 筛选器
  - 域名选择
  - 标签选择
  - 时间范围
- 搜索结果列表
- 搜索历史

**交互**:
- 实时搜索建议
- 清除搜索历史
- 保存搜索条件

#### 页面 6: 设置页 (Settings)
**功能**: 用户和偏好设置

**UI 元素**:
- 用户信息卡片
  - 头像
  - 昵称
  - 邮箱
- 同步设置
  - 最后同步时间
  - 手动同步按钮
  - Wi-Fi 仅同步开关
- 阅读设置
  - 默认字体大小
  - 默认主题
  - 自动标记已读（滚动到底部）
- 剪贴板监听开关
- 关于和帮助
- 退出登录

---

## 三、API 对接说明

### 3.1 认证 API

#### 注册
```
POST /api/v1/auth/register

Request:
{
  "email": "user@example.com",
  "password": "password123",
  "display_name": "John Doe"
}

Response:
{
  "access_token": "eyJhbGciOiJIUzI1NiIs...",
  "refresh_token": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": "uuid",
    "email": "user@example.com",
    "display_name": "John Doe",
    "avatar": ""
  }
}
```

#### 登录
```
POST /api/v1/auth/login

Request:
{
  "email": "user@example.com",
  "password": "password123"
}

Response: (同注册)
```

#### 刷新 Token
```
POST /api/v1/auth/refresh

Request:
{
  "refresh_token": "eyJhbGciOiJIUzI1NiIs..."
}

Response:
{
  "access_token": "new_access_token",
  "refresh_token": "new_refresh_token"
}
```

#### 获取当前用户
```
GET /api/v1/auth/me
Headers: Authorization: Bearer <access_token>

Response:
{
  "id": "uuid",
  "email": "user@example.com",
  "display_name": "John Doe",
  "avatar": "",
  "provider": "email"
}
```

### 3.2 URL 管理 API

**注意**: 所有 URL API 需要认证 (`Authorization: Bearer <token>`)

#### 创建 URL
```
POST /api/v1/urls

Request:
{
  "url": "https://example.com/article",
  "source": "ios"  // "ios" | "android"
}

Response:
{
  "id": "uuid",
  "original_url": "https://example.com/article",
  "normalized_url": "https://example.com/article",
  "title": "文章标题",
  "description": "文章摘要",
  "domain": "example.com",
  "company": "Example Inc",
  "content_type": "article",
  "parse_status": "pending",
  "is_read": false,
  "is_favorite": false,
  "is_archived": false,
  "collected_at": "2024-01-15T10:00:00Z",
  "created_at": "2024-01-15T10:00:00Z"
}
```

#### 获取 URL 列表
```
GET /api/v1/urls?page=1&limit=20&is_read=false&is_favorite=true

Response:
{
  "data": [
    {
      "id": "uuid",
      "title": "文章标题",
      "description": "摘要",
      "domain": "example.com",
      "company": "Example Inc",
      "favicon_url": "https://example.com/favicon.ico",
      "is_read": false,
      "is_favorite": true,
      "is_archived": false,
      "collected_at": "2024-01-15T10:00:00Z",
      "reading_time": 8
    }
  ],
  "total": 100,
  "page": 1,
  "limit": 20
}
```

#### 获取单个 URL 详情
```
GET /api/v1/urls/:id

Response:
{
  "id": "uuid",
  "original_url": "https://example.com/article",
  "title": "文章标题",
  "description": "摘要",
  "author": "John Doe",
  "site_name": "Example Blog",
  "published_at": "2024-01-14T08:00:00Z",
  "reading_time": 8,
  "word_count": 2000,
  "is_read": false,
  "is_favorite": false,
  "tags": [
    {"id": "uuid", "name": "技术", "color": "#FF5722"}
  ],
  "collections": [
    {"id": "uuid", "name": "必读文章"}
  ]
}
```

#### 更新 URL
```
PUT /api/v1/urls/:id

Request:
{
  "title": "新标题",
  "description": "新描述"
}
```

#### 标记已读/未读
```
POST /api/v1/urls/:id/read

Request:
{
  "is_read": true
}
```

#### 收藏/取消收藏
```
POST /api/v1/urls/:id/favorite

Request:
{
  "is_favorite": true
}
```

#### 归档
```
POST /api/v1/urls/:id/archive

Request:
{
  "is_archived": true
}
```

#### 删除 URL
```
DELETE /api/v1/urls/:id
```

#### 获取阅读器视图
```
GET /api/v1/urls/:id/reader

Response:
{
  "id": "uuid",
  "title": "文章标题",
  "author": "John Doe",
  "site_name": "Example Blog",
  "published_at": "2024-01-14T08:00:00Z",
  "reading_time": 8,
  "word_count": 2000,
  "content": "<div class=\"reader-content\">...processed HTML...</div>",
  "parse_status": "completed"
}
```

#### 重新解析
```
POST /api/v1/urls/:id/reparse

适用场景: 当 parse_status 为 "failed" 时，用户手动触发重新解析
```

### 3.3 收藏夹 API

#### 创建收藏夹
```
POST /api/v1/collections

Request:
{
  "name": "技术文章",
  "description": "优质技术文章收藏",
  "icon": "📚",
  "color": "#2196F3",
  "type": "manual"
}
```

#### 获取收藏夹列表
```
GET /api/v1/collections

Response:
{
  "data": [
    {
      "id": "uuid",
      "name": "技术文章",
      "description": "优质技术文章收藏",
      "icon": "📚",
      "color": "#2196F3",
      "type": "manual",
      "sort_order": 1
    }
  ]
}
```

#### 获取收藏夹详情
```
GET /api/v1/collections/:id

Response:
{
  "id": "uuid",
  "name": "技术文章",
  "description": "优质技术文章收藏",
  "urls": [
    {
      "id": "uuid",
      "title": "文章标题",
      "domain": "example.com",
      "collected_at": "2024-01-15T10:00:00Z"
    }
  ]
}
```

#### 添加 URL 到收藏夹
```
POST /api/v1/collections/:id/urls

Request:
{
  "url_id": "uuid"
}
```

#### 从收藏夹移除 URL
```
DELETE /api/v1/collections/:id/urls/:urlId
```

### 3.4 同步 API

#### 获取增量更新
```
GET /api/v1/sync/changes?since=2024-01-15T10:00:00Z

Response:
{
  "urls": [
    {
      "id": "uuid",
      "action": "create",  // "create" | "update" | "delete"
      "data": { ... }
    }
  ],
  "collections": [
    {
      "id": "uuid",
      "action": "update",
      "data": { ... }
    }
  ],
  "tags": [],
  "last_sync_at": "2024-01-15T11:00:00Z"
}
```

#### 推送本地更改
```
POST /api/v1/sync/push

Request:
{
  "changes": [
    {
      "resource_type": "url",
      "resource_id": "uuid",
      "action": "update",
      "data": {
        "is_read": true
      },
      "client_timestamp": "2024-01-15T10:30:00Z"
    }
  ]
}
```

### 3.5 搜索 API

#### 全文搜索
```
GET /api/v1/search?q=golang&page=1&limit=20

Response:
{
  "data": [
    {
      "id": "uuid",
      "title": "深入理解 Go 并发",
      "description": "...",
      "domain": "blog.example.com",
      "highlight": "<em>Go</em> 并发编程..."
    }
  ],
  "total": 15
}
```

---

## 四、关键交互流程

### 4.1 首次启动流程

```
1. 启动 App
2. 检查本地存储的 Token
   ├─ 有 Token → 验证有效性
   │   ├─ 有效 → 进入首页
   │   └─ 无效 → 显示登录页
   └─ 无 Token → 显示登录页
3. 用户登录/注册
4. 保存 Token 到本地（Keychain/Keystore）
5. 执行首次全量同步
6. 进入首页
```

### 4.2 剪贴板监听流程

#### iOS 实现
```swift
// 使用 UIPasteboard 检测剪贴板变化
class ClipboardMonitor {
    var lastChangeCount: Int = 0

    func checkClipboard() {
        let pasteboard = UIPasteboard.general
        if pasteboard.changeCount != lastChangeCount {
            lastChangeCount = pasteboard.changeCount

            if let urlString = pasteboard.string,
               URL(string: urlString) != nil {
                // 检测到 URL，显示提示
                showAddURLAlert(urlString)
            }
        }
    }
}

// 在后台定期检查（建议间隔 3-5 秒）
Timer.scheduledTimer(withTimeInterval: 3.0, repeats: true) { _ in
    clipboardMonitor.checkClipboard()
}
```

#### Android 实现
```kotlin
// Android 10+ 需要用户手动触发
// 使用 ClipboardManager.OnPrimaryClipChangedListener
class ClipboardMonitor(context: Context) {
    private val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    private val listener = ClipboardManager.OnPrimaryClipChangedListener {
        val clip = clipboard.primaryClip
        val item = clip?.getItemAt(0)
        val text = item?.text?.toString()

        if (text != null && isValidUrl(text)) {
            showAddURLDialog(text)
        }
    }

    fun startMonitoring() {
        clipboard.addPrimaryClipChangedListener(listener)
    }
}
```

**最佳实践**:
- iOS: 在 App 进入前台时检查，避免后台频繁检测
- Android: 注册监听器，系统自动通知
- 两者都应: 保存上次检测的 URL，避免重复提示
- 提供开关让用户禁用此功能

### 4.3 添加 URL 流程

```
1. 用户输入/粘贴 URL
2. 客户端验证 URL 格式
3. 调用 POST /api/v1/urls
4. 服务端返回 URL 对象（parse_status: "pending"）
5. 客户端显示"解析中"状态
6. 后台轮询 GET /api/v1/urls/:id 检查解析状态
   或使用 WebSocket（如果实现）
7. 解析完成后更新 UI
8. 可选: 添加到收藏夹/标签
```

**优化建议**:
- 先添加到本地数据库（乐观 UI）
- 后台异步同步到服务端
- 同步失败时显示错误并提供重试

### 4.4 阅读器加载流程

```
1. 用户点击 URL
2. 先检查本地缓存
   ├─ 有缓存 → 显示缓存
   └─ 无缓存 → 显示加载动画
3. 调用 GET /api/v1/urls/:id/reader
4. 接收 ReaderHTML
5. 渲染 HTML（WebView）
   ├─ 注入自定义 CSS
   ├─ 处理图片懒加载
   └─ 保存到本地缓存
6. 记录阅读开始时间
7. 用户滚动时同步阅读进度
```

**阅读器 WebView 配置**:

iOS (WKWebView):
```swift
let webView = WKWebView()

// 注入 CSS
let css = """
    .reader-content {
        max-width: 680px;
        margin: 0 auto;
        padding: 20px;
        font-family: -apple-system, BlinkMacSystemFont, sans-serif;
        font-size: \(fontSize)px;
        line-height: 1.6;
    }
    /* ... 更多样式 ... */
"""

let html = """
    <!DOCTYPE html>
    <html>
    <head>
        <style>\(css)</style>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        \(readerHTML)
    </body>
    </html>
"""

webView.loadHTMLString(html, baseURL: nil)
```

Android (WebView):
```kotlin
val webView = WebView(context)
webView.settings.javaScriptEnabled = true
webView.settings.domStorageEnabled = true

// 注入 CSS
val css = """
    .reader-content {
        max-width: 680px;
        margin: 0 auto;
        padding: 20px;
        font-family: system-ui, -apple-system, sans-serif;
        font-size: ${fontSize}px;
        line-height: 1.6;
    }
"""

val html = """
    <!DOCTYPE html>
    <html>
    <head>
        <style>$css</style>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
    </head>
    <body>
        $readerHTML
    </body>
    </html>
"""

webView.loadDataWithBaseURL(null, html, "text/html", "UTF-8", null)
```

### 4.5 多端同步流程

**增量同步策略**:

```
1. App 启动/进入前台时触发同步
2. 读取本地 last_sync_at
3. 调用 GET /api/v1/sync/changes?since=last_sync_at
4. 接收服务端变更
5. 应用变更到本地数据库
6. 推送本地未同步的更改
   POST /api/v1/sync/push
7. 更新本地 last_sync_at
8. 刷新 UI
```

**冲突处理**:
- 采用"服务端优先"策略
- 如果本地和服务端同时修改同一资源
- 以服务端数据为准，覆盖本地
- 可选: 提示用户冲突已被解决

**同步优化**:
- 仅在 Wi-Fi 下同步大量数据（可配置）
- 后台静默同步，不打断用户
- 失败重试机制（指数退避）
- 同步状态指示器

### 4.6 离线阅读实现

```
1. 用户收藏 URL 时自动下载 ReaderHTML
2. 下载所有图片到本地存储
3. 修改 HTML 中的图片引用为本地路径
4. 保存完整的离线包
5. 离线时从本地加载
```

**图片下载示例** (iOS):
```swift
func downloadImages(html: String, baseURL: URL) async -> [String: Data] {
    var images: [String: Data] = [:]

    // 解析 HTML 提取 img src
    if let doc = try? HTML(html: html, encoding: .utf8) {
        for img in doc.css("img[src]") {
            if let src = img["src"],
               let url = URL(string: src, relativeTo: baseURL) {
                // 下载图片
                if let data = try? Data(contentsOf: url) {
                    images[src] = data
                }
            }
        }
    }

    return images
}
```

---

## 五、数据模型映射

### 5.1 本地数据库设计

#### UserDefaults / SharedPreferences (轻量配置)
```swift
// 存储键
struct Keys {
    static let accessToken = "access_token"
    static let refreshToken = "refresh_token"
    static let lastSyncAt = "last_sync_at"
    static let clipboardEnabled = "clipboard_enabled"
    static let defaultFontSize = "default_font_size"
    static let defaultTheme = "default_theme"
    static let userId = "user_id"
}
```

#### CoreData / Room (主要数据)

**URL 实体**:
```swift
// Swift / CoreData
@objc(URL)
class URL: NSManagedObject {
    @NSManaged var id: UUID
    @NSManaged var userID: UUID
    @NSManaged var originalURL: String
    @NSManaged var normalizedURL: String
    @NSManaged var title: String
    @NSManaged var urlDescription: String  // "description" 是保留字
    @NSManaged var faviconURL: String?
    @NSManaged var domain: String?
    @NSManaged var company: String?
    @NSManaged var contentType: String?
    @NSManaged var author: String?
    @NSManaged var siteName: String?
    @NSManaged var publishedAt: Date?
    @NSManaged var readingTime: Int
    @NSManaged var wordCount: Int
    @NSManaged var isRead: Bool
    @NSManaged var isFavorite: Bool
    @NSManaged var isArchived: Bool
    @NSManaged var parseStatus: String
    @NSManaged var readerHTML: String?
    @NSManaged var snapshotURL: String?
    @NSManaged var summary: String?
    @NSManaged var keywords: [String]?
    @NSManaged var source: String?
    @NSManaged var collectedAt: Date
    @NSManaged var createdAt: Date
    @NSManaged var updatedAt: Date

    // 关系
    @NSManaged var tags: NSSet?
    @NSManaged var collections: NSSet?
}
```

**Collection 实体**:
```swift
@objc(Collection)
class Collection: NSManagedObject {
    @NSManaged var id: UUID
    @NSManaged var userID: UUID
    @NSManaged var name: String
    @NSManaged var collectionDescription: String
    @NSManaged var icon: String?
    @NSManaged var color: String?
    @NSManaged var type: String?
    @NSManaged var rule: String?
    @NSManaged var sortOrder: Int
    @NSManaged var createdAt: Date
    @NSManaged var updatedAt: Date

    @NSManaged var urls: NSSet?
}
```

**Tag 实体**:
```swift
@objc(Tag)
class Tag: NSManagedObject {
    @NSManaged var id: UUID
    @NSManaged var userID: UUID
    @NSManaged var name: String
    @NSManaged var color: String?
    @NSManaged var createdAt: Date

    @NSManaged var urls: NSSet?
}
```

**SyncLog 实体** (本地同步队列):
```swift
@objc(SyncLog)
class SyncLog: NSManagedObject {
    @NSManaged var id: UUID
    @NSManaged var resourceType: String  // "url" | "collection" | "tag"
    @NSManaged var resourceID: UUID
    @NSManaged var action: String  // "create" | "update" | "delete"
    @NSManaged var data: Data  // JSON 编码的变更数据
    @NSManaged var synced: Bool
    @NSManaged var createdAt: Date
}
```

### 5.2 数据同步策略

**双向同步**:
```
本地变更 → 记录到 SyncLog → 后台批量推送 → 标记已同步
服务端变更 → 轮询/推送 → 应用到本地数据库 → 刷新 UI
```

**数据一致性保证**:
- 使用 UUID 作为主键，避免冲突
- 基于 UpdatedAt 时间戳判断新旧
- 删除使用软删除（IsArchived = true）
- 重要操作先同步后完成（如收藏）

---

## 六、技术实现要点

### 6.1 iOS 技术栈

**推荐方案**:
- **语言**: Swift 5.9+
- **UI 框架**: SwiftUI
- **网络**: URLSession / async-await
- **数据库**: CoreData + CloudKit（可选）
- **图片加载**: SDWebImage / Kingfisher
- **HTML 渲染**: WKWebView
- **OAuth**: AuthenticationServices

**项目结构**:
```
ReaderApp/
├── App/
│   └── ReaderApp.swift
├── Models/
│   ├── User.swift
│   ├── URL.swift
│   ├── Collection.swift
│   └── Tag.swift
├── Views/
│   ├── Auth/
│   │   ├── LoginView.swift
│   │   └── RegisterView.swift
│   ├── Home/
│   │   ├── TimelineView.swift
│   │   └── URLCard.swift
│   ├── Reader/
│   │   └── ReaderView.swift
│   └── Settings/
│       └── SettingsView.swift
├── ViewModels/
│   ├── AuthViewModel.swift
│   ├── URLViewModel.swift
│   └── SyncViewModel.swift
├── Services/
│   ├── APIService.swift
│   ├── ClipboardMonitor.swift
│   └── SyncService.swift
├── Persistence/
│   └── CoreDataStack.swift
└── Utils/
    ├── Constants.swift
    └── Extensions.swift
```

### 6.2 Android 技术栈

**推荐方案**:
- **语言**: Kotlin 1.9+
- **UI 框架**: Jetpack Compose
- **架构**: MVVM + Clean Architecture
- **网络**: Retrofit + OkHttp
- **数据库**: Room
- **图片加载**: Coil
- **HTML 渲染**: WebView
- **OAuth**: Google Identity Services
- **依赖注入**: Hilt

**项目结构**:
```
ReaderApp/
├── app/src/main/
│   ├── java/com/reader/
│   │   ├── data/
│   │   │   ├── local/
│   │   │   │   ├── entity/
│   │   │   │   │   ├── User.kt
│   │   │   │   │   ├── URL.kt
│   │   │   │   │   └── Collection.kt
│   │   │   │   └── dao/
│   │   │   │       └── URLDao.kt
│   │   │   ├── remote/
│   │   │   │   ├── api/
│   │   │   │   │   ├── AuthAPI.kt
│   │   │   │   │   └── URLAPI.kt
│   │   │   │   └── dto/
│   │   │   └── repository/
│   │   │       └── URLRepository.kt
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   ├── usecase/
│   │   │   └── repository/
│   │   ├── presentation/
│   │   │   ├── auth/
│   │   │   ├── home/
│   │   │   ├── reader/
│   │   │   └── settings/
│   │   ├── service/
│   │   │   ├── ClipboardService.kt
│   │   │   └── SyncService.kt
│   │   └── di/
│   │       └── AppModule.kt
```

### 6.3 网络层实现

**API 服务封装** (Swift):
```swift
protocol APIServiceProtocol {
    func login(email: String, password: String) async throws -> AuthResponse
    func register(email: String, password: String, displayName: String) async throws -> AuthResponse
    func createURL(url: String, source: String) async throws -> URL
    func getURLs(page: Int, limit: Int, filters: URLFilters) async throws -> URLListResponse
    func getReaderView(id: UUID) async throws -> ReaderContent
    func syncChanges(since: Date?) async throws -> SyncChanges
    func pushChanges(changes: [SyncChange]) async throws -> SyncResponse
}

class APIService: APIServiceProtocol {
    private let baseURL = "https://api.yourdomain.com/api/v1"
    private let session: URLSession

    var accessToken: String? {
        didSet {
            // 保存到 Keychain
        }
    }

    func request<T: Decodable>(
        endpoint: String,
        method: HTTPMethod = .get,
        body: Encodable? = nil
    ) async throws -> T {
        var request = URLRequest(url: URL(string: baseURL + endpoint)!)
        request.httpMethod = method.rawValue
        request.setValue("application/json", forHTTPHeaderField: "Content-Type")

        if let token = accessToken {
            request.setValue("Bearer \(token)", forHTTPHeaderField: "Authorization")
        }

        if let body = body {
            request.httpBody = try JSONEncoder().encode(body)
        }

        let (data, response) = try await session.data(for: request)

        guard let httpResponse = response as? HTTPURLResponse,
              200..<300 ~= httpResponse.statusCode else {
            throw APIError.invalidResponse
        }

        return try JSONDecoder().decode(T.self, from: data)
    }
}
```

### 6.4 安全考虑

**Token 存储**:
- iOS: 使用 Keychain
- Android: 使用 EncryptedSharedPreferences
- Token 自动刷新: 401 响应时触发

**数据加密**:
- 本地数据库加密（SQLCipher）
- 敏感信息（OAuth Token）使用 Keychain/Keystore

**网络安全**:
- 强制 HTTPS（证书固定）
- API 请求签名（可选）

**隐私保护**:
- 剪贴板监听权限提示
- 明确告知数据用途
- 提供数据导出/删除功能

---

## 七、开发优先级

### Phase 1: 基础功能（2-3 周）
- [x] 认证（登录/注册）
- [x] API 服务层
- [x] 本地数据库搭建
- [x] URL 列表展示
- [x] 添加 URL 功能

### Phase 2: 核心功能（2-3 周）
- [x] 阅读器实现
- [x] 收藏夹管理
- [x] 标签系统
- [x] 搜索功能
- [x] 标记已读/收藏/归档

### Phase 3: 高级功能（2-3 周）
- [x] 多端同步
- [x] 剪贴板监听
- [x] 离线阅读
- [x] 图片缓存

### Phase 4: 优化和完善（1-2 周）
- [x] 性能优化
- [x] UI/UX 优化
- [x] 错误处理
- [x] 单元测试

---

## 八、测试策略

### 单元测试
- ViewModel 逻辑测试
- API 服务 Mock 测试
- 数据库 CRUD 测试

### 集成测试
- 同步流程测试
- 离线/在线切换测试
- 错误恢复测试

### UI 测试
- 关键用户流程
- UI 测试框架: XCUITest (iOS) / Compose Testing (Android)

---

## 九、性能优化建议

### 列表性能
- 使用差异刷新（DiffableDataSource）
- 图片懒加载
- 分页加载（每页 20-30 条）

### 内存管理
- WebView 及时销毁
- 图片压缩和缓存限制
- 大文件分片处理

### 网络优化
- 请求合并和批处理
- 响应缓存
- 失败重试和指数退避

### 启动优化
- 延迟初始化非关键组件
- 预加载常用数据
- 启动速度监控

---

## 十、后续扩展

### AI 摘要功能
- 调用 AI API 生成文章摘要
- 展示关键词和核心观点
- 支持多语言摘要

### 社交功能
- 分享收藏夹
- 关注其他用户
- 评论和讨论

### 浏览器集成
- iOS Share Extension
- Android Share Intent
- 快速保存到 App

### 数据统计
- 阅读时长统计
- 阅读习惯分析
- 每周/每月报告

---

**文档版本**: v1.0
**最后更新**: 2025-01-04
**维护者**: Backend Team
