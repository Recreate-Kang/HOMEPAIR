import { defineStore } from "pinia";
import { useAuthStore } from "./authStore";

export const useStompStore = defineStore("stompStore", {
  state: () => ({
    socket: null,
    connected: false,
    currentSubscription: null,
    currentRegionName: null,
    messages: [],
    baseURL: "/HOME/conn",
    reconnectAttempts: 0,
    maxReconnectAttempts: 10,
    reconnectDelay: 2000,
  }),

  actions: {
    async connect() {
      if (this.connected) return;

      const wsUrl = `ws://localhost:8080${this.baseURL}`;
      
      return new Promise((resolve, reject) => {
        try {
          this.socket = new WebSocket(wsUrl);

          this.socket.onopen = () => {
            console.log('WebSocket Connected');
            this.connected = true;
            this.reconnectAttempts = 0;
            
            if (this.currentSubscription) {
              this.resubscribe();
            }
            
            resolve();
          };

          this.socket.onmessage = (event) => {
            try {
              const message = JSON.parse(event.data);
              console.log('Received raw message:', message);
              
              // chatName이 비어있거나 null인 경우 sender 값을 사용
              if ((!message.chatName || message.chatName.trim() === '') && message.sender) {
                console.log('ChatName is empty, using sender:', message.sender);
                message.chatName = message.sender;
              }
              
              // 현재 로그인한 사용자의 ID 가져오기
              const currentUserId = useAuthStore().getUserID;
              console.log('Current User ID for validation:', currentUserId);
              
              // 메시지가 현재 사용자의 것인지 확인
              message.isMine = message.sender === currentUserId || message.chatName === currentUserId;
              console.log('Is message mine?', message.isMine);
              
              console.log('Processed message:', message);
              this.messages.push(message);
            } catch (error) {
              console.error('Error parsing message:', error);
            }
          };

          this.socket.onerror = (error) => {
            console.error('WebSocket Error:', error);
            this.handleConnectionError();
            reject(error);
          };

          this.socket.onclose = () => {
            console.log('WebSocket Disconnected');
            this.connected = false;
            this.handleConnectionError();
          };

        } catch (error) {
          console.error('Failed to connect to WebSocket:', error);
          this.handleConnectionError();
          reject(error);
        }
      });
    },

    handleConnectionError() {
      if (this.reconnectAttempts < this.maxReconnectAttempts) {
        this.reconnectAttempts++;
        const delay = this.reconnectDelay * Math.min(this.reconnectAttempts, 5);
        console.log(`Attempting to reconnect in ${delay}ms... (Attempt ${this.reconnectAttempts})`);
        setTimeout(() => this.connect(), delay);
      } else {
        console.error('Max reconnection attempts reached');
      }
    },

    async disconnect() {
      if (!this.connected) return;
      
      return new Promise((resolve) => {
        if (this.currentSubscription) {
          this.unsubscribe(this.currentSubscription);
        }
        this.socket.close();
        this.connected = false;
        this.messages = [];
        resolve();
      });
    },

    async subscribe(region, regionName) {
      if (!this.connected) {
        await this.connect();
      }

      if (this.currentSubscription && this.currentSubscription !== region) {
        this.unsubscribe(this.currentSubscription);
      }

      const subscribeMessage = `SUBSCRIBE\n\n${JSON.stringify({
        districtCode: region,
        jwtToken: useAuthStore().accessToken
      })}`;

      console.log('Sending subscribe message:', subscribeMessage);
      this.socket.send(subscribeMessage);
      this.currentSubscription = region;
      this.currentRegionName = regionName;

      // 시스템 메시지 추가
      this.messages.push({
        chatName: 'System',
        chatMessage: `${regionName}에 연결되었습니다.`,
        chatTime: new Date().toISOString(),
        isSystem: true
      });
    },

    async resubscribe() {
      if (this.currentSubscription) {
        await this.subscribe(this.currentSubscription, this.currentRegionName);
      }
    },

    async unsubscribe(region) {
      if (!this.connected || !this.currentSubscription) return;

      const unsubscribeMessage = `UNSUBSCRIBE\n\n${JSON.stringify({
        districtCode: region,
        jwtToken: useAuthStore().accessToken
      })}`;

      console.log('Sending unsubscribe message:', unsubscribeMessage);
      this.socket.send(unsubscribeMessage);
      this.currentSubscription = null;
      this.currentRegionName = null;
      this.messages = [];
    },

    async sendMessage(messageData) {
      if (!this.connected || !this.currentSubscription) {
        throw new Error('Not connected or no subscription');
      }

      const destination = `/topic/chat/${this.currentSubscription}`;
      const headers = {
        'destination': destination,
        'content-type': 'application/json'
      };

      const messageString = `SEND\ndestination:${destination}\ncontent-type:application/json\n\n${JSON.stringify({
        jwtToken: messageData.jwtToken,
        chatName: messageData.chatName,
        chatTime: messageData.chatTime,
        district: messageData.district,
        chatMessage: messageData.chatMessage,
        sender: messageData.sender
      })}`;
      
      console.log('Sending message:', messageString);
      this.socket.send(messageString);
    },

    async sendGptMessage(messageData) {
      if (!this.connected || !this.currentSubscription) {
        throw new Error('Not connected or no subscription');
      }

      const destination = `/topic/chat/${this.currentSubscription}`;
      const headers = {
        'destination': destination,
        'content-type': 'application/json'
      };
      
      const messageString = `SEND\ndestination:${destination}\ncontent-type:application/json\n\n${JSON.stringify({
        jwtToken: messageData.jwtToken,
        chatName: messageData.chatName,
        chatTime: messageData.chatTime,
        district: messageData.district,
        chatMessage: messageData.chatMessage,
        sender: messageData.sender
      })}`;
      
      console.log('Sending GPT message:', messageString);
      this.socket.send(messageString);
    },

    addMessage(message) {
      this.messages.push(message);
    },

    clearMessages() {
      this.messages = [];
    }
  },
});