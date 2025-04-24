package com.jiankun.gym.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.mapper.CoachMapper;
import com.jiankun.gym.mapper.CourseMapper;
import com.jiankun.gym.pojo.entity.Coach;
import com.jiankun.gym.pojo.entity.Course;
import com.jiankun.gym.pojo.query.CourseQuery;
import com.jiankun.gym.pojo.vo.CourseExcelVO;
import com.jiankun.gym.pojo.vo.CourseVO;
import com.jiankun.gym.service.ICourseService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiankun.gym.util.ExcelUtil;
import com.jiankun.gym.listener.CourseExcelListener;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private CoachMapper coachMapper;

    @Override
    public IPage<CourseVO> list(CourseQuery courseQuery) {
        IPage<Course> coursePage = new Page<>(courseQuery.getPage(), courseQuery.getLimit());
        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(courseQuery.getName()), "name", courseQuery.getName());
        queryWrapper.like(!ObjectUtils.isEmpty(courseQuery.getCoachId()), "coach_id", courseQuery.getCoachId());
        queryWrapper.between(!ObjectUtils.isEmpty(courseQuery.getBeginTime()) && !ObjectUtils.isEmpty(courseQuery.getEndTime()), "time", courseQuery.getBeginTime(), courseQuery.getEndTime());
        courseMapper.selectPage(coursePage, queryWrapper);
        // 转换到 CourseVO 分页对象
        IPage<CourseVO> courseVOIPage = new Page<>(coursePage.getCurrent(), coursePage.getSize(), coursePage.getTotal());
        courseVOIPage.setPages(coursePage.getPages());

        // 转换每条记录并设置 coachName
        List<CourseVO> courseVOList = coursePage.getRecords().stream().map(course -> {
            CourseVO courseVO = new CourseVO();
            // 复制公共属性（假设字段名一致）
            BeanUtils.copyProperties(course, courseVO);
            // 根据 coachId 查询教练名称
            Coach coach = coachMapper.selectById(course.getCoachId());
            String coachName = coach.getName();
            courseVO.setCoachName(coachName);
            return courseVO;
        }).collect(Collectors.toList());

        courseVOIPage.setRecords(courseVOList);

        return courseVOIPage;
    }

    @Override
    public void exportExcel(HttpServletResponse response) {
        List<Course> list = courseMapper.selectList(null);
        ArrayList<CourseExcelVO> courseExcelVOList = new ArrayList<>();
        for (Course course : list) {
            CourseExcelVO courseExcelVO = new CourseExcelVO();
            BeanUtils.copyProperties(course, courseExcelVO);
            courseExcelVOList.add(courseExcelVO);
        }
        ExcelUtil.exportExcel(response, courseExcelVOList, CourseExcelVO.class, "course");
    }

    @Override
    public void importExcel(MultipartFile file) {
        try {
            EasyExcel.read(file.getInputStream(), CourseExcelVO.class, new CourseExcelListener(courseMapper)).sheet().doRead();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
