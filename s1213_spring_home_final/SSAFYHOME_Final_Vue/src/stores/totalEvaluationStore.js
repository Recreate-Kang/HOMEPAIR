import { defineStore } from 'pinia'
import { useApiStore } from './apiStore'
import axios from 'axios'

export const useTotalEvaluationStore = defineStore('totalEvaluation', {
  state: () => ({
    evaluation: null,
    userDetails: {
      userId: 0,
      age: null,
      gender: '',
      occupation: '',
      hobbies: '',
      maritalStatus: '',
      currentArea: null,
      hasPets: '',
      bio: '',
      updatedAt: null
    },
    userFavorites: {
      favoriteId: 0,
      userId: 0,
      aptSeq: '',
      myPlaceName: ''
    },
    infraList: {
      hospitalList: [],
      libraryList: [],
      martList: [],
      parkList: [],
      schoolList: [],
      subwayList: []
    },
    aptInfo: {
      aptSeq: '',
      dongCode: '',
      aptName: '',
      buildYear: '',
      latitude: '',
      longitude: ''
    },
    aptDeal: {
      no: 0,
      aptSeq: '',
      aptDong: '',
      floor: 0,
      dealYear: 0,
      dealMonth: 0,
      dealDay: 0,
      excluUseAr: 0,
      dealAmount: 0,
      pricePerArea: 0,
      dealDate: null
    }
  }),

  actions: {
    // Action to prepare data for sending to backend
    prepareTotalEvaluationRequest() {
      return {
        userDetailsDTOList: [this.userDetails],
        userFavoritesDTOList: [this.userFavorites],
        infraListResponse: this.infraList,
        aptInfoDTO: this.aptInfo,
        aptDeal: this.aptDeal
      }
    },

    // Action to update user details
    setUserDetails(details) {
      this.userDetails = { ...this.userDetails, ...details }
    },

    // Action to update user favorites
    setUserFavorites(favorites) {
      this.userFavorites = { ...this.userFavorites, ...favorites }
    },

    // Action to update infra list
    setInfraList(infra) {
      this.infraList = { ...this.infraList, ...infra }
    },

    // Action to update apt info
    setAptInfo(info) {
      this.aptInfo = { ...this.aptInfo, ...info }
    },

    // Action to update apt deal
    setAptDeal(deal) {
      this.aptDeal = { ...this.aptDeal, ...deal }
    },

    // Action to request GPT evaluation
    async requestGptEvaluation() {
      const apiStore = useApiStore()
      const requestData = this.prepareTotalEvaluationRequest()
      
      try {
        const response = await axios.post(
          `${apiStore.baseURL}/GPT/evaluation`,
          requestData,
          {
            headers: {
              ...apiStore.getAuthHeaders(),
              'Content-Type': 'application/json'
            }
          }
        )
        this.evaluation = response.data
        return response.data
      } catch (error) {
        console.error('Error requesting GPT evaluation:', error)
        throw error
      }
    },

    // Action to reset all data
    resetAll() {
      this.$reset()
    },
    
    // Action to clear evaluation
    clearEvaluation() {
      this.evaluation = null
    }
  },

  getters: {
    // Getter to check if all required data is present
    isDataComplete: (state) => {
      return state.userDetails.userId !== 0 &&
             state.aptInfo.aptSeq !== '' &&
             state.aptDeal.aptSeq !== ''
    }
  }
})
