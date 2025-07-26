package cn.edu.guet.secondhandtransactionbackend.vo;

import lombok.Data;

/**
 * 用户摘要视图对象
 * 用于在商品详情等地方展示用户基本信息
 */
@Data
public class UserSummaryVO {
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像URL
     */
    private String avatarUrl;
}
