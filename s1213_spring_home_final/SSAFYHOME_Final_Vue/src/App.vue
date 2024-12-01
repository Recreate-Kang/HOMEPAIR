<script setup>
import Navbar from "./components/Navbar.vue";
import { ref } from "vue";

// 로그인 상태 관리
const isLoggedIn = ref(false);

function login() {
  isLoggedIn.value = true;
}

function logout() {
  isLoggedIn.value = false;
}
</script>

<template>
  <div id="app">
    <Navbar
      :isLoggedIn="isLoggedIn"
      @login="login"
      @logout="logout"
    />
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<style>
#app {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.main-content {
  margin-top: 60px;
  flex: 1;
  display: flex;
  flex-direction: column;
}

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Noto Sans KR', sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

/* 헤더 스타일 */
.header {
  flex-shrink: 0; /* 헤더 크기 고정 */
  width: 100%;
  background-color: #f8f9fa;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 1rem; /* 헤더 내부 간격 */
  gap: 1rem; /* 헤더 내 요소 간의 간격 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

/* 네비게이션 스타일 */
nav {
  width: 100%;
  font-size: 12px;
  text-align: center;
  display: flex; /* Flexbox로 링크 정렬 */
  justify-content: center; /* 가운데 정렬 */
  gap: 1rem; /* 링크 간의 간격 */
}

nav a.router-link-exact-active {
  color: var(--color-text);
}

nav a.router-link-exact-active:hover {
  background-color: transparent;
}

nav a {
  display: inline-block;
  padding: 0 1rem;
  border-left: 1px solid var(--color-border);
}

nav a:first-of-type {
  border: 0;
}

/* 데스크톱 환경 스타일 */
@media (min-width: 768px) {
  .header {
    /*padding: 1.5rem; /* 데스크톱에서 더 넉넉한 패딩 */
    /*gap: 1.5rem; /* 더 넓은 간격 */
  }

  nav {
    text-align: left;
    font-size: 1rem;
    padding: 1rem 0;
    margin-top: 0;
  }
}
</style>
