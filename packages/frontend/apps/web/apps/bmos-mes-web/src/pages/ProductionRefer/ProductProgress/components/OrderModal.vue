<template>
  <NormalModalForm
    v-model:open="open"
    :title="t('班次查看')"
    destroyOnClose
    wrap-class-name="modalSizeLarge"
    :showCancelButton="false"
    @okModal="okModal">
    <BMTableTitle :title="t('节点信息')"></BMTableTitle>
    <BMDescriptions :list="detailList" :column="2" :showBottomBorder="false"></BMDescriptions>
    <BMTable
      ref="tableInstance"
      :dataSource="dataSource"
      :columns="columns"
      :show-tool-bar="false"
      :show-search-border="false"
      :search="false"
      row-key="id"
      :pagination="{
        pageSize: 20,
      }"
      :scroll="{ x: 800, y: 400 }"></BMTable>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { NormalModalForm, BMTableTitle, BMDescriptions, BMTable, TableColumn, BMStateTag } from '@bmos/components';
  import { BMStateTagEnum, BMStateTagType } from '@bmos/components';
  import { reqFlowListChangeTeam } from '@/services';
  import { StateEnum } from '../enum';

  const open = defineModel<boolean>('orderModalOpen', {
    default: false,
  });
  const props = defineProps({
    params: {
      type: Object as PropType<any>,
      default: () => ({}),
    },
  });

  const StatusClassMap: Map<
    StateEnum,
    {
      type: BMStateTagType;
      stateName: string;
    }
  > = new Map([
    [
      StateEnum.INACTIVE,
      {
        type: BMStateTagEnum.DEFAULT,
        stateName: t('未开始'),
      },
    ],
    [
      StateEnum.ACTIVE,
      {
        type: BMStateTagEnum.PRIMARY,
        stateName: t('进行中'),
      },
    ],
    [
      StateEnum.IS_ACTIVE,
      {
        type: BMStateTagEnum.CONFIRM,
        stateName: t('已激活'),
      },
    ],
    [
      StateEnum.COMPLETE,
      {
        type: BMStateTagEnum.SUCCESS,
        stateName: t('已完成'),
      },
    ],
    [
      StateEnum.IS_END,
      {
        type: BMStateTagEnum.WARNING,
        stateName: t('已结束'),
      },
    ],
  ]);

  const detailList = computed(() => {
    const { name, procedureName } = props.params;
    return [
      {
        label: t('工序名称'),
        value: procedureName as string,
      },
      {
        label: t('步骤/任务'),
        value: name as string,
      },
    ];
  });

  const dataSource = ref([]);
  const columns: TableColumn[] = [
    {
      title: t('工艺班次'),
      dataIndex: 'processChangeNumber',
      width: 80,
      hideInSearch: true,
      customRender: ({ record }: any) => t('班次') + record.processChangeNumber,
    },
    {
      title: t('工序班次'),
      dataIndex: 'procedureChangeNumber',
      width: 80,
      hideInSearch: true,
      customRender: ({ record }: any) => t('班次') + record.procedureChangeNumber,
    },
    {
      title: t('状态'),
      dataIndex: 'state',
      width: 80,
      hideInSearch: true,
      customRender: ({ record }: any) => (
        <BMStateTag type={StatusClassMap.get(record.state)?.type}>
          {StatusClassMap.get(record.state)?.stateName}
        </BMStateTag>
      ),
    },
    {
      title: t('开始时间'),
      dataIndex: 'startTime',
      width: 130,
      hideInSearch: true,
    },
    {
      title: t('完成时间'),
      dataIndex: 'endTime',
      width: 130,
      hideInSearch: true,
    },
    {
      title: t('完成人'),
      dataIndex: 'completeBy',
      width: 100,
      hideInSearch: true,
    },
  ];

  watch(
    () => open.value,
    async newVal => {
      if (newVal) {
        await nextTick();
        const { data } = await reqFlowListChangeTeam(props.params?.params);
        dataSource.value = data;
      } else {
        dataSource.value = [];
      }
    },
    {
      immediate: true,
    },
  );

  const okModal = () => {
    open.value = false;
  };
</script>

<style lang="less"></style>
