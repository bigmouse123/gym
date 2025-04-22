package com.jiankun.gym.controller;


import com.jiankun.gym.pojo.entity.Permission;
import com.jiankun.gym.pojo.vo.PermissionVO;
import com.jiankun.gym.service.IPermissionService;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author Li
 * @since 2025-04-21
 */
@RestController
@RequestMapping("/permission")
public class PermissionController {
    @Autowired
    private IPermissionService permissionService;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/selectPermissionTree")
    public Result selectPermissionTree() {
        List<PermissionVO> list = permissionService.selectPermissionTree();
        return Result.ok(list);
    }

    @DeleteMapping("/deleteById/{id}")
    public Result deleteById(@PathVariable Integer id) {
        permissionService.removeById(id);
        return Result.ok("删除成功");
    }

    @PostMapping("/add")
    public Result add(@RequestBody Permission permission) {
        permissionService.save(permission);
        return Result.ok("添加成功");
    }

    @PutMapping("/update")
    public Result update(@RequestBody Permission permission) {
        permissionService.updateById(permission);
        return Result.ok("更新成功");
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Permission permission = permissionService.getById(id);
        return Result.ok(permission);
    }
}

