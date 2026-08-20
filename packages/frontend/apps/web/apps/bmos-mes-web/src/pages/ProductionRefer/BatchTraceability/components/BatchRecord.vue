<template>
  <div class="container">
    <!-- 左侧列表 -->
    <div class="left">
      <div
        v-for="(item, index) in leftInfo"
        :key="index"
        :class="['left-item', activedTab === index ? 'actived' : '']"
        @click="clickLeftItem(item, index)">
        <div>{{ item.name }}</div>
        <div class="left-item-content">{{ item.teamContent }}</div>
      </div>
    </div>
    <!-- 有批记录的 -->
    <div v-if="hasRecord" class="allRight">
      <div class="middle">
        <Record
          ref="EDITOR_INSTANCE"
          style="flex: 1"
          :headerStyle="{}"
          :activeKeys="NODE_ACTIVES"
          @node-click="NODE_CLICK"></Record>
      </div>
      <div class="right">
        <Steps v-if="StepList.length" direction="vertical" progress-dot :current="current">
          <Step v-for="(item, index) in StepList" :key="index" :title="item.operationTime">
            <template #description>
              <div class="history-content">
                <div class="history-content-item">
                  <div v-for="dom in historyContentDom" :key="dom.filed" class="history-content-item-box">
                    <div class="history-content-item-label">{{ dom.label }}:</div>
                    <div class="history-content-item-value">
                      {{
                        dom.filed === 'operationType'
                          ? item.systemCreate
                            ? t('更新')
                            : operationTypeObj[item[dom.filed]]
                          : item[dom.filed]
                      }}
                    </div>
                  </div>
                </div>
              </div>
            </template>
          </Step>
        </Steps>
        <Button v-if="showPhoto" type="primary" style="margin-left: 25px" @click="lookPhoto">
          {{ t('查看详情') }}
        </Button>
        <BMModalForm
          ref="modalFormRef"
          v-model:open="open"
          :title="t('查看详情')"
          :cancelText="t('取消')"
          :okText="t('确定')"
          wrapClassName="modalSizeMedium">
          <div v-for="(item, index) in imgSrcList" :key="index">
            <div>{{ t('取证人') }}:{{ item.createUsername }}</div>
            <div>{{ t('取证时间') }}:{{ item.createTime }}</div>
            <img style="width: 400px; margin-bottom: 20px" :src="item.imgSrc" alt="" />
          </div>
          <template #footer>
            <Button @click="open = false">{{ t('取消') }}</Button>
            <Button type="primary" @click="open = false">
              {{ t('确定') }}
            </Button>
          </template>
        </BMModalForm>
      </div>
    </div>
    <!-- 缺省页 -->
    <Empty v-else :emptyName="t('暂无批记录')" />
  </div>
</template>
<script lang="tsx" setup>
  import { t } from '@bmos/i18n';
  import { ref, onMounted } from 'vue';
  import { Record } from '@/components/Record';
  import { message, Button } from 'ant-design-vue';
  import { getIp } from '@bmos/utils';

  import {
    getPlanRetraceExecuteList,
    recordListComponent,
    getExecuteCopyVersionExistedList,
    getExecuteItemLatestData,
    getExecuteFieldDataList,
  } from '@/services';
  import { BMModalForm } from '@bmos/components';

  const props = defineProps({
    rowData: {
      type: Object,
      default: () => {},
    },
  });
  const current = computed(() => {
    return StepList.value.length;
  });
  const activedTab = ref<any>(0);
  const hasRecord = ref<any>(true);
  const showPhoto = ref<any>(false);
  const EDITOR_INSTANCE = ref();
  const leftItemInfo = ref<any>(); //存点左侧时的该项的信息
  const componentList = ref<any>();
  const allData = ref<any>(); //存回显的所有值
  const firstCopyVersion = ref<any>(); //存中间记录页的第一个复制项
  const componentListIds = ref<any>([]); //所有id集合数组
  // word节点
  const NODE_ACTIVES = ref<string[]>([]);
  const open = ref<any>(false);
  const imgSrcList = ref<any>();
  const StepList = ref<any>([]);
  // 左侧列表
  const leftInfo = ref<any>([]);
  const historyContentDom = [
    { filed: 'value', label: t('值') },
    { filed: 'operationType', label: t('操作') },
    { filed: 'operationUsername', label: t('操作人') },
    { filed: 'remark', label: t('备注') },
  ];
  const operationTypeObj: any = {
    save: t('录入'),
    modify: t('修订'),
    systemCreate: t('更新'),
    update: t('更新'),
  };
  const getPageNo = (str: string, style: number, flag: boolean) => {
    if (!str) {
      return;
    }
    if (str != '' && str.indexOf('{@pageNumber}') > 0) {
      str = str.replace('{@pageNumber}', ``);
    }
    if (flag) {
      return str + '<hr class="fhhr" style="margin:5px 0;"/>';
    } else {
      return '<hr class="fhhr" style="margin:5px 0;"/>' + str;
    }
  };
  // 渲染中间批记录
  const INIT_CONTENT = (VAL: any, pattern: number) => {
    NODE_ACTIVES.value = [];
    let content = VAL.fileContent || '';
    if (content.indexOf('<!-- remove_header_flag -->') < 0) {
      // !!!不可以换行,会被编辑器识别添加p标签
      content = `<!-- remove_header_flag -->${
        getPageNo(
          VAL.docxHeader?.headerPrimary?.content,
          VAL.docxHeader?.headerPrimary?.pageCodeHorizontalAlignment,
          true,
        ) || ''
      }<!-- remove_header_flag -->${content}<!-- remove_footer_flag -->${
        getPageNo(
          VAL.docxFooter?.footerPrimary?.content,
          VAL.docxFooter?.footerPrimary?.pageCodeHorizontalAlignment,
          false,
        ) || ''
      }<!-- remove_footer_flag -->`;
    }
    const regex = /\d{19}/g;
    componentListIds.value = [...new Set(content?.match(regex))];
    EDITOR_INSTANCE.value.setContent(content || ' ', { pattern });
  };

  // 通过fieldId回填值
  const setValueById = (data: any) => {
    componentListIds.value.forEach((item: any) => {
      const temp: any = document.getElementById(item);
      temp.value = '';
    });
    data?.forEach((item: any) => {
      const nodes: any = document.getElementById(item.fieldId) || [];
      nodes.value = item.value;
    });
  };
  const getExistedList = async (item: any) => {
    const params = {
      procedureChangeNumber: item.procedureChangeNumber,
      procedureStepId: item.procedureStepId,
      procedureStepModelId: item.procedureStepModelId,
      processChangeNumber: item.processChangeNumber,
      productPlanId: props.rowData.id,
      recordItemId: item.recordItemId,
      recordVersionId: item.recordVersionId,
      reuse: item.reuse,
    };
    try {
      //查询已存在的记录复制版本列表
      const { data } = await getExecuteCopyVersionExistedList(params);
      firstCopyVersion.value = data[0]?.version;
      if (data?.length > 0) {
        //查回填值的参数
        const backfillParams = {
          copyVersion: data[0]?.version, //默认展示第一个
          procedureStepId: item.procedureStepId,
          productPlanId: props.rowData.id,
          recordItemId: item.recordItemId,
          reuse: item.reuse,
        };
        //回显每个值
        const res = await getExecuteItemLatestData(backfillParams);
        allData.value = res.data;
        setValueById(res.data);
      }
    } catch (error: any) {
      message.error(error.message);
    }
  };

  // 点击左侧每一项
  const clickLeftItem = async (item: any, index: any) => {
    activedTab.value = index;
    leftItemInfo.value = item;
    if (!item.recordItemId || !item.recordVersionId) {
      hasRecord.value = false;
      componentList.value = [];
      return;
    } else {
      hasRecord.value = true;
    }
    try {
      const { data } = await recordListComponent({
        itemId: item.recordItemId,
        recordVersionId: item.recordVersionId,
      });
      const component = {
        ...data,
      };
      componentList.value = data.componentList;
      const pattern = JSON.parse(component.pageConfig);
      INIT_CONTENT(component, pattern.pattern);
      //去查已存在的记录复制版本列表
      await getExistedList(item);
    } catch (error: any) {
      message.error(error.message);
    }
  };
  const NODE_CLICK = async (tar: any, key: string) => {
    if (!key) return;
    showPhoto.value = false;

    NODE_ACTIVES.value = [key];
    try {
      if (firstCopyVersion.value) {
        //若未查询到数据证明该步骤没有点进去执行过 不需要再调用接口
        const params = {
          copyVersion: firstCopyVersion.value, //若有切换待改
          fieldId: key,
          procedureStepId: leftItemInfo.value?.procedureStepId,
          productPlanId: props.rowData.id,
          reuse: leftItemInfo.value?.reuse,
        };
        const { data } = await getExecuteFieldDataList(params);
        const temp = componentList.value?.find((item: any) => item.fieldId === key);
        if (temp?.componentType === 'PHOTO') {
          showPhoto.value = true;
          const temp2 = allData.value?.find((item: any) => item.fieldId === key)?.value;
          imgSrcList.value = JSON.parse(temp2)?.map((item2: any) => {
            return {
              ...item2,
              imgSrc: getIp() + item2.path,
            };
          });
        } else {
          showPhoto.value = false;
        }
        StepList.value = data;
      }
    } catch (error) {}
  };
  // 查左侧列表
  const getLeftList = async () => {
    const { data } = await getPlanRetraceExecuteList({
      id: props.rowData?.id,
    });
    data?.forEach((item: any) => {
      item.teamContent = t('班次') + item.processChangeNumber + '-' + item.procedureChangeNumber;
    });
    leftInfo.value = data;
  };
  // 拍照组件查看图片
  const lookPhoto = () => {
    open.value = true;
  };
  onMounted(async () => {
    await getLeftList();
    clickLeftItem(leftInfo.value[0], 0); //默认选中左侧列表第一个
  });
</script>
<style scoped lang="less">
  .container {
    width: 100%;
    height: 100%;
    display: flex;
    .left {
      width: 15%;
      height: 100%;
      overflow-y: scroll;
      .actived {
        background: #ebf1ff;
      }
      .left-item {
        padding: 10px 24px;
        border-radius: 8px;
        width: 100%;
        height: 58px;
        color: #6c6e73;
        .left-item-content {
          font-size: 12px;
        }
      }
      .left-item:hover {
        cursor: pointer;
        background: #ebf1ff;
      }
    }
    .allRight {
      width: 85%;
      height: 100%;
      display: flex;
    }
    .middle {
      width: 75%;
      height: 100%;
      overflow-y: scroll;
    }
    .right {
      width: 25%;
      height: 100%;
      overflow-y: scroll;
      :deep(.mes-steps-item-title) {
        font-size: 14px;
        color: #909398 !important;
      }
      .history-content {
        .history-content-item {
          padding: 10px;
          border-radius: 10px;
          background-color: #f5f6f7;
          margin-bottom: 15px;
          .history-content-item-box {
            height: 20px;
            display: flex;
            align-items: center;
            font-size: 12px;
            .history-content-item-label {
              width: 50px;
              color: #545659;
            }
            .history-content-item-value {
              width: calc(100% - 50px);
              height: 100%;
              overflow: hidden;
              text-overflow: ellipsis;
              white-space: nowrap;
              color: #242526;
            }
          }
        }
      }
    }
  }
</style>
