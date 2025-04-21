package com.jiankun.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.mapper.RoleMapper;
import com.jiankun.gym.pojo.entity.Role;
import com.jiankun.gym.pojo.entity.Role;
import com.jiankun.gym.mapper.RoleMapper;
import com.jiankun.gym.pojo.query.RoleQuery;
import com.jiankun.gym.service.IRoleService;
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
 * @since 2025-04-21
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {
    @Autowired
    private RoleMapper roleMapper;

    @Override
    public IPage<Role> list(RoleQuery roleQuery) {
        IPage<Role> page = new Page<>(roleQuery.getPage(), roleQuery.getLimit());
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!ObjectUtils.isEmpty(roleQuery.getName()), "name", roleQuery.getName());
        queryWrapper.between(!ObjectUtils.isEmpty(roleQuery.getBeginUpdateTime()) && !ObjectUtils.isEmpty(roleQuery.getEndUpdateTime()),
                "update_time", roleQuery.getBeginUpdateTime(), roleQuery.getEndUpdateTime());
        roleMapper.selectPage(page, queryWrapper);
        return page;
    }
}
