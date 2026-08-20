import PDFPlugin from './PDFPlugin';

export const printRecord = async (recordList: Array<any>, name: string = 'demo') => {
  let pdfP = new PDFPlugin(name);
  for (let index = 0; index < recordList.length; index++) {
    const element = recordList[index];
    const config = element.pageConfig ? JSON.parse(element.pageConfig) : { pattern: 1 };
    const target = document.getElementById(`${element.recordItemId}${element.procedureStepId}`);
    const textareaElements = target?.getElementsByTagName('textarea') || [];
    // 遍历所有的 p 元素并设置样式
    for (let i = 0; i < textareaElements.length; i++) {
      textareaElements[i].style.boxShadow = 'none';
      textareaElements[i].style.backgroundColor = 'transparent';
    }
    target && (await pdfP.add(target, config.pattern, element.footerContent, element.headerContent));
    if (element.attachments && element.attachments.length > 0) {
      element.attachments.forEach((item: any) => {
        pdfP.addImage(item.base64, config.pattern, item.width, item.height);
      });
    }
  }
  pdfP.print();
};
