# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

本项目是“二手优选”微信小程序的后端服务，基于 Spring Boot 和 MyBatis-Plus 实现。它提供了完整的 API，用于处理用户、商品、订单、分类和消息通知等核心功能。

## 技术栈

- **后端框架**: Spring Boot (v3.3.1)
- **编程语言**: Java 21
- **数据访问**: MyBatis-Plus (v3.5.7)
- **数据库**: MySQL
- **认证**: JWT (java-jwt v4.4.0), 认证逻辑主要位于 `cn.edu.guet.secondhandtransactionbackend.util.JwtUtil`。
- **构建工具**: Apache Maven
- **API 文档**: OpenAPI 3.0 (`api/swagger.yaml`)

## 代码架构

项目遵循经典的分层架构模式：

- `src/main/java/cn/edu/guet/secondhandtransactionbackend/`
  - `controller`: 存放 Spring MVC 控制器，负责接收和响应 HTTP 请求。大部分代码是由 OpenAPI 生成器根据 `api/swagger.yaml` 自动生成的。
  - `service`: 业务逻辑层，处理核心业务流程。
  - `mapper`: MyBatis-Plus 的 Mapper 接口，用于定义数据库操作。
  - `entity`: 数据库实体类（POJO）。
  - `util`: 存放工具类，如 `JwtUtil`。
- `src/main/resources/`
  - `application.properties`: Spring Boot 的主配置文件。
  - `mapper/`: 存放 MyBatis 的 XML 映射文件，用于编写复杂的 SQL 查询。

## 常用命令

- **构建项目**:
  ```shell
  ./mvnw clean install
  ```

- **运行项目**:
  ```shell
  ./mvnw spring-boot:run
  ```

- **运行所有测试**:
  ```shell
  ./mvnw test
  ```

- **运行单个测试类或方法**:
  ```shell
  # 运行单个测试类
  ./mvnw test -Dtest="YourTestClassName"
  # 运行单个测试方法
  ./mvnw test -Dtest="YourTestClassName#yourTestMethodName"
  ```

## 关键开发流程

项目使用 `openapi-generator-maven-plugin` 插件。当 `api/swagger.yaml` 文件更新后，执行 `./mvnw clean install` 会自动重新生成 `controller` 层的接口和 `dto`（数据传输对象）。业务逻辑的实现需要手动在 `service` 层完成。
