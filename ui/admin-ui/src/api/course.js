import service from '@/utils/request.js'

const courseApi = {
    list(conditions) {
        return service.get('/course/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete(`/course/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete(`/course/deleteAll/${ids}`)
    },
    add(course) {
        //course以JSON形式传递到后台，后端使用@RequestBody接收
        return service.post('/course/add', course)
    },
    selectById(id) {
        return service.get(`/course/selectById/${id}`)
    },
    update(course) {
        return service.put('/course/update', course)
    }
}

export default courseApi