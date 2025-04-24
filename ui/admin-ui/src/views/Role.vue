<script setup>
    import roleApi from "@/api/role.js";
    import {reactive, ref} from 'vue'
    import {ElMessage, ElMessageBox} from "element-plus";
    import {Plus} from '@element-plus/icons-vue'
    import {useTokenStore} from '@/store/token.js'
    import permissionApi from "@/api/permission.js";

    const list = ref([]);
    const total = ref(0);
    const roleQuery = reactive({
        name: '',
        page: 1,
        limit: 3
    })

    // function loadData() {
    //     roleApi.list(roleQuery).then(result => {
    //         if (result.code == 0) {
    //             list.value = result.data.records;
    //             total.value = result.data.total;
    //         }
    //     })
    // }

    //查询
    const loadData = () => {
        roleApi.list(roleQuery).then(result => {
            if (result.code == 0) {
                list.value = result.data.records;
                total.value = result.data.total;
            }
        })
    }

    //单行删除
    const deleteById = (id) => {
        ElMessageBox.confirm(
            '你确定要删除吗?',
            '警告',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                lockScroll: false
            }
        )
            .then(() => {
                roleApi.deleteById(id).then(result => {
                    if (result.code == 0) {
                        ElMessage.success(result.msg);
                        loadData();
                    } else {
                        ElMessage.error(result.msg);
                    }
                })
            })
    }


    //批量删除
    const ids = ref([]);
    const handleSelectChange = (rows) => {
        console.log("多选:" + rows)
        ids.value = rows.map(row => row.id)
        console.log("ids=" + ids);
        console.log("ids.value=" + ids.value);
    }
    const deleteAll = () => {
        ElMessageBox.confirm(
            '你确定要删除吗?',
            '警告',
            {
                confirmButtonText: '确定',
                cancelButtonText: '取消',
                type: 'warning',
                lockScroll: false
            }
        )
            .then(() => {
                roleApi.deleteAll(ids.value).then(result => {
                    if (result.code == 0) {
                        ElMessage.success(result.msg);
                        loadData();
                    } else {
                        ElMessage.error(result.msg);
                    }
                })
            })
    }

    //添加、编辑
    const dialogFormVisible = ref(false);
    const role = ref({})
    const title = ref();
    const showAddDialog = () => {
        dialogFormVisible.value = true;
        role.value = {};
        title.value = "添加";
    }

    const showUpdateDialog = (id) => {
        dialogFormVisible.value = true;
        role.value = {};
        title.value = "编辑";
        roleApi.selectById(id).then(result => {
            if (result.code == 0) {
                role.value = result.data;
            }
        })
    }
    const addOrUpdate = () => {
        if (role.value.id) {
            roleApi.update(role.value).then(result => {
                if (result.code == 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        } else {
            roleApi.add(role.value).then(result => {
                if (result.code == 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        }
    }

    loadData();

    const tokenStore = useTokenStore();
    const headers = ref({
        //携带token传递到后端
        Authorization: tokenStore.token
    })

    const dialogPermissionVisible = ref(false)
    const treeData = ref([])
    const treeRef = ref()
    const defaultProps = ref({
        children: 'children',
        label: 'name'
    })
    const showAssignedPermissionDialog = (row) => {
        dialogPermissionVisible.value = true;
        role.value = row;
        permissionApi.selectAssignedPermission(row.id).then(result => {
            if (result.code == 0) {
                treeData.value = result.data.permissionVOList;
                // treeRef.value.setCheckedKeys(result.data.assignedPermissionIdList);
                let checkedLeafIdList = [];
                getCheckedLeafIdList(result.data.permissionVOList, result.data.assignedPermissionIdList, checkedLeafIdList);
                treeRef.value.setCheckedKeys(checkedLeafIdList);
            }
        })
    }

    const getCheckedLeafIdList = (permissionVOList, assignedPermissionIdList, checkedLeafIdList) => {
        permissionVOList.forEach(permissionVO => {
            assignedPermissionIdList.forEach(id => {
                //这个角色下面的权限，而且是没有孩子的叶子节点
                if (permissionVO.id == id && permissionVO.children.length == 0) {
                    checkedLeafIdList.push(id);
                } else if (permissionVO.id == id && permissionVO.children.length != 0) {
                    getCheckedLeafIdList(permissionVO.children, assignedPermissionIdList, checkedLeafIdList);
                }
            })
        });
    }

    const assignPermission = () => {
        const checkedNodes = treeRef.value.getCheckedNodes(false, true);
        console.log(checkedNodes);
        let permissionIds = checkedNodes.map(node => node.id);
        permissionIds = permissionIds.join(',');
        permissionApi.assignPermission(role.value.id, permissionIds).then(result => {
            if (result.code == 0) {
                ElMessage.success(result.msg);
                dialogPermissionVisible.value = false;
            }
        })
    }

</script>

<template>
    <el-card>
        <template #header>
            <div class="header">
                <el-button type="primary" @click="showAddDialog">添加</el-button>
                <el-button type="primary" @click="deleteAll">批量删除</el-button>
            </div>
        </template>
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item label="角色名称">
                <el-input v-model="roleQuery.name" placeholder="请输入角色名称"></el-input>
            </el-form-item>
            <el-form-item label="更新时间" :label-width="100">
                <el-date-picker
                    v-model="roleQuery.beginUpdateTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
                <el-date-picker
                    v-model="roleQuery.endUpdateTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="结束时间"
                />
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="loadData">查询</el-button>
            </el-form-item>
        </el-form>
        <el-table :data="list" style="width: 100%" ref="multipleTableRef" @selection-change="handleSelectChange">
            <el-table-column type="selection" width="55"/>
            <el-table-column fixed prop="id" label="ID"/>
            <el-table-column prop="name" label="角色名称"/>
            <el-table-column prop="code" label="角色编码"/>
            <el-table-column prop="description" label="描述"/>
            <el-table-column prop="updateTime" label="更新时间"/>
            <el-table-column align="center" width="200px" fixed="right" label="Operations">
                <template #default="{ row }">
                    <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
                    <el-button size="small" type="success" @click="showAssignedPermissionDialog(row)">权限</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            v-model:current-page="roleQuery.page"
            v-model:page-size="roleQuery.limit"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @change="loadData"
            style="margin-top: 20px; justify-content: flex-end"
        />
    </el-card>

    <!--添加、编辑弹出框-->
    <el-dialog v-model="dialogFormVisible" :title="title" width="60%" :lock-scroll="false">
        <el-form :model="role">
            <el-form-item label="角色名称" :label-width="80">
                <el-input v-model="role.name" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="角色编码" :label-width="80">
                <el-input v-model="role.code" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="描述" :label-width="80">
                <el-input v-model="role.description" autocomplete="off"/>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogFormVisible = false">取消</el-button>
                <el-button type="primary" @click="addOrUpdate">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

    <!--    权限分配的dialog-->
    <el-dialog
        title="分配权限"
        v-model="dialogPermissionVisible"
        width="40%" :lock-scroll="false">
        <el-tree
            :data="treeData"
            ref="treeRef"
            show-checkbox
            node-key="id"
            default-expand-all
            :props="defaultProps">
        </el-tree>
        <template #footer>
            <div class="dialog-footer">
                <el-button type="primary" @click="assignPermission()">保存</el-button>
                <el-button @click="dialogPermissionVisible = false">取消</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<style scoped>

</style>