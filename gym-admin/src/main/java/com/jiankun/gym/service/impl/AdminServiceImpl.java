package com.jiankun.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.jiankun.gym.mapper.AdminRoleMapper;
import com.jiankun.gym.mapper.PermissionMapper;
import com.jiankun.gym.mapper.RoleMapper;
import com.jiankun.gym.pojo.entity.Admin;
import com.jiankun.gym.mapper.AdminMapper;
import com.jiankun.gym.pojo.entity.AdminRole;
import com.jiankun.gym.pojo.entity.Role;
import com.jiankun.gym.pojo.query.AdminQuery;
import com.jiankun.gym.pojo.vo.RouterVO;
import com.jiankun.gym.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiankun.gym.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Autowired
    private PermissionServiceImpl permissionService;

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

    @Override
    public Map<String, Object> selectAssignedRole(Long adminId) {
        List<Role> roleList = roleMapper.selectList(null);
        List<AdminRole> assignedAdminRoleList = adminRoleMapper.selectList(new QueryWrapper<AdminRole>().eq("admin_id", adminId));
        List<Long> assignedRoleIdList = new ArrayList<>();
        for (AdminRole adminRole : assignedAdminRoleList) {
            assignedRoleIdList.add(adminRole.getRoleId());
        }
        Map<String, Object> map = new HashMap<>();
        map.put("roleList", roleList);
        map.put("assignedRoleIdList", assignedRoleIdList);
        return map;
    }

    @Override
    public void assignRole(Long adminId, Long[] roleIds) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("admin_id", adminId));
        for (Long roleId : roleIds) {
            AdminRole adminRole = new AdminRole();
            adminRole.setAdminId(adminId);
            adminRole.setRoleId(roleId);
            adminRoleMapper.insert(adminRole);
        }
    }

    @Override
    public Map<String, Object> selectAdminPermissionInfoById(Integer id) {
        //根据id查询用户信息
        Admin admin = adminMapper.selectById(id);
        //根据adminId查找菜单权限
        List<RouterVO> routerVOList = permissionService.selectRouterTreeByAdminId(id);
        //根据adminId查找按钮权限
        List<String> btnList = permissionService.selectBtnListByAdmminId(id);

        Map<String, Object> map = new HashMap<>();
        map.put("admin", admin);
        map.put("routers", routerVOList);
        map.put("btns", btnList);
        return map;
    }
}
