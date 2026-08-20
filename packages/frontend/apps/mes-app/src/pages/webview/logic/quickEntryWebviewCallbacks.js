import {
  getComponentsApi,
  getHtmlApi,
  getRecordDataApi,
} from '@/api/webViewApi.js';
import { nullValueRef } from '@/utils/systemConfig/index.js';
import { IP_CONFIG } from '@/utils/uniStorage/const.js';
import { getStorageSync } from '@/utils/uniStorage/uniStorage.js';
import { t } from '@/utils/useBmosI18n.js';
import { ref } from 'vue';
import { encryptedString } from '../utils/encryptedString.js';
import { setComponentValue, urlQueryRef } from './fn/webViewEventCallbacks.js';

const wvRef = ref(null);
const paramsRef = ref({});
const pageBasicDataRef = ref({});

// 父组件配置Map数据
export const parentComponentsMap = new Map();
// 组件配置Map数据
export const componentsMap = new Map();
// 递归过滤树中的值组成新的数组
function filterTree(tree, result, parent) {
  tree.forEach((item) => {
    if (item.children && item.children.length > 0) {
      filterTree(
        item.children,
        result,
        parent || {
          componentType: item.componentType,
          id: item.id,
          fieldId: item.fieldId,
          configInfo: item.configInfo,
        },
      );
      parentComponentsMap.set(item.id, item);
    }
    else {
      if (item.used) {
        if (parent) {
          item.originalComponentType = item.componentType;
          item.componentType = parent.componentType;
          item.parent = { ...parent };
        }
        result.push(item);
      }
    }
  });
  return result;
}
// 设置组件state
export function setComponentState(item) {
  item.state = 'default';
  if (item.formulaId) {
    item.state = 'formula';
  }
  // 这里特殊处理一下，如果有children，就把children遍历
  if (item.children && item.children.length > 0) {
    item.children.forEach((child) => {
      setComponentState(child);
    });
  }
}
// 构建组件Map数据
export function constructComponentsMap(list) {
  componentsMap.clear();
  list.forEach((item) => {
    item.nullValue = nullValueRef.value;
    if (item.componentDetail) {
      item.componentDetail = JSON.parse(item.componentDetail);
    }
    if (item.configInfo) {
      item.configInfo = JSON.parse(item.configInfo);
    }
    if (item.formulaId) {
      item.formulaField = '';
    }
    item.value = '';
    setComponentState(item);
    componentsMap.set(item.fieldId, item);
  });
}
async function render({ wv, _data, params }) {
  wvRef.value = wv;
  paramsRef.value = params;
  const { htmlData, componentList } = await initFillData();
  const baseUrl
  = `http://${
    getStorageSync(IP_CONFIG) || '172.30.1.160:80'
  }/`;
  wvRef.value && wvRef.value.evalJS(`saveUrl('${baseUrl}')`);
  wv.evalJS(`quickEntryRender('${encryptedString(htmlData)}')`);
  wv.evalJS(`quickEntryInitElements('${encryptedString(componentList)}')`);
  if (htmlData === '') {
    wv.evalJS(`quickEntryRender('${encryptedString('<div style="font-size:20px;color: #545659;text-align: center;height: 500px;display: flex;align-items: center;justify-content: center;">暂无数据</div>')}')`);
    return;
  }
  initFillData2();
}

// 初始化模板内容及组件配置
async function initFillData() {
  const { processId, processVersion, productPlanId } = urlQueryRef.value;
  // 获取组件配置数据
  const res = await getComponentsApi({
    nodeId: paramsRef.value.nodeId,
    processId,
    processVersion,
    productPlanId,
  });
  if (res.data === null || !paramsRef.value.copyVersion) {
    return {
      htmlData: '',
      componentList: [],
    };
  }
  const componentList = filterTree(res.data.componentConfigs || [], []);
  pageBasicDataRef.value = { ...res.data } || {};
  delete pageBasicDataRef.value.componentConfigs;
  // 获取模板内容
  const res1 = await getHtmlApi({
    recordItemId: res.data.recordItemId,
    recordVersionId: res.data.recordVersionId,
  });
  let header = '';
  let footer = '';
  if (!res1.data.fileContent.includes('<!-- remove_header_flag -->')) {
    header = res1.data.docxHeader?.headerPrimary?.content || '';
    footer = res1.data.docxFooter?.footerPrimary?.content || '';
  }
  const htmlData = header + res1.data.fileContent + footer || '';
  // 构建组件Map数据
  constructComponentsMap(componentList);
  return {
    htmlData,
    componentList,
  };
}
// 填报数据及缓存数据回显
export async function initFillData2() {
  const { productPlanId } = urlQueryRef.value;
  const operationType = {
    save: 'saved',
    modify: 'changed',
  };
  if (!paramsRef.value.copyVersion) {
    return;
  }
  const apiData = {
    copyVersion: paramsRef.value.copyVersion,
    procedureStepId: pageBasicDataRef.value.procedureStepId,
    productPlanId,
    reuse: pageBasicDataRef.value.reusable,
    recordItemId: pageBasicDataRef.value.recordItemId,
  };
  // 获取组件填写的数据
  const res = await getRecordDataApi(apiData);
  const componentValues = res.data || [];
  // 已经保存到服务器的组件数据map对象
  const componentValuesMap = new Map();
  componentValues.forEach((item) => {
    componentValuesMap.set(item.fieldId, item);
  });
  // 传给webview赋值的list
  const componentList = [];
  componentsMap.forEach((component) => {
    let value = '';
    let state = '';
    let remark = '';
    let valueExtension = '';
    let emptyValue = false;
    let item = {};
    // 判断是否有保存值
    const valueItem = componentValuesMap.get(component.fieldId);
    // 服务端有数据，优先用服务端数据并清除缓存数据，否则使用缓存数据

    if (valueItem) {
      value = valueItem.value;
      state = operationType[valueItem.operationType] || 'saved';
      state = valueItem.error ? 'unusual' : state;
      emptyValue = valueItem.emptyValue || false;
    }
    else {
      value = '';
      remark = '';
      valueExtension = '';
      state = 'default';
    }
    const baseUrl
      = `http://${getStorageSync(IP_CONFIG) || '172.30.1.160:80'}`;
    switch (component.componentType) {
      case 'RADIO':
        item = { ...component, state, value, emptyValue };
        if (item.valueExtension) {
          item.valueExtension = JSON.parse(item.valueExtension);
        }
        if (item.formulaId && state !== 'unusual') {
          item.state = 'formula';
        }
        break;
      case 'CHECKBOX':
        if (!Array.isArray(value)) {
          try {
            value = JSON.parse(value);
          }
          catch (error) {
            console.log(error);
            value = [];
          }
        }
        if (item.valueExtension) {
          item.valueExtension = JSON.parse(item.valueExtension);
        }
        item = { ...component, state, value, emptyValue };
        if (item.formulaId && state !== 'unusual') {
          item.state = 'formula';
        }
        break;
      case 'HANDLE_SUBMIT_SIGN':
        item = { ...component, state, value, emptyValue };
        if (item.value) {
          item.value = `${baseUrl}/${item.value}`;
        }
        break;
      case 'HANDLE_REVIEW_SIGN':
        item = { ...component, state, value, emptyValue };
        if (item.value) {
          item.value = `${baseUrl}/${item.value}`;
        }
        break;
      case 'PHOTO':
        item = { ...component, state, value, emptyValue };
        wvRef.value.evalJS(`setPhotoValue('${item}')`);
        break;
      default:
        item = { ...component, state, value, remark, valueExtension, emptyValue };
        if (item.formulaId && state !== 'unusual') {
          item.state = 'formula';
        }
        break;
    }
    componentsMap.set(item.fieldId, item);
    componentList.push(item);
  });
  wvRef.value.evalJS(`quickEntryEchoData('${encryptedString(componentList)}')`);
}

// 点击事件
const componentClick = (data) => {
  if (!paramsRef.value.quickEntry) {
    return;
  }
  const component = componentsMap.get(data.data.id);
  if (
    [
      'RADIO',
      'CHECKBOX',
      'PHOTO',
      'HANDLE_SUBMIT_SIGN',
      'HANDLE_REVIEW_SIGN',
    ].includes(component.componentType)
  ) {
    uni.showToast({
      title: t('该组件无法快捷录入'),
      icon: 'none',
    });
    return;
  }
  if (!component.value) {
    return;
  }
  setComponentValue({
    fieldId: paramsRef.value.componentId,
    value: component.value,
    hasRight: component.hasRight,
    componentType: paramsRef.value.componentType,
    emptyValue: false,
  }, false);
  uni.navigateBack();
};

const reRenderData = ({ params }) => {
  paramsRef.value.copyVersion = params.copyVersion;
  initFillData2();
};

const eventsMap = new Map([
  ['QUICK_ENTRY_RENDER', render],
  ['QUICK_ENTRY_CLICK', componentClick],
  ['QUICK_ENTRY_INIT_DATA', reRenderData],
]);

export function executionQuickEntryEvent(params) {
  try {
    eventsMap.get(params.data.type) && eventsMap.get(params.data.type)(params);
  }
  catch (e) {
    console.log(e);
  }
}

export function toQuickEntry(data) {
  uni.navigateTo({
    url: `/pages/webview/quickEntry?quickEntry=true&componentId=${data.id}&componentType=${data.componentType}`,
  });
}
