// 页面导出为pdf格式 //title表示为下载的标题，html表示document.querySelector('#myPrintHtml')
import html2canvas from 'html2canvas';
import type JsPDF from 'jspdf';
import { defaultConfig, pageA4Height, pageA4Width, pageEnum } from './const';
import { mToPoint } from './printUtils';
import { Config } from './types';

const htmlPdf = {
  getPage(
    html: HTMLElement,
    pdf: JsPDF,
    header: string,
    footer: string,
    config: Config = defaultConfig,
  ): Promise<any> {
    return new Promise<void>((resolve, reject) => {
      createPageByHtml(html).then(({ canvas, pageData }) => {
        const { pattern, left, top, bottom, right } = config;
        let leftNum = Number(left),
          topNum = Number(top),
          rightNum = Number(right),
          bottomNum = Number(bottom);
        const is_pattern = pattern === 1;
        //未生成pdf的html页面高度

        let startX = leftNum;
        let startY = topNum;

        let a4Width = mToPoint(pageA4Width - leftNum - rightNum) || 595.28;
        let a4Height = mToPoint(pageA4Height - topNum - bottomNum) || 841.89; //A4大小，210mmx297mm，四边各保留10mm的边距，显示区域190x277
        let leftHeight = canvas.height;

        //一页pdf显示html页面生成的canvas高度;
        let a4HeightRef = Math.floor((canvas.width / a4Width) * a4Height);

        //pdf页面偏移
        let position = 0;

        // let pageData = canvas.toDataURL('image/jpeg', 1.0);
        let index = 1,
          canvas1 = document.createElement('canvas'),
          height;

        const footerHeight = (a4Width / config.footerW) * config.footer;
        const headerHeight = (a4Width / config.headerW) * config.header;

        const addFooter = () => {
          pdf.addImage(
            footer,
            'JPEG',
            startX,
            a4Height - footerHeight,
            a4Width,
            footerHeight,
          );
        };
        const addHeader = () => {
          pdf.addImage(header, 'JPEG', startX, startY, a4Width, headerHeight);
        };
        // addHeader();
        function createImpl(canvas) {
          if (leftHeight > 0) {
            index++;

            let checkCount = 0;
            if (leftHeight > a4HeightRef) {
              let i = position + a4HeightRef;
              for (i = position + a4HeightRef; i >= position; i--) {
                let isWrite = true;
                for (let j = 0; j < canvas.width; j++) {
                  let c = canvas.getContext('2d').getImageData(j, i, 1, 1).data;

                  if (c[0] != 0xff || c[1] != 0xff || c[2] != 0xff) {
                    isWrite = false;
                    break;
                  }
                }
                if (isWrite) {
                  checkCount++;
                  if (checkCount >= 10) {
                    break;
                  }
                } else {
                  checkCount = 0;
                }
              }
              height =
                Math.round(i - position) || Math.min(leftHeight, a4HeightRef);
              if (height <= 0) {
                height = a4HeightRef;
              }
            } else {
              height = leftHeight;
            }

            canvas1.width = canvas.width;
            canvas1.height = height;

            let ctx = canvas1.getContext('2d')!;

            ctx.drawImage(
              canvas,
              0,
              position,
              canvas.width,
              height,
              0,
              0,
              canvas.width,
              height,
            );
            let pageHeight = Math.round((a4Width / canvas.width) * height);
            //pdf.setPageSize(null,pageHeight)
            if (position != 0) {
              pdf.addPage();
            }
            addHeader();

            const addHeight = (a4Width / canvas1.width) * height - footerHeight;
            pdf.addImage(
              canvas1.toDataURL('image/jpeg', 1.0),
              'JPEG',
              startX,
              Number(startY) + headerHeight,
              a4Width,
              addHeight,
            );
            leftHeight -=
              (height - footerHeight * (canvas1.width / a4Width)) <= 0
                ? height
                : height - footerHeight * (canvas1.width / a4Width);
            position += height - footerHeight * (canvas1.width / a4Width);

            addFooter();
            if (leftHeight > 0) {
              setTimeout(createImpl, 500, canvas);
            } else {
              resolve();
            }
          }
        }

        //当内容未超过pdf一页显示的范围，无需页分
        if (leftHeight < a4HeightRef) {
          pdf.addImage(
            pageData,
            'JPEG',
            28,
            15,
            a4Width,
            (a4Width / canvas.width) * leftHeight,
          );
          addFooter();
          resolve();
        } else {
          try {
            pdf.deletePage(0);

            setTimeout(createImpl, 500, canvas);
          } catch (err) {
            //console.log(err);
            reject(err);
          }
        }
      });
    });
  },
};

export const createPageByHtml = (html: HTMLElement) => {
  return html2canvas(html, {
    allowTaint: true,
    useCORS: true,
    scale: window.devicePixelRatio * 4,
  }).then(canvas => {
    let pageData = canvas.toDataURL('image/jpeg', 1.0);
    return Promise.resolve({ canvas, pageData });
  });
};

export default htmlPdf;
