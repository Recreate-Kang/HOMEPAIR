<template>
  <div class="profile-section">
    <div class="profile-header">
      <h1>마이페이지</h1>
      <button
        class="edit-profile-btn"
        @click="showEditProfile = true"
      >
        <i class="fas fa-edit"></i> 프로필 {{ isEditProfile ? "수정" : "생성" }}
      </button>
      <EditProfileModal
        v-if="showEditProfile"
        :userInfo="userInfo"
        :isEdit="isEditProfile"
        @close="showEditProfile = false"
        @save="handleProfileUpdate"
      />
    </div>
    <div class="profile-card">
      <div class="profile-info">
        <div class="avatar" v-if="userInfo.name">{{ userInfo.name[0] }}</div>
        <div class="info-details">
          <h2>{{ userInfo.name }}</h2>
          <p class="email">{{ userInfo.email }}</p>
          <span class="account-badge" :class="userInfo.accountType">
            {{ userInfo.accountType === 'user' ? '일반회원' : '관리자' }}
          </span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useUserDetailStore } from '@/stores/userDetailStore';
import EditProfileModal from '../EditProfileModal.vue';

const authStore = useAuthStore();
const userDetailStore = useUserDetailStore();

const showEditProfile = ref(false);
const isEditProfile = ref(false);
const userInfo = ref({
  name: authStore.getUserName,
  email: authStore.getUserEmail,
  phone: authStore.getUserPhone,
  accountType: authStore.getUserRole === 'ADMIN' ? 'admin' : 'user'
});

// 프로필 업데이트 핸들러
const handleProfileUpdate = async (updatedInfo) => {
  try {
    if (isEditProfile.value) {
      await userDetailStore.updateUserDetail(updatedInfo);
    } else {
      await userDetailStore.createUserDetail(updatedInfo);
    }
    showEditProfile.value = false;
    await fetchUserInfo();
  } catch (error) {
    console.error("프로필 업데이트 실패:", error);
  }
};

// 현재 사용자 정보 가져오기
const fetchUserInfo = async () => {
  try {
    const userId = authStore.getUserNum;
    const details = await userDetailStore.fetchUserDetail(userId);

    if (details) {
      userInfo.value = {
        ...userInfo.value,
        ...details
      };
      isEditProfile.value = true;
    } else {
      isEditProfile.value = false;
    }
  } catch (error) {
    console.error("사용자 정보를 가져오지 못했습니다.", error);
    isEditProfile.value = false;
  }
};

onMounted(() => {
  fetchUserInfo();
});
</script>

<style scoped>
.profile-section {
  background: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.profile-header {
  padding: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #eee;
}

.profile-header h1 {
  margin: 0;
  font-size: 24px;
  color: #333;
}

.edit-profile-btn {
  background: #009688;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  transition: background 0.2s;
}

.edit-profile-btn:hover {
  background: #00796b;
}

.profile-card {
  padding: 20px;
}

.profile-info {
  display: flex;
  gap: 20px;
}

.avatar {
  width: 80px;
  height: 80px;
  background: #009688;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 32px;
  font-weight: bold;
}

.info-details {
  flex: 1;
}

.info-details h2 {
  margin: 0 0 8px 0;
  font-size: 20px;
  color: #333;
}

.email {
  color: #666;
  margin: 0 0 12px 0;
}

.account-badge {
  display: inline-block;
  padding: 4px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.account-badge.user {
  background: #e3f2fd;
  color: #1976d2;
}

.account-badge.admin {
  background: #fce4ec;
  color: #c2185b;
}
</style>
