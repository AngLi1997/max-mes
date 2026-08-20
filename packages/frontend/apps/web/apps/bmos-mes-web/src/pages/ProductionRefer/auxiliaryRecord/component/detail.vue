<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="goBack">{{ t('辅助记录') }}</breadcrumb-item>
        <breadcrumb-item>{{ t('查看') }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button @click="goBack">{{ t('返回') }}</Button>
    </template>
    <BMTableTitle :title="t('生产信息')"></BMTableTitle>
    <BMDescriptions :list="descData" :column="4" :showBottomBorder="false" hasTitle></BMDescriptions>
    <div class="form_box">
      <BMForm ref="myFormRef" v-bind="formProps" @submit="formSubmit" @reset="formSubmit"></BMForm>
    </div>
    <BMTable
      ref="tableInstance"
      :data-request="getDatasetPageList"
      :columns="columns"
      :show-tool-bar="false"
      :show-search-border="false"
      row-key="id"
      :search="false"
      :pagination="{
        pageSize: 20,
      }"
      showIndex
      :scroll="{ x: 844, y: 400 }"></BMTable>
  </BreadcrumbButton>
  <NormalModalForm
    :title="t('预览')"
    wrapClassName="modalSizeExtraLarge"
    :open="previewStatus"
    @cancelModal="() => (previewStatus = false)">
    <div class="record-content">
      <!-- <RecordPreview
        ref="recordRef"
        :node="currentRecord"
        :getApi="getSubsidiaryList"
        :params="{ id: currentRecord.id }"
        :processId="currentRecord.processId"
        :processVersion="currentRecord.processVersion"
        :productPlanId="currentRecord.productPlanId"></RecordPreview> -->
      <iframe v-if="file" :src="fileUrl + '#toolbar=0'" class="printIframe" frameborder="0"></iframe>
      <div v-else class="noPdf">
        <img :src="noPdf" alt="" />
        {{ t('暂无数据') }}
      </div>
    </div>
    <template #footer></template>
  </NormalModalForm>
</template>
<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import {
    BMTableTitle,
    BMDescriptions,
    BMForm,
    BMTable,
    FormProps,
    TableColumn,
    NormalModalForm,
  } from '@bmos/components';
  import { Breadcrumb, BreadcrumbItem, Button } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  // import { RecordPreview } from '@/components/Record';
  import { getSubRecordList } from '@/services';
  import noPdf from '@/assets/images/noPdf.png';
  import { message } from 'ant-design-vue';

  const props = withDefaults(
    defineProps<{
      rowData: any;
    }>(),
    {
      rowData: {},
    },
  );
  const emit = defineEmits(['close']);
  const myFormRef = ref();
  const goBack = () => {
    emit('close');
  };
  const previewStatus = ref(false);
  const tableInstance = ref();
  // const currentRecord = ref();
  const file = ref();
  const fileUrl = ref('');

  const formSubmit = () => {
    tableInstance.value.fetchData();
  };
  // 表单属性
  const formProps: Ref<FormProps> = ref({
    initialValues: {
      //默认值
    },
    transformDateFunc: (date: any) => {
      return date?.format?.('YYYY-MM-DD') ?? date;
    },
    actionColOptions: {
      span: 12,
    },
    labelWidth: 100,
    schemas: [
      {
        field: 'procedureName',
        component: 'Input',
        label: t('工序节点'),
      },
      {
        field: 'procedureStepName',
        component: 'Input',
        label: t('步骤/任务'),
      },
    ],
  });

  const descData = ref([
    {
      label: t('产品名称'),
      value: 'productName',
    },
    {
      label: t('产品编码'),
      value: 'productMergeCode',
    },
    {
      label: t('产品规格'),
      value: 'productSpecification',
    },
    {
      label: t('工艺名称'),
      value: 'processName',
    },
    {
      label: t('生产批号'),
      value: 'batchNo',
    },
    {
      label: t('生产开始时间'),
      value: 'startTime',
    },
    {
      label: t('生产结束时间'),
      value: 'endTime',
    },
  ]);

  const getFile = (url: any) => {
    if (!url) {
      return;
    }
    fileUrl.value = `${document.location.protocol}//${document.location.hostname}:${document.location.port}/${url}`;
    fetch(fileUrl.value)
      .then(response => {
        if (!response.ok) {
          throw new Error('Network response was not ok');
        }
        return response.blob(); // 将响应转换为Blob对象
      })
      .then(blob => {
        // 创建一个用于读取Blob的FileReader对象
        const reader = new FileReader();

        reader.onload = e => {
          // e.target.result 包含了文件的数据
          file.value = e.target?.result;
        };

        reader.onerror = error => {
          console.error('File could not be read!', error);
        };
        // 读取Blob数据
        reader.readAsArrayBuffer(blob);
      })
      .catch(error => {
        console.error('There has been a problem with your fetch operation:', error);
      });
  };

  const columns: TableColumn[] = [
    {
      title: t('工序节点'),
      dataIndex: 'procedureName',
      width: 200,
    },
    {
      title: t('步骤/任务节点'),
      dataIndex: 'procedureStepName',
      width: 200,
    },
    {
      title: t('工艺班次'),
      dataIndex: 'processChangeNumber',
      width: 200,
    },
    {
      title: t('工序班次'),
      dataIndex: 'procedureChangeNumber',
      width: 200,
      resizable: true,
    },
    {
      title: t('开始时间'),
      dataIndex: 'startTime',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('结束时间'),
      dataIndex: 'endTime',
      width: 200,
      resizable: true,
      sorter: true,
    },
    {
      title: t('完成人'),
      dataIndex: 'completeUserName',
      width: 200,
      resizable: true,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 140,
      actions: ({ record }) => [
        {
          label: t('预览'),
          onClick: () => {
            // currentRecord.value = record;
            previewStatus.value = true;
            getFile(record.archiveUrl);
          },
        },
        {
          label: t('打印'),
          onClick: () => {
            if (!record.archiveUrl) {
              message.error(t('找不到预览文件'));
              return;
            }
            let iframe;
            let doc: any = null;
            iframe = document.createElement('iframe');
            iframe.setAttribute('id', 'print-iframe');
            iframe.setAttribute(
              'src',
              `${document.location.protocol}//${document.location.hostname}:${document.location.port}/${record.archiveUrl}`,
            );
            document.body.appendChild(iframe);
            doc = iframe.contentWindow?.document;
            //这里可以自定义样式
            iframe.onload = () => {
              iframe.contentWindow?.focus();
              iframe.contentWindow?.print();
            };
            doc = iframe.contentWindow?.document;
            doc.close();
          },
        },
      ],
    },
  ];

  const getDatasetPageList = async (params: any) => {
    const { procedureName, procedureStepName } = myFormRef.value?.getFormValues() || {};
    return await getSubRecordList({
      ...params,
      procedureName,
      procedureStepName,
      productPlanId: props.rowData.id,
    });
  };
  onMounted(() => {
    descData.value.map(item => {
      item.value = props.rowData[item.value];
    });
  });
</script>
<style scoped lang="less">
  .form_box {
    border-top: 5px solid #f5f7fa;
    border-bottom: 5px solid #f5f7fa;
    margin-bottom: 20px;
    padding-top: 10px;
  }

  .record-content {
    height: 550px;
    position: relative;
  }
  .noPdf {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
  }
  .printIframe {
    width: 100%;
    height: 100%;
  }
</style>
