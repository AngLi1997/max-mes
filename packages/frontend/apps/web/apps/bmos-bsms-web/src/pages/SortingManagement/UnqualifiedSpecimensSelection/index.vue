<!-- 不合格标本分拣 -->
<template>
  <DubColTable
    ref="dubTableRef"
    :topTableProps="{
      ...topTableProps,
    }"
    :bottomTableProps="{
      ...bottomTableProps,
      treeData: checkTypeTree,
      selectedKeys: selectedKeys,
      defaultSelectedNode: checkTypeTree?.[0] || undefined,
      treeField: {
        field: {
          sortingBatchNo: 'key',
        },
      },
      showAllAddIcon: false,
      showAction: false,
    }">
    <template #topHeaderToolbar>
      <div class="table-header">
        <Button type="primary" style="margin-right: 8px" @click="() => scanRef?.focus()">{{ t('扫描分拣') }}</Button>
        <Input
          ref="scanRef"
          v-model:value="scanValue"
          style="margin-right: 16px"
          :placeholder="t('请扫描')"
          @press-enter="scanFn"></Input>
        <div style="min-width: 120px; margin-right: 8px">
          <Checkbox v-model:checked="isPlayVoice">{{ t('播放语音') }}</Checkbox>
        </div>
        <Button @click="printBox">{{ t('打印箱号') }}</Button>
      </div>
    </template>
    <template #bottomHeaderToolbar="{ treeNode }">
      <div class="table-header">
        <!-- <div style="width: 120px; margin-right: 8px">
          <Checkbox style="width: 120px" v-model:checked="isAutoPrint">{{ t('自动打印箱号') }}</Checkbox>
        </div> -->
        <Button type="primary" @click="manualSubmit(treeNode)">{{ t('手动提交') }}</Button>
      </div>
    </template>
  </DubColTable>
  <ViewModal ref="viewModelRef" :type="2" />
  <PrintBoxModal ref="printBoxModalRef" />
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import {
    getSortingUnqualifiedSampleType,
    sortingUnqualifiedSampleScan,
    sortingUnqualifiedSampleSubmit,
  } from '@/services';
  import { useDubTable } from './hooks';
  import { ViewModal } from './components';
  import DubColTable from '@/components/DubColTable/index.vue';
  import { Input, Checkbox, Modal, message } from 'ant-design-vue';
  import { ExclamationCircleOutlined } from '@ant-design/icons-vue';
  import PrintBoxModal from '@/components/PrintBoxModal/index.vue';
  import { playAudio } from '@/utils';

  defineOptions({
    name: 'UnqualifiedSpecimensSelection',
    inheritAttrs: false,
  });

  const { dubTableRef, topTableProps, bottomTableProps, fetchDubData } = useDubTable();

  const printBoxModalRef = ref<any>();

  const printBox = () => {
    printBoxModalRef.value?.openModal(2);
  };

  // 是否播放语音
  const isPlayVoice = ref<boolean>(true);

  const checkTypeTree = ref<any>([]);
  const selectedKeys = ref<string[]>([]);

  const getTree = async () => {
    const { data } = await getSortingUnqualifiedSampleType();
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

  // 扫描
  const scanRef = ref();
  const scanValue = ref('');
  const viewModelRef = ref();

  const scanFn = async () => {
    try {
      const { data } = await sortingUnqualifiedSampleScan(scanValue.value);
      viewModelRef.value?.openModal(data);
      selectedKeys.value = [data.planBatchNo];
      scanValue.value = '';
      if (isPlayVoice.value && data.voiceFile) {
        playAudio(`${window.location.origin}/${data.voiceFile}`);
      }
      await nextTick();
      fetchDubData();
      //   .then(async () => {
      //   if (isAutoPrint.value && getBottomTotal() >= MAXTOTAL) {
      //     const tempSelectNode = checkTypeTree.value.find((item: any) => item.key == selectedKeys.value?.[0]);
      //     if (!tempSelectNode) return;
      //     const { data: printBoxNo } = await sortingUnqualifiedSampleSubmit({
      //       sortingBatchNo: tempSelectNode.key,
      //       systemSortingManageId: tempSelectNode.systemSortingManageId,
      //     });
      //     printBoxModalRef.value?.request({
      //       boxNo: printBoxNo,
      //       itemType: 2,
      //     });
      //     message.success(t('操作成功'));
      //     await getTree();
      //     await fetchDubData();
      //   }
      // });
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
          await sortingUnqualifiedSampleSubmit({
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

  onMounted(async () => {
    await getTree();
  });
</script>

<style lang="less" scoped>
  .table-header {
    margin-left: 120px;
    display: flex;
    align-items: center;
    justify-content: flex-start;
  }
</style>
