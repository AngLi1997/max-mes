import {
  queryWeighCenterExecuteTaskById,
  weighCenterExecuteGetBalanceListByStationIdApi,
  weighCenterExecuteGetConfigByEquipmentIdApi,
} from '@/api';
import { usePermissionStore } from '@/stores/permission.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useModeDevice = ({ props }) => {
  const { hasPermission } = usePermissionStore();
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const { setSelectedBalance } = weighingMachineStore;
  const { showNotify } = useNotify();
  const mode = ref(true);
  const taskDetail = ref({});
  const deviceList = ref([]);

  const hasAutoPermission = computed(() => hasPermission('121020001000001'));
  const hasManualPermission = computed(() => hasPermission('121020001000002'));

  // 校验秤具是否空闲
  const checkBalanceIsFree = async (equipmentId) => {
    const res = await weighCenterExecuteGetConfigByEquipmentIdApi({
      equipmentId: selectedBalance.value.balanceId,
    });
    return res.data.status;
  };
  const handleNextStep = async () => {
    if (!hasAutoPermission.value && !hasManualPermission.value) {
      showNotify({
        message: t('暂无称量权限'),
        type: 'danger',
      });
      return;
    }
    if (mode.value) {
      if (!selectedBalance.value.balanceId) {
        uni.showToast({
          title: t('请选择秤具'),
          icon: 'none',
        });
        return;
      }
      const status = await checkBalanceIsFree(selectedBalance.value.balanceId);
      if (status === 2) {
        showNotify({
          message: t('秤具不可用'),
          type: 'danger',
        });
        return;
      }
      // 校验所选秤具的单位是否与该物料需求的单位一致
      if (selectedBalance.value.unitId !== taskDetail.value.unitId) {
        showNotify({
          message: t('请选择与物料需求量单位一致的秤具'),
          type: 'danger',
        });
        return;
      }
      if (status === 3) {
        showNotify({
          message: t('秤具已被占用'),
          type: 'danger',
        });
        return;
      }
      if (status === 4) {
        showNotify({
          message: t('秤具故障'),
          type: 'danger',
        });
        return;
      }
    }
    uni.navigateTo({
      url: `/pages/weighingCenter/materialWeighing/index?id=${props.id}&mode=${
        mode.value
      }&requirementId=${taskDetail.value.weighExecuteRequirement.id}`,
    });
  };

  const handleCancel = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/weighingCenter/result/index?id=${props.id}`,
    });
  };

  // 获取起称量任务详情 && 秤具列表
  const getWeighCenterExecuteTaskById = async () => {
    const res = await queryWeighCenterExecuteTaskById({ taskId: props.id });
    taskDetail.value = res.data;
    if (hasAutoPermission.value) {
      mode.value = true;
      const station = res.data.station;
      const res2 = await weighCenterExecuteGetBalanceListByStationIdApi(
        station,
      );
      deviceList.value = res2.data;
      if (deviceList.value.length > 0) {
        setSelectedBalance(deviceList.value[0]);
      }
      else {
        setSelectedBalance({});
      }
    }
    else {
      mode.value = false;
    }
  };

  onMounted(() => {
    getWeighCenterExecuteTaskById();
  });
  return {
    mode,
    taskDetail,
    selectedBalance,
    deviceList,
    hasAutoPermission,
    hasManualPermission,
    handleCancel,
    handleNextStep,
    toResult,
    setSelectedBalance,
  };
};
