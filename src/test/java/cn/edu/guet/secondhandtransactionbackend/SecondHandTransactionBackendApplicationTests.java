package cn.edu.guet.secondhandtransactionbackend;

import cn.edu.guet.secondhandtransactionbackend.entity.User;
import cn.edu.guet.secondhandtransactionbackend.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.sql.DataSource;
import java.sql.Connection;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebMvc
class SecondHandTransactionBackendApplicationTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WebApplicationContext webApplicationContext;

    private MockMvc mockMvc;

    @Autowired
    private DataSource dataSource;

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
        // 验证Spring上下文能够正常加载
        assertNotNull(userService);
        assertNotNull(dataSource);
        assertNotNull(webApplicationContext);
    }

    @Test
    void testDatabaseConnection() throws Exception {
        // 测试数据库连接
        try (Connection connection = dataSource.getConnection()) {
            assertNotNull(connection);
            assertTrue(connection.isValid(5));
            System.out.println("✅ 数据库连接测试通过");
        }
    }

    @Test
    void testUserServiceBasicOperations() {
        // 测试基本的数据库操作
        try {
            // 测试查询所有用户（可能为空，但不应该抛异常）
            List<User> users = userService.list();
            assertNotNull(users);
            System.out.println("✅ 用户查询操作测试通过，当前用户数量: " + users.size());

            // 测试创建用户
            User testUser = new User();
            testUser.setOpenid("test_openid_" + System.currentTimeMillis());
            testUser.setNickname("测试用户");
            testUser.setAvatarUrl("https://example.com/avatar.jpg");
            testUser.setCreatedAt(LocalDateTime.now());

            boolean saved = userService.save(testUser);
            assertTrue(saved);
            assertNotNull(testUser.getUserId());
            System.out.println("✅ 用户创建测试通过，用户ID: " + testUser.getUserId());

            // 测试根据ID查询用户
            User foundUser = userService.getById(testUser.getUserId());
            assertNotNull(foundUser);
            assertEquals(testUser.getNickname(), foundUser.getNickname());
            System.out.println("✅ 用户查询测试通过");

            // 测试更新用户
            foundUser.setNickname("更新后的昵称");
            boolean updated = userService.updateById(foundUser);
            assertTrue(updated);
            System.out.println("✅ 用户更新测试通过");

            // 测试删除用户
            boolean deleted = userService.removeById(testUser.getUserId());
            assertTrue(deleted);
            System.out.println("✅ 用户删除测试通过");

        } catch (Exception e) {
            fail("数据库操作测试失败: " + e.getMessage());
        }
    }

    @Test
    void testUserControllerHealthEndpoint() throws Exception {
        // 手动构建MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 测试健康检查接口
        mockMvc.perform(get("/api/users/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"))
                .andExpect(jsonPath("$.message").value("User service is running"));

        System.out.println("✅ 用户控制器健康检查接口测试通过");
    }

    @Test
    void testUserControllerListEndpoint() throws Exception {
        // 手动构建MockMvc
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        // 测试用户列表接口
        mockMvc.perform(get("/api/users/list"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.count").isNumber());

        System.out.println("✅ 用户列表接口测试通过");
    }

    @Test
    void testRestApiWithTestRestTemplate() {
        // 使用TestRestTemplate测试REST API
        String healthUrl = "http://localhost:" + port + "/api/users/health";
        ResponseEntity<Map> healthResponse = restTemplate.getForEntity(healthUrl, Map.class);

        assertEquals(200, healthResponse.getStatusCodeValue());
        Map<String, Object> healthBody = healthResponse.getBody();
        assertNotNull(healthBody);
        assertEquals("ok", healthBody.get("status"));

        System.out.println("✅ REST API集成测试通过");
    }

    @Test
    void testApplicationStartup() {
        // 测试应用启动
        String actuatorUrl = "http://localhost:" + port + "/api/users/health";
        ResponseEntity<String> response = restTemplate.getForEntity(actuatorUrl, String.class);
        assertEquals(200, response.getStatusCodeValue());
        System.out.println("✅ 应用启动测试通过，服务运行在端口: " + port);
    }
}
