import service from '@/utils/request.js'

const coachApi = {
    list(conditions) {
        return service.get('/coach/list', {params: conditions})
    },
    deleteById(id) {
        return service.delete(`/coach/deleteById/${id}`)
    },
    deleteAll(ids) {
        return service.delete(`/coach/deleteAll/${ids}`)
    },
    add(coach) {
        //coach以JSON形式传递到后台，后端使用@RequestBody接收
        return service.post('/coach/add', coach)
    },
    selectById(id) {
        return service.get(`/coach/selectById/${id}`)
    },
    update(coach) {
        return service.put('/coach/update', coach)
    },
    selectAll() {
        return service.get(`/coach/selectAll`)
    }
}

export default coachApi