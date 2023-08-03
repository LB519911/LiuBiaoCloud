import request from '@/utils/request'

// 查询分校管理列表
export function listSchool(query) {
  return request({
    url: '/school/school/list',
    method: 'get',
    params: query
  })
}

// 查询分校管理详细
export function getSchool(id) {
  return request({
    url: '/school/school/' + id,
    method: 'get'
  })
}

// 新增分校管理
export function addSchool(data) {
  return request({
    url: '/school/school',
    method: 'post',
    data: data
  })
}

// 修改分校管理
export function updateSchool(data) {
  return request({
    url: '/school/school',
    method: 'put',
    data: data
  })
}

// 删除分校管理
export function delSchool(id) {
  return request({
    url: '/school/school/' + id,
    method: 'delete'
  })
}
