package com.jiankun.gym.controller;


import com.jiankun.gym.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;


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

    @GetMapping("/list")
    public Result list() {
        System.out.println("AdminController.list");
        return Result.ok();
    }
}

