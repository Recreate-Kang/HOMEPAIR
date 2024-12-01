<template>
    <div class="apt-deal-info">
      <h3>AptInfo Details</h3>
      <p>아파트 이름: {{ apartment.aptName }}</p>
      <p>건축년도: {{ apartment.buildYear }}</p>
      <!-- 필요에 따라 상세 내용을 추가하세요 -->
    </div>
  </template>
  
  <script>
  export default {
    name: "AptInfo",
    props: {
      apartment: {
        type: Object,
        required: true,
      },
    },
  };
  </script>
  
  <style>
  .apt-deal-info {
    padding: 20px;
    background-color: #f5f5f5;
    border: 1px solid #ccc;
    border-radius: 8px;
    margin-top: 20px;
  }
  </style>