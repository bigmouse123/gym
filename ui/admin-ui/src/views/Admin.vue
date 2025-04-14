<script setup>
    import adminApi from "@/api/admin.js";
    import {reactive, ref} from 'vue'
    import {ElMessage, ElMessageBox} from "element-plus";

    const list = ref([0]);
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

    const loadData = () => {
        adminApi.list(adminQuery).then(result => {
            if (result.code == 0) {
                list.value = result.data.records;
                total.value = result.data.total;
            }
        })
    }

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
    loadData();
</script>

<template>
    <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="姓名">
            <el-input v-model="adminQuery.name" placeholder="请输入姓名"></el-input>
        </el-form-item>
        <el-form-item label="电话">
            <el-input v-model="adminQuery.phone" placeholder="请输入电话"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="loadData">查询</el-button>
        </el-form-item>
    </el-form>
    <el-table :data="list" style="width: 100%">
        <el-table-column type="selection" width="55"/>
        <el-table-column fixed prop="id" label="ID"/>
        <el-table-column prop="name" label="名字"/>
        <el-table-column prop="password" label="密码"/>
        <el-table-column prop="phone" label="电话"/>
        <el-table-column prop="email" label="邮箱"/>
        <el-table-column prop="image" label="头像"/>
        <el-table-column prop="updateTime" label="更新时间"/>
        <el-table-column fixed="right" label="Operations">
            <template #default="{ row }">
                <el-button type="primary" @click="showUpdateDialog(row.id)">编辑</el-button>
                <el-button type="danger" @click="deleteById(row.id)">删除</el-button>
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
</template>

<style scoped>

</style>