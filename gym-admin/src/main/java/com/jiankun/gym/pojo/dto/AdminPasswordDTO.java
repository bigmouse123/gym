package com.jiankun.gym.pojo.dto;

import lombok.Data;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/19 21:29
 */
@Data
public class AdminPasswordDTO {
    private String oldPassword;
    private String newPassword;
}
