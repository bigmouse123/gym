package com.jiankun.gym.pojo.query;

import lombok.Data;

import java.util.Date;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/14 11:38
 */
@Data
public class AdminQuery {
    private Integer page;
    private Integer limit;
    private String name;
    private String phone;
    private Date beginUpdateTime;
    private Date endUpdateTime;
}
