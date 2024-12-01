<template>
  <div class="welcome-page">
    <div class="content">
      <h1>WELCOME</h1>
      <p>
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod
        tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim
        veniam, quis nostrud exercitation.
      </p>
      <button class="get-started-btn" @click="goToFullPage">GET STARTED</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router';

const router = useRouter();

function goToFullPage() {
  router.push('/fullpage'); // '/fullpage' 경로로 이동
}
</script>

<style scoped>
/* 화면 전체를 차지하도록 설정 */
.welcome-page {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  height: 100vh; /* 화면의 전체 높이 차지 */
  width: 100vw; /* 화면의 전체 너비 차지 */
  background: linear-gradient(to top, #a7c4d7, #456994); /* 배경 그라데이션 */
  color: white;
  font-family: Arial, sans-serif;
  margin: 0;
  overflow: hidden; /* 스크롤바 숨기기 */
}

.navbar {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  padding: 1rem;
  background-color: #333;
  color: white;
  text-align: center;
  z-index: 1000;
  box-sizing: border-box;
}

.navbar a {
  color: white;
  text-decoration: none;
  margin: 0 1rem;
}

.content {
  max-width: 800px; /* 콘텐츠의 최대 너비 */
  padding: 2rem;
  text-align: center;
}

h1 {
  font-size: 3em;
  margin: 0.5em 0;
}

p {
  font-size: 1.2em; /* 텍스트 크기 조정 */
  line-height: 1.6;
  margin-bottom: 1.5em;
}

.get-started-btn {
  background-color: white;
  color: #456994;
  padding: 10px 20px;
  font-size: 1em;
  font-weight: bold;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.get-started-btn:hover {
  background-color: #ececec;
}
</style>
