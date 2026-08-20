<!-- 详情 -->
<template>
  <Drawer
    v-model:open="open"
    :title="t('详情')"
    placement="right"
    width="900px"
    destroyOnClose
    @after-open-change="afterOpenChange">
    <BMForm ref="formRef" v-bind="formProps"></BMForm>
    <template #footer>
      <div class="my-drawer-footer">
        <Button style="margin-right: 8px" @click="open = false">{{ t('取消') }}</Button>
        <Button type="primary" @click="submit">{{ t('保存') }}</Button>
      </div>
    </template>
  </Drawer>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useForm } from './hooks';
  import { BMForm } from '@bmos/components';
  import { getReportTemplateDetailById, saveOrUpdateReportTemplateDetail } from '@/services';
  import { message } from 'ant-design-vue';

  const emit = defineEmits(['submitSuccess']);

  const { formRef, formProps, setFormModels, changeSchemas } = useForm();

  const open = ref<boolean>(false);

  const afterOpenChange = (bool: boolean) => {
    console.log('afterOpenChange', bool);
  };

  const reportType = ref<number>(1);

  const showDrawer = async (row: any, type: number) => {
    reportType.value = type;
    changeSchemas(type);
    open.value = true;
    await nextTick();
    try {
      const { data } = await getReportTemplateDetailById(row.id);
      setFormModels(data);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const submit = async () => {
    try {
      console.log(formRef.value.formModel);
      await saveOrUpdateReportTemplateDetail(formRef.value.formModel);
      message.success(t('操作成功'));
      emit('submitSuccess');
      open.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  defineExpose({ showDrawer });
</script>

<style lang="less" scoped>
  .my-drawer-footer {
    display: flex;
    justify-content: end;
    // flex-direction: row-reverse;
    align-items: center;
  }
</style>
