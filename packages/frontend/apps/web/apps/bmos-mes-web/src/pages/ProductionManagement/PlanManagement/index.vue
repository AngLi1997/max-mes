<!-- 生产计划管理页 -->
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
      :requests="[reqPlanTemplatePageReq as any]"
      :columns="[columnsFirst as any]">
      <template #tableHeaderToolbar0>
        <Button v-hasAuth="120030011000001" type="primary" @click="addPlan()">
          {{ t('新建生产计划') }}
        </Button>
      </template>
      <template #tableHeaderTitle0>
        <BMTableTitle :title="t('生产计划列表')"></BMTableTitle>
      </template>
    </BMPageComponent>
  </keep-alive>
  <!-- 新建生产计划页面 -->
  <AddPage v-if="!pageHome && addPage" :type="type" :rowData="rowData" @back="back"></AddPage>
  <!-- 日历调整页面 -->
  <CalendarPage v-if="!pageHome && calendarPage" :rowData="rowData" @back="back"></CalendarPage>
</template>

<script lang="tsx" setup>
  import {} from '@/services';
  import { BMPageComponent, BMTableTitle } from '@bmos/components';
  import type { TableColumn } from '@bmos/components';

  import type { FormProps } from '@bmos/components';
  import { reactive } from 'vue';
  import { t } from '@bmos/i18n';
  import { message, Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { usePermissionStore } from '@/stores/permission';
  import { reqProductionListPage, reqProductionPlanNullify } from '@/services';
  import StateTag from '@/components/StateTag/index.vue';
  import AddPage from './components/addPage/index.vue'; //新增页面
  import CalendarPage from './components/calendarPage/index.vue'; //日历页面

  const { hasPermission } = usePermissionStore();
  const pageRef = ref<any>();
  const pageHome = ref<any>(true);
  const addPage = ref<any>(false);
  const calendarPage = ref<any>(false);

  const rowData = ref<any>();
  const type = ref<string>('');

  const formFirstProps = reactive<Partial<FormProps>>({
    actionColOptions: {},
    baseColProps: {
      // span: 6,
    },
    showAdvancedButton: false,
  });
  const columnsFirst: TableColumn[] = [
    {
      title: t('计划名称'),
      dataIndex: 'planName',
      width: 170,
      resizable: true,
    },
    {
      title: t('指令单类型'),
      dataIndex: 'planType',
      width: 180,
      resizable: true,
      formItemProps: {
        component: 'Select',
        order: 5,
        componentProps: () => ({
          options: [
            {
              label: t('生产批次'),
              value: 'PRODUCT',
            },
            {
              label: t('实验批次'),
              value: 'EXPERIMENT',
            },
            {
              label: t('验证批次'),
              value: 'VERIFY',
            },
          ],
        }),
      },
    },

    {
      title: t('生产指令单数量'),
      dataIndex: 'planNumber',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('计划首批生产日期'),
      dataIndex: 'planFirstDate',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('计划生产结束日期'),
      dataIndex: 'planEndDate',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('创建人'),
      dataIndex: 'createBy',
      width: 170,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('创建时间'),
      dataIndex: 'createTime',
      width: 170,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('状态'),
      dataIndex: 'planState',
      width: 170,
      resizable: true,
      formItemProps: {
        component: 'Select',
        defaultValue: 'SEND',
        componentProps: () => ({
          options: [
            {
              label: t('已下发'),
              value: 'SEND',
            },
            {
              label: t('已作废'),
              value: 'NULLIFY',
            },
          ],
        }),
      },
      customRender: ({ record }: any) => (
        <StateTag type={record.planState.value === 'SEND' ? 'success' : 'default'}>
          {record.planState.name || '-'}
        </StateTag>
      ),
    },
    {
      title: t('操作'),
      fixed: 'right',
      hideInSearch: true,
      width: 200,
      resizable: true,
      key: 'ACTION',
      actions: ({ record }, tableAction) => [
        {
          label: t('日历调整'),
          ifShow: hasPermission('120030011000002'),
          onClick: () => {
            pageHome.value = false;
            addPage.value = false;
            calendarPage.value = true;
            rowData.value = record;
          },
        },
        {
          label: t('查看'),
          ifShow: hasPermission('120030011000003'),
          onClick: () => {
            type.value = 'view';
            pageHome.value = false;
            addPage.value = true;
            calendarPage.value = false;
            rowData.value = record;
          },
        },

        {
          label: t('作废'),
          ifShow: record.planState !== 'NULLIFY' && hasPermission('120030011000004'),
          danger: true,

          onClick: () => {
            Modal.confirm({
              title: t('删除确认'),
              icon: h(ExclamationCircleOutlined),
              closable: true,
              content: t('是否作废该生产计划'),
              onOk: async () => {
                try {
                  await reqProductionPlanNullify(record.id);
                  message.success(t('操作成功'));
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
  // 新建生产计划
  const addPlan = () => {
    type.value = 'add';
    pageHome.value = false;
    addPage.value = true;
    calendarPage.value = false;
  };
  const back = () => {
    pageHome.value = true;
  };
  // 获取表格数据
  const reqPlanTemplatePageReq = async (params: any) => {
    return await reqProductionListPage(params);
  };
</script>
