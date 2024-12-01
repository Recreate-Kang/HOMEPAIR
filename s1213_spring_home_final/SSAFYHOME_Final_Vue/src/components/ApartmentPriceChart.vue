<template>
  <div class="chart-container">
    <h2>{{ apartment.aptName }} 평당가 추이</h2>
    <canvas ref="chartCanvas"></canvas>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from "vue";
import { useAptDealStore } from "@/stores/aptdealStore";
import { Chart } from 'chart.js/auto';

const props = defineProps({
  apartment: Object,
});

const chartCanvas = ref(null);
const aptDealStore = useAptDealStore();
let chart = null;

const createChart = (deals) => {
  if (chart) {
    chart.destroy();
  }

  // 날짜별로 정렬
  const sortedDeals = [...deals].sort((a, b) => new Date(a.dealDate) - new Date(b.dealDate));
  
  // 연도 추출
  const years = [...new Set(sortedDeals.map(deal => deal.dealDate.split('-')[0]))];
  const currentYear = years[0]; // 첫 번째 연도

  // 날짜 포맷팅 함수
  const formatDate = (dateStr) => {
    const [year, month, day] = dateStr.split('-');
    return month + '/' + day;
  };

  const data = {
    labels: sortedDeals.map(deal => formatDate(deal.dealDate)),
    datasets: [{
      label: `평당가 (만원) - ${currentYear}년`,
      data: sortedDeals.map(deal => deal.pricePerArea),
      borderColor: '#009688',
      backgroundColor: 'rgba(0, 150, 136, 0.1)',
      borderWidth: 2,
      fill: true,
      tension: 0.4,
      pointRadius: 4,
      pointHoverRadius: 6,
      pointBackgroundColor: '#009688',
    }]
  };

  const config = {
    type: 'line',
    data: data,
    options: {
      responsive: true,
      maintainAspectRatio: false,
      plugins: {
        legend: {
          position: 'top',
          labels: {
            padding: 10,
            generateLabels: function(chart) {
              const labels = Chart.defaults.plugins.legend.labels.generateLabels(chart);
              if (labels.length > 0) {
                labels[0].text = `평당가 (만원) - ${currentYear}년`;
              }
              return labels;
            }
          }
        },
        tooltip: {
          callbacks: {
            title: function(context) {
              const idx = context[0].dataIndex;
              return `${currentYear}-${sortedDeals[idx].dealDate.split('-').slice(1).join('-')}`;
            },
            label: function(context) {
              return `평당가: ${context.parsed.y.toFixed(2)}만원`;
            }
          }
        }
      },
      scales: {
        x: {
          title: {
            display: true,
            text: '거래 날짜',
            padding: {
              top: 10
            }
          },
          ticks: {
            maxRotation: 45,
            minRotation: 45
          }
        },
        y: {
          title: {
            display: true,
            text: '평당가 (만원)',
            padding: {
              bottom: 10
            }
          },
          beginAtZero: false
        }
      },
      layout: {
        padding: {
          left: 10,
          right: 20,
          top: 20,
          bottom: 10
        }
      }
    }
  };

  chart = new Chart(chartCanvas.value, config);
};

onMounted(async () => {
  try {
    await aptDealStore.fetchAptDeals(props.apartment.aptSeq);
    createChart(aptDealStore.aptDeals);
  } catch (error) {
    console.error("거래 정보를 불러오는 중 오류가 발생했습니다.", error);
  }
});

// apartment prop이 변경될 때마다 차트 업데이트
watch(() => props.apartment, async (newApartment) => {
  if (newApartment) {
    try {
      await aptDealStore.fetchAptDeals(newApartment.aptSeq);
      createChart(aptDealStore.aptDeals);
    } catch (error) {
      console.error("거래 정보를 불러오는 중 오류가 발생했습니다.", error);
    }
  }
}, { deep: true });
</script>

<style scoped>
.chart-container {
  background-color: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  margin: 15px 0;
  height: 450px;
  width: 100%;
  position: relative;
}

h2 {
  margin: 0 0 20px 0;
  font-size: 1.2rem;
  color: #333;
  text-align: center;
}

canvas {
  width: 100% !important;
  height: calc(100% - 40px) !important;
}
</style>
