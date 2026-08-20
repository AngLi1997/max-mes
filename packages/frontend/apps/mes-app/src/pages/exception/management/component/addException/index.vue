<template>
  <BMBasicPage
    v-show="showType === 'add' || showType === 'update'"
    :title="showType === 'add' ? t('新增异常') : t('异常编辑')"
    :default-padding="false"
    @left-click="toBack"
    @cancel="toBack"
    @confirm="confirm"
  >
    <BMForm
      ref="formRef"
      v-bind="formProps"
    />
  </BMBasicPage>
  <BMSignModal
    v-model:show="showSign"
    v-model="signValue"
    :signature-data="submitData"
    :label-list="labelList"
    :field-names="{
      label: 'userName',
      value: 'loginName',
      id: 'userId',
    }"
    @confirm="signConfirm"
  />
</template>

<script lang="ts" setup>
import {
  exceptionEdit,
  exceptionSave,
  getListByProcedureModelId,
  getListPlanByProcess,
  getRoomProcedureList,
  getVersionList,
  reqProcessListAll,

} from '@/api';
import { reqDictDownApi } from '@/api/webViewApi.js';
import {
  BMBasicPage,
  BMForm,
  BMSignModal,
} from '@/BMComponents';
import { timestampToTime } from '@/utils/time.js';
import { t } from '@/utils/useBmosI18n.js';
import { nextTick, onMounted, reactive, ref, watch } from 'vue';

const props = defineProps({
  showType: {
    type: String,
    default: 'index',
  },
  treeModalData: {
    type: Object,
    default: () => {},
  },
  rowData: {
    type: Object,
    default: () => {},
  },
});
const emit = defineEmits(['update:showType', 'submit']);
const formRef = ref();
const showSign = ref(false);
const signValue = ref({
  loginName1: '',
  password1: '',
  userId1: '',
});
const labelList = ref([
  {
    label: t('操作人'),
    // 签名动作
    signatureAction: 117,
    menuId: 121040001000007,
    currentUser: true,
  },
]);
const submitData = ref();
const exceptionTypeList = ref<any>({});

const getAllOptions = async () => {
  const { productId, processId, processVersion, procedureModelId }
    = props.rowData;
    // 获取所属工艺
  if (!productId) {
    return;
  }
  const { data: productId_options } = await reqProcessListAll({ productId });
  formRef.value?.updateSchema({
    field: 'processId',
    componentProps: {
      options: [...productId_options],
    },
  });
  // 获取工艺版本下拉
  if (!processId) {
    return;
  }
  const { data: processId_options } = await getVersionList(processId);
  formRef.value?.updateSchema({
    field: 'processVersion',
    componentProps: {
      options: [...processId_options],
    },
  });
  // 获取生产批次下拉
  if (!processId && !processVersion) {
    return;
  }
  const { data: productPlanIdOptions } = await getListPlanByProcess(
    processId,
    processVersion,
  );
  formRef.value?.updateSchema({
    field: 'productPlanId',
    componentProps: {
      options: [...productPlanIdOptions],
    },
  });
  // 获取所属工序下拉
  if (!processId && !processVersion) {
    return;
  }
  const { data: procedureModelId_options } = await getRoomProcedureList({
    processId,
    version: processVersion,
  });
  formRef.value?.updateSchema({
    field: 'procedureModelId',
    componentProps: {
      options: [...procedureModelId_options],
    },
  });
  // 获取所属工序步骤下拉
  if (!procedureModelId) {
    return;
  }
  const { data } = await getListByProcedureModelId(procedureModelId);
  formRef.value?.updateSchema({
    field: 'procedureStepModelId',
    componentProps: {
      options: [...data],
    },
  });
};
watch(
  () => props.showType,
  () => {
    if (props.showType === 'add') {
      formRef.value?.resetForm();
      formRef.value?.updateSchema({
        field: 'processId',
        componentProps: {
          options: [],
        },
      });
      formRef.value?.updateSchema({
        field: 'processVersion',
        componentProps: {
          options: [],
        },
      });
      formRef.value?.updateSchema({
        field: 'productPlanId',
        componentProps: {
          options: [],
        },
      });
      formRef.value?.updateSchema({
        field: 'procedureModelId',
        componentProps: {
          options: [],
        },
      });
      formRef.value?.updateSchema({
        field: 'procedureStepModelId',
        componentProps: {
          options: [],
        },
      });
    }
    else {
      formRef.value?.setFieldsValue({ ...props.rowData });
      nextTick(() => {
        getAllOptions();
      });
    }
    labelList.value[0].signatureAction = props.showType === 'add' ? 117 : 118;
    labelList.value[0].menuId = props.showType === 'add' ? 121040001000007 : 121040001000008;
  },
);

watch(
  () => props.treeModalData,
  () => {
    if (props.treeModalData.length > 0) {
      formRef.value?.updateSchema({
        field: 'productId',
        componentProps: {
          'tree-data': props.treeModalData,
        },
      });
    }
  },
);

const toBack = () => {
  emit('update:showType', 'index');
};
const confirm = async () => {
  // 表单校验
  formRef.value?.submit();
  const params = await formRef.value?.validate();
  submitData.value = { ...params };
  if (submitData.value.processVersion === '') {
    delete submitData.value.processVersion;
  }
  showSign.value = true;
};
const signConfirm = async () => {
  try {
    if (props.showType === 'add') {
      await exceptionSave({
        ...submitData.value,
        recordUserId: signValue.value.userId1,
        exceptionType:
            exceptionTypeList.value[submitData.value.exceptionTypeCode],
        recordTime: timestampToTime(submitData.value.recordTime),
      });
    }
    else {
      await exceptionEdit({
        ...submitData.value,
        editUserId: signValue.value.userId1,
        id: props.rowData.id,
        exceptionType:
            exceptionTypeList.value[submitData.value.exceptionTypeCode],
        recordTime: typeof submitData.value.recordTime == 'string' ? submitData.value.recordTime : timestampToTime(submitData.value.recordTime),
      });
    }
    showSign.value = false;
    toBack();
    emit('submit');
  }
  catch (error) {
    error.message
    && uni.showToast({
      title: error.message,
      icon: 'error',
      duration: 2000,
      mask: true,
    });
  }
};
const selectProduct = async (product) => {
  // 回显值
  formRef.value.setFieldsValue({
    processId: '',
    processVersion: '',
    productPlanId: '',
    procedureModelId: '',
    procedureStepModelId: '',
  });
  // 删除下拉框
  formRef.value?.updateSchema({
    field: 'processVersion',
    componentProps: {
      options: [],
    },
  });
  formRef.value?.updateSchema({
    field: 'productPlanId',
    componentProps: {
      options: [],
    },
  });
  formRef.value?.updateSchema({
    field: 'procedureModelId',
    componentProps: {
      options: [],
    },
  });
  formRef.value?.updateSchema({
    field: 'procedureStepModelId',
    componentProps: {
      options: [],
    },
  });
  if (!product?.showName) {
    formRef.value?.updateSchema({
      field: 'processId',
      componentProps: {
        options: [],
      },
    });
    return;
  }
  // 获取所属工艺
  const { data } = await reqProcessListAll({
    productId: product.id,
  });
  formRef.value?.updateSchema({
    field: 'processId',
    componentProps: {
      options: [...data],
    },
  });
};

onMounted(async () => {
  const { data } = await reqDictDownApi({ dictId: '120090001001' });
  data.forEach((item: any) => {
    exceptionTypeList.value[item.value] = item.label;
  });
  nextTick(() => {
    formRef.value?.updateSchema({
      field: 'exceptionTypeCode',
      componentProps: {
        options: [...data],
      },
    });
  });
});
// 表单配置
const formProps = reactive({
  schemas: [
    {
      field: 'formTitle1',
      component: 'FormTitle',
      label: t('生产信息'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'productId',
      component: 'BMFormSelect',
      label: t('产品信息'),
      colProps: {
        span: 12,
      },
      componentProps: () => {
        return {
          title: t('产品信息'),
          type: 'tree',
          fieldNames: {
            name: 'showName',
            key: 'id',
            checkKey: 'categoryFlag',
            checkKeyValue: true,
            parentId: 'parentId',
            children: 'children',
          },
          treeData: [],
          onConfirm: (data) => {
            selectProduct(data);
          },
          onClear: () => {
            formRef.value.setFieldsValue({
              processId: '',
              processVersion: '',
              productPlanId: '',
              procedureModelId: '',
              procedureStepModelId: '',
            });
            // 删除下拉框
            formRef.value?.updateSchema({
              field: 'processVersion',
              componentProps: {
                options: [],
              },
            });
            formRef.value?.updateSchema({
              field: 'productPlanId',
              componentProps: {
                options: [],
              },
            });
            formRef.value?.updateSchema({
              field: 'procedureModelId',
              componentProps: {
                options: [],
              },
            });
            formRef.value?.updateSchema({
              field: 'procedureStepModelId',
              componentProps: {
                options: [],
              },
            });
            formRef.value?.updateSchema({
              field: 'processId',
              componentProps: {
                options: [],
              },
            });
          },
        };
      },
    },
    {
      field: 'processId',
      component: 'BMFormSelect',
      label: t('所属工艺'),
      colProps: {
        span: 12,
      },
      componentProps: {
        placeholder: t('请选择所属工艺'),
        options: [],
        title: t('所属工艺'),
        fieldNames: {
          label: 'name',
          value: 'id',
        },
        onConfirm: async (value) => {
          // 重置数据
          formRef.value?.setFieldsValue({
            processVersion: '',
            productPlanId: '',
            procedureModelId: '',
            procedureStepModelId: '',
          });
          formRef.value?.updateSchema({
            field: 'procedureModelId',
            componentProps: {
              options: [],
            },
          });
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [],
            },
          });
          if (!value.id) {
            formRef.value?.updateSchema({
              field: 'processVersion',
              componentProps: {
                options: [],
              },
            });
            return;
          }
          // 获取工艺版本下拉
          const { data } = await getVersionList(value.id);
          formRef.value?.updateSchema({
            field: 'processVersion',
            componentProps: {
              options: [...data],
            },
          });
          // 获取生产批次下拉
          const { processVersion } = formRef.value?.getFormValues() || '';
          const { data: productPlanIdOptions } = await getListPlanByProcess(
            value.id,
            processVersion,
          );
          formRef.value?.updateSchema({
            field: 'productPlanId',
            componentProps: {
              options: [...productPlanIdOptions],
            },
          });
        },
        onChange: async () => {
          formRef.value?.setFieldsValue({
            processVersion: '',
            productPlanId: '',
            procedureModelId: '',
            procedureStepModelId: '',
          });
          formRef.value?.updateSchema({
            field: 'productPlanId',
            componentProps: {
              options: [],
            },
          });
          formRef.value?.updateSchema({
            field: 'procedureModelId',
            componentProps: {
              options: [],
            },
          });
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [],
            },
          });
          formRef.value?.updateSchema({
            field: 'processVersion',
            componentProps: {
              options: [],
            },
          });
        },
      },
    },
    {
      field: 'processVersion',
      component: 'BMFormSelect',
      label: t('工艺版本'),
      colProps: {
        span: 12,
      },
      componentProps: {
        options: [],
        placeholder: t('请选择工艺版本'),
        title: t('工艺版本'),
        fieldNames: {
          label: 'version',
          value: 'version',
        },
        onConfirm: async (value) => {
          // 重置数据
          formRef.value?.setFieldsValue({
            productPlanId: '',
            procedureModelId: '',
            procedureStepModelId: '',
          });
          formRef.value?.updateSchema({
            field: 'procedureModelId',
            componentProps: {
              options: [],
            },
          });
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [],
            },
          });
          const { processId } = formRef.value?.getFormValues() || '';
          // 获取生产批次下拉
          const { data: productPlanIdOptions } = await getListPlanByProcess(
            processId,
            value.version,
          );
          formRef.value?.updateSchema({
            field: 'productPlanId',
            componentProps: {
              options: [...productPlanIdOptions],
            },
          });
        },
        onChange: async () => {
          formRef.value?.setFieldsValue({
            productPlanId: '',
            procedureModelId: '',
            procedureStepModelId: '',
          });
          formRef.value?.updateSchema({
            field: 'procedureModelId',
            componentProps: {
              options: [],
            },
          });
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [],
            },
          });
        },
      },
    },
    {
      field: 'productPlanId',
      component: 'BMFormSelect',
      label: t('生产批次'),
      colProps: {
        span: 12,
      },
      componentProps: {
        options: [],
        placeholder: t('请选择生产批次'),
        title: t('生产批次'),
        fieldNames: {
          label: 'batchNo',
          value: 'id',
        },
        onConfirm: async (value) => {
          // 重置数据
          formRef.value?.setFieldsValue({
            procedureModelId: '',
            procedureStepModelId: '',
          });
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [],
            },
          });
          if (!value) {
            formRef.value?.updateSchema({
              field: 'procedureModelId',
              componentProps: {
                options: [],
              },
            });
            return;
          }
          // 获取所属工序下拉
          const { processId, processVersion }
              = formRef.value?.getFormValues() || '';
          const { data } = await getRoomProcedureList({
            processId,
            version: processVersion || value.processVersion,
          });
          formRef.value?.updateSchema({
            field: 'procedureModelId',
            componentProps: {
              options: [...data],
            },
          });
        },
        onChange: async (value) => {
          if (!value) {
            formRef.value?.setFieldsValue({
              procedureModelId: '',
              procedureStepModelId: '',
            });
            formRef.value?.updateSchema({
              field: 'procedureStepModelId',
              componentProps: {
                options: [],
              },
            });
            formRef.value?.updateSchema({
              field: 'procedureModelId',
              componentProps: {
                options: [],
              },
            });
          }
        },
      },
    },
    {
      field: 'procedureModelId',
      component: 'BMFormSelect',
      label: t('所属工序'),
      colProps: {
        span: 12,
      },
      componentProps: {
        placeholder: t('请选择所属工序'),
        options: [],
        title: t('所属工序'),
        fieldNames: {
          label: 'name',
          value: 'id',
        },
        onConfirm: async (value) => {
          // 重置数据
          formRef.value?.setFieldsValue({
            procedureStepModelId: '',
          });
          if (!value.id) {
            formRef.value?.updateSchema({
              field: 'procedureStepModelId',
              componentProps: {
                options: [],
              },
            });
            return;
          }
          // 获取所属工序步骤下拉
          const { data } = await getListByProcedureModelId(value.id);
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [...data],
            },
          });
        },
        onChange: async () => {
          formRef.value?.setFieldsValue({
            procedureStepModelId: '',
          });
          formRef.value?.updateSchema({
            field: 'procedureStepModelId',
            componentProps: {
              options: [],
            },
          });
        },
      },
    },
    {
      field: 'procedureStepModelId',
      component: 'BMFormSelect',
      label: t('所属工序步骤/任务'),
      colProps: {
        span: 12,
      },
      componentProps: {
        readonly: true,
        placeholder: t('请选择所属工序步骤/任务'),
        options: [],
        title: t('所属工序步骤/任务'),
        fieldNames: {
          label: 'name',
          value: 'id',
        },
      },
    },
    {
      field: 'formTitle2',
      component: 'FormTitle',
      label: t('异常信息'),
      colProps: {
        span: 24,
      },
    },
    {
      field: 'exceptionTypeCode',
      component: 'BMFormSelect',
      label: t('异常类型'),
      required: true,
      colProps: {
        span: 12,
      },
      componentProps: {
        title: t('异常类型'),
        placeholder: t('请选择异常类型'),
      },
    },
    {
      field: 'recordTime',
      component: 'BMFormDatePicker',
      label: t('记录时间'),
      required: true,
      colProps: {
        span: 12,
      },
      componentProps: {
        formatDate: 'yyyy-MM-dd HH:mm:ss',
      },
    },
    {
      field: 'exceptionDescription',
      component: 'Textarea',
      label: t('异常描述'),
      required: true,
      colProps: {
        span: 24,
      },
      componentProps: {
        placeholder: t('请输入异常描述'),
      },
    },
  ],
});
defineExpose({
  getAllOptions,
});
</script>

<style lang="scss" scoped>
  :deep(.bm-form) {
  width: 98.1% !important;
}
</style>
