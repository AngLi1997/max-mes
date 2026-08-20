import { FlowInstanceType } from '@/components/Flow/type';
import { reqProcessModify, reqProcessSave, reqProcessVersionCopy, reqProcessVersionSave } from '@/services';
import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
import { Cell } from '@antv/x6';
import { t } from '@bmos/i18n';
import { Button, Modal, Space, message } from 'ant-design-vue';
import { Ref, createVNode, ref } from 'vue';
import { useRouter } from 'vue-router';
import { LeftForm } from '.';
import { PROCESS_STATE } from '../../enum';
import { getProcedures, processFlowData } from '../../utils';
import { getProductCategoryId } from '../utils';

export interface ProcessIdAndVersion {
  processId: string;
  version: string;
  versionId: string;
}

export type UseHeadActionParams = LeftForm & {
  flowInstance: Ref<FlowInstanceType>;
  isSaveProcess: Ref<boolean>;
  productTree: Ref<any[]>;
  permissionCheckedKeys: Ref<string[]>;
  isNextProcedure: Ref<boolean>;
  permissionOpen: Ref<boolean>;
  originalModalJson: Ref<any>;
  versionId: Ref<string>;
  realProcessId: ComputedRef<string>;
  realVersion: ComputedRef<string>;
  getProcessInfo: Function;
};

export const useHeadAction = (headActionParams: UseHeadActionParams) => {
  const router = useRouter();
  const {
    flowInstance,
    isSaveProcess,
    leftFormRef,
    watchStatus,
    isView,
    productTree,
    permissionCheckedKeys,
    isNextProcedure,
    permissionOpen,
    originalModalJson,
    versionId,
    realProcessId,
    realVersion,
    getProcessInfo,
    spinning,
  } = headActionParams;

  const saveLoading = ref<boolean>(false);

  // 取消弹窗
  const cancelModal = () => {
    Modal.destroyAll();
  };
  // 不保存直接返回
  const noSaveBack = () => {
    Modal.destroyAll();
    router.push({
      name: 'process-config',
    });
  };
  // 返回
  const back = () => {
    // if (isSaveProcess.value || isView.value) {
    if ((isView.value || isSaveProcess.value) && watchStatus.value !== PROCESS_STATE.ADD_PROCESS) {
      noSaveBack();
    } else {
      Modal.confirm({
        title: t('提示'),
        wrapClassName: 'config-return-modal',
        icon: createVNode(ExclamationCircleOutlined),
        content: t('是否对工艺的修改进行保存'),
        footer() {
          return (
            <>
              <Space class='footer-btns'>
                <Button onClick={() => cancelModal()}>{t('取消')}</Button>
                <Button onClick={() => noSaveBack()}>{t('不保存')}</Button>
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
    }
  };

  const getSaveRequestByType = async (params: any) => {
    switch (watchStatus.value) {
      case PROCESS_STATE.ADD_PROCESS:
        return reqProcessSave({
          ...params,
          deptIds: permissionCheckedKeys.value,
          productCategoryId: getProductCategoryId(params.productId, productTree.value),
        });
      case PROCESS_STATE.ADD_VERSION:
        return reqProcessVersionSave({
          ...params,
          id: versionId.value,
          processId: realProcessId.value,
          originVersion: realVersion.value,
          productCategoryId: getProductCategoryId(params.productId, productTree.value),
        });
      case PROCESS_STATE.COPY_VERSION:
        return reqProcessVersionCopy({
          ...params,
          id: versionId.value,
          processId: realProcessId.value,
          originVersion: realVersion.value,
          deptIds: permissionCheckedKeys.value,
          productCategoryId: getProductCategoryId(params.productId, productTree.value),
        });
      case PROCESS_STATE.EDIT_VERSION:
        return reqProcessModify({
          ...params,
          id: versionId.value,
          processId: realProcessId.value,
        });
      default:
        return reqProcessSave(params);
    }
  };

  // 用于保存工艺id 和 version
  const processIdAndVersion = ref<ProcessIdAndVersion>({
    processId: '',
    version: '',
    versionId: '',
  });

  const saveFunc = async () => {
    try {
      saveLoading.value = true;
      spinning.value = true;
      const flowData = flowInstance.value.getFlowData() as {
        cells: Cell.Properties[];
      };
      const res = await leftFormRef.value?.submit();
      const processFlow = processFlowData(flowData, originalModalJson.value);
      // 校验至少配置一个工序节点
      if (flowData.cells.find(item => item.shape === 'custom-vue-node') === undefined) {
        message.error(t('请至少配置一个工序节点'));
        return Promise.reject();
      }

      // 校验每个工序节点都有流入和流出的连线
      // const findOutIn = processFlow.find(
      //   (item: any) =>
      //     item.type === FlowNodeType.USER_TASK &&
      //     (item.outgoing.length === 0 || item.incoming.length === 0),
      // );
      // if (findOutIn) {
      //   message.error(t('请为每个工序节点配置流入和流出的连线'));
      //   return Promise.reject();
      // }
      const params = {
        processModel: JSON.stringify(processFlow),
        procedures: getProcedures(flowData),
        ...res,
      } as any;
      const { data } = await getSaveRequestByType(params);
      processIdAndVersion.value = {
        processId: data?.processId,
        version: data?.version,
        versionId: data?.processVersionId,
      };
      message.success(t('保存成功'));
      isSaveProcess.value = true;
      return Promise.resolve();
    } catch (error: any) {
      error.message && message.error(error.message);
      return Promise.reject(error);
    } finally {
      saveLoading.value = false;
      spinning.value = false;
    }
  };

  const isBack = ref<boolean>(false);
  // save and back
  const saveAndBack = async () => {
    Modal.destroyAll();
    if (watchStatus.value === PROCESS_STATE.ADD_PROCESS || watchStatus.value === PROCESS_STATE.COPY_VERSION) {
      isNextProcedure.value = false;
      permissionOpen.value = true;
      isBack.value = true;
    } else {
      await saveFunc();
      noSaveBack();
    }
  };
  // save not back for save btn
  const saveNotBack = async () => {
    if (watchStatus.value === PROCESS_STATE.ADD_PROCESS || watchStatus.value === PROCESS_STATE.COPY_VERSION) {
      isNextProcedure.value = false;
      permissionOpen.value = true;
      isBack.value = false;
    } else {
      await saveFunc();
      if (watchStatus.value !== PROCESS_STATE.EDIT_VERSION) {
        router.push({
          name: 'process-flow',
          query: {
            status: PROCESS_STATE.EDIT_VERSION,
            ...unref(processIdAndVersion),
          },
        });
      } else {
        isSaveProcess.value = true;
        getProcessInfo(
          // @ts-ignore
          processIdAndVersion.value.processId,
          processIdAndVersion.value.version,
        );
      }
    }
  };

  const saveBack = async () => {
    try {
      await saveFunc();
      noSaveBack();
    } catch (error) {}
  };
  // 保存
  const save = async () => {
    Modal.confirm({
      title: t('提示'),
      icon: createVNode(ExclamationCircleOutlined),
      content: t('是否确认保存工艺'),
      async onOk() {
        saveNotBack();
        return Promise.resolve();
      },
    });
  };

  const toProcessConfig = () => {
    noSaveBack();
  };
  return {
    cancelModal,
    back,
    save,
    saveFunc,
    saveBack,
    noSaveBack,
    saveAndBack,
    toProcessConfig,
    processIdAndVersion,
    saveLoading,
    isBack,
  };
};
