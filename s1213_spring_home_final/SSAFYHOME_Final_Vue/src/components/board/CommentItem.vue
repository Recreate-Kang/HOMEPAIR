<template>
  <div class="comment-item" :class="{ 'child-comment': depth > 0 }">
    <div class="comment-header">
      <span class="comment-author">{{ comment.userId }}</span>
      <span class="comment-date">{{ formatDate(comment.createdTime) }}</span>
    </div>
    <div class="comment-content">
      {{ comment.content }}
    </div>
    <div class="comment-actions">
      <button 
        @click="startReply"
        class="action-btn"
      >
        답글
      </button>
      <template v-if="authStore.userId === comment.userId">
        <button 
          @click="startEdit"
          class="action-btn"
        >
          수정
        </button>
        <button 
          @click="handleDelete"
          class="action-btn delete"
        >
          삭제
        </button>
      </template>
    </div>

    <!-- 답글 작성 폼 -->
    <div v-if="showReplyForm" class="reply-form">
      <textarea 
        v-model="newReply"
        placeholder="답글을 작성하세요"
        rows="2"
        class="reply-textarea"
      ></textarea>
      <div class="reply-actions">
        <button 
          @click="submitReply"
          :disabled="!newReply || !authStore.loginID"
          class="reply-submit-btn"
        >
          답글 작성
        </button>
        <button 
          @click="cancelReply"
          class="reply-cancel-btn"
        >
          취소
        </button>
      </div>
    </div>

    <!-- 댓글 수정 폼 -->
    <div v-if="isEditing" class="edit-form">
      <textarea 
        v-model="editContent"
        rows="2"
        class="edit-textarea"
      ></textarea>
      <div class="edit-actions">
        <button 
          @click="saveEdit"
          :disabled="!editContent"
          class="edit-submit-btn"
        >
          수정 완료
        </button>
        <button 
          @click="cancelEdit"
          class="edit-cancel-btn"
        >
          취소
        </button>
      </div>
    </div>

    <!-- 재귀적으로 자식 댓글 표시 -->
    <div v-if="comment.children && comment.children.length > 0" class="children-comments">
      <CommentItem
        v-for="child in comment.children"
        :key="child.commentId"
        :comment="child"
        :depth="depth + 1"
        :articleId="articleId"
        @refresh="$emit('refresh')"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useApiStore } from '@/stores/apiStore';
import axios from 'axios';

const props = defineProps({
  comment: {
    type: Object,
    required: true
  },
  depth: {
    type: Number,
    default: 0
  },
  articleId: {
    type: [String, Number],
    required: true
  }
});

const emit = defineEmits(['refresh']);
const authStore = useAuthStore();
const apiStore = useApiStore();

// 상태 관리
const showReplyForm = ref(false);
const isEditing = ref(false);
const newReply = ref('');
const editContent = ref('');

// 날짜 포맷팅
const formatDate = (dateString) => {
  if (!dateString) return '';
  const date = new Date(dateString);
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
};

// 답글 관련 메서드
const startReply = () => {
  if (!authStore.isLoggedIn) {
    alert('로그인이 필요한 기능입니다.');
    return;
  }
  showReplyForm.value = true;
  newReply.value = '';
};

const cancelReply = () => {
  showReplyForm.value = false;
  newReply.value = '';
};

const submitReply = async () => {
  try {
    const reply = {
      userId: authStore.loginID,
      content: newReply.value,
      createdTime: new Date().toISOString(),
      parentCommentId: props.comment.commentId
    };

    await axios.post(
      `${apiStore.baseURL}/comment/write/${props.articleId}`,
      reply,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    showReplyForm.value = false;
    newReply.value = '';
    emit('refresh');
  } catch (error) {
    console.error('답글 작성 실패:', error);
    alert('답글 작성에 실패했습니다.');
  }
};

// 수정 관련 메서드
const startEdit = () => {
  isEditing.value = true;
  editContent.value = props.comment.content;
};

const cancelEdit = () => {
  isEditing.value = false;
  editContent.value = '';
};

const saveEdit = async () => {
  try {
    const updatedComment = {
      commentId: props.comment.commentId,
      content: editContent.value,
      userId: props.comment.userId,
      createdTime: new Date().toISOString(),
      parentCommentId: props.comment.parentCommentId
    };

    await axios.post(
      `${apiStore.baseURL}/comment/edit/${props.comment.articleNo}`,
      updatedComment,
      {
        headers: {
          'Content-Type': 'application/json'
        }
      }
    );

    isEditing.value = false;
    editContent.value = '';
    emit('refresh');
  } catch (error) {
    console.error('댓글 수정 실패:', error);
    alert('댓글 수정에 실패했습니다.');
  }
};

// 삭제 메서드
const handleDelete = async () => {
  if (!confirm('정말 삭제하시겠습니까?')) return;

  try {
    await axios.delete(`${apiStore.baseURL}/comment/delete/${props.comment.commentId}`);
    emit('refresh');
  } catch (error) {
    console.error('댓글 삭제 실패:', error);
    alert('댓글 삭제에 실패했습니다.');
  }
};
</script>

<style scoped>
.comment-item {
  padding: 1rem 0;
}

.child-comment {
  margin-left: 20px;
  position: relative;
}

.child-comment::before {
  content: '';
  position: absolute;
  left: -12px;
  top: 0;
  height: 100%;
  border-left: 2px solid #e0e0e0;
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

.reply-actions,
.edit-actions {
  display: flex;
  gap: 0.5rem;
  justify-content: flex-end;
}

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

.reply-submit-btn:disabled,
.edit-submit-btn:disabled {
  background-color: #ccc;
  cursor: not-allowed;
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

.children-comments {
  margin-top: 1rem;
}
</style>
