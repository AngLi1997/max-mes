<!-- 企业不合格血浆管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showHeader="[false]"
    :showToolBars="[true]"
    :rowSelections="rowSelections"
    :formProps="[formFirstProps]"
    :paginations="[paginationBig]"
    :requests="[getEnterpriseUnqualifiedPlasmaList as DataRequestFn]"
    :columns="[columnsFirst]">
    <template #tableHeaderTitle0="{ instance }">
      <div>
        <Button
          v-hasAuth="170070002000001"
          style="margin-left: 8px"
          :loading="loading"
          @click="exportExcel(instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel))">
          {{ t('导出') }}
        </Button>
      </div>
    </template>
    <template #tableHeaderToolbar0>
      <div>
        <Button
          v-hasAuth="170070002000003"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0"
          @click="putRecord(operationSelectedRow)">
          {{ t('出具不合格记录') }}
        </Button>
        <Button
          v-hasAuth="170070002000004"
          style="margin-left: 8px"
          :disabled="rowSelections[0]?.selectedRowKeys.length === 0 || !isNull(operationSelectedRow?.draftFlag)"
          @click="openCreateReport(operationSelectedRow)">
          {{ t('出具不合格血浆核查报告') }}
        </Button>
      </div>
    </template>
  </BMPageComponent>
  <!-- 查看影响血浆 -->
  <ViewEffectPlasma ref="viewEffectPlasmaRef" />
  <!-- 出具不合格血浆核查报告 -->
  <CreateReport ref="createReportRef" @submitSuccess="submitSuccess" />
  <Sign ref="signRef" :signatureAction="907" :afterSign="signSuccess" />
</template>

<script setup lang="ts">
  import {
    getEnterpriseUnqualifiedPlasmaList,
    unqualifiedPlasmaIssue,
    exportEnterpriseUnqualifiedPlasma,
  } from '@/services';
  import { useTable } from './hooks';
  import { CreateReport, ViewEffectPlasma } from './components';
  import { Sign } from '@/components/Sign';
  import { paginationBig } from '@/utils/paginationConfig';
  import { DataRequestFn, BMPageComponent } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Modal, message } from 'ant-design-vue';
  import { fileStreamDownload } from '@bmos/utils';
  import { isNull } from '@bmos/utils';

  defineOptions({
    name: 'EnterprisePlasmaMng',
  });

  const viewEffectPlasmaRef = ref();

  const openViewEffectPlasmaModal = async (row: any) => {
    await viewEffectPlasmaRef.value.openModal(row.id);
  };

  // 出具不合格血浆核查报告
  const createReportRef = ref();

  const openCreateReport = (row: any, type: 'create' | 'edit' = 'create') => {
    if (type === 'edit') {
      createReportRef.value.openModal(row, type);
      return;
    }
    Modal.confirm({
      title: t('出具不合格血浆核查报告'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否出具不合格血浆核查报告，确认后生成《不合格血浆核查报告》'),
      style: {
        width: '530px',
      },
      async onOk() {
        try {
          // await unqualifiedPlasmaIssue(row.batchNo);
          createReportRef.value.openModal(row, type);
          // message.success(t('操作成功'));
          // await submitSuccess();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  const { pageRef, columnsFirst, formFirstProps } = useTable(openViewEffectPlasmaModal, openCreateReport);

  const downloadFn = (data: any, fileName: string) => {
    try {
      const uint8Array = new Uint8Array(data);
      const decoder = new TextDecoder();
      const jsonString = decoder.decode(uint8Array);
      const error = JSON.parse(jsonString);
      error.message && message.error(error.message);
    } catch (error) {
      fileStreamDownload(data, fileName);
    }
  };

  const loading = ref(false);
  const exportExcel = async (data: any) => {
    try {
      loading.value = true;
      const res = await exportEnterpriseUnqualifiedPlasma(data);
      let fileName = res.headers['content-disposition']?.split("filename*=utf-8''")[1];
      // 文件名解码
      fileName && fileName.indexOf('%') > -1 && (fileName = decodeURI(fileName));
      downloadFn(res.data, fileName);
    } finally {
      loading.value = false;
    }
  };

  // 选中的数据
  const operationSelectedRow = ref<any>({});

  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: true,
      columnWidth: 50,
      fixed: true,
      selectedRowKeys: [] as any[],
      preserveSelectedRowKeys: true,
      getCheckboxProps: (_record: any) => {
        return {
          disabled: false,
        };
      },
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys?.length
            ? [selectedRowKeys[selectedRowKeys.length - 1]]
            : [];
          operationSelectedRow.value = selectedRows[selectedRows.length - 1];
        }
      },
    },
    null,
  ]);

  // ----------------签名相关-----------------
  const signRef = ref();

  // 出具不合格记录
  const putRecord = async (row: any) => {
    Modal.confirm({
      title: t('出具不合格血浆核查记录'),
      icon: h(ExclamationCircleOutlined),
      content: t('是否出具不合格血浆核查记录，确认后生成《不合格血浆核查记录》'),
      style: {
        width: '530px',
      },
      async onOk() {
        try {
          // await plasmaInWarehouseIn(row.batchNo);
          await signRef.value.openSign([
            {
              reportId: row.id,
              reportName: t('不合格血浆核查记录'),
              reportNo: row.reportNo,
            },
          ]);
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  const signSuccess = async (signUrl: string) => {
    try {
      await unqualifiedPlasmaIssue({
        id: operationSelectedRow.value.id,
        createSignature: signUrl,
      });
      message.success(t('操作成功'));
      submitSuccess();
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const submitSuccess = () => {
    if (rowSelections[0]?.selectedRowKeys) {
      rowSelections[0].selectedRowKeys = [];
      operationSelectedRow.value = {};
    }
    pageRef.value?.fetchData();
  };
</script>

<style lang="less" scoped></style>
