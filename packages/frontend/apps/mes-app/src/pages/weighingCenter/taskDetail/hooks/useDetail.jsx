import { queryWeighCenterExecuteTaskById } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { computed, reactive, ref } from 'vue';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useDetail = ({ props }) => {
  const detailData = ref({});
  const infoItems = [
    {
      label: t('称量物料'),
      field: 'weighMaterial',
    },
    {
      label: t('称量中心'),
      field: 'weighCentre',
    },
  ];

  const dataInfoItems = [
    {
      label: t('需求量'),
      field: 'requirementQuantity',
    },
    {
      label: t('已称量'),
      field: 'weighed',
    },
    {
      label: t('未称量'),
      field: 'unWeighed',
      color: 'var(--bmos-color-warning)',
    },
  ];
  const dataInfoData = computed(() => {
    return {
      requirementQuantity: {
        value: `${detailData.value.requirementQuantity || '-'}${
          detailData.value.unit
        }`,
      },
      weighed: {
        value: `${detailData.value.weighed || '-'}${detailData.value.unit}`,
      },
      unWeighed: {
        value: `${detailData.value.unWeighed || '-'}${detailData.value.unit}`,
      },
    };
  });
  const tableRef = ref();
  const tableProps = reactive({
    pagination: false,
    border: true,
    data: [],
    tableColProps: [
      {
        prop: 'productName',
        label: t('产品名称'),
        width: 200,
      },
      {
        prop: 'productMergeCode',
        label: t('产品编码'),
        width: 171,
      },
      {
        prop: 'processName',
        label: t('工艺名称'),
        width: 300,
      },
      {
        prop: 'batchNo',
        label: t('生产批号'),
        width: 240,
      },
      {
        prop: 'requirementQuantity',
        label: t('需求量'),
        width: 145,
      },
      {
        prop: 'weighed',
        label: t('已称量'),
        width: 145,
      },
      {
        prop: 'unWeighed',
        label: t('未称量'),
        width: 145,
      },

      {
        prop: 'unit',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'weighStatus',
        label: t('状态'),
        width: 112,
        customRender: ({ row }) => {
          return (
            <WdTag type={['default', 'warning', 'primary', 'success', 'default'][row.requirementStatus?.value]} plain>
              {row.requirementStatus.label}
            </WdTag>
          );
        },
      },
    ],
  });
  const leftClick = () => {
    uni.navigateBack();
  };
  // 获取起称量任务详情
  const getWeighCenterExecuteTaskById = async () => {
    const res = await queryWeighCenterExecuteTaskById({ taskId: props.id });
    detailData.value = res.data;
    tableProps.data = res.data.requirements;
    const {
      materialName,
      materialMergeCode,
      weighCentreName,
      weighCentreCode,
    } = res.data;
    detailData.value.weighMaterial = `${materialName}-${materialMergeCode}`;
    detailData.value.weighCentre = `${weighCentreCode}-${weighCentreName}`;
  };
  return {
    infoItems,
    detailData,
    dataInfoItems,
    dataInfoData,
    tableRef,
    tableProps,
    leftClick,
    getWeighCenterExecuteTaskById,
  };
};
