<template>
  <BMModalForm ref="modalFormRef" v-model:open="open" :title="t('查看房间信息')" wrapClassName="modalSizeMedium">
    <template #footer>
      <Button @click="open = false">{{ t('取消') }}</Button>
    </template>
    <div v-for="(item, i) in basicItems" :key="i" :class="`content content${i}`">
      <div>{{ item.label }}</div>
      <div v-if="item?.field === 'timeLimit'">{{ formData[item?.field] ?? '' }}{{ t('分钟') }}</div>
      <div v-else-if="item?.field === 'envProperty'">
        <template v-if="configEnvironmentParamsList.length > 0">
          <div v-for="(envProperty, index) in configEnvironmentParamsList" :key="index" class="env-property-row">
            <div class="env-property">{{ envProperty.envPropertyName }}</div>
            <span v-for="(item, i) in envProperty.equipmentDataList" :key="i" class="env-property">
              {{ item.equipmentName }}-{{ item.equipmentDataPropertyName
              }}{{ i === envProperty.equipmentDataList.length - 1 ? '' : '、' }}
            </span>
          </div>
        </template>
        <span v-else>-</span>
      </div>
      <div v-else>{{ formData[item?.field] ?? '-' }}</div>
    </div>
  </BMModalForm>
</template>

<script setup lang="ts">
  import { BMModalForm } from '@bmos/components';
  import { t } from '@bmos/i18n';
  import { watch } from 'vue';
  import { message } from 'ant-design-vue';
  import { reqFactoryRoomInfo } from '@/services';
  import { cloneDeep } from '@bmos/utils';

  const open = ref<boolean>(false);
  const props = withDefaults(
    defineProps<{
      rowId: string;
      cleanLevelInfo: any;
    }>(),
    {
      rowId: '',
      cleanLevelInfo: {},
    },
  );
  const formData = ref<any>({ stationDetails: [] });
  const basicItems = reactive<any>([
    {
      label: t('所属分类'),
      field: 'moduleName',
    },
    {
      label: t('所属产线'),
      field: 'lineInfoList',
    },
    {
      label: t('所属楼栋'),
      field: 'tenementName',
    },
    {
      label: t('所属楼层'),
      field: 'floorName',
    },
    {
      label: t('房间名称'),
      field: 'name',
    },
    {
      label: t('房间编码'),
      field: 'code',
    },
    {
      label: t('洁净等级'),
      field: 'cleanLevel',
    },
    {
      label: t('默认效期'),
      field: 'timeLimit',
    },
    {
      label: t('描述'),
      field: 'description',
    },
    {
      label: t('绑定工位'),
      field: 'stationDetails',
    },
    {
      label: t('工位编码'),
      field: 'stationCodes',
    },
    {
      label: t('环境参数'),
      field: 'envProperty',
    },
  ]);
  const configEnvironmentParamsList = ref<any>([]);
  // 回显环境参数
  const echoEnvironmentParams = (data: any) => {
    if (data.roomEnvPropertyDTOList) {
      const obj: any = {};
      data.roomEnvPropertyDTOList.forEach((item: any) => {
        if (obj[item.envPropertyCode]) {
          obj[item.envPropertyCode].equipmentDataList.push({
            equipmentDataPropertyCode: item.equipmentDataPropertyCode,
            equipmentId: item.equipmentId,
            itemIndex: `b${obj[item.envPropertyCode].equipmentDataList.length}`,
          });
        } else {
          obj[item.envPropertyCode] = {
            envPropertyCode: item.envPropertyCode,
            envPropertyName: item.envPropertyName,
            itemIndex: `a${Object.values(obj).length}`,
            equipmentDataList: [
              {
                ...item,
                equipmentDataPropertyCode: item.equipmentDataPropertyCode,
                equipmentId: item.equipmentId,
                itemIndex: `b0`,
              },
            ],
          };
        }
      });
      configEnvironmentParamsList.value = Object.values(obj);
    } else {
      configEnvironmentParamsList.value = [];
    }
  };
  watch(
    () => open.value,
    async val => {
      await nextTick();
      if (val) {
        try {
          const res = await reqFactoryRoomInfo(props.rowId);
          const stationDetails: any = cloneDeep(res.data?.stationDetails);
          formData.value = res.data;
          formData.value.stationDetails = stationDetails?.map((item: any) => item?.name)?.join('、'); //名称;
          formData.value.stationCodes = stationDetails?.map((item: any) => item?.code)?.join('、'); //编码
          formData.value.lineInfoList = res.data.lineInfoList
            ?.map((item: any) => item.code + '-' + item.name)
            .join('、');
          echoEnvironmentParams(res.data);
        } catch (error: any) {
          message.error(error.message);
        }
      }
    },
    {
      immediate: true,
    },
  );
  const openModal = () => {
    open.value = true;
  };
  defineExpose({
    openModal,
  });
</script>

<style scoped lang="less">
  .content {
    display: flex;
    justify-content: space-between;
    margin-bottom: 20px;
    > div:nth-child(1) {
      width: 85px;
      text-align: right;
      margin-right: 20px;
      color: #606266;
    }
    > div:nth-child(2) {
      width: calc(100% - 100px);
      color: #242526;
    }
    .env-property-row {
      width: 100%;
      min-height: 64px;
      .env-property {
        padding: 8px 0;
      }
    }
    .env-property-row > :first-child {
      padding-top: 0;
    }
  }
  .content9,
  .content11 {
    border-top: 1px solid #e1e3e5;
    padding-top: 20px;
  }
</style>
