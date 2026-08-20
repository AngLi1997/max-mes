<!-- 审核详情 -->
<template>
  <BreadcrumbButton>
    <template #breadcrumb>
      <Breadcrumb>
        <breadcrumb-item @click="returnDataSet">
          {{ t('数据集管理') }}
        </breadcrumb-item>
        <breadcrumb-item>{{ title }}</breadcrumb-item>
      </Breadcrumb>
    </template>
    <template #btns>
      <Button v-if="route.query.type === OperationType.Edit" v-hasAuth="120070001000006" @click="view">
        {{ t('查看') }}
      </Button>
      <Button v-if="route.query.type === OperationType.View" v-hasAuth="120070001000005" @click="edit">
        {{ t('编辑') }}
      </Button>
      <Button @click="returnDataSet">{{ t('返回') }}</Button>
      <Button v-if="!isView" type="primary" :loading="saveLoading" @click="done">{{ t('完成') }}</Button>
    </template>
    <div class="content">
      <div class="left">
        <BMForm ref="formRef" :key="formKey" v-bind="formProps" />
      </div>
      <div class="right">
        <template v-if="showRecord">
          <div class="record-top">
            <Space :size="16">
              <label for="recordSelect">{{ t('记录项') }}</label>
              <Select
                id="recordSelect"
                v-model:value="curSelectRecord"
                :options="recordList"
                :field-names="{
                  label: 'recordItemName',
                  value: 'uniqueKey',
                }"
                style="width: 400px"
                :filter-option="(input, option: any) => option.recordItemName.toLowerCase().indexOf(input.toLowerCase()) >= 0"
                :placeholder="t('请选择记录项')"
                showSearch
                @select="(value: any) => handleClickShowModalRecord(value)">
                <template #option="{ recordItemName, procedureName }">
                  <div class="data-set-record-select">
                    <span>{{ recordItemName }}</span>
                    <span class="data-set-record-select-desc">{{ procedureName }}</span>
                  </div>
                </template>
              </Select>
            </Space>
            <Space :size="16">
              <Button v-if="!isView" :loading="saveLoading" @click="autoSave">{{ t('自动生成') }}</Button>
              <Button v-if="!isView" type="primary" :loading="saveLoading" @click="() => save()">
                {{ t('保存') }}
              </Button>
            </Space>
          </div>
          <div class="record-template">
            <Record ref="recordRef" :activeKeys="templateActiveKeys" @node-click="templateNodeClick"></Record>
          </div>
        </template>
        <Empty v-else />
      </div>
    </div>
  </BreadcrumbButton>
  <PointPreviewModal
    v-model:open="showPointPreviewModal"
    :previewPointsParams="previewPointsParams"></PointPreviewModal>
  <BatchAddModal v-model:showBatchAddModal="showBatchAddModal" @ok="batchAddPoint"></BatchAddModal>
  <BatchAddModal v-model:showBatchAddModal="showDynamicBatchAddModal" @ok="dynamicBatchAddPoint"></BatchAddModal>
</template>

<script lang="tsx" setup>
  import BreadcrumbButton from '@/components/BreadcrumbButton/index.vue';
  import { t } from '@bmos/i18n';
  import { BMForm } from '@bmos/components';
  import { DatasetType, OperationType } from './type';
  import { useDetail, useRecord } from './hooks';
  import { message, Empty, Modal, Space, Button } from 'ant-design-vue';
  import { Record } from '@/components/Record/Record';
  import PointPreviewModal from './components/PointPreviewModal.vue';
  import { reqDatasetCreateDataset, reqDatasetEditDataset, reqDatasetQueryDatasetDetail } from '@/services';
  import BatchAddModal from './components/BatchAddModal.vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import { createVNode } from 'vue';
  import { isEmpty } from '@bmos/utils';
  import { ALL_DYNAMIC_TABLE_NODE } from '@/components/Record';

  const route = useRoute();
  const router = useRouter();

  const formRef = ref<InstanceType<typeof BMForm>>();
  const pointTableRef = ref<any>();
  const showRecord = ref<number>(0);
  const datasetCategoryId = ref<string>('');
  const isView = ref<boolean>(false);
  const status = ref<OperationType>(OperationType.Add);

  const leftWidth = ref<string>('500px');

  // 是否有修改
  const hasChange = ref<boolean>(false);

  const getDetail = async () => {
    try {
      saveLoading.value = true;
      const { id } = route.query;
      const { data } = await reqDatasetQueryDatasetDetail({ id });
      formRef.value?.setFormModels({
        ...data,
        datasetType: data.type?.value,
        ...(data.datasetDynamicReportDataList
          ? {
              datasetDynamicReportDataList: data.datasetDynamicReportDataList.map((item: any) => ({
                ...item,
                dataType: item.dynamicDataType?.value,
              })),
            }
          : {
              datasetDynamicReportDataList: [],
            }),
        datasetPointList: data.datasetPointList || [],
        datasetLotReleaseLinkList: data.datasetLotReleaseLinkList || [],
      });
      if (data.type?.value === DatasetType.POINT) {
        // showRecord.value = new Date().getTime();
      } else {
        showRecord.value = 0;
      }
      if (data.type?.value === DatasetType.LOT_RELEASE_LINK || data.type?.value === DatasetType.DYNAMIC_REPORT) {
        leftWidth.value = '700px';
      } else {
        leftWidth.value = '500px';
      }
    } catch (error) {
    } finally {
      saveLoading.value = false;
    }
  };

  const formKey = ref<string>(new Date().getTime().toString());

  // 监听路由变化
  watch(
    () => route.query,
    async query => {
      if (query.type === OperationType.View) {
        isView.value = true;
        status.value = OperationType.View;
      } else if (query.type === OperationType.Edit) {
        isView.value = false;
        status.value = OperationType.Edit;
      }
      await nextTick();
      formKey.value = new Date().getTime().toString();
      if (query.type === OperationType.Add) {
        if (query.datasetCategoryId) {
          datasetCategoryId.value = query.datasetCategoryId as string;
          formRef.value?.setFormModels({
            datasetCategoryId: query.datasetCategoryId,
          });
        }
        status.value = OperationType.Add;
      }
      if (query.type === OperationType.Edit) {
        formRef.value?.updateSchema([
          {
            field: 'datasetCategoryId',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'name',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'datasetType',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'productId',
            componentProps: {
              disabled: true,
            },
          },
          {
            field: 'processId',
            componentProps: {
              disabled: true,
            },
          },
        ]);
      }
      if (query.type === OperationType.View || query.type === OperationType.Edit) {
        getDetail();
      }
    },
    {
      immediate: true,
    },
  );
  // 批量模式
  const batchModel = ref<boolean>(false);

  const {
    recordList,
    recordRef,
    curSelectRecord,
    curSelectRecordItem,
    handleClickRecord,
    templateActiveKeys,
    templateNodeClick,
    relationComponentIconClick,
    relationComponentAddClick,
    setNodeActiveByTarget,
    endCheck,
    getUniqueKey,
    CHECK_STATUS,
    curComponentList,
  } = useRecord({
    showRecord,
    formRef,
    pointTableRef,
    batchModel,
    isView,
    hasChange,
  });
  const {
    formProps,
    showPointPreviewModal,
    previewPointsParams,
    showBatchAddModal,
    batchAddPoint,
    showDynamicBatchAddModal,
    dynamicBatchAddPoint,
    deletePoint,
  } = useDetail({
    formRef,
    pointTableRef,
    status,
    isView,
    showRecord,
    CHECK_STATUS,
    leftWidth,
    relationComponentIconClick,
    relationComponentAddClick,
    setNodeActiveByTarget,
    batchModel,
    hasChange,
  });

  const handleClickShowModalRecord = (uniqueKey: string) => {
    if (!isView.value) {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对数据集的修改进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button
                  onClick={() => {
                    curSelectRecord.value = curSelectRecordItem.value?.uniqueKey;
                    cancelModal();
                    saveLoading.value = false;
                  }}>
                  {t('取消')}
                </Button>
                <Button
                  onClick={() => {
                    curSelectRecord.value = uniqueKey;
                    handleClickRecord(uniqueKey);
                    cancelModal();
                    saveLoading.value = false;
                  }}>
                  {t('不保存')}
                </Button>
                {!isView.value && (
                  <Button
                    type='primary'
                    loading={saveLoading.value}
                    onClick={async () => {
                      await save(uniqueKey);
                    }}>
                    {t('保存')}
                  </Button>
                )}
              </Space>
            </>
          );
        },
      });
    } else {
      handleClickRecord(uniqueKey);
    }
  };

  const saveLoading = ref<boolean>(false);

  const save = async (uniqueKey?: string) => {
    try {
      saveLoading.value = true;
      const res = await formRef.value?.validate();
      const { type } = route.query;
      const params = {
        ...res,
        ...(!isEmpty(res.datasetPointList) && {
          datasetPointList: res.datasetPointList
            .filter((item: any) => item.fieldId)
            ?.map((item: any) => {
              const { recordItem } = JSON.parse(item.extra);
              return getUniqueKey(recordItem) === curSelectRecordItem.value?.uniqueKey ? item : null;
            })
            .filter((item: any) => item),
        }),
        ...(!isEmpty(res.datasetDynamicReportDataList) && {
          datasetDynamicReportDataList: res.datasetDynamicReportDataList.filter((item: any) => item.dataName),
        }),
      };
      if (type === OperationType.Add) {
        const { data } = await reqDatasetCreateDataset(params);
        sessionStorage.setItem(
          'dataSetManageDetailUniqueKey',
          uniqueKey ? uniqueKey : curSelectRecordItem.value?.uniqueKey,
        );
        router.push({
          name: 'data-set-manage-detail',
          query: {
            type: OperationType.Edit,
            id: data,
          },
        });
      } else if (type === OperationType.Edit) {
        if (deletePoint.value.length) {
          params.removeDatasetPointIds = deletePoint.value.map((item: any) => item.id).filter((item: any) => item);
        }
        const { data } = await reqDatasetEditDataset(params);
        sessionStorage.setItem(
          'dataSetManageDetailUniqueKey',
          uniqueKey ? uniqueKey : curSelectRecordItem.value?.uniqueKey,
        );
        router.push({
          name: 'data-set-manage-detail',
          query: {
            type: OperationType.Edit,
            id: data,
            timestamp: new Date().getTime(),
          },
        });
      }
      hasChange.value = false;
      message.success(t('保存成功'));
      cancelModal();
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    } finally {
      saveLoading.value = false;
    }
  };
  const loopComponentList = (list: any) => {
    // 递归遍历组件列表 如果有子组件则递归遍历,
    const result: any[] = [];
    list.forEach((item: any) => {
      if (item.used) {
        if (item.children && item.children.length) {
          result.push(...loopComponentList(item.children));
        } else {
          result.push(item);
        }
      }
    });
    return result;
  };

  const autoSave = async () => {
    try {
      saveLoading.value = true;
      const res = await formRef.value?.validate();
      const { type } = route.query;
      const { procedureStepId, reusable } = curSelectRecordItem.value;
      const autoPoint = loopComponentList(curComponentList.value)
        ?.map((item: any) => {
          if (ALL_DYNAMIC_TABLE_NODE.includes(item?.componentType)) return null;
          return {
            fieldId: item.fieldId,
            name: `${item?.componentName} ${item?.componentNumber}`,
            procedureStepId: reusable ? '0' : procedureStepId,
            extra: JSON.stringify({
              id: item?.id,
              fieldId: item?.fieldId,
              componentName: item?.componentName,
              componentType: item?.componentType,
              componentNumber: item?.componentNumber,
              recordItem: { ...curSelectRecordItem.value },
            }),
          };
        })
        .filter((item: any) => item);
      const params = {
        ...res,
        ...(!isEmpty(res.datasetPointList)
          ? {
              datasetPointList: res.datasetPointList
                .filter((item: any) => item.fieldId)
                ?.map((item: any) => {
                  const { recordItem } = JSON.parse(item.extra);
                  return getUniqueKey(recordItem) === curSelectRecordItem.value?.uniqueKey ? item : null;
                })
                .filter((item: any) => item)
                .concat(autoPoint),
            }
          : {
              datasetPointList: autoPoint,
            }),
      };
      if (type === OperationType.Add) {
        const { data } = await reqDatasetCreateDataset(params);
        sessionStorage.setItem('dataSetManageDetailUniqueKey', curSelectRecordItem.value?.uniqueKey);
        router.push({
          name: 'data-set-manage-detail',
          query: {
            type: OperationType.Edit,
            id: data,
          },
        });
      } else if (type === OperationType.Edit) {
        if (deletePoint.value.length) {
          params.removeDatasetPointIds = deletePoint.value.map((item: any) => item.id).filter((item: any) => item);
        }
        const { data } = await reqDatasetEditDataset(params);
        sessionStorage.setItem('dataSetManageDetailUniqueKey', curSelectRecordItem.value?.uniqueKey);
        router.push({
          name: 'data-set-manage-detail',
          query: {
            type: OperationType.Edit,
            id: data,
            timestamp: new Date().getTime(),
          },
        });
      }
      hasChange.value = false;
      message.success(t('保存成功'));
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    } finally {
      saveLoading.value = false;
    }
  };

  // 取消弹窗
  const cancelModal = () => {
    Modal.destroyAll();
  };

  const saveAndBack = async (type?: any) => {
    await save();
    cancelModal();
    if (type === 'view') {
      router.push({
        name: 'data-set-manage-detail',
        query: {
          type: OperationType.View,
          id: route.query?.id,
        },
      });
      title.value = t('查看数据集');
    } else {
      router.push({
        name: 'DataSetManage',
      });
    }
  };
  const done = async () => {
    if (hasChange.value && status.value === OperationType.Edit) {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对数据集的修改进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button
                  onClick={() => {
                    cancelModal();
                    router.push({
                      name: 'DataSetManage',
                    });
                  }}>
                  {t('不保存')}
                </Button>
                {!isView.value && (
                  <Button type='primary' loading={saveLoading.value} onClick={() => saveAndBack()}>
                    {t('保存')}
                  </Button>
                )}
              </Space>
            </>
          );
        },
      });
    } else if (status.value === OperationType.Add) {
      await save();
      router.push({
        name: 'DataSetManage',
      });
    } else {
      router.push({
        name: 'DataSetManage',
      });
    }
  };
  const returnDataSet = () => {
    if (hasChange.value) {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对数据集的修改进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button
                  onClick={() => {
                    cancelModal();
                    router.push({
                      name: 'DataSetManage',
                    });
                  }}>
                  {t('不保存')}
                </Button>
                {!isView.value && (
                  <Button type='primary' loading={saveLoading.value} onClick={() => saveAndBack()}>
                    {t('保存')}
                  </Button>
                )}
              </Space>
            </>
          );
        },
      });
    } else {
      router.push({
        name: 'DataSetManage',
      });
    }
  };
  // 编辑页面的查看按钮
  const view = () => {
    if (hasChange.value) {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对数据集的修改进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button
                  onClick={() => {
                    cancelModal();
                    router.push({
                      name: 'data-set-manage-detail',
                      query: {
                        type: OperationType.View,
                        id: route.query?.id,
                      },
                    });
                    title.value = t('查看数据集');
                  }}>
                  {t('不保存')}
                </Button>
                {!isView.value && (
                  <Button type='primary' loading={saveLoading.value} onClick={() => saveAndBack('view')}>
                    {t('保存')}
                  </Button>
                )}
              </Space>
            </>
          );
        },
      });
    } else {
      router.push({
        name: 'data-set-manage-detail',
        query: {
          type: OperationType.View,
          id: route.query?.id,
        },
      });
      title.value = t('查看数据集');
    }
  };

  // 查看页面的编辑按钮
  const edit = () => {
    router.push({
      name: 'data-set-manage-detail',
      query: {
        type: OperationType.Edit,
        id: route.query?.id,
      },
    });
    title.value = t('编辑数据集');
  };

  const title = ref<string>(t('新增数据集'));
  onMounted(async () => {
    await nextTick();
    cancelModal();
    deletePoint.value = [];
    try {
      const { type } = route.query;
      switch (type) {
        case OperationType.Add:
          title.value = t('新增数据集');
          break;
        case OperationType.Edit:
          title.value = t('编辑数据集');
          break;
        case OperationType.View:
          title.value = t('查看数据集');
          break;
        default:
          title.value = t('新增数据集');
          break;
      }
    } catch (error) {}
  });

  onUnmounted(() => {
    cancelModal();
    endCheck();
  });
</script>
<style lang="less">
  .data-set-record-select {
    display: flex;
    flex-direction: column;
    .data-set-record-select-desc {
      font-size: 12px;
      line-height: 16px;
      color: var(--bmos-fourth-level-text-color);
      overflow: hidden;
      white-space: nowrap;
      text-overflow: ellipsis;
      width: 100%;
      display: block;
    }
  }
</style>
<style lang="less" scoped>
  .content {
    display: flex;
    height: 100%;
    .left {
      width: v-bind(leftWidth);
      min-width: v-bind(leftWidth);
      height: 100%;
      border-right: 1px solid var(--bmos-second-level-border-color);
      padding: 0 12px 0 0;
      overflow-y: auto;
    }
    .right {
      flex: 1;
      height: 100%;
      position: relative;
      overflow-y: auto;
      display: flex;
      flex-direction: column;
      .mes-empty {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
      }
      .record-top {
        margin-left: 20px;
        display: flex;
        align-items: center;
        justify-content: space-between;
        .belong-step-label {
          color: var(--bmos-third-level-text-color);
          margin-left: 20px;
        }
        .belong-step {
          flex: 1;
          overflow: hidden;
          white-space: nowrap;
          text-overflow: ellipsis;
        }
      }
      .record-template {
        flex: 1;
        height: 100%;
        padding: var(--bmos-padding-mini);
        overflow-y: auto;
      }
    }
  }
  :deep(.formula .formula-container) {
    min-width: 960px;
    width: 960px;
    overflow-x: auto;
  }
</style>
