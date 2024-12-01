<template>
  <div class="map-section" v-if="showMapArea">
    <div class="map-container" id="map"></div>
    <div class="map-controls">
      <button class="add-place-btn" @click="addImportantPlace">
        <i class="fas fa-plus"></i> 중요 장소 추가
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue';

const props = defineProps({
  showMapArea: {
    type: Boolean,
    default: false
  }
});

const emit = defineEmits(['add-place']);

const kakaoMap = ref(null);
let marker = null;

const initializeMap = () => {
  if (window.kakao && window.kakao.maps) {
    const mapContainer = document.getElementById('map');
    const mapOption = {
      center: new window.kakao.maps.LatLng(37.5665, 126.9780), // 서울 시청
      level: 3
    };

    kakaoMap.value = new window.kakao.maps.Map(mapContainer, mapOption);

    // 클릭 이벤트 리스너 추가
    window.kakao.maps.event.addListener(kakaoMap.value, 'click', (mouseEvent) => {
      const latlng = mouseEvent.latLng;
      
      // 기존 마커 제거
      if (marker) {
        marker.setMap(null);
      }

      // 새 마커 생성
      marker = new window.kakao.maps.Marker({
        position: latlng
      });

      marker.setMap(kakaoMap.value);
    });
  }
};

const addImportantPlace = () => {
  if (marker) {
    const position = marker.getPosition();
    emit('add-place', {
      latitude: position.getLat(),
      longitude: position.getLng()
    });
    marker.setMap(null);
    marker = null;
  } else {
    alert('지도에서 장소를 선택해주세요.');
  }
};

onMounted(() => {
  if (props.showMapArea) {
    initializeMap();
  }
});

onUnmounted(() => {
  if (marker) {
    marker.setMap(null);
  }
});
</script>

<style scoped>
.map-section {
  margin-top: 20px;
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.map-container {
  width: 100%;
  height: 400px;
}

.map-controls {
  padding: 15px;
  border-top: 1px solid #eee;
  display: flex;
  justify-content: flex-end;
}

.add-place-btn {
  background: #009688;
  color: white;
  border: none;
  padding: 8px 16px;
  border-radius: 6px;
  cursor: pointer;
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 14px;
  transition: background 0.2s;
}

.add-place-btn:hover {
  background: #00796b;
}

.add-place-btn i {
  font-size: 12px;
}
</style>
