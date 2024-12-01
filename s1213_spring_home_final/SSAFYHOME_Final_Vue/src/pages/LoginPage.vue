<template>
  <div class="auth-container">
    <div class="auth-card">
      <div class="auth-header">
        <h1>환영합니다</h1>
        <p class="subtitle">로그인하여 시작하세요</p>
      </div>

      <form @submit.prevent="handleLogin" class="auth-form">
        <div class="form-group">
          <label for="userId">
            <i class="fas fa-user"></i>
            아이디
          </label>
          <input
            type="text"
            id="userId"
            v-model="form.userId"
            required
            placeholder="아이디를 입력하세요"
            autocomplete="username"
          >
        </div>

        <div class="form-group">
          <label for="password">
            <i class="fas fa-lock"></i>
            비밀번호
          </label>
          <div class="password-input">
            <input
              :type="showPassword ? 'text' : 'password'"
              id="password"
              v-model="form.password"
              required
              placeholder="비밀번호를 입력하세요"
              autocomplete="current-password"
            >
            <button 
              type="button" 
              class="toggle-password"
              @click="showPassword = !showPassword"
            >
              <i :class="showPassword ? 'fas fa-eye-slash' : 'fas fa-eye'"></i>
            </button>
          </div>
        </div>

        <div class="form-options">
          <label class="remember-me">
            <input
              type="checkbox"
              v-model="form.rememberMe"
            >
            <span>로그인 상태 유지</span>
          </label>
          <router-link to="/forgot-password" class="forgot-password">
            비밀번호 찾기
          </router-link>
        </div>

        <button type="submit" class="submit-btn" :disabled="isLoading">
          <span v-if="!isLoading">로그인</span>
          <span v-else class="loading-spinner">
            <i class="fas fa-circle-notch fa-spin"></i>
          </span>
        </button>

        <div class="social-login">
          <p>다른 방법으로 로그인</p>
          <div class="social-buttons">
            <button type="button" class="social-btn google" @click="socialLogin('google')">
              <i class="fab fa-google"></i>
            </button>
            <button type="button" class="social-btn kakao" @click="socialLogin('kakao')">
              <i class="fas fa-comment"></i>
            </button>
            <button type="button" class="social-btn naver" @click="socialLogin('naver')">
              <span>N</span>
            </button>
          </div>
        </div>
      </form>

      <div class="auth-footer">
        <p>
          아직 회원이 아니신가요? 
          <router-link to="/signup" class="signup-link">
            회원가입
          </router-link>
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue';
import { useRouter } from 'vue-router';
import { useAuthStore } from "@/stores/authStore";

const router = useRouter();
const authStore = useAuthStore();
const showPassword = ref(false);
const isLoading = ref(false);

const form = reactive({
  userId: '',
  password: '',
  rememberMe: false
});

const handleLogin = async () => {
  try {
    isLoading.value = true;
    await authStore.login(form.userId, form.password);
    alert("로그인 성공!");
    router.push("/mypage");
  } catch (error) {
    alert('로그인 실패. 아이디와 비밀번호를 확인해주세요.');
  } finally {
    isLoading.value = false;
  }
};

const socialLogin = (provider) => {
  // Implement social login logic here
  console.log(`Logging in with ${provider}`);
};
</script>

<style scoped>
.auth-container {
  min-height: calc(100vh - 60px);
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: #e0f2f1;
  padding: 2rem;
}

.auth-card {
  width: 100%;
  max-width: 450px;
  background: #fff;
  border-radius: 15px;
  padding: 2.5rem;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  position: relative;
  overflow: hidden;
}

.auth-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: #009688;
}

.auth-header {
  text-align: center;
  margin-bottom: 2rem;
}

.auth-header h1 {
  color: #00796b;
  font-size: 2rem;
  margin-bottom: 0.5rem;
  font-weight: 700;
}

.subtitle {
  color: #607d8b;
  font-size: 1rem;
}

.form-group {
  margin-bottom: 1.5rem;
}

.form-group label {
  display: block;
  margin-bottom: 0.5rem;
  color: #00796b;
  font-weight: 500;
}

.form-group input {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 2px solid #e0f2f1;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

.form-group input:focus {
  border-color: #009688;
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 150, 136, 0.1);
}

.password-input {
  position: relative;
}

.toggle-password {
  position: absolute;
  right: 1rem;
  top: 50%;
  transform: translateY(-50%);
  background: none;
  border: none;
  color: #636e72;
  cursor: pointer;
}

.submit-btn {
  width: 100%;
  padding: 1rem;
  background: #009688;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1.1rem;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn:hover {
  background: #00897b;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 150, 136, 0.2);
}

.submit-btn:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.remember-me {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #607d8b;
}

.forgot-password {
  color: #009688;
  text-decoration: none;
  font-weight: 500;
}

.forgot-password:hover {
  color: #00796b;
}

.social-login {
  margin-top: 2rem;
  text-align: center;
}

.social-login p {
  color: #607d8b;
  margin-bottom: 1rem;
  position: relative;
}

.social-login p::before,
.social-login p::after {
  content: '';
  position: absolute;
  top: 50%;
  width: 30%;
  height: 1px;
  background: #e9ecef;
}

.social-login p::before {
  left: 0;
}

.social-login p::after {
  right: 0;
}

.social-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

.social-btn {
  width: 45px;
  height: 45px;
  border-radius: 50%;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.2rem;
  cursor: pointer;
  transition: all 0.3s ease;
}

.social-btn:hover {
  transform: translateY(-2px);
}

.google {
  background: #fff;
  color: #00796b;
  border: 2px solid #00796b;
}

.kakao {
  background: #FEE500;
  color: #000000;
}

.naver {
  background: #03C75A;
  color: #fff;
  font-weight: bold;
}

.auth-footer {
  text-align: center;
  margin-top: 2rem;
  color: #607d8b;
}

.signup-link {
  color: #009688;
  text-decoration: none;
  font-weight: 600;
}

.signup-link:hover {
  color: #00796b;
}
</style>
