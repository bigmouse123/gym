// 创建一个路由器，并暴露出去
// 第一步：引入createRouter
import {createRouter, createWebHistory} from 'vue-router'
// 引入一个一个可能要呈现组件
import Layout from '@/views/Layout.vue'
import Login from '@/views/Login.vue'
import Admin from '@/views/Admin.vue'
import Coach from '@/views/Coach.vue'
import Course from '@/views/Course.vue'
import Chart from '@/views/Chart.vue'
import Role from "@/views/Role.vue";

//创建路由器
const router = createRouter({
    history: createWebHistory(),
    routes: [
        {path: '/login', component: Login},
        {
            path: '/', component: Layout, children: [
                {path: '/admin', component: Admin},
                {path: '/role', component: Role},
                {path: '/coach', component: Coach},
                {path: '/course', component: Course},
                {path: '/chart', component: Chart}
            ]
        }
    ]
})

// 暴露出去router
export default router