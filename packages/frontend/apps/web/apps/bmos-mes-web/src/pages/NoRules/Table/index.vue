<template>
  <div class="container2">
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      row-key="id"
      auto-height
      :autoHeightOffset="24"
      headerTitle=""
      :scroll="{ x: 844, y: 400 }"
      :extraParams="{
        type,
      }"
      :formProps="formProps">
      <template #toolbar>
        <Button v-hasAuth="120020009000002" @click="BatchConfiguration">{{ t('批量配置') }}</Button>
      </template>
    </BMTable>
    <!-- 编辑弹框  -->
    <editRules ref="editRulesRef" :rowData="rowData" :type="type" @updateTableData="updateTableData"></editRules>
    <!-- 批量配置弹框  -->
    <batchConfiguration
      ref="batchConfigurationRef"
      :rowData="rowData"
      :type="type"
      @updateTableData="updateTableData"></batchConfiguration>
  </div>
</template>
<script lang="tsx" setup>
  import type { DataRequestFn, FormProps } from '@bmos/components';
  import { BMTable, BMEllipsis, TableColumn } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { reactive, ref, onMounted } from 'vue';
  import { getNoRulesPage } from '@/services';
  import { Button } from 'ant-design-vue';
  import editRules from './editRules.vue';
  import batchConfiguration from './batchConfiguration.vue';
  const tableInstance = ref<any>();
  const props = defineProps({
    activeKey: {
      type: String || undefined,
      default: '',
    },
  });
  const type = computed(() => {
    return props.activeKey;
  });
  const rowData = ref();
  const editRulesRef = ref();
  const batchConfigurationRef = ref();
  const formProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 6,
    },
    // 是否展示更多
    showAdvancedButton: false,
    // 是否显示操作按钮
    showActionButtonGroup: true,
  });
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return getNoRulesPage(params);
  };

  const columns = ref<TableColumn[]>([
    {
      title: t('产品名称'),
      align: 'left',
      dataIndex: 'productName',
      resizable: true,
    },
    {
      title: t('产品编码'),
      align: 'left',
      dataIndex: 'productCode',
      resizable: true,
    },
    {
      title: t('工艺名称'),
      align: 'left',
      dataIndex: 'processName',
      resizable: true,
    },
    {
      title: props.activeKey == 'PRODUCT_PLAN_BATCH_NO' ? t('生产批号规则') : t('指令单编号规则'),
      align: 'left',
      dataIndex: 'codeRuleName',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('规则编码'),
      align: 'left',
      dataIndex: 'codeRuleCode',
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      hideInSearch: true,
      width: 200,
      resizable: true,
      customRender: ({ record }) => (
        <div class='action-list'>
          <Button
            v-hasAuth='120020009000001'
            style='max-width: 100px; min-width: 40px'
            type='link'
            onClick={() => {
              edit(record);
            }}>
            <BMEllipsis tooltip={true} style='max-width: 100%'>
              {{
                default: () => t('编辑'),
                title: () => t('编辑'),
              }}
            </BMEllipsis>
          </Button>
        </div>
      ),
    },
  ]);

  // 编辑弹窗保存
  const updateTableData = async () => {
    tableInstance.value.fetchData();
  };

  // 批量配置
  const BatchConfiguration = async () => {
    batchConfigurationRef.value.openModal();
  };

  // 编辑
  const edit = async (row: any) => {
    editRulesRef.value.openModal();
    rowData.value = row;
  };
  watch(
    () => props.activeKey,
    val => {
      if (val === 'PRODUCT_PLAN_BATCH_NO') {
        columns.value[3].title = t('生产批号规则');
      } else {
        columns.value[3].title = t('指令单编号规则');
      }
    },
  );

  onMounted(() => {});
</script>
<style scoped lang="less">
  :deep(.action-list) {
    .mes-btn {
      padding-right: 12px;
      padding-left: 0;
    }
  }
  .container2 {
    background-color: #fff;
    height: 100%;
    padding: 16px 0px 0px 0px;
  }

  .search {
    display: flex;
    align-items: center;
    > div {
      margin-right: 10px;
    }
  }
  :deep(.plat-table-thead .plat-table-cell-fix-left .plat-table-cell-content) {
    font-weight: 700;
  }
</style>
