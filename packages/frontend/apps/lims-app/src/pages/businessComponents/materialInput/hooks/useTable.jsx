import { t } from '@/utils/useBmosI18n.js';
import { reactive, ref } from 'vue';
import WdTag from 'wot-design-uni/components/wd-tag/wd-tag.vue';

export const useTable = () => {
  const tableRef = ref();
  const status = {
    1: { label: t('待投料'), type: 'primary' },
    2: { label: t('投料中'), type: 'success' },
    3: { label: t('已投料'), type: 'default' },
    4: { label: t('已失效'), type: 'danger' },
    6: { label: t('未签名'), type: 'warning' },
  };

  const tableProps = reactive({
    pagination: false,
    data: [],
    border: true,
    tableColProps: [
      {
        prop: 'materialName',
        label: t('物料名称'),
        width: 120,
      },
      {
        prop: 'materialMergeCode',
        label: t('物料编码'),
        width: 120,
      },
      {
        prop: 'storageMaterialBatchNo',
        label: t('物料批号'),
        width: 160,
      },
      {
        prop: 'storageMaterialNo',
        label: t('物料件号'),
        width: 120,
      },
      {
        prop: 'quantity',
        label: t('物料量'),
        width: 120,
      },
      {
        prop: 'unit',
        label: t('单位'),
        width: 90,
      },
      {
        prop: 'weighInputStatus',
        label: t('状态'),
        width: 60,
        customRender: ({ row }) => {
          return <WdTag plain type={status[row.weighInputStatus?.value]?.type}>{status[row.weighInputStatus?.value]?.label}</WdTag>;
        },
      },
      {
        prop: 'inputUserName',
        label: t('投料人'),
        width: 120,
      },
      {
        prop: 'inputTime',
        label: t('投料时间'),
        width: 220,
      },
      {
        prop: 'deviceName',
        label: t('设备名称'),
        width: 120,
      },
      {
        prop: 'deviceCode',
        label: t('设备编号'),
        width: 120,
      },
    ],
  });
  return {
    tableRef,
    tableProps,
  };
};
