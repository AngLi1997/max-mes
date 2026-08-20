<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    wrapClassName="modalSizeLarge inbound-model"
    :submit="submit"></BMModalForm>
  <SignModal
    v-model:open="signOpen"
    :signatureData="JSON.stringify(curFormModal)"
    :labelList="labelList"
    @signSuccess="signSuccess"></SignModal>
</template>
<script lang="tsx" setup>
  import { BMModalForm, FormProps, ModalFormInstance, Recordable, RenderCallbackParams } from '@bmos/components';
  import { Col, FormItem, FormItemRest, InputGroup, InputNumber, Row, Select, Space, message } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import BMIcons from '@bmos/icons';
  import SignModal from '@/components/SignModal';
  import { LabelList } from '@/components/SignModal/type';
  import { useMaterialInfo } from '../hooks';
  import { StorageLevel } from '../types';
  import { cloneDeep, debounce, isEmpty } from '@bmos/utils';
  import {
    MaterialBatchListByMaterialId,
    reqMaterialBatchListByMaterialId,
    reqMaterialFieldInfo,
    reqStorageMaterialBatchFieldList,
    reqStorageMaterialMangeQueryBatchDetail,
    reqStorageMaterialReceiveMobile,
  } from '@/services';

  const emit = defineEmits<{
    (e: 'update:open', open: boolean): void;
    (e: 'updateTable'): void;
  }>();

  const props = withDefaults(
    defineProps<{
      open: boolean;
      rowData?: any;
      treeData?: Recordable;
      treeNode?: Recordable;
    }>(),
    {
      rowData: () => ({}),
      treeData: () => ({}),
      treeNode: () => ({}),
    },
  );

  const open = computed({
    get: () => {
      return props.open;
    },
    set: val => {
      emit('update:open', val);
    },
  });

  const title = ref<string>(t('物料接收'));

  const signOpen = ref<boolean>(false);
  const labelList: LabelList[] = [
    {
      label: t('接收人'),
      action: 109,
      disabled: true,
    },
    {
      label: t('递交人'),
      action: 110,
      menuId: '120030008000015',
    },
  ];

  const request = async () => {
    try {
      await reqStorageMaterialReceiveMobile(curFormModal.value);
      message.success(t('操作成功'));
      emit('updateTable');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const signSuccess = (data: Recordable) => {
    const { userId0, userId1 } = data;
    curFormModal.value = {
      ...curFormModal.value,
      receiverId: userId0,
      senderId: userId1,
    };
    request();
  };
  const curFormModal = ref<Recordable>({});
  const submit = async (formModal: Recordable) => {
    try {
      signOpen.value = true;
      curFormModal.value = formModal;
      return Promise.resolve(true);
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(false);
    }
  };

  const removeInboundInfo = (formModel: any, index: number) => {
    formModel['inboundList'].splice(index, 1);
  };
  const addInboundInfo = (formModel: any) => {
    // 最多10条
    if (formModel['inboundList'].length >= 10) {
      return message.error(t('最多添加10条入库信息'));
    }
    formModel['inboundList'].push({
      singleQuantity: undefined,
      unitId: undefined,
      unitExtendId: undefined,
      size: undefined,
    });
  };

  const modalFormRef = ref<ModalFormInstance>();
  const { setMaterialOptions, setCodeAndSpecification, unitOptions } = useMaterialInfo({
    modalFormRef,
  });

  const getBatchNumberOptions = debounce(async (value: string, materialId: string) => {
    try {
      if (!materialId) throw new Error(t('请选择物料名称'));
      const { data } = await reqMaterialBatchListByMaterialId(materialId, value);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialBatchNo',
        componentProps: {
          options: data.map((item: any) => {
            return {
              label: item.materialBatchNo,
              value: item.materialBatchNo,
              ...item,
            };
          }),
        },
      });
    } catch (error) {
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialBatchNo',
        componentProps: {
          options: [],
        },
      });
    }
  }, 500);

  const setBatchNoAndDateDisable = (flag: boolean = false) => {
    modalFormRef.value?.formRef?.updateSchema([
      {
        field: 'originalBatchNo',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'productDate',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'expiredDate',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'hydration',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'noHydrationContent',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'reportNo',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'licenceNo',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'originalCode',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'supplier',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'producer',
        componentProps: {
          disabled: flag,
        },
      },
      {
        field: 'qualityStatus',
        componentProps: {
          disabled: flag,
        },
      },
    ]);
  };

  const setSchemaListMargin = (schemaList: any) => {
    if (schemaList.length > 0) {
      return schemaList.map((item: any, index: number) => {
        // 奇数 设置 marginRight: 'auto', margin-left: 34px
        // 偶数 设置 margin-right: 34px
        if (index % 2 !== 0) {
          return {
            ...item,
            colProps: {
              style: {
                marginRight: '34px',
              },
            },
          };
        } else {
          return {
            ...item,
            colProps: {
              style: {
                marginRight: 'auto',
                marginLeft: '34px',
              },
            },
          };
        }
      });
    }
    return [];
  };

  const dyFieldList = ref([]);
  const getDynamicField = async (materialId?: string, batchNo?: string) => {
    try {
      modalFormRef.value?.formRef?.removeSchemaByFiled(dyFieldList.value.map((item: any) => item.field));
      await nextTick();
      if (isEmpty(materialId)) {
        return;
      }
      if (isEmpty(batchNo)) {
        modalFormRef.value?.formRef?.removeSchemaByFiled(dyFieldList.value.map((item: any) => item.field));
        const { data: materialFieldList } = await reqMaterialFieldInfo(materialId);
        dyFieldList.value = materialFieldList.filter((item: any) => item.fieldType === 'MaterialBatchCustomFields');
        const schemaList = materialFieldList
          .map((item: any) => {
            if (item.fieldType === 'MaterialBatchCustomFields') {
              return {
                field: item.field,
                component: 'Input',
                label: item.fieldName,
                defaultValue: '',
              };
            }
            return null;
          })
          .filter((item: any) => item);
        modalFormRef.value?.formRef?.setFormModels({
          originalBatchNo: undefined,
          productDate: undefined,
          expiredDate: undefined,
          hydration: undefined,
          noHydrationContent: undefined,
          reportNo: undefined,
          licenceNo: undefined,
          originalCode: undefined,
          supplier: undefined,
          producer: undefined,
        });
        modalFormRef.value?.formRef?.appendSchemasByField(setSchemaListMargin(schemaList), 'producer');
        return;
      }
      const { data: batchList } = await MaterialBatchListByMaterialId({
        materialId,
        batchNo,
      });
      const curBatch = batchList?.find((item: any) => item.materialBatchNo === batchNo);
      if (isEmpty(batchList) || !curBatch) {
        modalFormRef.value?.formRef?.removeSchemaByFiled(dyFieldList.value.map((item: any) => item.field));
        const { data: materialFieldList } = await reqMaterialFieldInfo(materialId);
        dyFieldList.value = materialFieldList.filter((item: any) => item.fieldType === 'MaterialBatchCustomFields');
        const schemaList = materialFieldList
          .map((item: any) => {
            if (item.fieldType === 'MaterialBatchCustomFields') {
              return {
                field: item.field,
                component: 'Input',
                label: item.fieldName,
                defaultValue: '',
              };
            } else {
              return null;
            }
          })
          .filter((item: any) => item);
        modalFormRef.value?.formRef?.appendSchemasByField(setSchemaListMargin(schemaList), 'producer');
        modalFormRef.value?.formRef?.setFormModels({
          originalBatchNo: undefined,
          productDate: undefined,
          expiredDate: undefined,
          hydration: undefined,
          noHydrationContent: undefined,
          reportNo: undefined,
          licenceNo: undefined,
          originalCode: undefined,
          supplier: undefined,
          producer: undefined,
          qualityStatus: 'QUARANTINE',
        });
      } else if (curBatch) {
        const { data } = await reqStorageMaterialMangeQueryBatchDetail({
          id: curBatch.id,
        });
        modalFormRef.value?.formRef?.setFieldsValue({
          originalBatchNo: data.factoryBatchNo,
          productDate: data.produceDate,
          expiredDate: data.expiredDate,
          hydration: data.hydration,
          noHydrationContent: data.noHydrationContent,
          reportNo: data.reportNo,
          licenceNo: data.licenceNo,
          originalCode: data.originalBatchNo,
          supplier: data.supplier,
          producer: data.producer,
          qualityStatus: data.qualityStatus.value,
        });
        modalFormRef.value?.formRef?.updateSchema([
          {
            field: 'originalBatchNo',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'productDate',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'expiredDate',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'hydration',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'noHydrationContent',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'reportNo',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'licenceNo',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'originalCode',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'supplier',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'producer',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'qualityStatus',
            componentProps: {
              disabled: data.qualityStatus ? true : false,
            },
          },
        ]);
        const { data: batchDynamicFieldList } = await reqStorageMaterialBatchFieldList(curBatch.id);
        dyFieldList.value = batchDynamicFieldList.filter((item: any) => item.fieldType === 'MaterialBatchCustomFields');
        const schemaList = batchDynamicFieldList
          .map((item: any) => {
            if (item.fieldType === 'MaterialBatchCustomFields') {
              return {
                field: item.field,
                component: 'Input',
                label: item.fieldName,
                defaultValue: item.fieldValue,
                componentProps: {
                  disabled: true,
                },
              };
            } else {
              return null;
            }
          })
          .filter((item: any) => item);
        modalFormRef.value?.formRef?.appendSchemasByField(setSchemaListMargin(schemaList), 'producer');
      }
    } catch (error) {
      //
    }
  };

  const formProps: Ref<FormProps> = ref({
    baseColProps: {
      span: 11,
    },
    // 处理日期为YYYY-MM-DD格式
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    schemas: [
      {
        field: 'materialInfo',
        component: 'Divider',
        label: t('物料信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'type',
        component: 'Select',
        label: t('物料类型'),
        required: true,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            options: [
              { label: t('原辅包'), value: 0 },
              { label: t('中间品'), value: 1 },
            ],
            onChange: (value: number) => {
              formInstance.setFieldsValue({
                materialId: undefined,
                code: undefined,
                specification: undefined,
                materialBatchNo: undefined,
                materialBatchNoId: undefined,
                originalBatchNo: undefined,
                productDate: undefined,
                expiredDate: undefined,
                hydration: undefined,
                noHydrationContent: undefined,
                reportNo: undefined,
                licenceNo: undefined,
                originalCode: undefined,
                supplier: undefined,
                producer: undefined,
                inboundList: [
                  {
                    singleQuantity: undefined,
                    unitId: undefined,
                    unitExtendId: undefined,
                    size: undefined,
                  },
                ],
              });
              formInstance.updateSchema({
                field: 'materialBatchNo',
                componentProps: {
                  options: [],
                },
              });
              setBatchNoAndDateDisable(false);
              setMaterialOptions(value);
              unitOptions.value = [];
            },
          };
        },
      },
      {
        field: 'materialId',
        component: 'TreeSelect',
        label: t('物料名称'),
        required: true,
        componentProps: ({ formInstance }: RenderCallbackParams) => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: (value: string) => {
              formInstance.setFieldsValue({
                code: undefined,
                specification: undefined,
                materialBatchNo: undefined,
                materialBatchNoId: undefined,
                originalBatchNo: undefined,
                productDate: undefined,
                expiredDate: undefined,
                hydration: undefined,
                noHydrationContent: undefined,
                reportNo: undefined,
                licenceNo: undefined,
                originalCode: undefined,
                supplier: undefined,
                producer: undefined,
                inboundList: [
                  {
                    singleQuantity: undefined,
                    unitId: undefined,
                    unitExtendId: undefined,
                    size: undefined,
                  },
                ],
              });
              formInstance.updateSchema({
                field: 'materialBatchNo',
                componentProps: {
                  options: [],
                },
              });
              setBatchNoAndDateDisable(false);
              setCodeAndSpecification(value);
              if (value) {
                getDynamicField(value);
              } else {
                getDynamicField();
              }
            },
          };
        },
      },
      {
        field: 'code',
        component: 'Input',
        label: t('物料编码'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'specification',
        component: 'Input',
        label: t('物料规格'),
        required: true,
        componentProps: {
          disabled: true,
        },
      },
      {
        field: 'materialBatchNo',
        component: 'AutoComplete',
        label: t('物料批号'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            options: [],
            onSearch: async (value: string) => {
              getBatchNumberOptions(value, formModel['materialId']);
            },
            onChange: () => {
              formModel['materialBatchNoId'] = undefined;
              setBatchNoAndDateDisable(false);
            },
            onSelect: (value: string, option: Recordable) => {
              setBatchNoAndDateDisable(true);
              formModel['materialBatchNoId'] = option.id;
              getDynamicField(formModel['materialId'], value);
            },
          };
        },
      },
      {
        field: 'qualityStatus',
        component: 'Select',
        label: t('质量状态'),
        defaultValue: 'QUARANTINE',
        required: true,
        componentProps: {
          options: [
            { label: t('待验'), value: 'QUARANTINE' },
            { label: t('合格'), value: 'QUALIFIED' },
            { label: t('不合格'), value: 'UNQUALIFIED' },
            { label: t('已取样'), value: 'SAMPLED' },
            { label: t('限制性放行'), value: 'RESTRICTED_RELEASE' },
          ],
        },
      },
      {
        field: 'productDate',
        component: 'DatePicker',
        label: t('生产日期'),
        componentProps: {
          disabledDate: (current: any) => {
            return current > Date.now();
          },
          style: {
            width: '100%',
          },
        },
      },
      {
        field: 'expiredDate',
        component: 'DatePicker',
        label: t('有效期至'),
        required: true,
        componentProps: {
          disabledDate: (current: any) => {
            return current && current < Date.now() - 86400000;
          },
          style: {
            width: '100%',
          },
        },
      },
      {
        field: 'originalBatchNo',
        component: 'Input',
        label: t('原厂批号'),
        componentProps: {
          disabled: false,
        },
      },
      {
        field: 'originalCode',
        component: 'Input',
        label: t('原始编码'),
      },
      {
        field: 'hydration',
        component: 'Input',
        label: t('水分') + '(%)',
        dynamicRules: () => {
          return [
            {
              required: false,
              validator: async (rule, value) => {
                if (isEmpty(value)) {
                  return Promise.resolve();
                }
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入为正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,3}(\.\d{1,4})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为3位,小数位数最多为4位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'noHydrationContent',
        component: 'Input',
        label: t('含量') + '(%)',
        dynamicRules: () => {
          return [
            {
              required: false,
              validator: async (rule, value) => {
                if (isEmpty(value)) {
                  return Promise.resolve();
                }
                if (isNaN(Number(value)) || Number(value) <= 0) {
                  return Promise.reject(t('请输入为正数'));
                }
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^-?\d{1,3}(\.\d{1,4})?$/;
                if (!reg.test(value)) {
                  return Promise.reject(t('整数部分最多为3位,小数位数最多为4位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'reportNo',
        component: 'Input',
        label: t('报告单编号'),
      },
      {
        field: 'licenceNo',
        component: 'Input',
        label: t('放行单编号'),
      },
      {
        field: 'supplier',
        component: 'Input',
        label: t('供应商'),
      },
      {
        field: 'producer',
        component: 'Input',
        label: t('生产商'),
      },
      {
        field: 'inboundInfo',
        component: 'Divider',
        label: t('入库信息'),
        colProps: {
          span: 24,
        },
        componentProps: {
          orientation: 'left',
          orientationMargin: '0px',
          showLeftBorder: true,
        },
      },
      {
        field: 'inboundList',
        label: t('test'),
        colProps: {
          span: 24,
        },
        noLabel: true,
        defaultValue: [
          {
            singleQuantity: undefined,
            unitId: undefined,
            unitExtendId: undefined,
            size: undefined,
          },
        ],
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              {formModel['inboundList'].map((_item: any, index: number) => {
                return (
                  <>
                    <Row>
                      <Col
                        span={12}
                        style={{
                          paddingRight: '16px',
                        }}>
                        <FormItem
                          name={['inboundList', index, 'singleQuantity']}
                          label={t('单件量')}
                          style={{ marginLeft: '34px' }}
                          labelCol={{
                            style: { width: '80px', textAlign: 'right' },
                          }}
                          wrapperCol={{
                            style: {
                              width: `calc(100% - 80px)`,
                            },
                          }}
                          required={true}
                          rules={[
                            {
                              required: true,
                              message: t('请输入单件量'),
                            },
                            {
                              trigger: 'blur',
                              validator: async (_rule: any, value: string) => {
                                if (!value) return Promise.resolve();
                                if (value === '0') {
                                  return Promise.reject(t('请输入为正数'));
                                }
                                // 如果值 整数或小数不能超过15位 则报错，否则通过
                                const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                                if (!reg.test(value)) {
                                  return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                                }
                                if (!formModel['inboundList'][index]['unitId']) {
                                  return Promise.reject(t('请选择单位'));
                                }
                                return Promise.resolve();
                              },
                            },
                          ]}>
                          <InputGroup compact>
                            <InputNumber
                              v-model:value={formModel['inboundList'][index]['singleQuantity']}
                              stringMode={true}
                              min={0}
                              style={{ width: '60%' }}
                              placeholder={t('请输入单件量')}
                            />
                            <FormItemRest>
                              <Select
                                v-model:value={formModel['inboundList'][index]['unitId']}
                                style={{ width: '40%' }}
                                fieldNames={{
                                  label: 'name',
                                  value: 'unitId',
                                }}
                                onChange={(value: any, option: any) => {
                                  if (!option.isUnit) {
                                    formModel['inboundList'][index]['unitExtendId'] = value;
                                  } else {
                                    formModel['inboundList'][index]['unitExtendId'] = undefined;
                                  }
                                  modalFormRef.value?.formRef?.validateFields([
                                    ['inboundList', index, 'singleQuantity'],
                                  ]);
                                }}
                                placeholder={t('请选择单位')}
                                dropdownMatchSelectWidth={300}
                                options={unitOptions.value}>
                                {{
                                  option: (option: any) => {
                                    return (
                                      <div class='flex-between'>
                                        <span>{option.name}</span>
                                        <span class='fourth-level-text'>{option.expression}</span>
                                      </div>
                                    );
                                  },
                                }}
                              </Select>
                            </FormItemRest>
                          </InputGroup>
                        </FormItem>
                      </Col>
                      <Col
                        span={12}
                        style={{
                          paddingRight: '8px',
                          paddingLeft: '8px',
                        }}>
                        <FormItem
                          name={['inboundList', index, 'size']}
                          label={t('入库件数')}
                          labelCol={{ style: { width: '80px' } }}
                          wrapperCol={{
                            style: {
                              width: `calc(100% - 80px)`,
                            },
                          }}
                          rules={[
                            {
                              required: true,
                              message: t('请输入入库件数'),
                            },
                          ]}>
                          <InputNumber
                            v-model:value={formModel['inboundList'][index]['size']}
                            precision={0}
                            max={99}
                            min={1}
                            style={{ width: '90%' }}
                            placeholder={t('请输入入库件数')}
                          />
                          {index > 0 && (
                            <BMIcons
                              style={{
                                cursor: 'pointer',
                                marginLeft: '8px',
                                width: '20px',
                                color: 'var(--bmos-danger-color)',
                              }}
                              icon='Delete'
                              onClick={() => removeInboundInfo(formModel, index)}
                            />
                          )}
                        </FormItem>
                      </Col>
                    </Row>
                  </>
                );
              })}
              {true && (
                <span>
                  <Space size={8}>
                    <span
                      style={{
                        color: `var(--bmos-primary-color)`,
                        marginLeft: '112px',
                        cursor: 'pointer',
                      }}
                      onClick={() => addInboundInfo(formModel)}>
                      {t('添加入库信息')}
                    </span>
                  </Space>
                </span>
              )}
            </>
          );
        },
      },

      {
        field: 'materialPositionId',
        component: 'TreeSelect',
        label: t('暂存货位'),
        componentProps: {
          fieldNames: {
            value: 'id',
            label: 'name',
          },
          showSearch: true,
          treeNodeFilterProp: 'name',
          dropdownMatchSelectWidth: 340,
        },
      },
      {
        field: 'linkExplain',
        component: 'InputTextArea',
        label: t('来源去向'),
        colProps: {
          span: 11,
        },
        required: true,
      },
    ],
  });

  // 监听 open
  watch(
    () => open.value,
    async val => {
      if (val) {
        await nextTick();
        modalFormRef.value?.formRef?.updateSchema([
          {
            field: 'materialPositionId',
            componentProps: {
              treeData: loopSelectableNotValueTree(
                cloneDeep(props.treeData?.[0]?.children as []) as Record<string, any>[],
                'level.value',
                StorageLevel.POSITION,
              ) as Record<string, any>[],
            },
          },
        ]);
        if (props.treeNode && props.treeNode?.id !== 'all' && props.treeNode?.level?.value === StorageLevel.POSITION) {
          modalFormRef.value?.formRef?.setFieldsValue({
            materialPositionId: props.treeNode.id,
          });
        }
        try {
        } catch (error) {}
      } else {
        unitOptions.value = [];
      }
    },
  );
</script>
