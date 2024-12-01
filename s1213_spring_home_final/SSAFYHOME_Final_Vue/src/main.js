import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';
import router from './router';
import { useKakao } from 'vue3-kakao-maps/@utils';
import './assets/main.css';

useKakao('appkey'); // Kakao API 초기화

const app = createApp(App);
app.use(createPinia());
app.use(router);
app.mount('#app');