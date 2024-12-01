import { createRouter, createWebHistory } from "vue-router";
import { useAuthStore } from "@/stores/authStore"; // Ensure this store exists
import WelcomePage from "@/pages/WelcomePage.vue";
import LoginPage from "@/pages/LoginPage.vue";
import MyPage from "@/pages/MyPage.vue";
import FullPage from "@/pages/FullPage.vue";
import SignupPage from "@/pages/SignupPage.vue";
import ChatPage from "@/pages/ChatPage.vue";
import AptInfo from "@/components/AptInfo.vue";
import WebSocket from "@/pages/WebSocket.vue"; // Ensure the correct filename
import BoardPage from "@/pages/BoardPage.vue";
import BoardList from "@/components/board/BoardList.vue";
import BoardDetail from "@/components/board/BoardDetail.vue";
import BoardWrite from "@/components/board/BoardWrite.vue";

const routes = [
  { path: "/", name: "Welcome", component: WelcomePage },
  { path: "/login", name: "Login", component: LoginPage },
  { path: "/signup", name: "Signup", component: SignupPage },
  {
    path: "/mypage",
    name: "MyPage",
    component: MyPage,
    meta: { requiresAuth: true },
  },
  { path: "/fullpage", name: "FullPage", component: FullPage },
  { path: "/chatpage", name: "ChatPage", component: ChatPage },
  { path: "/websocket", name: "WebSocket", component: WebSocket },
  {
    path: "/board",
    component: BoardPage,
    children: [
      { path: "", name: "BoardList", component: BoardList },
      { path: ":id", name: "BoardDetail", component: BoardDetail },
      {
        path: "write",
        component: BoardWrite,
      },
      {
        path: "edit/:id",
        component: BoardWrite,
      },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Global navigation guard
router.beforeEach((to, from, next) => {
  console.log("Navigating to:", to.name);
  const authStore = useAuthStore();
  if (to.meta.requiresAuth && !authStore.isLoggedIn) {
    alert("로그인이 필요합니다.");
    next("/login");
  } else {
    console.log("Access granted to:", to.name);
    next();
  }
});

export default router;
