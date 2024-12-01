import { defineStore } from "pinia";
import { useApiStore } from "@/stores/apiStore";
import axios from "axios";
export const useApartmentStore = defineStore("apartment", {
  state: () => ({
    apartments: [], // 아파트 리스트
    selectedApt: null, // 선택된 아파트
    isLoading: false, // 로딩 상태
    error: null, // 에러 메시지
    legacyApt: null,
    isDuplicate: false,
    showDetails: false, // Add state for details visibility
  }),
  getters: {
    // computed와 동일한 방식으로 구현
    duplicate: (state) => {
      return (
        state.selectedApt?.aptSeq === state.legacyApt?.aptSeq
      );
    },
  },
  actions: {
    /**
     * Region Code를 기반으로 아파트 리스트를 검색
     * @param {string} code - 지역 코드
     */
    async fetchApartmentsByRegion(code) {
      //console.log("Fetching apartments by region:", code);
      if (!code) {
        throw new Error("Region code is required");
      }
      //this.isLoading = true;
      //this.error = null;
      this.clearApartments();
      const apiStore = useApiStore(); // apiStore 가져오기

      try {
        // apiStore의 searchDongCode 호출
        const response = await apiStore.searchDongCode(code);

        if (response?.aptlist?.length) {
          //console.log("Found apartments:", response.aptlist); 
          this.apartments = response.aptlist; // 검색 결과 저장
        } else {
          this.apartments = [];
          this.error = "No apartments found for the given region.";
        }
      } catch (error) {
        this.error = error.message || "Failed to fetch apartments";
        console.error("Error fetching apartments:", error);
        this.apartments = [];
      } finally {
        this.isLoading = false;
      }
    },
    async fetchApartmentByAptCode(aptSeq) {
      if (!aptSeq) {
        throw new Error("AptSeq is required");
      }

      this.isLoading = true;
      this.error = null;

      try {
        // axios를 사용하여 POST 요청
        //console.log("TRYING TO FETCH single APT");
        const response = await axios.post("http://localhost:8080/HOME/apt/select", {
          aptSeq: aptSeq,
        }, {
          headers: {
            "Content-Type": "application/json",
          },
        });

        if (response && response.data) {
          // 성공적으로 데이터를 가져오면 selectedApt에 저장
          this.selectedApt = response.data;
          //console.log("Fetched apartment data:", this.selectedApt);
        } else {
          // 데이터가 없을 경우 처리
          this.selectedApt = null;
          this.error = "No data found for the given aptSeq.";
          console.warn(this.error);
        }
      } catch (error) {
        // 에러 처리
        this.error = error.message || "Failed to fetch apartment data.";
        console.error("Error fetching apartment by aptSeq:", error);
        this.selectedApt = null;
      } finally {
        this.isLoading = false;
      }
    },

    /**
     * 아파트 리스트 초기화
     */
    clearApartments() {
      this.apartments = [];
      this.selectedApt = null;
      this.isLoading = false;
      this.error = null;
      this.legacyApt = null;
      this.isDuplicate = false;
      this.showDetails = false; // Reset showDetails when clearing
    },

    /**
     * 선택된 아파트 설정
     * @param {Object} apt - 선택된 아파트 데이터
     */
    selectApartment(apt) {
      //console.log("Select: " + apt);
  
      this.selectedApt = apt;
      this.showDetails = apt !== null; // Show details when apartment is selected
    },
    checkDuplicateApt() {
      if (this.isDuplicate){
        this.isDuplicate = false;
        //this.duplicate = false; // duplicate를 false로 ���기화
      }
      else {
        this.isDuplicate = this.duplicate; // duplicate를 기반으로 중복 상태 업데이트  
      }

      console.log("checkDuplicateApt() called: " + this.isDuplicate);
    },

    storeLegacyApt() {
      console.log("Storing legacy apt:");
      this.legacyApt = { ...this.selectedApt }; // `selectedApt` 복사

    },
  }
});
