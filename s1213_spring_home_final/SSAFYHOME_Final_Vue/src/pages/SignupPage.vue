<template>
  <div class="auth-page">
    <h1>회원가입</h1>
    <form v-if="!isVerificationPhase" @submit.prevent="handleSignup">
      <div>
        <label for="name">이름</label>
        <input type="text" id="name" v-model="name" required placeholder="이름을 입력하세요" />
      </div>
      <div>
        <label for="userId">아이디</label>
        <input type="text" id="userId" v-model="userId" required placeholder="아이디를 입력하세요" />
      </div>
      <div>
        <label for="password">비밀번호</label>
        <input type="password" id="password" v-model="password" required placeholder="비밀번호를 입력하세요" />
      </div>
      <div>
        <label for="confirmPassword">비밀번호 확인</label>
        <input type="password" id="confirmPassword" v-model="confirmPassword" required placeholder="비밀번호를 다시 입력하세요" />
      </div>
      <div>
        <label for="email">이메일</label>
        <input type="email" id="email" v-model="email" required placeholder="이메일을 입력하세요" />
      </div>
      <div>
        <label for="phone_number">전화번호</label>
        <input type="phone_number" id="phone_number" v-model="phone_number" required placeholder="전화번호를 입력하세요" />
      </div>
      <button type="submit">가입하기</button>
    </form>

    <!-- 이메일 인증 단계 -->
    <div v-else>
      <h2>이메일 인증</h2>
      <p>입력하신 이메일로 인증번호가 발송되었습니다. 아래에 인증번호(123456)를 입력해주세요:</p>
      <div>
        <label for="verificationCode">인증번호</label>
        <input type="text" id="verificationCode" v-model="verificationCode" required placeholder="인증번호 6자리 입력" />
      </div>
      <button @click="handleVerification">인증하기</button>
    </div>

    <p v-if="!isVerificationPhase">
      이미 계정이 있으신가요? <router-link to="/login">로그인하기</router-link>
    </p>
  </div>
</template>

<script>
import { ref } from "vue";
import { useRouter } from "vue-router";
import { useApiStore } from "@/stores/apiStore";

export default {
  name: "Signup",
  setup() {
    const apiStore = useApiStore();
    const router = useRouter();

    // Form inputs
    const name = ref("");
    const userId = ref("");
    const password = ref("");
    const confirmPassword = ref("");
    const email = ref("");
    const phone_number = ref("");
    const verificationCode = ref("");

    // State
    const isVerificationPhase = ref(false);

    const handleSignup = async () => {
  if (password.value !== confirmPassword.value) {
    alert("Passwords do not match!");
    return;
  }

  try {
    const signupData = {
      username: name.value,
      userId: userId.value,
      password: password.value,
      email: email.value,
      phoneNumber: phone_number.value, // Changed to camelCase
    };
    console.log("Signup form data:", signupData);

    // Simulate signup API request
    await apiStore.signup(signupData);

    alert("Signup successful! A verification code has been sent to your email.");
    isVerificationPhase.value = true; // Move to verification phase

  } catch (error) {
    console.error("Signup error:", error);
    alert("Signup failed. Please try again.");
  }
};
    // Verification handler
    const handleVerification = async () => {
      try {
        // Simulate verification API request
        await apiStore.verifyEmail({
          email: email.value,
          code: verificationCode.value,
        });

        alert("Email verified successfully! Please log in.");
        router.push("/login"); // Redirect to login page
      } catch (error) {
        alert("Verification failed. Please check the code and try again.");
      }
    };

    return {
      name,
      userId,
      password,
      confirmPassword,
      email,
      phone_number,
      verificationCode,
      isVerificationPhase,
      handleSignup,
      handleVerification,
    };
  },
};
</script>

<style scoped>
.auth-page {
  max-width: 450px;
  margin: 2rem auto;
  padding: 2.5rem;
  background: #fff;
  border-radius: 15px;
  box-shadow: 0 10px 20px rgba(0, 0, 0, 0.08);
  position: relative;
}

.auth-page::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 5px;
  background: #009688;
}

body {
  background-color: #e0f2f1;
}

h1, h2 {
  text-align: center;
  color: #00796b;
  margin-bottom: 2rem;
  font-weight: 700;
}

form div {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #00796b;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 2px solid #e0f2f1;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

input:focus {
  border-color: #009688;
  outline: none;
  box-shadow: 0 0 0 3px rgba(0, 150, 136, 0.1);
}

button {
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

button:hover {
  background: #00897b;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 150, 136, 0.2);
}

p {
  text-align: center;
  margin-top: 2rem;
  color: #607d8b;
}

a {
  color: #009688;
  text-decoration: none;
  font-weight: 600;
}

a:hover {
  color: #00796b;
}

.verification {
  text-align: center;
}

.verification p {
  margin-bottom: 2rem;
  line-height: 1.6;
}
</style>
