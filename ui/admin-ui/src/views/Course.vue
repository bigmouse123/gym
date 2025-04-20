<script setup>
    import courseApi from "@/api/course.js";
    import coachApi from "@/api/coach.js";
    import {reactive, ref} from 'vue'
    import {ElMessage, ElMessageBox} from "element-plus";
    import {useTokenStore} from '@/store/token.js'
    import {
        Delete,
        Edit,
        Upload,
        Download,
        Plus
    } from '@element-plus/icons-vue'


    const list = ref([]);
    const total = ref(0);
    const courseQuery = reactive({
        page: 1,
        limit: 3
    })

    // function loadData() {
    //     courseApi.list(courseQuery).then(result => {
    //         if (result.code == 0) {
    //             list.value = result.data.records;
    //             total.value = result.data.total;
    //         }
    //     })
    // }

    //查询
    const loadData = () => {
        courseApi.list(courseQuery).then(result => {
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
                courseApi.deleteById(id).then(result => {
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
                courseApi.deleteAll(ids.value).then(result => {
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
    const course = ref({})
    const title = ref();
    const showAddDialog = () => {
        dialogFormVisible.value = true;
        course.value = {};
        title.value = "添加";
    }

    const showUpdateDialog = (id) => {
        dialogFormVisible.value = true;
        course.value = {};
        title.value = "编辑";
        courseApi.selectById(id).then(result => {
            if (result.code == 0) {
                course.value = result.data;
            }
        })
    }
    const addOrUpdate = () => {
        if (course.value.id) {
            courseApi.update(course.value).then(result => {
                if (result.code == 0) {
                    ElMessage.success(result.msg)
                    dialogFormVisible.value = false
                    loadData()
                } else {
                    ElMessage.error(result.msg)
                }
            })
        } else {
            courseApi.add(course.value).then(result => {
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
        course.value.image = result.data
    }

    //更新状态
    const handleSwitchChange = (result) => {
        const course = ref({});
        course.id = result.id;
        course.status = result.status;
        courseApi.update(course).then(result => {
            if (result.code == 0) {
                ElMessage.success(result.msg)
            } else {
                ElMessage.error(result.msg)
            }
        })
    }

    loadData();

    //教练下拉框
    const coachList = ref([]);
    const loadCoachList = () => {
        coachApi.selectAll().then(result => {
            if (result.code == 0) {
                coachList.value = result.data;
            }
        })
    }

    const selectChange = (value) => {
        console.log(value)
    }

    loadCoachList();

    const tokenStore = useTokenStore();
    const headers = ref({
        //携带token传递到后端
        Authorization: tokenStore.token
    })

    //导入、导出excel
    const exportExcel = () => {
        courseApi.exportExcel().then((result) => {
            let blob = new Blob([result], {type: "application/xlsx"});
            let url = window.URL.createObjectURL(blob);
            const link = document.createElement("a"); // 创建a标签
            link.href = url;
            link.download = "课程列表.xlsx"; // 重命名文件
            link.click();
            URL.revokeObjectURL(url);
        });
    }

    const importExcelSuccess = (result) => {
        if (result.code == 0) {
            ElMessage.success(result.msg)
            loadData()
        }
    }
</script>

<template>
    <el-card>
        <template #header>
            <div class="header">
                <el-button type="primary" @click="showAddDialog">添加</el-button>
                <el-button type="primary" @click="deleteAll">批量删除</el-button>
                <el-button type="primary" :icon="Download" @click="exportExcel">导出Excel</el-button>
                <el-upload
                    :icon="Upload"
                    class="inline-block"
                    multiple=""
                    method="post"
                    action="/api/course/importExcel"
                    style="display:inline-block;margin-left: 12px"
                    accept=".xlsx,.xls"
                    :show-file-list="false"
                    :on-success="importExcelSuccess"
                    :headers="headers"
                    name="file">
                    <el-button type="primary" :icon="Upload">导入Excel</el-button>
                </el-upload>
            </div>
        </template>
        <el-form :inline="true" :model="formInline" class="demo-form-inline">
            <el-form-item label="课程名称">
                <el-input v-model="courseQuery.name" placeholder="请输入课程名称"></el-input>
            </el-form-item>
            <el-form-item label="教练">
                <el-select clearable v-model="courseQuery.coachId" @change="selectChange" placeholder="请选择教练"
                           style="width: 140px">
                    <el-option
                        v-for="coach in coachList"
                        :key="coach.id"
                        :label="coach.name"
                        :value="coach.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="创建时间" :label-width="100">
                <el-date-picker
                    v-model="courseQuery.beginTime"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
                <el-date-picker
                    v-model="courseQuery.endTime"
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
            <el-table-column prop="name" label="课程名称"/>
            <el-table-column prop="coachName" label="教练"/>
            <el-table-column prop="time" label="课程时间"/>
            <el-table-column prop="length" label="课程时长"/>
            <el-table-column prop="maxCount" label="最大人数"/>
            <el-table-column prop="room" label="教室"/>
            <!--<el-table-column prop="image" label="头像"/>-->
            <el-table-column prop="avatar" label="课程封面">
                <template #default="scope">
                    <img :src="scope.row.image" style="max-height: 40px; max-width: 120px;"/>
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
            <el-table-column align="center" width="200px" fixed="right" label="Operations">
                <template #default="{ row }">
                    <el-button size="small" type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
                    <el-button size="small" type="danger" @click="deleteById(row.id)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>
        <el-pagination
            v-model:current-page="courseQuery.page"
            v-model:page-size="courseQuery.limit"
            :page-sizes="[10, 20, 30, 40]"
            layout="total, sizes, prev, pager, next, jumper"
            :total="total"
            @change="loadData"
            style="margin-top: 20px; justify-content: flex-end"
        />
    </el-card>

    <!--添加、编辑弹出框-->
    <el-dialog v-model="dialogFormVisible" :title="title" width="60%" :lock-scroll="false">
        <el-form :model="course">
            <el-form-item label="课程名称" :label-width="80">
                <el-input v-model="course.name" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="教练" :label-width="80">
                <el-select clearable v-model="course.coachId" placeholder="请选择教练"
                           style="width: 140px">
                    <el-option
                        v-for="coach in coachList"
                        :key="coach.id"
                        :label="coach.name"
                        :value="coach.id"
                    />
                </el-select>
            </el-form-item>
            <el-form-item label="课程时间" :label-width="80">
                <el-date-picker
                    v-model="course.time"
                    type="datetime"
                    value-format="YYYY-MM-DD HH:mm:ss"
                    placeholder="开始时间"
                />
            </el-form-item>
            <el-form-item label="课程时长" :label-width="80">
                <el-input-number v-model="course.length" :min="1" :max="100" :step="1" placeholder="课程时长"/>
            </el-form-item>
            <el-form-item label="最大人数" :label-width="80">
                <el-input-number v-model="course.maxCount" :min="10" :max="120" :step="10" placeholder="最大人数"/>
            </el-form-item>
            <el-form-item label="教室" :label-width="80">
                <el-input v-model="course.room" autocomplete="off"/>
            </el-form-item>
            <el-form-item label="封面" :label-width="80">
                <el-upload
                    class="avatar-uploader"
                    action="/api/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :headers="headers">
                    <img v-if="course.image" :src="course.image" class="avatar"/>
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