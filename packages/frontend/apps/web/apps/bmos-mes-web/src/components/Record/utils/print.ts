import PDFPlugin from './PDFPlugin';
const footerContent = `"<?xml version="1.0" encoding="utf-8"?><!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns:mc="http://schemas.openxmlformats.org/markup-compatibility/2006" xmlns:m="http://schemas.openxmlformats.org/officeDocument/2006/math" xmlns:r="http://schemas.openxmlformats.org/officeDocument/2006/relationships"><head><meta content="text/html; charset=utf-8" http-equiv="Content-Type" /><style><!--/*paged media */ div.header {display: none }div.footer {display: none } /*@media print { */@page { size: A4; margin: 10%; @top-center {content: element(header) } @bottom-center {content: element(footer) } }/*element styles*/ .del  {text-decoration:line-through;color:red;} .ins {text-decoration:none;background:#c0ffc0;padding:1px;}
 /* TABLE STYLES */ 
 /* PARAGRAPH STYLES */ 
.DocDefaults {display:block;margin-top: 0in;margin-bottom: 0in;line-height: 100%;font-size: 10.0pt;}
.Normal {display:block;font-size: 12.0pt;}

 /* CHARACTER STYLES */ --></style><script type="text/javascript"><!--function toggleDiv(divid){if(document.getElementById(divid).style.display == 'none'){document.getElementById(divid).style.display = 'block';}else{document.getElementById(divid).style.display = 'none';}}
--></script></head><body>
  
  <!-- userBodyTop goes here -->
  
  
  
  <div class="document">
  
  <p class="Normal DocDefaults "> </p></div>
  
  
  
  
  
  <div class="footer">
  
  <p class="Normal DocDefaults " style="text-align: left;"> </p></div>
  
  <!-- userBodyTail goes here -->
  
  </body></html>"`;
export const printRecord = async (recordList: Array<any>, name: string = 'demo') => {
  let pdfP = new PDFPlugin(name);
  console.log(pdfP, recordList);
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
    target && (await pdfP.add(target, config.pattern, element.headerContent, footerContent));
    if (element.attachments && element.attachments.length > 0) {
      element.attachments.forEach((item: any) => {
        pdfP.addImage(item.base64, config.pattern, item.width, item.height);
      });
    }
  }
  pdfP.print();
};
