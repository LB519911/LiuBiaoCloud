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

// 查看审批流程图
export function hiFlow(id) {
  return request({
    url: '/school/school/hiFlow/' + id,
    method: 'get'
  })
}

// 查看审批流程图
export function hiFlowText(id) {
  return request({
    url: '/school/school/HiFlowText/' + id,
    method: 'get'
  })
}

// 查询分校成立审批列表
export function apListSchool(query) {
  return request({
    url: '/school/school/apList',
    method: 'get',
    params: query
  })
}

// 审批
export function apSchool(id, next) {
  return request({
    url: '/school/school/apSchool/' + id + '/' + next,
    method: 'get'
  })
}
