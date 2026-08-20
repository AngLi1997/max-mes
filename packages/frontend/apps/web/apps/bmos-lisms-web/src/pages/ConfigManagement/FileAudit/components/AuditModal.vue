<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('文件模板审核')"
    :formProps="formProps"
    wrapClassName="modalSizeExtraLarge"
    :submit="submit">
    <template #formBefore>
      <div :style="{ height: tableData.length > 4 ? '40vh' : 'auto' }">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="tableData"
          :columns="columns"
          row-key="id"
          :showToolBar="false"
          :scroll="{ x: 800, y: 400 }"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, FormProps, Recordable, BMTable, TableColumn } from '@bmos/components';
  import { message } from 'ant-design-vue';
  import { postConfigFileAudit } from '@/services';

  defineOptions({
    inheritAttrs: false,
  });

  const { auditResultDict } = getDicts();

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  const emit = defineEmits(['ok']);
  const props = withDefaults(
    defineProps<{
      tableData: Recordable[];
    }>(),
    {
      tableData: () => [],
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  const formProps = reactive<FormProps>({
    schemas: [
      {
        label: t('审核结果'),
        field: 'auditResult',
        required: true,
        component: 'Select',
        colProps: {
          span: 12,
          style: {
            marginRight: 'auto',
          },
        },
        componentProps: {
          options: auditResultDict,
        },
      },
      {
        label: t('备注'),
        field: 'reviewRemark',
        component: 'InputTextArea',
        colProps: {
          span: 24,
        },
      },
    ],
  });
  const { getDateFormat } = useConfig();
  const columns: TableColumn[] = [
    {
      title: t('文件模板编号'),
      dataIndex: 'templateNo',
      width: 160,
    },
    {
      title: t('文件名称'),
      dataIndex: 'templateName',
      width: 170,
    },
    {
      title: t('文件类型'),
      dataIndex: 'fileTypeName',
      width: 140,
    },
    {
      title: t('版本'),
      dataIndex: 'versionNumber',
      width: 100,
    },
    {
      title: t('文件编号'),
      dataIndex: 'standardNumber',
      width: 170,
    },
    {
      title: t('文件版本号'),
      dataIndex: 'buildNumber',
      width: 170,
    },
    {
      title: t('生效日期'),
      dataIndex: 'effectiveDate',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.effectiveDate);
      },
    },
    {
      title: t('提交人'),
      dataIndex: 'createBy',
      width: 140,
    },
    {
      title: t('提交日期'),
      dataIndex: 'createTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.createTime);
      },
    },
  ];

  const submit = async (formModal: Recordable) => {
    try {
      await postConfigFileAudit({
        auditIds: props.tableData.map((item: any) => item.auditId),
        ...formModal,
        templateNo: props.tableData.map((item: any) => item.templateNo).join(','),
      });
      emit('ok');
      message.success(t('操作成功'));
      open.value = false;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    }
  };
</script>

<style scoped lang="less"></style>
