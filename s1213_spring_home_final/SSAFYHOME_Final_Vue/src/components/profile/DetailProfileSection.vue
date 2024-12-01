<template>
  <div class="detail-profile-section">
    <h2>세부 프로필</h2>
    <div v-if="!userDetailStore.hasUserDetail" class="no-details">
      <i class="fas fa-user-circle"></i>
      <p>아직 세부 프로필이 작성되지 않았습니다</p>
      <button class="create-btn" @click="openModal"> 프로필 작성하기 </button>
    </div>
    <div v-else class="profile-container">
      <div class="profile-grid">
        <div class="profile-item">
          <label>나이:</label>
          <span>{{ userDetailStore.userDetails.age }}세</span>
        </div>
        <div class="profile-item">
          <label>성별:</label>
          <span>{{ 
            userDetailStore.userDetails.gender === 'male' ? '남성' : 
            userDetailStore.userDetails.gender === 'female' ? '여성' : '기타'
          }}</span>
        </div>
        <div class="profile-item">
          <label>직업:</label>
          <span>{{ userDetailStore.userDetails.occupation }}</span>
        </div>
        <div class="profile-item">
          <label>취미:</label>
          <span>{{ userDetailStore.userDetails.hobbies }}</span>
        </div>
        <div class="profile-item">
          <label>결혼여부:</label>
          <span>{{ 
            userDetailStore.userDetails.maritalStatus === 'SINGLE' ? '미혼' : 
            userDetailStore.userDetails.maritalStatus === 'MARRIED' ? '기혼' : '기타'
          }}</span>
        </div>
        <div class="profile-item">
          <label>반려동물:</label>
          <span>{{ userDetailStore.userDetails.hasPets ? '있음' : '없음' }}</span>
        </div>
        <div class="profile-item bio-container">
          <label>자기소개:</label>
          <p class="bio">{{ userDetailStore.userDetails.bio }}</p>
        </div>
      </div>
    </div>
    <!-- 프로필 모달 -->
    <ProfileModal
      v-if="isModalOpen"
      @close="closeModal"
      :isEdit="userDetailStore.hasUserDetail"
    />
  </div>
</template>

<script setup>
import { ref, computed, watch } from 'vue';
import { useUserDetailStore } from '@/stores/userDetailStore';
import { useAuthStore } from '@/stores/authStore';
import { storeToRefs } from 'pinia';

const userDetailStore = useUserDetailStore();
const authStore = useAuthStore();
const { getUserDetail } = storeToRefs(userDetailStore);

const showModal = ref(false);
const isEditing = computed(() => getUserDetail.value && Object.keys(getUserDetail.value).length > 0);

// 프로필 폼 데이터
const profileForm = ref({
  userId: authStore.userNum,
  age: null,
  gender: '',
  occupation: '',
  hobbies: '',
  current_area: null,
  marital_status: '',
  pets: '',
  bio: ''
});

// getUserDetail이 변경될 때마다 폼 데이터 업데이트
watch(getUserDetail, (newValue) => {
  if (newValue) {
    profileForm.value = {
      userId: authStore.userNum,
      age: newValue.age,
      gender: newValue.gender,
      occupation: newValue.occupation,
      hobbies: newValue.hobbies,
      current_area: newValue.current_area,
      marital_status: newValue.marital_status,
      pets: newValue.pets,
      bio: newValue.bio
    };
  }
}, { immediate: true });

// 프로필 저장
const handleSubmit = async () => {
  try {
    if (isEditing.value) {
      await userDetailStore.updateUserDetail(profileForm.value);
    } else {
      await userDetailStore.createUserDetail(profileForm.value);
    }
    showModal.value = false;
  } catch (error) {
    console.error('프로필 저장 실패:', error);
  }
};

// 모달 닫기
const closeModal = () => {
  showModal.value = false;
  // 편집 중이었다면 원래 데이터로 복원
  if (isEditing.value) {
    const detail = getUserDetail.value;
    profileForm.value = {
      userId: authStore.userNum,
      age: detail.age,
      gender: detail.gender,
      occupation: detail.occupation,
      hobbies: detail.hobbies,
      current_area: detail.current_area,
      marital_status: detail.marital_status,
      pets: detail.pets,
      bio: detail.bio
    };
  }
};

// 기존 포맷 함수들 유지
const formatGender = (gender) => {
  const genderMap = {
    'male': '남성',
    'female': '여성',
    'other': '기타'
  };
  return genderMap[gender] || '미입력';
};

const formatMaritalStatus = (status) => {
  const statusMap = {
    'single': '미혼',
    'married': '기혼',
    'divorced': '이혼',
    'widowed': '사별'
  };
  return statusMap[status] || '미입력';
};
</script>

<style scoped>
.detail-profile-section {
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.profile-container {
  padding: 20px;
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.profile-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.profile-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.bio-container {
  grid-column: 1 / -1;
}

.bio {
  flex: 1;
  white-space: pre-wrap;
  word-break: break-all;
}

label {
  font-weight: bold;
  min-width: 100px;
  color: #666;
}

p {
  margin: 0;
  color: #333;
}

.no-details {
  text-align: center;
  padding: 40px;
}

.no-details i {
  font-size: 48px;
  color: #6c757d;
  margin-bottom: 16px;
}

.create-btn {
  margin-top: 16px;
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.create-btn:hover {
  background-color: #0056b3;
}
</style>
