import axios from 'axios'

const STORAGE_KEY = 'googleUser'

function readUserFromStorage() {
  const raw = localStorage.getItem(STORAGE_KEY)
  if (!raw) {
    return null
  }

  try {
    return JSON.parse(raw)
  } catch (error) {
    console.warn('Invalid auth data found in storage, clearing it.', error)
    localStorage.removeItem(STORAGE_KEY)
    return null
  }
}

function applyAuthHeader(token) {
  if (token) {
    axios.defaults.headers.common.Authorization = `Bearer ${token}`
  }
}

function clearAuthHeader() {
  delete axios.defaults.headers.common.Authorization
}

export function initializeAuthFromStorage() {
  const user = readUserFromStorage()
  if (user?.credential) {
    applyAuthHeader(user.credential)
  }
  return user
}

export function isAuthenticated() {
  return Boolean(readUserFromStorage()?.credential)
}

export function getCurrentUser() {
  return readUserFromStorage()
}

export function setCurrentUser(user) {
  if (!user?.credential) {
    throw new Error('A valid Google credential is required to persist the user session.')
  }

  localStorage.setItem(STORAGE_KEY, JSON.stringify(user))
  applyAuthHeader(user.credential)
}

export function clearCurrentUser() {
  localStorage.removeItem(STORAGE_KEY)
  clearAuthHeader()
}
