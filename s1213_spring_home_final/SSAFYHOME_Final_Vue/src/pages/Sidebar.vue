<template>
  <div class="sidebar">
    <RegionSelector 
      @regionSelected="handleRegionSelection"
      @resetAll="resetAll"
    />
    <ApartmentList 
      v-if="isRegionSelected" 
      @showDetails="showDetails"
    />
  </div>
</template>

<script>
import { defineComponent, ref } from "vue";
import RegionSelector from "@/components/RegionSelector.vue";
import ApartmentList from "@/components/ApartmentList.vue";
import { useRegionStore } from "@/stores/regionStore";
import { useApartmentStore } from "@/stores/apartmentStore";
import { useStompStore } from "@/stores/stompStore";
import { useApiStore } from "@/stores/apiStore"; // Added import statement

export default defineComponent({
  name: "Sidebar",
  components: {
    RegionSelector,
    ApartmentList,
  },
  emits: ["updateMap", "showDetails", "closeSidebar"],
  setup(_, { emit }) {
    const regionStore = useRegionStore();
    const apartmentStore = useApartmentStore();
    const stompStore = useStompStore();
    const apiStore = useApiStore(); // Added apiStore variable
    const isRegionSelected = ref(false);

    // 지역 선택 후 처리
    const handleRegionSelection = async (code) => {
      try {
        // 1. 지역 코드 설정
        regionStore.setCode(code);

        // 2. 아파트 리스트 가져오기
        await apartmentStore.fetchApartmentsByRegion(code);
        const { apartments } = apartmentStore;

        if (apartments?.length > 0) {
          isRegionSelected.value = true;

          // 3. 부모에게 지도 좌표 업데이트 요청
          const coordinates = apartments[0]?.coordinates || { lat: 0, lng: 0 };
          emit("updateMap", coordinates);

          // 4. 채팅 구독은 구 선택 시에만 처리
          if (code.length === 5) { // 구 코드는 5자리
            await stompStore.subscribe(code);
          }
        } else {
          alert("선택한 지역에 아파트 데이터가 없습니다.");
          apartmentStore.clearApartments();
          isRegionSelected.value = false;
        }
      } catch (error) {
        console.error("Error in region selection:", error);
        alert("지역 데이터를 가져오는 데 실패했습니다.");
        apartmentStore.clearApartments();
        isRegionSelected.value = false;
      }
    };

    // 상세 정보 표시 요청
    const showDetails = (apartment) => {
      emit("showDetails", apartment);
    };

    // 모든 상태 초기화
    const resetAll = () => {
      regionStore.resetSelection();
      apartmentStore.clearApartments();
      apiStore.resetState(); // Added apiStore.resetState() call
      if (stompStore.currentSubscription) {
        stompStore.unsubscribe(stompStore.currentSubscription);
      }
      isRegionSelected.value = false;
    };

    return {
      isRegionSelected,
      handleRegionSelection,
      resetAll,
      showDetails,
    };
  },
});
</script>

<style scoped>
.sidebar {
  position: fixed;
  top: 80px;
  left: 20px;
  width: 380px;
  max-height: calc(100vh - 100px);
  background: white;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
  padding: 25px;
  border-radius: 12px;
  overflow-y: auto;
  z-index: 1000;
  color: #333;
}

.sidebar.show {
  transform: translateX(0);
}

.sidebar.hide {
  transform: translateX(-100%);
}

/* 기존 스타일 유지 */
.toggle-button {
  position: fixed;
  top: 20px;
  left: 20px;
  z-index: 1001;
  padding: 10px 15px;
  background-color: white;
  border: none;
  border-radius: 5px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  cursor: pointer;
  font-size: 1.2em;
  transition: all 0.3s ease;
}

.toggle-button:hover {
  background-color: #f0f0f0;
}
</style>