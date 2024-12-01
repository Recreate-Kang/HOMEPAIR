import { defineStore } from "pinia";
import { ref } from "vue";

export const useRegionStore = defineStore("region", () => {
  // Reactive state

  const selectedCode = ref(null);
  const selectedPath = ref("");
  const regionString = ref("");
  const isLoading = ref(false);
  const error = ref(null);

  // Actions

  const setCode = (code) => {
    selectedCode.value = code;
  };

  const setPath = (path) => {
    selectedPath.value = path;
  };

  const getSelectedPath = (dict) => {
    if (dict !== undefined && Object.keys(dict).length > 0) {
      console.log("Getting selected path:", dict);
      console.log("dict keys:", Object.keys(dict));
      // Safely extract the names of city, district, and dong
      const cityName = dict.city?.name || dict.city || "";
      const districtName = dict.district?.name || dict.district || "";
      const dongName = dict.dong?.name || dict.dong || "";
  
      // Combine them into a single string
      regionString.value = `${cityName} ${districtName} ${dongName}`;
      //console.log("Selected region string:", regionString.value);
    }
    return regionString;
  };

  const resetSelection = () => {
    regionString.value = "";
    selectedCode.value = null;
    selectedPath.value = "";
    error.value = null;
    isLoading.value = false;
  };

  // Expose the state and actions
  return {
    selectedCode,
    selectedPath,
    isLoading,
    error,
    regionString,
    setCode,
    setPath,
    getSelectedPath,
    resetSelection,
  };
});