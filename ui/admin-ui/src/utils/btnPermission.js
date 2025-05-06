import {useAdminInfoStore} from '@/store/adminInfo.js'

//判断当前用户是否具有这个按钮权限 admin:add
export default function hasBtnPermission(permission) {
    const adminInfoStore = useAdminInfoStore()
    //获取当前用户所有的按钮权限
    let myBtns = adminInfoStore.btns;
    //['admin:add','admin:query']
    return myBtns.indexOf(permission) !== -1
}