package com.jiankun.gym.pojo.query;

import lombok.Data;

import java.util.Date;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/21 14:49
 */
@Data
public class RoleQuery {
    private Integer page;
    private Integer limit;
    private String name;
    private Date beginUpdateTime;
    private Date endUpdateTime;
}
