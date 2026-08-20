<template>
  <BMLayout>
    <BMBasicPage
      :title="t('成品产出')"
      @left-click="toBack"
      @right-click="toResult"
      @cancel="toBack"
      @confirm="submit"
    >
      <template #titleRight>
        <wd-button type="text">{{ t('产出结果') }}</wd-button>
      </template>
      <view class="container">
        <BMInfoDisplay
          class="info-display"
          :title="t('成品批次')"
          :basic-items="batchModelItems"
          :info-data="batchModel"
        />
        <scroll-view class="content" scroll-y="true">
          <template v-for="(item) in infoModel.productInfo" :key="item.key">
            <BMForm :ref="(el) => setFormRefs(el, item)" v-bind="formProps" />
            <wd-divider custom-class="divider" style="margin-bottom: 11.72rpx;" />
          </template>
          <view class="flex-center" @click="addProductInfo">
            <wd-icon
              class-prefix="bmos-app-icon"
              name="tianjia"
              size="14.07rpx"
              color="#2871FF"
            />
            <wd-button type="text" style="margin: 0;">{{ t('添加产出信息') }}</wd-button>
          </view>
        </scroll-view>
      </view>
    </BMBasicPage>
    <!-- 签名 -->
    <BMSignModal 
      v-model:show="showSign" 
      v-model="signValue" 
      :label-list="labelList"
      :title="t('签名确认')" 
      :signature-data="curParams"
      @confirm="confirmSignPopup"
    />
  </BMLayout>
</template>

<script setup lang="jsx">
  import { t } from '@/utils/useBmosI18n.js';
  import { BMBasicPage, BMInfoDisplay, BMForm, BMLayout, BMSignModal } from '@/BMComponents/index.js';
  import { reactive, ref, nextTick } from 'vue';
  import { getCurrentCopyRecordItem, urlQueryRef, pageBasicDataRef,
           initFillData2 } from '@/pages/webview/logic/fn/webViewEventCallbacks.js';
  import {
    onLoad
  } from '@dcloudio/uni-app';
  import Icon from 'wot-design-uni/components/wd-icon/wd-icon.vue';
  import {
    getFinishedDetail,
    getMesUnitListDownExtendBound,
    reqOutputFinishedSave
  } from '@/api';
  import { useToast } from 'wot-design-uni';
  
  const toast = useToast();
  // 返回
  const toBack = () => {
    uni.navigateBack();
  };
  const toResult = () => {
    uni.navigateTo({
      url: `/pages/businessComponents/productOutput/result?id=${productDetailRef.value.id}&field=${field.value}`
    });
  };

  const componentId = ref('');

  const batchModel = reactive({});
  const batchModelItems = [
    {
      label: t('成品名称'),
      field: 'productName'
    },
    {
      label: t('成品编码'),
      field: 'mergeCode'
    },
    {
      label: t('成品规格'),
      field: 'specification'
    },
    {
      label: t('成品批号'),
      field: 'batchNo'
    }
  ];

  const formRefs = ref({});
  const setFormRefs = (el, item) => {
    if (el) {
      formRefs.value[item.key] = el;
    }
  };
  const formProps = reactive({
    schemas: [
      {
        field: 'formTitle1',
        component: 'FormTitle',
        label: t('产出信息'),
        vIf: ({ formModel }) => {
          return formModel.first !== 1;
        },
        // 自定义组件背景色
        componentProps: {
          color: '#fff'
        },
        colProps: {
          span: 24
        }
      },
      {
        field: 'singleQuantity',
        component: 'Input',
        label: t('单件量'),
        colProps: {
          span: 8
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入整数单件量'),
              validator: (val) => {
                if (!val) return Promise.reject(t('请输入整数单件量'));
                // 输入正数
                if (Number(val) <= 0) {
                  return Promise.reject(t('请输入正数'));
                }
                if (!/^\d{1,10}(\.\d{1,9})?$/.test(val)) {
                  return Promise.reject(t('正数部分最多为10位，小数部分最多9位'));
                }
                return Promise.resolve();
              }
            }
          ];
        }
      },
      {
        field: 'singleUnitId',
        component: 'BMFormSelect',
        label: t('单位'),
        colProps: {
          span: 8
        },
        required: true,
        componentProps: () => {
          return {
            fieldNames: {
              label: 'label',
              value: 'id'
            },
            options: unitList.value
          };
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请选择单位')
            }
          ];
        }
      },
      {
        field: 'size',
        component: 'Input',
        label: t('产出件数'),
        colProps: {
          span: 7
        },
        dynamicRules: () => {
          return [
            {
              required: true,
              message: t('请输入整数产出件数'),
              validator: (val) => {
                if (!val) return Promise.reject(t('请输入整数产出件数'));
                if (!/^[1-9]\d*$/.test(val)) {
                  return Promise.reject(t('请输入正整数'));
                }
                if (Number(val) > 10000) return Promise.reject(t('小于10000的正整数'));
                return Promise.resolve();
              }
            }
          ];
        }
      },
      {
        field: 'formDelete',
        component: ({ formModel }) => {
          return <Icon
                    class-prefix="bmos-app-icon"
                    name="shanchu2"
                    size="18.75rpx"
                    color="#FF5633"
                    style={{
                      cursor: 'pointer'
                    }}
                    onClick={() => {
                      removeProductInfo(formModel.key);
                    }}
                  />;
        },
        label: () => <view style={{ opacity: 0 }}>11</view>,
        vIf: ({ formModel }) => {
          return formModel.first === 1;
        },
        colProps: {
          span: 1
        }
      }
    ]
  });
  const infoModel = reactive({
    productInfo: [
      {
        key: Date.now(),
        singleQuantity: '',
        singleUnit: '',
        singleUnitId: '',
        size: ''
      }
    ]
  });

  const addProductInfo = () => {
    const key = Date.now();
    const data = {
      key,
      first: 1,
      singleQuantity: '',
      singleUnit: '',
      singleUnitId: '',
      size: ''
    };
    infoModel.productInfo.push(data);
    nextTick(() => {
      formRefs.value[key].setFormModels(data);
    });
  };
  const removeProductInfo = (key) => {
    const index = infoModel.productInfo.findIndex((item) => item.key === key);
    infoModel.productInfo.splice(index, 1);
    delete formRefs.value[key];
  };

  const showSign = ref(false);
  const signValue = ref({
    loginName1: '',
    password1: '',
    userId1: ''
  });
  const labelList = ref([
    {
      label: t('操作人'),
      signatureAction: 42
    }
  ]);
  const curParams = ref({});
  const submit = async() => {
    try {
      const formRefList = Object.values(formRefs.value);
      const validateResult = await Promise.all(formRefList.map((formInstance) => {
        return formInstance.validate();
      }));
      const { procedureStepId, procedureStepModelId, recordItemId, recordVersionId, reusable } = pageBasicDataRef.value;
      const { batchNo, processId, processVersion, productPlanId } = urlQueryRef.value;
      const { version } = getCurrentCopyRecordItem();
      curParams.value = {
        batchNo,
        componentId: componentId.value,
        copyVersion: version,
        id: productDetailRef.value.id,
        outputList: validateResult.map(item => ({
          singleQuantity: item.singleQuantity,
          unitId: item.singleUnitId,
          number: item.size
        })),
        procedureStepId,
        procedureStepModelId,
        processId,
        processVersion,
        productPlanId,
        recordItemId,
        recordVersionId,
        reuse: reusable
      };
      showSign.value = true;
    } catch (error) {
      //
    }
  };

  const confirmSignPopup = () => {
    const { userId1 } = signValue.value;
    saveReq(userId1);
    showSign.value = false;
  };

  const saveReq = async(operatorId) => {
    try {
      curParams.value.operatorId = operatorId;
      await reqOutputFinishedSave(curParams.value);
      initFillData2();
      toBack();
    } catch (error) {
      error.message && toast.error(error.message);
    }
  };

  const unitList = ref([]);

  const setUnitOptions = async(materialId, unitId, unitName) => {
    try {
      const { data } = await getMesUnitListDownExtendBound({
        materialId
      });
      unitList.value = data.map(item => ({
        label: item.extendUnitName,
        id: item.id
      }));

      unitList.value.unshift({
        label: unitName,
        id: unitId
      });
    } catch (error) {
      unitList.value = [];
    }
  };

  const productDetailRef = ref();
  const field = ref('');
  
  const getDetail = async(query) => {
    try {
      const { id, curFieldId } = query;
      componentId.value = id;
      field.value = curFieldId;
      const { procedureStepModelId } = pageBasicDataRef.value;
      const { productPlanId } = urlQueryRef.value;
      const { version } = getCurrentCopyRecordItem();
      const { data } = await getFinishedDetail({
        componentId: id,
        procedureStepModelId,
        productPlanId,
        copyVersion: version
      });
      productDetailRef.value = data;
      batchModel.productName = data.productName;
      batchModel.mergeCode = data.productMergeCode;
      batchModel.specification = data.specification;
      batchModel.batchNo = data.productBatchNo;
      setUnitOptions(data.productId, data.unitId, data.unitName);
    } catch (error) {
      error.message && toast.error(error.message);
    }
  };

  onLoad(async(e) => {
    // #ifdef APP-PLUS
    const query = Object.fromEntries(Object.keys(e)
      .map(key => [decodeURIComponent(key), decodeURIComponent(e[key])]));
    getDetail(query);
    // #endif
    // #ifdef H5
    getDetail(e);
    // #endif
  });
</script>

<style lang="scss" scoped>
.container {
  height: 100%;
  display: flex;
  flex-direction: column;
}
.content {
  background-color: #fff;
  flex: 1;
  overflow: scroll;
  :deep(.wd-divider__content) {
    display: none;
  }
}
.info-display {
  background-color: var(--bmos-bg-form);
}
</style>
