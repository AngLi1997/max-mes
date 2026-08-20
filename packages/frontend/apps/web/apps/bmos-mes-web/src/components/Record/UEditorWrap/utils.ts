import {
  BreakLineClass,
  BreakLineContainerClass,
  defaultAheight,
  defaultLineConfig,
} from './enum';
import { PageBreakLine } from './type';

export const createBreakContainer = (): HTMLElement => {
  const container = document.createElement('div');
  container.className = BreakLineContainerClass;
  container.id = BreakLineContainerClass;
  return container;
};

export const createBreak = () => {
  const defaultBreak = document.createElement('div');
  let nums: number = 0;
  let beforeHeight: number = 0;
  let breakHeight: number = 0;
  // 794px*1123
  const getBreakHeight = (
    config: PageBreakLine = {
      ...defaultLineConfig,
      pageHeight: defaultAheight,
    },
  ) => {
    const {
      pageHeight = defaultAheight,
      pageHeaderHeight = 20,
      pageFooterHeight = 20,
    } = config;
    return pageHeight - pageHeaderHeight - pageFooterHeight;
  };

  const createLineNode = () => {
    const node: HTMLDivElement = defaultBreak.cloneNode(
      true,
    ) as unknown as HTMLDivElement;
    node.className = BreakLineClass;
    return node;
  };

  const setNodeStyle = (
    node: HTMLElement,
    num: number,
    config?: PageBreakLine,
  ) => {
    node.style.top = `${num * breakHeight}px`;
  };

  const breakNodes = new Map<number, HTMLElement>();

  const pargeBeark =  (container: Node, parent: Node, config?: PageBreakLine) => {
    breakHeight = getBreakHeight(config);

    const { height } = (parent as Element).getBoundingClientRect();
    const pageNumbers = Math.floor(height / breakHeight);
    const needNodes = pageNumbers - nums;

    if (needNodes === 0 && beforeHeight === breakHeight) return;
    if (needNodes === 0 && beforeHeight !== breakHeight) {
      for (let index = 0; index < container.childNodes.length; index++) {
        const element = container.childNodes[index] as HTMLElement;
        setNodeStyle(element, index + 1, config);
      }
    }

    if (beforeHeight !== breakHeight) beforeHeight = breakHeight;

    if (needNodes < 0) {
      for (let index = pageNumbers; index < nums; index++) {
        const element = container.childNodes[index];
        element?.remove();
      }
    } else {
      for (let index = nums; index < pageNumbers; index++) {
        if (breakNodes.has(index)) {
          container.appendChild(breakNodes.get(index)!);
          continue;
        }
        const node = createLineNode();
        setNodeStyle(node, index + 1, config);
        breakNodes.set(index, node);
        container.appendChild(node);
      }
    }

    nums = pageNumbers;
  };
  pargeBeark.refresh = (container: Node, parent: Node, config?: PageBreakLine)=>{
    breakHeight = getBreakHeight(config);
    for (let index = 0; index < container.childNodes.length; index++) {
      const element = container.childNodes[index] as HTMLElement;
      setNodeStyle(element, index + 1, config);
    }
  }
  return pargeBeark
};
