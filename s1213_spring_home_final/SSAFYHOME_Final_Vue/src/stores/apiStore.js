import { defineStore } from "pinia";
import axios from "axios";

export const useApiStore = defineStore("api", {
  state: () => ({
    baseURL: "http://localhost:8080/HOME",
    endpoint: "/apt",
    dongCodes: [],
    isLoading: false,
    error: null,
  }),
  actions: {
    getAuthHeaders() {
      const jwtToken = localStorage.getItem("jwtToken"); // authStore에서 관리된 토큰 사용
      if (!jwtToken) {
        console.error("JWT token is missing");
        return {};
      }
      return {
        Authorization: `Bearer ${jwtToken}`,
      };
    },

    async fetchDongCodesIfEmpty() {
      if (this.dongCodes.length > 0) {
        console.log("Dong codes are already loaded");
        return;
      }
      this.isLoading = true;
      this.error = null;
      try {
        console.log("LOADING DONG CODES...");
        const response = await axios.get(`${this.baseURL}/code`, {
          headers: this.getAuthHeaders(),
        });
        this.dongCodes = response.data;
      } catch (error) {
        this.error = error.message || "Failed to fetch dong codes";
        //console.error("Error fetching dong codes:", error);
      } finally {
        this.isLoading = false;
      }
    },
    async signup({ username, userId, password, email, phone_number }) {
      console.log("Signing up...");
      console.log("username:", username);
      console.log("userId:", userId);
      this.isLoading = true;
      this.error = null;
    
      try {
        const response = await axios.post(
          `${this.baseURL}/auth/signup`,
          {
            username: username,
            userId: userId,
            password: password,
            email: email,
            phone_number: phone_number,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
    
        console.log("Signup response:", response.data); // Log response for debugging
        return response.data; // Return successful response
      } catch (error) {
        this.error = error.response?.data?.message || "Signup failed";
        console.error("Signup error:", this.error);
        throw new Error(this.error); // Propagate the error
      } finally {
        this.isLoading = false;
      }
    },
    async verifyEmail({ email, code }) {
      this.isLoading = true;
      this.error = null;

      try {
        console.log("Mock verifyEmail called with:", { email, code });

        // Simulate success for any input during testing
        if (code === "123456" || code) {
          console.log(`Email verification succeeded for: ${email}`);
          return { message: "Verification successful" };
        } else {
          throw new Error("Invalid verification code");
        }
      } catch (error) {
        console.error("Email verification error:", error.message);
        throw error;
      } finally {
        this.isLoading = false;
      }
    },
    async searchDongCode(code) {
      if (!code) {
        throw new Error("Code is empty");
      }
      
      this.isLoading = true;
      this.error = null;

      const controller = new AbortController();
      const timeout = setTimeout(() => {
        controller.abort();
      }, 5000); // 5초 타임아웃

      try {
        const response = await axios.post(
          `${this.baseURL}${this.endpoint}/aptlist`,
          { code },
          { 
            headers: this.getAuthHeaders(),
            signal: controller.signal,
            timeout: 5000
          }
        );
        return response.data;
      } catch (error) {
        if (axios.isCancel(error)) {
          this.error = '요청 시간이 초과되었습니다.';
          console.error('Request was cancelled:', error.message);
        } else {
          this.error = error.message || "Failed to search dong code";
          console.error("Error searching dong code:", error);
        }
        throw error;
      } finally {
        clearTimeout(timeout);
        this.isLoading = false;
      }
    },
    resetState() {
      this.dongCodes = [];
      this.isLoading = false;
      this.error = null;
    },
  },
});
