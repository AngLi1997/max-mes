<template>
  <NormalModalForm
    v-model:open="filingOrderOpen"
    :title="t('归档顺序')"
    destroyOnClose
    :confirmLoading="confirmLoading"
    wrap-class-name="modalSizeExtraLarge filing-order-modal"
    :maskClosable="false"
    v-bind="otherProps"
    @okModal="handleFilingOrderOk">
    <div class="container">
      <div class="left">
        <div v-if="recordList.length" ref="recordListRef">
          <template v-for="item in recordList" :key="item.recordItemOrder + item.procedureName">
            <div :class="['record-item', item.className]" @click="() => handleClickRecord(item)">
              <BMIcon
                v-show="isEditVersionStatus"
                :class="['record-drag-icon', item.className.length ? 'show-drag-icon' : '']"
                type="Move" />
              <div class="record-item-procedureName">
                <span class="recordItemName">{{ item.recordItemName }}</span>
                <div class="procedureName">
                  <BMEllipsis>
                    {{ item.procedureName }}
                    <template #title>{{ item.procedureName }}</template>
                  </BMEllipsis>
                </div>
              </div>
            </div>
          </template>
        </div>
        <Empty v-else />
      </div>
      <div class="right">
        <Record ref="recordRef" style="flex: 1"></Record>
      </div>
    </div>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { message } from 'ant-design-vue';
  import { useSortable } from '@/hooks/useSortable';
  import { isNullAndUnDef } from '@bmos/utils';
  import { Record } from '@/components/Record/Record';
  import { BMIcon, Recordable } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import {
    reqProcessVersionRecordOrderReq,
    reqProcessVersionSaveOrderReq,
    reqRecordListComponentReq,
  } from '@/services';
  import { VersionStatus } from '../enum';
  import { NormalModalForm, BMEllipsis } from '@bmos/components';

  const emits = defineEmits(['update:filingOrderOpen']);
  const props = defineProps({
    filingOrderOpen: {
      type: Boolean,
      default: false,
    },
    processId: {
      type: String,
      default: '',
    },
    selectProcessVersion: {
      type: Object as PropType<Recordable>,
      default: () => ({}),
    },
  });

  const filingOrderOpen = computed<boolean>({
    get() {
      return props.filingOrderOpen;
    },
    set(val) {
      emits('update:filingOrderOpen', val);
    },
  });

  const isEditVersionStatus = computed(() => {
    return props.selectProcessVersion?.actionState?.value === VersionStatus.EDIT;
  });

  const otherProps = computed(() => {
    // 如果状态为 VersionStatus.APPROVAL和VersionStatus.CONFIRM 不显示 footer
    return {
      ...(!isEditVersionStatus.value && {
        footer: null,
      }),
    };
  });

  const recordList = ref<any>([]);

  const recordRef = ref();
  const setRecordContent = async (id: string, versionId: string) => {
    if (!recordRef.value) return;
    await nextTick();
    try {
      const { data } = await reqRecordListComponentReq({
        itemId: id,
        recordVersionId: versionId,
      } as any);
      recordRef.value?.setContentByConfig(data);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  const handleClickRecord = (item: {
    recordItemName: string;
    recordItemId: string;
    recordVersionId: string;
    recordItemOrder: string;
    reusable: boolean;
    procedureName: string;
  }) => {
    setRecordContent(item.recordItemId, item.recordVersionId);
    recordList.value = recordList.value.map(
      (record: {
        recordItemId: string;
        className: string;
        recordItemOrder: string;
        reusable: boolean;
        procedureName: string;
      }) => {
        if (
          record.recordItemId === item.recordItemId &&
          item.recordItemOrder === record.recordItemOrder &&
          item.reusable === record.reusable &&
          item.procedureName === record.procedureName
        ) {
          record.className = 'record-item-select';
        } else {
          record.className = '';
        }
        return record;
      },
    );
  };

  const getRecordList = async () => {
    try {
      const { data } = await reqProcessVersionRecordOrderReq(props.processId, props.selectProcessVersion.version);
      recordList.value = data?.map((item: any) => {
        return {
          ...item,
          className: '',
        };
      });
      if (!recordList.value.length) return;
      recordList.value[0].className = 'record-item-select';
      setRecordContent(recordList.value[0].recordItemId, recordList.value[0].recordVersionId);
    } catch (error: any) {
      error.message && message.error(error.message);
    }
  };

  // 监听 filingOrderOpen 变化， 打开时执行函数
  const recordListRef = ref<HTMLDivElement>();
  watch(
    () => filingOrderOpen.value,
    async val => {
      if (val) {
        await getRecordList();
        await nextTick();
        if (!isEditVersionStatus.value) return;
        const recordListEl = unref(recordListRef);
        if (!recordListEl) return;
        // Drag and drop sort
        const { initSortable } = useSortable(recordListEl, {
          handle: '.record-drag-icon',
          onEnd: (evt: { oldIndex: any; newIndex: any }) => {
            const { oldIndex, newIndex } = evt;

            if (isNullAndUnDef(oldIndex) || isNullAndUnDef(newIndex) || oldIndex === newIndex) {
              return;
            }
            // Sort list
            recordList.value.splice(newIndex, 0, recordList.value.splice(oldIndex, 1)[0]);
          },
        });
        initSortable();
      }
    },
  );

  const confirmLoading = ref<boolean>(false);

  const handleFilingOrderOk = async () => {
    try {
      if (!recordList.value.length) {
        filingOrderOpen.value = false;
        return;
      }
      confirmLoading.value = true;
      await reqProcessVersionSaveOrderReq({
        processId: props.processId,
        processVersion: props.selectProcessVersion.version,
        processVersionId: props.selectProcessVersion.id,
        recordOrders: recordList.value.map((item: Recordable, index: number) => {
          return {
            ...item,
            recordItemOrder: index + 1,
          };
        }),
      });
      message.success(t('保存成功'));
      filingOrderOpen.value = false;
      confirmLoading.value = false;
    } catch (error: any) {
      error.message && message.error(error.message);
    } finally {
      confirmLoading.value = false;
    }
  };
</script>

<style lang="less">
  .filing-order-modal {
    .mes-modal-body {
      height: calc(100vh - 200px - 52px - 52px);
    }
    .container {
      display: flex;
      height: calc(100vh - 200px - 52px - 52px);
      border-bottom: 1px solid var(--bmos-second-level-border-color);
      .left {
        padding: var(--bmos-padding-small) 0;
        width: 310px;
        border-right: 1px solid var(--bmos-second-level-border-color);
        overflow-y: scroll;
      }
      .right {
        height: 100%;
        flex: 1;
        .formula {
          overflow-y: auto;
          height: 100%;
        }
      }
    }
    .record-item {
      display: flex;
      align-items: center;
      justify-content: flex-start;
      padding: 8px;
      .record-drag-icon {
        cursor: move;
        font-size: 16px;
        visibility: hidden;
      }
      .show-drag-icon {
        visibility: visible;
      }
      .record-item-procedureName {
        display: flex;
        flex-direction: column;
        margin-left: var(--bmos-margin-small);
        width: calc(100% - 25px - var(--bmos-margin-small));
        .recordItemName {
          font-weight: 400;
          line-height: 20px;
          color: var(--bmos-second-level-text-color);
        }
        .procedureName {
          font-size: 12px;
          color: var(--bmos-fourth-level-text-color);
          overflow: hidden;
          text-overflow: ellipsis;
          text-wrap: nowrap;
        }
      }
    }
    .record-item-select {
      background-color: var(--bmos-primary-color-background);
    }
  }
</style>
