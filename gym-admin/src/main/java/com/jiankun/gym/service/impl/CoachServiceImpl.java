package com.jiankun.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.pojo.entity.Coach;
import com.jiankun.gym.mapper.CoachMapper;
import com.jiankun.gym.pojo.query.CoachQuery;
import com.jiankun.gym.service.ICoachService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Li
 * @since 2025-04-14
 */
@Service
public class CoachServiceImpl extends ServiceImpl<CoachMapper, Coach> implements ICoachService {
    @Autowired
    private CoachMapper coachMapper;

    @Override
    public IPage<Coach> list(CoachQuery coachQuery) {
        IPage<Coach> page = new Page<>(coachQuery.getPage(), coachQuery.getLimit());
        QueryWrapper<Coach> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(coachQuery.getName()), "name", coachQuery.getName());
        queryWrapper.like(!ObjectUtils.isEmpty(coachQuery.getPhone()), "phone", coachQuery.getPhone());
        queryWrapper.between(!ObjectUtils.isEmpty(coachQuery.getBeginUpdateTime()) && !ObjectUtils.isEmpty(coachQuery.getEndUpdateTime()), "update_time", coachQuery.getBeginUpdateTime(), coachQuery.getEndUpdateTime());
        coachMapper.selectPage(page, queryWrapper);
        return page;
    }
}
