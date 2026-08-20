import { getOutputBalanceListByStationIdApi, getStationIdListByComponentInstanceIdApi, weighCenterExecuteGetConfigByEquipmentIdApi } from '@/api';
import { usePermissionStore } from '@/stores/permission.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useModeDevice = () => {
  const { hasPermission } = usePermissionStore();
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const { showNotify } = useNotify();

  const query = ref({});
  const mode = ref(0);
  const modeList = computed(() => {
    return [
      {
        value: 0,
        label: t('秤具称量'),
        show: hasPermission('121010001002026'),
        showDeviceList: true,
      },
      {
        value: 1,
        label: t('手动称量'),
        show: hasPermission('121010001002027'),
      },
    ];
  });
  const stepList = [
    {
      title: t('设备&模式'),
    },
    {
      title: t('称量'),
    },
  ];
  const deviceList = ref([]);
  const taskDetail = ref({});

  // 校验秤具是否空闲
  const checkBalanceIsFree = async () => {
    const res = await weighCenterExecuteGetConfigByEquipmentIdApi({
      equipmentId: selectedBalance.value.balanceId,
    });
    return res.data.status;
  };
  const handleNextStep = async () => {
    if (mode.value === null) {
      showNotify({
        message: t('暂无称量权限'),
        type: 'danger',
      });
      return;
    }
    if (mode.value === 0) {
      if (!selectedBalance.value.balanceId) {
        showNotify({
          message: t('请选择秤具'),
          type: 'danger',
        });
        return;
      }
      const status = await checkBalanceIsFree();
      if (status === 2) {
        showNotify({
          message: t('秤具不可用'),
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
      url: `/pages/businessComponents/weighingData/weighing/index?id=${query.value.id}&mode=${
        mode.value
      }`,
    });
  };

  const handleCancel = () => {
    uni.navigateBack();
  };

  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/weighingData/result/index?id=${query.value.id}`,
    });
  };

  // 获取秤具列表
  const getBalanceList = async () => {
    try {
      // 根据componentInstanceId获取工位id列表
      const { data } = await getStationIdListByComponentInstanceIdApi({
        componentInstanceId: query.value.id,
      });
      // 根据工位id获取秤具列表
      const res = await getOutputBalanceListByStationIdApi(data);
      deviceList.value = res.data;
    }
    catch (error) {
      error.message && showNotify({
        message: error.message,
        type: 'danger',
      });
    }
  };

  onMounted(() => {
    getBalanceList();
  });
  return {
    query,
    mode,
    modeList,
    stepList,
    taskDetail,
    deviceList,
    handleCancel,
    handleNextStep,
    toResult,
  };
};
