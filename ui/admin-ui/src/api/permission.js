import service from '@/utils/request.js'

const permissionApi = {
    selectPermissionTree() {
        return service.get('/permission/selectPermissionTree', {})
    },
    deleteById(id) {
        return service.delete(`/permission/deleteById/${id}`)
    },
    add(permission) {
        //permission以JSON形式传递到后台，后端使用@RequestBody接收
        return service.post('/permission/add', permission)
    },
    selectById(id) {
        return service.get(`/permission/selectById/${id}`)
    },
    update(permission) {
        return service.put('/permission/update', permission)
    },
    selectAssignedPermission(roleId) {
        return service.get(`/permission/selectAssignedPermission/${roleId}`)
    },
    assignPermission(roleId, permissionIds) {
        return service.post(`/permission/assignPermission`, null, {params: {roleId, permissionIds}})
    }
}

export default permissionApi