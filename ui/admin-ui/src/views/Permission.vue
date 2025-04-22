<script setup>
    import permissionApi from '@/api/permission.js'
    import {ref, reactive} from 'vue'
    import {ElMessage, ElMessageBox} from "element-plus";

    //保存返回树形结构数据，但是本质上List<PermissionVO> list;
    const list = ref([])

    const loadData = () => {
        permissionApi.selectPermissionTree().then(result => {
            list.value = result.data
        })
    }
    //加载数据
    loadData()
</script>

<template>
    <el-button type="success" size="mini" @click="showAddDialog()">添加顶级菜单</el-button>
    <el-table
        :data="list"
        style="width: 100%; margin-bottom: 20px"
        row-key="id"
        border
    >
        <el-table-column prop="name" label="名称"/>
        <el-table-column prop="icon" width="80px" label="图标" #default="scope">
            <el-icon>
                <component :is="scope.row.icon"/>
            </el-icon>
        </el-table-column>
        <el-table-column prop="type" label="权限类型" #default="scope">
            <el-tag v-if="scope.row.type == 0">目录权限</el-tag>
            <el-tag v-if="scope.row.type == 1" type="success">菜单权限</el-tag>
            <el-tag v-if="scope.row.type == 2" type="warning">按钮权限</el-tag>
        </el-table-column>
        <el-table-column prop="path" label="路由地址"></el-table-column>
        <el-table-column prop="permissionValue" label="按钮权限"></el-table-column>
        <el-table-column prop="sort" label="排序"></el-table-column>
        <el-table-column label="操作" align="center" width="200px" fixed="right" #default="scope">
            <el-button size="small"
                       type="success"
                       @click="showAddDialog(scope.row)"
            >添加
            </el-button>
            <el-button size="small"
                       type="primary"
                       @click="showUpdateDialog(scope.row)"
            >修改
            </el-button>
            <el-button size="small"
                       type="danger"
                       @click="deleteById(scope.row.id)"
                       :disabled="scope.row.children?.length > 0"
            >删除
            </el-button>
        </el-table-column>
    </el-table>
</template>

<style scoped>

</style>