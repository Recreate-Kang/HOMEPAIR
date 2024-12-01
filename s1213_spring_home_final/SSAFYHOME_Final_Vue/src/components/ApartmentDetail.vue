<template>
  <Transition name="slide-fade">
    <div v-if="apartmentStore.selectedApt" class="apartment-detail">
      <button class="close-button" @click="closeDetail">&times;</button>
      <div class="detail-header">
        <h2>{{ apartmentStore.selectedApt.aptName }}</h2>
        <div class="build-year">건축년도: {{ apartmentStore.selectedApt.buildYear }}년</div>
        <h3>are you listening</h3>
      </div>
      <div class="aptdeal">
        <ApartmentDeal :apartment="apartmentStore.selectedApt" />
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
      </div>
    </div>
  </Transition>
</template>

<script setup>
import { useApartmentStore } from "@/stores/apartmentStore";
import { useInfraStore } from "@/stores/infraStore";
import { useAptDealStore } from "@/stores/aptdealStore";
const apartmentStore = useApartmentStore();
const infraStore = useInfraStore();

const closeDetail = () => {
  apartmentStore.selectedApt = null;
  infraStore.clearInfraData();
};

</script>

<style scoped>
.apartment-detail {
  position: fixed;
  top: 20px;
  left: 420px; /* 사이드바 너비(400px) + 여백(20px) */
  width: 380px;
  max-height: calc(100vh - 40px);
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  padding: 25px;
  border-radius: 12px;
  overflow-y: auto;
  z-index: 1000;
}

.detail-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 2px solid #e9ecef;
}

.detail-header h2 {
  margin: 0;
  font-size: 1.5rem;
  color: #212529;
}

.build-year {
  color: #6c757d;
  margin-top: 5px;
}

.infra-container {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
  margin-top: 20px;
}

.infra-section {
  background-color: #f8f9fa;
  border-radius: 8px;
  padding: 15px;
}

.infra-section h3 {
  margin: 0 0 10px 0;
  color: #343a40;
  font-size: 1rem;
}

.infra-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.infra-section li {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 8px 0;
  border-bottom: 1px solid #e9ecef;
}

.infra-section li:last-child {
  border-bottom: none;
}

.infra-name {
  flex: 1;
  font-weight: 500;
}

.infra-type {
  color: #6c757d;
  font-size: 0.9rem;
}

.infra-distance {
  color: #495057;
  font-size: 0.9rem;
  white-space: nowrap;
}

/* 스크롤바 스타일링 */
.apartment-detail::-webkit-scrollbar {
  width: 6px;
}

.apartment-detail::-webkit-scrollbar-track {
  background: #f1f1f1;
}

.apartment-detail::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 3px;
}

.apartment-detail::-webkit-scrollbar-thumb:hover {
  background: #555;
}

/* 닫기 버튼 */
.close-button {
  position: absolute;
  top: 20px;
  right: 20px;
  background: none;
  border: none;
  font-size: 1.5rem;
  color: #666;
  cursor: pointer;
  padding: 5px;
  transition: color 0.2s;
  z-index: 2;
}

.close-button:hover {
  color: #333;
}

/* 애니메이션 */
.slide-fade-enter-active {
  transition: all 0.3s ease-out;
}

.slide-fade-leave-active {
  transition: all 0.3s cubic-bezier(1, 0.5, 0.8, 1);
}

.slide-fade-enter-from,
.slide-fade-leave-to {
  transform: translateX(20px);
  opacity: 0;
}




</style>
