import {
  getLiquidMeasurePlanDetailApi,
  getLiquidMeasurePlanListApi,
} from '@/api';
import { urlQueryRef } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
import { useLiquidMeasureStore } from '@/stores/businessComponents/liquidMeasure/index.js';
import { t } from '@/utils/useBmosI18n.js';
import { onShow } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';
import { useNotify } from 'wot-design-uni';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useDetail = ({ props }) => {
  const liquidMeasureStore = useLiquidMeasureStore();
  const { setSelectedLiquidMeasureSheet, setSelectedMaterialBatch } = liquidMeasureStore;
  const { showNotify } = useNotify();

  // 配液单详情
  const liquidSheetDetail = ref({});
  // 配液单
  const showLiquidSheet = ref(false);
  const liquidSheetValue = ref('');
  const liquidSheetOptions = ref([]);
  // 切换配液单
  const handleSwitchLiquidSheet = async () => {
    if (props.switch === 'false') {
      showNotify({
        message: t('已确认配液单，无法切换'),
        type: 'danger',
      });
      return;
    }
    showLiquidSheet.value = true;
  };
  const infoItems = [
    {
      label: t('配液单'),
      field: 'name',
      type: 'text',
    },
    {
      label: t('切换配液单'),
      type: 'button',
      click: handleSwitchLiquidSheet,
    },
  ];

  const statusType = {
    UNMEASURED: 'warning',
    MEASURING: 'primary',
    COMPLETED: 'success',
  };

  const tableRef = ref();

  const tableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 204,
      },
      {
        prop: 'materialMergeCode',
        label: t('物料编码'),
        width: 171,
      },
      {
        prop: 'materialBatchNo',
        label: t('物料批号'),
        width: 240,
      },
      {
        prop: 'preparationQuantity',
        label: t('配液量'),
        width: 145,
      },
      {
        prop: 'measuredQuantity',
        label: t('已量取'),
        width: 145,
      },
      {
        prop: 'unmeasuredQuantity',
        label: t('未量取'),
        width: 145,
      },
      {
        prop: 'unitName',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'name',
        label: t('状态'),
        width: 108,
        customRender: ({ row }) => {
          return (
            <WdTag
              type={statusType[row.measureStatus.value]}
              plain
            >
              {row.measureStatus.label}
            </WdTag>
          );
        },
      },
    ],
  });

  const leftClick = () => {
    uni.navigateBack();
  };

  // 获取未量取的配液单列表
  const getLiquidMeasurePlanList = async () => {
    const res = await getLiquidMeasurePlanListApi({
      productPlanId: urlQueryRef.value.productPlanId,
    });
    liquidSheetOptions.value = res.data || [];
  };
  // 获取配液单详情
  const getLiquidMeasurePlanDetail = async () => {
    const res = await getLiquidMeasurePlanDetailApi({
      id: props.id,
      liquidPreparationId: liquidSheetValue.value,
    });
    liquidSheetDetail.value = res.data;
    tableProps.data = res.data.batchList || [];
  };

  const onLiquidSheetConfirm = (data) => {
    if (data) {
      setSelectedLiquidMeasureSheet({
        liquidPreparationPlanName: data.name,
        liquidPreparationPlanId: data.id,
      });
      getLiquidMeasurePlanDetail();
      setSelectedMaterialBatch(null);
    }
    else {
      showNotify({
        type: 'danger',
        message: t('请选择配液单'),
      });
    }
  };

  onShow(() => {
    liquidSheetValue.value = props.liquidPreparationId;
    getLiquidMeasurePlanDetail();
    getLiquidMeasurePlanList();
  });
  return {
    liquidSheetDetail,
    infoItems,
    tableRef,
    tableProps,
    showLiquidSheet,
    liquidSheetValue,
    liquidSheetOptions,
    leftClick,
    onLiquidSheetConfirm,
  };
};
