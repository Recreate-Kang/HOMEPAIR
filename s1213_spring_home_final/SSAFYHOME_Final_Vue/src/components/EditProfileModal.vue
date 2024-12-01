<script setup>
import { ref } from 'vue';
import { useUserDetailStore } from "@/stores/userDetailStore";
import { useAuthStore } from "@/stores/authStore";

const authStore = useAuthStore(); // 로그인한 사용자
const userId = authStore.getUserNum;

const props = defineProps({
  userInfo: Object, // 부모에서 전달받은 사용자 정보
  isEdit: Boolean,  // 새 프로필 생성인지 수정인지 구분
});

const emit = defineEmits(["close", "save"]); // 부모로 이벤트 전달
const userDetailStore = useUserDetailStore();

const form = ref({ ...props.userInfo });

// 모달 닫기
const closeModal = () => {
  emit("close");
};

// 새 프로필 저장
const submitProfile = async () => {
  try {
    console.log("USERID(저장): ", userId);
    form.value.userId = userId; // userId를 form.value에 추가
    console.log("USERVALUE: ", form.value);
    await userDetailStore.createUserDetail(form.value);
    alert("프로필이 성공적으로 저장되었습니다!");
    emit("save", form.value);
    closeModal();
  } catch (error) {
    console.error("프로필 저장 실패:", error);
    alert("프로필 저장에 실패했습니다.");
  }
};

// 기존 프로필 수정
const modifyProfile = async () => {
  try {
    console.log("USERID(수정): ", userId);
    form.value.userId = userId; // userId를 form.value에 추가
    console.log("USERVALUE: ", form.value);
    await userDetailStore.updateUserDetail(form.value);
    alert("프로필이 성공적으로 수정되었습니다!");
    emit("save", form.value);
    closeModal();
  } catch (error) {
    console.error("프로필 수정 실패:", error);
    alert("프로필 수정에 실패했습니다.");
  }
};
</script>

<template>
  <div class="modal-backdrop" @click.self="closeModal">
    <div class="modal-content">
      <h2>프로필 {{ isEdit ? "수정" : "생성" }}</h2>
      <form @submit.prevent="isEdit ? modifyProfile() : submitProfile()">
          <!-- 나이 입력 -->
          <div class="form-group">
            <label for="age">나이</label>
            <input id="age" type="number" v-model="form.age" required />
          </div>
  
          <!-- 성별 선택 -->
          <div class="form-group">
            <label>성별</label>
            <div class="radio-group">
              <label><input type="radio" value="male" v-model="form.gender" /> 남성</label>
              <label><input type="radio" value="female" v-model="form.gender" /> 여성</label>
              <label><input type="radio" value="other" v-model="form.gender" /> 기타</label>
            </div>
          </div>
  
          <!-- 직업 입력 -->
          <div class="form-group">
            <label for="occupation">직업</label>
            <input id="occupation" type="text" v-model="form.occupation" placeholder="직업을 입력하세요" />
          </div>
  
          <!-- 취미 입력 -->
          <div class="form-group">
            <label for="hobbies">취미</label>
            <input id="hobbies" type="text" v-model="form.hobbies" placeholder="취미를 입력하세요" />
          </div>
  
          <!-- 평수 입력 -->
          <div class="form-group">
            <label for="current_area">현재 거주 평수</label>
            <input id="current_area" type="number" v-model="form.current_area" required />
          </div>
  
          <!-- 결혼 상태 선택 -->
          <div class="form-group">
            <label>결혼 상태</label>
            <div class="radio-group">
              <label><input type="radio" value="single" v-model="form.marital_status" /> 미혼</label>
              <label><input type="radio" value="married" v-model="form.marital_status" /> 기혼</label>
              <label><input type="radio" value="divorced" v-model="form.marital_status" /> 이혼</label>
              <label><input type="radio" value="widowed" v-model="form.marital_status" /> 사별</label>
            </div>
          </div>
  
          <!-- 반려동물 여부 -->
          <div class="form-group">
            <label for="pets">반려동물을 키우시나요?</label>
            <textarea id="pets" v-model="form.has_pets" placeholder = "없으면 생략하셔도 됩니다."></textarea>
            <!--label><input type="checkbox" v-model="form.has_pets" /> 반려동물이 있습니다</!--label>-->
          </div>
  
          <!-- 자기소개 -->
          <div class="form-group">
            <label for="bio">자기소개</label>
            <textarea id="bio" v-model="form.bio" placeholder="작성하신 내용은 GPT와의 대화에 활용됩니다."></textarea>
          </div>
  

        <!-- 나머지 입력 필드 -->
        <div class="form-actions">
          <button type="submit" class="save-btn">저장</button>
          <button type="button" class="cancel-btn" @click="closeModal">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

  <style scoped>

  .modal-backdrop {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background-color: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
  }
  
  .modal-content {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    width: 400px;
    max-width: 90%;
  }
  
  h2 {
    margin-bottom: 20px;
    text-align: center;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  label {
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  input,
  textarea {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .radio-group label {
    display: inline-block;
    margin-right: 10px;
  }
  
  textarea {
    resize: none;
    height: 80px;
  }
  
  .form-actions {
    display: flex;
    justify-content: space-between;
    gap: 10px;
  }
  
  button {
    padding: 8px 12px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .save-btn {
    background-color: #009688;
    color: white;
    font-weight: bold;
  }
  .delete-btn {
  background-color: #dc3545;
  color: white;
  font-weight: bold;
}

.delete-btn:hover {
  background-color: #c82333;
}
  .cancel-btn {
    background-color: #e9ecef;
    color: #495057;
  }
  
  .save-btn:hover {
    background-color: #00796b;
  }
  
  .cancel-btn:hover {
    background-color: #dee2e6;
  }
  .radio-group {
  display: flex;
  flex-wrap: wrap;
  gap: 10px; /* 버튼 간의 간격 */
}

.radio-group label {
  display: flex;
  align-items: center;
  gap: 8px; /* 버튼과 텍스트 사이 간격 */
  font-size: 12px; /* 텍스트 크기 */
  font-weight: 500;
  cursor: pointer;
}

.radio-group input[type="radio"] {
  appearance: none; /* 기본 브라우저 스타일 제거 */
  width: 6px; /* 버튼 크기 줄이기 */
  height: 6px;
  border: 1.5px solid #ccc; /* 테두리 두께 조정 */
  border-radius: 50%; /* 원형 버튼 */
  outline: none;
  cursor: pointer;
  transition: all 0.3s ease;
}

.radio-group input[type="radio"]:checked {
  border-color: #009688;
  background-color: #009688;
    box-shadow: 0 0 0 3px rgba(0, 150, 136, 0.2); /* 선택된 버튼 강조 효과 */
}

.radio-group input[type="radio"]:hover {
  border-color: #00796b;
}

.radio-group span {
  color: #333;
  font-size: 12px; /* 텍스트 크기 줄이기 */
}
  </style>
  