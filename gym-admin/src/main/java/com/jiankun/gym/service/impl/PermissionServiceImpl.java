package com.jiankun.gym.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jiankun.gym.mapper.RolePermissionMapper;
import com.jiankun.gym.pojo.entity.Permission;
import com.jiankun.gym.mapper.PermissionMapper;
import com.jiankun.gym.pojo.entity.RolePermission;
import com.jiankun.gym.pojo.vo.PermissionVO;
import com.jiankun.gym.pojo.vo.RouterVO;
import com.jiankun.gym.service.IPermissionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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
 * @since 2025-04-21
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements IPermissionService {
    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolePermissionMapper rolePermissionMapper;


    @Override
    public List<PermissionVO> selectPermissionTree() {
        //1.查找所有分类，返回所有权限的集合
        QueryWrapper<Permission> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("sort");
        List<Permission> permissionList = permissionMapper.selectList(queryWrapper);

        List<PermissionVO> permissionVOList = new ArrayList<>();
        permissionList.forEach(permission -> {
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(permission, permissionVO);
            permissionVOList.add(permissionVO);
        });

        List<PermissionVO> permissionVOTree = buildTree(permissionVOList);
        return permissionVOTree;
    }

    @Override
    public Map<String, Object> selectAssignedPermission(Long roleId) {
        List<PermissionVO> permissionVOList = selectPermissionTree();
        //根据roleId查找该角色下所有的权限
        QueryWrapper<RolePermission> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("role_id", roleId).select("permission_id");
        List<RolePermission> assignedRolePermissionList = rolePermissionMapper.selectList(queryWrapper);
        List<Long> assignedPermissionIdList = new ArrayList<>();
        assignedRolePermissionList.forEach(rolePermission -> {
            assignedPermissionIdList.add(rolePermission.getPermissionId());
        });
        Map<String, Object> map = new HashMap<>();
        map.put("permissionVOList", permissionVOList);
        map.put("assignedPermissionIdList", assignedPermissionIdList);
        return map;
    }

    @Override
    public void assignPermission(Long roleId, Long[] permissionIds) {
        rolePermissionMapper.delete(new QueryWrapper<RolePermission>().eq("role_id", roleId));
        for (Long permissionId : permissionIds) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }
    }

    @Override
    public List<RouterVO> selectRouterTreeByAdminId(Integer id) {
        //查找用户所有的权限
        List<Permission> permissionList = permissionMapper.selectPermissionByAdminId(id);
        List<PermissionVO> permissionVOList = new ArrayList<>();
        permissionList.forEach(permission -> {
            PermissionVO permissionVO = new PermissionVO();
            BeanUtils.copyProperties(permission, permissionVO);
            permissionVOList.add(permissionVO);
        });
        //构建树形结构
        List<PermissionVO> permissionVOTree = buildTree(permissionVOList);
        //转换为路由结构
        List<RouterVO> routerTreeList = buildRouterTree(permissionVOTree);
        return routerTreeList;
    }

    @Override
    public List<String> selectBtnListByAdmminId(Integer id) {
        List<Permission> permissionList = permissionMapper.selectPermissionByAdminId(id);
        List<String> btnList = new ArrayList<>();
        for (Permission permission : permissionList) {
            if (permission.getType() == 2) {
                btnList.add(permission.getPermissionValue());
            }
        }
        return btnList;
    }

    private List<RouterVO> buildRouterTree(List<PermissionVO> permissionVOTree) {
        List<RouterVO> routerTreeList = new ArrayList<>();
        //首先遍历一级目录
        permissionVOTree.forEach(permissionVO -> {
            RouterVO routerVO = new RouterVO();
            routerVO.setName(permissionVO.getName());
            routerVO.setIcon(permissionVO.getIcon());
            routerVO.setPath(permissionVO.getPath());
            List<PermissionVO> childPermissionVOTree = permissionVO.getChildren();
            if (!CollectionUtils.isEmpty(childPermissionVOTree) && permissionVO.getType() == 0) {
                List<RouterVO> childRouterTree = new ArrayList<>();
                childPermissionVOTree.forEach(childPermissionVO -> {
                    RouterVO childRouter = new RouterVO();
                    childRouter.setName(childPermissionVO.getName());
                    childRouter.setIcon(childPermissionVO.getIcon());
                    childRouter.setPath(childPermissionVO.getPath());
                    childRouterTree.add(childRouter);
                });
                routerVO.setChildren(childRouterTree);
            }
            routerTreeList.add(routerVO);
        });
        return routerTreeList;
    }

    private List<PermissionVO> buildTree(List<PermissionVO> permissionVOList) {
        List<PermissionVO> treeList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionVOList)) {
            for (PermissionVO permissionVO : permissionVOList) {
                if (permissionVO.getParentId() == 0) {
                    permissionVO.setChildren(buildChildTree(permissionVO, permissionVOList));
                    treeList.add(permissionVO);
                }
            }
        }
        return treeList;
    }

    /**
     * @param permissionVO     childTree的父亲
     * @param permissionVOList 所有权限的集合
     * @return childTree
     */
    private List<PermissionVO> buildChildTree(PermissionVO permissionVO, List<PermissionVO> permissionVOList) {
        List<PermissionVO> childTree = new ArrayList<>();
        if (!CollectionUtils.isEmpty(permissionVOList)) {
            for (PermissionVO permission : permissionVOList) {
                if (permissionVO.getId().longValue() == permission.getParentId().longValue()) {
                    permission.setChildren(buildChildTree(permission, permissionVOList));
                    childTree.add(permission);
                }
            }
        }
        return childTree;
    }
}
