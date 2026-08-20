import { reqAuditCheckoutDeploymentReq, reqDeployFlowAuditReq, reqSaveFlowAuditReq } from '@/services';
import { OperationType } from '@/services/enum/const';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Cell } from '@antv/x6';
import { Recordable, formInstance } from '@bmos/components';
import { Button, Modal, Space, message } from 'ant-design-vue';
import { createVNode } from 'vue';
import { flow_STATE } from '../../enum';
import { dealAuditMegDTOList, dealAuditUserList, dealFlowData, getAuditMegDTOList, getAuditUserList } from '../utils';

export type UseSaveParams = {
  setFormRef: Ref<formInstance>;
  flowInstance: Ref<any>;
  originalModalJson: Ref<any>;
  flowDetail: Ref<any>;
  watchStatus: Ref<flow_STATE>;
  isSaveFlow: Ref<boolean>;
  sourceVersion: Ref<string>;
};

export const useSave = (useSaveContext: UseSaveParams) => {
  const { setFormRef, flowInstance, originalModalJson, flowDetail, watchStatus, isSaveFlow, sourceVersion } =
    useSaveContext;
  const router = useRouter();

  const saveLoading = ref<boolean>(false);

  const handleSave = async (params: Recordable, isPublish?: boolean) => {
    try {
      saveLoading.value = true;
      let newParams: Recordable = {
        ...params,
        ...(watchStatus.value === flow_STATE.updateVersion
          ? {
              changeVersion: true,
              logParams: {
                type: OperationType.add,
                business: t('新增流程版本'),
              },
            }
          : {
              logParams: {
                type: watchStatus.value === flow_STATE.editVersion ? OperationType.edit : OperationType.add,
                business: watchStatus.value === flow_STATE.editVersion ? t('编辑流程版本') : t('新增流程模型'),
              },
            }),
      };

      if (watchStatus.value === flow_STATE.editVersion) {
        newParams = {
          ...flowDetail.value,
          ...newParams,
          auditUserList: dealAuditUserList(params.auditUserList, flowDetail.value),
          auditMegDTOList: dealAuditMegDTOList(params.auditMegDTOList, flowDetail.value),
        };
      }

      if (watchStatus.value === flow_STATE.updateVersion) {
        newParams.flowAuditId = flowDetail.value.flowAuditId;
      }

      if (isPublish) {
        await reqDeployFlowAuditReq({
          ...newParams,
        } as any);
        message.success(t('发布成功'));
        noSaveBack();
      } else {
        await reqSaveFlowAuditReq(newParams);
        switch (watchStatus.value) {
          case flow_STATE.addFlow:
            message.success(t('保存成功'));
            break;
          case flow_STATE.editVersion:
            message.success(t('编辑成功'));
            break;
          case flow_STATE.updateVersion:
            message.success(t('升级版本成功'));
            break;
          default:
            message.success(t('保存成功'));
            break;
        }
      }
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject();
    } finally {
      saveLoading.value = false;
    }
  };
  // 保存方法
  const saveFunc = async (isPublish?: boolean) => {
    try {
      const commonForm = await setFormRef.value?.submit();
      const flowData = flowInstance.value.getFlowData() as {
        cells: Cell.Properties[];
      };
      const params = {
        ...commonForm,
        flowAuditModel: JSON.stringify(dealFlowData(flowData, originalModalJson.value)),
        auditUserList: getAuditUserList(flowData),
        auditMegDTOList: getAuditMegDTOList(flowData),
        sourceVersion: sourceVersion.value || '',
      };
      await handleSave(params, isPublish);
      isSaveFlow.value = true;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      isSaveFlow.value = false;
      return Promise.reject();
    }
  };

  // 是否校验成功
  const isCheckoutSuccess = ref<boolean>(false);
  // 校验流程 loading
  const checkoutFlowLoading = ref<boolean>(false);
  // 校验流程
  const checkoutFlow = async () => {
    try {
      checkoutFlowLoading.value = true;
      const flowData = flowInstance.value.getFlowData() as {
        cells: Cell.Properties[];
      };
      await reqAuditCheckoutDeploymentReq({
        flowAuditModel: JSON.stringify(dealFlowData(flowData, originalModalJson.value)),
        userList: getAuditUserList(flowData),
        megUserList: getAuditMegDTOList(flowData),
      });
      isCheckoutSuccess.value = true;
      message.success(t('校验成功'));
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      checkoutFlowLoading.value = false;
    }
  };

  // 取消弹窗
  const cancelModal = () => {
    Modal.destroyAll();
  };

  // 不保存直接返回
  const noSaveBack = () => {
    Modal.destroyAll();
    router.push({
      name: 'audit-config',
    });
  };

  const saveAndBack = async () => {
    await saveFunc();
    noSaveBack();
  };

  // 点击保存
  const save = async () => {
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否确认保存流程模型'),
      async onOk() {
        saveAndBack();
        return Promise.resolve();
      },
    });
  };

  // 点击返回
  const handleClickReturn = () => {
    if (watchStatus.value === flow_STATE.viewVersion || isSaveFlow.value) {
      noSaveBack();
      return;
    }
    Modal.confirm({
      title: t('提示'),
      wrapClassName: 'config-return-modal',
      icon: createVNode(ExclamationCircleOutlined),
      content: t('返回后，未保存数据将会丢失，请您谨慎操作！'),
      footer() {
        return (
          <>
            <Space class='footer-btns'>
              <Button onClick={() => cancelModal()}>{t('取消')}</Button>
              <Button onClick={() => noSaveBack()}>{t('不保存')}</Button>
              {watchStatus.value !== flow_STATE.viewVersion && (
                <Button type='primary' loading={saveLoading.value} onClick={() => saveAndBack()}>
                  {t('保存并返回')}
                </Button>
              )}
            </Space>
          </>
        );
      },
    });
  };

  // 点击发布
  const handleClickPublish = () => {
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否发布当前版本流程模型？'),
      async onOk() {
        saveFunc(true);
        return Promise.resolve();
      },
    });
  };

  return {
    save,
    checkoutFlow,
    checkoutFlowLoading,
    isCheckoutSuccess,
    handleClickReturn,
    handleClickPublish,
  };
};
