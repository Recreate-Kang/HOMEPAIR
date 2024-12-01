<template>
  <div v-if="apartmentStore.showDetails" class="details-container" :class="{ 'slide-out': !isOpen }">
    <button class="toggle-button" @click="toggleDetails">×</button>
    <div class="header">
      <h2>{{ apartmentStore.selectedApt.aptName }}</h2>
    </div>
    <div class="build-year">
      건축년도: {{ apartmentStore.selectedApt.buildYear }}년
    </div>
    <ApartmentEvaluation />
    <div class="aptdeal">
      <ApartmentPriceChart :apartment="apartmentStore.selectedApt" />
    </div>
    <div class="infra-container">
      <div v-if="infraStore.hospitals.length" class="infra-section">
        <h3>주변 병원</h3>
        <ul>
          <li v-for="hospital in infraStore.hospitals.slice(0, 3)" :key="hospital.keyId">
            <div class="infra-name">{{ hospital.serviceName }}</div>
            <div class="infra-distance">{{ Math.round(hospital.distance) }}m</div>
          </li>
        </ul>
      </div>

      <div v-if="infraStore.schools.length" class="infra-section">
        <h3>주변 학교</h3>
        <ul>
          <li v-for="school in infraStore.schools.slice(0, 3)" :key="school.keyId">
            <div class="infra-name">{{ school.schoolName }}</div>
            <div class="infra-type">({{ school.schoolLevel }})</div>
            <div class="infra-distance">{{ Math.round(school.distance) }}m</div>
          </li>
        </ul>
      </div>

      <div v-if="infraStore.subways.length" class="infra-section">
        <h3>주변 지하철</h3>
        <ul>
          <li v-for="subway in infraStore.subways.slice(0, 3)" :key="subway.keyId">
            <div class="infra-name">{{ subway.subwayName }}역</div>
            <div class="infra-type">({{ subway.subwayLine }})</div>
            <div class="infra-distance">{{ Math.round(subway.distance) }}m</div>
          </li>
        </ul>
      </div>

      <div v-if="infraStore.parks.length" class="infra-section">
        <h3>주변 공원</h3>
        <ul>
          <li v-for="park in infraStore.parks.slice(0, 3)" :key="park.manageId">
            <div class="infra-name">{{ park.parkName }}</div>
            <div class="infra-type">({{ park.typeName }})</div>
            <div class="infra-distance">{{ Math.round(park.distance) }}m</div>
          </li>
        </ul>
      </div>

      <div v-if="infraStore.marts.length" class="infra-section">
        <h3>주변 마트</h3>
        <ul>
          <li v-for="mart in infraStore.marts.slice(0, 3)" :key="mart.keyId">
            <div class="infra-name">{{ mart.serviceName }}</div>
            <div class="infra-distance">{{ Math.round(mart.distance) }}m</div>
          </li>
        </ul>
      </div>

      <div v-if="infraStore.libraries.length" class="infra-section">
        <h3>주변 도서관</h3>
        <ul>
          <li v-for="library in infraStore.libraries.slice(0, 3)" :key="library.keyId">
            <div class="infra-name">{{ library.serviceName }}</div>
            <div class="infra-distance">{{ Math.round(library.distance) }}m</div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useApartmentStore } from '@/stores/apartmentStore';
import { useInfraStore } from '@/stores/infraStore';
import { useAptDealStore } from '@/stores/aptdealStore';
import { ref } from 'vue';
import ApartmentPriceChart from './ApartmentPriceChart.vue';
import ApartmentEvaluation from './ApartmentEvaluation.vue';

const apartmentStore = useApartmentStore();
const aptDealStore = useAptDealStore();
const infraStore = useInfraStore();
const isOpen = ref(true);

const toggleDetails = () => {
  isOpen.value = !isOpen.value;
  if (!isOpen.value) {
    setTimeout(() => {
      apartmentStore.showDetails = false;
      infraStore.clearInfraData();
    }, 300); // 애니메이션 시간과 동일하게 설정
  }
};
</script>

<style scoped>
.details-container {
  position: fixed;
  top: 80px;
  left: 410px;
  width: 380px;
  max-height: calc(100vh - 100px);
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  padding: 25px;
  border-radius: 12px;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
  z-index: 100;
  animation: slideIn 0.3s ease-out forwards;
  color: #333;
}

.toggle-button {
  position: absolute;
  right: 10px;
  top: 10px;
  width: 30px;
  height: 30px;
  background: transparent;
  border: none;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  font-size: 1.5rem;
  color: #666;
  transition: color 0.2s;
}

.toggle-button:hover {
  color: #333;
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #333;
}

.build-year {
  color: #666;
  margin-bottom: 20px;
  font-size: 0.95rem;
}

.infra-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.infra-section {
  background: #f8f9fa;
  padding: 20px;
  border-radius: 12px;
  transition: transform 0.2s ease;
}

.infra-section:hover {
  transform: translateY(-2px);
}

.infra-section h3 {
  margin: 0 0 15px 0;
  font-size: 1.1rem;
  color: #333;
}

ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

li {
  display: flex;
  align-items: center;
  padding: 8px 0;
  gap: 10px;
}

li:not(:last-child) {
  border-bottom: 1px solid #eee;
}

.infra-name {
  flex: 1;
  font-weight: 500;
  color:#bbb;
}

.infra-type {
  color: #666;
  font-size: 0.9rem;
  margin-right: 10px;
}

.infra-distance {
  color: #2196f3;
  font-size: 0.9rem;
  min-width: 60px;
  text-align: right;
  font-weight: 500;
}

@keyframes slideIn {
  from {
    transform: translateX(-100%);
    opacity: 0;
  }
  to {
    transform: translateX(0);
    opacity: 1;
  }
}

@keyframes slideOut {
  from {
    transform: translateX(0);
    opacity: 1;
  }
  to {
    transform: translateX(-100%);
    opacity: 0;
  }
}

.slide-out {
  animation: slideOut 0.3s ease-out forwards;
}
</style>