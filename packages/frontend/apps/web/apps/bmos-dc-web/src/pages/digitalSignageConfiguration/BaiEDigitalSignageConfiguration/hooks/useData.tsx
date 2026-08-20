import {
  getEffectiveProcessListTreeReq,
  queryBelarusDashboardConfigCreate,
  queryBelarusDashboardConfigDetail,
  queryBelarusDashboardConfigUpdate,
  queryDatasetDetailApi,
  queryDatasetListByProcessIdApi,
  recordRoundingList,
} from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import type { RenderCallbackParams } from '@bmos/components';
import { FormProps } from '@bmos/components';
import { loopSelectableNotValueTree } from '@bmos/utils';
import { Modal, message } from 'ant-design-vue';
export const useData = () => {
  const pointList = ref<any>([]);
  const formRefs = ref<any>({});
  const configCode = ref<any>();
  const isNew = ref(false);
  const configList = ref([
    {
      title: t('投浆量'),
      code: 1,
    },
    {
      title: t('人血白蛋白批次产量'),
      code: 2,
    },
    {
      title: t('免疫球蛋白批次产量'),
      code: 3,
    },
    {
      title: t('人血白蛋白生产进度'),
      code: 4,
    },
    {
      title: t('免疫球蛋白生产进度'),
      code: 5,
    },
  ]);
  const formCode = ref(1);
  const changeConfigCode = async (code: any) => {
    // 重置
    formCode.value = 1;
    configCode.value = code;
    formRefs.value = {};
    pointList.value = [];
    // 查询历史数据
    const { data } = await queryBelarusDashboardConfigDetail({ type: code });
    isNew.value = data.length == 0;
    if (data.length == 0) {
      // 没有历史数据为新建,不需要回显
      pointList.value.push({
        formCode: formCode.value,
      });
      formCode.value++;
      return;
    }
    data.forEach((item: any) => {
      pointList.value.push({
        formCode: formCode.value,
        ...item,
      });
      formCode.value++;
    });
    nextTick(() => {
      pointList.value.forEach(async (item: any) => {
        if (configCode.value == 4 || configCode.value == 5) {
          formRefs.value[item.formCode]?.setFieldsValue(item);
          return;
        }
        // 获取数据集下拉
        const { data: datasetIdOptions } = await queryDatasetListByProcessIdApi({
          processId: item.processId,
          datasetType: 'POINT',
        });
        formRefs.value[item.formCode]?.updateSchema({
          field: 'datasetId',
          componentProps: {
            options: datasetIdOptions,
          },
        });
        // 获取数据点下拉
        const { data: datapointIdOptions } = await queryDatasetDetailApi({ id: item.datasetId });
        formRefs.value[item.formCode]?.updateSchema({
          field: 'datapointId',
          componentProps: {
            options: datapointIdOptions.datasetPointList,
          },
        });
        formRefs.value[item.formCode]?.setFieldsValue(item);
        formRefs.value[item.formCode].formModel.datasetName = item?.datasetName;
        formRefs.value[item.formCode].formModel.datapointName = item?.datapointName;
      });
    });
  };
  const getFormRefs = (el: any, item: any) => {
    if (el) {
      formRefs.value[item.formCode] = el;
    }
  };
  const deleteConditionList = (formItem: any) => {
    Modal.confirm({
      title: t('是否删除该数据'),
      icon: h(ExclamationCircleOutlined),
      content: t(`删除后无法恢复，是否删除？`),
      async onOk() {
        pointList.value = pointList.value.filter((item: any) => item.formCode !== formItem.formCode);
        delete formRefs.value[formItem.formCode];
      },
    });
  };
  const addPoint = () => {
    pointList.value.push({
      formCode: formCode.value,
    });
    formCode.value++;
  };
  const saveData = async () => {
    try {
      const formRefList = Object.values(formRefs.value);
      const validateResult = await Promise.all(formRefList.map((formInstance: any) => formInstance.validate()));
      if (validateResult.length == 0) {
        message.error(t('请配置数据点'));
        return;
      }
      if (isNew.value) {
        // 新增
        await queryBelarusDashboardConfigCreate({
          type: configCode.value,
          processList: validateResult,
        });
        message.success(t('创建成功'));
      } else {
        // 编辑
        await queryBelarusDashboardConfigUpdate({
          type: configCode.value,
          processList: validateResult,
        });
        message.success(t('编辑成功'));
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
  // 表单属性
  const formProps: Ref<FormProps> = ref({
    layout: 'vertical',
    showAdvancedButton: false,
    showActionButtonGroup: false,
    rowProps: {
      gutter: [32, 0],
    },
    baseColProps: {
      span: 12,
    },
    initialValues: {},
    schemas: [
      {
        field: 'processId',
        component: 'TreeSelect',
        label: t('工艺'),
        required: true,
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            options: [],
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            treeNodeFilterProp: 'showName',
            request: async () => {
              // 获取工艺树
              const { data } = await getEffectiveProcessListTreeReq({ activeProcess: true, filterPermission: true });
              return loopSelectableNotValueTree(data, 'isFlag', true);
            },
            onChange: async (processId: string) => {
              if (configCode.value == 4 || configCode.value == 5) {
                return;
              }
              formModel.datasetId = undefined;
              formModel.datapointId = undefined;
              if (!processId) {
                formInstance.updateSchema({
                  field: 'datasetId',
                  componentProps: {
                    options: [],
                  },
                });
                formInstance.updateSchema({
                  field: 'datapointId',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              const { data } = await queryDatasetListByProcessIdApi({ processId, datasetType: 'POINT' });
              formInstance.updateSchema({
                field: 'datasetId',
                componentProps: {
                  options: data,
                },
              });
            },
          };
        },
      },
      {
        field: 'datasetId',
        label: t('数据集名称'),
        component: 'Select',
        required: true,
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        vIf: () => configCode.value != 4 && configCode.value != 5,
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: async (id: string, option: any) => {
              formModel.datapointId = undefined;
              formModel.datasetName = option?.name;
              if (!id) {
                // 清空重置
                formInstance.updateSchema({
                  field: 'datapointId',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              const { data } = await queryDatasetDetailApi({ id });
              formInstance.updateSchema({
                field: 'datapointId',
                componentProps: {
                  options: data.datasetPointList,
                },
              });
            },
          };
        },
      },
      {
        field: 'datapointId',
        label: t('索引-数据点名称'),
        component: 'Select',
        required: true,
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        vIf: () => configCode.value != 4 && configCode.value != 5,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: async (id: string, option: any) => {
              formModel.datapointName = option?.name;
            },
          };
        },
      },
      {
        field: 'coefficient',
        label: t('计算系数'),
        component: 'Input',
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        vIf: () => configCode.value != 4 && configCode.value != 5,
        dynamicRules() {
          return [
            {
              required: false,
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (!value) {
                  return Promise.resolve();
                }
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (value.includes('.')) {
                  const int = value.split('.')[0];
                  const float = value.split('.')[1];
                  if (int.length > 10) {
                    return Promise.reject(t('整数部分不能超过10位'));
                  }
                  if (float.length > 9) {
                    return Promise.reject(t('小数部分不能超过9位'));
                  }
                } else {
                  if (value.length > 10) {
                    return Promise.reject(t('整数部分不能超过10位'));
                  }
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'roundingCode',
        label: t('修约方式'),
        component: 'Select',
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
        vIf: () => configCode.value != 4 && configCode.value != 5,
        componentProps: () => {
          return {
            request: async () => {
              // 获取设备数据
              try {
                const { data } = await recordRoundingList();
                return data;
              } catch (error: any) {
                console.log(error);
              }
            },
          };
        },
      },
      {
        field: 'valuePrecision',
        label: t('精度'),
        component: 'Input',
        vIf: () => configCode.value != 4 && configCode.value != 5,
        dynamicRules() {
          return [
            {
              required: false,
              trigger: 'blur',
              validator: async (_rule: any, value: any) => {
                if (!value) {
                  // 非必填,为填值不校验
                  return Promise.resolve();
                }
                const number = value * 1;
                if (Number.isNaN(number)) {
                  return Promise.reject(t('请输入数字'));
                } else if (number < 0) {
                  return Promise.reject(t('请输入大于等于0的整数'));
                } else if (number > 15) {
                  return Promise.reject(t('请输入小于等于15的整数'));
                } else if (!Number.isInteger(number)) {
                  return Promise.reject(t('请输入整数'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        formItemProps: {
          htmlFor: 'name' + Math.random(),
        },
      },
    ],
  });
  onMounted(async () => {
    changeConfigCode(1);
  });
  return {
    pointList,
    getFormRefs,
    formProps,
    deleteConditionList,
    addPoint,
    configList,
    changeConfigCode,
    configCode,
    saveData,
  };
};
