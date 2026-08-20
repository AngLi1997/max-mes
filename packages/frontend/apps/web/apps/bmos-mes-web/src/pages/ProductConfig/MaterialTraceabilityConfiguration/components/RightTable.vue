<template>
  <div class="container">
    <Tabs v-model:activeKey="activeKey" @change="typeChange">
      <TabPane v-if="productType === 0 || productType === 1" key="1" :tab="t('消耗信息')"></TabPane>
      <TabPane v-if="productType === 2 || productType === 1" key="2" :tab="t('产出信息')"></TabPane>
    </Tabs>
    <div class="table_box">
      <BMTable
        ref="tableInstance"
        :columns="columns"
        :dataSource="showTableData"
        :pagination="false"
        :search="false"
        :scroll="{ x: 800, y: 500 }">
        <template #toolbar>
          <Button type="primary" :disabled="type === 'look' ? true : false" @click="add">
            {{ t('新增数据') }}
          </Button>
        </template>
      </BMTable>
    </div>
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMTable, TableColumn } from '@bmos/components';
  import { Tabs, TabPane, Modal, Button, Select } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { getProcedureList, getListByProcedureModelId } from '@/services';

  const props = defineProps({
    type: {
      //新增编辑查看
      type: String,
      default: () => '',
    },
    productType: {
      type: [String, Number],
      default: () => 2,
    },
    processList: {
      type: Array,
      default: () => [],
    },
    tableData: {
      //树节点的表格数据
      type: Array,
      default: () => [],
    },
  });
  const showTableData = computed(() => {
    return newTableData.value.filter((item: any) => item.traceType == (activeKey.value == '1' ? 1 : 2));
  });
  const activeKey = ref<any>(
    props.productType === 0 || props.productType === 1
      ? '1'
      : props.productType === 2 || props.productType === 1
      ? '2'
      : '1',
  );
  const newTableData = defineModel<any[]>('tableList', { default: [] });
  const columns: TableColumn[] = [
    {
      title: t('工艺'),
      dataIndex: 'processId',
      width: 220,
      resizable: true,
      customRender: ({ record }: any) => {
        if (!record.processList) {
          getRowProcessList(record);
        }
        return (
          <div>
            <Select
              v-model:value={record.processId}
              style='width: 100%'
              placeholder={t('请选择')}
              optionFilterProp='name'
              showSearch
              allow-clear
              getPopupContainer={triggerNode => triggerNode.parentNode}
              disabled={props.type === 'look'}
              fieldNames={{
                label: 'name',
                value: 'id',
              }}
              onChange={async (val: any, option: any) => {
                record.procedureId = undefined;
                record.procedureList = [];
                record.procedureStepId = undefined;
                record.taskList = [];
                if (val) {
                  const { data } = await getProcedureList({
                    //查对应工序集合
                    processId: record.processId,
                    version: option?.activeVersion,
                  });
                  record.procedureList = data;
                }
              }}
              options={record.processList}></Select>
          </div>
        );
      },
    },
    {
      title: t('工序'),
      dataIndex: 'procedureId',
      width: 220,
      resizable: true,
      customRender: ({ record }: any) => {
        if (!record.procedureList) {
          getRowProcedureList(record);
        }
        return (
          <div>
            <Select
              v-model:value={record.procedureId}
              style='width: 100%'
              placeholder={t('请选择')}
              optionFilterProp='name'
              showSearch
              allow-clear
              getPopupContainer={triggerNode => triggerNode.parentNode}
              disabled={props.type === 'look'}
              fieldNames={{
                label: 'name',
                value: 'procedureId',
              }}
              onChange={async (val: any, option: any) => {
                record.procedureStepId = undefined;
                record.taskList = [];
                //根据工序模型id查询步骤列表
                if (val) {
                  const { data } = await getListByProcedureModelId(option?.id);
                  record.taskList = data;
                }
              }}
              options={record.procedureList}></Select>
          </div>
        );
      },
    },
    {
      title: t('步骤/任务'),
      dataIndex: 'procedureStepId',
      width: 220,
      resizable: true,
      customRender: ({ record }: any) => {
        if (!record.taskList) {
          getRowTaskList(record);
        }
        return (
          <div>
            <Select
              v-model:value={record.procedureStepId}
              style='width: 100%'
              placeholder={t('请选择')}
              optionFilterProp='name'
              showSearch
              allow-clear
              getPopupContainer={triggerNode => triggerNode.parentNode}
              disabled={props.type === 'look'}
              fieldNames={{
                label: 'name',
                value: 'procedureStepId',
              }}
              options={record.taskList}></Select>
          </div>
        );
      },
    },
    {
      title: t('操作'),
      fixed: 'right',
      hideInSearch: true,
      width: 100,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }) => [
        {
          label: t('删除'),
          danger: true,
          disabled: props.type === 'look' ? true : false,
          onClick: () => {
            Modal.confirm({
              title: t('是否删除配置数据？'),
              icon: h(ExclamationCircleOutlined),
              content: t('确认后该配置数据删除，列表中不显示'),
              okText: t('确定'),
              cancelText: t('取消'),
              onOk: () => {
                if (record.id) {
                  newTableData.value = newTableData.value.filter((item: any) => item?.id !== record.id);
                } else {
                  newTableData.value = newTableData.value.filter((item: any) => item.key !== record.key);
                }
              },
            });
          },
        },
      ],
    },
  ];
  const add = () => {
    newTableData.value.push({
      processId: undefined,
      procedureId: undefined,
      procedureStepId: undefined,
      key: new Date().getTime().toString(),
      traceType: activeKey.value === '1' ? 1 : 2,
    });
  };

  // 获取表格的工艺list
  const getRowProcessList = async (record: any) => {
    const Index = props.processList?.findIndex((item: any) => item.id === record.processId);
    if (Index < 0 && record.processId) {
      // 如果不在下拉框内,就添进去
      record.processList = [
        ...props.processList,
        { id: record.processId, name: record.processName, activeVersion: record.processVersion, disabled: true },
      ];
    } else {
      record.processList = props.processList;
    }
  };
  // 获取表格的工序list
  const getRowProcedureList = async (record: any) => {
    if (!record.processId) {
      record.procedureList = [];
      return;
    }
    const temp: any = record.processList?.find((item: any) => item.id === record.processId);
    const { data } = await getProcedureList({
      //查对应工序集合
      processId: record.processId,
      version: temp?.activeVersion || record?.processVersion,
    });

    const Index = data?.findIndex((item: any) => item.procedureId === record.procedureId);
    if (Index < 0) {
      // 如果不在下拉框内,就添进去
      record.procedureList = [...data, { name: record.procedureName, procedureId: record.procedureId, disabled: true }];
    } else {
      record.procedureList = data;
    }
  };
  // 获取步骤/任务list
  const getRowTaskList = async (record: any) => {
    const temp: any = record.procedureList?.find((item: any) => item.procedureId === record.procedureId);
    const { data } = await getListByProcedureModelId(temp.id);
    const Index = data?.findIndex((item: any) => item.procedureStepId === record.procedureStepId);
    if (Index < 0) {
      // 如果不在下拉框内,就添进去
      record.taskList = [
        ...data,
        { name: record.procedureStepName, procedureStepId: record.procedureStepId, disabled: true },
      ];
    } else {
      record.taskList = data;
    }
  };
  // 切换tab
  const typeChange = (val: any) => {
    activeKey.value = val;
  };
  watch(
    () => props.productType,
    val => {
      if (val === 2) {
        activeKey.value = '2';

        return;
      }
      if (val === 0 || val === 1) {
        activeKey.value = '1';
      }
    },
    {
      deep: true,
    },
  );
</script>
<style lang="less" scoped>
  .container {
    height: 100%;
    .table_box {
      height: calc(100% - 30px);
      overflow-y: scroll;
    }
  }
  :deep(.mes-table-cell) {
    overflow: visible;
  }
</style>
