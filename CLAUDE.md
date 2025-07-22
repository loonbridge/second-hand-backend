# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## 项目概述

本项目是“二手优选”微信小程序的后端服务，基于 Spring Boot 和 MyBatis-Plus 实现。它提供了完整的 API，用于处理用户、商品、订单、分类和消息通知等核心功能。认证机制采用基于 JWT 的 Bearer Token。

## 技术栈

- **后端框架**: Spring Boot 3.5.3
- **编程语言**: Java 21
- **数据访问**: MyBatis-Plus 3.5.9
- **数据库**: MySQL
- **认证**: JWT (java-jwt 4.5.0)
- **构建工具**: Apache Maven

## 代码架构

项目遵循经典的分层架构模式：

- `src/main/java/cn/edu/guet/secondhandtransactionbackend/`
  - `controller`: 存放 Spring MVC 控制器，负责接收和响应 HTTP 请求。
  - `service`: 业务逻辑层，处理核心业务流程。
  - `mapper`: MyBatis-Plus 的 Mapper 接口，用于定义数据库操作。
  - `entity`: 数据库实体类（POJO）。
- `src/main/resources/`
  - `application.properties`: Spring Boot 的主配置文件。
  - `mapper/`: 存放 MyBatis 的 XML 映射文件，用于编写复杂的 SQL 查询。
  - `api/swagger.yaml`: 项目的 OpenAPI 规范文件。

## 常用命令

- **构建项目**:
  ```shell
  ./mvnw clean install
  ```

- **运行项目**:
  ```shell
  ./mvnw spring-boot:run
  ```

- **运行测试**:
  ```shell
  ./mvnw test
  ```
