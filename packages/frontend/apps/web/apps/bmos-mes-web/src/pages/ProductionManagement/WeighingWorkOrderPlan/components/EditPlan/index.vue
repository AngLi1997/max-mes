<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb class="mes-breadcrumb">
        <breadcrumb-item @click="handleCancel">{{ t('称量工单规划') }}</breadcrumb-item>
        <breadcrumb-item>{{ isEdit ? t('编辑工单') : t('查看工单') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="handleCancel">{{ t('返回') }}</Button>
      <Button v-if="isEdit" :loading="saveLoading" type="primary" @click="handleSave">
        {{ t('保存') }}
      </Button>
    </template>
    <div class="detail-content">
      <BMForm ref="myFormRef" v-bind="formProps" />
      <div style="flex: 1; overflow: hidden; border-top: 1px solid #e1e3e5">
        <BMTable
          ref="tableRef"
          :scroll="{ x: 844, y: 400 }"
          :headerTitle="t('物料称量需求')"
          :search="false"
          :loading="loading"
          :columns="columns"
          row-key="id"
          :pagination="false"
          :data-source="tableData">
          <template #toolbar>
            <Button v-if="isEdit" type="primary" @click="openAddModal">
              {{ t('添加物料') }}
            </Button>
          </template>
        </BMTable>
      </div>
    </div>
  </BreadcrumbButton>
  <AddModal ref="addModalRef" @submit="addIdsChange" />
</template>

<script setup lang="tsx">
  import router from '@/router';
  import { BMForm, BMTable } from '@bmos/components';
  import { weighingWorkOrderPlanEdit } from '@/services';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { useForm, useTable } from './hooks';
  import { Button, message, Modal, Space } from 'ant-design-vue';
  import AddModal from './AddModal.vue';
  import { createVNode } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const route = useRoute();

  const isEdit = computed(() => {
    return route.query.type === 'edit';
  });

  const isEditData = ref(false);

  const back = () => {
    router.push({
      name: 'weighing-work-order-plan',
    });
  };

  // 取消
  const handleCancel = () => {
    if (!isEditData.value) return back();
    // 点击返回会给弹框提示
    Modal.confirm({
      title: t('提示'),
      wrapClassName: 'config-return-modal',
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否对该工单进行保存'),
      footer() {
        return (
          <>
            <Space class='footer-btns'>
              <Button onClick={() => Modal.destroyAll()}>{t('取消')}</Button>
              <Button
                onClick={() => {
                  Modal.destroyAll();
                  back();
                }}>
                {t('不保存')}
              </Button>
              <Button
                type='primary'
                onClick={() => {
                  Modal.destroyAll();
                  handleSave();
                }}>
                {t('保存')}
              </Button>
            </Space>
          </>
        );
      },
    });
  };

  const changeEditData = () => {
    isEditData.value = true;
  };

  const { myFormRef, formProps, setFormModels } = useForm(isEdit, changeEditData);

  const { columns, tableRef, tableData, loading, loadTableData, addRequirementIds, deleteRequirementIds } = useTable(
    isEdit,
    changeEditData,
  );

  const addModalRef = ref<InstanceType<typeof AddModal>>();

  const openAddModal = () => {
    addModalRef.value?.openModal(route.query, addRequirementIds.value, deleteRequirementIds.value);
  };

  const addIdsChange = (rows: any[]) => {
    if (!rows.length) return;
    rows.forEach((item: any) => {
      // tableData中存在则不添加
      if (tableData.value.some((tableItem: any) => tableItem.id === item.id)) return;
      tableData.value.push(item);
      if (deleteRequirementIds.value.includes(item.id)) {
        deleteRequirementIds.value = deleteRequirementIds.value.filter((id: any) => id !== item.id);
      } else {
        addRequirementIds.value.push(item.id);
      }
    });
  };

  const saveLoading = ref(false);

  const handleSave = async () => {
    // 保存
    try {
      const formModel = await myFormRef.value?.validate();
      saveLoading.value = true;
      await weighingWorkOrderPlanEdit({
        id: route.query.id as string,
        planDate: formModel.planDate,
        addRequirementIds: addRequirementIds.value,
        deleteRequirementIds: deleteRequirementIds.value,
      });
      message.success(t('操作成功'));
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      saveLoading.value = false;
    }
  };

  onMounted(async () => {
    if (!route.query?.id) return;
    setFormModels(route.query);
    await loadTableData(route.query.id as string);
  });
</script>

<style scoped lang="less">
  .detail-content {
    display: flex;
    flex-direction: column;
    gap: 16px;
    height: 100%;
    padding-bottom: 8px;
  }
</style>
