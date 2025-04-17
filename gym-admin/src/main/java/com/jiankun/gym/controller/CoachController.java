package com.jiankun.gym.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.Coach;
import com.jiankun.gym.pojo.query.CoachQuery;
import com.jiankun.gym.service.ICoachService;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
@RestController
@RequestMapping("/coach")
public class CoachController {
    @Autowired
    private ICoachService coachService;

    @GetMapping("/list")
    public Result list(CoachQuery coachQuery) {
        System.out.println("CoachController.list");
//        IPage<Coach> page = new Page<>(CoachQuery.getPage(), CoachQuery.getLimit());
//        QueryWrapper<Coach> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(!ObjectUtils.isEmpty(CoachQuery.getName()), "name", CoachQuery.getName());
//        queryWrapper.like(!ObjectUtils.isEmpty(CoachQuery.getEmail()), "email", CoachQuery.getEmail());
//        coachService.page(page, queryWrapper);
//        Map<String, Object> map = new HashMap<>();
//        map.put("total", page.getTotal());
//        map.put("records", page.getRecords());
        IPage<Coach> page = coachService.list(coachQuery);
        return Result.ok(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        coachService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        coachService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("删除成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Coach Coach) {
        coachService.save(Coach);
        return Result.ok("添加成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Coach Coach) {
        coachService.updateById(Coach);
        return Result.ok("更新成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Coach coach = coachService.getById(id);
        return Result.ok(coach);
    }

    @GetMapping("/selectAll")
    public Result selectAll() {
        return Result.ok(coachService.list());
    }
}

