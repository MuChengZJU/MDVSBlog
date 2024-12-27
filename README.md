# MDVSBlog

> 一个基于 **M**ySQL + **D**ocker + **V**ue + **S**pring **B**oot 的前后端分离博客系统

## 目录

- [项目简介](#项目简介)
- [功能特点](#功能特点)
- [技术栈](#技术栈)
- [项目结构](#项目结构)
- [安装与运行](#安装与运行)
    - [前端](#前端)
    - [后端](#后端)
    - [Docker 部署](#docker-部署)
- [截图展示](#截图展示)
- [贡献指南](#贡献指南)
- [许可证](#许可证)

---

## 项目简介

本项目是一个博客管理系统，支持管理员登录、文章管理，以及基于 Markdown 的内容编辑器。它通过前后端分离架构实现高效开发与易用性。

---

## 功能特点

- 访客阅读和查找文章
- 基于 JWT 的管理员登录认证与权限管理的增删改
- Markdown 编辑器支持
- 标签和分类管理
- Docker 支持，便于部署

---

## 技术栈

### 前端
- Vue 3
- Element Plus

### 后端
- Spring Boot
- MySQL

### 部署与构建
- Docker & Docker Compose
- Maven

---

## 项目结构

```plaintext
.
├── blog-frontend/          # 前端代码
│   ├── public/             # 静态资源
│   ├── src/                # 前端源代码
│   │   ├── components/     # 公共组件
│   │   ├── views/          # 页面组件
│   │   ├── router/         # 路由配置
│   │   ├── services/       # API 接口
│   └── vue.config.js       # 配置文件
├── src/                    # 后端代码
│   ├── main/java/          # Java 源代码
│   ├── main/resources/     # 配置和资源文件
│   └── test/               # 测试代码
├── docker-compose.yml      # Docker 编排文件
├── pom.xml                 # Maven 配置
├── demo.env                # 示例环境变量(需重命名为 .env)
└── README.md               # 项目说明
```

---

## 安装与运行

### Docker 快速部署

1. 克隆前后端镜像：

   ```bash
   docker pull muchengzju/blog-frontend:latest
   docker pull muchengzju/blog-backend:latest
   ```
   
2. 克隆本仓库内的 `docker-compose.yml` 和 `demo.env` 文件：

   ```bash
   curl -O https://raw.githubusercontent.com/MuChengZJU/Blog/master/docker-compose.yml
   curl -O https://raw.githubusercontent.com/MuChengZJU/Blog/master/demo.env
   ```

3. 复制 `demo.env` 文件为 `.env` 文件，并根据需要修改其中的环境变量，在文件中有对应说明。

4. 运行 Docker 容器：

   ```bash
   docker-compose up
   ```

   项目会根据 `docker-compose.yml` 文件中的配置启动前端、后端和数据库容器。

   - 前端：`http://localhost:9091`
   - 后端 API：`http://localhost:9090/api`

### 项目完整克隆

找一个合适的地方，悄悄地把项目克隆到本地：
```bash
git clone https://github.com/MuChengZJU/Blog.git
```
或者用你喜欢的方式把代码扒下来 :)

### 本地开发运行

注意：

由于能力有限，项目目前跑 Docker 流程最顺，因为环境变量配置在 `.env` 文件中，而这造成 Maven 无法直接读取，所以我禁用了检查，当前代码跑得起来。如果你在在本地运行，可能需要手动修改 `application.properties` 文件中的数据库连接信息。

#### 前端

1. 在 `Blog/blog-fronted` 前端目录安装依赖：
   ```bash
   cd blog-frontend
   npm install
   ```
2. 运行开发服务器：

   ```bash
   npm run serve
   ```

#### 后端

1. 配置数据库（MySQL）：
    - 在 `src/main/resources/application.properties` 中修改数据库连接信息。
    - 初始化数据库
      - 设置root密码
      - 创建数据库：`CREATE DATABASE db_blog;`
      - ( 可选 ) 创建专用的数据库用户

2. 启动后端服务：

   如果你在 `IDEA` 中运行后端，可以直接运行 `BlogApplication` 类。

   或者在 `Blog` 根目录运行：
   ```bash
   mvn spring-boot:run
   ```

### 打包项目和部署

1. 在 `Blog/blog-frontend` 前端目录打包：
   ```bash
   npm run build
   ```
   会在 `dist` 目录下生成前端静态文件。

2. 在 `Blog` 根目录打包后端：
   ```bash
   mvn clean package
   ```
   会在 `target` 目录下生成后端 jar 包。

3. 在你的服务器上部署后端 jar 包和前端静态文件，方法自选，例如
    - 使用 `nohup` 后台运行 jar 包
    - 使用 `nginx` 部署前端静态文件

### Docker 部署

1. 配置环境变量：
    - 复制 `demo.env` 文件为 `.env` 文件，并根据需要修改其中的环境变量，在文件中有对应说明。
    - 这些环境变量会在 Docker 容器中使用。
    有如下的环境变量：
   ```plaintext
   SPRING_DATASOURCE_URL=jdbc:mysql://db:3306/blog_db # 数据库连接
   SPRING_DATASOURCE_USERNAME=EDITABLE_USERNAME
   SPRING_DATASOURCE_PASSWORD=EDITABLE_PASSWORD
   SERVER_PORT=9090                                   # 后端端口
   JWT_SECRET=EDITABLE_SECRET                         # 需要符合base64编码规则
   ADMIN_USERNAME=EDITABLE_USERNAME
   ADMIN_PASSWORD=EDITABLE_PASSWORD
   # MySQL 环境变量
   MYSQL_ROOT_PASSWORD=EDITABLE_PASSWORD              # 初始化数据库密码
   MYSQL_DATABASE=EDITABLE_DATABASE                   # 初始化数据库名称
   ```
   
2. 在 `Blog` 根目录构建并运行容器：

   ```bash
   docker-compose up --build
   ```
    项目会根据 `docker-compose.yml` 文件中的配置启动前端、后端和数据库容器。
    - 前端：`Blog\blog-frontend\Dockerfile`
    - 后端：`Blog\Dockerfile`
    - 数据库：`mysql:8`，在服务器上会自己创建容器。
      - 注意可能遇到 Docker 拉取镜像慢的问题，可以使用一些*魔法*，以及先手动拉取镜像。

### 访问项目：

    - 前端：`http://localhost:9091`
    - 后端 API：`http://localhost:9090/api`

---


## 功能展示

1. **首页**
    
    - 首页有按照时间线排列的所有文章。
    - 顶栏有搜索框，可以根据关键字搜索文章。
    - 右上角有可自定义的头像和个人链接。
    - 点击标签可以查看对应标签的文章。

    ![1_MainPage.png](docs/images/1_MainPage.png)

2. **文章内容**

    点击`查看详情`可以进入查看完整的文章内容。（它甚至可以加载图片，此图是从 98 的图床加载过来的，但是不经过博客服务器，也就是用户前端从 98 读取的，所以需要在可连接图床的环境下才能看到，也就是连接校网才能看到 98 图床里的图片）

    ![2_ArticleDetail.png](docs/images/2_ArticleDetail.png)

3. **管理员登录**

   点击右上角的`管理员登录`按钮，输入管理员账号密码即可登录。管理员账号密码通过配置文件中设置，通过 Docker 部署则通过 `.env -> docker-compose.yml -> application.properties -> 后端程序` 在后端初始化且无法改变（是的这很不优雅 QAQ）。

    ![3_AdminLogin.png](docs/images/3_AdminLogin.png)

    登录后，请**刷新网页**，这样可以看到文章管理页面和发布文章的选项，可以对文章进行增删改查。

    ![4_AdminMainPage.png](docs/images/4_AdminMainPage.png)

4. **文章管理**

    - 点击`文章管理`可以查看所有文章，可以对文章进行增删改查。
    - 点击修改会跳转到文章编辑页面，可以修改文章内容。
    - 点击删除会删除文章，且有弹窗提示。

    ![5_Management.png](docs/images/5_Management.png)

    ![6_DeleteWarning.png](docs/images/6_DeleteWarning.png)

5. **文章编辑和发布**

    - 点击`发布文章`可以进入文章编辑页面，支持 Markdown 编辑。
   
    ![7_Publish.png](docs/images/7_Publish.png)

---

## 贡献指南

欢迎贡献代码或提出建议！

1. Fork 仓库
2. 创建新分支：
   ```bash
   git checkout -b feature/your-feature
   ```
3. 提交代码并创建 PR。

---

## 许可证

本项目采用 [MIT License](LICENSE)。

