module.exports = {
    devServer: {
      proxy: {
        "/HOME/conn": {
          target: "http://localhost:8080", // Spring Boot 서버 URL
          changeOrigin: true,
          ws: true, // WebSocket 프록시 활성화
        },
      },
    },
  };
  