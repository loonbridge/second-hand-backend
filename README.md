# second-hand-transaction-backend

本项目是“二手优选”微信小程序的后端服务。

## 在 IntelliJ IDEA 中启动项目

按照以下步骤在 IntelliJ IDEA 中配置和启动后端服务。

### 1. 配置 `application.properties`

在 `src/main/resources/application.properties` 文件中，你需要配置数据库连接、服务器端口等信息。

**示例配置:**
```properties
# 服务器端口
server.port=8080

# 数据库连接
spring.datasource.url=jdbc:mysql://localhost:3306/your_database_name?useSSL=false&serverTimezone=UTC
spring.datasource.username=your_username
spring.datasource.password=your_password
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# MyBatis-Plus 配置
mybatis-plus.mapper-locations=classpath:/mapper/*.xml
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
```
请将 `your_database_name`, `your_username`, 和 `your_password` 替换为你的实际数据库信息。

### 2. 安装 Maven 依赖

IDEA 通常会自动识别 `pom.xml` 文件并提示你加载 Maven 依赖。

1.  打开项目后，IDEA 的右下角会弹出提示 “Maven projects need to be imported”。点击 “Import Changes”。
2.  或者，你可以手动打开右侧的 Maven 面板，点击刷新按钮（Reload All Maven Projects）来同步依赖。

这将下载并安装 `pom.xml` 中定义的所有项目依赖。

### 3. 解决代码找不到符号的问题（重要）

当你首次导入项目或更新了 `api/swagger.yaml` 文件后，可能会在 `controller` 或 `dto` 包中看到大量的代码错误，提示找不到类或符号。

这是因为项目使用了 `openapi-generator-maven-plugin` 插件，它会根据 `api/swagger.yaml` 自动生成一部分 Java 代码。这些代码在你执行 Maven 构建命令之前是不存在的。

**解决方法:**

在 IDEA 的 Maven 面板中，执行 `compile` 生命周期阶段。

1.  打开右侧的 Maven 面板。
2.  展开 `second-hand-transaction-backend` -> `Lifecycle`。
3.  双击 `compile`。

Maven 会执行编译过程，其中 `openapi-generator` 插件会自动运行，生成所需的源代码。完成后，IDEA 会重新索引文件，代码中的错误应该会消失。

### 4. 运行项目

完成以上步骤后，找到主启动类 `cn.edu.guet.secondhandtransactionbackend.SecondHandTransactionBackendApplication`，右键点击并选择 “Run” 或 “Debug” 来启动项目。
