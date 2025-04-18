package com.jiankun.gym.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.dto.AdminLoginDTO;
import com.jiankun.gym.pojo.entity.Admin;
import com.jiankun.gym.pojo.query.AdminQuery;
import com.jiankun.gym.service.IAdminService;
import com.jiankun.gym.util.JwtUtil;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
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
    @Autowired
    private RedisTemplate redisTemplate;

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

    @PutMapping("/login")
    public Result login(@RequestBody AdminLoginDTO adminLoginDTO) {
        //判断验证码是否正确
        String captcha = (String) redisTemplate.opsForValue().get("captcha:" + adminLoginDTO.getUuid());
        if (ObjectUtils.isEmpty(captcha)) {
            return Result.error("验证码失效");
        }
        if (!captcha.equals(adminLoginDTO.getCaptcha())) {
            return Result.error("验证码错误");
        }

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(!ObjectUtils.isEmpty(adminLoginDTO.getName()), "name", adminLoginDTO.getName());
        Admin loginAdmin = adminService.getOne(queryWrapper);

        //判断用户是否存在
        if (ObjectUtils.isEmpty(loginAdmin)) {
            return Result.error("用户名错误");
        }
        //判断密码是否正确
        if (!loginAdmin.getPassword().equals(adminLoginDTO.getPassword())) {
            return Result.error("密码错误");
        }

        //登陆成功
        Map<String, Object> map = new HashMap<>();
        map.put("id", loginAdmin.getId());
        map.put("name", loginAdmin.getName());
        String token = JwtUtil.createToken(map);
        return Result.ok("登录成功", token);
    }

    @GetMapping("/adminInfo")
    public Result adminInfo(@RequestHeader(name = "Authorization") String token) {
        Map<String, Object> map = JwtUtil.parseToken(token);
        Integer id = (Integer) map.get("id");
        Admin admin = adminService.getById(id);
        return Result.ok(admin);
    }

}

