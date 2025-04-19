import service from '@/utils/request.js'

const adminApi = {
    list(conditions) {
        return service.get('/admin/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete(`/admin/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete(`/admin/deleteAll/${ids}`)
    },
    add(admin) {
        //admin以JSON形式传递到后台，后端使用@RequestBody接收
        return service.post('/admin/add', admin)
    },
    selectById(id) {
        return service.get(`/admin/selectById/${id}`)
    },
    update(admin) {
        return service.put('/admin/update', admin)
    },
    login(admin) {
        return service.put('/admin/login', admin)
    },
    adminInfo() {
        return service.get(`/admin/adminInfo`)
    },
    resetPassword(adminPasswordDTO) {
        return service.put('/admin/resetPassword', adminPasswordDTO)
    }
}

export default adminApi