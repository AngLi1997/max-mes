<template>
  <NormalModalForm v-model:open="open" wrapClassName="modalSizeLarge" :title="title" destroyOnClose>
    <div class="container">
      <Steps
        :current="current"
        style="width: 100%"
        :items="[
          {
            title: t('开始生成'),
          },
          {
            title: t('数据引用确认'),
          },
          {
            title: t('批记录生成'),
          },
          {
            title: t('完成'),
          },
        ]"
        labelPlacement="horizontal"
        size="small"></Steps>
      <div v-if="open" class="content">
        <div v-show="current === StepCurrent.One" class="step-one">
          <BMForm ref="stepOneRef" v-bind="stepOneFormProps" />
        </div>
        <div v-show="current === StepCurrent.Two" class="step-two">
          <Space direction="vertical">
            <BMTableTitle :title="t('批次引用')" />
            <BMTable
              ref="stepTwoTableRef"
              :columns="stepTwoTableColumns"
              :dataSource="stepTwoTableDataSource"
              :pagination="false"
              :search="false"
              :customRow="customRow"
              :showToolBar="false"
              :scroll="{ x: 400, y: 200 }" />
          </Space>
        </div>
        <div v-show="current === StepCurrent.Three" class="step-three">
          <Space direction="vertical">
            <img style="width: 100px" :src="percentGif" :alt="t('加载中')" />
            <span style="color: var(--bmos-primary-color)">{{ percent }}%</span>
            <span>{{ t('批记录生成中，请等待') }}</span>
          </Space>
        </div>
        <div v-show="current === StepCurrent.Four" class="step-four">
          <template v-if="status === Status.SUCCESS">
            <BMIcons
              icon="CreatePdfSuccess"
              :style="{
                fontSize: '40px',
                width: '80px',
                height: '80px',
              }" />
            <span>
              {{ t('批记录生成成功，请') }}
              <Button type="link" @click="downFile">{{ t('下载批记录') }}</Button>
              {{ t('查看') }}
            </span>
          </template>
          <template v-else>
            <BMIcons
              icon="CreatePdfFail"
              :style="{
                fontSize: '40px',
                width: '80px',
                height: '80px',
              }" />
            <span>
              {{ t('批记录生成失败，请') }}
              <Button type="link" @click="reCreate">{{ t('重新生成') }}</Button>
            </span>
          </template>
        </div>
      </div>
    </div>
    <template #footer>
      <template v-if="current === StepCurrent.One">
        <Button @click="open = false">{{ t('取消') }}</Button>
        <Button type="primary" @click="nextStepHandle(1)">{{ t('下一步') }}</Button>
      </template>
      <template v-if="current === StepCurrent.Two">
        <Button @click="upStepHandle">{{ t('上一步') }}</Button>
        <Button type="primary" @click="nextStepHandle(2)">{{ t('下一步') }}</Button>
      </template>
      <template v-if="current === StepCurrent.Three">
        <Button @click="upStepHandle">{{ t('上一步') }}</Button>
        <Button @click="open = false">{{ t('取消') }}</Button>
      </template>
      <template v-if="current === StepCurrent.Four">
        <template v-if="status === Status.FAIL">
          <Button @click="open = false">{{ t('取消') }}</Button>
          <Button type="primary" @click="reCreate">{{ t('重新生成') }}</Button>
        </template>
        <Button v-else @click="open = false">{{ t('完成') }}</Button>
      </template>
    </template>
  </NormalModalForm>
</template>

<script setup lang="tsx">
  import { t } from '@bmos/i18n';
  import { NormalModalForm, Recordable, BMForm, BMTableTitle, BMTable } from '@bmos/components';
  import { useStepOne, useStepTwo, useStepThree, useStepFour, Status } from './hooks';
  import { Button, Space, Steps } from 'ant-design-vue';
  import { BMIcons } from '@bmos/icons';
  import { StepCurrent } from './enum';
  import { reqLotRecordsManageDownload } from '@/services';
  import { fileStreamDownload } from '@bmos/utils';
  import { useDragTable } from '@/hooks';
  import percentGif from '@/assets/gif/batchRelease.gif';

  const open = defineModel<boolean>('open', { default: false });

  const props = withDefaults(
    defineProps<{
      formValue: Recordable;
      isMange?: boolean;
    }>(),
    {
      formValue: () => ({}),
      isMange: false,
    },
  );

  const curKey = ref<number>(0);

  const current = ref<number>(StepCurrent.One);
  const upStepHandle = () => {
    current.value--;
    curKey.value = new Date().getTime();
    if (stepThreeTimer.value) {
      clearInterval(stepThreeTimer.value);
    }
  };

  const fileUrl = ref<string>('');

  const { stepOneRef, stepOneFormProps } = useStepOne({ props });
  const stepOneFormValue = ref<Recordable>({});

  const { stepTwoTableRef, stepTwoTableColumns, stepTwoTableDataSource, getStepTwoTableData } = useStepTwo({
    props,
    stepOneFormValue,
  });

  const { customRow } = useDragTable<Recordable>(stepTwoTableDataSource);

  const { percent, startCreate, stepThreeTimer } = useStepThree({ props, curKey, fileUrl });

  const { status } = useStepFour({ props });

  const downFile = async () => {
    try {
      const res = await reqLotRecordsManageDownload(fileUrl.value);
      const suffix = fileUrl.value?.split('.')?.pop() || 'pdf';
      fileStreamDownload(
        res,
        `${props.formValue?.name}${stepOneFormValue.value?.lotRecordsVersion}.${suffix}`,
        'application/pdf',
      );
    } catch (error: any) {}
  };
  const onSecondStepFun = async () => {
    try {
      const values = await stepOneRef.value?.validate();
      stepOneFormValue.value = values;
      getStepTwoTableData();
      return Promise.resolve();
    } catch (error) {
      return Promise.reject();
    }
  };

  const nextStepHandle = async (num: number) => {
    try {
      if (num === StepCurrent.Two) {
        await onSecondStepFun();
        current.value = num;
      } else if (num === StepCurrent.Three) {
        startCreate(stepOneFormValue.value, stepTwoTableDataSource.value, curKey.value, (isSuccess: boolean) => {
          if (isSuccess) {
            status.value = Status.SUCCESS;
          } else {
            status.value = Status.FAIL;
          }
          current.value = StepCurrent.Four;
        });
        current.value = num;
      } else {
        current.value = num;
      }
    } catch (error) {
      //
    }
  };

  const reCreate = () => {
    current.value = StepCurrent.Three;
    startCreate(stepOneFormValue.value, stepTwoTableDataSource.value, curKey.value, (isSuccess: boolean) => {
      if (isSuccess) {
        status.value = Status.SUCCESS;
      } else {
        status.value = Status.FAIL;
      }
      current.value = StepCurrent.Four;
    });
  };

  const title = ref<string>(t('批记录验证'));

  watch(
    () => open.value,
    async val => {
      if (!val) {
        current.value = StepCurrent.One;
        if (stepThreeTimer.value) {
          clearInterval(stepThreeTimer.value);
        }
      } else {
        await nextTick();
        curKey.value = new Date().getTime();
        if (props.isMange) {
          title.value = t('批记录生成');
          if (props.formValue?.archiveId) {
            stepOneRef.value?.setFormModels({
              lotRecordsVersion: props.formValue.lotRecordsVersion,
              archiveId: props.formValue.archiveId,
              lotRecordsTemplateId: props.formValue.lotRecordsTemplateId,
              planId: props.formValue.planId,
              processId: props.formValue.processId,
              productId: props.formValue.productId,
            });
            await onSecondStepFun();
            current.value = StepCurrent.Two;
          } else {
            current.value = StepCurrent.One;
            stepOneRef.value?.setFormModels({
              lotRecordsTemplateId: props.formValue.lotRecordsTemplateId,
              planId: props.formValue.planId,
              processId: props.formValue.processId,
              productId: props.formValue.productId,
            });
          }
        } else {
          title.value = t('批记录验证');
          current.value = StepCurrent.One;
          stepOneRef.value?.setFormModels({
            lotRecordsVersion: props.formValue.lotRecordsVersion,
            lotRecordsVersionId: props.formValue.lotRecordsVersionId,
            lotRecordsTemplateId: props.formValue.lotRecordsTemplateId,
          });
        }
      }
    },
  );

  onUnmounted(() => {
    if (stepThreeTimer.value) {
      clearInterval(stepThreeTimer.value);
    }
  });
</script>

<style lang="less" scoped>
  .container {
    min-height: 550px;
    height: 550px;
    .content {
      margin-top: var(--bmos-margin-large);
      display: flex;
      justify-content: center;
      align-items: center;
      height: calc(100% - 40px);
    }
    .step-one {
      max-width: 500px;
    }
    .step-two {
      display: flex;
      width: 100%;
      height: 100%;
      .mes-space {
        width: 100%;
        .mes-space-item {
          width: 100%;
        }
      }
    }
    .step-three {
      width: 400px;
      display: flex;
      justify-content: center;
      align-items: center;
      .mes-space {
        width: 100%;
      }
    }
    .step-four {
      display: flex;
      width: 400px;
      justify-content: center;
      align-items: center;
      flex-direction: column;
      .mes-btn {
        padding-left: 0;
        padding-right: 0;
      }
    }
  }
  :deep(.step-two .mes-form) {
    padding-left: 10px;
    padding-right: 10px;
  }
  :deep(.step-two .mes-form > .mes-row) {
    justify-content: flex-start;
  }
  :deep(.step-two .bm-form.mes-form-vertical .from-col) {
    margin-bottom: 20px;
  }
  :deep(.step-two .bmos-table) {
    height: 240px;
  }
  :deep(.mes-progress-line) {
    display: flex;
    flex-direction: column;
    gap: 20px;
    align-items: center;
  }
  :deep(.step-three .mes-space-item) {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
