import { getIntactMergeList } from '@/services';
import { EmitFn } from '@bmos/components';
import { pageA4Height, pageA4Width, printparamsConst } from '../utils/const';
import { getPxByConfig, imageLoadFuc, insertElementValue } from '../utils/domutils';
import { printRecord } from '../utils/print';
import { pxToPoint } from '../utils/printUtils';
import { renderUtil } from '../utils/rernderUtil';
import { RecordPropsType, emits } from './type';

const idleCallback = window.requestIdleCallback || setTimeout;

export const useRender = (record: any, emit: EmitFn<emits>, props: RecordPropsType) => {
  const attachmentsArray: any[] = [];
  const { container, initContent, configs } = record;
  const reacrdList = ref([]);
  let LoadItems = reactive({ value: 0 });
  let AllItems = reactive({ value: 0 });
  let attachmentsLen = 0;
  const currentTemplate = reactive<{
    count: number;
    templates: Array<{
      id: string;
      ele: HTMLElement;
    }>;
  }>({
    count: 0,
    templates: [],
  });
  const templateMap = new Map();
  let syncUtil = renderUtil();
  const handleComponent = (div: DocumentFragment, componentList: any[], target: HTMLElement): DocumentFragment => {
    const parentDiv = document.getElementById(target.id);
    // 同步地清空所有具有id属性的元素的文本内容
    if (parentDiv) {
      const elementsWithId = parentDiv.querySelectorAll('[name]');
      elementsWithId.forEach(element => {
        element.innerHTML = '';
      });
    }
    syncUtil(() => {
      if (parentDiv) {
        for (const component of componentList) {
          const childDiv: NodeListOf<HTMLElement> = parentDiv.querySelectorAll(
            `[id^="${component.fieldId}"]`,
          ) as NodeListOf<HTMLElement>;
          if (!childDiv || childDiv.length === 0) continue;
          const childElement = childDiv[0];
          insertElementValue(component, childElement);
        }
      }
    });
    // @ts-ignore

    return div;
  };

  const appendChild = (target: HTMLElement, childs: HTMLElement[], callback: Function = () => {}) => {
    childs.forEach(item => {
      target.appendChild(item);
    });
    callback();
  };

  const getList = async (data: any) => {
    // reacrdList.value = []
    const { processId, processVersion, productPlanId, id } = data;
    try {
      const { data } = await (props.getApi ? props.getApi : getIntactMergeList)(
        props.getApi
          ? { ...props.params }
          : {
              processId,
              processVersion,
              productPlanId: productPlanId || id,
            },
      );
      data.sort((a: any, b: any) => {
        if (Number(a.order) === Number(b.order)) {
          return a.copyVersion - b.copyVersion;
        }
        return Number(a.order) - Number(b.order);
      });
      reacrdList.value = data;
      // startRender();
      AllItems.value = data.length;
    } catch (error) {}
  };

  const setItemClassName = (element: any, target: HTMLElement) => {
    const config = element.pageConfig ? JSON.parse(element.pageConfig) : { pattern: 1 };
    const { top, bottom, left, right } = getPxByConfig(JSON.parse(configs.value[printparamsConst].value) || {});
    const pattWid = config.pattern === 1 ? pageA4Width : pageA4Height;
    const pattH = config.pattern === 1 ? pageA4Height : pageA4Width;
    const width = pxToPoint(pattWid) - left - right;
    const height = pxToPoint(pattH) - bottom - top;
    target.style.width = width + 'px';
    // target.style.width = height + 'px';
  };

  const handleAttachmentsArray = (attachments: any[], target: HTMLElement): HTMLImageElement[] => {
    attachmentsArray.push(...attachments);
    let images: HTMLImageElement[] = [];
    attachments.forEach(item => {
      const image = document.createElement('img');
      image.id = item.id;
      image.className = 'attachment-image';
      imageLoadFuc(image, item.path, (img: HTMLImageElement, base64: string) => {
        LoadItems.value += 1;
        item.base64 = base64;
        item.height = img.height;
        item.width = img.width;
      });
      images.push(image);
    });
    return images;
  };

  const startRender = (list: Array<any> = reacrdList.value) => {
    container.value.innerHTML = '';

    list.forEach(item => {
      renderRecordByData(item);
    });
  };

  const renderRecordByData = (data: Record<string, any>, target: HTMLElement = container.value) => {
    let newContent = data.fileContent;
    if (newContent.indexOf('<!-- remove_header_flag -->') < 0) {
      // !!!不可以换行,会被编辑器识别添加p标签
      newContent = `<!-- remove_header_flag -->${
        getPageNo(
          data.docxHeader?.headerPrimary?.content,
          data.docxHeader?.headerPrimary?.pageCodeHorizontalAlignment,
          true,
        ) || ''
      }<!-- remove_header_flag -->${newContent}<!-- remove_footer_flag -->${
        getPageNo(
          data.docxFooter?.footerPrimary?.content,
          data.docxFooter?.footerPrimary?.pageCodeHorizontalAlignment,
          false,
        ) || ''
      }<!-- remove_footer_flag -->`;
    }
    const content = initContent(newContent, `${data.recordItemId}${data.procedureStepId}${data.copyVersion}`);
    setItemClassName(data, content);
    templateMap.set(`${data.recordItemId}${data.procedureStepId}${data.copyVersion}`, content);
    let attachmentsEle: HTMLImageElement[] = [];
    if (data.attachments && data.attachments.length > 0) {
      attachmentsEle = handleAttachmentsArray(data.attachments, content);
      attachmentsLen = data.attachments ? data.attachments.length : 0;
    }
    appendChild(content, attachmentsEle);
    appendChild(target, [content], () => {
      nextTick(() => {
        LoadItems.value += 1;
      });
    });
    idleCallback(() => handleComponent(content, data.dataList, target));
    return true;
  };

  const getPageNo = (str: string, style: number, flag: boolean) => {
    if (!str) {
      return '';
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

  const replaceElement = () => {
    const next = currentTemplate.count + 1;
    const ele = currentTemplate.templates[next];
    if (ele) {
      const old = templateMap.get(ele.id);
      container.value.replaceChild(ele.ele, old);
    }
  };

  const printRecordStart = () => {
    // if (AllItems !== LoadItems.vlaue) {
    //   while (true) {
    //     if (AllItems === LoadItems.vlaue) break;
    //   }
    // }
    printRecord(reacrdList.value, props.node?.processName);
  };

  const isRendered = (num: number) => {
    return num === attachmentsLen + 1;
  };

  watch(
    () => currentTemplate.templates,
    val => {
      idleCallback(replaceElement);
    },
  );

  return {
    getList,
    printRecordStart,
    reacrdList,
    renderRecordByData,
    AllItems,
    LoadItems,
    isRendered,
  };
};
