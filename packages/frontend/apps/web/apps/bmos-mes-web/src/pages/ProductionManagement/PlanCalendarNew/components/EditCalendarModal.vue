<template>
  <!-- 编辑工艺或工序日历弹框 -->
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="modalTitle"
    :formProps="formProps"
    :cancelText="t('取消')"
    :okText="t('确定')"
    wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
      <Button type="primary" @click="ok">
        {{ t('确定') }}
      </Button>
    </template>
  </BMModalForm>
</template>
<script lang="tsx" setup>
  import { BMModalForm, ModalFormInstance } from '@bmos/components';
  import { reactive, ref } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Button } from 'ant-design-vue';
  import { reqProductionChangeCalendar } from '@/services';
  import dayjs from 'dayjs';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
    tableData: {
      type: Array,
      default: () => [] as any[],
    },
    modalTitle: {
      type: String,
      default: () => '',
    },
    currentProcessItem: {
      type: Object,
      default: () => {},
    },
    productionPlanItemId: {
      //指令单的productionPlanItemId
      type: String,
      default: () => '',
    },
    source: {
      //计划而来
      type: String,
      default: () => '',
    },
    procedureIndex: {
      //前端修改工序时存该工序的下标
      type: String || Number,
      default: () => '',
    },
  });

  const emit = defineEmits(['reqUpdateCalendar', 'planUpdateCalendar']);
  const modalFormRef = ref<ModalFormInstance>();
  const open = ref<boolean>(false);
  const openModal = () => {
    open.value = true;
  };
  const closeModal = () => {
    open.value = false;
  };
  // 编辑的表单
  const formProps = reactive<any>({
    initialValues: {
      // id:''
      //默认值
      // planNo: '999',
      // gender:1,
      // test: '',
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    labelCol: { span: 5 },
    wrapperCol: { span: 18 },
    schemas: [
      {
        field: 'startTime',
        component: 'DatePicker',
        label: t('计划开始日期'),
        required: true,
        componentProps: ({ formModel }: any) => {
          return {
            onChange: (date: any, data2: any) => {
              const temp = dayjs(props.rowData?.endTime).diff(dayjs(props.rowData?.startTime), 'day');
              formModel.endTime = dayjs(data2).add(
                props.rowData?.processIdItemDuration - 1 || props.rowData?.procedureItemDuration - 1 || temp,
                'day',
              );
            },
          };
        },
      },
      {
        field: 'whetherAdjust', //后续是否对应调整(新增计划调整时所需)
        component: 'RadioGroup',
        label: t(''),
        defaultValue: false,
        vIf: () => props.source === 'sourcePlan',
        componentProps: () => {
          return {
            options: [
              {
                label: props.modalTitle === t('工序计划日期调整') ? t('该批次后续工序不调整') : t('后续计划不调整'),
                value: false,
              },
              {
                label: props.modalTitle === t('工序计划日期调整') ? t('该批次后续工序相应调整') : t('后续计划相应调整'),
                value: true,
              },
            ],
          };
        },
      },
      {
        field: 'endTime',
        component: 'DatePicker',
        label: t('计划结束日期'),
        required: true,
      },
    ],
  });

  // 弹窗确定按钮
  const ok = async () => {
    const res: any = await modalFormRef.value?.validate();
    const { startTime, endTime } = res;
    if (dayjs(endTime).isBefore(dayjs(startTime))) {
      message.error(t('结束日期不能小于开始日期'));
      return;
    }
    if (props.source === 'sourcePlan') {
      // 前端修改
      emit('planUpdateCalendar', res, props.currentProcessItem, props.procedureIndex);
    } else {
      //后端修改
      try {
        const data = {
          ...res,
          productionPlanItemId: props.productionPlanItemId || props.currentProcessItem.id,
          procedureId: props.rowData?.procedureId,
        };
        await reqProductionChangeCalendar(data);
        message.success(t('操作成功'));
        emit('reqUpdateCalendar');
        open.value = false;
      } catch (error: any) {
        message.error(error.message);
      }
    }
  };

  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        //回显值
        modalFormRef.value?.formRef?.setFieldsValue({
          startTime: props.rowData.startTime,
          endTime: props.rowData.endTime,
        });
      }
    },
    { immediate: true },
  );

  defineExpose({ openModal, closeModal });
</script>
<style lang="less" scoped></style>
