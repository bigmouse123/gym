package com.jiankun.gym.pojo.dto;

import lombok.Data;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/18 11:51
 */
@Data
public class AdminLoginDTO {
    private String name;
    private String password;
    private String captcha;
    private String uuid;
}
