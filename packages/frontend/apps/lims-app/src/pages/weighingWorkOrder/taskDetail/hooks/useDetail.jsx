import { queryWeighCenterExecuteTicketDetail } from '@/api';
import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { computed, reactive, ref } from 'vue';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useDetail = ({ props }) => {
  const { math } = useMathJs();
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
      field: 'weighRequirementWeighedQuantity',
    },
    {
      label: t('未称量'),
      field: 'notWeighedQuantity',
      color: 'var(--bmos-color-warning)',
    },
  ];
  const dataInfoData = computed(() => {
    return {
      requirementQuantity: {
        value: `${detailData.value.requirementQuantity || '-'}${
          detailData.value.unitName
        }`,
      },
      weighRequirementWeighedQuantity: {
        value: `${detailData.value.weighRequirementWeighedQuantity || '-'}${detailData.value.unitName}`,
      },
      notWeighedQuantity: {
        value: `${detailData.value.notWeighedQuantity || '-'}${detailData.value.unitName}`,
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
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 300,
      },
      {
        prop: 'requirementQuantity',
        label: t('配料量'),
        width: 145,
      },
      {
        prop: 'weighedQuantity',
        label: t('已称量'),
        width: 145,
      },
      {
        prop: 'unWeighed',
        label: t('未称量'),
        width: 145,
        customRender: ({ row }) => {
          const a = math.bignumber(row.requirementQuantity);
          const b = math.bignumber(row.weighedQuantity);
          const c = math.subtract(a, b).toString();
          return c;
        },
      },

      {
        prop: 'unitName',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'materialName',
        label: t('产品名称'),
        width: 200,
      },
      {
        prop: 'materialMergeCode',
        label: t('产品编码'),
        width: 200,
      },
      {
        prop: 'batchNo',
        label: t('生产批号'),
        width: 240,
      },
      {
        prop: 'planDate',
        label: t('计划生产时间'),
        width: 240,
      },
      {
        prop: 'requirementUsage',
        label: t('需求用途'),
        width: 240,
      },
      {
        prop: 'remark',
        label: t('备注'),
        width: 240,
      },
      {
        prop: 'requirementStatus',
        label: t('状态'),
        width: 112,
        fixed: 'right',
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
  // 获取称量任务详情
  const getWeighCenterExecuteTaskById = async () => {
    const res = await queryWeighCenterExecuteTicketDetail(props.id);
    detailData.value = res.data;
    tableProps.data = res.data.requirements;
    const {
      materialName,
      materialMergeCode,
      centreName,
      centreCode,
    } = res.data;
    detailData.value.weighMaterial = `${materialMergeCode}-${materialName}`;
    detailData.value.weighCentre = `${centreCode}-${centreName}`;
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
