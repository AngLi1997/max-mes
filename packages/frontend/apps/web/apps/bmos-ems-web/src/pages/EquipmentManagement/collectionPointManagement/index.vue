<!-- 采集点管理 -->
<template>
  <BMPageComponent
    ref="pageRef"
    :rowKeys="['id']"
    :search="[true]"
    :hideRightTree="true"
    :showToolBars="[true]"
    :formProps="[formFirstProps as any]"
    :isSelects="[false, false]"
    :rowSelections="rowSelections"
    :requests="[reqCollectionPointListReq as any]"
    :columns="[columnsFirst as any]">
    <template #tableHeaderToolbar0="{ instance }:any">
      <!-- 新增编辑查看采集点弹框 -->
      <CollectionPointModal
        ref="CollectionPointModalRef"
        :rowId="rowId"
        :type="type"
        :formData="formData"
        @updateTable="updateTable"></CollectionPointModal>
      <!-- 导入弹框 -->
      <ImportModal ref="ImportModalRef" @updateTable="updateTable"></ImportModal>
      <!-- 关联设备数据弹框 -->
      <RelationEquipmentModal
        ref="RelationEquipmentModalRef"
        v-model:open="openRelationModal"
        :selectedRows="operationSelectedRows"
        @updateTableAndSelectedRows="updateTableAndSelectedRows"></RelationEquipmentModal>
      <Button v-hasAuth="160010003000001" type="primary" @click="handleCollectionPoint({}, 'add')">
        {{ t('新增采集点') }}
      </Button>
      <Button @click="relation">
        {{ t('关联设备数据') }}
      </Button>
      <Dropdown>
        <Button>
          {{ t('更多') }}
          <BMIcons
            icon="Group"
            style="width: 14px; height: 14px; transform: translate(6px, -2px); vertical-align: middle"></BMIcons>
        </Button>
        <template #overlay>
          <Menu>
            <MenuItem key="1" v-hasAuth="160010003000005" @click="batchImport">{{ t('导入') }}</MenuItem>
            <MenuItem key="2" v-hasAuth="160010003000006" @click="export1(instance, 'screen')">
              {{ t('导出筛选数据') }}
            </MenuItem>
            <MenuItem key="3" v-hasAuth="160010003000006" @click="export1(instance, 'currentPage')">
              {{ t('导出当前页数据') }}
            </MenuItem>
            <MenuItem key="4" v-hasAuth="160010003000007" @click="startStop('start')">
              {{ t('启用') }}
            </MenuItem>
            <MenuItem key="5" v-hasAuth="160010003000007" @click="startStop('stop')">{{ t('停用') }}</MenuItem>
          </Menu>
        </template>
      </Dropdown>
    </template>
    <template #tableHeaderTitle0>
      <BMTableTitle :title="t('采集点管理')"></BMTableTitle>
    </template>
  </BMPageComponent>
</template>

<script lang="tsx" setup>
  import {
    reqAcquisitionPointPage,
    reqAcquisitionPointDisable,
    reqAcquisitionPointEnable,
    reqAcquisitionPointBatch,
    reqAcquisitionPointExport,
    getQueryListDictDown,
  } from '@/services';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { FormProps } from '@bmos/components';
  import { reactive, onMounted } from 'vue';
  import { t } from '@bmos/i18n';
  import { usePermissionStore } from '@/stores/permission';
  import { BMIcons } from '@bmos/icons';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { Dropdown, Menu, MenuItem, Switch, Modal, message } from 'ant-design-vue';
  import { fileStreamDownload } from '@bmos/utils';
  import CollectionPointModal from './components/CollectionPointModal.vue';
  import ImportModal from './components/ImportModal.vue';
  import RelationEquipmentModal from './components/RelationEquipmentModal.vue';

  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();
  const CollectionPointModalRef = ref<any>();
  const ImportModalRef = ref<any>();
  const RelationEquipmentModalRef = ref<any>();
  const openRelationModal = ref<boolean>(false);
  const rowId = ref<any>();
  const formData = ref<any>();
  const type = ref<string>('');
  const equipmentData = ref<any>(); //设备关联数据下拉框
  const queryParams = ref<any>(); //存查询过的参数
  const selectedRowKeys1 = ref<any>([]); //多选时的表格ids
  const operationSelectedRows = ref<any>([]); //存多选的数据
  // 多选
  const rowSelections = reactive([
    {
      type: 'checkbox',
      hideSelectAll: false,
      selectedRowKeys: selectedRowKeys1.value,
      onChange: (selectedRowKeys: any[], selectedRows: any[]) => {
        if (rowSelections[0]?.selectedRowKeys) {
          rowSelections[0].selectedRowKeys = selectedRowKeys;
        }
        operationSelectedRows.value = selectedRows;
      },
    },
    null,
  ]);
  const formFirstProps = reactive<Partial<FormProps>>({
    showAdvancedButton: false, //展示更多
    actionColOptions: {
      // span: 6,
    },
    baseColProps: {
      span: 6,
    },
  });
  const columnsFirst = ref<any>([
    {
      title: t('采集点名称'),
      dataIndex: 'name',
      fixed: 'left',
      width: 180,
      resizable: true,
    },
    {
      title: t('采集点编码'),
      dataIndex: 'code',
      width: 180,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('采集点类型'),
      dataIndex: 'type',
      width: 180,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('属性'),
              value: 'ATTR',
            },
            {
              label: t('服务'),
              value: 'SERVICE',
            },
            {
              label: t('事件'),
              value: 'EVENT',
            },
          ],
        }),
      },
      customRender: ({ record }: any) => {
        return record?.type?.label;
      },
    },
    {
      title: t('数据类型'),
      dataIndex: 'dataType',
      width: 180,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record?.dataType?.label;
      },
    },
    {
      title: t('数采平台'),
      dataIndex: 'acquisitionPlatform',
      width: 180,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return record?.acquisitionPlatform?.label || '-';
      },
    },
    {
      title: t('数据点位名称'),
      dataIndex: 'dataPointName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('设备数据'),
      dataIndex: 'equipmentTagDataCode',
      width: 180,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }: any) => {
        return <div>{getEquipmentDataLabel(record.equipmentTagDataCode)}</div>;
      },
    },
    {
      title: t('描述'),
      dataIndex: 'description',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('更新人'),
      dataIndex: 'updateByName',
      width: 180,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('更新时间'),
      dataIndex: 'updateTime',
      width: 180,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('启用状态'),
      align: 'left',
      dataIndex: 'status',
      hideInTable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('启用'),
              value: 'ENABLE',
            },
            {
              label: t('停用'),
              value: 'DISABLE',
            },
          ],
        }),
      },
    },
    {
      title: t('启停'),
      dataIndex: 'status',
      width: 76,
      fixed: 'right',
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const status = record?.status?.value === 'ENABLE' ? true : false;
        return (
          <Switch
            v-hasAuth='160010003000007'
            checked={status}
            onChange={checked => {
              changeState(record, checked as boolean, tableAction);
            }}
          />
        );
      },
    },
    {
      title: t('操作'),
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 180,
      key: 'ACTION',
      resizable: true,
      actions: ({ record }: any, tableAction: any) => [
        {
          label: t('详情'),
          ifShow: record.status?.value === 'ENABLE' && hasPermission('160010003000003'),
          onClick: () => {
            handleCollectionPoint(record, 'view');
          },
        },
        {
          label: t('编辑'),
          ifShow: record.status?.value === 'DISABLE' && hasPermission('160010003000002'),
          onClick: () => {
            handleCollectionPoint(record, 'edit');
          },
        },

        {
          label: t('删除'),
          ifShow: record.status?.value === 'DISABLE' && hasPermission('160010003000004'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该采集点'),
              onOk: async () => {
                try {
                  await reqAcquisitionPointBatch([record.id]);
                  message.success(t('删除成功'));
                  tableAction.fetchData();
                } catch (error: any) {
                  error.message && message.error(error.message);
                }
              },
            });
          },
        },
      ],
    },
  ]);
  // 获取关联设备数据 回显表格label
  const getEquipmentData = async () => {
    const { data } = await getQueryListDictDown({ dictId: '160010002002' });
    equipmentData.value = data;
  };

  const getEquipmentDataLabel = (val: any) => {
    const temp = equipmentData.value?.find((item: any) => val === item.value);
    return temp?.label || val || '-';
  };

  // 获取表格数据
  const reqCollectionPointListReq = async (params: any) => {
    queryParams.value = params;
    return await reqAcquisitionPointPage(params as any);
  };
  // 刷新表格
  const updateTable = () => {
    pageRef.value?.fetchData();
  };

  const updateTableAndSelectedRows = () => {
    rowSelections[0].selectedRowKeys = [];
    selectedRowKeys1.value = [];
    operationSelectedRows.value = [];
    pageRef.value?.fetchData();
  };
  // 截取
  const getContentBetweenChars = (str: any) => {
    return decodeURI(str.match(/filename=(\S*).xlsx/)[1]);
  };
  // 导出筛选数据/导出当前页数据
  const export1 = async (instance: any, type: any) => {
    const data = type === 'screen' ? instance.queryFormRef?.getFormValues() : queryParams.value;
    const data2 = { ...data, all: type === 'screen' ? true : false };
    try {
      const res: any = await reqAcquisitionPointExport(data2);
      const fileName: any = getContentBetweenChars(res.headers['content-disposition']);
      fileStreamDownload(res.data, fileName);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  // 更多-启用/停用
  const startStop = (type: any) => {
    const ids: any = [];
    if (operationSelectedRows.value.length !== 0) {
      operationSelectedRows.value.forEach((item: any) => {
        ids.push(item?.id);
      });
    } else {
      return message.error(t('请先选择采集点'));
    }
    const title = type === 'start' ? t('是否启用所选采集点') : t('是否停用所选采集点');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          if (type === 'start') {
            await reqAcquisitionPointEnable(ids);
            message.success(t('启用成功'));
            pageRef.value?.fetchData();
          } else {
            await reqAcquisitionPointDisable(ids);
            message.success(t('停用成功'));
            pageRef.value?.fetchData();
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
      onCancel() {},
    });
  };
  // 更多-关联设备数据
  const relation = () => {
    if (operationSelectedRows.value.length !== 0) {
      openRelationModal.value = true;
    } else {
      return message.error(t('请先选择采集点'));
    }
  };
  // 操作栏启停
  const changeState = async (record: any, checked: boolean, tableAction: any) => {
    const title = checked ? t('是否启用此采集点') : t('是否停用此采集点');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          if (checked) {
            await reqAcquisitionPointEnable([record.id]);
            message.success(t('启用成功'));
            tableAction.fetchData();
          } else {
            await reqAcquisitionPointDisable([record.id]);
            message.success(t('停用成功'));
            tableAction.fetchData();
          }
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
      onCancel() {},
    });
  };
  // 新增编辑查看采集点
  const handleCollectionPoint = (formData1: any, type1: any) => {
    formData.value = formData1;
    type.value = type1;
    CollectionPointModalRef.value.openModal();
  };
  const batchImport = () => {
    ImportModalRef.value.openModal();
  };
  onMounted(() => {
    getEquipmentData();
  });
</script>
