package com.jiankun.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.mapper.CourseMapper;
import com.jiankun.gym.pojo.Course;
import com.jiankun.gym.pojo.Course;
import com.jiankun.gym.mapper.CourseMapper;
import com.jiankun.gym.pojo.query.CourseQuery;
import com.jiankun.gym.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements ICourseService {
    @Autowired
    private CourseMapper courseMapper;

    @Override
    public IPage<Course> list(CourseQuery courseQuery) {
        IPage<Course> page = new Page<>(courseQuery.getPage(), courseQuery.getLimit());
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(courseQuery.getName()), "name", courseQuery.getName());
        queryWrapper.like(!ObjectUtils.isEmpty(courseQuery.getCoachId()), "coach_id", courseQuery.getCoachId());
        queryWrapper.between(!ObjectUtils.isEmpty(courseQuery.getBeginTime()) && !ObjectUtils.isEmpty(courseQuery.getEndTime()), "time", courseQuery.getBeginTime(), courseQuery.getEndTime());
        courseMapper.selectPage(page, queryWrapper);
        return page;
    }
}
