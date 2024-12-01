import { defineStore } from "pinia";
import axios from "axios";
import { useApiStore } from "@/stores/apiStore";

export const useUserDetailStore = defineStore("userDetailStore", {
  state: () => ({
    userDetails: {
      userId: 0,
      age: null,
      gender: '',
      occupation: '',
      hobbies: '',
      maritalStatus: '',
      currentArea: null,
      hasPets: null,
      bio: '',
      updatedAt: null
    },
  }),
  getters: {
    getUserDetail: (state) => state.userDetails,
    hasUserDetail: (state) => Object.keys(state.userDetails).length > 0 && state.userDetails.age !== null,
  },
  actions: {
    async fetchUserDetail(userNum) {
      const apiStore = useApiStore();
      try {
        const response = await axios.post(`${apiStore.baseURL}/user/detail`, { 
          userId: userNum  
        });
        
        // 기본값 설정과 함께 데이터 변환
        this.userDetails = {
          userId: response.data.userId || 0,
          age: response.data.age || null,
          gender: response.data.gender || 'other',
          occupation: response.data.occupation || '',
          hobbies: response.data.hobbies || '',
          maritalStatus: response.data.maritalStatus || 'SINGLE',
          currentArea: response.data.currentArea || 0,
          hasPets: response.data.hasPets || '없음',
          bio: response.data.bio || '',
          updatedAt: response.data.updatedAt || null
        };
        return this.userDetails;
      } catch (error) {
        console.error("사용자 정보 가져오기 실패:", error);
        return null;
      }
    },
    async createUserDetail(userDetailsDTO) {
      const apiStore = useApiStore();
      try {
        // 데이터 형식 변환
        const formattedData = {
          userId: userDetailsDTO.userId,
          age: parseInt(userDetailsDTO.age) || null,
          gender: userDetailsDTO.gender || 'other',
          occupation: userDetailsDTO.occupation || '',
          hobbies: userDetailsDTO.hobbies || '',
          maritalStatus: userDetailsDTO.marital_status || 'SINGLE',
          currentArea: parseInt(userDetailsDTO.current_area) || 0,
          hasPets: userDetailsDTO.pets || '없음',  
          bio: userDetailsDTO.bio || ''
        };

        const response = await axios.patch(`${apiStore.baseURL}/user/detail`, formattedData);
        // 프로필 생성 후 최신 데이터 다시 가져오기
        await this.fetchUserDetail(userDetailsDTO.userId);
        console.log("프로필 생성 성공");
        return response.data;
      } catch (error) {
        console.error("프로필 생성 실패:", error);
        throw error;
      }
    },
    async updateUserDetail(userDetailsDTO) {
      const apiStore = useApiStore();
      try {
        // 데이터 형식 변환
        const formattedData = {
          userId: userDetailsDTO.userId,
          age: parseInt(userDetailsDTO.age) || null,
          gender: userDetailsDTO.gender || 'other',
          occupation: userDetailsDTO.occupation || '',
          hobbies: userDetailsDTO.hobbies || '',
          maritalStatus: userDetailsDTO.marital_status || 'SINGLE',
          currentArea: parseInt(userDetailsDTO.current_area) || 0,
          hasPets: userDetailsDTO.pets || '없음',  
          bio: userDetailsDTO.bio || ''
        };

        const response = await axios.patch(`${apiStore.baseURL}/user/detail`, formattedData);
        // 프로필 수정 후 최신 데이터 다시 가져오기
        await this.fetchUserDetail(userDetailsDTO.userId);
        console.log("프로필 수정 성공");
      } catch (error) {
        console.error("프로필 수정 실패:", error);
        throw error;
      }
    },
  },
});