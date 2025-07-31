-- 插入默认商品分类数据
INSERT INTO category (category_id, name, icon_url) VALUES
(1, '数码产品', 'https://example.com/icons/digital.png'),
(2, '服装配饰', 'https://example.com/icons/clothing.png'),
(3, '家居用品', 'https://example.com/icons/home.png'),
(4, '图书音像', 'https://example.com/icons/books.png'),
(5, '运动户外', 'https://example.com/icons/sports.png'),
(6, '美妆护肤', 'https://example.com/icons/beauty.png'),
(7, '母婴用品', 'https://example.com/icons/baby.png'),
(8, '其他', 'https://example.com/icons/other.png')
ON DUPLICATE KEY UPDATE
name = VALUES(name),
icon_url = VALUES(icon_url);

-- 插入测试用户数据
INSERT INTO user (user_id, openid, nickname, avatar_url, created_at) VALUES
(1, 'test_openid_001', '测试用户', 'https://example.com/avatars/test_user.png', NOW()),
(2, 'test_openid_002', '演示用户', 'https://example.com/avatars/demo_user.png', NOW())
ON DUPLICATE KEY UPDATE
nickname = VALUES(nickname),
avatar_url = VALUES(avatar_url);
