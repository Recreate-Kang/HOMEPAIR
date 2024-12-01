<template>
  <div class="board-write-container">
    <h2>{{ isEdit ? '게시글 수정' : '새 게시글 작성' }}</h2>
    <form @submit.prevent="handleSubmit" class="write-form">
      <div class="form-group">
        <label for="subject">제목</label>
        <input 
          type="text" 
          id="subject" 
          v-model="article.subject" 
          required 
          placeholder="제목을 입력하세요"
        >
      </div>
      <div class="form-group">
        <label for="content">내용</label>
        <textarea 
          id="content" 
          v-model="article.content" 
          required 
          placeholder="내용을 입력하세요"
          rows="10"
        ></textarea>
      </div>
      <div class="button-group">
        <button type="button" @click="$router.push('/board')" class="cancel-btn">취소</button>
        <button type="submit" class="submit-btn">{{ isEdit ? '수정하기' : '작성하기' }}</button>
      </div>
    </form>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useApiStore } from '@/stores/apiStore';
import { useAuthStore } from '@/stores/authStore';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const apiStore = useApiStore();
const authStore = useAuthStore();

const article = ref({
  userId: authStore.loginID || '',
  subject: '',
  content: '',
  articleNo: null
});

const isEdit = ref(false);

onMounted(async () => {
  if (!authStore.isLoggedIn) {
    alert('로그인이 필요합니다.');
    router.push('/login');
    return;
  }

  if (route.params.id) {
    isEdit.value = true;
    try {
      const response = await axios.get(`${apiStore.baseURL}/board/read/${route.params.id}`);
      // 현재 로그인한 사용자와 게시글 작성자가 다르면 리다이렉트
      if (response.data.userId !== authStore.loginID) {
        alert('수정 권한이 없습니다.');
        router.push('/board');
        return;
      }
      article.value = {
        userId: authStore.loginID,
        subject: response.data.subject,
        content: response.data.content,
        articleNo: parseInt(route.params.id)
      };
    } catch (error) {
      console.error('게시글 조회 실패:', error);
      router.push('/board');
    }
  }
});

const handleSubmit = async () => {
  try {
    if (!authStore.isLoggedIn) {
      alert('로그인이 필요합니다.');
      router.push('/login');
      return;
    }

    const endpoint = isEdit.value ? 'update' : 'write';
    const data = isEdit.value ? article.value : {
      userId: article.value.userId,
      subject: article.value.subject,
      content: article.value.content
    };
    
    await axios.post(`${apiStore.baseURL}/board/${endpoint}`, data);
    router.push('/board');
  } catch (error) {
    console.error('게시글 저장 실패:', error);
    alert('게시글 저장에 실패했습니다.');
  }
};
</script>

<style scoped>
.board-write-container {
  max-width: 95%;
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

h2 {
  color: #00796b;
  margin-bottom: 2rem;
  font-size: 1.5rem;
}

.write-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

label {
  color: #00796b;
  font-weight: 500;
}

input, textarea {
  padding: 0.75rem;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 1rem;
  transition: border-color 0.3s ease;
}

input:focus, textarea:focus {
  outline: none;
  border-color: #009688;
}

.button-group {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 1rem;
}

.cancel-btn, .submit-btn {
  padding: 0.75rem 2rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
}

.cancel-btn {
  background: transparent;
  border: 1px solid #009688;
  color: #009688;
}

.submit-btn {
  background: #009688;
  border: none;
  color: white;
}

.cancel-btn:hover {
  background: #e0f2f1;
}

.submit-btn:hover {
  background: #00897b;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 150, 136, 0.2);
}
</style>
