<script setup>
    import {ref} from "vue";
    import {User, Lock} from "@element-plus/icons-vue";
    import adminApi from "@/api/admin.js";
    import {ElMessage} from "element-plus";
    import {useTokenStore} from "@/store/token.js";
    import {useRouter} from "vue-router";
    import captchaApi from "@/api/captcha.js";

    const tokenStore = useTokenStore();
    const router = useRouter();

    const adminLoginDTO = ref({})

    const login = () => {
        adminApi.login(adminLoginDTO.value).then(result => {
            if (result.code == 0) {
                tokenStore.setToken(result.data)
                ElMessage.success(result.msg);
                router.push("/");
            } else {
                ElMessage.error(result.msg);
                loadCaptcha();
            }
        })
    }

    const captcha = ref('')
    const loadCaptcha = () => {
        captchaApi.captcha().then(result => {
            if (result.code == 0) {
                captcha.value = result.data.captcha;
                adminLoginDTO.value.uuid = result.data.uuid;
            }
        })
    }
    loadCaptcha();
</script>

<template>
    <div class="login-bg">
        <!-- 登录表单 -->
        <el-form class="form-login" ref="form" size="large" autocomplete="off" :model="adminLoginDTO" :rules="rules">
            <el-form-item>
                <h1>登录</h1>
            </el-form-item>
            <el-form-item prop="name">
                <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="adminLoginDTO.name"></el-input>
            </el-form-item>
            <el-form-item prop="password">
                <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                          v-model="adminLoginDTO.password"></el-input>
            </el-form-item>
            <el-form-item prop="captcha">
                <el-input name="captcha" v-model="adminLoginDTO.captcha" placeholder="验证码" auto-complete="off"
                          prefix-icon="el-icon-refresh"></el-input>
                <img width="160px" :src="captcha" @click="loadCaptcha" alt="验证码"/>
            </el-form-item>
            <el-form-item class="flex">
                <div class="flex">
                    <el-checkbox>记住我</el-checkbox>
                    <el-link type="primary" :underline="false">忘记密码？</el-link>
                </div>
            </el-form-item>
            <!-- 登录按钮 -->
            <el-form-item>
                <el-button class="button" type="primary" auto-insert-space @click="login">登录</el-button>
            </el-form-item>
        </el-form>
    </div>
</template>

<style scoped>
    .login-bg {
        height: 100%;
        background-repeat: no-repeat;
        background-position: center;
        background-attachment: fixed;
        background-size: cover;
    }

    .form-login {
        width: 280px;
        padding: 20px;
        position: absolute;
        top: 20%;
        left: calc(50% - 150px);
        background-color: #FFF;
        box-shadow: 10px 10px 30px #000;
    }
</style>