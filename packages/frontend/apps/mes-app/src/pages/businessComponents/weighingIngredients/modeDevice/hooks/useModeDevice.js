import { getBalanceListByStationIdApi, getConfigByEquipmentIdApi } from '@/api';
import { useWeighingIngredientsStore } from '@/stores/businessComponents/weighingIngredients/index.js';
import { usePermissionStore } from '@/stores/permission.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useModeDevice = () => {
  const { math } = useMathJs();
  const { hasPermission } = usePermissionStore();
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);
  const { showNotify } = useNotify();

  const weighingIngredientsStore = useWeighingIngredientsStore();
  const { weighingIngredientsData, weighingDetailsData: detailData } = storeToRefs(
    weighingIngredientsStore,
  );
  const {
    setSelectedBalance,
    queryWeighDetailByPlanIdAndBatchId,
    setTableData,
  } = weighingIngredientsStore;

  // 页面参数
  const query = ref({});

  // 称量模式
  const weightMode = ref([0, 1]);

  const mode = ref(0);
  const modeList = computed(() => {
    return [
      {
        value: 0,
        label: t('配料称量'),
        show: weightMode.value.includes(0) && hasPermission(121010001002012),
        showDeviceList: true,
      },
      {
        value: 1,
        label: t('手动称量'),
        show: weightMode.value.includes(1) && hasPermission(121010001002013),
      },
    ];
  });
  const deviceList = ref([]);

  // 校验秤具是否空闲
  const checkBalanceIsFree = async (equipmentId) => {
    const res = await getConfigByEquipmentIdApi({ equipmentId });
    return res.data.status;
  };

  // 上一部
  const toBack = () => {
    uni.redirectTo({
      url: `/pages/businessComponents/weighingIngredients/index?componentId=${query.value.componentId}`,
    });
  };
    // 下一步
  const submit = async () => {
    if (mode.value === null) {
      showNotify({
        message: t('暂无称量权限'),
        type: 'danger',
      });
      return;
    }
    const isAuto = (mode.value === 0 || mode.value === 3);
    if (isAuto) {
      // 校验秤具是否校准
      if (!selectedBalance.value.isCalibrated) {
        showNotify({
          message: t('秤具未校准'),
          type: 'danger',
        });
        return;
      }
      // 校验秤具精度
      const a1 = detailData.value.scale || 0; // 物料精度
      const e1 = selectedBalance.value.precision || 0;// 秤具精度
      // 如果物料精度小于秤具精度
      const result = math.smaller(a1, e1);
      if (result) {
        showNotify({
          message: t('请选择精度更准确的秤具'),
          type: 'danger',
        });
        return;
      }

      // 校验秤具单位与物料单位是否一致
      const balanceUnitId = selectedBalance.value.unitId;
      const materialUnitId = detailData.value.unitId;
      if (balanceUnitId !== materialUnitId) {
        showNotify({
          message: t('请选择与物料单位一致的秤具'),
          type: 'danger',
        });
        return;
      }

      // 校验秤具是否空闲
      const res = await checkBalanceIsFree(selectedBalance.value.balanceId);
      if (res === 3) {
        showNotify({
          message: t('秤具已占用'),
          type: 'danger',
        });
        return;
      }
      if (res === 4 || res === 2) {
        showNotify({
          message: t('秤具不可用'),
          type: 'danger',
        });
        return;
      }
    }
    setSelectedBalance(selectedBalance.value || null);
    uni.navigateTo({
      url: `/pages/businessComponents/weighingIngredients/removePeel/index?auto=${mode.value}&componentId=${query.value.componentId}`,
    });
  };
    // 称量结果
  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/weighingIngredients/weighingResults/index?componentId=${query.value.componentId}`,
    });
  };
  onMounted(() => {
    // 获取秤具列表
    getBalanceListByStationIdApi(weighingIngredientsData.value.station).then(
      (res) => {
        deviceList.value = res.data || [];
      },
    );
    // 获取称量详情
    queryWeighDetailByPlanIdAndBatchId();
    // 清空表格
    setTableData([]);
  });
  return {
    query,
    mode,
    modeList,
    deviceList,
    weightMode,
    toBack,
    submit,
    toResult,
  };
};
