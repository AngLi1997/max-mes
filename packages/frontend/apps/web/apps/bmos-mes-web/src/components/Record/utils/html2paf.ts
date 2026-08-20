// 页面导出为pdf格式 //title表示为下载的标题，html表示document.querySelector('#myPrintHtml')
import html2canvas from 'html2canvas';
import type JsPDF from 'jspdf';

import { defaultConfig, pageEnum } from './const';

import { Config } from './types';

//A4大小，
const A4_WIDTH = 592.28;
const A4_HEIGHT = 841.89;

const htmlPdf = {
  getPage(
    html: HTMLElement,
    pdf: JsPDF,
    headerImg: string,
    footerImg: string,
    config: Config = defaultConfig,
  ): Promise<any> {
    return new Promise<void>((resolve, reject) => {
      createPageByHtml(html, config).then(({ width, height, canvas, pageData }: any) => {
        const { pattern, contentWidth, header, footer } = config;
        console.log(config);
        const is_pattern = pattern === 1;
        pdf.addPage('a4', is_pattern ? pageEnum.P : pageEnum.L);
        // 元素在网页页面的宽度
        const elementWidth = html.offsetWidth;
        console.log(elementWidth);
        // PDF内容宽度 和 在HTML中宽度 的比， 用于将 元素在网页的高度 转化为 PDF内容内的高度， 将 元素距离网页顶部的高度  转化为 距离Canvas顶部的高度
        const rate = contentWidth / elementWidth;
        // 每一页的分页坐标， PDF高度， 初始值为根元素距离顶部的距离
        const pages = [rate * getElementTop(html)];
        // 距离PDF左边的距离，/ 2 表示居中
        const baseX = (A4_WIDTH - contentWidth) / 2; // 预留空间给左边
        // 距离PDF 页眉和页脚的间距， 留白留空
        const baseY = 15;
        // 出去页头、页眉、还有内容与两者之间的间距后 每页内容的实际高度
        const originalPageHeight = A4_HEIGHT - footer - header - 2 * baseY;

        // 添加
        const addImage = ({ _x, _y }: any) => {
          pdf.addImage(pageData, 'JPEG', _x, _y, width, height);
        };
        // 添加页脚
        const addHeader = () => {
          pdf.addImage(headerImg, 'JPEG', 0, 0, A4_WIDTH, header);
        };
        // 添加页眉
        const addFooter = () => {
          pdf.addImage(footerImg, 'JPEG', 0, A4_HEIGHT - footer, A4_WIDTH, footer);
        };

        // 增加空白遮挡
        const addBlank = ({ x, y, height }: any) => {
          pdf.setFillColor(255, 255, 255);
          pdf.rect(x, y, Math.ceil(A4_WIDTH), Math.ceil(height), 'F');
        };
        // 可能跨页元素位置更新的方法
        // 需要考虑分页元素，则需要考虑两种情况
        // 1. 普通达顶情况，如上
        // 2. 当前距离顶部高度加上元素自身高度 大于 整页高度，则需要载入一个分页点
        const updatePos = (eheight: any, top: any) => {
          // 如果高度已经超过当前页，则证明可以分页了
          if (top - (pages.length > 0 ? pages[pages.length - 1] : 0) >= originalPageHeight) {
            pages.push((pages.length > 0 ? pages[pages.length - 1] : 0) + originalPageHeight);
          }
          // 若 距离当前页顶部的高度 加上元素自身的高度 大于 一页内容的高度, 则证明元素跨页，将当前高度作为分页位置
          else if (
            top + eheight - (pages.length > 0 ? pages[pages.length - 1] : 0) > originalPageHeight &&
            top != (pages.length > 0 ? pages[pages.length - 1] : 0)
          ) {
            pages.push(top);
          }
        };
        // 普通元素更新位置的方法
        // 普通元素只需要考虑到是否到达了分页点，即当前距离顶部高度 - 上一个分页点的高度 大于 正常一页的高度，则需要载入分页点
        const updateNomalElPos = (top: any) => {
          if (top - (pages.length > 0 ? pages[pages.length - 1] : 0) > originalPageHeight) {
            pages.push((pages.length > 0 ? pages[pages.length - 1] : 0) + originalPageHeight);
          }
        };
        // 对于富文本元素，观察所得段落之间都是以<p> / <img> 元素相隔，因此不需要进行深度遍历 (仅针对个人遇到的情况)
        const traversingEditor = (nodes: any) => {
          // 遍历子节点
          for (let i = 0; i < nodes.length; ++i) {
            const one = nodes[i];
            let { offsetHeight } = one;
            let offsetTop = getElementTop(one);
            const top = (contentWidth / elementWidth) * offsetTop;
            updatePos((contentWidth / elementWidth) * offsetHeight, top);
          }
        };
        // 遍历正常的元素节点
        const traversingNodes = (nodes: any) => {
          for (let i = 0; i < nodes.length; ++i) {
            const one = nodes[i];
            // 需要判断跨页且内部存在跨页的元素
            const isDivideInside = one.classList && one.classList.contains('divide-inside');

            // 图片元素不需要继续深入，作为深度终点
            const isIMG = one.tagName === 'IMG';
            // table的每一行元素也是深度终点
            const isTableCol = one.classList && one.classList.contains('ant-table-row');
            // 特殊的富文本元素
            const isEditor = one.classList && one.classList.contains('editor');
            // 对需要处理分页的元素，计算是否跨界，若跨界，则直接将顶部位置作为分页位置，进行分页，且子元素不需要再进行判断
            let { offsetHeight } = one;
            // 计算出最终高度
            let offsetTop = getElementTop(one);

            // dom转换后距离顶部的高度
            // 转换成canvas高度
            const top = rate * offsetTop;
            console.log('xx', offsetTop, top);
            // 对于需要进行分页且内部存在需要分页（即不属于深度终点）的元素进行处理
            if (isDivideInside) {
              // 执行位置更新操作
              updatePos(rate * offsetHeight, top);
              // 执行深度遍历操作
              traversingNodes(one.childNodes);
            }
            // 对于深度终点元素进行处理
            else if (isTableCol || isIMG) {
              // dom高度转换成生成pdf的实际高度
              // 代码不考虑dom定位、边距、边框等因素，需在dom里自行考虑，如将box-sizing设置为border-box
              updatePos(rate * offsetHeight, top);
            }
            // 对于深度终点元素进行处理
            else if (isTableCol || isIMG) {
              // dom高度转换成生成pdf的实际高度
              // 代码不考虑dom定位、边距、边框等因素，需在dom里自行考虑，如将box-sizing设置为border-box
              updatePos(rate * offsetHeight, top);
            } else if (isEditor) {
              // 执行位置更新操作
              updatePos(rate * offsetHeight, top);
              // 遍历富文本节点
              traversingEditor(one.childNodes);
            }
            // 对于普通元素，则判断是否高度超过分页值，并且深入
            else {
              // 执行位置更新操作
              updateNomalElPos(top);
              // 遍历子节点
              traversingNodes(one.childNodes);
            }
          }
          return;
        };
        // 深度遍历节点的方法
        traversingNodes(html.childNodes);
        // 可能会存在遍历到底部元素为深度节点，可能存在最后一页位置未截取到的情况
        if (pages[pages.length - 1] + originalPageHeight < height) {
          pages.push(pages[pages.length - 1] + originalPageHeight);
        }
        for (let i = 0; i < pages.length; ++i) {
          // 根据分页位置新增图片
          addImage({ _x: baseX, _y: baseY + header - pages[i] });
          // 将 内容 与 页眉之间留空留白的部分进行遮白处理
          addBlank({ x: 0, y: header, height: baseY });
          // 将 内容 与 页脚之间留空留白的部分进行遮白处理
          addBlank({ x: 0, y: A4_HEIGHT - baseY - header, height: baseY });
          // 对于除最后一页外，对 内容 的多余部分进行遮白处理
          if (i < pages.length - 1) {
            // 获取当前页面需要的内容部分高度
            const imageHeight = pages[i + 1] - pages[i];
            // 对多余的内容部分进行遮白
            addBlank({ x: 0, y: baseY + imageHeight + header, height: A4_HEIGHT - imageHeight });
          }
          //添加页眉
          addHeader();
          // 添加页脚
          addFooter();
          // 若不是最后一页，则分页
          if (i !== pages.length - 1) {
            // 增加分页
            pdf.addPage();
          }
        }
        setTimeout(() => {
          resolve();
        }, 2000);
      });
    });
  },
};

export const createPageByHtml = (html: HTMLElement, config: Config) => {
  return html2canvas(html, {
    allowTaint: true,
    useCORS: true,
    scale: window.devicePixelRatio * 4, // 增加清晰度
  }).then(canvas => {
    const { contentWidth } = config;
    // 获取canavs转化后的宽度
    const canvasWidth = canvas.width;
    // 获取canvas转化后的高度
    const canvasHeight = canvas.height;
    // 高度转化为PDF的高度
    const height = (contentWidth / canvasWidth) * canvasHeight;
    let pageData = canvas.toDataURL('image/jpeg', 1.0);
    return Promise.resolve({ width: contentWidth, height, canvas, pageData });
  });
};

// 获取元素距离网页顶部的距离
// 通过遍历offsetParant获取距离顶端元素的高度值
const getElementTop = (html: HTMLElement | any) => {
  let actualTop = html?.offsetTop;
  let current = html?.offsetParent;
  while (current && current !== null) {
    actualTop += current?.offsetTop;
    current = current?.offsetParent;
  }
  return actualTop;
};
export default htmlPdf;
