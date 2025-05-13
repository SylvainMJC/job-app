import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth/';

class AuthService {
  async login(username, password) {
    const response = await axios.post(API_URL + 'login', {
      username,
      password
    });
    if (response.data.token) {
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  }

  logout() {
    localStorage.removeItem('user');
  }

  async register(username, email, password, nom, prenom) {
    const response = await axios.post(API_URL + 'register', {
      username,
      email,
      password,
      nom,
      prenom
    });
    if (response.data.token) {
      localStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  }

  getCurrentUser() {
    return JSON.parse(localStorage.getItem('user'));
  }

  isAuthenticated() {
    const user = this.getCurrentUser();
    return !!user;
  }

  hasRole(role) {
    const user = this.getCurrentUser();
    if (!user || !user.roles) {
      return false;
    }
    return user.roles.includes(role);
  }
  
  isRecruteur() {
    return this.hasRole('RECRUTEUR') || this.hasRole('ADMIN');
  }
  
  isCandidat() {
    return this.hasRole('CANDIDAT') || this.hasRole('ADMIN');
  }
  
  isAdmin() {
    return this.hasRole('ADMIN');
  }

  getAuthHeader() {
    const user = this.getCurrentUser();
    if (user && user.token) {
      return { Authorization: 'Bearer ' + user.token };
    } else {
      return {};
    }
  }
}

export default new AuthService(); 