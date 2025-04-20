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
    },
    exportExcel() {
        return service({
            url: `/course/exportExcel`,
            method: 'get',
            //XMLHttpRequest 属性 responseType 是一个枚举字符串值，用于指定响应中包含的数据类型。
            //"blob": response 是一个包含二进制数据的 Blob 对象。
            responseType: 'blob'
        })
    }
}

export default courseApi