<template>
    <div>
      <h1>WebSocket 명령 처리</h1>
      <div>
        <h2>메시지 수신:</h2>
        <ul>
          <li v-for="msg in messages" :key="msg.id">{{ msg }}</li>
        </ul>
      </div>
      <div>
        <h2>SEND 명령</h2>
        <input type="text" v-model="chatMessage" placeholder="메시지 입력" />
        <input type="text" v-model="chatName" placeholder="채팅 이름 입력" />
        <button @click="sendChatMessage">SEND 전송</button>
      </div>
      <div>
        <h2>SUBSCRIBE 명령</h2>
        <input type="text" v-model="districtCode" placeholder="구독 코드 입력" />
        <button @click="sendSubscribeMessage">SUBSCRIBE 전송</button>
      </div>
      <button @click="disconnect">연결 종료</button>
    </div>
  </template>
  
  <script>
  export default {
    data() {
      return {
        socket: null, // WebSocket 객체
        messages: [], // 서버에서 받은 메시지 저장
        chatMessage: "", // SEND 메시지
        chatName: "DefaultChat", // 기본 채팅 이름
        districtCode: "1111010", // SUBSCRIBE 메시지
      };
    },
    methods: {
      // WebSocket 연결
      connectWebSocket() {
        this.socket = new WebSocket("ws://localhost:8080/HOME/conn");
  
        this.socket.onopen = () => {
          console.log("WebSocket 연결 성공");
        };
  
        this.socket.onmessage = (event) => {
          console.log("서버로부터 메시지 수신:", event.data);
          this.messages.push(event.data); // 받은 메시지 저장
        };
  
        this.socket.onclose = () => {
          console.log("WebSocket 연결 종료");
        };
  
        this.socket.onerror = (error) => {
          console.error("WebSocket 오류:", error);
        };
      },
  
      // SEND 명령 전송
      sendChatMessage() {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
          const payload = {
            jwtToken: "exampleToken", // 서버에서 JWT 인증에 필요하다면 변경
            chatName: this.chatName,
            chatTime: new Date().toISOString(),
            district: this.districtCode, // 필요에 따라 수정
            chatMessage: this.chatMessage,
          };
  
          const message = `SEND\n\n${JSON.stringify(payload)}`;
          this.socket.send(message);
          console.log("SEND 메시지 전송:", message);
          this.chatMessage = ""; // 입력 필드 초기화
        } else {
          console.error("WebSocket 연결이 열려있지 않습니다.");
        }
      },
  
      // SUBSCRIBE 명령 전송
      sendSubscribeMessage() {
        if (this.socket && this.socket.readyState === WebSocket.OPEN) {
          const payload = {
            districtCode: this.districtCode,
          };
  
          const message = `SUBSCRIBE\n\n${JSON.stringify(payload)}`;
          this.socket.send(message);
          console.log("SUBSCRIBE 메시지 전송:", message);
          this.districtCode = ""; // 입력 필드 초기화
        } else {
          console.error("WebSocket 연결이 열려있지 않습니다.");
        }
      },
  
      // 연결 종료
      disconnect() {
        if (this.socket) {
          this.socket.close();
          console.log("WebSocket 연결 종료 요청");
        }
      },
    },
  
    mounted() {
      this.connectWebSocket();
    },
  
    beforeDestroy() {
      this.disconnect(); // 컴포넌트가 파괴되기 전에 연결 종료
    },
  };
  </script>
  
  <style scoped>
  h1 {
    font-size: 1.5rem;
  }
  div {
    margin-bottom: 1rem;
  }
  input {
    margin-right: 0.5rem;
  }
  button {
    margin-right: 0.5rem;
  }
  </style>
  