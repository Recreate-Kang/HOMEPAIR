import { fileURLToPath, URL } from 'node:url';

import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vueDevTools from 'vite-plugin-vue-devtools';

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
      // sockjs-client를 브라우저 전용 빌드로 해결
      'sockjs-client': 'sockjs-client/dist/sockjs.min.js',
    },
  },
  define: {
    global: {}, // 브라우저에서 global 객체 정의
  },
  optimizeDeps: {
    include: ['vue-jwt-decode'],
  },
});
