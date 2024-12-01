<template>
  <div class="modal-backdrop" @click.self="$emit('close')">
    <div class="modal-content">
      <h2>프로필 수정</h2>
      <form @submit.prevent="handleSubmit">
        <div class="form-group">
          <label>이름</label>
          <input type="text" v-model="form.name" required />
        </div>
        <div class="form-group">
          <label>이메일</label>
          <input type="email" v-model="form.email" required disabled />
        </div>
        <div class="form-group">
          <label>비밀번호</label>
          <input type="password" v-model="form.password" placeholder="변경하려면 입력하세요" />
        </div>
        <div class="form-group">
          <label>비밀번호 확인</label>
          <input type="password" v-model="form.passwordConfirm" placeholder="비밀번호 확인" />
        </div>
        <div class="button-group">
          <button type="submit" class="save-btn">저장</button>
          <button type="button" class="cancel-btn" @click="$emit('close')">취소</button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const props = defineProps({
  user: {
    type: Object,
    required: true
  }
})

const emit = defineEmits(['close', 'save'])

const form = ref({
  name: props.user.name,
  email: props.user.email,
  password: '',
  passwordConfirm: ''
})

const handleSubmit = () => {
  if (form.value.password && form.value.password !== form.value.passwordConfirm) {
    alert('비밀번호가 일치하지 않습니다.')
    return
  }
  
  emit('save', {
    name: form.value.name,
    email: form.value.email,
    password: form.value.password || undefined
  })
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 2rem;
  border-radius: 15px;
  width: 100%;
  max-width: 400px;
  position: relative;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

h2 {
  color: #34495e;
  margin-bottom: 1.5rem;
  font-size: 1.5rem;
  font-weight: 600;
}

.form-group {
  margin-bottom: 1.5rem;
}

label {
  display: block;
  margin-bottom: 0.5rem;
  color: #34495e;
  font-weight: 500;
}

input {
  width: 100%;
  padding: 0.8rem 1rem;
  border: 2px solid #e9ecef;
  border-radius: 8px;
  font-size: 1rem;
  transition: all 0.3s ease;
}

input:focus {
  border-color: #3498db;
  outline: none;
  box-shadow: 0 0 0 3px rgba(52, 152, 219, 0.1);
}

input:disabled {
  background: #f5f7fa;
  cursor: not-allowed;
}

.button-group {
  display: flex;
  gap: 1rem;
  margin-top: 2rem;
}

button {
  flex: 1;
  padding: 0.8rem;
  border-radius: 8px;
  font-weight: 600;
  cursor: pointer;
  transition: all 0.3s ease;
}

.save-btn {
  background: linear-gradient(to right, #3498db, #2ecc71);
  color: white;
  border: none;
}

.save-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(52, 152, 219, 0.2);
}

.cancel-btn {
  background: transparent;
  border: 2px solid #3498db;
  color: #3498db;
}

.cancel-btn:hover {
  background: #3498db;
  color: white;
}
</style>