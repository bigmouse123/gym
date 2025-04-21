import service from '@/utils/request.js'

const roleApi = {
    list(conditions) {
        return service.get('/role/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete(`/role/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete(`/role/deleteAll/${ids}`)
    },
    add(role) {
        //role以JSON形式传递到后台，后端使用@RequestBody接收
        return service.post('/role/add', role)
    },
    selectById(id) {
        return service.get(`/role/selectById/${id}`)
    },
    update(role) {
        return service.put('/role/update', role)
    }
}

export default roleApi