<template>
  <div class="board-page">
    <router-view></router-view>
  </div>
</template>

<script setup>
import BoardList from '@/components/board/BoardList.vue';
defineOptions({
  name: 'BoardPage'
});
</script>

<style scoped>
.board-page {
  height: calc(100vh - 64px);  /* navbar 높이만큼 뺌 */
  overflow-y: auto;
  padding-top: 1rem;  /* navbar와의 간격 */
  background-color: #e0f2f1;
}
</style>