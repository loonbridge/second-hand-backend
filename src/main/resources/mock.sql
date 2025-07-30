-- 关闭外键约束，以便按任意顺序插入数据
SET FOREIGN_KEY_CHECKS=0;

-- 清空旧数据 (可选, 如果您想在干净的环境中运行)
TRUNCATE TABLE user_user_follow_fnn;
TRUNCATE TABLE user_product_favorite_fnn;
TRUNCATE TABLE review;
TRUNCATE TABLE product_image;
TRUNCATE TABLE `order`;
TRUNCATE TABLE product;
TRUNCATE TABLE category;
TRUNCATE TABLE user;

-- =================================================================
-- 1. 插入用户数据 (user)
-- =================================================================
INSERT INTO `user` (user_id, openid, nickname, avatar_url, created_at) VALUES
                                                                           (1, 'openid_user_001', '极客买家', 'https://example.com/avatars/001.png', '2023-10-01 10:00:00'),
                                                                           (2, 'openid_user_002', '潮流卖家', 'https://example.com/avatars/002.png', '2023-10-02 11:30:00'),
                                                                           (3, 'openid_user_003', '数码控', 'https://example.com/avatars/003.png', '2023-10-03 14:00:00'),
                                                                           (4, 'openid_user_004', '爱书人', 'https://example.com/avatars/004.png', '2023-10-04 18:45:00'),
                                                                           (5, 'openid_user_005', '游戏大神', 'https://example.com/avatars/005.png', '2023-10-05 20:00:00');

-- =================================================================
-- 2. 插入分类数据 (category)
-- =================================================================
INSERT INTO `category` (category_id, name, icon_url) VALUES
                                                         (1, '数码产品', 'https://example.com/icons/digital.png'),
                                                         (2, '潮流服饰', 'https://example.com/icons/fashion.png'),
                                                         (3, '图书音像', 'https://example.com/icons/books.png'),
                                                         (4, '游戏外设', 'https://example.com/icons/gaming.png');

-- =================================================================
-- 3. 插入商品数据 (product)
-- 用户2(潮流卖家), 3(数码控), 5(游戏大神) 是卖家
-- =================================================================
INSERT INTO `product` (product_id, title, description, price, stock, status, user_id, category_id, created_at, updated_at) VALUES
                                                                                                                               (1, '九成新二手智能手机', '256GB, 黑色, 屏幕有轻微划痕, 功能完好, 带原装充电器。', 2800.00, 1, 'ON_SALE', 3, 1, '2023-11-10 09:00:00', '2023-11-10 09:00:00'),
                                                                                                                               (2, '限量版复古运动夹克', 'L码, 仅穿过一次, 99新, 设计非常独特。', 850.50, 1, 'ON_SALE', 2, 2, '2023-11-12 15:20:00', '2023-11-12 15:20:00'),
                                                                                                                               (3, '经典科幻小说套装(全三册)', '几乎全新, 无折痕无笔记, 非常适合收藏。', 120.00, 5, 'ON_SALE', 4, 3, '2023-11-15 11:00:00', '2023-11-15 11:00:00'),
                                                                                                                               (4, '高性能机械键盘', '青轴, RGB背光, 87键布局, 手感极佳。卖家升级装备故出售。', 450.00, 1, 'SOLD_OUT', 5, 4, '2023-11-18 18:00:00', '2023-12-05 14:10:00'),
                                                                                                                               (5, '专业降噪蓝牙耳机', '音质出色, 续航强劲, 配件齐全, 适合通勤和学习。', 680.00, 1, 'ON_SALE', 3, 1, '2023-11-20 13:30:00', '2023-11-20 13:30:00'),
                                                                                                                               (6, '绝版潮流印花T恤', '纯棉材质, M码, 图案无开裂, 已绝版。', 299.00, 0, 'SOLD_OUT', 2, 2, '2023-11-21 16:00:00', '2023-12-02 20:00:00');

-- =================================================================
-- 4. 插入商品图片数据 (product_image)
-- =================================================================
INSERT INTO `product_image` (product_image_id, product_id, image_url, display_order) VALUES
-- 手机图片
(1, 1, 'https://example.com/products/phone_main.jpg', 0), -- 主图
(2, 1, 'https://example.com/products/phone_scratch.jpg', 1), -- 划痕细节
(3, 1, 'https://example.com/products/phone_back.jpg', 2),
-- 夹克图片
(4, 2, 'https://example.com/products/jacket_front.jpg', 0),
(5, 2, 'https://example.com/products/jacket_detail.jpg', 1),
-- 小说图片
(6, 3, 'https://example.com/products/books_cover.jpg', 0),
-- 键盘图片
(7, 4, 'https://example.com/products/keyboard_main.jpg', 0),
(8, 4, 'https://example.com/products/keyboard_light.jpg', 1),
-- 耳机图片
(9, 5, 'https://example.com/products/headphone_main.jpg', 0),
-- T恤图片
(10, 6, 'https://example.com/products/tshirt.jpg', 0);

-- =================================================================
-- 5. 插入订单数据 (order)
-- 用户1(极客买家) 和 5(游戏大神) 是主要买家
-- =================================================================
INSERT INTO `order` (order_id, order_number, status, price_at_purchase, quantity, total_price, user_id, product_id, created_at) VALUES
-- 用户1购买了键盘 (已完成)
(1, 'ORD20231201001', 'COMPLETED', 450.00, 1, 450.00, 1, 4, '2023-12-01 10:00:00'),
-- 用户5购买了T恤 (已收货)
(2, 'ORD20231202002', 'TO_RECEIVE', 299.00, 1, 299.00, 5, 6, '2023-12-02 19:30:00'),
-- 用户1购买了耳机 (待发货)
(3, 'ORD20231208003', 'TO_SHIP', 680.00, 1, 680.00, 1, 5, '2023-12-08 22:00:00'),
-- 用户4购买了小说 (已取消)
(4, 'ORD20231210004', 'CANCELED', 120.00, 1, 120.00, 4, 3, '2023-12-10 11:00:00'),
-- 用户3购买了小说 (待付款)
(5, 'ORD20231215005', 'TO_PAY', 120.00, 2, 240.00, 3, 3, '2023-12-15 09:00:00');


-- =================================================================
-- 6. 插入评论数据 (review)
-- 只有已完成(COMPLETED)或已收货(TO_RECEIVE)的订单才能评论
-- =================================================================
INSERT INTO `review` (review_id, content, rating, product_id, user_id, created_at) VALUES
-- 用户1评论键盘
(1, '键盘太棒了！手感一流，RGB灯效酷炫，卖家发货也很快！', 5, 4, 1, '2023-12-05 14:00:00'),
-- 用户5评论T恤
(2, '衣服很合身，图案确实是绝版货，很有收藏价值。好评！', 5, 6, 5, '2023-12-07 08:00:00');

-- =================================================================
-- 7. 插入用户收藏数据 (user_product_favorite_fnn)
-- =================================================================
INSERT INTO `user_product_favorite_fnn` (user_id, product_id) VALUES
                                                                              (1, 2), -- 极客买家 收藏了 潮流夹克
                                                                              (1, 5), -- 极客买家 收藏了 降噪耳机 (后来购买了)
                                                                              (4, 1), -- 爱书人 收藏了 智能手机
                                                                              (5, 1); -- 游戏大神 也收藏了 智能手机

-- =================================================================
-- 8. 插入用户关注数据 (user_user_follow_fnn)
-- =================================================================
INSERT INTO `user_user_follow_fnn` (follower_user_id, following_user_id) VALUES
                                                                                         (1, 2), -- 极客买家(1) 关注了 潮流卖家(2)
                                                                                         (1, 3), -- 极客买家(1) 关注了 数码控(3)
                                                                                         (4, 2), -- 爱书人(4) 关注了 潮流卖家(2)
                                                                                         (5, 3); -- 游戏大神(5) 关注了 数码控(3)

-- 重新开启外键约束
SET FOREIGN_KEY_CHECKS=1;