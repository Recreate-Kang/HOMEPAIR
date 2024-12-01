<template>
  <div class="board-detail">
    <div v-if="!article" class="loading-container">
      <i class="fas fa-spinner fa-spin loading-spinner"></i>
      <p>게시글을 불러오는 중입니다...</p>
    </div>

    <div v-else class="detail-container">
      <div class="article-header">
        <div class="article-category">
          <span class="category-label">{{ article.category || '일반' }}</span>
        </div>
        <h1 class="article-title">{{ article.title }}</h1>
        <div class="article-meta">
          <div class="meta-left">
            <span class="author">
              <i class="fas fa-user"></i>
              {{ article.userId }}
            </span>
            <span class="date">
              <i class="far fa-calendar-alt"></i>
              {{ formatDate(article.registerTime) }}
            </span>
          </div>
          <div class="meta-right">
            <span class="hit">
              <i class="fas fa-eye"></i> 조회수 {{ article.hit }}
            </span>
          </div>
        </div>
      </div>

      <div class="article-body">
        <div class="content" v-html="formattedContent"></div>
      </div>

      <CommentList :articleId="route.params.id" />

      <div class="button-group">
        <div class="left-buttons">
          <button class="btn list-btn" @click="goToList">
            <i class="fas fa-list"></i>
            목록
          </button>
        </div>
        <div v-if="authStore.loginID === article.userId" class="right-buttons">
          <button class="btn edit-btn" @click="handleEdit">
            <i class="fas fa-edit"></i>
            수정
          </button>
          <button class="btn delete-btn" @click="handleDelete">
            <i class="fas fa-trash"></i>
            삭제
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useAuthStore } from '@/stores/authStore';
import { useApiStore } from '@/stores/apiStore';
import CommentList from './CommentList.vue';
import axios from 'axios';

const route = useRoute();
const router = useRouter();
const authStore = useAuthStore();
const apiStore = useApiStore();
const article = ref(null);

const formattedContent = computed(() => {
  if (!article.value?.content) return '';
  return article.value.content.replace(/\n/g, '<br>');
});

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

const fetchArticle = async () => {
  try {
    const response = await axios.get(`${apiStore.baseURL}/board/read/${route.params.id}`, {
      headers: apiStore.getAuthHeaders()
    });
    article.value = response.data;
  } catch (error) {
    console.error('게시글 로딩 실패:', error);
    alert('게시글을 불러오는데 실패했습니다.');
  }
};

const goToList = () => {
  router.push('/board');
};

const handleEdit = () => {
  router.push(`/board/modify/${route.params.id}`);
};

const handleDelete = async () => {
  if (!confirm('정말 삭제하시겠습니까?')) return;
  
  try {
    await axios.delete(`${apiStore.baseURL}/board/delete/${route.params.id}`, {
      headers: apiStore.getAuthHeaders()
    });
    alert('게시글이 삭제되었습니다.');
    router.push('/board');
  } catch (error) {
    console.error('게시글 삭제 실패:', error);
    alert('게시글 삭제에 실패했습니다.');
  }
};

onMounted(() => {
  fetchArticle();
});
</script>

<style scoped>
.board-detail {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.loading-container {
  text-align: center;
  padding: 50px;
}

.loading-spinner {
  font-size: 2rem;
  color: #4a90e2;
  margin-bottom: 10px;
}

.detail-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.article-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.article-category {
  margin-bottom: 10px;
}

.category-label {
  background-color: #f8f9fa;
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 0.9em;
  color: #666;
  border: 1px solid #e9ecef;
}

.article-title {
  font-size: 24px;
  margin: 15px 0;
  color: #333;
  font-weight: 600;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #666;
  font-size: 0.9em;
}

.meta-left span {
  margin-right: 15px;
  display: inline-flex;
  align-items: center;
}

.meta-left i, .meta-right i {
  margin-right: 6px;
  color: #888;
}

.article-body {
  margin: 25px 0;
  padding: 20px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.content {
  line-height: 1.6;
  color: #333;
  white-space: pre-wrap;
}

.button-group {
  display: flex;
  justify-content: space-between;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #eee;
}

.btn {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.9em;
  display: inline-flex;
  align-items: center;
  transition: all 0.2s;
}

.btn i {
  margin-right: 6px;
}

.list-btn {
  background-color: #f8f9fa;
  color: #495057;
  border: 1px solid #e9ecef;
}

.list-btn:hover {
  background-color: #e9ecef;
}

.edit-btn {
  background-color: #4CAF50;
  color: white;
}

.edit-btn:hover {
  background-color: #45a049;
}

.delete-btn {
  background-color: #dc3545;
  color: white;
}

.delete-btn:hover {
  background-color: #c82333;
}

.comments-section {
  margin-top: 30px;
  padding: 20px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
