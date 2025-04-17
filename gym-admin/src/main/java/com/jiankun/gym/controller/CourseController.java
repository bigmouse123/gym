package com.jiankun.gym.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.Course;
import com.jiankun.gym.pojo.query.CourseQuery;
import com.jiankun.gym.service.ICourseService;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
@RestController
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private ICourseService courseService;

    @GetMapping("/list")
    public Result list(CourseQuery courseQuery) {
        System.out.println("CourseController.list");
//        IPage<Course> page = new Page<>(CourseQuery.getPage(), CourseQuery.getLimit());
//        QueryWrapper<Course> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(!ObjectUtils.isEmpty(CourseQuery.getName()), "name", CourseQuery.getName());
//        queryWrapper.like(!ObjectUtils.isEmpty(CourseQuery.getEmail()), "email", CourseQuery.getEmail());
//        courseService.page(page, queryWrapper);
//        Map<String, Object> map = new HashMap<>();
//        map.put("total", page.getTotal());
//        map.put("records", page.getRecords());
        IPage<Course> page = courseService.list(courseQuery);
        return Result.ok(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        courseService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        courseService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("删除成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Course Course) {
        courseService.save(Course);
        return Result.ok("添加成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Course Course) {
        courseService.updateById(Course);
        return Result.ok("更新成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Course course = courseService.getById(id);
        return Result.ok(course);
    }
}

