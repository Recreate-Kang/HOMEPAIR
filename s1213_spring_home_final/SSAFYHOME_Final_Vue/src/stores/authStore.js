import { defineStore } from "pinia";
import axios from "axios";
import { jwtDecode } from 'jwt-decode';

import { ref } from "vue";
import { useUserDetailStore } from "@/stores/userDetailStore";
import { useUserFavoriteStore } from "@/stores/userFavoriteStore";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    jwtToken: localStorage.getItem("jwtToken") || null,
    baseURL: "http://localhost:8080/HOME", // API 기본 URL
    isLoading: false,
    error: null,
    userNum: "",
    userRole: "",
    userID: "",
    userName: "",
    userPhone: "",
    userEmail: "",
  }),
  getters: {
    isLoggedIn: (state) => !!state.jwtToken, // JWT 토큰 여부로 로그인 상태 확인
    getUserName: () => localStorage.getItem("userName"),
    getUserPhone: () => localStorage.getItem("userPhone"),
    getUserEmail: () => localStorage.getItem("userEmail"),
    getUserID: () => localStorage.getItem("userID"),
    getUserRole: () => localStorage.getItem("userRole"),
    getUserNum: () => localStorage.getItem("userNum"),
  },
  actions: {
    async login(userId, password) {
      this.isLoading = true;
      this.error = null;
      try {
        console.log("Sending login request:", { userId, password });
            
        const response = await axios.post(`${this.baseURL}/auth/login`, {
          userId,
          password,
        });
        console.log("Received login response:", response.data);
        const token = response.data?.jwtToken;
        if (!token) {
          throw new Error("Login failed: No token received");
        }
        else {
          console.log("Storing JWT token and user info");
          console.log("USERPhone:", response.data.userPhone, response.data.userEmail, response.data.userName, userId);
          this.jwtToken = token;
          this.userID = userId;
          const decoded = jwtDecode(token);
          console.log("User info:", decoded);
          this.userNum = decoded.sub;
          this.userRole = decoded.role;
          this.userName = response.data.userName;
          this.userEmail = response.data.userEmail;
          this.userPhone = response.data.userPhone;      
          console.log("User info filled:" + this.userNum + this.userRole);
        }

        localStorage.setItem("jwtToken", token);
        localStorage.setItem("userNum", this.userNum);
        localStorage.setItem("userRole", this.userRole);
        localStorage.setItem("userID", this.userID);
        localStorage.setItem("userName", this.userName);
        localStorage.setItem("userEmail", this.userEmail);
        localStorage.setItem("userPhone", this.userPhone);
        console.log("Login successful");

        // 사용자 상세 정보와 즐겨찾기 정보 가져오기
        const userDetailStore = useUserDetailStore();
        const userFavoriteStore = useUserFavoriteStore();
        
        try {
          // 로그인한 사용자의 userNum으로 정보 가져오기
          await Promise.all([
            userDetailStore.fetchUserDetail(this.userNum),
            userFavoriteStore.initializeUserFavorite(this.userNum),
            userFavoriteStore.fetchUserFavorite(this.userNum)
          ]);
          console.log("User details and favorites initialized successfully");
        } catch (error) {
          console.error("Failed to initialize user details or favorites:", error);
        }
            
      } catch (error) {
        this.error = error.message || "Failed to login";
        throw error; // 에러는 호출한 곳에서 처리
      } finally {
        this.isLoading = false;
      }
    },
    
    logout() {
      this.jwtToken = null;
      this.userID = null;
      localStorage.removeItem("jwtToken");
      localStorage.removeItem("userNum");
      localStorage.removeItem("userRole");
      localStorage.removeItem("userID");
      localStorage.removeItem("userName");
      localStorage.removeItem("userEmail");
      localStorage.removeItem("userPhone");
    },
  },
});
