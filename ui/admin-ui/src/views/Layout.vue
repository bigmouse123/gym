<script setup>
    import {
        Management,
        Promotion,
        UserFilled,
        User,
        Crop,
        EditPen,
        SwitchButton,
        CaretBottom, Plus
    } from '@element-plus/icons-vue'
    import defaultAvatar from '@/assets/default.png'
    import {useTokenStore} from '@/store/token.js'
    import {useAdminInfoStore} from '@/store/adminInfo.js'
    //条目被点击后,调用的函数
    import {useRouter} from 'vue-router'
    import {ElMessage, ElMessageBox} from 'element-plus'
    import adminApi from "@/api/admin.js";
    import {ref} from "vue";

    const tokenStore = useTokenStore();
    const adminInfoStore = useAdminInfoStore();

    const router = useRouter();

    // 菜单  用户管理， 分类管理， 商品管理
    const menuData = ref([
        {
            name: '权限管理', icon: 'GobletFull', children: [
                {name: '管理员管理', icon: 'GobletSquareFull', path: "/admin"},
                {name: '角色管理', icon: 'TrendCharts', path: "/role"},
                {name: '权限管理', icon: 'TrendCharts', path: "/permission"},
            ]
        },
        {
            name: '商品分类管理', icon: 'Notebook', children: [
                {name: '商品管理', icon: 'Document', path: "/product"},
                {name: '分类管理', icon: 'Calendar', path: "/category"},
            ]
        }
    ]);

    const dialogUpdateAdminInfoVisible = ref(false)
    const admin = ref({})
    const handleCommand = (command) => {
        //判断指令
        if (command === 'logout') {
            //退出登录
            tokenStore.removeToken();
            adminInfoStore.removeAdminInfo()
            ElMessage.success("退出成功")
            //跳转到登录
            router.push('/login')
        } else if (command === 'updateAdminInfo') {
            dialogUpdateAdminInfoVisible.value = true;
            //这样下会有严重问题，两个数据是绑定在一起的，修改了admin里面数据，adminInfoStore.admin也会修改，
            //如果用户点击取消没有修改，就会造成adminInfoStore.admin里面数据修改了
            // admin.value = adminInfoStore.admin;
            Object.assign(admin.value, adminInfoStore.admin);
        } else if (command === 'resetPassword') {
            dialogResetPasswordDialog.value = true;
        } else {
            //路由
            router.push('/admin/' + command)
        }
    }

    adminApi.adminInfo().then(result => {
        //数据存储在pinia中
        adminInfoStore.setAdminInfo(result.data.admin)
        adminInfoStore.setBtns(result.data.btns)
        menuData.value = result.data.routers
    })

    const headers = ref({
        //携带token传递到后端
        Authorization: tokenStore.token
    })
    //图片上传
    const handleAvatarSuccess = (result) => {
        admin.value.avatar = result.data
    }

    const updateAdminInfo = () => {
        adminApi.update(admin.value).then(result => {
            if (result.code === 0) {
                ElMessage({message: result.msg, type: 'success',})
                dialogUpdateAdminInfoVisible.value = false
            } else {
                ElMessage.error(result.msg)
            }
        })
    }

    //重置密码
    const adminPasswordDTO = ref({
        'oldPassword': '',
        'newPassword': ''
    });
    const dialogResetPasswordDialog = ref(false)
    const resetForm = ref()
    //自定义确认密码的校验函数
    const rePasswordValid = (rule, value, callback) => {
        if (value == null || value === '') {
            return callback(new Error('请再次确认密码'))
        }
        //响应式对象要：registerData.value才能拿到值
        if (adminPasswordDTO.value.newPassword !== value) {
            return callback(new Error('两次输入密码不一致'))
        }

        callback()
    }
    const rules = ref({
        oldPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
        ],
        newPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {min: 3, max: 16, message: '密码长度必须为3~16位', trigger: 'blur'}
        ],
        reNewPassword: [
            {required: true, message: '请输入密码', trigger: 'blur'},
            {validator: rePasswordValid, trigger: 'blur'}
        ]
    })
    const resetPassword = async (formEl) => {
        if (!formEl) return
        await formEl.validate((valid, fields) => {
            if (valid) {
                adminApi.resetPassword(adminPasswordDTO.value).then(result => {
                    if (result.code === 0) {
                        ElMessage.success(result.msg)
                        dialogUpdateAdminInfoVisible.value = false
                        tokenStore.removeToken();
                        adminInfoStore.removeAdminInfo();
                        // 跳转到登录
                        router.push('/login')
                    } else {
                        ElMessage.error(result.msg)
                    }
                })
            } else {
                ElMessage.error('表单验证失败');
            }
        })
    }
</script>

<template>
    <!-- element-plus中的容器 -->
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo"></div>
            <!-- element-plus的菜单标签 -->
            <!--            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
                                 router>
                            <el-sub-menu>
                                <template #title>
                                    <el-icon>
                                        <UserFilled/>
                                    </el-icon>
                                    <span>权限管理</span>
                                </template>
                                <el-menu-item index="/admin">
                                    <el-icon>
                                        <User/>
                                    </el-icon>
                                    <span>管理员管理</span>
                                </el-menu-item>
                                <el-menu-item index="/role">
                                    <el-icon>
                                        <Crop/>
                                    </el-icon>
                                    <span>角色管理</span>
                                </el-menu-item>
                                <el-menu-item index="/permission">
                                    <el-icon>
                                        <EditPen/>
                                    </el-icon>
                                    <span>权限管理</span>
                                </el-menu-item>
                            </el-sub-menu>
                            <el-menu-item index="/coach">
                                <el-icon>
                                    <Management/>
                                </el-icon>
                                <span>教练管理</span>
                            </el-menu-item>
                            <el-menu-item index="/course">
                                <el-icon>
                                    <Promotion/>
                                </el-icon>
                                <span>课程管理</span>
                            </el-menu-item>
                            <el-menu-item index="/chart">
                                <el-icon>
                                    <Promotion/>
                                </el-icon>
                                <span>统计图表</span>
                            </el-menu-item>
                        </el-menu>-->
            <el-menu active-text-color="#ffd04b" background-color="#232323" text-color="#fff" router>
                <!-- 动态生成菜单 -->
                <template v-for="(menu, index) in menuData" :index="index.toString()">
                    <el-sub-menu v-if="menu.children?.length>0" :index="menu.name">
                        <template #title>
                            <component
                                class="icons"
                                :is="menu.icon"
                                style="width: 1em; height: 1em; margin-right: 8px">
                            </component>
                            <span>{{ menu.name }}</span>
                        </template>
                        <el-menu-item v-for="(child, ind) in menu.children" :index="child.path">
                            <el-icon>
                                <component
                                    :is="child.icon">
                                </component>
                            </el-icon>
                            <span>{{ child.name }}</span>
                        </el-menu-item>
                    </el-sub-menu>
                    <el-menu-item v-else :index="menu.path">
                        <el-icon>
                            <component :is="menu.icon"></component>
                        </el-icon>
                        <span>{{ menu.name }}</span>
                    </el-menu-item>
                </template>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div><strong>后台管理系统{{ zhansgan }}</strong></div>
                <!-- 下拉菜单 -->
                <!-- command: 条目被点击后会触发,在事件函数上可以声明一个参数,接收条目对应的指令 -->
                <el-dropdown placement="bottom-end" @command="handleCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="adminInfoStore.admin.avatar?adminInfoStore.admin.avatar:defaultAvatar"/>
                        <el-icon>
                            <CaretBottom/>
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="updateAdminInfo" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="resetPassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <!-- <div style="width: 1290px; height: 570px;border: 1px solid red;">
                    内容展示区
                </div> -->
                <router-view></router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>后台管理 ©2024 Created by Gao</el-footer>
        </el-container>
    </el-container>

    <!--个人信息-->
    <el-dialog v-model="dialogUpdateAdminInfoVisible" :title="个人信息" width="60%" :lock-scroll="false">
        <el-form :model="admin">
            <el-form-item label="姓名" :label-width="80">
                <el-input v-model="admin.name" autocomplete="off"/>
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
                <el-button @click="dialogUpdateAdminInfoVisible = false">取消</el-button>
                <el-button type="primary" @click="updateAdminInfo">
                    确定
                </el-button>
            </div>
        </template>
    </el-dialog>

    <el-dialog v-model="dialogResetPasswordDialog" title="重置密码" width="500" :lock-scroll="false">
        <el-form ref="resetForm" :rules="rules" :model="adminPasswordDTO">
            <el-form-item prop="oldPassword" label="原密码" :label-width="100">
                <el-input v-model="adminPasswordDTO.oldPassword" autocomplete="off"/>
            </el-form-item>
            <el-form-item prop="newPassword" label="新密码" :label-width="100">
                <el-input v-model="adminPasswordDTO.newPassword" autocomplete="off"/>
            </el-form-item>
            <el-form-item prop="reNewPassword" label="重复新密码" :label-width="100">
                <el-input v-model="adminPasswordDTO.reNewPassword" autocomplete="off"/>
            </el-form-item>
        </el-form>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="dialogResetPasswordDialog = false">取消</el-button>
                <el-button type="primary" @click="resetPassword(resetForm)">
                    确认
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

<style lang="scss" scoped>
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

    .layout-container {
        height: 100vh;

        .el-aside {
            background-color: #232323;

            &__logo {
                height: 120px;
                background: url('@/assets/logo.png') no-repeat center / 120px auto;
            }

            .el-menu {
                border-right: none;
            }
        }

        .el-header {
            background-color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;

            .el-dropdown__box {
                display: flex;
                align-items: center;

                .el-icon {
                    color: #999;
                    margin-left: 10px;
                }

                &:active,
                &:focus {
                    outline: none;
                }
            }
        }

        .el-footer {
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 14px;
            color: #666;
        }
    }
</style>