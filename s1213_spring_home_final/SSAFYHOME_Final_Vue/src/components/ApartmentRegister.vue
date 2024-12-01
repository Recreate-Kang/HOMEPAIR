<template>
  <form @submit.prevent="registerApartment" class="register-form">
    <h2>아파트 등록</h2>
    <div class="form-group">
      <label for="spaceName">공간 이름</label>
      <input
        id="spaceName"
        v-model="spaceName"
        type="text"
        placeholder="공간 이름을 입력하세요"
        required
      />
    </div>

    <div class="button-group">
      <button type="submit" class="submit-btn">등록</button>
      <button type="button" class="cancel-btn" @click="emit('close')">취소</button>
    </div>
  </form>
</template>

<script setup>
import { ref } from "vue";
import { useAuthStore } from "@/stores/authStore";
import { useUserFavoriteStore } from "@/stores/userFavoriteStore";
import { useApiStore } from "@/stores/apiStore";

// Props
const props = defineProps({
  apartment: Object, // 부모에서 전달된 아파트 정보
});

// State
const spaceName = ref("");
const authStore = useAuthStore();
const userFavoriteStore = useUserFavoriteStore();
const apiStore = useApiStore();

const emit = defineEmits(['close', 'register-success']);

// API 호출
const registerApartment = async () => {
  try {
    // Get userNum from localStorage directly
    const userNum = localStorage.getItem("userNum");
    if (!userNum) {
      throw new Error("사용자 정보를 찾을 수 없습니다.");
    }

    const favoriteData = {
      userId: userNum,
      aptSeq: props.apartment.aptSeq,
      myPlaceName: spaceName.value
    };

    await userFavoriteStore.addUserFavorite(favoriteData);
    emit('register-success');
    emit('close');
  } catch (error) {
    console.error("아파트 등록 실패:", error);
    alert(error.message || "아파트 등록에 실패했습니다. 다시 시도해주세요.");
  }
};
</script>

<style scoped>
.register-form {
  width: 100%;
  padding: 2rem;
}

h2 {
  color: #00796b;
  margin-bottom: 2rem;
  font-size: 1.5rem;
  text-align: center;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  color: #00796b;
  font-weight: 500;
  margin-bottom: 0.5rem;
}

input {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

input:focus {
  outline: none;
  border-color: #009688;
  box-shadow: 0 0 0 3px rgba(0, 150, 136, 0.1);
}

.button-group {
  display: flex;
  gap: 1rem;
  justify-content: flex-end;
  margin-top: 2rem;
}

.submit-btn, .cancel-btn {
  padding: 0.75rem 2rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.submit-btn {
  background: #009688;
  border: none;
  color: white;
}

.submit-btn:hover {
  background: #00897b;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 150, 136, 0.2);
}

.cancel-btn {
  background: transparent;
  border: 1px solid #009688;
  color: #009688;
}

.cancel-btn:hover {
  background: #e0f2f1;
}
</style>