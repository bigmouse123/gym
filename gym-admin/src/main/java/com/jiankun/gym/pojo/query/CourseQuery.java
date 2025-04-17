package com.jiankun.gym.pojo.query;

import lombok.Data;

import java.util.Date;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/17 10:34
 */
@Data
public class CourseQuery {
    private Integer page;
    private Integer limit;
    private String name;
    private Long coachId;
    private Date beginTime;
    private Date endTime;
}
