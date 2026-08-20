<!-- 生产计划模板 -->
<template>
  <keep-alive>
    <BMPageComponent
      v-if="pageHome"
      ref="pageRef"
      :rowKeys="['id']"
      :search="[true]"
      :hideRightTree="true"
      :showToolBars="[true]"
      :formProps="[formFirstProps as any]"
      :showIndexs="[true]"
      :requests="[reqPlanTemplatePageReq as any]"
      :columns="[columnsFirst as any]">
      <template #tableHeaderToolbar0>
        <Button v-hasAuth="120020014000001" type="primary" @click="addPlanTemplate()">
          {{ t('新建计划模板') }}
        </Button>
      </template>
      <template #tableHeaderTitle0>
        <BMTableTitle :title="t('生产计划模板列表')"></BMTableTitle>
      </template>
    </BMPageComponent>
  </keep-alive>
  <EditPage v-if="!pageHome" :type="type" :rowData="rowData" @back="back"></EditPage>
</template>

<script lang="tsx" setup>
  import {} from '@/services';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { TableColumn, Recordable, TableActionType } from '@bmos/components';

  import type { FormProps } from '@bmos/components';
  import { reactive } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Modal, Switch } from 'ant-design-vue';
  import StateTag from '@/components/StateTag/index.vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { usePermissionStore } from '@/stores/permission';
  import { reqPlanTemplatePage, reqPlanTemplateChangeState, reqPlanTemplateDelete } from '@/services';
  import EditPage from './components/editPage.vue'; //跳转新增编辑页面

  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();
  const pageHome = ref<boolean>(true);
  const type = ref<string>('');
  const rowData = ref<any>();
  const formFirstProps = reactive<Partial<FormProps>>({
    actionColOptions: {
      span: 12,
    },
    baseColProps: {
      // span: 6,
    },
    showAdvancedButton: false,
  });
  const columnsFirst: TableColumn[] = [
    {
      title: t('模板名称'),
      dataIndex: 'name',
      width: 170,
      resizable: true,
    },
    {
      title: t('状态'),
      dataIndex: 'confirmed',
      width: 170,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: () => ({
          options: [
            {
              label: t('已确认'),
              value: true,
            },
            {
              label: t('待确认'),
              value: false,
            },
          ],
        }),
      },
      customRender: ({ record }: any) => (
        <StateTag type={record.confirmed ? 'success' : 'warning'}>
          {record.confirmed ? t('已确认') : t('待确认')}
        </StateTag>
      ),
    },
    {
      title: t('操作人'),
      dataIndex: 'operatorUserName',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('操作时间'),
      dataIndex: 'operationTime',
      width: 170,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('启停'),
      dataIndex: 'state',
      fixed: 'right',
      width: 76,
      resizable: true,
      hideInSearch: true,
      customRender: (col: any) => {
        const { record, tableAction } = col;
        const state = record?.state;
        return (
          <Switch
            v-hasAuth='120020014000005'
            checked={state}
            onChange={checked => {
              changeState(record, checked as boolean, tableAction);
            }}
          />
        );
      },
    },
    {
      title: t('操作'),
      fixed: 'right',
      hideInSearch: true,
      width: 180,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }, tableAction) => [
        {
          label: t('查看'),
          ifShow: hasPermission('120020014000003'),
          onClick: () => {
            type.value = 'view';
            pageHome.value = false;
            rowData.value = record;
          },
        },
        {
          label: t('编辑'),
          ifShow: !record.state && hasPermission('120020014000002'),
          onClick: () => {
            type.value = 'edit';
            pageHome.value = false;
            rowData.value = record;
          },
        },

        {
          label: t('删除'),
          ifShow: !record.state && hasPermission('120020014000004'),
          danger: true,
          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否删除该计划模板'),
              onOk: async () => {
                try {
                  await reqPlanTemplateDelete({ id: record.id });
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
  ];
  // 新建计划模板
  const addPlanTemplate = () => {
    type.value = 'add';
    pageHome.value = false;
    console.log('新增模板');
  };
  // 启停
  const changeState = async (record: Recordable, checked: boolean, tableAction: TableActionType) => {
    const title = checked ? t('是否启用此计划模板') : t('是否停用此计划模板');
    Modal.confirm({
      title: t('提示'),
      icon: h(ExclamationCircleOutlined),
      content: `${title}`,
      onOk: async () => {
        try {
          await reqPlanTemplateChangeState({
            id: record.id,
            state: checked ? true : false,
          });
          message.success(checked ? t('启用成功') : t('停用成功'));
          tableAction.fetchData();
        } catch (error: any) {
          error.message && message.error(error.message);
        }
      },
      onCancel() {},
    });
  };

  const back = () => {
    pageHome.value = true;
  };
  // 获取表格数据
  const reqPlanTemplatePageReq = async (params: any) => {
    return await reqPlanTemplatePage(params);
  };
</script>
