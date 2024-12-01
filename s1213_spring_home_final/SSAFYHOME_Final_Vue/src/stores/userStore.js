import { defineStore } from 'pinia';
import { useAuthStore } from './authStore';
//import jwtDecode from 'jwt-decode';

export const useUserStore = defineStore('user', {
  state: () => ({
    //user: null,
    isLoggedIn: false,
    userRole: null,
    userName: null,
    userEmail: null,
    userPhone: null,
  }),

  actions: {
    setUserFromToken() {
      const authStore = useAuthStore();
      const token = authStore.jwtToken;

      if (token) {
        try {
          //const decodedToken = jwtDecode.decode(token);
          //this.user = {
          //  id: decodedToken.sub || null,
          //  role: decodedToken.role || "guest",
          //};
          //this.userRole = decodedToken.role || "guest";
          this.userName = authStore.userName || "Unknown";
          this.userEmail = authStore.userEmail || "Unknown";
          this.userPhone = authStore.userPhone || "Unknown";
          this.isLoggedIn = true;
        } catch (error) {
          console.error('Error decoding JWT:', error);
          this.clearUser();
        }
      } else {
        this.clearUser();
      }
    },

    setUser(userData) {
      //his.user = userData;
      this.isLoggedIn = true;
      //this.userRole = userData.role || null;
      this.userName = userData.name || null;
      this.userEmail = userData.email || null;
      this.userPhone = userData.phone || null;
    },

    clearUser() {
      //this.user = null;
      this.isLoggedIn = false;
      //this.userRole = null;
      this.userName = null;
      this.userEmail = null;
      this.userPhone = null;
    },

    checkLoginStatus() {
      const authStore = useAuthStore();
      if (authStore.jwtToken) {
        this.setUserFromToken();
        return true;
      } else {
        this.clearUser();
        return false;
      }
    },
  },

  getters: {
    getUserId: (state) => state.user?.id || null,
    //getCurrentUser: (state) => state.user || null,
    getLoginStatus: (state) => state.isLoggedIn,
    //getUserRole: (state) => state.userRole,
    getUserName: (state) => state.userName,
    getUserEmail: (state) => state.userEmail,
    getUserPhone: (state) => state.userPhone,
  },
});

/*
import { defineStore } from 'pinia';

export const useUserStore = defineStore('user', {
  state: () => ({
    user: null,
    isLoggedIn: false
  }),

  actions: {
    setUser(userData) {
      this.user = userData;
      this.isLoggedIn = true;
    },

    clearUser() {
      this.user = null;
      this.isLoggedIn = false;
    },

    // 로그인 상태 체크
    checkLoginStatus() {
      const token = localStorage.getItem('token');
      const userData = localStorage.getItem('user');
      if (token && userData) {
        try {
          this.user = JSON.parse(userData);
          this.isLoggedIn = true;
          return true;
        } catch (e) {
          this.clearUser();
          return false;
        }
      }
      return false;
    }
  },

  getters: {
    // 현재 로그인한 사용자의 ID
    getCurrentUserId: (state) => state.user?.id || null,
    getCurrentUser : (state) => state.user || null,
    // 로그인 여부
    getLoginStatus: (state) => state.isLoggedIn
  }
});
*/
