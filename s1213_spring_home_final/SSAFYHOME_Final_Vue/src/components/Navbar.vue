<template>
  <nav class="navbar">
    <div class="nav-container">
      <div class="nav-brand">
        <router-link to="/" class="brand-link">
          <i class="fas fa-home"></i>
          <span>HOMEPAIR</span>
        </router-link>
      </div>
      
      <div class="nav-links">
        <router-link to="/" class="nav-link">홈</router-link>
        <router-link to="/board" class="nav-link">게시판</router-link>
        <router-link to="/mypage" class="nav-link">마이페이지</router-link>
      </div>

      <div class="auth-status">
        <div v-if="isLoggedIn" class="user-menu">
          <img 
            class="profile-img"
            src="https://ssl.pstatic.net/static/common/myarea/myInfo.gif" 
            :onerror="handleImageError"
            width="32" 
            height="32" 
            alt="프로필 이미지"
            @click="toggleProfileOverlay"
          >
          <ProfileOverlay 
            :is-open="isProfileOpen" 
            @close="closeProfileOverlay"
          />
        </div>
        <router-link v-else to="/login" class="user-button">
          <i class="fas fa-sign-in-alt"></i>
          <span>Login</span>
        </router-link>
      </div>
    </div>
  </nav>
</template>

<script>
import { computed, ref } from "vue";
import { useAuthStore } from "@/stores/authStore";
import ProfileOverlay from "./ProfileOverlay.vue";

export default {
  name: "Navbar",
  components: {
    ProfileOverlay
  },
  setup() {
    const authStore = useAuthStore();
    console.log('Auth Store State:', authStore); // 디버깅용
    
    const isLoggedIn = computed(() => {
      console.log('Login Status:', authStore.isLoggedIn);
      console.log('Login ID:', authStore.loginId);
      return authStore.isLoggedIn;
    });

    const userId = computed(() => {
      return authStore.loginId || '';
    });

    const isProfileOpen = ref(false);

    const handleImageError = "this.src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAMAAAC5zwKfAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAADNQTFRF8PDw5ubm+vr6+/v76enp+Pj47e3t5+fn9/f37Ozs9PT08vLy7+/v6urq9fX15OTk/f39pqwodgAAAQNJREFUeNrs11EOgyAMgOEWUAF19f6n3cMeplJHoSZbsv4H+DIWaCNsNwcGGmiggQZ+D/Q5reuasr8H9ID0CsHfALpA74JTgwPSPnRK0Ac6FrwOXOjcogIdlTkNuDDgogGRAVEBDsQ19IMzC879ILAgGNgAZhbMP3RttonxJs1Lgea/sDYcyreHXjW+oPUHVid2MWC1K+A0sqsDW7CksGmlCNZoFt9pIfjYgY8bwMMexUENQuOtqYCxfHtT7AddYodDcp0gIPEh9IBxous+nBuaTis4Nw/OSLVwloN+JEmjGJR5FyIDRpIWZeAoBkcZGMRgkIEkz0D7GjXQQAMN/GvwKcAAEGGHJh0mmaAAAAAASUVORK5CYII='";

    const toggleProfileOverlay = () => {
      isProfileOpen.value = !isProfileOpen.value;
    };

    const closeProfileOverlay = () => {
      isProfileOpen.value = false;
    };

    const logout = async () => {
      await authStore.logout();
    };

    return {
      isLoggedIn,
      userId,
      authStore,
      isProfileOpen,
      toggleProfileOverlay,
      closeProfileOverlay,
      logout,
      handleImageError
    };
  },
};
</script>

<style scoped>
.navbar {
  background: #009688;
  height: 60px;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1000;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  align-items: center;
}

.nav-container {
  height: 100%;
  display: grid;
  grid-template-columns: 200px 1fr 200px;
  align-items: center;
  width: 100%;
}

.nav-brand {
  display: flex;
  align-items: center;
  height: 100%;
}

.brand-link {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  color: white;
  text-decoration: none;
  font-size: 1.25rem;
  font-weight: 600;
}

.brand-link i {
  font-size: 1.5rem;
}

.nav-links {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
}

.nav-link {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: white;
  text-decoration: none;
  padding: 0 1rem;
  height: 100%;
  font-weight: 500;
  transition: all 0.2s ease;
}

.nav-link:hover {
  background: rgba(255, 255, 255, 0.1);
}

.auth-status {
  display: flex;
  justify-content: flex-end;
  align-items: center;
  height: 100%;
}

.user-menu {
  position: relative;
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 1rem;
}

.profile-img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  cursor: pointer;
  transition: opacity 0.2s;
}

.profile-img:hover {
  opacity: 0.9;
}

.user-button {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  background: transparent;
  border: 1px solid rgba(255, 255, 255, 0.6);
  color: white;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  font-size: 0.95rem;
}

.user-button:hover {
  background: rgba(255, 255, 255, 0.1);
  border-color: white;
}

.user-button i {
  font-size: 1rem;
}

@media (max-width: 768px) {
  .navbar {
    padding: 0.5rem 1rem;
  }

  .nav-links {
    display: none;
  }
  
  .nav-brand {
    font-size: 1.1rem;
  }
  
  .nav-brand i {
    font-size: 1.3rem;
  }

  .user-button {
    padding: 0.25rem 0.5rem;
  }
}
</style>
