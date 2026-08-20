<template>
  <BMModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('已上传文件')"
    wrapClassName="modalSizeLarge"
    :formProps="formProps"
    :submit="submit"
    @cancelModal="closeModal">
    <template #formBefore>
      <div :style="{ height: 'auto' }">
        <BMTable
          ref="tableRef"
          :search="false"
          :data-source="tableData"
          :columns="columns"
          row-key="id"
          headerTitle=""
          :scroll="{ x: 844, y: 400 }"
          :showRefresh="false"
          show-index
          :pagination="false"></BMTable>
      </div>
    </template>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useTable, useForm } from './hooks';
  import { getMaterialUseSpotCheckPassDetail, materialUseSpotCheckPassUpdate } from '@/services';
  import { BMModalForm, BMTable } from '@bmos/components';
  import { useWarn } from '@/hooks';
  import { message } from 'ant-design-vue';

  const emits = defineEmits(['submitSuccess']);

  const open = ref(false);

  const { warnModal } = useWarn();

  const tableData = ref<any>([]);

  const deleteFile = (record: any) => {
    warnModal(t('是否删除该文件?'), {
      async onOk() {
        try {
          tableData.value = tableData.value.filter((item: any) => item.identify !== record.identify);
          setFormModels({
            fileList: tableData.value.map((item: any) => {
              return {
                uid: item.identify,
                name: item.fileName,
                status: 'done',
                url: item.fileSize,
                response: item,
              };
            }),
          });
          message.success(t('操作成功'));
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  const { tableRef, columns } = useTable(deleteFile);

  const changeFile = (info: any) => {
    if (info.file.status === 'done') {
      tableData.value = info.fileList
        .filter((file: any) => file.status === 'done')
        .map((item: any) => {
          return item.response;
        });
    } else if (['removed', 'error'].includes(info.file.status)) {
      setFormModels({
        fileList: tableData.value.map((item: any) => {
          return {
            uid: item.identify,
            name: item.fileName,
            status: 'done',
            url: item.fileSize,
            response: item,
          };
        }),
      });
    }
  };

  const { modalFormRef, formProps, setFormModels } = useForm(changeFile);

  const openModal = async (row: any) => {
    try {
      const { data } = await getMaterialUseSpotCheckPassDetail(row.useFormIdentify);
      tableData.value = data;
      const fileList = data.map((item: any) => {
        return {
          uid: item.identify,
          name: item.fileName,
          status: 'done',
          url: item.fileSize,
          response: item,
        };
      });
      open.value = true;
      await nextTick();
      setFormModels({
        useFormIdentify: row.useFormIdentify,
        fileList,
      });
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const submit = async (formModel: any) => {
    try {
      const params = {
        passFiles: tableData.value,
        inWarehouseNo: formModel.inWarehouseNo,
        useFormIdentify: formModel.useFormIdentify,
      };
      await materialUseSpotCheckPassUpdate(params);
      message.success(t('操作成功'));
      emits('submitSuccess');
      closeModal();
    } catch (error: any) {
      console.log('error', error);
      error.message && message.error(error.message);
    }
  };

  const closeModal = () => {
    open.value = false;
    tableData.value = [];
  };

  defineExpose({
    openModal,
    closeModal,
  });
</script>

<style lang="less" scoped>
  :deep(.delete-icon) {
    color: var(--bmos-danger-color);
  }
</style>
