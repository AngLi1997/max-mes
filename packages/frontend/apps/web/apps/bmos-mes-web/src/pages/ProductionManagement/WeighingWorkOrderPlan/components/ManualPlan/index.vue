<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb class="mes-breadcrumb">
        <breadcrumb-item @click="handleCancel">{{ t('称量工单规划') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('工单规划') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="handleCancel">{{ t('返回') }}</Button>
      <Button :loading="saveLoading" type="primary" @click="submit">
        {{ t('保存') }}
      </Button>
    </template>
    <BMTable
      ref="tableRef"
      :scroll="{ x: 844, y: 400 }"
      :header="false"
      :columns="columns"
      row-key="id"
      :pagination="false"
      :formProps="formProps"
      :row-selection="rowSelection"
      :data-request="reqWeighingWorkOrderPlanRequirementList"></BMTable>
  </BreadcrumbButton>
</template>

<script setup lang="tsx">
  import router from '@/router';
  import { BMTable, TableProps } from '@bmos/components';
  import { reqWeighingWorkOrderPlanRequirementList, weighingWorkOrderPlanManual } from '@/services';
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks';
  import { Button, message, Modal, Space } from 'ant-design-vue';
  import { createVNode } from 'vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  const { columns, tableRef, formProps } = useTable();

  const selectedRowKeys = ref<(string | number)[]>([]);
  const selectedRows = ref<any[]>([]);
  const rowSelection = computed<TableProps['rowSelection']>(() => {
    return {
      selectedRowKeys: selectedRowKeys.value,
      onChange: async (keys: (string | number)[], selectRows: any[]) => {
        selectedRowKeys.value = keys;
        selectedRows.value = selectRows;
      },
    };
  });

  const back = () => {
    router.push({
      name: 'weighing-work-order-plan',
    });
  };

  // 取消
  const handleCancel = () => {
    if (!selectedRowKeys.value.length) return back();
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

  const saveLoading = ref(false);

  const handleSave = async () => {
    if (!selectedRowKeys.value.length) {
      message.warning(t('请勾选物料需求进行规划'));
      return;
    }
    // 保存
    try {
      saveLoading.value = true;
      await weighingWorkOrderPlanManual(selectedRowKeys.value);
      message.success(t('操作成功'));
      back();
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      saveLoading.value = false;
    }
  };

  const submit = async () => {
    if (!selectedRowKeys.value.length) {
      message.warning(t('请勾选物料需求进行规划'));
      return;
    }
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否对所选称量需求进行称量工单规划?'),
      async onOk() {
        try {
          await handleSave();
          return Promise.resolve();
        } catch (error: any) {
          message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };
</script>

<style scoped lang="less"></style>
