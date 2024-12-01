<template>
    <div>
      <!-- 지역 설정 버튼 -->
      <button
        v-if="!isCompleted && !selectedRegion.district"
        @click="toggleRegionSelection"
        class="region-button"
      >
        지역 설정
      </button>
      <button
        v-else-if="selectedRegion.district && !selectedRegion.dong && !isCompleted"
        @click="selectDongSkip(selectedRegion.district)"
        class="skip-button"
      >
        중간 선택
      </button>
      <!-- 초기화 버튼 -->
      <button
        v-else
        @click="reset"
        class="reset-button"
      >
        초기화
      </button>

      <!-- 지역 선택 UI -->
      <div v-if="isRegionSelectionOpen" class="region-selection">
        <transition-group name="slide-merge" mode="out-in" tag="div">
          <!-- 시/도 선택 -->
          <div v-if="!selectedRegion.city" key="city" class="selection-container">
            <h3>시/도 선택</h3>
            <div
              v-for="city in dummyData"
              :key="city.name"
              class="region-item"
              @click="selectCity(city)"
            >
              {{ city.name }}
            </div>
          </div>
  
          <!-- 군/구 선택 -->
          <div v-else-if="selectedRegion.city && !selectedRegion.district" key="district" class="selection-container">
            <h3>{{ selectedRegion.city.name }} - 군/구 선택</h3>
            <div
              v-for="district in selectedRegion.city.children"
              :key="district.name"
              class="region-item"
              @click="selectDistrict(district)"
            >
              {{ district.name }}
            </div>
          </div>
  
          <!-- 동 선택 -->
          <div v-else-if="selectedRegion.district && !selectedRegion.dong" key="dong" class="selection-container">
            <h3>{{ selectedRegion.district.name }} - 동 선택</h3>
            <div
              v-for="dong in selectedRegion.district.children"
              :key="dong.name"
              class="region-item"
              @click="selectDong(dong)"
            >
              {{ dong.name }}
            </div>
          </div>
        </transition-group>
      </div>
    </div>
  </template>
  
  <script>
  import { ref, watch } from "vue";
  import { useApiStore } from "@/stores/apiStore";
  import { useRegionStore } from "@/stores/regionStore";
  import { useStompStore } from "@/stores/stompStore";

  export default {
    name: "RegionSelector",
    emits: ["regionSelected", "resetAll"],
    setup(_, { emit }) {
      const apiStore = useApiStore();
      const regionStore = useRegionStore();
      const stompStore = useStompStore();
      const isRegionSelectionOpen = ref(false);
      const isCompleted = ref(false); // 지역 설정 완료 상태
      const dummyData = ref([]);
      const selectedRegion = ref({
        city: null,
        district: null,
        dong: null,
      });
  
      const toggleRegionSelection = async () => {
        isRegionSelectionOpen.value = !isRegionSelectionOpen.value;
        if (isRegionSelectionOpen.value && dummyData.value.length === 0) {
          try {
            await apiStore.fetchDongCodesIfEmpty();
            dummyData.value = apiStore.dongCodes;
          } catch (error) {
            console.error("Error fetching dummy data:", error);
          }
        }
      };
  
      const selectCity = (city) => {
        selectedRegion.value.city = city;
        selectedRegion.value.district = null;
        selectedRegion.value.dong = null;
      };
  
      const selectDistrict = async (district) => {
        selectedRegion.value.district = district;
        selectedRegion.value.dong = null;
        console.log(selectedRegion.value.city.code, "city code");
        console.log(selectedRegion.value.district.code, "district code");

        // 구군 코드로 웹소켓 구독
        try {
          const regionName = `${selectedRegion.value.city.name} ${district.name}`;
          await stompStore.subscribe(district.code, regionName);
          console.log("Subscribed to district:", district.name);
        } catch (error) {
          console.error("Failed to subscribe to district:", error);
        }
      };
      const selectDongSkip = (district) => {
        if (!district || !district.code) {
          console.error("Invalid district selected:", district);
          return;
        }

        console.log("Selected district:", district);
        selectedRegion.value.district = district;
        isCompleted.value = true; // 지역 선택 완료
        isRegionSelectionOpen.value = false;
        regionStore.getSelectedPath(selectedRegion.value);
        emit("regionSelected", district.code);

      };
      const selectDong = (dong) => {
        if (!dong || !dong.code) return;
        selectedRegion.value.dong = dong;
        isCompleted.value = true; // 지역 선택 완료
        isRegionSelectionOpen.value = false;
        regionStore.getSelectedPath(selectedRegion.value);
        emit("regionSelected", dong.code);
      };
  
      const reset = () => {
        // 기존 구독 해제
        if (selectedRegion.value.district) {
          stompStore.unsubscribe(selectedRegion.value.district.code);
        }
        
        selectedRegion.value = { city: null, district: null, dong: null };
        isCompleted.value = false;
        isRegionSelectionOpen.value = false;
        emit("resetAll"); // 부모 컴포넌트에 초기화 이벤트 전달
      };
  
      return {
        isRegionSelectionOpen,
        isCompleted,
        dummyData,
        selectedRegion,
        toggleRegionSelection,
        selectCity,
        selectDistrict,
        selectDong,
        selectDongSkip,
        reset,
      };
    },
  };
  </script>
  
  <style scoped>
  .region-button {
    display: block;
    width: 100%;
    margin-bottom: 15px;
    padding: 10px;
    background-color: #4caf50;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
    font-weight: bold;
    text-align: center;
  }
  
  .reset-button {
    display: block;
    width: 100%;
    margin-bottom: 15px;
    padding: 10px;
    background-color: #f44336;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
    font-weight: bold;
    text-align: center;
  }
  .skip-button {
    display: block;
    width: 100%;
    margin-bottom: 15px;
    padding: 10px;
    background-color: #f4b136;
    color: white;
    border: none;
    cursor: pointer;
    border-radius: 5px;
    font-weight: bold;
    text-align: center;
  }
  .region-selection {
    padding: 10px;
  }
  
  .region-item {
    padding: 8px 12px;
    background-color: #fff;
    border: 1px solid #ddd;
    margin-bottom: 8px;
    cursor: pointer;
    border-radius: 4px;
    transition: background-color 0.3s ease;
  }
  
  .region-item:hover {
    background-color: #f1f1f1;
  }
  </style>