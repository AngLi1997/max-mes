import {
  postRequisitionReserveCancel,
  repositoryReservedBatch,
  requisitionReservedMaterial,
  requisitionStorageCancel,
} from '@/api';
import { BMIcon } from '@/BMComponents';

import {
  t,
} from '@/utils/useBmosI18n.js';
import Big from 'big.js';
import {
  computed,
  nextTick,
  ref,
} from 'vue';
// 设置保留小数点后10位，根据需求调整
Big.RM = Big.DP = 10;
export const useTable = ({
  UseColumns,
  props,
}) => {
  const {
    tabSub,
    current,
    isCur,
    orderQuantity,
    loading,
    pendingQuantity,
    totalPlannedQuantity,
    totalTheoreticalQuantity,
  } = UseColumns;
  const tableData = ref([]);
  const tableRef = ref();
  const deleteRow = ref({});
  const showDeletePopup = ref(false);

  const viewDelete = (data) => {
    deleteRow.value = data;
    showDeletePopup.value = true;
  };
  const tableProps = computed(() => {
    const tableColProps = [
      {
        label: '',
        fixed: 'left',
        prop: 'BMOSDelete',
        width: 50,
        customRender: ({ row }) => {
          return <BMIcon onClick={() => viewDelete(row)} name="shanchu" size="18.75rpx" color="var(--bmos-color-error)" />;
        },
      },
      {
        label: t('物料批号'),
        prop: 'materialBatchNo',
      },
      {
        label: t('物料件号'),
        prop: 'materialNo',
      },
      {
        label: t('物料量'),
        prop: 'quantity',
      },
      {
        label: t('理论量'),
        prop: 'theoreticalQuantity',
      },
      {
        label: t('单位'),
        prop: 'unitName',
      },
      {
        label: t('暂存货位'),
        prop: 'materialPositionName',
      },
      {
        label: t('货位编码'),
        prop: 'materialPositionCode',
      },
      {
        label: t('预定人'),
        prop: 'userName',
      },
      {
        label: t('有效期至'),
        prop: 'expiredDate',
      },
    ];
    return {
      pagination: false,
      tableColProps,
    };
  });
  // 待领量
  const calculate = (totalNum, current) => {
    return new Big(totalNum || 0).minus(new Big(current || 0)).toString();
  };
  // 合计
  const addUpTo = (list, key) => {
    orderQuantity.value = list.map(item => item[key] || 0).reduce((sum, value) =>
      sum.plus(new Big(value)), new Big(0)).toString();
  };
  const addCur = (num, cur) => {
    return new Big(num || 0).plus(new Big(cur || 0)).toString();
  };
  // 暂存物料api
  const reservedMaterial = async (e) => {
    loading.value = true;
    try {
      const data = {
        productPlanId: e?.productPlanId,
        materialId: e?.materialId,
        formulaMaterialId: e?.id,
        requisitionPlanId: e.requisitionPlanId,
      };
      const res = await requisitionReservedMaterial(data);
      tableData.value = res.data.reservedList;
      addUpTo(res.data.reservedList, 'theoreticalQuantity');
      isCur.value = addCur(res.data.totalPlannedQuantity, orderQuantity.value);
      nextTick(() => {
        loading.value = false;
      });
    }
    catch (error) {
      // TODO handle the exception
      loading.value = false;
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  // 处理相应接口请求
  const getPage = async () => {
    tableData.value = [];
    switch (tabSub.index) {
      case 0:
        await reservedMaterial(current.currentList);
        break;
    }
  };
  // 暂存删除
  const deleteZcTable = async (id) => {
    try {
      const postParams = {
        componentInstanceId: current.currentList?.componentInstanceId,
        storageMaterialId: id,
      };
      const postParams2 = {
        batchId: current.currentList?.productPlanId,
        processId: current.currentList?.processId,
        storageMaterialId: id,
      };
      props.confirmBefore ? await requisitionStorageCancel(postParams2) : await postRequisitionReserveCancel(postParams);
      uni.showToast({
        title: t('删除成功'),
        icon: 'success',
        duration: 2000,
        mask: true,
        success: async () => {
          await getPage();
        },
      });
      showDeletePopup.value = false;
    }
    catch (error) {
      // TODO handle the exception
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  const confirmDeletePopup = async () => {
    try {
      switch (tabSub.index) {
        case 0:
          await deleteZcTable(deleteRow.value?.storageMaterialId);
          break;
      }
    }
    catch (error) {
      console.log(error);
      //
    }
  };
  const cancelDeletePopup = () => {
    showDeletePopup.value = false;
  };
  // 批次仓库物料
  const reservedBatch = async (e) => {
    loading.value = true;
    try {
      const data = {
        requisitionPlanId: e?.requisitionPlanId,
        formulaMaterialId: e?.id,
      };
      const res = await repositoryReservedBatch(data);
      tableData.value = res.data.batchList;
      orderQuantity.value = res.data.storageTheoreticalQuantity;
      pendingQuantity.value = calculate(current.currentList?.theoreticalQuantity, orderQuantity.value);
      totalPlannedQuantity.value = res.data.totalPlannedQuantity;
      totalTheoreticalQuantity.value = res.data.totalTheoreticalQuantity;
      isCur.value = addCur(res.data.totalPlannedQuantity, res.data.storageTheoreticalQuantity);
      nextTick(() => {
        loading.value = false;
      });
    }
    catch (error) {
      // TODO handle the exception
      loading.value = false;
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };

  // 处理刷新
  const estOute = (data) => {
    if (data) {
      getPage();
    }
  };
  return {
    tableRef,
    tableProps,
    showDeletePopup,
    confirmDeletePopup,
    cancelDeletePopup,
    viewDelete,
    tableData,
    reservedBatch,
    reservedMaterial,
    getPage,
    estOute,
  };
};
