package com.jiankun.gym.pojo.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Course implements Serializable {


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
