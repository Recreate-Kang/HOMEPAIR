<template>
  <div class="evaluation-container">
    <button 
      v-if="!loading && !totalEvaluationStore.evaluation" 
      class="evaluate-button" 
      @click="handleEvaluation"
    >
      <i class="fas fa-search"></i>
      이 집은 어떤지 알아보기
    </button>

    <div v-if="loading" class="loading-container">
      <div class="loading-spinner">
        <div class="spinner"></div>
      </div>
      <div class="loading-text">AI가 분석중입니다...</div>
    </div>

    <div v-if="totalEvaluationStore.evaluation" class="evaluation-results" @click="handleClearEvaluation">
      <div class="result-header">
        <h3>AI 평가 결과</h3>
        <div class="overall-rating" :class="{
          '매우추천': totalEvaluationStore.evaluation.총평 === '매우추천', 
          '추천': totalEvaluationStore.evaluation.총평 === '추천', 
          '평범': totalEvaluationStore.evaluation.총평 === '평범', 
          '조금아쉬움': totalEvaluationStore.evaluation.총평 === '조금아쉬움', 
          '다른곳을찾아봐요': totalEvaluationStore.evaluation.총평 === '다른곳을찾아봐요' 
        }">
          {{ totalEvaluationStore.evaluation.총평 }}
        </div>
      </div>

      <div class="result-section factors">
        <h4>🎯 고려한 요소</h4>
        <div class="tags">
          <span v-for="(factor, index) in totalEvaluationStore.evaluation['고려한 요소']" 
                :key="index" 
                class="tag">
            {{ factor }}
          </span>
        </div>
      </div>

      <div class="result-section positives">
        <h4>👍 긍정적 요소</h4>
        <div class="evaluation-grid">
          <div v-for="(item, index) in totalEvaluationStore.evaluation['긍정적 요소']" 
               :key="index" 
               class="evaluation-card positive">
            <div class="card-content">
              <h5>{{ item.항목 }}</h5>
              <p>{{ item.설명 }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="result-section negatives">
        <h4>👎 부정적 요소</h4>
        <div class="evaluation-grid">
          <div v-for="(item, index) in totalEvaluationStore.evaluation['부정적 요소']" 
               :key="index" 
               class="evaluation-card negative">
            <div class="card-content">
              <h5>{{ item.항목 }}</h5>
              <p>{{ item.설명 }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="result-section others">
        <h4>💡 기타 고려사항</h4>
        <div class="evaluation-grid">
          <div v-for="(item, index) in totalEvaluationStore.evaluation['기타 요소']" 
               :key="index" 
               class="evaluation-card neutral">
            <div class="card-content">
              <h5>{{ item.항목 }}</h5>
              <p>{{ item.설명 }}</p>
            </div>
          </div>
        </div>
      </div>

      <div class="result-section alternatives">
        <h4>🏠 대체 추천 매물</h4>
        <div class="evaluation-grid">
          <div v-for="(item, index) in totalEvaluationStore.evaluation['대체 추천 매물']" 
               :key="index" 
               class="evaluation-card alternative">
            <div class="card-content">
              <div class="alternative-header">
                <h5>{{ item.매물명 }}</h5>
                <span class="location">{{ item.지역 }}</span>
              </div>
              <p>{{ item.설명 }}</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useTotalEvaluationStore } from '@/stores/totalEvaluationStore'
import { useApartmentStore } from '@/stores/apartmentStore'
import { useInfraStore } from '@/stores/infraStore'
import { useUserDetailStore } from '@/stores/userDetailStore'
import { useUserFavoriteStore } from '@/stores/userFavoriteStore'
import { useAptDealStore } from '@/stores/aptdealStore'

const loading = ref(false)
const totalEvaluationStore = useTotalEvaluationStore()
const apartmentStore = useApartmentStore()
const infraStore = useInfraStore()
const userDetailStore = useUserDetailStore()
const userFavoriteStore = useUserFavoriteStore()
const aptDealStore = useAptDealStore()

const handleEvaluation = async () => {
  loading.value = true
  try {
    // Set apartment info
    const aptInfo = {
      aptSeq: apartmentStore.selectedApt.aptSeq,
      dongCode: apartmentStore.selectedApt.dongCode,
      aptName: apartmentStore.selectedApt.aptName,
      buildYear: apartmentStore.selectedApt.buildYear,
      latitude: apartmentStore.selectedApt.latitude,
      longitude: apartmentStore.selectedApt.longitude
    }
    totalEvaluationStore.setAptInfo(aptInfo)
    
    // Set infra info with complete data
    const infraList = {
      hospitalList: infraStore.hospitals,
      schoolList: infraStore.schools,
      parkList: infraStore.parks,
      martList: infraStore.marts,
      libraryList: infraStore.libraries,
      subwayList: infraStore.subways
    }
    totalEvaluationStore.setInfraList(infraList)

    // Set user details from userDetailStore
    const userDetails = userDetailStore.userDetails
    totalEvaluationStore.setUserDetails(userDetails)

    // Set user favorites from userFavoriteStore's getter
    const userFavorites = {
      favoriteId: 0,
      userId: userDetailStore.userDetails.userId,
      aptSeq: apartmentStore.selectedApt.aptSeq,
      myPlaceName: apartmentStore.selectedApt.aptName
    }
    
    // If the apartment exists in favorites, use that data instead
    const existingFavorite = userFavoriteStore.userFavorites.find(
      fav => fav.aptSeq === apartmentStore.selectedApt.aptSeq
    )
    if (existingFavorite) {
      userFavorites.favoriteId = existingFavorite.favoriteId
      userFavorites.userId = existingFavorite.userId
      userFavorites.aptSeq = existingFavorite.aptSeq
      userFavorites.myPlaceName = existingFavorite.myPlaceName
    }
    
    totalEvaluationStore.setUserFavorites(userFavorites)

    // Set apt deal from aptDealStore
    const recentDeal = aptDealStore.aptDeals[0] || {
      no: 0,
      aptSeq: apartmentStore.selectedApt.aptSeq,
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
    totalEvaluationStore.setAptDeal(recentDeal)

    // Request evaluation
    await totalEvaluationStore.requestGptEvaluation()
  } catch (error) {
    console.error('평가 요청 중 오류 발생:', error)
  } finally {
    loading.value = false
  }
}

const handleClearEvaluation = () => {
  totalEvaluationStore.clearEvaluation()
}
</script>

<style scoped>
.evaluation-container {
  margin: 15px 0;
  padding: 0 15px;
  max-width: 800px;
  margin: 0 auto;
}

.evaluate-button {
  width: 100%;
  padding: 12px 20px;
  background: #009688;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s ease;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.evaluate-button:hover {
  background: #009688;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
}

.evaluate-button i {
  font-size: 1.1rem;
}

.evaluation-results {
  background-color: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  font-size: 0.9rem;
  letter-spacing: -0.03em;
  line-height: 1.3;
  max-width: 720px;
  margin: 0 auto;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
  
  h3 {
    font-size: 1.1rem;
    margin: 0;
  }
}

.result-section {
  margin-bottom: 12px;
  padding: 8px;
  background: #fafafa;
  border-radius: 8px;
  
  h4 {
    font-size: 0.9rem;
    margin: 0 0 6px 0;
    color: #555;
  }
}

.evaluation-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 8px;
}

.evaluation-card {
  padding: 8px;
  border-radius: 6px;
  background: white;
  border: 1px solid #eee;
  
  h5 {
    font-size: 0.85rem;
    margin: 0 0 2px 0;
  }
  
  p {
    font-size: 0.8rem;
    margin: 0;
    color: #666;
    line-height: 1.2;
  }
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  
  .tag {
    font-size: 0.75rem;
    padding: 3px 6px;
    background: white;
    border: 1px solid #eee;
    border-radius: 4px;
    color: #666;
  }
}

.alternative-header {
  h5 {
    font-size: 0.85rem;
  }
  
  .location {
    font-size: 0.7rem;
    color: #888;
    background: #f0f0f0;
    padding: 2px 4px;
    border-radius: 3px;
  }
}

.overall-rating {
  font-size: 0.85rem;
  padding: 4px 12px;
  border-radius: 16px;
  font-weight: 600;
  
  &.매우추천 {
    background-color: #2563eb;
    color: white;
    box-shadow: 0 2px 4px rgba(37, 99, 235, 0.2);
  }
  
  &.추천 {
    background-color: #10b981;
    color: white;
    box-shadow: 0 2px 4px rgba(16, 185, 129, 0.2);
  }
  
  &.평범 {
    background-color: #f59e0b;
    color: white;
    box-shadow: 0 2px 4px rgba(245, 158, 11, 0.2);
  }
  
  &.조금아쉬움 {
    background-color: #ef4444;
    color: white;
    box-shadow: 0 2px 4px rgba(239, 68, 68, 0.2);
  }
  
  &.다른곳을찾아봐요 {
    background-color: #dc2626;
    color: white;
    box-shadow: 0 2px 4px rgba(220, 38, 38, 0.2);
  }
}

.loading-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 20px;
  gap: 15px;
}

.loading-spinner {
  display: flex;
  justify-content: center;
  align-items: center;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f3f3f3;
  border-top: 4px solid #00897b;
  border-radius: 50%;
  animation: spin 1s linear infinite;
}

.loading-text {
  color: #00897b;
  font-size: 0.9rem;
  font-weight: 500;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

@media (max-width: 640px) {
  .evaluation-container {
    padding: 0 10px;
  }
  
  .evaluation-grid {
    grid-template-columns: 1fr;
  }
  
  .evaluation-results {
    padding: 12px;
  }
}
</style>
