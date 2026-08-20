import { t } from '@/utils/useBmosI18n.js';
import { ref, reactive } from 'vue';
import { useMathJs } from '@/utils/useMathJs.js';

export const useData = () => {
  const { math } = useMathJs();
  const details = ref([
    {
      title: t('物料名称'),
      dataIndex: 'materialName'
    },
    {
      title: t('物料编码'),
      dataIndex: 'mergeCode'
    },
    {
      title: t('物料批次'),
      dataIndex: 'materialBatchNo'
    },
    {
      title: t('物料件号'),
      dataIndex: 'materialNo'
    },
    {
      title: t('可用量'),
      dataIndex: 'availableQuantity',
      color: '#ff9933'
    },
    {
      title: t('单位'),
      dataIndex: 'unit'
    }
  ]);
  const formsRef = ref();
  // 表单配置
  const formProps = reactive({
    schemas: [
      {
        field: 'initQuantity',
        component: 'Input',
        label: t('初始量'),
        componentProps: {
          type: 'number'
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入初始量'),
              validator: (val) => {
                if (!val) return Promise.reject(t('请输入初始量'));
                // 输入正数
                if (Number(val) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                if (!/^\d{1,10}(\.\d{1,9})?$/.test(val)) {
                  return Promise.reject(
                    t('整数部分最多为10位，小数部分最多9位')
                  );
                }
                return Promise.resolve();
              }
            }
          ];
        }
      },
      {
        field: 'consumeQuantity',
        component: 'Input',
        label: t('消耗量'),
        colProps: {
          span: 12
        },
        componentProps: {
          type: 'number'
        },
        dynamicRules: ({ formModel }) => {
          return [
            {
              required: true,
              message: t('请输入消耗量'),
              validator: (val) => {
                if (!val) return Promise.reject(t('请输入消耗量'));
                // 输入正数
                if (Number(val) < 0) {
                  return Promise.reject(t('请输入非负数'));
                }
                if (!/^\d{1,10}(\.\d{1,9})?$/.test(val)) {
                  return Promise.reject(
                    t('整数部分最多为10位，小数部分最多9位')
                  );
                }
                if (
                  math.evaluate(
                    `${formModel.consumeQuantity} > ${formModel.initQuantity}`
                  )
                ) {
                  return Promise.reject(t('消耗量不能大于初始量'));
                }
                return Promise.resolve();
              }
            }
          ];
        }
      },
      {
        field: 'useUp',
        component: 'BMFormRadio',
        label: t('是否用尽'),
        required: true,
        colProps: {
          span: 12
        },
        componentProps: {
          options: [
            {
              label: t('是'),
              value: true
            },
            {
              label: t('否'),
              value: false
            }
          ]
        }
      },
      {
        field: 'remark',
        component: 'Input',
        label: t('备注'),
        required: true,
        colProps: {
          span: 12
        }
      }
    ]
  });

  return {
    details,
    formsRef,
    formProps
  };
};
