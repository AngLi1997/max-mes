import { t } from '@/utils/useBmosI18n.js';
import { ref, onMounted } from 'vue';

import { useNotify } from 'wot-design-uni';
import { storeToRefs } from 'pinia';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { usePermissionStore } from '@/stores/permission.js';

export const useModeDevice = ({ props }) => {
  const { hasPermission } = usePermissionStore();

  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const { setSelectedBalance } = weighingMachineStore;
  const { showNotify } = useNotify();
  const mode = ref(false);
  const deviceList = ref([]);

  const hasAutoPermission = ref(false);
  const hasManualPermission = ref(hasPermission('121010001002020'));

  const handleNextStep = async() => {
    if (!hasAutoPermission.value && !hasManualPermission.value) {
      showNotify({
        message: t('暂无称量权限'),
        type: 'danger'
      });
      return;
    }
    if (mode.value) {
      if (!selectedBalance.value.balanceId) {
        uni.showToast({
          title: t('请选择秤具'),
          icon: 'none'
        });
        return;
      }
    }
    uni.navigateTo({
      url: `/pages/businessComponents/liquidMeasure/measure/index?id=${
        props.id
      }&measureBatchId=${props.measureBatchId}&mode=${mode.value}&componentId=${
        props.componentId
      }`
    });
  };

  const handleCancel = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/liquidMeasure/result/index?id=${
        props.id
      }&componentId=${props.componentId}`
    });
  };

  onMounted(() => {});
  return {
    mode,
    selectedBalance,
    deviceList,
    hasAutoPermission,
    hasManualPermission,
    handleCancel,
    handleNextStep,
    toResult,
    setSelectedBalance
  };
};
