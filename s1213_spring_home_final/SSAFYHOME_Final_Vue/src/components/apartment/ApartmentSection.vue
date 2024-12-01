<template>
  <div class="apartment-section">
    <h2>나의 아파트</h2>
    <div v-if="favorite?.selectedHomeAPT" class="apartment-details">
      <div class="apartment-header">
        <h3>{{ favorite.selectedHomeAPT.aptName }}</h3>
        <span class="apartment-code">{{ favorite.selectedHomeAPT.aptSeq }}</span>
      </div>
      
      <div class="details-grid">
        <div class="detail-item">
          <i class="fas fa-map-marker-alt"></i>
          <div class="detail-content">
            <span class="label">주소</span>
            <span class="value">{{ favorite.selectedHomeAPT.dongCode }}</span>
          </div>
        </div>
        
        <div class="detail-item">
          <i class="fas fa-building"></i>
          <div class="detail-content">
            <span class="label">건물 정보</span>
            <span class="value">{{ favorite.selectedHomeAPT.buildYear }}년 준공</span>
          </div>
        </div>
        
        <div class="detail-item">
          <i class="fas fa-expand-arrows-alt"></i>
          <div class="detail-content">
            <span class="label">면적(평균)</span>
            <span class="value">{{ favorite.avgArea }}㎡</span>
          </div>
        </div>
        
        <div class="detail-item">
          <i class="fas fa-won-sign"></i>
          <div class="detail-content">
            <span class="label">최근 거래가</span>
            <span class="value">{{ formatPrice(favorite.recentPrice) }}</span>
          </div>
        </div>

        <div class="detail-item">
          <i class="fas fa-chart-line"></i>
          <div class="detail-content">
            <span class="label">가격 변동</span>
            <span class="value">{{ formatPrice(favorite.priceDelta) }}</span>
          </div>
        </div>

        <div class="detail-item">
          <i class="fas fa-calendar-alt"></i>
          <div class="detail-content">
            <span class="label">최근 거래일</span>
            <span class="value">{{ formatDate(favorite.recentDeal) }}</span>
          </div>
        </div>
      </div>

      <div class="amenities-section">
        <h4>주변 시설</h4>
        <div class="amenities-grid">
          <div class="amenity-item" v-for="(count, type) in favorite.selectedHomeAPT.amenities" :key="type">
            <i :class="getAmenityIcon(type)"></i>
            <span class="amenity-label">{{ formatAmenityType(type) }}</span>
            <span class="amenity-count">{{ count }}</span>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="no-apartment">
      <i class="fas fa-home"></i>
      <p>선택된 아파트가 없습니다</p>
      <button class="select-apt-btn" @click="$router.push('/fullpage')">
        아파트 선택하기
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useAuthStore } from '@/stores/authStore';
import { useUserFavoriteStore } from '@/stores/userFavoriteStore';

// 상태 관리
const favorite = ref(null); // 데이터를 저장할 상태
const userFavoriteStore = useUserFavoriteStore();
const authStore = useAuthStore();

// 데이터 포맷팅 함수
const formatPrice = (price) => {
  if (!price) return '정보 없음';
  return new Intl.NumberFormat('ko-KR', {
    style: 'currency',
    currency: 'KRW'
  }).format(price);
};

const formatDate = (date) => {
  if (!date) return '정보 없음';
  return new Date(date).toLocaleDateString('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  });
};

const getAmenityIcon = (type) => {
  const icons = {
    hospital: 'fas fa-hospital',
    school: 'fas fa-school',
    subway: 'fas fa-subway',
    bus: 'fas fa-bus',
    park: 'fas fa-tree',
    market: 'fas fa-shopping-cart'
  };
  return icons[type] || 'fas fa-building';
};

const formatAmenityType = (type) => {
  const types = {
    hospital: '병원',
    school: '학교',
    subway: '지하철',
    bus: '버스정류장',
    park: '공원',
    market: '마트'
  };
  return types[type] || type;
};

// 컴포넌트 로드 시 데이터 가져오기
onMounted(async () => {
  try {
    const userNum = authStore.getUserNum;
    if (userNum) {
      await userFavoriteStore.fetchUserFavorites(userNum); // 즐겨찾기 데이터 로드
      await userFavoriteStore.processHomeFavorites(userNum); // HOME 데이터 가공

      // processedFavorites에서 HOME 데이터 가져오기
      const processedFavorites = userFavoriteStore.processedFavorites || [];
      favorite.value = processedFavorites;

      if (!favorite.value) {
        console.warn("HOME 데이터가 없습니다.");
      }
    } else {
      console.error("사용자 정보가 없습니다.");
    }
  } catch (error) {
    console.error("데이터 로드 중 오류 발생:", error);
  }
});
</script>
<style scoped>
.apartment-section {
  margin-top: 20px;
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.apartment-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.apartment-header h3 {
  margin: 0;
  font-size: 20px;
  color: #333;
}

.apartment-code {
  color: #666;
  font-size: 14px;
}

.details-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.detail-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
}

.detail-item i {
  color: #009688;
  font-size: 20px;
  margin-top: 3px;
}

.detail-content {
  display: flex;
  flex-direction: column;
}

.label {
  font-size: 12px;
  color: #666;
  margin-bottom: 4px;
}

.value {
  font-size: 16px;
  color: #333;
}

.price-up {
  color: #f44336;
}

.price-down {
  color: #2196f3;
}

.no-change {
  color: #666;
}

.amenities-section {
  border-top: 1px solid #eee;
  padding-top: 20px;
}

.amenities-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
  gap: 15px;
  margin-top: 15px;
}

.amenity-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px;
  background: #f5f5f5;
  border-radius: 8px;
}

.amenity-item i {
  color: #009688;
}

.amenity-label {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.amenity-count {
  font-weight: 500;
  color: #009688;
}

.no-apartment {
  text-align: center;
  padding: 40px 20px;
}

.no-apartment i {
  font-size: 48px;
  color: #ccc;
  margin-bottom: 16px;
}

.no-apartment p {
  color: #666;
  margin-bottom: 20px;
}

.select-apt-btn {
  background: #009688;
  color: white;
  border: none;
  padding: 8px 20px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: background 0.2s;
}

.select-apt-btn:hover {
  background: #00796b;
}
</style>
