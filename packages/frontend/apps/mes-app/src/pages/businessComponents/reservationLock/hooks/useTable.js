import {
  getRequisitionReserveAvailableList,
  postRequisitionReserve,
  postStorageMaterialConfirmBatchReserve,
} from '@/api';
import {
  t,
} from '@/utils/useBmosI18n.js';
import {
  reactive,
  ref,
} from 'vue';

export const useTable = ({
  UseSubTab,
}) => {
  const {
    params,
    currentList,
    addUpTo,
  } = UseSubTab;
  // 表格数据
  const tableData = ref([]);
  const tableRef = ref();
  const tableProps = reactive({
    pagination: false,
    type: 'selection',
    tableColProps: [{
      label: t('物料批号'),
      prop: 'materialBatchNo',
    }, {
      label: t('物料件号'),
      prop: 'materialNo',
    }, {
      label: t('物料量'),
      prop: 'quantity',
    }, {
      label: t('理论量'),
      prop: 'theoreticalQuantity',
    }, {
      label: t('单位'),
      prop: 'unitName',
    }, {
      label: `${t('水分')}%`,
      prop: 'hydration',
    }, {
      label: `${t('含量')}%`,
      prop: 'noHydrationContent',
    }, {
      label: t('暂存货位'),
      prop: 'materialPositionName',
    }, {
      label: t('货位编码'),
      prop: 'materialPositionCode',
    }, {
      label: t('供应商'),
      prop: 'supplier',
    }, {
      label: t('生产商'),
      prop: 'producer',
    }, {
      label: t('有效期至'),
      prop: 'expiredDate',
    }],
  });
  const selectionChange = (selectedRows) => {
    params.selectData = selectedRows;
    addUpTo();
  };
  const materialApi = async () => {
    try {
      const paramsModel = {
        formulaMaterialId: currentList.value?.id,
      };
      const res = await getRequisitionReserveAvailableList(paramsModel);
      tableData.value = res.data;
      params.whole = currentList.value?.theoreticalQuantity;
      params.orderQuantity = currentList.value?.orderQuantity;
      addUpTo();
    }
    catch (error) {
      // TODO handle the exception
      console.log(error);
    }
  };
  // 提交
  const submit = async () => {
    if (params.selectData.length === 0) {
      return uni.showToast({
        title: t('请勾选要预定的物料'),
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
    // return;
    try {
      const data = {
        componentInstanceId: currentList.value?.componentInstanceId,
        formulaMaterialId: currentList.value?.id,
        storageMaterialIdList: params.selectData.map(item => item.storageMaterialId),
      };
      const data2 = {
        productPlanId: currentList.value?.productPlanId,
        storageMaterialIdList: params.selectData.map(item => item.storageMaterialId),
      };
      uni.showLoading({
        title: t('保存中...'),
        mask: true,
      });
      currentList.value?.confirmBefore === 'true' ? await postStorageMaterialConfirmBatchReserve(data2) : await postRequisitionReserve(data);
      uni.hideLoading();
      uni.navigateBack();
    }
    catch (error) {
      // TODO handle the exception
      uni.hideLoading();
      error.message && uni.showToast({
        title: error.message,
        icon: 'none',
        duration: 2000,
        mask: true,
      });
    }
  };
  return {
    tableData,
    tableRef,
    tableProps,
    selectionChange,
    materialApi,
    submit,
  };
};
