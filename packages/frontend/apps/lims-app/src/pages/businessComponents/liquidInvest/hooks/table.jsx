import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useTable = () => {
  const tableRef = ref();
  const tagTypes = {
    1: {
      type: 'primary',
      label: t('待投料'),
    },
    2: {
      type: 'success',
      label: t('投料中'),
    },
    3: {
      type: 'default',
      label: t('已投料'),
    },
    4: {
      type: 'danger',
      label: t('已失效'),
    },
    5: {
      type: 'warning',
      label: t('未签名'),
    },
  };

  const tableProps = reactive({
    pagination: false,
    data: [],
    border: false,
    tableColProps: [
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 350,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'materialMergeCode',
        label: t('物料编码'),
        width: 300,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 400,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 400,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'quantity',
        label: t('物料量'),
        width: 300,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 150,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'inputStatus',
        label: t('状态'),
        width: 240,
        thProps: {
          align: 'left',
        },
        customRender: ({ row }) => {
          return (
            <WdTag
              type={tagTypes[row.inputStatus.value]?.type}
              plain
            >
              {tagTypes[row.inputStatus.value]?.label}
            </WdTag>
          );
        },
      },
      {
        prop: 'importerName',
        label: t('投料人'),
        width: 400,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'inputTime',
        label: t('投料时间'),
        width: 400,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'deviceName',
        label: t('设备名称'),
        width: 400,
        thProps: {
          align: 'left',
        },
      },
      {
        prop: 'deviceCode',
        label: t('设备编号'),
        width: 400,
        thProps: {
          align: 'left',
        },
      },
    ],
  });

  return {
    tableRef,
    tableProps,
  };
};
