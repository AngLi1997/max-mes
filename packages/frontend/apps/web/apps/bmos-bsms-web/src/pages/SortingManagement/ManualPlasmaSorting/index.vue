<!-- 血浆手动分拣 -->
<template>
  <DubColTable
    ref="dubTableRef"
    :topTableProps="{
      ...topTableProps,
      isExtraParamsChangeQuerys: [false],
      tableFields: [
        {
          default: { checkNo },
        },
      ],
    }"
    :bottomTableProps="{
      ...bottomTableProps,
      treeData: checkTypeTree,
      selectedKeys: selectedKeys,
      defaultSelectedNode: checkTypeTree[0],
      treeField: {
        field: {
          sortingBatchNo: 'key',
        },
      },
      showAllAddIcon: false,
      showAction: false,
      tableFields: [
        {
          default: { checkNo },
        },
      ],
    }">
    <template #topform-submitAfter>
      <div class="table-header">
        <Button type="primary" style="margin-right: 8px" @click="() => scanInput?.focus()">{{ t('扫描分拣') }}</Button>
        <Input
          ref="scanRef"
          v-model:value="scanValue"
          style="margin-right: 16px"
          :placeholder="t('请扫描')"
          @press-enter="scanFn"></Input>
        <div style="margin-right: 8px; min-width: 120px">
          <Checkbox v-model:checked="isPlayVoice">{{ t('播放语音') }}</Checkbox>
        </div>
        <Button @click="printBox">{{ t('打印箱号') }}</Button>
      </div>
    </template>
    <template #bottomHeaderToolbar="{ treeNode }">
      <div class="table-header">
        <div style="margin-right: 8px">
          <Checkbox v-model:checked="isAutoPrint">{{ t('自动打印箱号') }}</Checkbox>
        </div>
        <Button type="primary" @click="manualSubmit(treeNode)">{{ t('手动提交') }}</Button>
      </div>
    </template>
  </DubColTable>
  <CheckModal ref="checkModalRef" @checkSuccess="checkSuccess" />
  <ViewModal ref="viewModelRef" />
  <PrintBoxModal ref="printBoxModalRef" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { CheckModal, ViewModal } from './components';
  import { sortingPlasmaScan, getSortingPlasmaType, sortingPlasmaSubmit } from '@/services';
  import { useDubTable } from './hooks';
  import DubColTable from '@/components/DubColTable/index.vue';
  import { Modal, Input, Checkbox, message } from 'ant-design-vue';
  import { playAudio } from '@/utils';
  import PrintBoxModal from '@/components/PrintBoxModal/index.vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';

  defineOptions({
    name: 'ManualPlasmaSorting',
  });

  const { dubTableRef, topTableProps, bottomTableProps, fetchDubData, getBottomTotal } = useDubTable();

  const scanInput = ref();

  // 是否播放语音
  const isPlayVoice = ref<boolean>(true);
  // 是否自动打印
  const isAutoPrint = ref<boolean>(true);

  const printBoxModalRef = ref<any>();

  const printBox = () => {
    printBoxModalRef.value?.openModal(1);
  };

  const checkNo = ref('');

  // 分拣计划类型
  const checkTypeTree = ref<any[]>([]);

  const selectedKeys = ref<string[]>([]);

  const getTree = async () => {
    const { data } = await getSortingPlasmaType(checkNo.value);
    if (!data || data.length == 0) {
      selectedKeys.value = [];
      checkTypeTree.value = [];
      return;
    }
    const tempSelect = data.find((item: any) => item.planBatchNo == selectedKeys.value?.[0]);
    if (tempSelect) {
      selectedKeys.value = [tempSelect.planBatchNo];
    } else {
      selectedKeys.value = [data[0].planBatchNo];
    }
    checkTypeTree.value = data.map((item: any) => {
      return {
        title: `${item.typeDescribe}${item.planBatchNo != '-1' ? `(${item.planBatchNo}) ` : ''}`,
        key: item.planBatchNo,
        systemSortingManageId: item.systemSortingManageId,
      };
    });
  };

  const checkModalRef = ref();
  const checkSuccess = async (formValues: any) => {
    checkNo.value = formValues.checkNo;
    await getTree();
    await fetchDubData();
  };

  // 扫描
  const scanRef = ref();
  const scanValue = ref('');
  const viewModelRef = ref();

  const MAXTOTAL = 40; // 箱子最大容量

  const scanFn = async () => {
    try {
      if (!checkNo.value) {
        message.error(t('请先输入核查批号'));
        return;
      }
      const { data } = await sortingPlasmaScan({ checkNo: checkNo.value, plasmaOrgNo: scanValue.value });
      viewModelRef.value?.openModal(data);
      selectedKeys.value = data.planBatchNo ? [data.planBatchNo] : [];
      scanValue.value = '';
      if (isPlayVoice.value && data.voiceFile) {
        playAudio(`${window.location.origin}/${data.voiceFile}`);
      }
      await nextTick();
      await fetchDubData();
      if (isAutoPrint.value && getBottomTotal() >= MAXTOTAL) {
        const tempSelectNode = checkTypeTree.value.find((item: any) => item.key == selectedKeys.value?.[0]);
        if (!tempSelectNode) return;
        const { data: printBoxNo } = await sortingPlasmaSubmit({
          checkNo: checkNo.value,
          sortingBatchNo: tempSelectNode.key,
          systemSortingManageId: tempSelectNode.systemSortingManageId,
        });
        printBoxModalRef.value?.request({
          boxNo: printBoxNo,
          itemType: 1,
        });
        await getTree();
        await fetchDubData();
      }
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 手动提交
  const manualSubmit = async (treeNode: any) => {
    Modal.confirm({
      title: t('是否进行手动提交?'),
      icon: h(ExclamationCircleOutlined),
      async onOk() {
        try {
          await sortingPlasmaSubmit({
            checkNo: checkNo.value,
            sortingBatchNo: treeNode.key,
            systemSortingManageId: treeNode.systemSortingManageId,
          });
          message.success(t('操作成功'));
          await getTree();
          await fetchDubData();
        } catch (error: any) {
          error.message && message.error(error.message);
          return Promise.reject();
        }
      },
      onCancel() {},
    });
  };

  onActivated(() => {
    checkModalRef.value.open = true;
  });

  onMounted(() => {
    checkModalRef.value.open = true;
  });
</script>

<style lang="less" scoped>
  .table-header {
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
</style>
