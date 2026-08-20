<template>
  <!-- 编辑框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('查看详情')"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
  </BMModalForm>
</template>
<script lang="ts" setup>
  import { BMModalForm } from '@bmos/components';
  import { reactive, ref, nextTick, watch } from 'vue';
  import { t } from '@bmos/i18n';
  import { Button, message } from 'ant-design-vue';
  import { reqPlatformLogOperationDetailInfo, reqLimsLogDetail, reqWmsLogDetail, reqLismsLogDetail } from '@/services';
  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    treeNodeId: {
      type: String,
      default: () => '',
    },
  });
  const modalFormRef = ref<any>();
  const open = ref<boolean>(false);
  const openModal = () => {
    open.value = true;
  };
  // 查看的表单
  const formProps = reactive<any>({
    initialValues: {},
    disabled: true,
    transformDateFunc: (date: any) => {
      console.log(date, 'date');
      return date?.format?.('YYYY-MM-DD HH:mm:ss') ?? date;
    },
    labelCol: { span: 6 },
    wrapperCol: { span: 17 },
    schemas: [
      {
        field: 'operationType',
        label: t('操作类型'),
        component: 'Select',
        componentProps: {
          options: [
            {
              label: t('新增'),
              value: 0,
            },
            {
              label: t('编辑'),
              value: 1,
            },
            {
              label: t('删除'),
              value: 2,
            },
            {
              label: t('导出'),
              value: 3,
            },
            {
              label: t('关联'),
              value: 4,
            },
            {
              label: t('审核'),
              value: 5,
            },
          ],
        },
      },
      {
        field: 'operationBusiness',
        component: 'Input',
        label: t('业务操作'),
      },
      {
        field: 'userName',
        component: 'Input',
        label: t('操作人'),
      },
      {
        field: 'operationObject',
        component: 'InputTextArea',
        label: t('操作对象'),
      },
      {
        field: 'operationDetail',
        component: 'InputTextArea',
        label: t('操作详情'),
      },
      {
        field: 'operationTime',
        component: 'Input',
        label: t('操作时间'),
      },
      {
        field: 'remark',
        component: 'InputTextArea',
        label: t('备注'),
        componentProps: {
          placeholder: '',
        },
      },
    ],
  });

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        try {
          const data = { id: props.rowData.id };
          let res1: any;
          switch (props.treeNodeId.slice(0, 3)) {
            case '100':
              res1 = await reqPlatformLogOperationDetailInfo({ ...data, operationTime: props.rowData?.operationTime });
              break;
            case '120':
              res1 = await reqPlatformLogOperationDetailInfo({ ...data, operationTime: props.rowData?.operationTime });
              break;
            case '121':
              res1 = await reqPlatformLogOperationDetailInfo({ ...data, operationTime: props.rowData?.operationTime });
              break;
            case '130':
              res1 = await reqLimsLogDetail(data);
              break;
            case '150':
              res1 = await reqWmsLogDetail(data);
              break;
            case '210':
              res1 = await reqLismsLogDetail(data);
              break;
            default: //默认走调平台的接口
              res1 = await reqPlatformLogOperationDetailInfo({ ...data, operationTime: props.rowData?.operationTime });
              break;
          }
          modalFormRef.value?.formRef.setFieldsValue({
            ...props.rowData,
            operationType: props.rowData?.operationType?.value,
            operationObject: res1.data?.operationObject,
            operationDetail: res1.data?.operationDetail,
          }); //回显编辑框
        } catch (error: any) {
          message.error(error.message);
        }
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, formProps });
</script>
<style lang="less" scoped></style>
