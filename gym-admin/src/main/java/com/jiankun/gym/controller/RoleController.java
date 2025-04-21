package com.jiankun.gym.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.entity.Role;
import com.jiankun.gym.pojo.query.RoleQuery;
import com.jiankun.gym.service.IRoleService;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Li
 * @since 2025-04-21
 */


@RestController
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private IRoleService roleService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/list")
    public Result list(RoleQuery roleQuery) {
        System.out.println("RoleController.list");
        IPage<Role> page = roleService.list(roleQuery);
        return Result.ok(page);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        roleService.removeById(id);
        return Result.ok("删除成功");
    }

    @DeleteMapping("/deleteAll/{ids}")
    public Result deleteAll(@PathVariable Integer[] ids) {
        roleService.removeBatchByIds(Arrays.asList(ids));
        return Result.ok("删除成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Role role) {
        roleService.save(role);
        return Result.ok("添加成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Role role) {
        roleService.updateById(role);
        return Result.ok("更新成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Role role = roleService.getById(id);
        return Result.ok(role);
    }
}


