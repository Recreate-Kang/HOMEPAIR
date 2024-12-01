<template>
  <div class="comments-section">
    <h3>댓글 {{ totalCommentCount }}개</h3>

    <!-- 댓글 작성 폼 -->
    <div class="comment-form">
      <textarea
        v-model="newComment.content"
        placeholder="댓글을 작성하세요"
        rows="3"
        class="comment-textarea"
      ></textarea>
      <button
        @click="handleSubmitComment"
        :disabled="!newComment.content || !authStore.loginID"
        class="comment-submit-btn"
      >
        댓글 작성
      </button>
    </div>

    <!-- 댓글 목록 -->
    <div class="comments-list">
      <CommentItem
        v-for="comment in comments"
        :key="comment.commentId"
        :comment="comment"
        :depth="0"
        :article-id="articleId"
        @refresh="fetchComments"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue';
import axios from 'axios';
import { useAuthStore } from '@/stores/authStore';
import { useApiStore } from '@/stores/apiStore';
import CommentItem from './CommentItem.vue';

const props = defineProps({
  articleId: {
    type: [String, Number],
    required: true
  }
});

const authStore = useAuthStore();
const apiStore = useApiStore();

const comments = ref([]);
const totalCommentCount = computed(() => {
  const countComments = (comments) => {
    let count = comments.length;
    for (const comment of comments) {
      if (comment.children && comment.children.length > 0) {
        count += countComments(comment.children);
      }
    }
    return count;
  };
  return countComments(comments.value);
});

const newComment = ref({
  content: '',
  userId: ''
});

const newReply = ref({
  content: '',
  userId: ''
});

const replyTo = ref(null);
const editingComment = ref(null);
const editContent = ref('');
const showReplyForm = ref(false);
const replyingTo = ref(null);

const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

const organizeComments = (rawComments) => {
  const processComment = (comment, depth = 0) => {
    const processedComment = { ...comment };
    processedComment.depth = depth;
    if (comment.subComment && comment.subComment.length > 0) {
      processedComment.children = comment.subComment.map(subComment => processComment(subComment, depth + 1));
    } else {
      processedComment.children = [];
    }
    delete processedComment.subComment;
    return processedComment;
  };

  return rawComments.map(comment => processComment(comment));
};

const getCommentIndent = (depth) => {
  return depth * 20; // 20px씩 들여쓰기
};

const fetchComments = async () => {
  try {
    const currentTime = new Date().toISOString();
    const response = await axios.post(
      `${apiStore.baseURL}/comment/list/${props.articleId}`,
      currentTime,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
    
    if (response.data && response.data.commentList) {
      comments.value = organizeComments(response.data.commentList);
    } else {
      comments.value = [];
    }
  } catch (error) {
    console.error('댓글 목록 조회 실패:', error);
    alert('댓글 목록을 불러오는데 실패했습니다.');
  }
};

const handleSubmitComment = async () => {
  if (!newComment.value.content || !authStore.loginID) return;

  try {
    const comment = {
      userId: authStore.loginID,
      content: newComment.value.content,
      createdTime: new Date().toISOString(),
      parentCommentId: null
    };

    await axios.post(
      `${apiStore.baseURL}/comment/write/${props.articleId}`,
      comment,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    newComment.value.content = '';
    await fetchComments();
  } catch (error) {
    console.error('댓글 작성 실패:', error);
    alert('댓글 작성에 실패했습니다.');
  }
};

const handleSubmitReply = async () => {
  if (!newReply.value.content || !replyingTo.value || !authStore.loginID) return;

  try {
    const comment = {
      userId: authStore.loginID,
      content: newReply.value.content,
      createdTime: new Date().toISOString(),
      parentCommentId: replyingTo.value.commentId
    };

    await axios.post(
      `${apiStore.baseURL}/comment/write/${props.articleId}`,
      comment,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    newReply.value.content = '';
    showReplyForm.value = false;
    replyingTo.value = null;
    await fetchComments();
  } catch (error) {
    console.error('답글 작성 실패:', error);
    alert('답글 작성에 실패했습니다.');
  }
};

const handleDeleteComment = async (commentId) => {
  if (!confirm('댓글을 삭제하시겠습니까?')) return;

  try {
    await axios.delete(
      `${apiStore.baseURL}/comment/delete/${props.articleId}/${commentId}`,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );
    
    await fetchComments();
  } catch (error) {
    console.error('댓글 삭제 실패:', error);
    alert('댓글 삭제에 실패했습니다.');
  }
};

const handleSaveEdit = async () => {
  if (!editingComment.value || !editContent.value) return;

  try {
    const comment = {
      commentId: editingComment.value.commentId,
      content: editContent.value,
      userId: editingComment.value.userId,
      createdTime: new Date().toISOString(),
      parentCommentId: editingComment.value.parentCommentId
    };

    await axios.post(
      `${apiStore.baseURL}/comment/edit/${props.articleId}`,
      comment,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    editingComment.value = null;
    editContent.value = '';
    await fetchComments();
  } catch (error) {
    console.error('댓글 수정 실패:', error);
    alert('댓글 수정에 실패했습니다.');
  }
};

const startReply = (comment) => {
  if (!authStore.isLoggedIn) {
    alert('로그인이 필요한 기능입니다.');
    return;
  }
  showReplyForm.value = true;
  replyingTo.value = comment;
  newReply.value.content = '';
};

const cancelReply = () => {
  showReplyForm.value = false;
  replyingTo.value = null;
  newReply.value.content = '';
};

const startEdit = (comment) => {
  editingComment.value = { ...comment };
  editContent.value = comment.content;
};

const cancelEdit = () => {
  editingComment.value = null;
  editContent.value = '';
};

const autoRefreshInterval = 30000; // 30초
let refreshTimer = null;

onMounted(() => {
  fetchComments();
  refreshTimer = setInterval(fetchComments, autoRefreshInterval);
});

</script>

<style scoped>
.comments-section {
  margin-top: 2rem;
}

.comments-section h3 {
  margin-bottom: 1rem;
  color: #2c3e50;
  font-size: 1.2rem;
}

.comment-form {
  margin-bottom: 2rem;
}

.comment-textarea,
.reply-textarea,
.edit-textarea {
  width: 100%;
  padding: 0.8rem;
  border: 1px solid #e0e0e0;
  border-radius: 4px;
  resize: vertical;
  font-size: 0.95rem;
  margin-bottom: 0.5rem;
}

.comment-submit-btn,
.reply-submit-btn,
.edit-submit-btn {
  background-color: #1976d2;
  color: white;
  border: none;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.comment-submit-btn:disabled,
.reply-submit-btn:disabled,
.edit-submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.comment-item {
  padding: 1rem 0;
  border-bottom: 1px solid #eee;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 1rem;
  margin-bottom: 0.5rem;
}

.comment-author {
  font-weight: 500;
  color: #2c3e50;
}

.comment-date {
  color: #666;
  font-size: 0.85rem;
}

.comment-content {
  margin: 0.5rem 0;
  line-height: 1.5;
  color: #333;
}

.comment-actions {
  display: flex;
  gap: 0.5rem;
  margin-top: 0.5rem;
}

.action-btn {
  background: none;
  border: none;
  color: #666;
  cursor: pointer;
  font-size: 0.85rem;
  padding: 0.2rem 0.5rem;
}

.action-btn:hover {
  color: #1976d2;
}

.action-btn.delete:hover {
  color: #d32f2f;
}

.reply-form,
.edit-form {
  margin-top: 1rem;
  padding: 1rem 0;
}

.reply-actions,
.edit-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
  margin-top: 0.5rem;
}

.reply-cancel-btn,
.edit-cancel-btn {
  background-color: #f5f5f5;
  border: 1px solid #e0e0e0;
  padding: 0.5rem 1rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9rem;
}

.reply-cancel-btn:hover,
.edit-cancel-btn:hover {
  background-color: #e0e0e0;
}

@media (max-width: 768px) {
  .comment-actions {
    flex-wrap: wrap;
  }
  
  .action-btn {
    flex: 1;
    text-align: center;
  }
}
</style>
