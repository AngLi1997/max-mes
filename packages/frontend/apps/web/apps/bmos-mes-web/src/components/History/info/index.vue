<template>
  <div :class="['info-container', data.remark || data.comment ? 'row-gap' : '']">
    <div class="info-title">
      <span class="info-type">{{ data.operationType.label }}</span>
      <span class="info-name">
        <BMIcons
          :style="{
            marginRight: 'var(--bmos-margin-small)',
          }"
          icon="UserEdit" />
        {{ data.createUsername }}
      </span>
    </div>
    <div class="info-content">
      <span v-if="data.path">
        <Button class="down-btn" type="link" @click="() => downFile(data)">{{ t('下载文件') }}</Button>
      </span>
      <span v-if="data.comment">{{ t('审核意见') }}：{{ data.comment }}</span>
      <span v-if="data.remark">{{ t('备注') }}：{{ data.remark }}</span>
      <span v-if="data.nodeName">{{ t('节点名称') }}：{{ data.nodeName }}</span>
      <div v-if="showDetail">
        <div v-for="(item, feild) in data.detail" :key="feild">{{ detailLabel[feild] }}：{{ item }}</div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { BMIcons } from '@bmos/icons';
  import { t } from '@bmos/i18n';
  import { Recordable } from '@bmos/components';
  import { reqLotReleaseMangeDownloadByUrl } from '@/services';
  import { fileStreamDownload, isEmpty } from '@bmos/utils';

  const props = defineProps({
    data: {
      type: Object,
      default: () => ({}),
    },
    showDetail: {
      type: Boolean,
      default: false,
    },
    detailLabel: {
      type: Object,
      default: () => {},
    },
    downFileApi: {
      type: Function,
      default: reqLotReleaseMangeDownloadByUrl,
    },
    downFileType: {
      type: String,
      default: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
    },
    downFileName: {
      type: String,
      default: '',
    },
  });

  const downFile = async (data: Recordable) => {
    console.log('下载文件', data);
    try {
      const res = await props.downFileApi(data.path);
      // data.ext 为文件地址 去 / 之后的文件名
      fileStreamDownload(
        res,
        `${isEmpty(props.downFileName) ? data.path?.split('/')?.pop() : props.downFileName}`,
        props.downFileType,
      );
    } catch (error: any) {}
  };
</script>

<style scoped lang="less">
  .info-container {
    padding: 12px 10px;
    background-color: var(--bmos-background-color);
    color: var(--bmos-second-level-text-color);
    display: flex;
    flex-direction: column;
  }
  .row-gap {
    row-gap: 12px;
  }
  .info-title {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  .info-content {
    color: #808080;
    display: flex;
    flex-direction: column;
    row-gap: 8px;
  }
  .down-btn {
    padding-left: 0;
  }
</style>
