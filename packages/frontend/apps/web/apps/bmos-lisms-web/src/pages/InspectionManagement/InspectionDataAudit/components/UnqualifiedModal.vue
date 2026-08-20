<template>
  <NormalModalForm
    ref="modalFormRef"
    v-model:open="open"
    :title="t('不合格数据')"
    :showOkButton="false"
    :cancelButtonText="t('返回')"
    wrapClassName="modalSizeExtraLarge">
    <div class="unqualified-table">
      <BMTable
        ref="tableRef"
        :search="false"
        :data-source="tableData"
        :columns="columns"
        row-key="publishId"
        :pagination="false"
        :showToolBar="false"
        :scroll="{ x: 800, y: 400 }"></BMTable>
    </div>
  </NormalModalForm>
</template>

<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { BMModalForm, Recordable, BMTable, TableColumn, NormalModalForm } from '@bmos/components';
  import { InspectionProjectEnum, InspectionResultEnum } from '@/types';
  import { isEmpty } from '@bmos/utils';

  defineOptions({
    inheritAttrs: false,
  });

  const { InspectionProjectDict } = getDicts();

  const open = defineModel<boolean>('modalOpen', {
    default: false,
  });

  withDefaults(
    defineProps<{
      tableData: Recordable[];
    }>(),
    {
      tableData: () => [],
    },
  );

  const modalFormRef = ref<InstanceType<typeof BMModalForm>>();
  const { getDateFormat } = useConfig();
  const columns: TableColumn[] = [
    {
      title: t('标本批号'),
      dataIndex: 'sampleBatchNo',
      width: 160,
    },
    {
      title: t('标本编号'),
      dataIndex: 'orgSampleNo',
      width: 160,
    },
    {
      title: t('献浆者编号'),
      dataIndex: 'donorNo',
      width: 170,
    },
    {
      title: t('采浆日期'),
      dataIndex: 'donorTime',
      width: 170,
      customRender: ({ record }) => {
        return getDateFormat(record.donorTime);
      },
    },
    {
      title: t('血浆编号'),
      dataIndex: 'plasmaNo',
      width: 170,
    },
    {
      title: t('检品状态'),
      dataIndex: ['testArticleStatus', 'label'],
      width: 170,
    },
    {
      title: t('检验结论'),
      dataIndex: ['inspectResult', 'label'],
      width: 170,
      key: 'inspectResult.label',
      customRender: ({ record }: any) => {
        return record.inspectResult?.value === InspectionResultEnum.UNQUALIFIED ? (
          <span class='error-text'>{record.inspectResult?.label}</span>
        ) : (
          record.inspectResult?.label ?? '-'
        );
      },
    },
  ];

  const tableRef = ref<InstanceType<typeof BMTable>>();
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        tableRef.value?.addColumnAfter(
          InspectionProjectDict?.map((item: any) => {
            return {
              title: item?.label,
              dataIndex: [item.value, 'result', 'label'],
              width: 150,
              customRender: ({ record }: any) => {
                if (isEmpty(record[`${item.value}`]?.code)) {
                  return '-';
                }
                switch (record[`${item.value}`].code) {
                  case InspectionProjectEnum.HBsAg:
                  case InspectionProjectEnum.AntiHCV:
                  case InspectionProjectEnum.HIVAgAb:
                  case InspectionProjectEnum.AntiTP:
                    if (record[`${item.value}`]?.result?.value) {
                      return record[`${item.value}`]?.result?.value === InspectionResultEnum.UNQUALIFIED ? (
                        <span class='error-text'>{t('阳性')}</span>
                      ) : (
                        t('阴性')
                      );
                    } else {
                      return '-';
                    }

                  default:
                    return record[`${item.value}`]?.result?.value === InspectionResultEnum.UNQUALIFIED ? (
                      <span class='error-text'>{record[`${item.value}`]?.result?.label}</span>
                    ) : (
                      record[`${item.value}`]?.result?.label ?? '-'
                    );
                }
              },
            };
          }),
          'inspectResult.label',
        );
      }
    },
  );
</script>

<style lang="less" scoped>
  .unqualified-table {
    height: 100px;
  }
  :deep(.bmos-table .lisms-table-body) {
    border-bottom: none !important;
  }
</style>
