<template>
  <div style="height: 100%">
    <Row class="header">
      <Col :span="12">
        <Breadcrumb class="crumb">
          <breadcrumb-item>{{ comRouter }}</breadcrumb-item>
          <breadcrumb-item>{{ t('批量导入') }}</breadcrumb-item>
        </Breadcrumb>
      </Col>
      <Col :span="12" class="header-btn">
        <!-- <div class="header-btn"> -->
        <Button @click="back">{{ t('返回') }}</Button>
        <!-- </div> -->
      </Col>
    </Row>
    <div style="height: calc(100% - 48px); background-color: white; overflow: auto">
      <Card class="header-card">
        <Steps v-model:current="stepVal" type="navigation" :items="stepItems" />
      </Card>
      <Card v-if="stepVal === 0">
        <ImportCom :templateFile="route.params.templateFile as string" @next="toDetail" />
      </Card>
      <Card v-else-if="stepVal === 1">
        <Detail
          :info="info"
          @back="
            () => {
              stepVal = 0;
            }
          " />
      </Card>
      <Card v-else>
        <Result
          status="success"
          :title="t('批量导入完成')"
          :sub-title="`${t('成功导入数量')}: ${info.successNum ?? 0}`">
          <template #icon>
            <img
              src="data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iODAiIGhlaWdodD0iODAiIGZpbGw9Im5vbmUiIHhtbG5zPSJodHRwOi8vd3d3LnczLm9yZy8yMDAwL3N2ZyI+PGcgY2xpcC1wYXRoPSJ1cmwoI2NsaXAwXzIwMl8yMDE0OCkiPjxwYXRoIGQ9Ik0wIDM5Ljc5N2EzOS43OTcgMzkuNzk3IDAgMSAwIDc5LjU5MyAwQzc5LjU5MyAxNy44MTcgNjEuNzc2IDAgMzkuNzk3IDAgMTcuODE3IDAgMCAxNy44MTggMCAzOS43OTdaIiBmaWxsPSIjRTNGRkYzIi8+PHBhdGggZD0iTTY5LjI0MyAzOS44ODJjLS4wNzcgMTYuMjYyLTEzLjM1MSAyOS40Ny0yOS41MjggMjkuMzc5LTE2LjMzOS0uMDktMjkuNTEtMTMuNDItMjkuMzUxLTI5LjcwNC4xNS0xNi4xOCAxMy40MzMtMjkuMzI5IDI5LjUxNC0yOS4yMTYgMTYuMjguMTA5IDI5LjQ0MiAxMy4zNTEgMjkuMzY1IDI5LjU0MVoiIGZpbGw9IiMwNUM0NkQiLz48cGF0aCBkPSJNNjQuMzUzIDIzLjU3Yy01LjI1Ny03LjkxLTE0LjI1MS0xMy4xNjEtMjQuNDc1LTEzLjIyOS0xNi4wODEtLjExMy0yOS4zNjUgMTMuMDM1LTI5LjUxOSAyOS4yMTEtLjEgMTAuMjUxIDUuMDg1IDE5LjMyNyAxMy4wMTcgMjQuNjc0LjM5OC4wMTMuNzk2LjAxOCAxLjE5NC4wMTggMjEuOTc1IDAgMzkuNzkyLTE3LjgxNyAzOS43OTItMzkuNzk3IDAtLjI5My0uMDA1LS41ODctLjAxLS44NzdaIiBmaWxsPSIjMURDRTc1Ii8+PHBhdGggZD0iTTEwLjM2IDM5LjU1M2EyOS4xODUgMjkuMTg1IDAgMCAwIDEuODQ4IDEwLjUyNmMuNDg4LjAxOC45OC4wMzIgMS40NzMuMDMyIDIuNjkgMCA5LjMwNS0uODI3IDEyLjYxNi0xLjYgMy4zMS0uNzcxIDE3Ljk1MS02LjUzNyAyMi42NzItMTYuODM2IDQuNzItMTAuMyA0LjM2OS0xNC4yOTMgNC4zNjktMTcuOTg5YTI5LjU2NSAyOS41NjUgMCAwIDAtMTMuNDYtMy4zNGMtMTYuMDgxLS4xMTctMjkuMzY1IDEzLjAzLTI5LjUxOSAyOS4yMDdaIiBmaWxsPSIjM0NEMzhFIi8+PHBhdGggZD0iTTEwLjY5NCAzNS40MDhjMTMuODktMi40NzMgMjUuMjgzLTEyLjE1NCAzMC4xODMtMjUuMDM1LS4zMzUtLjAxNC0uNjY1LS4wMzItMS4wMDQtLjAzMi0xNC42NzUtLjEtMjcuMDE5IDEwLjg1Mi0yOS4xOCAyNS4wNjdaIiBmaWxsPSIjNDhFMUFBIi8+PHBhdGggZD0iTTU2Ljk1MyAyNi41NzRhMy4xNjQgMy4xNjQgMCAwIDEgLjQ1OCA0LjM3MmwtLjA2OC4wODVMMzkuOTEgNTEuODA1YTMuMTY0IDMuMTY0IDAgMCAxLTQuMzczLjQ1OWwtLjA4NC0uMDY5LTEwLjc4MS05LjA0N2EzLjE2NCAzLjE2NCAwIDAgMSAzLjk4Mi00LjkxNmwuMDg1LjA2OSA4LjM1OCA3LjAxMiAxNS4zOTgtMTguMzVhMy4xNjQgMy4xNjQgMCAwIDEgNC4zNzItLjQ1OGwuMDg1LjA2OVoiIGZpbGw9IiNFM0ZGRjMiLz48L2c+PC9zdmc+"
              alt="" />
          </template>
          <template #extra>
            <Button type="primary" @click="back">{{ t('完成') }}</Button>
          </template>
        </Result>
      </Card>
    </div>
  </div>
</template>

<script setup lang="ts">
  import { t } from '@bmos/i18n';
  import { useRouter } from 'vue-router';
  import Card from '@/components/Card/index.vue';
  import { ImportCom, Detail } from './components';

  defineOptions({
    name: 'ImportExcel',
  });

  const route = useRoute();

  const router = useRouter();
  const comRouter = computed(() => {
    return t(router.currentRoute.value.query.fromRouteId as string);
  });

  const back = () => {
    router.back();
  };

  const stepVal = ref(0);

  const stepItems = ref([
    {
      title: t('上传文件'),
      disabled: true,
    },
    {
      title: t('执行导入'),
      disabled: true,
    },
    {
      title: t('导入完成'),
      disabled: true,
    },
  ]);

  const info = ref<any>({});
  // 下一步
  const toDetail = (data: any) => {
    info.value = data;
    if (data.errorNum) {
      stepVal.value = 1;
    } else {
      stepVal.value = 2;
    }
  };
</script>

<style lang="less" scoped>
  .mr-4 {
    margin-right: 8px;
    height: 26px;
    line-height: 24px;
    padding-inline: 10px;
    border-radius: 13px;
  }

  .header {
    position: sticky;
    top: 0;
    display: flex;
    justify-content: space-between;
    align-items: center;
    // background-color: #fff;
    flex-grow: 0;
    width: 100% !important;
    padding-bottom: 12px;
    // margin-bottom: var(--bmos-margin-small);
    backdrop-filter: blur(6px);
    z-index: 1000;
    .crumb {
      // line-height: 36px;
    }
    &-btn {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
  }

  .header-title {
    display: flex;
    justify-content: flex-start;
    align-items: center;
    background-color: #fff;
    flex-grow: 0;
    width: 100% !important;

    padding-bottom: 12px;
    margin-bottom: var(--bmos-margin-small);

    &-info {
      display: flex;
      justify-content: flex-start;
      align-items: center;
      &-code {
        color: #18191a;
        font-size: 14px;
        line-height: 1;
      }
    }
  }
  .header-card {
    position: sticky;
    top: 0;
    z-index: 1000;
    box-shadow: 0 2px 8px #f0f1f2;
  }

  .tag-1 {
    background-color: #59bf78;
    color: #ffffff;
  }
</style>
