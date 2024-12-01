<template>
  <div class="places-section">
    <h2>중요 장소</h2>
    <div class="places-grid" v-if="places.length > 0">
      <div v-for="place in places" :key="place.id" class="place-card">
        <div class="place-header">
          <div class="place-type">
            <i :class="getPlaceIcon(place.type)"></i>
            <span>{{ formatPlaceType(place.type) }}</span>
          </div>
          <button class="delete-btn" @click="deletePlace(place.id)">
            <i class="fas fa-trash-alt"></i>
          </button>
        </div>
        
        <div class="place-details">
          <div class="detail-row">
            <i class="fas fa-map-marker-alt"></i>
            <span>{{ place.address }}</span>
          </div>
          
          <div class="detail-row">
            <i class="fas fa-walking"></i>
            <span>도보 {{ place.walkingTime }}분</span>
          </div>
          
          <div class="detail-row">
            <i class="fas fa-car"></i>
            <span>차량 {{ place.drivingTime }}분</span>
          </div>
          
          <div class="detail-row">
            <i class="fas fa-bus"></i>
            <span>대중교통 {{ place.transitTime }}분</span>
          </div>
        </div>

        <div class="place-notes" v-if="place.notes">
          <i class="fas fa-sticky-note"></i>
          <p>{{ place.notes }}</p>
        </div>
      </div>
    </div>
    <div v-else class="no-places">
      <i class="fas fa-map-signs"></i>
      <p>등록된 중요 장소가 없습니다</p>
      <p class="sub-text">지도에서 장소를 선택하여 추가해보세요</p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const places = ref([
  {
    id: 1,
    type: 'work',
    address: '서울특별시 강남구 테헤란로 123',
    walkingTime: 15,
    drivingTime: 5,
    transitTime: 10,
    notes: '회사 위치'
  },
  {
    id: 2,
    type: 'school',
    address: '서울특별시 강남구 학동로 456',
    walkingTime: 20,
    drivingTime: 8,
    transitTime: 15,
    notes: '자녀 학교'
  }
]);

const getPlaceIcon = (type) => {
  const icons = {
    work: 'fas fa-briefcase',
    school: 'fas fa-school',
    hospital: 'fas fa-hospital',
    shopping: 'fas fa-shopping-cart',
    gym: 'fas fa-dumbbell',
    restaurant: 'fas fa-utensils',
    park: 'fas fa-tree',
    other: 'fas fa-map-marker'
  };
  return icons[type] || icons.other;
};

const formatPlaceType = (type) => {
  const types = {
    work: '직장',
    school: '학교',
    hospital: '병원',
    shopping: '쇼핑',
    gym: '운동',
    restaurant: '식당',
    park: '공원',
    other: '기타'
  };
  return types[type] || '기타';
};

const deletePlace = (id) => {
  if (confirm('이 장소를 삭제하시겠습니까?')) {
    places.value = places.value.filter(place => place.id !== id);
  }
};
</script>

<style scoped>
.places-section {
  margin-top: 20px;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.places-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-top: 20px;
}

.place-card {
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 15px;
  transition: transform 0.2s, box-shadow 0.2s;
}

.place-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.place-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

.place-type {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #009688;
  font-weight: 500;
}

.place-type i {
  font-size: 18px;
}

.delete-btn {
  background: none;
  border: none;
  color: #f44336;
  cursor: pointer;
  padding: 4px;
  opacity: 0.7;
  transition: opacity 0.2s;
}

.delete-btn:hover {
  opacity: 1;
}

.place-details {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.detail-row {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #666;
}

.detail-row i {
  width: 16px;
  color: #009688;
}

.place-notes {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #eee;
  display: flex;
  gap: 10px;
  color: #666;
}

.place-notes i {
  color: #009688;
  margin-top: 3px;
}

.place-notes p {
  margin: 0;
  font-size: 14px;
  line-height: 1.4;
}

.no-places {
  text-align: center;
  padding: 40px 20px;
}

.no-places i {
  font-size: 48px;
  color: #ccc;
  margin-bottom: 16px;
}

.no-places p {
  color: #666;
  margin: 0;
}

.no-places .sub-text {
  font-size: 14px;
  color: #999;
  margin-top: 8px;
}
</style>
