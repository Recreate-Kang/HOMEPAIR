<template>
  <div class="apartment-list-container">
        <!-- Page navigation -->
    <div class="navigation">
      <button :disabled="currentPage === 1" @click="handlePrevPage">◀ 이전</button>
      <span class="page-indicator">{{ currentPage }} / {{ totalPages }}</span>
      <button :disabled="currentPage === totalPages" @click="handleNextPage">다음 ▶</button>
    </div>
    <div class="search-info">
      <span class="region"><strong>검색 지역:</strong> {{ selectedRegion }}</span>
      <span class="count"><strong>아파트 수:</strong> {{ apartments.length }}</span>
    </div>
    <!-- 아파트 리스트 -->
    <div class="apartment-list">
      <div
        v-for="(apartment, index) in paginatedApartments"
        :key="apartment.aptSeq"
        class="apartment-item"
        :class="{ active: isHighlighted(apartment) }"
        @click="handleAptListClick(apartment)"
      >
        <div class="apt-name">{{ apartment.aptName }}</div>
        <div class="apt-info">
          <span class="build-year">{{ apartment.buildYear }}년</span>
          <img
            src="../assets/star-off.png"
            class="star"
            @click.stop="handleStarClick(apartment)"
          />
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div v-if="isModalOpen" class="modal-backdrop" @click="closeModal">
      <div class="modal-content" @click.stop>
        <ApartmentRegister
          :apartment="selectedApartmentForRegister"
          @close="closeModal"
        />
      </div>
    </div>
  </div>
</template>
<script setup>
import { ref, computed, watch } from "vue";
import { useApartmentStore } from "@/stores/apartmentStore";
import { useRegionStore } from "@/stores/regionStore";
import { useInfraStore } from "@/stores/infraStore";
import { useTotalEvaluationStore } from "@/stores/totalEvaluationStore";
import ApartmentRegister from "@/components/ApartmentRegister.vue";

// Pinia stores
const apartmentStore = useApartmentStore();
const regionStore = useRegionStore();
const infraStore = useInfraStore();
const isModalOpen = ref(false); 
// Reactive value for selected region
const selectedRegion = computed(() => regionStore.getSelectedPath()); // Computed to ensure reactivity

// Log the selected region whenever it changes
watch(
  () => selectedRegion.value,
  (newRegion) => {
    console.log("Selected Region Updated:", newRegion);
  }
);

// Apartments and pagination
const apartments = computed(() => apartmentStore.apartments);
const currentPage = ref(1);
const itemsPerPage = 7;
const totalPages = computed(() => Math.ceil(apartments.value.length / itemsPerPage));

const selectedApartmentForRegister = ref(null); // 선택된 아파트 정보

const handleStarClick = (apartment) => {
  selectedApartmentForRegister.value = apartment; // 선택된 아파트 저장
  isModalOpen.value = true; // 팝업 열기
};

const closeModal = () => {
  isModalOpen.value = false; // 팝업 닫기
};

const paginatedApartments = computed(() => {
  const start = (currentPage.value - 1) * itemsPerPage;
  const end = start + itemsPerPage;
  return apartments.value.slice(start, end);
});

// Highlighting logic
const isHighlighted = (apartment) => {
  return apartmentStore.selectedApt?.aptSeq === apartment.aptSeq;
};

// Event handlers
const handleAptListClick = async (apartment) => {
  try {
    const totalEvaluationStore = useTotalEvaluationStore();
    totalEvaluationStore.clearEvaluation(); // Clear previous evaluation
    
    apartmentStore.storeLegacyApt();
    apartmentStore.selectApartment(apartment);
    apartmentStore.showDetails = !apartmentStore.showDetails
    await infraStore.getStoreListInRange(apartment.latitude, apartment.longitude);
  } catch (error) {
    console.error('Error handling apartment selection:', error);
  }
};

const handleAptPagebyClick = (selectedApartment) => {
  //apartmentStore.selectApartment(selectedApartment);

  const index = apartments.value.findIndex(
    (apt) => apt.aptSeq === selectedApartment.aptSeq
  );

  if (index !== -1) {
    currentPage.value = Math.floor(index / itemsPerPage) + 1;
  }
};

watch(
  () => apartmentStore.selectedApt,
  //() => apartmentStore.selectApartmentm
  (selectedApt) => {
    if (selectedApt) {
      console.log("Apartment selected:", selectedApt);
      handleAptPagebyClick(selectedApt);
    }
  }
);

// Page navigation
const handlePrevPage = () => {
  if (currentPage.value > 1) {
    currentPage.value--;
  }
};

const handleNextPage = () => {
  if (currentPage.value < totalPages.value) {
    currentPage.value++;
  }
};
</script>

<style scoped>
.apartment-list-container {
  height: calc(100% - 60px);
  overflow-y: auto;
  scrollbar-width: none;  /* Firefox */
  -ms-overflow-style: none;  /* IE and Edge */
}

.apartment-list-container::-webkit-scrollbar {
  display: none;  /* Chrome, Safari, Opera */
}

.apartment-list {
  padding: 0 1rem;
  overflow-y: auto;
  scrollbar-width: none;
  -ms-overflow-style: none;
}

.apartment-list::-webkit-scrollbar {
  display: none;
}

.navigation {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
  margin-bottom: 15px;
  padding: 10px;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.navigation button {
  padding: 8px 15px;
  border: none;
  background-color: #e9ecef;
  color: #495057;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
}

.navigation button:not(:disabled):hover {
  background-color: #dee2e6;
}

.navigation button:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.page-indicator {
  font-size: 0.9rem;
  color: #495057;
  min-width: 60px;
  text-align: center;
}

.search-info {
  display: flex;
  flex-direction: column;
  gap: 10px;
  padding: 15px;
  background-color: white;
  border-radius: 8px;
  margin-bottom: 15px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.search-info .region,
.search-info .count {
  font-size: 0.9rem;
  color: #495057;
  display: block;
}

.search-info strong {
  color: #212529;
  margin-right: 5px;
  display: inline-block;
  width: 80px;
}

.apartment-item {
  padding: 15px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  transition: background-color 0.2s;
}

.apartment-item:hover {
  background-color: #f5f5f5;
}

.apartment-item.active {
  background-color: #e3f2fd;
}

.apt-name {
  font-weight: 500;
  color: #333;
  margin-bottom: 5px;
}

.apt-info {
  font-size: 0.9rem;
  color: #666;
  display: flex; /* Flexbox 활성화 */
  align-items: center; /* 세로 중앙 정렬 */
  justify-content: space-between; /* 좌우 끝 정렬 */
}

.build-year {
  display: inline-block;
  padding: 2px 6px;
  background: #f0f0f0;
  border-radius: 4px;
  font-size: 0.8rem;
}
.star{
  width: 20px;
  height: 20px;
  cursor: pointer;
  margin-right: 10px;
}
.modal-backdrop {
  position: absolute; /* 부모 요소 기준으로 위치 */
  top: 0;
  left: 0;
  width: 100%; /* 부모 요소의 전체 크기 */
  height: 100%; /* 부모 요소의 전체 크기 */
  background-color: rgba(0, 0, 0, 0.3); /* 어두운 배경 */
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
  width: 300px; /* 모달 너비 */
  height: 330px;
  max-width: 90%; /* 부모 요소 너비에 맞추기 */
  animation: fadeIn 0.3s ease-out;
}

/* 간단한 페이드 애니메이션 */
@keyframes fadeIn {
  from {
    opacity: 0;
    transform: scale(0.9);
  }
  to {
    opacity: 1;
    transform: scale(1);
  }
}
</style>