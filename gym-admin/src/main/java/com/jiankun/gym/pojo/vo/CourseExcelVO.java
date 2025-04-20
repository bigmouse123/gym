package com.jiankun.gym.pojo.vo;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/21 0:37
 */
@Data
public class CourseExcelVO {

    /**
     * 课程id
     */
    @ExcelProperty(value = "程id")
    private Long id;

    /**
     * 课程名
     */
    @ExcelProperty(value = "课程名")
    private String name;

    /**
     * 教练id
     */
    @ExcelProperty(value = "教练id")
    private Long coachId;

    /**
     * 课程时间
     */
    @ExcelProperty(value = "课程时间")
    private Date time;

    /**
     * 课程时长
     */
    @ExcelProperty(value = "课程时长")
    private Integer length;

    /**
     * 最大人数
     */
    @ExcelProperty(value = "最大人数")
    private Integer maxCount;

    /**
     * 教室
     */
    @ExcelProperty(value = "教室")
    private String room;

    /**
     * 课程封面
     */
    @ExcelProperty(value = "课程封面")
    private String image;

    /**
     * 状态（1：正常 0：停用）
     */
    @ExcelProperty(value = "状态")
    private Integer status;

    /**
     * 逻辑删除 1 表示删除，0 表示未删除
     */
    //@ExcelProperty(value = "逻辑删除")
    @ExcelIgnore
    private Integer deleted;

    /**
     * 创建时间
     */
    @ExcelProperty(value = "创建时间")
    private Date createTime;

    /**
     * 更新时间
     */
    @ExcelProperty(value = "更新时间")
    private Date updateTime;

}