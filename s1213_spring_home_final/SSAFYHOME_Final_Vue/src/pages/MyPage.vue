<template>
  <main class="mypage-container">
    <ProfileSection />
    <DetailProfileSection @edit="openEditProfileModal" @create="openCreateProfileModal" />

    <ApartmentSection />

    <PlacesSection />
    
    <!-- 세부 프로필 모달 -->
    <div v-if="showDetailProfileModal" class="modal-overlay">
      <div class="modal-content">
        <h2>{{ isEditingProfile ? '프로필 수정' : '프로필 생성' }}</h2>
        <form @submit.prevent="handleDetailProfileSubmit">
          <!-- 나이 입력 -->
          <div class="form-group">
            <label for="age">나이</label>
            <input id="age" type="number" v-model="detailProfileForm.age" required>
          </div>

          <!-- 성별 선택 -->
          <div class="form-group">
            <label>성별</label>
            <div class="radio-group">
              <label><input type="radio" v-model="detailProfileForm.gender" value="male"> 남성</label>
              <label><input type="radio" v-model="detailProfileForm.gender" value="female"> 여성</label>
              <label><input type="radio" v-model="detailProfileForm.gender" value="other"> 기타</label>
            </div>
          </div>

          <!-- 직업 입력 -->
          <div class="form-group">
            <label for="occupation">직업</label>
            <input id="occupation" type="text" v-model="detailProfileForm.occupation" placeholder="직업을 입력하세요">
          </div>

          <!-- 취미 입력 -->
          <div class="form-group">
            <label for="hobbies">취미</label>
            <input id="hobbies" type="text" v-model="detailProfileForm.hobbies" placeholder="취미를 입력하세요">
          </div>

          <!-- 평수 입력 -->
          <div class="form-group">
            <label for="current_area">현재 거주 평수</label>
            <input id="current_area" type="number" v-model="detailProfileForm.current_area" required>
          </div>

          <!-- 결혼 상태 선택 -->
          <div class="form-group">
            <label>결혼 상태</label>
            <div class="radio-group">
              <label><input type="radio" v-model="detailProfileForm.marital_status" value="single"> 미혼</label>
              <label><input type="radio" v-model="detailProfileForm.marital_status" value="married"> 기혼</label>
              <label><input type="radio" v-model="detailProfileForm.marital_status" value="divorced"> 이혼</label>
              <label><input type="radio" v-model="detailProfileForm.marital_status" value="widowed"> 사별</label>
            </div>
          </div>

          <!-- 반려동물 여부 -->
          <div class="form-group">
            <label for="pets">반려동물을 키우시나요?</label>
            <textarea id="pets" v-model="detailProfileForm.pets" placeholder="없으면 생략하셔도 됩니다."></textarea>
          </div>

          <!-- 자기소개 -->
          <div class="form-group">
            <label for="bio">자기소개</label>
            <textarea id="bio" v-model="detailProfileForm.bio" placeholder="작성하신 내용은 GPT와의 대화에 활용됩니다."></textarea>
          </div>

          <div class="form-actions">
            <button type="submit" class="save-btn">저장</button>
            <button type="button" class="cancel-btn" @click="closeDetailProfileModal">취소</button>
          </div>
        </form>
      </div>
    </div>
  </main>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import ProfileSection from '@/components/profile/ProfileSection.vue';
import ApartmentSection from '@/components/apartment/ApartmentSection.vue';
import PlacesSection from '@/components/places/PlacesSection.vue';
import DetailProfileSection from '@/components/profile/DetailProfileSection.vue';
import { useAuthStore } from '@/stores/authStore';
import { useUserFavoriteStore } from '@/stores/userFavoriteStore';
import { useUserDetailStore } from '@/stores/userDetailStore';
import { useApiStore } from '@/stores/apiStore';


// 상태 변수
const showDetailProfileModal = ref(false);
const isEditingProfile = ref(false);
const processedFavorite = computed(() => userFavoriteStore.processedFavorites);
// 상세 프로필 폼 초기값
const detailProfileForm = ref({
  age: null,
  gender: '',
  occupation: '',
  hobbies: '',
  current_area: null,
  marital_status: '',
  pets: '',
  bio: ''
});

// Store
const authStore = useAuthStore();
const apiStore = useApiStore();
const userFavoriteStore = useUserFavoriteStore();
const userDetailStore = useUserDetailStore();

// 모달 열기/닫기 핸들러
const openEditProfileModal = () => {
  isEditingProfile.value = true;
  showDetailProfileModal.value = true;
};

const openCreateProfileModal = () => {
  isEditingProfile.value = false;
  showDetailProfileModal.value = true;
};

const closeDetailProfileModal = () => {
  showDetailProfileModal.value = false;
};

// 프로필 제출 핸들러
const handleDetailProfileSubmit = async () => {
  try {
    if (isEditingProfile.value) {
      await userDetailStore.updateUserDetail(detailProfileForm.value);
    } else {
      await userDetailStore.createUserDetail(detailProfileForm.value);
    }
    closeDetailProfileModal();
  } catch (error) {
    console.error('프로필 저장 중 오류 발생:', error);
  }
};

// 페이지 로드 시 초기화
onMounted(async () => {
  try {
    const userNum = authStore.getUserNum;
    if (userNum) {
      // 즐겨찾기 데이터를 초기화
      await userFavoriteStore.fetchUserFavorites(userNum);
      await userDetailStore.fetchUserDetail(userNum);
      await userFavoriteStore.processHomeFavorites(userNum);
      console.log(processedFavorite.value + "PROCESSED");
    } else {
      console.error('사용자 정보가 없습니다. 로그인이 필요합니다.');
    }
  } catch (error) {
    console.error('데이터 초기화 중 오류 발생:', error);
  }
});
</script>
<style scoped>
.mypage-container {
  width: 70%;
  margin: 0 auto;
  padding: 20px;
  min-width: 800px;
  max-width: 1600px;
}

@media (max-width: 1200px) {
  .mypage-container {
    width: 85%;
  }
}

@media (max-width: 900px) {
  .mypage-container {
    width: 95%;
    min-width: auto;
  }
}

.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 30px;
  border-radius: 12px;
  width: 90%;
  max-width: 600px;
  max-height: 90vh;
  overflow-y: auto;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 8px;
  color: #333;
  font-weight: 500;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group textarea {
  width: 100%;
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.form-group textarea {
  min-height: 100px;
  resize: vertical;
}

.radio-group {
  display: flex;
  gap: 20px;
}

.radio-group label {
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
}

.form-actions {
  display: flex;
  gap: 10px;
  justify-content: flex-end;
  margin-top: 30px;
}

.save-btn,
.cancel-btn {
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.save-btn {
  background: #009688;
  color: white;
  border: none;
}

.save-btn:hover {
  background: #00796b;
}

.cancel-btn {
  background: white;
  color: #666;
  border: 1px solid #ddd;
}

.cancel-btn:hover {
  background: #f5f5f5;
}
</style>