<!-- 版本管理 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnBack">
          {{ t('批签发管理') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ t('版本管理') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="returnBack">{{ t('返回') }}</Button>
      <Button type="primary" @click="create">{{ t('批签发生成') }}</Button>
    </template>
    <BMTableTitle :title="t('生产信息')"></BMTableTitle>
    <BMDescriptions :list="detailList" :column="4" :showBottomBorder="false"></BMDescriptions>
    <BMTable
      ref="tableInstance"
      :data-request="loadData"
      :columns="columns"
      :show-tool-bar="false"
      :show-search-border="false"
      :search="false"
      :extraParams="extraParams"
      row-key="id"
      :pagination="{
        pageSize: 20,
      }"
      :scroll="{ x: 1200, y: 400 }"></BMTable>
  </BreadcrumbButton>
  <Step v-model:open="stepOpen" :formValue="stepFormValue" :isMange="true" :again="again" />
  <History
    v-model:historyOpen="historyOpen"
    :businessId="historyBusinessId"
    :down-file-name="historyDownLoadName"
    :getApi="reqLotReleaseManageHistory" />
  <UploadTemplateModal v-model:uploadModalOpen="uploadTemplateModalOpen" :rowData="rowData" @ok="updateTable" />
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import {
    BMTableTitle,
    BMDescriptions,
    DescriptionsItemProps,
    BMTable,
    BMStateTag,
    TableColumn,
    Recordable,
  } from '@bmos/components';
  import { StatusClassMap, StatusType } from './type';
  import Step from '../components/step/index.vue';
  import History from '@/components/History/index.vue';
  import UploadTemplateModal from './components/UploadTemplateModal.vue';
  import { message, Modal } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import {
    reqLotReleaseManageSubmit,
    reqLotReleaseManageHistory,
    reqLotReleaseManageQueryVersionPage,
    reqLotReleaseManageScrap,
    reqLotReleaseManageDownload,
    reqPlanDetail,
  } from '@/services';
  import { fileStreamDownload, isNullOrUnDef } from '@bmos/utils';

  const route = useRoute();
  const router = useRouter();

  const returnBack = () => {
    router.push({
      name: 'BatchReleaseManagement',
    });
  };

  const tableInstance = ref<InstanceType<typeof BMTable> | null>(null);

  const extraParams = ref<Record<string, any>>({});

  const rowData = ref<Record<string, any>>({});

  const stepOpen = ref<boolean>(false);
  const again = ref<boolean>(false);
  const stepFormValue = ref<Record<string, any>>({});

  const historyOpen = ref<boolean>(false);
  const historyBusinessId = ref<string>('');
  const historyDownLoadName = ref<string>('');

  const uploadTemplateModalOpen = ref<boolean>(false);

  const voidVersion = async (record: Recordable) => {
    Modal.confirm({
      title: t('是否作废次此版本'),
      icon: h(ExclamationCircleOutlined),
      content: t('作废后此版本的批签发将无法使用'),
      async onOk() {
        try {
          await reqLotReleaseManageScrap(record.id);
          message.success(t('操作成功'));
          tableInstance.value?.fetchData();
          return Promise.resolve();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
    });
  };

  const downFile = async (record: Recordable) => {
    try {
      const res = await reqLotReleaseManageDownload(record.id);
      fileStreamDownload(
        res,
        `${stepFormValue.value?.name}--${record?.templateVersion}.${record.fileUrl?.split('.')?.pop()}`,
      );
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const columns: TableColumn[] = [
    {
      title: t('批签发编号'),
      dataIndex: 'no',
      width: 100,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('模版版本'),
      dataIndex: 'templateVersion',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('生成人'),
      dataIndex: 'generatorName',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('生成时间'),
      dataIndex: 'generateTime',
      width: 120,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生效时间'),
      dataIndex: 'effectTime',
      width: 120,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('状态'),
      dataIndex: 'status',
      width: 100,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <BMStateTag type={StatusClassMap.get(record.status?.value)?.type}>{record.status?.label}</BMStateTag>
      ),
    },
    {
      title: t('备注'),
      dataIndex: 'remark',
      width: 100,
      hideInSearch: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 180,
      actions: ({ record }: any) => [
        {
          label: t('重新生成'),
          ifShow: record.status?.value === StatusType.EDIT,
          onClick: () => {
            stepFormValue.value = {
              ...stepFormValue.value,
              lotReleaseVersion: record.templateVersion,
            };
            again.value = true;
            stepOpen.value = true;
          },
        },
        {
          label: t('上传'),
          ifShow: record.status?.value === StatusType.EDIT,
          onClick: () => {
            rowData.value = record;
            uploadTemplateModalOpen.value = true;
          },
        },
        {
          label: t('审核进度'),
          ifShow: record.status?.value === StatusType.PROCESSING,
          onClick: () => {
            router.push({
              name: 'batch-release-review-schedule',
              query: {
                processInstanceId: record.processInstanceId,
                deploymentId: record.deploymentId,
                fromList: 'fromList',
              },
            });
          },
        },
        {
          label: t('下载'),
          onClick: () => {
            downFile(record);
          },
        },
        {
          label: t('提交审核'),
          ifShow: record.status?.value === StatusType.EDIT,
          onClick: async () => {
            try {
              await reqLotReleaseManageSubmit({
                id: record.id,
              });
              tableInstance.value?.fetchData();
              sendMessage(MessageType.UpdateMessageCount);
              message.success(t('操作成功'));
            } catch (error: any) {
              error.message && message.error(error.message);
            }
          },
        },
        {
          label: t('历史'),
          onClick: () => {
            historyOpen.value = true;
            historyBusinessId.value = record.id;
            historyDownLoadName.value = `${stepFormValue.value?.name}--${record.templateVersion}.${record.fileUrl
              ?.split('.')
              ?.pop()}`;
          },
        },
        {
          label: t('作废'),
          ifShow: record.status?.value === StatusType.EDIT || record.status?.value === StatusType.EFFECTIVE,
          danger: true,
          onClick: () => {
            voidVersion(record);
          },
        },
      ],
    },
  ];

  const loadData = async (params: any) => {
    if (isNullOrUnDef(params.lotReleaseTemplateId)) return [];
    return reqLotReleaseManageQueryVersionPage(params);
  };

  const updateTable = () => {
    tableInstance.value?.fetchData();
  };

  watch(
    () => stepOpen.value,
    val => {
      if (!val) {
        updateTable();
      }
    },
  );

  const detailList = ref<DescriptionsItemProps[]>([]);
  // 监听路由变化
  watch(
    () => route.query,
    async query => {
      await nextTick();
      const { data } = await reqPlanDetail(query.planId as string);
      const {
        productName,
        productMergeCode,
        productSpecification,
        processName,
        batchNo,
        processId,
        productId,
        startTime,
        endTime,
      } = data;
      const { lotReleaseTemplateId, name, planId } = query;
      extraParams.value = {
        lotReleaseTemplateId,
        planId,
      };
      stepFormValue.value = {
        lotReleaseTemplateId,
        name,
        productName,
        processName,
        batchNo,
        planId,
        processId,
        productId,
      };
      detailList.value = [
        {
          label: t('产品名称'),
          value: productName as string,
        },
        {
          label: t('产品编码'),
          value: productMergeCode as string,
        },
        {
          label: t('产品规格'),
          value: productSpecification as string,
        },
        {
          label: t('工艺名称'),
          value: processName as string,
        },
        {
          label: t('生产批号'),
          value: batchNo as string,
        },
        {
          label: t('生产开始时间'),
          value: startTime as string,
        },
        {
          label: t('生产结束时间'),
          value: endTime as string,
        },
        {
          label: t('批签发模版'),
          value: name as string,
        },
      ];
    },
    {
      immediate: true,
    },
  );

  const create = () => {
    stepFormValue.value = {
      ...stepFormValue.value,
      lotReleaseVersion: undefined,
    };
    stepOpen.value = true;
  };
</script>

<style lang="less" scoped>
  .bmos-table {
    margin-top: var(--bmos-margin-large);
    flex: 1;
    overflow: hidden;
  }
</style>
