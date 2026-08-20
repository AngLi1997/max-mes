import {
  getPlanProcessList,
  queryDatasetDetailApi,
  queryDatasetListByProcessIdApi,
  reqProductMaterialProductTreeReq,
} from '@/services';
export const utils = (formRef: any) => {
  // 获取数据集下拉
  const getDatasetIdOptions = async (processId: string) => {
    const { data } = await queryDatasetListByProcessIdApi({ processId, datasetType: 'POINT' });
    formRef.value.updateSchema({
      field: 'datasetId',
      componentProps: {
        options: data,
      },
    });
  };
  // 获取数据点下拉
  const getDatapointIdOptions = async (id: string) => {
    const { data } = await queryDatasetDetailApi({ id });
    formRef.value.updateSchema({
      field: 'datapointId',
      componentProps: {
        options: data.datasetPointList,
      },
    });
  };
  // 获取产品树
  const fetchProductOptionTree = async () => {
    try {
      const { data } = await reqProductMaterialProductTreeReq();
      // return data;
      // 循环树形结构数据 data, 根据 categoryFlag true 添加属性 selectable false
      const loop = (data: any[]) => {
        return data.map(item => {
          if (item.categoryFlag) {
            item.selectable = false;
          } else {
            item.selectable = true;
          }
          if (item.children) {
            loop(item.children);
          }
          return item;
        });
      };
      return loop(data);
    } catch (error) {
      //
    }
  };
  // 产品下拉改变时获取生产工艺下拉列表
  const getProcessList = async (val: string) => {
    const data = { productId: val, active: true };
    const res: any = await getPlanProcessList(data);
    const options = res.data.map((item: any) => {
      return {
        ...item,
        label: item.name,
        value: item.id,
      };
    });
    formRef.value?.updateSchema({
      field: 'processId',
      componentProps: {
        options,
      },
    });
  };
  const setFormValue = (data: any) => {
    formRef.value.setFieldsValue({
      ...data,
      productIds: data.dashboardDataSourceType?.value == 'INTERNAL' ? data.productIds : data.productIds[0],
      date: [data.statisticsRangeStartDate, data.statisticsRangeEndDate],
      statisticsRangeType: data.statisticsRangeType?.value,
      dashboardDataSourceType: data.dashboardDataSourceType?.value,
      dashboardShowStyle: data.dashboardShowStyle?.value,
      chartType: data.chartType?.value,
      productStatus: data.productStatus?.value,
      granularityStrategy: data.granularityStrategy?.value,
      xAxisStrategy: data.xAxisStrategy?.value,
      yAxisStrategy: data.yAxisStrategy?.value,
      referOne: data.tagValues?.[0],
      referTwo: data.tagValues?.[1],
    });
    formRef.value.updateSchema({
      field: 'productIds',
      componentProps: {
        multiple: data.dashboardDataSourceType.value == 'INTERNAL',
      },
    });
    if (data.dashboardDataSourceType.value == 'DATASET') {
      // 获取工艺/数据集/数据点下拉
      getProcessList(data.productIds[0]);
      getDatasetIdOptions(data.processId);
      getDatapointIdOptions(data.datasetId);
    }
  };
  const formUpdateSchema = (flag: string) => {
    formRef.value.updateSchema({
      field: 'datapointId',
      componentProps: {
        options: [],
      },
    });
    if (flag == 'datasetId') {
      return;
    }
    formRef.value.updateSchema({
      field: 'datasetId',
      componentProps: {
        options: [],
      },
    });
    if (flag == 'processId') {
      return;
    }
    formRef.value?.updateSchema({
      field: 'processId',
      componentProps: {
        options: [],
      },
    });
  };
  return {
    getDatasetIdOptions,
    getDatapointIdOptions,
    fetchProductOptionTree,
    getProcessList,
    setFormValue,
    formUpdateSchema,
  };
};
