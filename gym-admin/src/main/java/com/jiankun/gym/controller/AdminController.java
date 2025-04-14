package com.jiankun.gym.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.pojo.Admin;
import com.jiankun.gym.pojo.query.AdminQuery;
import com.jiankun.gym.service.IAdminService;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private IAdminService adminService;

    @GetMapping("/list")
    public Result list(AdminQuery adminQuery) {
        System.out.println("AdminController.list");
//        IPage<Admin> page = new Page<>(adminQuery.getPage(), adminQuery.getLimit());
//        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
//        queryWrapper.like(!ObjectUtils.isEmpty(adminQuery.getName()), "name", adminQuery.getName());
//        queryWrapper.like(!ObjectUtils.isEmpty(adminQuery.getEmail()), "email", adminQuery.getEmail());
//        adminService.page(page, queryWrapper);
//        Map<String, Object> map = new HashMap<>();
//        map.put("total", page.getTotal());
//        map.put("records", page.getRecords());
        IPage<Admin> page = adminService.list(adminQuery);
        return Result.ok(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        adminService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        adminService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("删除成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Admin admin) {
        adminService.save(admin);
        return Result.ok("添加成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.updateById(admin);
        return Result.ok("更新成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }

}

