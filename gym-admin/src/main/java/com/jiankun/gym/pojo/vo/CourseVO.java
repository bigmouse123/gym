package com.jiankun.gym.pojo.vo;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/17 14:34
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CourseVO implements Serializable {


    /**
     * 课程id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 课程名
     */
    private String name;

    /**
     * 教练id
     */
    @TableField("coach_id")
    private Long coachId;

    /**
     * 教练姓名
     */
    private String coachName;

    /**
     * 课程时间
     */
    private Date time;

    /**
     * 课程时长
     */
    private Integer length;

    /**
     * 最大人数
     */
    @TableField("max_count")
    private Integer maxCount;

    /**
     * 教室
     */
    private String room;

    /**
     * 课程封面
     */
    private String image;

    /**
     * 状态（1：正常 0：停用）
     */
    private Integer status;

    /**
     * 逻辑删除 1 表示删除，0 表示未删除
     */
    @TableLogic
    private Integer deleted;

    /**
     * 创建时间
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;


}