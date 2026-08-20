import { useConfig } from '@/stores/config';
import html2canvas from 'html2canvas';
import { storeToRefs } from 'pinia';
import { imageLoadFuc } from '../utils/domutils';
import { createHeaderOrFooter } from '../utils/printUtils';
export const htmlPrint = (html: string, pagePrintCss: string) => {
  let iframe;
  let doc: any = null;
  const printStyle = `
  <style lang="css">
    @page {
      size: landscape;
      margin: 0;
    }
  </style>
  <style lang="css" media="print">
    body {
      margin: 0;
    }
    .record-component {
      box-shadow: none !important;
      background-color: transparent !important;
      border: none !important;
      overflow: visible;
      height: auto !important;
      resize: none !important;
    }
    textarea::-webkit-scrollbar {
      display: none; /* 隐藏滚动条 */
    }
    textarea {
      -ms-overflow-style: none; /* IE和Edge上隐藏滚动条 */
    }
    .print-header-space div.header {
      display: block;
    }
    .print-header div.header {
      display: block;
    }
    .print-footer-space div.footer {
      display: block !important;
    }
    .print-footer div.footer {
      display: block;
    }
    .print-footer {
      position: fixed;
      bottom: 0;
      width: 100%;
    }
    .print-area {
      break-after: page;
      position: relative;
      display: table;
    }
    thead {display: table-header-group !important;}
    tbody tr {
      -webkit-column-break-inside: avoid !important;
      page-break-inside: avoid !important;
      break-inside: avoid !important;
    }
    ${pagePrintCss}
  </style>`;
  iframe = document.createElement('iframe');
  iframe.setAttribute('id', 'print-iframe');
  // iframe.setAttribute('style', 'display:none;');
  document.body.appendChild(iframe);
  doc = iframe.contentWindow?.document;
  //这里可以自定义样式
  doc.write(printStyle); //解决出现页眉页脚和路径的问题
  doc = iframe.contentWindow?.document;
  doc.write('<div>' + html + '</div>');
  doc.close();
  iframe.contentWindow?.focus();
  iframe.contentWindow?.print();
};

export const generateImage = async (element: HTMLDivElement) => {
  const canvas = await html2canvas(element);
  return canvas.toDataURL('image/png');
};

export const printRecord = async (recordList: Array<any>, _name: string = 'demo') => {
  let allElementStr: string = '';
  let pagePrintCss: string = '';
  for (let index = 0; index < recordList.length; index++) {
    const element = recordList[index];
    const config = element.pageConfig ? JSON.parse(element.pageConfig) : { pattern: 1 };
    const target = document.getElementById(`${element.recordItemId}${element.procedureStepId}${element.copyVersion}`)
      ?.innerHTML;
    const configStore = useConfig();
    const { configs } = storeToRefs(configStore);
    const { div: headerDiv } = createHeaderOrFooter(
      element.headerContent as unknown as string,
      config.pattern,
      JSON.parse(configs.value['mes.record.margin'].value)!,
    );
    const headerImg = await generateImage(headerDiv);
    const { width: headerWidth, height: headerHeight } = headerDiv.getBoundingClientRect();
    const { div: footerDiv } = createHeaderOrFooter(
      element.footerContent as unknown as string,
      config.pattern,
      JSON.parse(configs.value['mes.record.margin'].value)!,
    );
    const footerImg = await generateImage(footerDiv);
    const { height: footerHeight } = footerDiv.getBoundingClientRect();
    console.log('1', HTMLDivElement);
    pagePrintCss += `
      #print-table-footer${index + 1} {
        content: url(${footerImg});
        position: fixed;
        bottom: 0;
        width: 100%;
        height: ${footerHeight}px;
      }
      #print-table-header${index + 1}::before {
        content: url(${headerImg});
        height: ${headerHeight}px;
      }
      .print-table-header-space${index + 1} {
        height: ${headerHeight}px;
      }
    `;
    target &&
      (allElementStr += `
      <table class="print-table" id="print-table-record${index + 1}" style="@page: { ${
        !config.pattern ? 'size: a4 landscape' : 'size: a4 portrait'
      } })>
        <thead style="display:table-header-group; height: ${headerHeight}px;">
          <div id="print-table-header${index + 1}"></div>
        </thead>
        <tbody class="print-table-tbody">
          <!--*** CONTENT GOES HERE ***-->
          <div class="tbody-print-content">${target.toString()}</div>
        </tbody>
        <tfoot class="print-table-tfoot" style="height: ${footerHeight}px;">
          <tr>
            <td>
              <!--place holder for the fixed-position footer-->
              <div id="print-table-footer${index + 1}" style="height: ${footerHeight}px;"></div>
            </td>
          </tr>
        </tfoot>
    </table>
    `);

    if (element.attachments && element.attachments.length > 0) {
      // 创建一个Promise数组来跟踪所有图片的加载
      const promises = element.attachments.map((item: any) => {
        return new Promise<HTMLImageElement>((resolve, reject) => {
          // 创建 img 标签
          const image = document.createElement('img');
          image.id = item.id;
          // 调用imageLoadFuc函数加载图片
          imageLoadFuc(image, item.path, (img: HTMLImageElement, base64: string) => {
            item.base64 = base64; // 设置图片源为Base64编码
            item.height = img.height;
            item.width = img.width;
            item['HTMLImageElement'] = img.outerHTML;
          });
          resolve(item);
        });
      });
      // 使用Promise.all等待所有图片加载完成
      Promise.all(promises)
        .then(res => {
          res.forEach(htmlImage => {
            const wrapperDiv = document.createElement('div');
            wrapperDiv.className = 'print-img';
            wrapperDiv.style.zIndex = '9999';
            wrapperDiv.style.width = `${htmlImage.width}px`;
            wrapperDiv.style.height = `${htmlImage.height}px`;
            const img = document.createElement('img');
            img.id = htmlImage.id;
            img.src = htmlImage.base64; // 假设htmlImage.base64是Base64编码的图像数据
            img.style.width = `${htmlImage.width}px`;
            img.style.height = `${htmlImage.height}px`;
            wrapperDiv.appendChild(img);
            allElementStr += wrapperDiv.outerHTML;
          });
          // 在这里执行所有图片加载完成后的逻辑，比如更新UI等
        })
        .catch(error => {
          console.error('图片加载失败', error);
          // 处理图片加载失败的情况
        });
    }
    allElementStr = allElementStr.replaceAll('<textarea', '<span style="text-decoration: underline;"');
    allElementStr = allElementStr.replaceAll('</textarea', '</span');
  }
  htmlPrint(`<div class="print-area">${allElementStr}</div>`, pagePrintCss);
};
