import { useConfig } from '@/stores/config';
import JsPDF from 'jspdf';
import { storeToRefs } from 'pinia';
import { pageA4Height, pageA4Width, pageEnum, printparamsConst } from './const';
import htmlPdf, { createPageByHtml } from './html2paf';
import { createHeaderOrFooter, mToPoint, pxToPoint } from './printUtils';
import { Distance } from './types';

@DistanceFunc
class PDFPlugin {
  private JS_PDF: JsPDF;
  title: string = '';
  hOrFMap: Map<any, any>;
  header: string = '';
  footer: string = '';
  headerHeight: number = 20;
  footerHeight: number = 20;
  headerWidth: number = 20;
  footerWidth: number = 20;
  distance: Distance | undefined;
  constructor(title: string) {
    this.JS_PDF = new JsPDF('p', 'pt', 'a4');
    this.JS_PDF.setDisplayMode('fullwidth', 'continuous', 'FullScreen');
    this.title = title;
    this.hOrFMap = new Map();
    const configStore = useConfig();
    const { configs } = storeToRefs(configStore);
    this.distance = JSON.parse(configs.value[printparamsConst].value)!;
  }
  async add(
    html: HTMLElement,
    pattern: number = 1,
    header: string | HTMLElement = this.header,
    footer: string | HTMLElement = this.header,
  ) {
    await this.addPageHeader(header, pattern);
    await this.addPageFooter(footer, pattern);
    const temp = {
      header: this.headerHeight,
      headerW: this.headerWidth,
      footer: this.footerHeight,
      footerW: this.footerWidth,
      pattern,
      ...this.distance!,
    }
    console.log(temp);
    await htmlPdf.getPage(html, this.JS_PDF, this.header, this.footer, temp);
  }
  async addImage(image: string, pattern: number = 1, width: number, height: number) {
    const { left, top, bottom, right } = this.distance!;
    let leftNum = Number(left),
      topNum = Number(top),
      rightNum = Number(right),
      bottomNum = Number(bottom);
    let a4Width = mToPoint(pageA4Width - leftNum - rightNum) || 595.28;
    let a4Height = mToPoint(pageA4Height - topNum - bottomNum) || height;
    this.JS_PDF.addPage('a4', pattern === 1 ? pageEnum.P : pageEnum.L);
    this.JS_PDF.addImage(
      image,
      'JPEG',
      leftNum,
      topNum,
      a4Height > height ? a4Width : (a4Height / height) * a4Width,
      a4Height > height ? height : a4Height,
    );
  }
  setParams(config: Distance) {
    this.distance = config;
  }

  save() {
    this.JS_PDF.save(this.title + '.pdf');
  }

  private async addPageHeader(header: string | HTMLElement, pattern: number = 1) {
    if (!header) return;
    if (this.hOrFMap.has(header)) {
      this.header = this.hOrFMap.get(header);
      return;
    }
    if (header === this.header) return;
    let asHeader = header;
    let parent: HTMLElement;
    if (typeof header === 'string') {
      const { par, div } = createHeaderOrFooter(asHeader as unknown as string, pattern, this.distance!);
      const { width, height } = div.getBoundingClientRect();
      this.headerHeight = height;
      this.headerWidth = width;
      asHeader = div;
      parent = par;
    } else {
      const { height } = (asHeader as HTMLElement)?.getBoundingClientRect();
      this.headerHeight = pxToPoint(height);
    }
    await createPageByHtml(asHeader as HTMLElement).then(({ canvas, pageData }) => {
      this.header = pageData;
      this.hOrFMap.set(header, pageData);
      // parent?.remove?.();
    });
  }

  private async addPageFooter(footer: string | HTMLElement, pattern: number = 1) {
    if (!footer) return;
    if (this.hOrFMap.has(footer)) {
      this.footer = this.hOrFMap.get(footer);
      return;
    }
    if (footer === this.footer) return;
    let asFooter = footer;
    let parent: HTMLElement;
    if (typeof asFooter === 'string') {
      const { par, div } = createHeaderOrFooter(asFooter, pattern, this.distance!);
      asFooter = div;
      parent = par;

      const { width, height } = div.getBoundingClientRect();
      this.footerHeight = height;
      this.footerWidth = width;
    } else {
      const { height } = asFooter?.getBoundingClientRect();
      this.footerHeight = pxToPoint(height);
    }
    await createPageByHtml(asFooter).then(({ canvas, pageData }) => {
      this.footer = pageData;
      this.hOrFMap.set(footer, pageData);
      // parent?.remove?.();
    });
  }
  print() {
    // this.JS_PDF.autoPrint();
    const blob = this.JS_PDF.output('blob'); // 将PDF输出为Blob对象
    const url = URL.createObjectURL(blob);
    const previframe = document.querySelector('#print_iframe_record');
    if (previframe) previframe.remove();
    const iframe = document.createElement('iframe');
    iframe.style.display = 'none';
    iframe.src = url;
    iframe.id = 'print_iframe_record';

    document.body.appendChild(iframe);
    iframe.contentWindow!.onafterprint = val => {
      iframe.remove();
    };
    // 等待iframe加载完毕后进行打印
    iframe.onload = function () {
      iframe.contentWindow!.print();
    };
  }
  open() {
    // this.JS_PDF.autoPrint();
    const blob = this.JS_PDF.output('blob'); // 将PDF输出为Blob对象
    const url = URL.createObjectURL(blob);
    window.open(url);
  }
}

function DistanceFunc(target: typeof PDFPlugin) {}

export default PDFPlugin;
