<template>
  <div class="board-container">
    <div class="board-header">
      <h2>자유 게시판</h2>
      <div class="board-controls">
        <select v-model="pageSize" @change="handlePageSizeChange" class="page-size-select">
          <option v-for="size in pageSizeOptions" :key="size" :value="size">
            {{ size }}개씩 보기
          </option>
        </select>
        <button @click="$router.push('/board/write')" class="write-btn" v-if="authStore.isLoggedIn">
          <i class="fas fa-pen"></i> 글쓰기
        </button>
      </div>
    </div>

    <div class="board-list">
      <table>
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="article in articles" :key="article.articleNo" @click="goToDetail(article.articleNo)" class="article-row">
            <td>{{ article.articleNo }}</td>
            <td class="subject">{{ article.subject }}</td>
            <td>{{ article.userId }}</td>
            <td>{{ formatDate(article.registerTime) }}</td>
            <td>{{ article.hit }}</td>
          </tr>
          <tr v-if="!articles.length">
            <td colspan="5" class="no-articles">게시글이 없습니다.</td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- 페이지네이션 -->
    <div class="pagination" v-if="pageInfo.totalElements > 0">
      <div class="pagination-info">
        전체 {{ pageInfo.totalElements }}개 중 {{ startIndex + 1 }}-{{ endIndex }} 표시
      </div>
      <div class="pagination-controls">
        <button 
          :disabled="pageInfo.first" 
          @click="changePage(0)"
          class="page-btn"
        >
          처음
        </button>
        <button 
          :disabled="pageInfo.first" 
          @click="changePage(pageInfo.number - 1)"
          class="page-btn"
        >
          이전
        </button>
        <button 
          v-for="pageNum in displayedPages" 
          :key="pageNum"
          @click="changePage(pageNum - 1)"
          :class="['page-num-btn', { active: pageNum === pageInfo.number + 1 }]"
        >
          {{ pageNum }}
        </button>
        <button 
          :disabled="pageInfo.last" 
          @click="changePage(pageInfo.number + 1)"
          class="page-btn"
        >
          다음
        </button>
        <button 
          :disabled="pageInfo.last" 
          @click="changePage(pageInfo.totalPages - 1)"
          class="page-btn"
        >
          마지막
        </button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';
import { useApiStore } from '@/stores/apiStore';
import { useAuthStore } from '@/stores/authStore';
import axios from 'axios';

const router = useRouter();
const apiStore = useApiStore();
const authStore = useAuthStore();

const articles = ref([]);
const pageInfo = ref({
  number: 0,
  totalPages: 1,
  first: true,
  last: true,
  totalElements: 0,
  size: 10
});

const pageSizeOptions = [5, 10, 20, 30, 50];
const pageSize = ref(10);

const startIndex = computed(() => pageInfo.value.number * pageInfo.value.size);
const endIndex = computed(() => Math.min(
  startIndex.value + pageInfo.value.size,
  pageInfo.value.totalElements
));

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

const fetchArticles = async (page = 0, size = pageSize.value) => {
  try {
    const response = await axios.get(`${apiStore.baseURL}/board/list`, {
      params: { page, size }
    });
    articles.value = response.data.content;
    pageInfo.value = {
      ...response.data,
      size
    };
  } catch (error) {
    console.error('게시글 목록 조회 실패:', error);
  }
};

const handlePageSizeChange = () => {
  fetchArticles(0, pageSize.value);
};

const changePage = (newPage) => {
  if (newPage >= 0 && newPage < pageInfo.value.totalPages) {
    fetchArticles(newPage, pageSize.value);
  }
};

const displayedPages = computed(() => {
  const currentPage = pageInfo.value.number + 1;
  const totalPages = pageInfo.value.totalPages;
  const maxPages = 5;

  let startPage = Math.max(currentPage - Math.floor(maxPages / 2), 1);
  let endPage = startPage + maxPages - 1;

  if (endPage > totalPages) {
    endPage = totalPages;
    startPage = Math.max(endPage - maxPages + 1, 1);
  }

  return Array.from({ length: endPage - startPage + 1 }, (_, i) => startPage + i);
});

const goToDetail = (articleNo) => {
  router.push(`/board/${articleNo}`);
};

onMounted(() => {
  fetchArticles();
});
</script>

<style scoped>
.board-container {
  max-width: 95%;
  margin: 2rem auto;
  padding: 2rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.board-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.board-controls {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.page-size-select {
  padding: 0.5rem;
  border: 1px solid #009688;
  border-radius: 4px;
  color: #009688;
  background: white;
  cursor: pointer;
}

.write-btn {
  background: #009688;
  color: white;
  border: none;
  padding: 0.75rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 0.5rem;
  transition: all 0.3s ease;
}

.write-btn:hover {
  background: #00897b;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(0, 150, 136, 0.2);
}

.board-list {
  background-color: white;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

table {
  width: 100%;
  border-collapse: collapse;
}

th, td {
  padding: 12px;
  text-align: center;
  border-bottom: 1px solid #eee;
}

th {
  background-color: #f8f9fa;
  font-weight: 600;
  color: #495057;
}

.subject {
  text-align: left;
  color: #333;
}

.article-row {
  cursor: pointer;
  transition: background-color 0.2s;
}

.article-row:hover {
  background-color: #f8f9fa;
}

td:nth-child(1) { width: 8%; }  /* 번호 */
td:nth-child(2) { width: 50%; } /* 제목 */
td:nth-child(3) { width: 15%; } /* 작성자 */
td:nth-child(4) { width: 15%; } /* 작성일 */
td:nth-child(5) { width: 12%; } /* 조회수 */

.no-articles {
  text-align: center;
  padding: 20px;
  color: #666;
}

.pagination {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
  margin-top: 2rem;
}

.pagination-info {
  color: #616161;
  font-size: 0.9rem;
}

.pagination-controls {
  display: flex;
  gap: 0.5rem;
}

.page-btn, .page-num-btn {
  padding: 0.5rem 1rem;
  border: 1px solid #009688;
  background: white;
  color: #009688;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 40px;
  text-align: center;
}

.page-btn:disabled {
  border-color: #e0e0e0;
  color: #9e9e9e;
  cursor: not-allowed;
}

.page-btn:not(:disabled):hover,
.page-num-btn:hover {
  background: #e0f2f1;
}

.page-num-btn.active {
  background: #009688;
  color: white;
  border-color: #009688;
}
</style>
