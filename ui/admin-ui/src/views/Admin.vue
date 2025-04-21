<script setup>
    import adminApi from "@/api/admin.js";
    import {reactive, ref} from 'vue'
    import {ElMessage, ElMessageBox} from "element-plus";
    import {Plus} from '@element-plus/icons-vue'
    import {useTokenStore} from '@/store/token.js'

    const list = ref([]);
    const total = ref(0);
    const adminQuery = reactive({
        name: '',
        phone: '',
        page: 1,
        limit: 3
    })

    // function loadData() {
    //     adminApi.list(adminQuery).then(result => {
    //         if (result.code == 0) {
    //             list.value = result.data.records;
    //             total.value = result.data.total;
    //         }
    //     })
    // }

    //查询
    const loadData = () => {
        adminApi.list(adminQuery).then(result => {
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
                adminApi.deleteById(id).then(result => {
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
                adminApi.deleteAll(ids.value).then(result => {
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
    const admin = ref({})
    const title = ref();
    const showAddDialog = () => {
        dialogFormVisible.value = true;
        admin.value = {};
        title.value = "添加";
    }

    const showUpdateDialog = (id) => {
        dialogFormVisible.value = true;
        admin.value = {};
        title.value = "编辑";
        adminApi.selectById(id).then(result => {
            if (result.code == 0) {
                admin.value = result.data;
            }
        })
    }
    const addOrUpdate = () => {
        if (admin.value.id) {
            adminApi.update(admin.value).then(result => {
                if (result.code == 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        } else {
            adminApi.add(admin.value).then(result => {
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

    //图片上传
    const handleAvatarSuccess = (result) => {
        admin.value.avatar = result.data
    }

    //更新状态
    const handleSwitchChange = (result) => {
        const admin = ref({});
        admin.id = result.id;
        admin.status = result.status;
        adminApi.update(admin).then(result => {
            if (result.code == 0) {
                ElMessage.success(result.msg)
            } else {
                ElMessage.error(result.msg)
            }
        })
    }

    loadData();

    const tokenStore = useTokenStore();
    const headers = ref({
        //携带token传递到后端
        Authorization: tokenStore.token
    })

    //分配角色
    //添加角色相关
    const dialogRoleVisible = ref(false)
    const roleList = ref([])
    const assignedRoleIdList = ref([])
    const showAssignedRoleDialog = (row) => {
        admin.value = row;
        adminApi.selectAssignedRole(row.id).then((result) => {
            roleList.value = result.data.roleList;
            assignedRoleIdList.value = result.data.assignedRoleIdList;
            dialogRoleVisible.value = true;
        });
    }
    const assignRole = () => {
        let roleIds = assignedRoleIdList.value.join(',');
        adminApi.assignRole(admin.value.id, roleIds).then((result) => {
            ElMessage.success(result.msg)
            dialogRoleVisible.value = false;
        });
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
            <el-form-item label="姓名">
                <el-input v-model="adminQuery.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="电话">
                <el-input v-model="adminQuery.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
            <el-form-item label="更新时间" :label-width="100">
                <el-date-picker
                    v-model="adminQuery.beginUpdateTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
                <el-date-picker
                    v-model="adminQuery.endUpdateTime"
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
            <el-table-column prop="name" label="名字"/>
            <el-table-column prop="password" label="密码"/>
            <el-table-column prop="phone" label="电话"/>
            <el-table-column prop="email" label="邮箱"/>
            <!--<el-table-column prop="image" label="头像"/>-->
            <el-table-column prop="avatar" label="头像">
                <template #default="scope">
                    <img :src="scope.row.avatar" style="max-height: 40px; max-width: 120px;"/>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态">
                <template #default="scope">
                    <el-switch
                        v-model="scope.row.status"
                        style="--el-switch-on-color: #13ce66; --el-switch-off-color: #ff4949"
                        :active-value="1"
                        :inactive-value="0"
                        @change="handleSwitchChange(scope.row)"
                    />
                </template>
            </el-table-column>
            <el-table-column prop="updateTime" label="更新时间"/>
            <el-table-column align="center" width="200px" fixed="right" label="Operations">
                <template #default="{ row }">
                    <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
                    <el-button type="success" size="small" @click="showAssignedRoleDialog(row)">角色</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            v-model:current-page="adminQuery.page"
            v-model:page-size="adminQuery.limit"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @change="loadData"
            style="margin-top: 20px; justify-content: flex-end"
        />
    </el-card>

    <!--添加、编辑弹出框-->
    <el-dialog v-model="dialogFormVisible" :title="title" width="60%" :lock-scroll="false">
        <el-form :model="admin">
            <el-form-item label="姓名" :label-width="80">
                <el-input v-model="admin.name" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="密码" :label-width="80">
                <el-input v-model="admin.password" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="80">
                <el-input v-model="admin.email" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="手机" :label-width="80">
                <el-input v-model="admin.phone" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="头像" :label-width="80">
                <el-upload
                    class="avatar-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers">
                    <img v-if="admin.avatar" :src="admin.avatar" class="avatar"/>
                    <el-icon v-else class="avatar-uploader-icon">
                        <Plus/>
                    </el-icon>
                </el-upload>
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

    <!-- 角色分配dialog-->
    <el-dialog title="分配角色" v-model="dialogRoleVisible" width="40%" :lock-scroll="false">
        <el-form ref="form" :model="admin" label-width="80px">
            <el-form-item label="用户名">
                <el-input v-model="admin.name" disabled></el-input>
            </el-form-item>
            <el-form-item label="角色列表">
                <el-checkbox-group v-model="assignedRoleIdList">
                    <el-checkbox v-for="role in roleList" :key="role.id" :label="role.id">{{ role.name }}</el-checkbox>
                </el-checkbox-group>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="assignRole">保存</el-button>
                <el-button @click="dialogRoleVisible = false">取消</el-button>
            </el-form-item>
        </el-form>
    </el-dialog>
</template>

<style scoped>
    .avatar-uploader .avatar {
        width: 178px;
        height: 178px;
        display: block;
    }
</style>

<style>
    .avatar-uploader .el-upload {
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);
    }

    .avatar-uploader .el-upload:hover {
        border-color: var(--el-color-primary);
    }

    .el-icon.avatar-uploader-icon {
        font-size: 28px;
        color: #8c939d;
        width: 178px;
        height: 178px;
        text-align: center;
    }
</style>