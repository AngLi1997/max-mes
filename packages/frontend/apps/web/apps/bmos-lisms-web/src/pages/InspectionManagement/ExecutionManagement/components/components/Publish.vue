<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="inspectItem.label + t('发布')"
    :formProps="formProps"
    wrapClassName="modalSizeExtraLarge"
    :getContainer="getContainer"
    :submit="submit">
    <template #formBefore>
      <div :style="{ height: tableData.length > 4 ? '40vh' : 'auto' }">
        <BMTable
          v-if="hasRequest"
          ref="tableRef"
          :search="false"
          :dataRequest="postInspectSingledataList"
          :columns="columns"
          :extraParams="{
            sampleBatchNo: props.sampleBatchNo,
            inspectItemCode: props.inspectItem.value,
            inspectDataStatus: 'TO_CHECK',
          }"
          row-key="id"
          :rowClassName="(record: any) => {
            return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row'
          }"
          :showToolBar="false"
          :scroll="{ x: 800, y: 400 }"></BMTable>
        <BMTable
          v-else
          ref="tableRef"
          :search="false"
          :data-source="tableData"
          :columns="columns"
          row-key="id"
          :rowClassName="(record: any) => {
            return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? 'unqualified-row' : 'qualified-row'
          }"
          :showToolBar="false"
          :scroll="{ x: 800, y: 400 }"></BMTable>
      </div>
    </template>
  </BMModalForm>
  <Sign ref="signRef" :signatureAction="1005" :afterSign="signSuccess" />
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, BMTable, TableColumn, RenderCallbackParams } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { InspectionProjectEnum, InspectionResultEnum } from '@/types';
  import {
    getLaboratoryInstrumentQueryInspect,
    getLaboratoryUseInventoryList,
    postInspectSingledataCheck,
    postLaboratoryUseQueryKey,
    postLaboratoryUseQueryLastQuality,
    reqPlatformUserListByMenuId,
    postInspectSingledataList,
  } from '@/services';
  import { useConfig } from '@/stores';
  import QualityControl from './QualityControl.vue';
  import { isEmpty } from '@bmos/utils';
  import { Sign } from '@/components/Sign';

  defineOptions({
    inheritAttrs: false,
  });

  const { controlStatusDict } = getDicts();

  const { getConfigEnumsValueByParamId } = useConfig();

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);
  const props = withDefaults(
    defineProps<{
      tableData?: Recordable[];
      inspectItem: Recordable;
      hasRequest?: boolean;
      sampleBatchNo?: string;
    }>(),
    {
      tableData: () => [],
      inspectItem: () => ({}),
      hasRequest: false,
      sampleBatchNo: '',
    },
  );

  // document 获取 bmos-page-component-container class 的节点
  const getContainer = (): HTMLElement => {
    return document.querySelector('.bmos-page-component-container') as unknown as HTMLElement;
  };

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();

  const qualityControlCountChange = async (
    value: number,
    formInstance: any,
    formModelData: any,
    defaultFlag: boolean,
  ) => {
    await nextTick();
    formInstance.clearValidate();
    formInstance.removeSchemaByFiled('qualityControlList', true);
    let showOriginValue = true;
    switch (props.inspectItem.value) {
      case InspectionProjectEnum.HBsAg:
      case InspectionProjectEnum.AntiHCV:
      case InspectionProjectEnum.HIVAgAb:
      case InspectionProjectEnum.AntiTP:
        showOriginValue = false;
    }
    // 添加质控品字段, 根据质控品个数动态添加
    formInstance.appendSchemasByField(
      Array.from({ length: value }).map((_, index: number) => {
        return {
          label: t('质控品'),
          field: `qualityControlList${index}`,
          vIf: ({ formModel }: RenderCallbackParams) => {
            return formModel.qualityControlCount > 0;
          },
          component: ({ formModel }: RenderCallbackParams) => {
            return (
              <>
                <QualityControl
                  v-model:qualityControl={formModel[`qualityControlList${index}`]}
                  qualityControlCount={formModel.qualityControlCount}
                  qualityControlCountList={formModel.qualityControlCountList}
                  showOriginValue={showOriginValue}
                />
              </>
            );
          },
          dynamicRules: ({ formModel }: RenderCallbackParams) => {
            return [
              {
                required: true,
                trigger: 'blur',
                validator: () => {
                  if (formModel.qualityControlCount === 0) {
                    return Promise.resolve();
                  }
                  if (formModel.qualityControlCount >= 1 && isEmpty(formModel[`qualityControlList${index}`]?.id)) {
                    return Promise.reject(t('请选择质控品'));
                  }
                  if (formModel.qualityControlCount > 1) {
                    if (isEmpty(formModel[`qualityControlList${index}`]?.type)) {
                      return Promise.reject(t('请选择质控品类型'));
                    }
                    if (isEmpty(formModel[`qualityControlList${index}`]?.originValue) && showOriginValue) {
                      return Promise.reject(t('请输入内置控值'));
                    }
                  }
                  return Promise.resolve();
                },
              },
            ];
          },
        };
      }),
      'qualityControlCount',
    );
    // 根据质控品个数动态添加质控品字段
    if (defaultFlag) {
      if (value > 1) {
        formModelData.qualityControlValueList?.forEach((item: any, index: number) => {
          const qualityControlItem = formModelData.qualityControlCountList?.find((qualityControlItem: any) => {
            return qualityControlItem.type === item.type;
          });
          if (qualityControlItem) {
            formInstance?.setFormModels({
              [`qualityControlList${index}`]: {
                id: qualityControlItem.value,
                type: undefined,
                originValue: item.value,
              },
            });
          } else {
            formInstance?.setFormModels({
              [`qualityControlList${index}`]: {
                id: undefined,
                type: undefined,
                originValue: item.value,
              },
            });
          }
        });
      } else {
        const defaultFlagItemArr = formModelData.qualityControlCountList?.filter((item: any) => item.defaultFlag);
        if (defaultFlagItemArr?.length) {
          defaultFlagItemArr?.forEach((item: any, index: number) => {
            formInstance?.setFormModels({
              [`qualityControlList${index}`]: {
                id: item.value,
                type: undefined,
                originValue: undefined,
              },
            });
          });
        } else {
          formInstance?.setFormModels({
            [`qualityControlList0`]: {
              id: undefined,
              type: undefined,
              originValue: undefined,
            },
          });
        }
      }
    } else {
      Array.from({ length: value }).forEach((_, index: number) => {
        formInstance?.setFormModels({
          [`qualityControlList${index}`]: {
            id: undefined,
            type: undefined,
            originValue: undefined,
          },
        });
      });
    }
  };

  const formProps = reactive<FormProps>({
    baseColProps: {
      span: 12,
    },
    labelWidth: 100,
    schemas: [
      {
        label: t('领用库'),
        field: 'warehouseNo',
        required: true,
        component: 'Select',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            request: async () => {
              const { data } = await getLaboratoryUseInventoryList({
                flag: 1,
                itemCode: props.inspectItem.value,
              });
              data.forEach((item: any) => {
                if (item.defaultFlag) {
                  formModel.warehouseNo = item.value;
                }
              });
              return data;
            },
            allowClear: false,
          };
        },
      },
      {
        label: t('复核人'),
        field: 'inspector',
        component: 'Select',
        required: true,
        vIf: props.inspectItem.value !== InspectionProjectEnum.ProteinElectrophoresis,
        componentProps: {
          request: async () => {
            try {
              let menuId = '210030004';
              switch (props.inspectItem.value) {
                case InspectionProjectEnum.ProteinContent:
                  menuId = '210030004';
                  break;
                case InspectionProjectEnum.ALT:
                  menuId = '210030005';
                  break;
                case InspectionProjectEnum.HBsAg:
                  menuId = '210030006';
                  break;
                case InspectionProjectEnum.AntiHCV:
                  menuId = '210030007';
                  break;
                case InspectionProjectEnum.HIVAgAb:
                  menuId = '210030008';
                  break;
                case InspectionProjectEnum.AntiTP:
                  menuId = '210030009';
                  break;
              }
              const { data } = await reqPlatformUserListByMenuId(menuId);
              return data.map((userItem: any) => {
                return {
                  label: userItem.userName + '-' + userItem.loginName,
                  value: userItem.userId,
                };
              });
            } catch (error) {}
          },
        },
        // dynamicRules: ({ formModel }: RenderCallbackParams) => {
        //   return [
        //     {
        //       validator: () => {
        //         if (props.tableData.includes(formModel['inspector'])) {
        //           return Promise.reject(t('复核人不能与检验人为同一人') + '！');
        //         }
        //         return Promise.resolve();
        //       },
        //     },
        //   ];
        // },
      },
      {
        label: t('试剂'),
        field: 'reagentId',
        required: true,
        component: 'Select',
        componentProps: ({ formModel, formInstance }: RenderCallbackParams) => {
          return {
            disabled: false,
            request: {
              watchFields: ['warehouseNo'],
              callback: async () => {
                formModel.reagentId = undefined;
                formModel.qualityControlCount = undefined;
                const { data } = await postLaboratoryUseQueryKey({
                  keyCategoryType: 'REAGENT',
                  warehouseNo: formModel.warehouseNo,
                  itemCode: props.inspectItem.value,
                });
                // const disabled = !data.length;
                // formInstance.updateSchema([
                //   {
                //     field: 'reagentId',
                //     componentProps: {
                //       disabled,
                //     },
                //   },
                //   {
                //     field: 'qualityControlCount',
                //     componentProps: {
                //       disabled,
                //     },
                //   },
                // ]);
                data.forEach((item: any) => {
                  if (item.defaultFlag) {
                    formModel.reagentId = item.value;
                  }
                });
                const { data: qualityControl } = await postLaboratoryUseQueryKey({
                  keyCategoryType: 'QUALITY_CONTROL',
                  warehouseNo: formModel.warehouseNo,
                  itemCode: props.inspectItem.value,
                });
                const { data: qualityControlValueList } = await postLaboratoryUseQueryLastQuality({
                  keyCategoryType: 'QUALITY_CONTROL',
                  warehouseNo: formModel.warehouseNo,
                  itemCode: props.inspectItem.value,
                });
                formModel.qualityControlCountList = qualityControl;
                formModel.qualityControlValueList = qualityControlValueList;
                if (formModel.maxQualityControlCount) {
                  formModel.qualityControlCount =
                    qualityControlValueList?.length > formModel.maxQualityControlCount
                      ? formModel.maxQualityControlCount
                      : qualityControlValueList?.length;
                } else {
                  formModel.qualityControlCount = qualityControlValueList?.length;
                }
                qualityControlCountChange(formModel.qualityControlCount, formInstance, formModel, true);
                return data;
              },
            },
          };
        },
      },
      {
        label: t('质控品个数'),
        field: 'qualityControlCount',
        required: true,
        component: 'Select',
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: ({ formInstance, formModel }: RenderCallbackParams) => {
          return {
            disabled: false,
            request: async () => {
              const data = await getConfigEnumsValueByParamId('质控品最大数量');
              formModel.maxQualityControlCount = data;
              // data 为 3 时，返回 [0, 1, 2, 3]
              return [...Array(Number(data) + 1).keys()].map(item => {
                return {
                  label: item.toString(),
                  value: item,
                };
              });
            },
            onChange: (value: number) => {
              qualityControlCountChange(value, formInstance, formModel, false);
            },
          };
        },
      },
      {
        label: t('是否在控'),
        field: 'inControl',
        required: true,
        component: 'Select',
        vIf: ({ formModel }: RenderCallbackParams) => {
          return formModel.qualityControlCount > 0;
        },
        defaultValue: 'Y',
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          options: controlStatusDict,
        },
      },
      {
        label: t('检验设备'),
        field: 'instrument',
        required: true,
        colProps: {
          span: 24,
        },
        component: 'Select',
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            request: async () => {
              const { data } = await getLaboratoryInstrumentQueryInspect({
                item: props.inspectItem.value,
              });
              data.forEach((item: any) => {
                if (item.defaultFlag) {
                  formModel.instrument = item.value;
                }
              });
              return data;
            },
          };
        },
      },
    ],
  });
  const { getDateFormat } = useConfig();
  const columns: TableColumn[] = [
    {
      title: t('检验项目'),
      dataIndex: 'inspectItemName',
      width: 200,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 200,
    },
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('检品状态'),
      dataIndex: ['testArticleStatus', 'label'],
      width: 120,
    },
    {
      title: t('检验次数'),
      dataIndex: ['inspectTimes', 'label'],
      width: 120,
    },
    {
      title: t('结果值'),
      dataIndex: 'inspectValue',
      width: 150,
      sorter: true,
      customRender: ({ record }) => {
        if (isEmpty(record.inspectValue)) return '-';
        switch (props.inspectItem.value) {
          case InspectionProjectEnum.HBsAg:
          case InspectionProjectEnum.AntiHCV:
          case InspectionProjectEnum.HIVAgAb:
          case InspectionProjectEnum.AntiTP:
            return record.inspectValue === '-' ? `-(${t('阴性')})` : `+(${t('阳性')})`;
          default:
            return record.inspectValue;
        }
      },
    },
    {
      title: t('检验结果'),
      dataIndex: ['inspectResult', 'label'],
      width: 120,
      customRender: ({ record }) => {
        if (isEmpty(record.inspectValue)) return '-';
        switch (props.inspectItem.value) {
          case InspectionProjectEnum.HBsAg:
          case InspectionProjectEnum.AntiHCV:
          case InspectionProjectEnum.HIVAgAb:
          case InspectionProjectEnum.AntiTP:
            return record.inspectValue === '-' ? `${t('阴性')}` : `${t('阳性')}`;
          default:
            return record.inspectResult?.label;
        }
      },
    },
    {
      title: t('检验人'),
      dataIndex: 'inspector',
      width: 120,
    },
    {
      title: t('检验日期'),
      dataIndex: 'inspectTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.inspectTime);
      },
    },
  ];

  const tableRef = ref<InstanceType<typeof BMTable>>();
  const submitParams = ref<any>({}); // 提交参数
  const signRef = ref<InstanceType<typeof Sign>>();
  const { updateTable } = inject('page') as { updateTable: () => void };
  const submit = async (formModal: Recordable) => {
    try {
      let qualityControlListMap = [];
      if (formModal.qualityControlCount > 1) {
        // 找出所有的 qualityControlList + index 的值
        qualityControlListMap = Array.from({ length: formModal.qualityControlCount }).map((_, index: number) => {
          return formModal[`qualityControlList${index}`];
        });
      }
      submitParams.value = {
        ...(props.hasRequest
          ? { sampleBatchNo: props.sampleBatchNo }
          : {
              sampleNos: props.tableData.map((item: any) => item.sampleNo),
              sampleNo: props.tableData.map((item: any) => item.sampleNo).join(','),
            }),
        inspectItemCode: props.inspectItem.value,
        reagentId: formModal.reagentId,
        instrument: formModal.instrument,
        inspector: formModal.inspector,
        warehouseNo: formModal.warehouseNo,
        ...(formModal.qualityControlCount > 0 && {
          inControl: formModal.inControl,
        }),
        ...(formModal.qualityControlCount === 1 && {
          qualityControlInfo: [
            {
              id: formModal.qualityControlList0.id,
            },
          ],
        }),
        ...(formModal.qualityControlCount > 1 && {
          qualityControlInfo: qualityControlListMap.map((item: any) => {
            return {
              id: item.id,
              type: item.type,
              typeName: item.typeName,
            };
          }),
          qualityControlValue: qualityControlListMap.map((item: any) => {
            return {
              type: item.type,
              typeName: item.typeName,
              originValue: item.originValue,
            };
          }),
        }),
      };
      if (props.inspectItem.value === InspectionProjectEnum.ProteinElectrophoresis) {
        await signRef.value?.openSign(submitParams.value);
      } else {
        await postInspectSingledataCheck({
          ...submitParams.value,
        });
        if (props.hasRequest) {
          updateTable();
        }
        emit('ok');
        message.success(t('操作成功'));
        open.value = false;
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await postInspectSingledataCheck({
        ...submitParams.value,
        checkUrl: signUrl,
      });
      if (props.hasRequest) {
        updateTable();
      }
      emit('ok');
      message.success(t('操作成功'));
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };
</script>

<style lang="less"></style>
