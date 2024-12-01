import { defineStore } from 'pinia';
import axios from 'axios';
import { useAuthStore } from './authStore';

// 디바운스 함수 추가
function debounce(func, wait) {
  let timeout;
  return function executedFunction(...args) {
    const later = () => {
      clearTimeout(timeout);
      func(...args);
    };
    clearTimeout(timeout);
    timeout = setTimeout(later, wait);
  };
}

export const useInfraStore = defineStore('infraStore', {
  state: () => ({
    hospitals: [],
    libraries: [],
    marts: [],
    parks: [],
    schools: [],
    subways: [],
    isLoading: false,
    error: null,
    baseURL: 'http://localhost:8080/HOME',
    lastUpdate: null,
    aptSelected: null
  }),

  actions: {
    setAptSelected(apt) {
      this.aptSelected = apt;
      if (apt) {
        this.getStoreListInRange(apt.latitude, apt.longitude);
      } else {
        this.clearInfraData();
      }
    },

    clearInfraData() {
      this.hospitals = [];
      this.libraries = [];
      this.marts = [];
      this.parks = [];
      this.schools = [];
      this.subways = [];
    },

    async getStoreListInRange(latitude, longitude) {
      try {
        this.isLoading = true;
        const response = await axios.post(`/infra/list`, {
          latitude: String(latitude),
          longitude: String(longitude)
        }, {
          baseURL: this.baseURL,
          headers: {
            'Content-Type': 'application/json'
          }
        });

        if (response.data) {
          // 각 카테고리별 데이터 저장
          this.hospitals = response.data.hospitalList || [];
          this.libraries = response.data.libraryList || [];
          this.marts = response.data.martList || [];
          this.parks = response.data.parkList || [];
          this.schools = response.data.schoolList || [];
          this.subways = response.data.subwayList || [];
          
          this.lastUpdate = Date.now();
          return response.data;
        }
        return null;
      } catch (error) {
        console.error('Failed to fetch stores in range:', error);
        this.error = error.message;
        return null;
      } finally {
        this.isLoading = false;
      }
    }
  }
});
