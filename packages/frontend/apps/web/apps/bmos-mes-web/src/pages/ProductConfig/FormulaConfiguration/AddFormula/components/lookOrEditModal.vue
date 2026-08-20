<template>
  <!-- 编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="title"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeLarge"
    @cancel="cancel">
    <template #formBefore>
      <Segmented
        v-model:value="checkValue"
        block
        :options="[
          { label: t('物料信息'), value: 0 },
          { label: t('允差信息'), value: 1 },
          { label: t('称量需求'), value: 2 },
        ]" />
    </template>
    <template #footer>
      <Button @click="cancel">
        {{ type !== 'look' ? t('取消') : t('关闭') }}
      </Button>
      <Button v-if="type !== 'look'" type="primary" @click="ok">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, BMTable, TableColumn } from '@bmos/components';
  import { reactive, ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Button, Select, FormItemRest, InputNumber, Input, Modal, FormItem } from 'ant-design-vue';
  import ToleranceFormItem from './ToleranceFormItem.vue';
  import QuantityFormItem from './QuantityFormItem.vue';
  import DryPureFormItem from './DryPureFormItem.vue';
  import { loopTree } from '../utils';
  import {
    reqProductMaterialProductTreeReq,
    getProductMaterialDetailApi,
    getRoundingList,
    reqFormulaExtendUnit,
  } from '@/services';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    type: {
      type: String,
      default: '',
    },
    dataSource: {
      type: Array,
      default: () => [],
    },
    index: {
      type: [Number, String],
      default: '',
    },
  });

  const emits = defineEmits(['addTableData', 'updateTableData']);
  const modalFormRef = ref<any>();
  const open = ref<boolean>(false);
  const title = ref<any>(t('新增物料'));
  const unitOptions = ref<any>([]);
  // 当前物料信息
  const curMaterialInfo = ref<any>({});
  const unitName = ref<any>(''); //单位改变时候存单位name
  const defaultWeighingItem = (unitId: string) => ({
    requirementQuantity: undefined,
    unitId,
    requirementUsage: '',
  });

  const checkValue = ref<any>(0);

  const addWeighingDemand = (formModel: any) => {
    if (!formModel.weighRequirementList) {
      formModel.weighRequirementList = [];
    }
    formModel.weighRequirementList.push(defaultWeighingItem(formModel.unitId));
  };

  const removeWeighingDemand = (formModel: any, index: number) => {
    formModel.weighRequirementList.splice(index, 1);
  };

  const openModal = () => {
    open.value = true;
  };
  // 编辑的表单
  const formProps = reactive<any>({
    initialValues: {},
    disabled: false,
    baseColProps: {
      span: 11,
    },
    schemas: [
      // {
      //   field: 'materialInfo',
      //   component: 'Divider',
      //   label: t('物料信息'),
      //   colProps: {
      //     span: 24,
      //   },
      //   componentProps: {
      //     orientation: 'left',
      //     orientationMargin: '0px',
      //     showLeftBorder: true,
      //   },
      // },
      {
        field: 'materialType',
        component: 'Select',
        label: t('物料类型'),
        required: true,
        vShow: () => checkValue.value === 0,
        componentProps: ({ formInstance }: any) => {
          return {
            options: [
              { label: t('原辅包'), value: 0 },
              { label: t('中间品'), value: 1 },
            ],
            onChange: (value: number) => {
              formInstance.setFieldsValue({
                materialId: undefined,
                unitId: undefined,
              });
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
        vShow: () => checkValue.value === 0,
        componentProps: ({ formInstance }: any) => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: (value: string) => {
              formInstance.setFieldsValue({
                unitId: undefined,
                quantityType: undefined,
                quantity: undefined,
              });
              // 物料名称改变时候查单位
              setCodeAndSpecification(value);
            },
          };
        },
      },
      // 数量
      {
        field: 'sendBackList',
        noLabel: true,
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        vShow: () => checkValue.value === 0,
        component: ({ formModel }: any) => {
          return (
            <QuantityFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              quantityTypeField='quantityType'
              quantityValueField='quantity'
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      {
        field: 'unitId',
        label: t('单位'),
        required: true,
        vShow: () => checkValue.value === 0,
        component: 'Select',
        componentProps: ({ formModel }: any) => {
          return {
            fieldNames: {
              label: 'name',
              value: 'unitId',
            },
            dropdownMatchSelectWidth: 300,
            options: unitOptions.value,
            onChange: (value: any, option: any) => {
              if (formModel['unpackingToleranceType'] == 1) {
                formModel['unpackingToleranceUnit'] = option.name;
              }
              if (formModel['chargeMixtureToleranceType'] == 1) {
                formModel['chargeMixtureToleranceUnit'] = option.name;
              }
              if (formModel['oddmentToleranceType'] == 1) {
                formModel['oddmentToleranceUnit'] = option.name;
              }
              if (formModel['liquidMeasureToleranceType'] == 1) {
                formModel['liquidMeasureToleranceUnit'] = option.name;
              }
              if (formModel['oddLiquidMeasureToleranceType'] == 1) {
                formModel['oddLiquidMeasureToleranceUnit'] = option.name;
              }
              formModel.weighRequirementList?.forEach((item: any) => {
                item.unitId = value;
              });
              unitName.value = option.name;
              formModel['unitName'] = option.name;
            },
          };
        },
        componentSlots: {
          option: ({ slotData }: any) => {
            return (
              <div class='flex-between'>
                <span>{slotData.name}</span>
                <span class='fourth-level-text'>{slotData.expression}</span>
              </div>
            );
          },
        },
      },
      {
        field: 'scale',
        component: 'Input',
        label: t('物料精度'),
        componentProps: {
          style: {
            width: '100%',
          },
        },
        vShow: () => checkValue.value === 0,
        dynamicRules: () => {
          return [
            {
              required: true,
              trigger: 'blur',
              validator: (_rule: any, value: any) => {
                if (!value) return Promise.reject(t('请输入物料精度'));
                // 如果值 整数或小数不能超过15位 则报错，否则通过
                const reg = /^\d{1,10}$|^\d{1,10}[.]\d{1,9}$/;
                if (!reg.test(value) || Number(value) <= 0) {
                  return Promise.reject(t('正数,整数部分最多为10位,小数位数最多为9位'));
                }
                return Promise.resolve();
              },
            },
          ];
        },
      },
      {
        field: 'rounding',
        component: 'Select',
        label: t('修约方式'),
        required: true,
        vShow: () => checkValue.value === 0,
        componentProps: {
          options: [],
        },
      },
      // 折干折纯 dryPure
      {
        field: 'dryPureList',
        noLabel: true,
        colProps: {
          span: 11,
          style: {
            marginRight: 'auto',
            marginLeft: '30px',
          },
        },
        vShow: () => checkValue.value === 0,
        component: ({ formModel }: any) => {
          return (
            <DryPureFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              typeField='dryPureType'
              paramField='dryPureParam'
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      // {
      //   field: 'sendBackInfo',
      //   component: 'Divider',
      //   label: t('允差信息'),
      //   colProps: {
      //     span: 24,
      //   },
      //   componentProps: {
      //     orientation: 'left',
      //     orientationMargin: '0px',
      //     showLeftBorder: true,
      // },
      // },
      {
        field: 'unpackingList', //拆包允差
        noLabel: true,
        colProps: {
          span: 24,
        },
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        vShow: () => checkValue.value === 1,
        component: ({ formModel }: any) => {
          return (
            <ToleranceFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              label={t('拆包允差')}
              typeField='unpackingToleranceType'
              upperField='unpackingToleranceUpper'
              lowerField='unpackingToleranceLower'
              unitField='unpackingToleranceUnit'
              baseUnitName={formModel['unitName']}
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      {
        field: 'chargeMixtureList', //配料允差
        noLabel: true,
        colProps: {
          span: 24,
        },
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        vShow: () => checkValue.value === 1,
        component: ({ formModel }: any) => {
          return (
            <ToleranceFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              label={t('配料允差')}
              typeField='chargeMixtureToleranceType'
              upperField='chargeMixtureToleranceUpper'
              lowerField='chargeMixtureToleranceLower'
              unitField='chargeMixtureToleranceUnit'
              baseUnitName={formModel['unitName']}
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      {
        field: 'surplusMaterialList', //余料允差
        noLabel: true,
        colProps: {
          span: 24,
        },
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        vShow: () => checkValue.value === 1,
        component: ({ formModel }: any) => {
          return (
            <ToleranceFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              label={t('余料允差')}
              typeField='oddmentToleranceType'
              upperField='oddmentToleranceUpper'
              lowerField='oddmentToleranceLower'
              unitField='oddmentToleranceUnit'
              baseUnitName={formModel['unitName']}
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      {
        field: 'liquidMeasureList', //配液允差
        noLabel: true,
        colProps: {
          span: 24,
        },
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        vShow: () => checkValue.value === 1,
        component: ({ formModel }: any) => {
          return (
            <ToleranceFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              label={t('配液允差')}
              typeField='liquidMeasureToleranceType'
              upperField='liquidMeasureToleranceUpper'
              lowerField='liquidMeasureToleranceLower'
              unitField='liquidMeasureToleranceUnit'
              baseUnitName={formModel['unitName']}
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      {
        field: 'oddLiquidMeasureList', //余液允差oddLiquidMeasureToleranceType
        noLabel: true,
        colProps: {
          span: 24,
        },
        formItemProps: {
          style: {
            marginBottom: '-20px',
          },
        },
        vShow: () => checkValue.value === 1,
        component: ({ formModel }: any) => {
          return (
            <ToleranceFormItem
              modalFormRef={modalFormRef.value}
              formModel={formModel}
              label={t('余液允差')}
              typeField='oddLiquidMeasureToleranceType'
              upperField='oddLiquidMeasureToleranceUpper'
              lowerField='oddLiquidMeasureToleranceLower'
              unitField='oddLiquidMeasureToleranceUnit'
              baseUnitName={formModel['unitName']}
              viewMode={props.type === 'look'}
            />
          );
        },
      },
      //称量需求
      {
        field: 'weighRequirementList',
        label: t('称量需求列表'),
        noLabel: true,
        colProps: {
          span: 24,
        },
        vShow: () => checkValue.value === 2,
        component: ({ formModel }: any) => {
          const columns: TableColumn[] = [
            {
              title: t('需求目标量'),
              dataIndex: 'requirementQuantity',
              width: 180,
              customRender: ({ record, index }: any) => {
                return (
                  <FormItem
                    label=''
                    style={{ width: '100%', marginBottom: '0' }}
                    name={['weighRequirementList', index, 'requirementQuantity']}
                    required
                    rules={[
                      {
                        required: true,
                        validator: (_rule: any, value: any) => {
                          if (value === '' || value === undefined || value === null) {
                            return Promise.reject(t('请输入需求目标量'));
                          }
                          if (value > 0) {
                            const reg = /^-?\d{1,10}(\.\d{1,9})?$/;
                            if (!reg.test(value)) {
                              return Promise.reject(t('整数部分最多为10位,小数位数最多为9位'));
                            }
                            return Promise.resolve();
                          }
                          return Promise.reject(new Error(t('请输入正数')));
                        },
                        trigger: ['blur', 'change'],
                      },
                    ]}>
                    <InputNumber
                      v-model:value={record.requirementQuantity}
                      stringMode
                      placeholder={t('请输入')}
                      style={{ width: '100%' }}
                      disabled={props.type === 'look'}
                    />
                  </FormItem>
                );
              },
            },
            {
              title: t('单位'),
              dataIndex: 'unitId',
              key: 'unitId',
              width: 100,
              customRender: ({ record }: any) => {
                return (
                  <Select
                    v-model:value={record.unitId}
                    style={{ width: '100%' }}
                    fieldNames={{
                      label: 'name',
                      value: 'unitId',
                    }}
                    disabled
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
                );
              },
            },
            {
              title: t('需求用途'),
              dataIndex: 'requirementUsage',
              customRender: ({ record }: any) => {
                return (
                  <FormItem>
                    <Input
                      v-model:value={record.requirementUsage}
                      placeholder={t('请输入')}
                      disabled={props.type === 'look'}
                    />
                  </FormItem>
                );
              },
            },
            {
              title: t('操作'),
              key: 'ACTION',
              width: 80,
              fixed: 'right',
              hideInTable: props.type === 'look',
              actions: ({ record, index }: any) => [
                {
                  label: t('删除'),
                  danger: true,
                  onClick: () => {
                    console.log('record', index, record);
                    if (record.requirementQuantity) {
                      Modal.confirm({
                        title: t('是否删除该数据'),
                        icon: h(ExclamationCircleOutlined),
                        content: t('删除后无法恢复，是否删除？'),
                        onOk() {
                          try {
                            removeWeighingDemand(formModel, index);
                            message.success(t('删除成功'));
                            return Promise.resolve();
                          } catch (error: any) {
                            message.error(error);
                            return Promise.reject();
                          }
                        },
                        onCancel() {},
                      });
                    } else {
                      removeWeighingDemand(formModel, index);
                    }
                  },
                },
              ],
            },
          ];

          return (
            <div class='weigh-requirements-table' style={{ width: '100%' }}>
              <FormItemRest>
                <BMTable
                  showIndex
                  showToolBar={false}
                  search={false}
                  dataSource={formModel.weighRequirementList}
                  columns={columns}
                  pagination={false}
                  scroll={{ x: 300, y: 300 }}
                />
              </FormItemRest>
              {props.type !== 'look' && (
                <Button type='primary' onClick={() => addWeighingDemand(formModel)} style={{ marginTop: '12px' }}>
                  {t('新增数据')}
                </Button>
              )}
            </div>
          );
        },
      },
    ],
  });

  const cancel = () => {
    open.value = false;
    checkValue.value = 0;
  };

  // 物料类型改变时候查物料名称
  const setMaterialOptions = async (value: number) => {
    try {
      const { data } = await reqProductMaterialProductTreeReq(value);
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialId',
        componentProps: {
          treeData: loopTree(data) || [],
        },
      });
    } catch (error) {
      modalFormRef.value?.formRef?.updateSchema({
        field: 'materialId',
        componentProps: {
          treeData: [],
        },
      });
      unitOptions.value = [];
    }
  };

  // 物料名称改变时候查对应的单位
  const setCodeAndSpecification = async (value: string) => {
    unitOptions.value = [];
    try {
      if (!value) {
        unitOptions.value = [];
        return;
      }
      const { data } = await getProductMaterialDetailApi({
        id: value,
      });
      modalFormRef.value?.formRef?.setFormModel('materialName', data.name); //保存物料名称
      modalFormRef.value?.formRef?.setFormModel('materialMergeCode', data.mergeCode); //保存物料编码
      modalFormRef.value?.formRef?.setFormModel('materialSpecification', data.specification); //保存物料规格
      curMaterialInfo.value = data;
      getUnitOptions(value, data.unitId);
    } catch (error) {
      unitOptions.value = [];
    }
  };

  // 回显三类允差的允差上下限单位
  const getToleranceUnit = () => {
    if (props.rowData.unpackingToleranceType === 1) {
      modalFormRef.value?.formRef?.setFormModel('unpackingToleranceUnit', props.rowData.unitName); //拆包允差上下限单位
    }
    if (props.rowData.unpackingToleranceType === 0) {
      modalFormRef.value?.formRef?.setFormModel('unpackingToleranceUnit', '%');
    }
    if (props.rowData.chargeMixtureToleranceType === 1) {
      modalFormRef.value?.formRef?.setFormModel('chargeMixtureToleranceUnit', props.rowData.unitName); //配料允差上下限单位
    }
    if (props.rowData.chargeMixtureToleranceType === 0) {
      modalFormRef.value?.formRef?.setFormModel('chargeMixtureToleranceUnit', '%');
    }
    if (props.rowData.oddmentToleranceType === 1) {
      modalFormRef.value?.formRef?.setFormModel('oddmentToleranceUnit', props.rowData.unitName); //余料允差上下限单位
    }
    if (props.rowData.oddmentToleranceType === 0) {
      modalFormRef.value?.formRef?.setFormModel('oddmentToleranceUnit', '%');
    }
    if (props.rowData.liquidMeasureToleranceType === 1) {
      modalFormRef.value?.formRef?.setFormModel('liquidMeasureToleranceUnit', props.rowData.unitName); //配液允差上下限单位(单位待改)
    }
    if (props.rowData.liquidMeasureToleranceType === 0) {
      modalFormRef.value?.formRef?.setFormModel('liquidMeasureToleranceUnit', '%');
    }
    if (props.rowData.oddLiquidMeasureToleranceType === 1) {
      modalFormRef.value?.formRef?.setFormModel('oddLiquidMeasureToleranceUnit', props.rowData.unitName); //余料允差上下限单位
    }
    if (props.rowData.oddLiquidMeasureToleranceType === 0) {
      modalFormRef.value?.formRef?.setFormModel('oddLiquidMeasureToleranceUnit', '%');
    }
  };

  const getUnitOptions = async (unitId: string, unitId2: string) => {
    try {
      const extendRes = await reqFormulaExtendUnit({ materialId: unitId });
      (extendRes.data || []).forEach((item: any) => {
        item.unitId = item.id;
        item.name = item.extendUnitName;
      });
      unitOptions.value = [
        {
          unitId: unitId2,
          expression: t('标准单位'),
          name: curMaterialInfo.value.unitName,
          isUnit: true,
        },
        ...extendRes.data,
      ];
    } catch (error) {}
  };
  // 弹窗确定按钮
  const ok = async () => {
    if (props.type === 'look') {
      cancel();
      return;
    }
    try {
      const data: any = await modalFormRef.value?.validate();
      const weighRequirementList =
        data.weighRequirementList?.filter((item: any) => {
          return item.requirementQuantity > 0;
        }) || [];
      const params = {
        ...data,
        weighRequirementList,
      };
      let isMaterialNoExist;
      let isMaterialNoExist2;
      switch (props.type) {
        case 'add':
          // 校验生产BOM物料是否已存在
          isMaterialNoExist = props.dataSource.some((item: any) => {
            return item.materialMergeCode === params.materialMergeCode; //物料编码做唯一标识
          });
          if (isMaterialNoExist) {
            return message.error(t('生产BOM物料已存在'));
          }
          emits('addTableData', {
            ...params,
          });
          cancel();
          message.success(t('新增成功'));
          return Promise.resolve();
        case 'edit':
          // 校验生产BOM物料是否已存在
          isMaterialNoExist2 = props.dataSource.some((item: any) => {
            return (
              item.materialMergeCode === params.materialMergeCode &&
              item.materialMergeCode !== props.rowData?.materialMergeCode
            ); //物料编码做唯一标识
          });
          if (isMaterialNoExist2) {
            return message.error(t('生产BOM物料已存在'));
          }
          emits('updateTableData', params, props.index);
          cancel();
          message.success(t('编辑成功'));
          return Promise.resolve();
        default:
          cancel();
          return Promise.reject();
      }
    } catch (error: any) {
      if (error.errorFields?.length > 0) {
        console.log('error', error);
        const errorItem = error.errorFields[0];
        if (
          [
            'materialType',
            'materialId',
            'sendBackList',
            'quantity',
            'unitId',
            'scale',
            'rounding',
            'dryPureType',
          ].includes(errorItem.name?.[0])
        ) {
          checkValue.value = 0;
        } else if (errorItem.name?.[0] === 'weighRequirementList') {
          checkValue.value = 2;
        } else {
          checkValue.value = 1;
        }
      }
      error.message && message.error(error.message);
    }
  };

  const resetForm = () => {
    modalFormRef.value?.resetForm();
  };
  // 获取修约方式
  const getroundingList = async () => {
    try {
      const res: any = await getRoundingList({});
      const temp = res.data.map((item: any) => {
        return {
          ...item,
          label: t(item.label),
        };
      });
      modalFormRef.value?.formRef?.updateSchema({
        field: 'rounding',
        componentProps: {
          options: temp || [],
        },
      });
    } catch (error: any) {
      console.log(error);
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        getroundingList();
        switch (props.type) {
          case 'add':
            title.value = t('新增物料');
            formProps.disabled = false;
            unitOptions.value = [];
            // 默认的修约方式与折干折纯
            modalFormRef.value?.formRef?.setFormModels({
              rounding: 'roundingSix',
              dryPureType: 0,
            });
            break;
          case 'edit':
            title.value = t('编辑物料');
            modalFormRef.value?.formRef?.setFormModels({
              ...props.rowData,
              quantity: props.rowData.quantity == 0 ? undefined : props.rowData.quantity,
            });
            formProps.disabled = false;
            setMaterialOptions(props.rowData.materialType); //回显物料名称
            setCodeAndSpecification(props.rowData.materialId); //物料名称查单位
            getToleranceUnit(); //回显三类允差的允差上下限单位
            break;
          case 'look':
            title.value = t('查看物料');
            modalFormRef.value?.formRef?.setFormProps({
              disabled: true,
            });
            modalFormRef.value?.formRef?.setFormModels({
              ...props.rowData,
              quantity: props.rowData.quantity == 0 ? undefined : props.rowData.quantity,
            });
            setMaterialOptions(props.rowData.materialType); //回显物料名称
            setCodeAndSpecification(props.rowData.materialId); //物料名称查单位
            getToleranceUnit(); //回显三类允差的允差上下限单位

            break;
          default:
            break;
        }
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, resetForm });
</script>
<style lang="less">
  .weigh-requirements-table {
    // .mes-table-tbody > tr.mes-table-measure-row + tr > td {
    //   padding-top: 4px;
    //   padding-bottom: 4px;
    //   vertical-align: top;
    //   &:first-child {
    //     vertical-align: revert;
    //   }
    // }
    .mes-form-item {
      padding: 10px 0;
      margin-bottom: 0;
    }
  }
</style>
