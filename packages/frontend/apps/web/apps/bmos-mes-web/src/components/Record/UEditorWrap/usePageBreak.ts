import { EmitFn } from '@bmos/components';
import { BreakLineContainerID, PageBreakContainerID, defaultAheight, defaultAwidth, defaultLineConfig } from './enum';
import { pxToPoint } from '../utils/printUtils';
import { PageBreak, PageBreakLine } from './type';
import { createBreak, createBreakContainer } from './utils';
type emitsType = ['update:modelValue', 'before-init', 'ready', 'change', 'pattern-change'];
import { getParameter } from '@/services';

const handleOptions = (config: PageBreak): PageBreakLine => {
  const pageHeight = config.pattern === 1 ? defaultAheight : defaultAwidth;
  return { ...config, pageHeight };
};

const setWidth = async (config: PageBreakLine) => {
  const container = document.querySelector('.edui-editor-iframeholder')! as HTMLElement;
  // 获取默认页面距
  const { data } = await getParameter('mes.record.margin');
  const padding = JSON.parse(data.value)
  container.style.width = config.pattern === 1 ? `${defaultAwidth - pxToPoint(padding.left) - pxToPoint(padding.right)}px` : `${defaultAheight - pxToPoint(padding.left) - pxToPoint(padding.right)}px`;
  container.style.paddingLeft = (padding.left || 10)+'mm';
  container.style.paddingTop = (padding.top || 10)+'mm';
  container.style.paddingRight = (padding.right || 10)+'mm';
  container.style.paddingBottom = (padding.bottom || 10)+'mm';
  container.style.boxSizing = 'content-box' ;
};

export const usePageBreak = (emits: EmitFn<emitsType>) => {
  let EDITOR: any;
  const breakFunction = createBreak();
  const PageConfig = defaultLineConfig;
  let lineContainer: Node;
  let observeTarget: Node;
  const createPageBreak = (editor: any) => {
    EDITOR = editor;
    observeTarget = document.querySelector(`#${editor.ui.id}_iframeholder`) as unknown as Node;
    if (!observeTarget) return;
    // 打印线容器
    lineContainer = (observeTarget as HTMLElement).querySelector(BreakLineContainerID) as Node;
    if (!lineContainer) {
      lineContainer = createBreakContainer();
      observeTarget.appendChild(lineContainer);
    }

    if (!observeTarget || !lineContainer) return;

    if (window.MutationObserver) {
      const observeConfig = { attributes: true };
      const fallback: MutationCallback = (mutationsList, observer) => {
        // console.log(mutationsList, observer, 'mutationsList, observer');
        // 处理提示分界线
        nextTick(() => {
          breakFunction(lineContainer, observeTarget, handleOptions(PageConfig));
        });
      };
      const observer = new MutationObserver(fallback);
      observer.observe(observeTarget, observeConfig);
    } else {
      EDITOR.addListener('contentChange', () => {
        breakFunction(lineContainer, observeTarget, handleOptions(PageConfig));
      });
    }
  };

  const changeLineByConfig = (config: PageBreak = defaultLineConfig) => {
    const opt = handleOptions(config);
    setConfig(config);
    setWidth(opt);
    breakFunction.refresh(lineContainer, observeTarget, opt);
  };

  const setConfig = (config: PageBreak) => {
    Object.assign(PageConfig, config);
    EDITOR &&
      EDITOR.setOpt({
        initialFrameWidth: config.pattern === 1 ? defaultAwidth : defaultAheight,
      });
  };

  const togglePattern = () => {
    const config = {
      ...PageConfig,
      pattern: Number(!PageConfig.pattern),
    };
    changeLineByConfig(config);

    emits('pattern-change', config);
  };

  return { createPageBreak, changeLineByConfig, setConfig, togglePattern, setWidth };
};
