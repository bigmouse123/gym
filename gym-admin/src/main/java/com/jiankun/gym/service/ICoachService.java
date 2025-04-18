package com.jiankun.gym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.jiankun.gym.pojo.entity.Coach;
import com.baomidou.mybatisplus.extension.service.IService;
import com.jiankun.gym.pojo.query.CoachQuery;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
public interface ICoachService extends IService<Coach> {
    IPage<Coach> list(CoachQuery coachQuery);
}
