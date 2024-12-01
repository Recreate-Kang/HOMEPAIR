<template>
  <div class="container">
    <!-- Base Map -->
    <div id="map" class="map-container">Map Component</div>

    <!-- Left Overlay -->
    <div class="overlay left-overlay">
      <p>This is the left overlay.</p>
    </div>

    <!-- Right Overlay -->
    <div class="overlay right-overlay">
      <p>This is the right overlay.</p>
    </div>
  </div>
</template>
<script setup>
import { ref } from 'vue';
</script>
<style scoped>
/* Base container styling */
.container {
  position: relative;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background-color: #e0e0e0;
}

/* Map component */
.map-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: #d3f3ff;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  color: #0077b6;
  z-index: 1; /* Map is the base layer */
}

/* Overlay styling */
.overlay {
  position: absolute;
  width: 300px;
  height: 100px;
  background-color: rgba(0, 0, 0, 0.6); /* Semi-transparent black */
  color: white;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
  z-index: 10; /* Ensure overlays appear above the map */
}

/* Left overlay */
.left-overlay {
  top: 20px;
  left: 20px;
}

/* Right overlay */
.right-overlay {
  top: 20px;
  right: 20px;
}
</style>
