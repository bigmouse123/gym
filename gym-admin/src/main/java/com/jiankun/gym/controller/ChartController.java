package com.jiankun.gym.controller;

import com.jiankun.gym.pojo.vo.CoachCountVO;
import com.jiankun.gym.service.ICoachService;
import com.jiankun.gym.service.ICourseService;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author OfferKing
 * @version 1.0
 * @date 2025/4/20 23:36
 */
@RestController
@RequestMapping("/chart")
public class ChartController {
    @Autowired
    private ICoachService coachService;

    @GetMapping("/selectCoachCount")
    public Result selectCoachCount() {
        List<CoachCountVO> list = coachService.selectCoachCount();
        return Result.ok(list);
    }
}
