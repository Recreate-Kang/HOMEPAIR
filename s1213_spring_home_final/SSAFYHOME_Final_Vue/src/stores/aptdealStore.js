import { defineStore } from "pinia";
import axios from "axios";
import { useApiStore } from "@/stores/apiStore";

export const useAptDealStore = defineStore("aptDealStore", {
  state: () => ({
    aptDeals: [], // 아파트 거래 데이터 리스트
  }),
  getters: {
    getAptDeals: (state) => state.aptDeals,
  },
  actions: {
    /**
     * 특정 aptSeq로 아파트 거래 데이터를 서버에서 가져옵니다.
     * @param {String} aptSeq - 아파트 고유 코드
     * @returns {Promise<void>}
     */
    async fetchAptDeals(aptSeq) {
      const apiStore = useApiStore();
      try {
        //console.log(`Fetching deals for aptSeq: ${aptSeq}`);
        const response = await axios.get(`${apiStore.baseURL}/apt/deals/${aptSeq}`);
        this.aptDeals = response.data;
        //console.log("Fetched Apt Deals:", this.aptDeals);
      } catch (error) {
        //console.error("Failed to fetch apartment deals:", error);
        throw error;
      }
    },

    /**
     * 상태에 저장된 아파트 거래 데이터를 초기화합니다.
     */
    clearAptDeals() {
      this.aptDeals = [];
      console.log("Apt Deals state has been cleared.");
    },
  },
});