import { defineStore } from "pinia";
import axios from "axios";
import { useAptDealStore } from "@/stores/aptdealStore";
import { useRegionStore } from "@/stores/regionStore";
import { useApartmentStore } from "@/stores/apartmentStore";
import { useApiStore } from "@/stores/apiStore";

export const useUserFavoriteStore = defineStore("userFavoriteStore", {
  state: () => ({
    userFavorites: [],
    isInitialized: false,
    isLoading: false,
    error: null,
    processedFavorites: [], // 계산된 Favorite 데이터를 저장하는 상태
  }),
  getters: {
    getUserFavorites: (state) => state.userFavorites,
    getProcessedFavorites: (state) => state.processedFavorites, // 처리된 데이터를 가져오는 Getter
    getIsInitialized: (state) => state.isInitialized,
  },
  actions: {
    /**
     * "HOME" 즐겨찾기 데이터 처리
     * @returns {Promise<void>}
     */
    async processHomeFavorites(userNum) {
      try {
        console.log("USER NUM:", userNum);
        const aptDealStore = useAptDealStore();
        const regionStore = useRegionStore();
        const apartmentStore = useApartmentStore();

        this.userData = await this.fetchUserFavorites(userNum);
        // HOME에 해당하는 객체 찾기
        let homeFavorite = null;
        //console.log("Looking for HOME favorite...", this.userData.userFavorites);
        //console.log("Looking for HOME favorite...", this.userData.userFavorites.length);
        for (const fav of this.userData.userFavorites || []) {
          //console.log("Looking for HOME favorite:", fav.myPlaceName);
          if (fav.myPlaceName === "HOME") {
            homeFavorite = fav;
            break; // HOME은 하나뿐이므로 찾으면 즉시 종료
          }
        }
    
        // HOME 데이터가 없는 경우 처리
        if (!homeFavorite) {
          console.warn("No HOME favorite found.");
          return;
        }
    
        console.log("Processing HOME favorite:", homeFavorite);
    
        // aptDealStore에서 거래 데이터 가져오기fetchApartmentByAptCode
        await aptDealStore.fetchAptDeals(homeFavorite.aptSeq);
        const aptDeals = aptDealStore.getAptDeals;
        console.log("Apt Deals:", aptDeals);
        await apartmentStore.fetchApartmentByAptCode(homeFavorite.aptSeq);
        //console.log("Apartment Info:", apartmentStore.selectedApt);
        const aptInfo = apartmentStore.selectedApt;
        //homeFavorite.aptName = aptInfo.aptName;
        //homeFavorite.address = aptInfo.dongCode;
        //homeFavorite.buildYear = aptInfo.buildYear;
        homeFavorite.selectedHomeAPT = aptInfo;
        console.log(homeFavorite.selectedHomeAPT.aptName + " APTNAME");
        if (aptDeals.length > 0) {
          // 필요한 데이터 처리 (예: 가격 변동, 가용 면적 계산 등)
          const prices = aptDeals.map((deal) => deal.dealAmount);
          const priceChange = Math.max(...prices) - Math.min(...prices);
          const dates = aptDeals.map((deal) => deal.dealDate);
          const recentDeal = dates[0];
          homeFavorite.recentDeal = recentDeal;
          const recentPrice = prices[0];
          const oldestPrice = prices[prices.length - 1];
          const PriceDelta = recentPrice - oldestPrice;
          // aptDealStore에서 
          const avgPrice = prices.reduce((sum, price) => sum + price, 0) / prices.length;
          const areas = aptDeals.map((deal) => deal.excluUseAr);
          const avgArea = areas.reduce((sum, area) => sum + area, 0) / areas.length;
          
          console.log(`Price Change: ${priceChange}, Avg Area: ${avgArea}`);
          // HOME favorite 업데이트
          homeFavorite.priceChange = priceChange;
          homeFavorite.avgPrice = avgPrice;
          homeFavorite.avgArea = avgArea;
          homeFavorite.priceDelta = PriceDelta;
          homeFavorite.recentPrice = recentPrice;
          console.log("Updated HOME favorite:", homeFavorite);
          this.processedFavorites = homeFavorite;
          // 기타 데이터 처리
        } else {
          console.warn(`No deals found for aptSeq: ${homeFavorite.aptSeq}`);
        }
      } catch (error) {
        console.error("Error processing home favorites:", error);
      }
    },
    // 사용자 즐겨찾기 목록 가져오기
    async fetchUserFavorites(userNum) {
      //console.log("Fetching user favorites for userNum:", userNum);
      const apiStore = useApiStore();
      this.isLoading = true;
      this.error = null;
      try {
        const response = await axios.post(
          `${apiStore.baseURL}/user/favorite`,
          {
            userId: parseInt(userNum),
          },
          {
            headers: apiStore.getAuthHeaders(),
          }
        );
        this.userFavorites = response.data;
        return response.data;
      } catch (error) {
        console.error("Failed to fetch user favorites:", error);
        this.error = error;
        return [];
      } finally {
        this.isLoading = false;
      }
    },
  },
});