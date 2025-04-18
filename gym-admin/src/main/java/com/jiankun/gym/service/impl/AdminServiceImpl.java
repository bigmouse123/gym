package com.jiankun.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.pojo.entity.Admin;
import com.jiankun.gym.mapper.AdminMapper;
import com.jiankun.gym.pojo.query.AdminQuery;
import com.jiankun.gym.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public IPage<Admin> list(AdminQuery adminQuery) {
        IPage<Admin> page = new Page<>(adminQuery.getPage(), adminQuery.getLimit());
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(adminQuery.getName()), "name", adminQuery.getName());
        queryWrapper.like(!ObjectUtils.isEmpty(adminQuery.getPhone()), "phone", adminQuery.getPhone());
        queryWrapper.between(!ObjectUtils.isEmpty(adminQuery.getBeginUpdateTime()) && !ObjectUtils.isEmpty(adminQuery.getEndUpdateTime()),
                "update_time", adminQuery.getBeginUpdateTime(), adminQuery.getEndUpdateTime());
        adminMapper.selectPage(page, queryWrapper);
        return page;
    }
}
