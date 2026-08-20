<!-- 班组管理 -->
<template>
  <keep-alive>
    <EmptyBlock v-if="!open">
      <div class="main bg-white">
        <BMTable
          ref="tableInstance"
          :data-request="loadData"
          :columns="columns"
          row-key="id"
          auto-height
          :autoHeightOffset="24"
          :scroll="{ x: 1144, y: 400 }"
          :formProps="formProps"
          showSearchBorder>
          <template #toolbar>
            <Button v-hasAuth="120030005000001" type="primary" @click="openDecompose('create', null)">
              {{ t('新建班组') }}
            </Button>
          </template>
        </BMTable>
      </div>
    </EmptyBlock>
  </keep-alive>
  <Decompose v-if="open" :showType="DecomposeType" :rowData="rowData" @close="open = false" />
  <Modal v-model:open="openView" :title="t('班组详情')" :width="800" :bodyStyle="{ height: '520px' }" centered>
    <Descriptions :column="2">
      <DescriptionsItem :label="t('班组名称')" :labelStyle="{ color: '#606266' }">
        {{ detailData.name }}
      </DescriptionsItem>
      <DescriptionsItem :label="t('班组编码')" :labelStyle="{ color: '#606266' }">
        {{ detailData.code }}
      </DescriptionsItem>
      <DescriptionsItem :label="t('班组描述')" :labelStyle="{ color: '#606266' }">
        <div class="description-box">
          <Tooltip placement="topLeft">
            <template #title>{{ detailData.description }}</template>
            {{ detailData.description }}
          </Tooltip>
        </div>
      </DescriptionsItem>
      <DescriptionsItem :label="t('班组人数')" :labelStyle="{ color: '#606266' }">
        {{ detailData.peopleNum }}
      </DescriptionsItem>
    </Descriptions>
    <div class="msg-card-header">{{ t('人员列表') }}</div>
    <div class="msg-people-box">
      <div class="msg-people-header">{{ t('人员名称') }}</div>
      <div class="msg-people-list">
        <div v-for="item in detailData.peoples" :key="item.userId" class="msg-people-item">
          {{ item.userName }}-{{ item.loginName }}
        </div>
      </div>
    </div>
    <template #footer>
      <Button type="primary" @click="openView = false">{{ t('确定') }}</Button>
    </template>
  </Modal>
  <PermissionDeptModal
    v-model:permissionOpen="permissionDeptModalOpen"
    :resourceId="firstRowData?.id"
    @ok="savePermissionDept" />
  <Production v-model:open="productionLineModalOpen" :menuId="productionLine?.id" />
</template>

<script setup lang="tsx">
  import type { DataRequestFn, FormProps, TableInstance } from '@bmos/components';
  import { BMTable } from '@bmos/components';
  import { message, Modal, Descriptions, DescriptionsItem, Tooltip } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { useTable } from './hooks/useTable';
  import Decompose from './components/Decompose.vue';
  import Production from './components/Production.vue';
  import PermissionDeptModal from '@/components/PermissionDept/index.vue';
  import EmptyBlock from '@/components/EmptyBlock/index.vue';

  import { planTeamPage, planTeamEnable, planTeamDisable, getPlanTeamPeoPle } from '@/services';

  const tableInstance = ref<TableInstance>();
  const savePermissionDept = async () => {
    tableInstance.value?.fetchData();
  };
  // 表单配置
  const formProps: Ref<Partial<FormProps>> = ref({
    actionColOptions: {
      span: 12,
    },
    showAdvancedButton: false,
  });
  // 搜索数据
  const loadData: DataRequestFn = async (params): Promise<any> => {
    return planTeamPage({ ...params });
  };
  // 是否打开新建/编辑页
  const open = ref(false);
  // 是否打开详情
  const openView = ref(false);
  // 当前行数据
  const rowData = ref<any>({});
  // 查看数据
  const detailData = ref<any>({});
  // 详情页/分解页type
  const DecomposeType = ref('');
  //  打开详情页
  const openDecompose = async (type: string, row: any) => {
    rowData.value = row;
    if (type == 'view') {
      // 获取详情数据
      const { data } = await getPlanTeamPeoPle(row.id);
      detailData.value = data;
      // 打开详情
      openView.value = true;
      return;
    }
    DecomposeType.value = type;
    open.value = true;
  };
  // 启用/停用
  const changeEnabled = async (row: any) => {
    row.loading = true;
    Modal.confirm({
      title: row.status.value == 'TRUE' ? t('请确认是否启用班组') + row.name : t('请确认是否停用班组') + row.name,
      closable: true,
      content: '',
      okText: t('确定'),
      cancelText: t('取消'),
      onOk: async () => {
        try {
          if (row.status.value == 'TRUE') {
            // 启用
            await planTeamEnable(row.id);
            message.success(t('启用成功'));
          } else {
            // 停用
            await planTeamDisable(row.id);
            message.success(t('停用成功'));
          }
        } catch (error: any) {
          row.status.value = row.status.value == 'TRUE' ? 'FALSE' : 'TRUE';
          error.message && message.error(error.message);
        } finally {
          row.loading = false;
        }
      },
      onCancel: () => {
        row.loading = false;
        row.status.value = row.status.value == 'TRUE' ? 'FALSE' : 'TRUE';
      },
    });
  };
  // 表格配置
  const { columns, permissionDeptModalOpen, firstRowData, productionLine, productionLineModalOpen } = useTable({
    openDecompose,
    changeEnabled,
  });
  /*
    事件
  */
</script>

<style scoped lang="less">
  .main {
    height: 100%;
    min-height: 100%;
    background-color: white;
    padding: 10px;
  }
  .description-box {
    width: 300px;
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
  }

  .msg-card-header {
    padding: 10px 20px;
    border-bottom: 1px solid #e1e3e5;
    margin-bottom: 10px;
    position: relative;
    &::before {
      content: '';
      position: absolute;
      width: 4px;
      height: 16px;
      left: 10px;
      top: 13px;
      background-color: #2871ff;
    }
  }
  .msg-people-box {
    height: 380px;
    .msg-people-header {
      height: 48px;
      box-sizing: border-box;
      border-bottom: 1px solid #e1e3e5;
      background-color: #fafafa;
      color: #606266;
      line-height: 48px;
      padding: 0 16px;
    }
    .msg-people-list {
      height: 332px;
      overflow: auto;
      .msg-people-item {
        height: 48px;
        box-sizing: border-box;
        border-bottom: 1px solid #e1e3e5;

        line-height: 48px;
        padding: 0 16px;
      }
    }
  }
</style>
