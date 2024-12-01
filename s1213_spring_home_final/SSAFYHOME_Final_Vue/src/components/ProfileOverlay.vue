<template>
  <div class="profile-overlay" v-if="isOpen">
    <div class="profile-content">
      <div class="profile-img-area">
        <span class="profile-mask"></span>
        <img 
          src="https://ssl.pstatic.net/static/common/myarea/myInfo.gif" 
          onerror="this.src='data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAFAAAABQCAMAAAC5zwKfAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAADNQTFRF8PDw5ubm+vr6+/v76enp+Pj47e3t5+fn9/f37Ozs9PT08vLy7+/v6urq9fX15OTk/f39pqwodgAAAQNJREFUeNrs11EOgyAMgOEWUAF19f6n3cMeplJHoSZbsv4H+DIWaCNsNwcGGmiggQZ+D/Q5reuasr8H9ID0CsHfALpA74JTgwPSPnRK0Ac6FrwOXOjcogIdlTkNuDDgogGRAVEBDsQ19IMzC879ILAgGNgAZhbMP3RttonxJs1Lgea/sDYcyreHXjW+oPUHVid2MWC1K+A0sqsDW7CksGmlCNZoFt9pIfjYgY8bwMMexUENQuOtqYCxfHtT7AddYodDcp0gIPEh9IBxous+nBuaTis4Nw/OSLVwloN+JEmjGJR5FyIDRpIWZeAoBkcZGMRgkIEkz0D7GjXQQAMN/GvwKcAAEGGHJh0mmaAAAAAASUVORK5CYII='" 
          width="80" 
          height="80" 
          alt="프로필 이미지"
        >
      </div>
      <div class="profile-info-area">
        <p class="profile-account">
          <span class="profile-name">
            <a class="profile-nick" href="/mypage">{{ userName }}님</a>
          </span>
          <button class="profile-logout-btn" @click="handleLogout">
            <span class="btn-text">로그아웃</span>
          </button>
        </p>
        <a class="profile-email">{{ userEmail }}</a>
        <ul class="profile-menu-list">
          <li><a href="/mypage">마이페이지</a></li>
          <li><a href="/settings">설정</a></li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import { useAuthStore } from '@/stores/authStore';

export default defineComponent({
  name: 'ProfileOverlay',
  props: {
    isOpen: {
      type: Boolean,
      required: true
    }
  },
  setup(props, { emit }) {
    const authStore = useAuthStore();

    const handleLogout = async () => {
      await authStore.logout();
      emit('close');
    };

    return {
      userName: authStore.getUserID,
      userEmail: authStore.getUserEmail,
      handleLogout
    };
  }
});
</script>

<style scoped>
.profile-overlay {
  position: absolute;
  top: 100%;
  right: 0;
  margin-top: 10px;
  background: white;
  border: 1px solid #ddd;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.1);
  width: 320px;
  z-index: 1000;
}

.profile-content {
  padding: 20px;
}

.profile-img-area {
  text-align: center;
  margin-bottom: 15px;
}

.profile-img-area img {
  border-radius: 50%;
  border: 1px solid #eee;
}

.profile-info-area {
  color: #333;
}

.profile-account {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.profile-name {
  font-size: 1.1rem;
  font-weight: 500;
}

.profile-nick {
  color: #333;
  text-decoration: none;
}

.profile-logout-btn {
  background: transparent;
  border: 1px solid #ddd;
  padding: 5px 10px;
  border-radius: 4px;
  cursor: pointer;
  color: #666;
  font-size: 0.9rem;
}

.profile-email {
  display: block;
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 15px;
  text-decoration: none;
}

.profile-menu-list {
  list-style: none;
  padding: 0;
  margin: 0;
  border-top: 1px solid #eee;
  padding-top: 15px;
}

.profile-menu-list li a {
  display: block;
  padding: 8px 0;
  color: #333;
  text-decoration: none;
  font-size: 0.9rem;
}

.profile-menu-list li a:hover {
  color: #009688;
}
</style>
