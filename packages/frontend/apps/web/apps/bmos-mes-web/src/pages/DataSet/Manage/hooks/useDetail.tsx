import { ComponentNode } from '@/components/Record';
import { getPlanProcessList, reqDatasetCategoryTree, reqProductMaterialProductTreeReq } from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { BMTableTitle, FormProps, Recordable, RenderCallbackParams } from '@bmos/components';
import { getUUID, isEmpty, loopSelectableTree } from '@bmos/utils';
import { Button, Divider, FormItemRest, Modal, Space, message } from 'ant-design-vue';
import { SelectValue } from 'ant-design-vue/es/select';
import { Reactive, createVNode } from 'vue';
import DynamicReport from '../components/DynamicReport.vue';
import LotReleaseLink from '../components/LotReleaseLink.vue';
import PointTable from '../components/PointTable.vue';
import { DatasetType, DatasetTypeMap, OperationType } from '../type';

export type UseDetailParams = {
  formRef: Ref<any>;
  pointTableRef: Ref<any>;
  status: Ref<OperationType>;
  isView: Ref<boolean>;
  showRecord: Ref<number>;
  leftWidth: Ref<string>;
  relationComponentIconClick: (record: Recordable) => void;
  relationComponentAddClick: (record: Recordable) => void;
  setNodeActiveByTarget: (target: ComponentNode) => void;
  CHECK_STATUS: Reactive<any>;
  batchModel: Ref<boolean>;
  hasChange: Ref<boolean>;
};

export const useDetail = ({
  formRef,
  pointTableRef,
  status,
  isView,
  showRecord,
  leftWidth,
  relationComponentIconClick,
  relationComponentAddClick,
  setNodeActiveByTarget,
  CHECK_STATUS,
  batchModel,
  hasChange,
}: UseDetailParams) => {
  const showPointPreviewModal = ref<boolean>(false);
  const previewPointsParams = ref<Recordable>({});
  /*
   * @description 预览数据点
   */
  const previewPoints = (formModel: Recordable) => {
    showPointPreviewModal.value = true;
    const { processId, datasetPointList } = formModel;
    previewPointsParams.value = {
      processId,
      points: datasetPointList,
    };
  };

  // 批量添加弹窗
  const showBatchAddModal = ref<boolean>(false);

  const batchAddPoint = (number: number) => {
    try {
      const formFieldValue = formRef.value?.getFormModelByField('datasetPointList');
      // length 为 number, 值为 { key: number }
      const pointList = Array.from({ length: number }).map(_item => ({
        key: getUUID(),
      }));
      if (isEmpty(formFieldValue)) {
        formRef.value?.setFormModel('datasetPointList', pointList);
      } else {
        formRef.value?.setFormModel('datasetPointList', [...formFieldValue, ...pointList]);
      }
    } catch (error) {}
  };

  // 动态数据批量添加弹窗
  const showDynamicBatchAddModal = ref<boolean>(false);
  const dynamicBatchAddPoint = (number: number) => {
    try {
      const formFieldValue = formRef.value?.getFormModelByField('datasetDynamicReportDataList');
      // length 为 number, 值为 { key: number }
      const pointList = Array.from({ length: number }).map(_item => ({
        key: getUUID(),
      }));
      if (isEmpty(formFieldValue)) {
        formRef.value?.setFormModel('datasetDynamicReportDataList', pointList);
      } else {
        formRef.value?.setFormModel('datasetDynamicReportDataList', [...formFieldValue, ...pointList]);
      }
    } catch (error) {}
  };

  const deletePoint = ref<any[]>([]);
  const setDeletePoint = (record: any) => {
    // 如果 deletePoint 中已经存在该 record，return, 否则添加
    const index = deletePoint.value.findIndex(item => item.id === record.id);
    if (index === -1) {
      deletePoint.value.push(record);
    }
  };

  const formProps: Ref<FormProps> = ref({
    showActionButtonGroup: false,
    baseColProps: {
      span: 24,
    },
    layout: 'vertical',
    schemas: [
      {
        field: 'datasetCategoryId',
        component: 'TreeSelect',
        label: t('所属分类'),
        required: true,
        componentProps: {
          disabled: isView.value || status.value === OperationType.Edit,
          fieldNames: {
            label: 'name',
            value: 'id',
          },
          showSearch: true,
          treeNodeFilterProp: 'name',
          request: async () => {
            try {
              const { data } = await reqDatasetCategoryTree();
              return data;
            } catch (error) {
              return [];
            }
          },
          onChange: () => {
            hasChange.value = true;
          },
        },
      },
      {
        field: 'name',
        component: 'Input',
        label: t('名称'),
        required: true,
        componentProps: {
          disabled: isView.value || status.value === OperationType.Edit,
        },
      },
      {
        field: 'datasetType',
        component: 'Select',
        label: t('类型'),
        required: true,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            disabled: isView.value || status.value === OperationType.Edit,
            options: [
              ...Array.from(DatasetTypeMap).map(([key, value]) => ({
                label: value,
                value: key,
              })),
            ],
            onChange: (value: SelectValue) => {
              hasChange.value = true;
              if (value === DatasetType.LOT_RELEASE_LINK || value === DatasetType.DYNAMIC_REPORT) {
                leftWidth.value = '700px';
              } else {
                leftWidth.value = '500px';
              }
              if (value === DatasetType.POINT && formModel.processId) {
                showRecord.value = new Date().getTime();
              } else {
                showRecord.value = 0;
              }
              batchModel.value = false;
              formModel.originalProcessId = undefined;
              formModel.datasetPointList = [];
            },
          };
        },
      },
      {
        field: 'productId',
        component: 'TreeSelect',
        label: t('产品'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) =>
          formModel.datasetType === DatasetType.POINT || formModel.datasetType === DatasetType.LOT_RELEASE_LINK,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            disabled: isView.value || status.value === OperationType.Edit,
            fieldNames: {
              label: 'showName',
              value: 'id',
            },
            showSearch: true,
            treeNodeFilterProp: 'showName',
            request: async () => {
              try {
                const { data } = await reqProductMaterialProductTreeReq();
                return loopSelectableTree(data, 'categoryFlag', true);
              } catch (error) {
                return [];
              }
            },
            onSelect: () => {
              formModel.processId = undefined;
              showRecord.value = 0;
              if (formModel.datasetType === DatasetType.POINT) {
                formModel.datasetPointList = [];
                formModel.originalProcessId = undefined;
              }
            },
            onChange: () => {
              hasChange.value = true;
            },
          };
        },
      },
      {
        field: 'processId',
        component: 'Select',
        label: t('工艺'),
        required: true,
        vIf: ({ formModel }: RenderCallbackParams) =>
          formModel.datasetType === DatasetType.POINT || formModel.datasetType === DatasetType.LOT_RELEASE_LINK,
        componentProps: ({ formModel }: RenderCallbackParams) => {
          return {
            disabled: isView.value || status.value === OperationType.Edit,
            fieldNames: { label: 'name', value: 'id' },
            request: {
              watchFields: ['productId'],
              options: {
                immediate: true,
              },
              callback: async () => {
                try {
                  if (!formModel.productId) {
                    return [];
                  }
                  const { data } = await getPlanProcessList({
                    productId: formModel.productId,
                  });
                  if (formModel.processId && formModel.datasetType === DatasetType.POINT) {
                    const findItem = data.find((item: any) => item.id === formModel.processId);
                    if (findItem) {
                      formModel.activeVersion = findItem.activeVersion;
                      showRecord.value = new Date().getTime();
                    }
                  }
                  return data;
                } catch (error) {
                  return [];
                }
              },
            },
            onChange: (value: SelectValue, option: any) => {
              if (value && formModel.originalProcessId) {
                Modal.confirm({
                  title: t('提示'),
                  wrapClassName: 'config-return-modal',
                  icon: createVNode(ExclamationCircleOutlined),
                  content: t('切换工艺会清空数据点配置，是否切换') + '?',
                  footer() {
                    return (
                      <>
                        <Space class='footer-btns'>
                          <Button
                            onClick={() => {
                              Modal.destroyAll();
                              formModel.processId = formModel.originalProcessId;
                            }}>
                            {t('取消')}
                          </Button>
                          <Button
                            type='primary'
                            onClick={() => {
                              Modal.destroyAll();
                              if (formModel.datasetType === DatasetType.POINT) {
                                showRecord.value = new Date().getTime();
                              } else {
                                showRecord.value = 0;
                              }
                              formModel.activeVersion = option.activeVersion;
                              if (isEmpty(option.activeVersion)) {
                                showRecord.value = 0;
                                message.warning(t('该工艺没有生效版本'));
                              }
                              formModel.originalProcessId = value;
                              formModel.datasetPointList = [];
                            }}>
                            {t('确定')}
                          </Button>
                        </Space>
                      </>
                    );
                  },
                });
              } else if (value) {
                if (formModel.datasetType === DatasetType.POINT) {
                  showRecord.value = new Date().getTime();
                } else {
                  showRecord.value = 0;
                }
                formModel.activeVersion = option.activeVersion;
                if (isEmpty(option.activeVersion)) {
                  showRecord.value = 0;
                  message.warning(t('该工艺没有生效版本'));
                }
                formModel.datasetPointList = [];
                formModel.originalProcessId = value;
              } else {
                showRecord.value = 0;
                formModel.activeVersion = undefined;
                formModel.datasetPointList = [];
                formModel.originalProcessId = undefined;
              }
              hasChange.value = true;
            },
          };
        },
      },
      {
        label: () => {
          return (
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                width: '100%',
              }}>
              <BMTableTitle title={t('动态数据')} />
              {!isView.value && (
                <Button
                  type='link'
                  style={{ paddingLeft: 0, paddingRight: 0 }}
                  onClick={() => {
                    showDynamicBatchAddModal.value = true;
                  }}>
                  {t('批量添加')}
                </Button>
              )}
            </div>
          );
        },
        field: 'datasetDynamicReportDataList',
        vIf: ({ formModel }: RenderCallbackParams) => formModel.datasetType === DatasetType.DYNAMIC_REPORT,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <DynamicReport
                  v-model:datasetDynamicReportDataList={formModel.datasetDynamicReportDataList}
                  isView={isView.value}
                  onUpdate:datasetDynamicReportDataList={() => {
                    hasChange.value = true;
                  }}
                />
              </FormItemRest>
            </>
          );
        },
      },
      {
        label: () => {
          return (
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                width: '100%',
              }}>
              <BMTableTitle title={t('批签发引用')} />
            </div>
          );
        },
        field: 'datasetLotReleaseLinkList',
        vIf: ({ formModel }: RenderCallbackParams) => formModel.datasetType === DatasetType.LOT_RELEASE_LINK,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <LotReleaseLink
                  v-model:datasetLotReleaseLinkList={formModel.datasetLotReleaseLinkList}
                  isView={isView.value}
                  processId={formModel.processId}
                  onUpdate:datasetLotReleaseLinkList={() => {
                    hasChange.value = true;
                  }}
                />
              </FormItemRest>
            </>
          );
        },
      },
      {
        label: ({ formModel }: RenderCallbackParams) => {
          return (
            <div
              style={{
                display: 'flex',
                justifyContent: 'space-between',
                alignItems: 'center',
                width: '100%',
              }}>
              <BMTableTitle title={t('数据点配置')} />
              <Space>
                {!isView.value && (
                  <>
                    <Button
                      type='link'
                      style={{ paddingLeft: 0, paddingRight: 0, color: batchModel.value ? '#ffb300' : '#1677ff' }}
                      onClick={() => {
                        batchModel.value = !batchModel.value;
                        relationComponentAddClick({});
                      }}>
                      {t('批量模式')}
                    </Button>
                    <Divider type='vertical' />
                  </>
                )}
                <Button
                  type='link'
                  style={{ paddingLeft: 0, paddingRight: 0 }}
                  onClick={() => {
                    previewPoints(formModel);
                  }}>
                  {t('预览')}
                </Button>
              </Space>
            </div>
          );
        },
        field: 'datasetPointList',
        vIf: ({ formModel }: RenderCallbackParams) => formModel.datasetType === DatasetType.POINT,
        component: ({ formModel }: RenderCallbackParams) => {
          return (
            <>
              <FormItemRest>
                <PointTable
                  v-model:datasetPointList={formModel.datasetPointList}
                  ref={pointTableRef}
                  isView={isView.value}
                  checkStatus={CHECK_STATUS}
                  datasetKey={formModel.datasetKey}
                  onUpdate:datasetPointList={() => {
                    hasChange.value = true;
                  }}
                  onAddClick={(record: Recordable) => {
                    batchModel.value = false;
                    relationComponentAddClick(record);
                  }}
                  onNodeClick={(target: ComponentNode) => {
                    setNodeActiveByTarget(target);
                  }}
                  onDeleteClick={(record: Recordable) => {
                    setDeletePoint(record);
                    relationComponentIconClick(record);
                  }}
                />
              </FormItemRest>
            </>
          );
        },
      },
    ],
  });
  return {
    formProps,

    showPointPreviewModal,
    previewPointsParams,

    showBatchAddModal,
    batchAddPoint,
    deletePoint,

    showDynamicBatchAddModal,
    dynamicBatchAddPoint,
  };
};
