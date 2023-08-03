import request from '@/utils/request'

// 查询分校成立列表
export function listSchool(query) {
  return request({
    url: '/school/school/list',
    method: 'get',
    params: query
  })
}

// 查询分校成立详细
export function getSchool(id) {
  return request({
    url: '/school/school/' + id,
    method: 'get'
  })
}

// 新增分校成立
export function addSchool(data) {
  return request({
    url: '/school/school',
    method: 'post',
    data: data
  })
}

// 修改分校成立
export function updateSchool(data) {
  return request({
    url: '/school/school',
    method: 'put',
    data: data
  })
}

// 删除分校成立
export function delSchool(id) {
  return request({
    url: '/school/school/' + id,
    method: 'delete'
  })
}

// 修改分校成立
export function startFlow(id) {
  return request({
    url: '/school/school/startFlow/' + id,
    method: 'get'
  })
}
