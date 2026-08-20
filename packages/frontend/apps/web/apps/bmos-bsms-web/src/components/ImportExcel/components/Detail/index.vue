<template>
  <div class="detail">
    <img
      src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGcgY2xpcC1wYXRoPSJ1cmwoI2NsaXAwXzIwMl8yMDA3MSkiPjxwYXRoIGQ9Ik0wIDM5Ljc5N2EzOS43OTcgMzkuNzk3IDAgMSAwIDc5LjU5MyAwQzc5LjU5MyAxNy44MTcgNjEuNzc2IDAgMzkuNzk3IDAgMTcuODE3IDAgMCAxNy44MTcgMCAzOS43OTdaIiBmaWxsPSIjRkVEIi8+PHBhdGggZD0iTTY5LjI0MyAzOS44ODJjLS4wNzcgMTYuMjYyLTEzLjM1MSAyOS40Ny0yOS41MjggMjkuMzgtMTYuMzM5LS4wOTItMjkuNTEtMTMuNDItMjkuMzUxLTI5LjcwNS4xNDktMTYuMTggMTMuNDMzLTI5LjMyOSAyOS41MTQtMjkuMjE2IDE2LjI4LjEwOSAyOS40NDIgMTMuMzUxIDI5LjM2NSAyOS41NDFaIiBmaWxsPSIjRjY5OTM2Ii8+PHBhdGggZD0iTTY0LjM1MyAyMy41N2MtNS4yNTctNy45MS0xNC4yNTEtMTMuMTYxLTI0LjQ3NS0xMy4yMjktMTYuMDgxLS4xMTMtMjkuMzY1IDEzLjAzNS0yOS41MTkgMjkuMjExLS4xIDEwLjI1MSA1LjA4NSAxOS4zMjcgMTMuMDE3IDI0LjY3NC4zOTguMDEzLjc5Ni4wMTggMS4xOTQuMDE4IDIxLjk3NSAwIDM5Ljc5Mi0xNy44MTcgMzkuNzkyLTM5Ljc5NyAwLS4yOTMtLjAwNS0uNTg3LS4wMS0uODc3WiIgZmlsbD0iI0ZEQTU0NyIvPjxwYXRoIGQ9Ik0xMC4zNiAzOS41NTNhMjkuMTg1IDI5LjE4NSAwIDAgMCAxLjg0OCAxMC41MjZjLjQ4OC4wMTguOTguMDMyIDEuNDczLjAzMiAyLjY5IDAgOS4zMDUtLjgyNyAxMi42MTYtMS42IDMuMzEtLjc3MSAxNy45NTEtNi41MzcgMjIuNjcyLTE2LjgzNiA0LjcyLTEwLjMgNC4zNjktMTQuMjkzIDQuMzY5LTE3Ljk4OWEyOS41NjUgMjkuNTY1IDAgMCAwLTEzLjQ2LTMuMzRjLTE2LjA4MS0uMTE3LTI5LjM2NSAxMy4wMy0yOS41MTkgMjkuMjA3WiIgZmlsbD0iI0ZDQkI3NyIvPjxwYXRoIGQ9Ik0xMC42OTQgMzUuNDA4YzEzLjg5LTIuNDczIDI1LjI4My0xMi4xNTQgMzAuMTgzLTI1LjAzNS0uMzM1LS4wMTQtLjY2NS0uMDMyLTEuMDA0LS4wMzItMTQuNjc1LS4xLTI3LjAxOSAxMC44NTItMjkuMTggMjUuMDY3WiIgZmlsbD0iI0ZGRDhBRSIvPjxwYXRoIGZpbGwtcnVsZT0iZXZlbm9kZCIgY2xpcC1ydWxlPSJldmVub2RkIiBkPSJNMzkuNzc0IDIyLjU5OWEzLjE2NCAzLjE2NCAwIDAgMSAzLjE2NCAzLjE2M3YyMC43OTFhMy4xNjQgMy4xNjQgMCAxIDEtNi4zMjggMHYtMjAuNzlhMy4xNjQgMy4xNjQgMCAwIDEgMy4xNjQtMy4xNjRabTAgMzAuNzM0YTMuMTY0IDMuMTY0IDAgMSAxIDAgNi4zMjggMy4xNjQgMy4xNjQgMCAwIDEgMC02LjMyOFoiIGZpbGw9IiNGRUQiLz48L2c+PC9zdmc+"
      alt="" />
    <div class="batch-upload-topic">
      {{ t('导入失败') }}，{{ t('共计失败数量') }}：
      <span class="fail">{{ `${numObj.failNum}${t('份')}` }}</span>
    </div>
    <div class="batch-upload-btn">
      <!-- <Button :disabled="numObj.failNum" type="primary" @click="next">{{ t('下一步') }}</Button> -->
      <Button style="margin-left: 10px" @click="back">{{ t('返回重新上传') }}</Button>
    </div>
    <Card :title="t('导入失败信息')">
      <BMTable
        ref="tableInstance"
        :data-source="tableData"
        :columns="props.errorColumns"
        row-key="id"
        :auto-height="true"
        :autoHeightOffset="24"
        :scroll="{ x: 880, y: 400 }"
        :showToolBar="false"
        :showRefresh="false"
        :search="false"
        :pagination="{
          pageSize: 20,
        }"></BMTable>
    </Card>
  </div>
</template>

<script setup lang="tsx">
  import Card from '@/components/Card/index.vue';
  import { BMTable, Columns } from '@bmos/components';
  import { t } from '@bmos/i18n';

  defineOptions({
    inheritAttrs: false,
  });

  const props = defineProps({
    info: {
      type: Object,
      default: () => {},
    },
    errorColumns: {
      type: Array,
      default: () => [],
    },
  });

  const emit = defineEmits(['next', 'back']);

  const next = () => {
    emit('next');
  };
  const back = () => {
    emit('back');
  };

  const tableData = ref<any[]>([]);
  const numObj = computed(() => {
    return {
      failNum: props.info?.failNum ?? 0,
      successNum: props.info?.successNum ?? 0,
    };
  });

  onMounted(() => {
    tableData.value = props.info?.failData ?? [];
  });
</script>

<style lang="less" scoped>
  .detail {
    display: flex;
    justify-content: center;
    align-items: center;
    padding: 20px 16px;
    flex-direction: column;
    color: #909398;
    text-align: center;
    font-size: 14px;
    .batch-upload-topic {
      margin: 30px;
      .suc {
        color: #2d70ff;
      }
      .fail {
        color: #ff5e3d;
      }
    }
  }
</style>
