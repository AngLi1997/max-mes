import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useLiquidMeasureStore = defineStore('liquidMeasure', () => {
  // 选中的配液单
  const selectedLiquidMeasureSheet = ref(null);
  // 选中的物料批次
  const selectedMaterialBatch = ref(null);
  // 设置选中的配液单
  const setSelectedLiquidMeasureSheet = (sheet) => {
    selectedLiquidMeasureSheet.value = sheet;
  };
  // 设置选中的物料批次
  const setSelectedMaterialBatch = (batch) => {
    selectedMaterialBatch.value = batch;
  };

  return {
    selectedLiquidMeasureSheet,
    selectedMaterialBatch,
    setSelectedLiquidMeasureSheet,
    setSelectedMaterialBatch
  };
});
