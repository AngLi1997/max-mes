<template>
  <NormalModalForm
    v-model:open="open"
    :title="t('预览')"
    wrapClassName="modalSizeExtraLarge datasetPreviewDatasetPointData"
    :footer="null">
    <div class="preview-content">
      <Select
        v-model:value="selectedPlan"
        :options="planOptions"
        mode="multiple"
        :placeholder="t('请选择批次')"
        :field-names="{ label: 'batchNo', value: 'planId' }"
        :allow-clear="false"
        max-tag-count="responsive"
        style="width: 300px; margin-bottom: 10px"
        @change="changeBatch"></Select>
    </div>
    <BMTable
      ref="tableRef"
      :columns="columns"
      :dataRequest="getList"
      :extraParams="previewPointsParams"
      :pagination="false"
      :search="false"
      :virtualScroll="true"
      :showToolBar="false"
      :scroll="{ x: 400, y: 300 }" />
  </NormalModalForm>
</template>
<script lang="tsx" setup>
  import { reqDatasetPreviewDatasetPointData } from '@/services';
  import { NormalModalForm, BMTable, TableColumn, Recordable, DataRequestFn, TableListResult } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';

  defineOptions({
    name: 'PointTable',
    inheritAttrs: false,
  });

  const props = withDefaults(
    defineProps<{
      previewPointsParams?: Recordable;
    }>(),
    {
      previewPointsParams: () => ({}),
    },
  );

  const open = defineModel<boolean>('open', {
    default: false,
  });

  const tableRef = ref<any>(null);

  const columns: TableColumn[] = [];

  const selectedPlan = ref<SelectValue>([]);
  const planOptions = ref<any[]>([]);

  const changeBatch = (value: SelectValue) => {
    const arr: any = [];
    // @ts-expect-error
    value?.forEach((item: any) => {
      const plan = planOptions.value.find((it: any) => it.planId === item);
      if (plan) {
        arr.push({
          title: plan.batchNo,
          dataIndex: plan.planId,
          width: 100,
        });
      }
    });
    tableRef.value?.replaceColumn([
      {
        title: '',
        dataIndex: 'name',
        width: 50,
      },
      ...arr,
    ]);
  };

  const getList: DataRequestFn = async (params: any) => {
    selectedPlan.value = [];
    const { data } = await reqDatasetPreviewDatasetPointData(params);
    const { list } = data;
    const listData: any = [
      {
        name: t('工艺版本'),
      },
    ];
    planOptions.value = list;
    tableRef.value?.replaceColumn([
      {
        title: '',
        dataIndex: 'name',
        width: 50,
      },
      // 判断是否超过 20 个批次，超过 20 个直显示前 20 个
      ...(list.length > 20 ? list.slice(0, 20) : list).map((item: any) => {
        // @ts-expect-error
        selectedPlan.value?.push(item.planId as SelectValue);
        listData[0][item.planId] = item.processVersion;
        return {
          title: item.batchNo,
          dataIndex: item.planId,
          width: 100,
        };
      }),
    ]);
    if (props.previewPointsParams?.points?.length) {
      props.previewPointsParams?.points?.forEach((item: any) => {
        const obj: any = {};
        list.forEach((it: any) => {
          obj[it.planId] = it?.data?.find(
            (d: any) => d.fieldId === item.fieldId && d.procedureStepId === item.procedureStepId,
          )?.value;
        });
        listData.push({
          name: item.name,
          ...obj,
        });
      });
    }
    return {
      data: {
        list: listData,
      },
    } as TableListResult;
  };
</script>
<style lang="less">
  .datasetPreviewDatasetPointData {
    .bmos-table {
      height: 500px;
    }
  }
</style>
