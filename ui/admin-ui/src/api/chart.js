import request from '@/utils/request.js'

const chartApi = {
    selectCoachCount() {
        return request.get('/chart/selectCoachCount')
    }
}

export default chartApi