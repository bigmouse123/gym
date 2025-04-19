<script setup>
    import coachApi from "@/api/coach.js";
    import {reactive, ref} from 'vue'
    import {ElMessage, ElMessageBox} from "element-plus";
    import {Plus} from '@element-plus/icons-vue'
    import WangEditor from "@/components/WangEditor.vue";
    import {useTokenStore} from "@/store/token.js";

    const list = ref([]);
    const total = ref(0);
    const coachQuery = reactive({
        name: '',
        phone: '',
        page: 1,
        limit: 3
    })

    // function loadData() {
    //     coachApi.list(coachQuery).then(result => {
    //         if (result.code == 0) {
    //             list.value = result.data.records;
    //             total.value = result.data.total;
    //         }
    //     })
    // }

    //查询
    const loadData = () => {
        coachApi.list(coachQuery).then(result => {
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
                coachApi.deleteById(id).then(result => {
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
                coachApi.deleteAll(ids.value).then(result => {
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
    const coach = ref({})
    const title = ref();
    const showAddDialog = () => {
        dialogFormVisible.value = true;
        coach.value = {};
        title.value = "添加";
    }

    const showUpdateDialog = (id) => {
        dialogFormVisible.value = true;
        coach.value = {};
        title.value = "编辑";
        coachApi.selectById(id).then(result => {
            if (result.code == 0) {
                coach.value = result.data;
            }
        })
    }
    const addOrUpdate = () => {
        if (coach.value.id) {
            coachApi.update(coach.value).then(result => {
                if (result.code == 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        } else {
            coachApi.add(coach.value).then(result => {
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
        coach.value.avatar = result.data
    }

    //更新状态
    const handleSwitchChange = (result) => {
        const coach = ref({});
        coach.id = result.id;
        coach.status = result.status;
        coachApi.update(coach).then(result => {
            if (result.code == 0) {
                ElMessage.success(result.msg)
            } else {
                ElMessage.error(result.msg)
            }
        })
    }

    //富文本编辑器
    const onEditorChange = (introduction) => {
        console.log(introduction);
        coach.value.introduction = introduction;
    }

    //简介
    const dialogIntroductionVisible = ref(false)
    const introduction = ref('')
    const showIntroduction = (intro) => {
        dialogIntroductionVisible.value = true
        introduction.value = intro
    }

    loadData();

    const tokenStore = useTokenStore();
    const headers = ref({
        //携带token传递到后端
        Authorization: tokenStore.token
    })

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
                <el-input v-model="coachQuery.name" placeholder="请输入姓名"></el-input>
            </el-form-item>
            <el-form-item label="电话">
                <el-input v-model="coachQuery.phone" placeholder="请输入电话"></el-input>
            </el-form-item>
            <el-form-item label="更新时间" :label-width="100">
                <el-date-picker
                    v-model="coachQuery.beginUpdateTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
                <el-date-picker
                    v-model="coachQuery.endUpdateTime"
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
            <!--            <el-table-column prop="introduction" label="个人简介"/>-->
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
                    <el-button size="small" type="success" @click="showIntroduction(row.introduction)">简介</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            v-model:current-page="coachQuery.page"
            v-model:page-size="coachQuery.limit"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @change="loadData"
            style="margin-top: 20px; justify-content: flex-end"
        />
    </el-card>

    <!--添加、编辑弹出框-->
    <el-dialog v-model="dialogFormVisible" :title="title" width="60%" :lock-scroll="false">
        <el-form :model="coach">
            <el-form-item label="姓名" :label-width="80">
                <el-input v-model="coach.name" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="密码" :label-width="80">
                <el-input v-model="coach.password" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="邮箱" :label-width="80">
                <el-input v-model="coach.email" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="手机" :label-width="80">
                <el-input v-model="coach.phone" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="头像" :label-width="80">
                <el-upload
                    class="avatar-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers">
                    <img v-if="coach.avatar" :src="coach.avatar" class="avatar"/>
                    <el-icon v-else class="avatar-uploader-icon">
                        <Plus/>
                    </el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="个人简介" :label-width="80">
                <WangEditor :initValue="coach.introduction" @getEditorContent="onEditorChange" v-if="dialogFormVisible"
                            @close="dialogFormVisible = false">
                </WangEditor>
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

    <el-dialog v-model="dialogIntroductionVisible" width="60%" :lock-scroll="false">
        <div v-html="introduction"></div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogIntroductionVisible = false">关闭</el-button>
            </div>
        </template>
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