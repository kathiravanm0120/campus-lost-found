export function getUser() {
  const raw = localStorage.getItem('user')
  return raw ? JSON.parse(raw) : null
}

export function saveAuth(data) {
  localStorage.setItem('token', data.token)
  localStorage.setItem('user', JSON.stringify({
    id: data.userId,
    name: data.name,
    email: data.email,
    role: data.role,
  }))
}

export function logout() {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
}

export function isLoggedIn() {
  return !!localStorage.getItem('token')
}
