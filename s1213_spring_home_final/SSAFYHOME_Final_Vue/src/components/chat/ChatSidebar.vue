<template>
  <div class="chat-sidebar" :class="{ 'chat-sidebar--open': isOpen, 'chat-sidebar--closed': !isOpen }">
    <div class="chat-sidebar__toggle" @click="toggleSidebar">
      <span>{{ isOpen ? '>' : '<' }}</span>
    </div>
    
    <div class="chat-sidebar__content">
      <div class="chat-header">
        <h3>실시간 채팅</h3>
        <div class="chat-region">
          <i class="fas fa-map-marker-alt"></i> 
          {{ currentRegionName || '선택된 지역 없음' }}
        </div>
      </div>

      <div class="chat-messages" ref="messagesContainer">
        <div v-for="(msg, index) in messages" :key="index" 
             class="message-wrapper"
             :class="{ 
               'message-wrapper--mine': msg.sender === userID || msg.chatName === userID,
               'message-wrapper--system': msg.isSystem 
             }">
          <div class="message" 
               :class="[
                 msg.chatName === 'GPT' ? 'message--gpt' : '',
                 (msg.sender === userID || msg.chatName === userID) ? 'message--mine' : '',
                 msg.isSystem ? 'message--system' : ''
               ]">
            <div class="message__sender" v-if="msg.sender !== userID && msg.chatName !== userID && !msg.isSystem">
              {{ msg.sender || msg.chatName }}
            </div>
            <div class="message__content">{{ msg.chatMessage }}</div>
            <div class="message__time" v-if="!msg.isSystem">{{ formatTime(msg.chatTime) }}</div>
          </div>
        </div>
      </div>

      <div class="chat-input">
        <div class="input-container">
          <input 
            v-model="newMessage" 
            @keyup.enter="sendMessage"
            placeholder="메시지를 입력하세요..."
          />
          <button @click="sendMessage" :disabled="!isMessageButtonEnabled || !authStore.isLoggedIn">전송</button>
        </div>
        <div class="gpt-container">
          <button @click="sendGptMessage" :disabled="!isGptButtonEnabled || !authStore.isLoggedIn">
            GPT에게 물어보기
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch, computed } from 'vue';
import { useStompStore } from '@/stores/stompStore';
import { useAuthStore } from '@/stores/authStore';
import { storeToRefs } from 'pinia';

const props = defineProps({
  isOpen: {
    type: Boolean,
    required: true
  }
});

const emit = defineEmits(['update:isOpen']);

const stompStore = useStompStore();
const authStore = useAuthStore();

const { loginID } = storeToRefs(authStore);
const { currentRegionName, messages } = storeToRefs(stompStore);

const userID = computed(() => authStore.getUserID);

const newMessage = ref('');
const messagesContainer = ref(null);

const toggleSidebar = () => {
  emit('update:isOpen', !props.isOpen);
};

const formatTime = (timestamp) => {
  if (!timestamp) return '';
  const date = new Date(timestamp);
  return date.toLocaleTimeString('ko-KR', { hour: '2-digit', minute: '2-digit' });
};

const scrollToBottom = () => {
  if (messagesContainer.value) {
    setTimeout(() => {
      messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight;
    }, 100);
  }
};

// 새 메시지가 추가될 때마다 스크롤을 아래로 이동
watch(messages, () => {
  scrollToBottom();
}, { deep: true });

const sendMessage = async () => {
  if (!newMessage.value.trim()) return;

  try {
    const currentUserId = authStore.getUserID;
    console.log('Current User ID:', currentUserId);

    const messageData = {
      jwtToken: authStore.jwtToken,
      chatName: currentUserId,
      chatTime: new Date().toISOString(),
      district: stompStore.currentSubscription,
      chatMessage: newMessage.value.trim(),
      sender: currentUserId
    };

    console.log('Sending message data:', messageData);
    await stompStore.sendMessage(messageData);
    newMessage.value = '';
  } catch (error) {
    console.error('Failed to send message:', error);
  }
};

const sendGptMessage = async () => {
  if (!newMessage.value.trim()) return;

  try {
    const currentUserId = authStore.getUserID;
    const messageData = {
      jwtToken: authStore.jwtToken,
      chatName: 'GPT',
      chatTime: new Date().toISOString(),
      district: stompStore.currentSubscription,
      chatMessage: newMessage.value.trim(),
      sender: currentUserId
    };

    console.log('Sending GPT message data:', messageData);
    await stompStore.sendGptMessage(messageData);
    newMessage.value = '';
  } catch (error) {
    console.error('Failed to send GPT message:', error);
  }
};

const isMessageButtonEnabled = computed(() => {
  return newMessage.value.trim() !== '' && stompStore.currentSubscription !== null;
});

const isGptButtonEnabled = computed(() => {
  return newMessage.value.trim() !== '' && stompStore.currentSubscription !== null;
});

onMounted(async () => {
  if (!stompStore.connected) {
    await stompStore.connect();
  }
});

onUnmounted(() => {
  if (stompStore.connected) {
    stompStore.disconnect();
  }
});
</script>

<style scoped>
.chat-sidebar {
  position: fixed;
  top: 80px;
  right: 20px;
  width: 380px;
  height: calc(100vh - 100px);
  transition: right 0.3s ease;
  z-index: 999;
  display: flex;
}

.chat-sidebar--open {
  right: 20px;
}

.chat-sidebar--closed {
  right: -380px;
}

.chat-sidebar__toggle {
  position: absolute;
  left: -30px;
  top: 20px;
  width: 30px;
  height: 60px;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: -2px 0 5px rgba(0, 0, 0, 0.1);
  cursor: pointer;
  border-radius: 5px 0 0 5px;
  font-size: 1.2rem;
  color: #666;
  transition: color 0.2s;
}

.chat-sidebar__toggle:hover {
  color: #333;
}

.chat-sidebar__toggle span {
  display: inline-flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  height: 100%;
}

.chat-sidebar__content {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
}

.chat-header {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 80px;
  background: white;
  border-bottom: 1px solid #eee;
  padding: 15px 20px;
  border-radius: 12px 12px 0 0;
}

.chat-header h3 {
  margin: 0;
  font-size: 1.2rem;
  color: #333;
}

.chat-region {
  margin-top: 8px;
  color: #666;
  font-size: 0.9rem;
}

.chat-region i {
  margin-right: 5px;
  color: #2196f3;
}

.chat-messages {
  position: absolute;
  top: 80px;
  bottom: 120px;
  left: 0;
  right: 0;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.chat-messages::-webkit-scrollbar {
  display: none;
}

.chat-input {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 120px;
  border-top: 1px solid #eee;
  background: white;
  padding: 15px 20px;
  border-radius: 0 0 12px 12px;
}

.input-container {
  display: flex;
  gap: 10px;
  margin-bottom: 10px;
}

.input-container input {
  flex: 1;
  padding: 10px 15px;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 0.95rem;
  transition: border-color 0.2s;
}

.input-container input:focus {
  outline: none;
  border-color: #2196f3;
}

.input-container button,
.gpt-container button {
  padding: 10px 20px;
  background: #2196f3;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 0.95rem;
  cursor: pointer;
  transition: background-color 0.2s;
}

.input-container button:hover,
.gpt-container button:hover {
  background: #1976d2;
}

.input-container button:disabled,
.gpt-container button:disabled {
  background: #ccc;
  cursor: not-allowed;
}

.gpt-container {
  display: flex;
  justify-content: center;
}

.gpt-container button {
  width: 100%;
  background: #4caf50;
}

.gpt-container button:hover {
  background: #388e3c;
}

.gpt-container button:disabled {
  background: #ccc;
}

.message-wrapper {
  display: flex;
  margin-bottom: 10px;
  padding: 0 15px;
}

.message-wrapper--mine {
  justify-content: flex-end;
}

.message-wrapper--system {
  justify-content: center;
}

.message {
  max-width: 70%;
  padding: 8px 12px;
  border-radius: 12px;
  background-color: #f1f1f1;
  position: relative;
}

.message--mine {
  background-color: #dcf8c6;
  margin-left: auto;
}

.message--gpt {
  background-color: #e3f2fd;
}

.message--system {
  background-color: #e8eaf6;
  color: #3f51b5;
  text-align: center;
  max-width: 70%;
  margin: 8px auto;
  font-size: 0.8em;
  padding: 4px 12px;
  border-radius: 12px;
}

.message--system .message__content {
  color: inherit;
}

.message__sender {
  font-size: 0.8em;
  color: #666;
  margin-bottom: 4px;
}

.message__content {
  word-break: break-word;
}

.message__time {
  font-size: 0.7em;
  color: #999;
  text-align: right;
  margin-top: 4px;
}
</style>
