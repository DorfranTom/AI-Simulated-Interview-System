# AI模拟面试系统

## 项目简介
AI模拟面试系统是一个帮助用户提升面试技巧的工具，它通过模拟真实的面试场景，为用户提供面试问题，并根据用户的回答生成相应的反馈和报告。系统支持多种岗位类型的面试，让用户能够针对不同的岗位进行针对性的练习。

## 功能特性
- **面试管理**：支持新建面试、查看历史面试记录、选择面试岗位类型等功能。
- **实时交互**：在面试过程中，用户可以实时输入回答，系统会根据回答给出下一个问题，直到面试结束。
- **面试报告**：面试结束后，系统会生成详细的面试报告，包括整体得分、维度得分、知识差距和建议等。


## 技术栈
- **前端**：HTML、CSS、JavaScript、Tailwind CSS、Font Awesome
- **后端**：Java、Spring Boot
- **数据库**：JPA

## 环境搭建

### 前端环境
前端部分主要使用HTML、CSS和JavaScript，无需额外的构建工具。只需确保你的浏览器支持现代的HTML5和CSS3特性。

### 后端环境
1. **JDK安装**：确保你已经安装了Java Development Kit (JDK) 8或更高版本。你可以从[Oracle官方网站](https://www.oracle.com/java/technologies/javase-downloads.html )或[OpenJDK](https://openjdk.java.net/ )下载并安装。
2. **Maven安装**：项目使用Maven进行依赖管理和构建。你可以从[Maven官方网站](https://maven.apache.org/download.cgi )下载并安装。
3. **IDE选择**：推荐使用IntelliJ IDEA或Eclipse作为开发工具。

### 数据库环境
项目使用JPA进行数据库操作，默认使用MySQL数据库。如果你需要使用其他数据库（如H2、PostgreSQL等），请在`application.properties`文件中配置相应的数据库连接信息。

## 运行项目
1. **克隆项目**：
```bash
git clone https://github.com/your-repo/AIInterview_v2.git 
cd AIInterview_v2
```
2. **构建项目**：
```bash
mvn clean install
```
3. **启动项目**：
```bash
mvn spring-boot:run
```
4. **访问项目**：
   打开浏览器，访问`http://localhost:8080`，即可看到AI模拟面试系统的界面。

## 测试
项目提供了基本的单元测试，你可以使用以下命令运行测试：
```bash
mvn test
```

## 贡献
如果你想为这个项目做出贡献，请遵循以下步骤：
1. Fork这个项目。
2. 创建一个新的分支：`git checkout -b feature/your-feature`。
3. 提交你的更改：`git commit -m 'Add some feature'`。
4. 推送你的分支：`git push origin feature/your-feature`。
5. 提交一个Pull Request。

## 许可证
本项目采用[MIT许可证](LICENSE)。

## 联系信息
如果你有任何问题或建议，请通过以下方式联系我们：
- **邮箱**：your-email@example.com
- **GitHub Issues**：[https://github.com/your-repo/AIInterview_v2/issues ](https://github.com/your-repo/AIInterview_v2/issues )              
