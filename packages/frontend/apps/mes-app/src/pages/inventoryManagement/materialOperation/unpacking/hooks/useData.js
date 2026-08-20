import { t } from '@/utils/useBmosI18n.js';
import { useMathJs } from '@/utils/useMathJs.js';
import { reactive, ref } from 'vue';

export const useData = ({ onScanConfirm, urlQuery }) => {
  const { math } = useMathJs();
  const details = ref([
    {
      title: t('物料名称'),
      dataIndex: 'materialName',
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode',
    },
    {
      title: t('物料批次'),
      dataIndex: 'materialBatchNo',
    },
    {
      title: t('物料件号'),
      dataIndex: 'materialNo',
    },
    {
      title: t('物料量'),
      dataIndex: 'quantity',
      color: '#ff9933',
    },
    {
      title: t('单位'),
      dataIndex: 'unit',
    },
  ]);
  const formsRef = ref();
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'quantity',
        component: 'Input',
        label: t('出库量'),
        componentProps: {
          type: 'number',
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入出库量'),
              validator: (val) => {
                if (!val)
                  return Promise.reject(t('请输入出库量'));
                // 输入正数
                if (Number(val) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                if (!/^\d{1,10}(\.\d{1,9})?$/.test(val)) {
                  return Promise.reject(
                    t('整数部分最多为10位，小数部分最多9位'),
                  );
                }
                if (math.evaluate(`${val} > ${urlQuery.value.quantity}`)) {
                  return Promise.reject(t('出库量应小于当前物料件的物料量'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'unit',
        component: 'Input',
        label: t('单位'),
        colProps: {
          span: 12,
        },
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'container',
        component: 'Input',
        label: t('容器'),
        colProps: {
          span: 12,
        },
        componentProps: ({ formModel }) => {
          return {
            suffixIcon: 'search',
            onClicksuffixicon: () => {
              onScanConfirm(formModel);
            },
            onConfirm: () => {
              onScanConfirm(formModel);
            },
          };
        },
      },
      {
        field: 'remark',
        component: 'Input',
        label: t('备注'),
        required: true,
        colProps: {
          span: 12,
        },
      },
    ],
  });

  return {
    details,
    formsRef,
    formProps,
  };
};
