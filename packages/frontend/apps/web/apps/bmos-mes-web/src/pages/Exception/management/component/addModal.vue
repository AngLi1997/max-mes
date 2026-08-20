<template>
  <div class="add_box">
    <BMModalForm
      ref="myFormRef"
      v-model:open="openAddModal"
      :title="isUpdate ? t('编辑异常') : t('新增异常')"
      :formProps="formProps"
      wrapClassName="modalSizeLarge inbound-model"
      :confirmLoading="loading">
      <template #footer>
        <Button @click="openAddModal = false">{{ t('取消') }}</Button>
        <Button type="primary" :loading="loading" @click="addExceptionSubmit">
          {{ t('确定') }}
        </Button>
      </template>
    </BMModalForm>
    <SignModal
      v-model:open="signOpen"
      :signatureData="JSON.stringify(curFormModal)"
      :labelList="labelList"
      @cancelModal="loading = false"
      @signSuccess="signSuccess"></SignModal>
  </div>
</template>
<script lang="ts" setup>
  import { t } from '@bmos/i18n';
  import { openAddModal, myFormRef } from '../hooks/datas';
  import { BMModalForm, FormProps } from '@bmos/components';
  import { ref } from 'vue';
  import SignModal from '@/components/SignModal';
  import { message } from 'ant-design-vue';
  import {
    reqProcessListAll,
    getVersionList,
    getListPlanByProcess,
    getProcedureList,
    getListByProcedureModelId,
    getDictNoRulesList,
    exceptionSave,
    exceptionEdit,
  } from '@/services';
  const signOpen = ref(false);
  const curFormModal = ref<any>({});
  const labelList = ref([
    {
      label: t('操作人'),
      disabled: false,
      menuId: 120090001000007,
      action: 117,
      currentUser: true,
    },
  ]);
  const exceptionTypeList = ref<any>({});
  const editData = ref();
  const loading = ref(false);

  const getAlloptions = async (row: any) => {
    editData.value = row;
    // 获取所属工艺
    if (!row.productId) {
      return;
    }
    const { data: processId_options } = await reqProcessListAll({
      productId: row.productId,
    });
    myFormRef.value?.formRef?.updateSchema({
      field: 'processId',
      componentProps: {
        options: [...processId_options],
      },
    });
    // 获取工艺版本下拉
    if (!row.processId) {
      return;
    }
    const { data: processVersion_options } = await getVersionList(row.processId);
    myFormRef.value?.formRef?.updateSchema({
      field: 'processVersion',
      componentProps: {
        options: [...processVersion_options],
      },
    });
    // 获取生产批次下拉
    if (!row.processId && !row.processVersion) {
      return;
    }
    const { data: productPlanIdOptions } = await getListPlanByProcess(row.processId, row.processVersion);
    myFormRef.value?.formRef?.updateSchema({
      field: 'productPlanId',
      componentProps: {
        options: [...productPlanIdOptions],
      },
    });
    // 获取所属工序下拉
    if (!row.processId && !row.processVersion) {
      return;
    }
    const { data: procedureModelId_options } = await getProcedureList({
      processId: row.processId,
      version: row.processVersion,
    });
    myFormRef.value?.formRef?.updateSchema({
      field: 'procedureModelId',
      componentProps: {
        options: [...procedureModelId_options],
      },
    });
    // 获取所属工序步骤下拉
    if (!row.procedureModelId) {
      return;
    }
    const { data: procedureStepId_options } = await getListByProcedureModelId(row.procedureModelId);
    myFormRef.value?.formRef?.updateSchema({
      field: 'procedureStepModelId',
      componentProps: {
        options: [...procedureStepId_options],
      },
    });
  };

  const props = withDefaults(
    defineProps<{
      isUpdate: boolean;
    }>(),
    {
      isUpdate: false,
    },
  );
  const emit = defineEmits(['submit']);

  // 新增异常确定
  const addExceptionSubmit = async () => {
    try {
      loading.value = true;
      // 表单校验
      myFormRef.value?.submit();
      const params = await myFormRef.value?.validate();
      // 打开签名弹窗
      labelList.value[0].action = props.isUpdate ? 118 : 117;
      labelList.value[0].menuId = props.isUpdate ? 120090001000008 : 120090001000007;
      curFormModal.value = params;
      signOpen.value = true;
    } catch (error) {
      loading.value = false;
      console.log('====新增异常', error);
    }
  };
  // 签名成功
  const signSuccess = async (value: any) => {
    try {
      if (props.isUpdate) {
        await exceptionEdit({
          ...curFormModal.value,
          editUserId: value.userId0,
          exceptionType: exceptionTypeList.value[curFormModal.value.exceptionTypeCode],
          id: editData.value.id,
        });
        message.success(t('编辑数据成功'));
      } else {
        await exceptionSave({
          ...curFormModal.value,
          recordUserId: value.userId0,
          exceptionType: exceptionTypeList.value[curFormModal.value.exceptionTypeCode],
        });
        message.success(t('新增数据成功'));
      }
      openAddModal.value = false;
      emit('submit');
    } catch (error: any) {
      message.error(error.message);
    } finally {
      loading.value = false;
    }
  };
  // 表单属性
  const formProps: Ref<FormProps> = ref({
    initialValues: {
      //默认值
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelWidth: 130,
    baseColProps: {
      span: 12,
    },
    schemas: [
      {
        field: 'productInfo',
        component: 'Divider',
        label: t('生产信息'),
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
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品信息'),
        componentProps: () => {
          return {
            treeData: [],
            fieldNames: {
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'label',
            onChange: async (value: any) => {
              myFormRef.value?.formRef?.setFieldsValue({
                processId: undefined,
                processVersion: undefined,
                productPlanId: undefined,
                procedureModelId: undefined,
                procedureStepModelId: undefined,
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'processVersion',
                componentProps: {
                  options: [],
                },
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureModelId',
                componentProps: {
                  options: [],
                },
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureStepModelId',
                componentProps: {
                  options: [],
                },
              });
              // 获取工艺版本下拉
              myFormRef.value?.formRef?.updateSchema({
                field: 'productPlanId',
                componentProps: {
                  options: [],
                },
              });
              if (!value) {
                myFormRef.value?.formRef?.updateSchema({
                  field: 'processId',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              // 获取所属工艺
              const { data } = await reqProcessListAll({
                productId: value,
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'processId',
                componentProps: {
                  options: [...data],
                },
              });
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('所属工艺'),
        componentProps: () => {
          return {
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            options: [],
            onChange: async (value: any) => {
              // 重置数据
              myFormRef.value?.formRef?.setFieldsValue({
                processVersion: undefined,
                productPlanId: undefined,
                procedureModelId: undefined,
                procedureStepModelId: undefined,
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'processVersion',
                componentProps: {
                  options: [],
                },
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureModelId',
                componentProps: {
                  options: [],
                },
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureStepModelId',
                componentProps: {
                  options: [],
                },
              });
              // 获取工艺版本下拉
              if (!value) {
                myFormRef.value?.formRef?.updateSchema({
                  field: 'processVersion',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              const { data } = await getVersionList(value);
              myFormRef.value?.formRef?.updateSchema({
                field: 'processVersion',
                componentProps: {
                  options: [...data],
                },
              });
              const { processVersion } = myFormRef.value?.getFormValues() || '';
              // 获取生产批次下拉
              const { data: productPlanIdOptions } = await getListPlanByProcess(value, processVersion);
              myFormRef.value?.formRef?.updateSchema({
                field: 'productPlanId',
                componentProps: {
                  options: [...productPlanIdOptions],
                },
              });
            },
          };
        },
      },
      {
        field: 'processVersion',
        component: 'Select',
        label: t('工艺版本'),
        componentProps: () => {
          return {
            options: [],
            fieldNames: {
              label: 'version',
              value: 'version',
            },
            onChange: async (value: any) => {
              // 重置数据
              myFormRef.value?.formRef?.setFieldsValue({
                productPlanId: undefined,
                procedureModelId: undefined,
                procedureStepModelId: undefined,
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureModelId',
                componentProps: {
                  options: [],
                },
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureStepModelId',
                componentProps: {
                  options: [],
                },
              });
              const { processId } = myFormRef.value?.getFormValues() || '';
              // 获取生产批次下拉
              const { data: productPlanIdOptions } = await getListPlanByProcess(processId, value);
              myFormRef.value?.formRef?.updateSchema({
                field: 'productPlanId',
                componentProps: {
                  options: [...productPlanIdOptions],
                },
              });
            },
          };
        },
      },
      {
        field: 'productPlanId',
        component: 'Select',
        label: t('生产批次'),
        componentProps: () => {
          return {
            options: [],
            fieldNames: {
              label: 'batchNo',
              value: 'id',
            },
            onChange: async value => {
              // 重置数据
              myFormRef.value?.formRef?.setFieldsValue({
                procedureModelId: undefined,
                procedureStepModelId: undefined,
              });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureStepModelId',
                componentProps: {
                  options: [],
                },
              });
              if (!value) {
                myFormRef.value?.formRef?.updateSchema({
                  field: 'procedureModelId',
                  componentProps: {
                    options: [],
                  },
                });
              }
              // 获取所属工序下拉
              const { processId, processVersion } = myFormRef.value?.getFormValues() || '';
              const { data } = await getProcedureList({ processId, version: processVersion });
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureModelId',
                componentProps: {
                  options: [...data],
                },
              });
            },
          };
        },
      },
      {
        field: 'procedureModelId',
        component: 'Select',
        label: t('所属工序'),
        componentProps: () => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
            onChange: async (val: any) => {
              myFormRef.value?.formRef?.setFieldsValue({
                procedureStepModelId: undefined,
              });
              if (!val) {
                // 重置数据
                myFormRef.value?.formRef?.updateSchema({
                  field: 'procedureStepModelId',
                  componentProps: {
                    options: [],
                  },
                });
                return;
              }
              // 获取所属工序步骤下拉
              const { data } = await getListByProcedureModelId(val);
              myFormRef.value?.formRef?.updateSchema({
                field: 'procedureStepModelId',
                componentProps: {
                  options: [...data],
                },
              });
            },
          };
        },
      },
      {
        field: 'procedureStepModelId',
        component: 'Select',
        label: t('所属工序步骤/任务'),
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: () => {
          return {
            options: [],
            fieldNames: {
              label: 'name',
              value: 'id',
            },
          };
        },
      },
      {
        field: 'exceptionInfo',
        component: 'Divider',
        label: t('异常信息'),
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
        field: 'exceptionTypeCode',
        component: 'Select',
        label: t('异常类型'),
        required: true,
        componentProps: () => {
          return {
            request: async () => {
              // 获取设备数据
              try {
                const { data } = await getDictNoRulesList({ dictId: '120090001001' });
                data.map((item: any) => {
                  exceptionTypeList.value[item.value] = item.label;
                });
                return data;
              } catch (error: any) {
                console.log('======异常类型', error);
              }
            },
          };
        },
      },
      {
        field: 'recordTime',
        label: t('记录时间'),
        required: true,
        component: 'DatePicker',
        componentProps: () => {
          return {
            showTime: true,
            showNow: true,
            format: 'YYYY-MM-DD HH:mm:ss',
            valueFormat: 'YYYY-MM-DD HH:mm:ss',
          };
        },
      },
      {
        field: 'exceptionDescription',
        component: 'InputTextArea',
        label: t('异常描述'),
        required: true,
        colProps: {
          style: {
            marginRight: 'auto',
          },
        },
      },
    ],
  });
  defineExpose({
    getAlloptions,
  });
</script>
