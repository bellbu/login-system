import api from './api';

// 로그인
export const login = (email, password) => api.post(`/login?email=${email}&password=${password}`)

// 사용자 정보
export const info = () => api.get(`/admin/info`)

// 회원 가입
export const join = (data) => api.post(`/admin`, data)

// 회원 정보 수정
export const update = (data) => api.put(`/admin`, data)

// 회원 탈퇴
export const remove = (email) => api.put(`/admin/${email}`)

