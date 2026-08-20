import {
  recordQueryListRecordByProductId,
  reqAllFactoryProcessLineList,
  reqAllProductFormulaProcessEnableList,
  reqAllRecordQueryProcessRecordVersion,
  reqGetDetailUsingGET,
  reqGetProcessModelUsingGET,
  reqProductMaterialProductTreeReq,
  reqRecordQueryProcessRecordVersionList,
} from '@/services';
import { FormProps, RenderCallbackParams, formInstance } from '@bmos/components';
import { t } from '@bmos/i18n';
import { Button, Tag, message } from 'ant-design-vue';
import { SelectValue } from 'ant-design-vue/es/select';
import { computed, ref, unref, watch } from 'vue';
import { PROCESS_STATE } from '../../enum';

export const FormFieldArrayItem = [
  'name',
  'productId',
  'version',
  'description',
  'productFormulaVersionId',
  'productionLineIds',
  'productionStageCode',
  'batchRecordItems',
];

export type LeftForm = ReturnType<typeof useLeftForm>;

export type UseLeftFormParams = {
  realVersion: ComputedRef<string>;
  realProcessId: ComputedRef<string>;
  status: Ref<string>;
  productId: Ref<string>;
  isSaveProcess: Ref<boolean>;
  realVersionId: Ref<string>;
};

export const useLeftForm = ({
  realVersion,
  realProcessId,
  productId,
  status,
  isSaveProcess,
  realVersionId,
}: UseLeftFormParams) => {
  const leftFormRef = ref<formInstance>();

  const productTree = ref<any[]>([]);
  const fetchProductOptionTree = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      // return data;
      // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
      const loop = (data: any[]) => {
        return data.map(item => {
          if (item.categoryFlag) {
            item.selectable = false;
          } else {
            item.selectable = true;
          }
          if (item.children) {
            loop(item.children);
          }
          return item;
        });
      };
      productTree.value = data;
      return loop(data);
    } catch (error) {
      //
    }
  };

  const formProps: Ref<FormProps> = ref({
    initialValues: {
      productId: productId.value || undefined,
    },
    layout: 'vertical',
    schemas: [
      {
        field: 'name',
        component: 'Input',
        label: t('工艺名称'),
        required: true,
        componentProps: {
          disabled: status.value === PROCESS_STATE.ADD_VERSION || status.value === PROCESS_STATE.EDIT_VERSION,
          onChange: () => {
            isSaveProcess.value = false;
          },
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            disabled: status.value === PROCESS_STATE.ADD_VERSION || status.value === PROCESS_STATE.EDIT_VERSION,
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              return await fetchProductOptionTree();
            },
            onSelect: (val: SelectValue) => {
              formModel.productFormulaVersionId = undefined;
              formModel.batchRecordItems = [
                {
                  batchRecordId: undefined,
                  batchRecordVersion: undefined,
                  batchRecordVersionId: undefined,
                },
              ];
              if (val) {
                getRecordList(val as string);
                getFormulaEnableList(val as string);
              }
            },
            onChange: () => {
              isSaveProcess.value = false;
            },
          };
        },
      },
      {
        field: 'version',
        component: 'Input',
        label: t('版本号'),
        required: true,
        componentProps: {
          onChange: () => {
            isSaveProcess.value = false;
          },
        },
      },
      {
        field: 'description',
        component: 'Input',
        label: t('版本描述'),
        componentProps: {
          onChange: () => {
            isSaveProcess.value = false;
          },
        },
      },
      {
        field: 'productFormulaVersionId',
        component: 'Select',
        label: t('产品生产BOM'),
        required: true,
        componentProps: {
          showSearch: true,
          filterOption: (input: string, option: any) => {
            return option.label?.toLowerCase().indexOf(input.toLowerCase()) >= 0;
          },
          options: [],
          onChange: () => {
            isSaveProcess.value = false;
          },
        },
      },
      {
        field: 'productionLineIds',
        component: 'TreeSelect',
        label: t('产线'),
        required: true,
        componentProps: {
          multiple: true,
          showSearch: true,
          options: [],
          treeNodeFilterProp: 'showName',
          fieldNames: {
            label: 'showName',
            value: 'id',
            children: 'children',
          },
          request: async () => {
            try {
              const { data } = await reqAllFactoryProcessLineList({
                ...(realVersionId.value && { processVersionId: realVersionId.value }),
              });
              return loopSelectableNotValueTree(data, 'lineFlag', true);
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            isSaveProcess.value = false;
          },
        },
        componentSlots: {
          tagRender: ({ slotData }: any) => {
            if (!slotData) return;
            const { label, closable, onClose, disabled } = slotData;
            return (
              <Tag closable={closable || disabled} class='dynamicSelectTag' onClose={onClose}>
                {label}
              </Tag>
            );
          },
        },
      },
      {
        field: 'productionStageCode',
        component: 'Input',
        label: t('生产阶段代码'),
        componentProps: {
          onChange: () => {
            isSaveProcess.value = false;
          },
        },
      },
      {
        field: 'batchRecordItems',
        formItemProps: {
          labelCol: { span: 24 },
        },
        labelFullWidth: true,
        disabledLabelWidth: true,
        label: ({ formModel }: RenderCallbackParams) => {
          return (
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                width: '100%',
              }}>
              <span>{t('批记录')}</span>
              <Button
                type='link'
                onClick={() => {
                  addRecord(formModel);
                }}>
                {t('添加批记录')}
              </Button>
            </div>
          );
        },
        slot: 'batchRecordItems',
        required: true,
        dynamicRules: ({ formModel }) => {
          return [
            {
              required: true,
              validator: async () => {
                // 如果批记录为空，报错
                if (formModel['batchRecordItems'].length === 0) {
                  return Promise.reject(t('请选择批记录'));
                }
                // 如果批记录中有 batchRecordId 空值，报错
                if (formModel['batchRecordItems'].some((it: any) => !it.batchRecordId)) {
                  return Promise.reject(t('请选择批记录'));
                }
                // 如果批记录中有 batchRecordVersionId 空值，报错
                if (formModel['batchRecordItems'].some((it: any) => !it.batchRecordVersionId)) {
                  return Promise.reject(t('请选择批记录版本'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
        defaultValue: [
          {
            batchRecordId: undefined,
            batchRecordVersion: undefined,
            batchRecordVersionId: undefined,
          },
        ],
      },
    ],
    labelWidth: 120,
    baseColProps: {
      span: 24,
    },
    showActionButtonGroup: false,
    showAdvancedButton: false,
  });

  const getFormulaEnableList = async (productId: string) => {
    try {
      const { data } = await reqAllProductFormulaProcessEnableList(productId, realVersionId.value);
      leftFormRef.value?.updateSchema({
        field: 'productFormulaVersionId',
        componentProps: {
          options: data.map((item: any) => {
            return {
              ...item,
              label: item.productFormulaName + '-' + item.productFormulaVersionNo,
              value: item.productFormulaVersionId,
            };
          }),
        },
      });
    } catch (error) {
      return [];
    }
  };

  const optionsRecordList = ref<any[]>([]);
  const recordFetching = ref<boolean>(false);
  const getRecordList = async (productId: string, batchRecordItems?: any) => {
    try {
      optionsRecordList.value = [];
      recordFetching.value = true;
      const { data } = await recordQueryListRecordByProductId(productId);
      optionsRecordList.value = data;
      if (batchRecordItems) {
        optionsRecordList.value = data.map((item: any) => {
          return {
            ...item,
            disabled: batchRecordItems.findIndex((it: any) => it.batchRecordId === item.value) > -1,
          };
        });
      }
      recordFetching.value = false;
    } catch (error) {
      recordFetching.value = false;
    }
  };
  const optionsRecordVersionList = ref<any[][]>([]);
  const getRecordVersionList = async (recordId: string, index: number) => {
    try {
      optionsRecordVersionList.value[index] = [];
      const { data } = await reqAllRecordQueryProcessRecordVersion(recordId, realVersionId.value);
      optionsRecordVersionList.value[index] = data;
    } catch (error) {
      optionsRecordVersionList.value[index] = [];
    }
  };

  const getRecordVersionLists = async (processVersionId: string, recordIdStr: string[]) => {
    try {
      optionsRecordVersionList.value = [];
      const { data } = await reqRecordQueryProcessRecordVersionList(processVersionId, recordIdStr.join(','));
      recordIdStr.forEach((recordId, index) => {
        optionsRecordVersionList.value[index] = data.find((item: any) => item.recordId === recordId)?.versionList;
      });
    } catch (error) {
      optionsRecordVersionList.value = [];
    }
  };

  const recordSelect = (val: SelectValue, recordListIds: any[], index: number, formModel: any) => {
    getRecordVersionList(val as string, index);
    // 对应的批记录版本清空
    formModel.batchRecordItems[index].batchRecordVersionId = undefined;
    // 已选择的批记录版本禁用
    optionsRecordList.value = unref(optionsRecordList).map((item: any) => {
      return {
        ...item,
        disabled: recordListIds.findIndex(it => it.batchRecordId === item.value) > -1,
      };
    });
  };

  const recordChange = (index: number, formModel: any, recordListIds: any[]) => {
    // 对应的批记录版本清空
    formModel.batchRecordItems[index].batchRecordVersionId = undefined;
    // 已选择的批记录版本禁用
    optionsRecordList.value = unref(optionsRecordList).map((item: any) => {
      return {
        ...item,
        disabled: recordListIds.findIndex(it => it.batchRecordId === item.value) > -1,
      };
    });
    isSaveProcess.value = false;
  };

  const filterOption = (input: string, option: any) => {
    return option.name.toLowerCase().indexOf(input.toLowerCase()) >= 0;
  };

  const removeRecord = (formModel: any, index: number) => {
    formModel.batchRecordItems.splice(index, 1);
    isSaveProcess.value = false;
    // 已选择的批记录版本禁用
    optionsRecordList.value = unref(optionsRecordList).map((item: any) => {
      return {
        ...item,
        disabled: formModel.batchRecordItems.findIndex((it: any) => it.batchRecordId === item.value) > -1,
      };
    });
  };

  const addRecord = (formModel: any) => {
    formModel.batchRecordItems.push({
      batchRecordId: undefined,
      batchRecordVersion: undefined,
      batchRecordVersionId: undefined,
    });
  };

  const versionChange = () => {
    isSaveProcess.value = false;
  };

  const versionSelect = (val: SelectValue, formModel: any, index: number) => {
    unref(optionsRecordVersionList)[index].forEach(item => {
      if (item.value === val) {
        formModel.batchRecordItems[index].batchRecordVersion = item.name;
      }
    });
    leftFormRef.value?.clearValidate('batchRecordItems');
  };

  const watchStatus = computed(() => {
    return (status.value || PROCESS_STATE.ADD_PROCESS) as string;
  });

  const modalJson = ref<any>([]);
  const isView = ref<boolean>(false);
  const detailProcedures = ref<any[]>([]);
  const spinning = ref<boolean>(false);
  // 原始modalJson
  const originalModalJson = ref<any>([]);
  const getProcessInfo = async (processId: number, version: number) => {
    try {
      spinning.value = true;
      const { data } = await reqGetDetailUsingGET({
        processId,
        version,
      } as unknown as API.MesProcessDetailReq);
      FormFieldArrayItem.forEach(item => {
        if (item === 'batchRecordItems') {
          getRecordVersionLists(
            realVersionId.value,
            data.batchRecordItems.map((it: any) => it.batchRecordId),
          );
        }
        leftFormRef.value?.setFormModel(item, data[item]);
        if (item === 'productId') {
          getRecordList(data[item], data.batchRecordItems);
          getFormulaEnableList(data[item]);
        }
      });
      const modalRes = await reqGetProcessModelUsingGET({
        processModelId: data.processModelId,
      });
      detailProcedures.value = data.procedures;
      originalModalJson.value = JSON.parse(modalRes.data);
      modalJson.value = JSON.parse(modalRes.data).map((item: any) => {
        const metaInfo = JSON.parse(item.metaInfo);
        const procedure = data.procedures.find((it: any) => it.nodeId === metaInfo.id);
        return {
          ...metaInfo,
          data: {
            ...metaInfo.data,
            ...procedure,
            ...(procedure?.name ? { label: procedure.name } : {}),
            ...(item.type.includes('gateway') && {
              gatewayType: item.type,
              conditionOnNodes: item.conditionOnNodes,
            }),
          },
        };
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      spinning.value = false;
    }
  };

  const statusWatchFun = async (val: string) => {
    switch (val) {
      case PROCESS_STATE.ADD_PROCESS:
        formProps.value.initialValues = {
          productId: productId.value || undefined,
        };
        if (productId.value) {
          getRecordList(productId.value as string);
          getFormulaEnableList(productId.value as string);
        }
        isView.value = false;
        break;
      case PROCESS_STATE.VIEW_VERSION:
        isView.value = true;
        formProps.value.disabled = true;
        await getProcessInfo(realProcessId.value as unknown as number, realVersion.value as unknown as number);
        break;
      case PROCESS_STATE.COPY_VERSION:
        isView.value = false;
        await getProcessInfo(realProcessId.value as unknown as number, realVersion.value as unknown as number);
        break;
      case PROCESS_STATE.ADD_VERSION:
        isView.value = false;
        await getProcessInfo(realProcessId.value as unknown as number, realVersion.value as unknown as number);
        break;
      case PROCESS_STATE.EDIT_VERSION:
        isView.value = false;
        await getProcessInfo(realProcessId.value as unknown as number, realVersion.value as unknown as number);
        break;
      default:
        isView.value = true;
        formProps.value.disabled = true;
        await getProcessInfo(realProcessId.value as unknown as number, realVersion.value as unknown as number);
        break;
    }
  };

  watch(
    () => watchStatus.value,
    async val => {
      await statusWatchFun(val);
    },
    {
      immediate: true,
      deep: true,
    },
  );

  return {
    leftFormRef,
    formProps,
    productTree,
    optionsRecordList,
    optionsRecordVersionList,
    modalJson,
    watchStatus,
    isView,
    removeRecord,
    addRecord,
    versionChange,
    versionSelect,
    recordSelect,
    getRecordList,
    recordFetching,
    detailProcedures,
    originalModalJson,
    getProcessInfo,
    filterOption,
    recordChange,
    spinning,
  };
};
