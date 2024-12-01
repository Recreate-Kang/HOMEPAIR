<template>
  <div class="store-list-container">
    <h3>Nearby Stores</h3>
    <div v-if="infraStore.isLoading" class="loading">
      Loading stores...
    </div>
    <div v-else-if="infraStore.error" class="error">
      <p>{{ infraStore.error }}</p>
      <p v-if="infraStore.error.includes('Map')">Please wait for the map to initialize...</p>
    </div>
    <div v-else>
      <div class="store-count">
        Found {{ infraStore.stores.length }} stores nearby
      </div>
      <ul class="store-list">
        <li v-for="store in infraStore.stores" :key="store.id" class="store-item">
          <div class="store-name">{{ store.name }}</div>
          <div class="store-details">
            <span class="store-type">{{ store.type }}</span>
            <span class="store-distance">{{ store.distance }}m</span>
          </div>
        </li>
      </ul>
    </div>
  </div>
</template>

<script setup>
import { useInfraStore } from '@/stores/infraStore';

const infraStore = useInfraStore();
</script>

<style scoped>
.store-list-container {
  padding: 15px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

h3 {
  margin-top: 0;
  margin-bottom: 15px;
  color: #333;
}

.loading, .error {
  text-align: center;
  padding: 20px;
  color: #666;
}

.error {
  color: #dc3545;
}

.store-count {
  margin-bottom: 10px;
  color: #666;
  font-size: 0.9em;
}

.store-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.store-item {
  padding: 10px;
  border-bottom: 1px solid #eee;
  transition: background-color 0.2s;
}

.store-item:last-child {
  border-bottom: none;
}

.store-item:hover {
  background-color: #f8f9fa;
}

.store-name {
  font-weight: bold;
  margin-bottom: 5px;
}

.store-details {
  display: flex;
  justify-content: space-between;
  font-size: 0.9em;
  color: #666;
}

.store-type {
  background: #e9ecef;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 0.8em;
}

.store-distance {
  color: #28a745;
}
</style>
