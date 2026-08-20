import { getMesUnitListDownExtendBound, reqFreeWeighGetBalanceList } from '@/api';
import { getConfigByEquipmentIdApi } from '@/api/weighingIngredientsApi.js';
import { usePermissionStore } from '@/stores/permission.js';
import { useWeighingMachineStore } from '@/stores/weighingMachine/index.js';
import { useMaterialWeighingStore } from '@/stores/workbench/materialWeighing/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { storeToRefs } from 'pinia';
import { computed, onMounted, ref } from 'vue';
import { useNotify } from 'wot-design-uni';

export const useModeDevice = () => {
  const materialWeighingStore = useMaterialWeighingStore();
  const weighingMachineStore = useWeighingMachineStore();
  const { selectedBalance } = storeToRefs(weighingMachineStore);

  const { hasPermission } = usePermissionStore();
  const { detailData } = storeToRefs(materialWeighingStore);
  const { showNotify } = useNotify();

  // 页面参数
  const query = ref({});

  const mode = ref(0);
  const modeList = computed(() => {
    return [
      {
        value: 0,
        label: t('增重称量'),
        show: hasPermission(121020005000001),
        showDeviceList: true,
      },
      {
        value: 1,
        label: t('手动称量'),
        show: hasPermission(121020005000002),
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
    uni.navigateBack();
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
    const isAuto = mode.value === 0;
    if (isAuto) {
      // 校验秤具是否校准
      if (!selectedBalance.value.isCalibrated) {
        showNotify({
          message: t('秤具未校准'),
          type: 'danger',
        });
        return;
      }

      // 校验秤具单位与物料单位是否一致
      const balanceUnitId = selectedBalance.value.unitId;
      let unitOptions = [detailData.value.unitId];
      try {
        const { data } = await getMesUnitListDownExtendBound({
          materialId: detailData.value.materialId,
        });
        unitOptions.push(...data.map(item => item.id));
      }
      catch (_error) {
        unitOptions = [detailData.value.unitId];
      }
      if (!unitOptions.includes(balanceUnitId)) {
        showNotify({
          message: t('请选择与物料单位一致的秤具'),
          type: 'danger',
        });
        return;
      }

      // 校验秤具是否空闲
      const status = await checkBalanceIsFree(selectedBalance.value.balanceId);
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
    const params = [
      `mode=${mode.value}`,
    ];
    uni.navigateTo({
      url: `/pages/materialWeighing/removePeel/index?${params.join(
        '&',
      )}`,
    });
  };

  onMounted(() => {
    reqFreeWeighGetBalanceList().then((res) => {
      deviceList.value = res.data || [];
    });
  });
  return {
    query,
    mode,
    modeList,
    deviceList,
    toBack,
    submit,
  };
};
