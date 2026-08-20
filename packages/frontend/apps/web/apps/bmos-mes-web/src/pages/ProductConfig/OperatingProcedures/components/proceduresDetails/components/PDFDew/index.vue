<template>
  <div class="project-display" :style="{width: pdfSize + '%'}">
    <canvas v-for="i in pdfPages" :key="i" :id="`pdf-canvas-${i}`" />
  </div>
</template>
<script lang="ts" setup>
  import * as pdfJsLib from 'pdfjs-dist';
  import * as PdfWorker from 'pdfjs-dist/build/pdf.worker.mjs';
  const props = withDefaults(
    defineProps<{
      file: string;
      type: string;
      pdfSize: number;
    }>(),
    {},
  );
  let pdfDoc: any = reactive({}); // 保存加载的pdf文件流
  let pdfPages: Ref = ref(0); // pdf文件的页数
  let pdfUrl: Ref = ref(''); //pdf文件的链接
  let pdfScale: Ref = ref(1.5); // 缩放比例
  //获取pdf文档流与pdf文件的页数
  const loadFile = async (url: any) => {
    pdfJsLib.GlobalWorkerOptions.workerSrc = PdfWorker;
    const loadingTask = pdfJsLib.getDocument(url);
    loadingTask.promise.then(pdf => {
      pdfDoc = pdf;
      pdfPages.value = pdf.numPages;
      nextTick(() => {
        renderPage(1);
      });
    });
  };
  //渲染pdf文件
  const renderPage = (num: number) => {
    pdfDoc.getPage(num).then((page: any) => {
      const canvasId = 'pdf-canvas-' + num;
      const canvas: any = document.getElementById(canvasId);
      const ctx = canvas.getContext('2d');
      const dpr = window.devicePixelRatio || 1;
      const bsr =
        ctx.webkitBackingStorePixelRatio ||
        ctx.mozBackingStorePixelRatio ||
        ctx.msBackingStorePixelRatio ||
        ctx.oBackingStorePixelRatio ||
        ctx.backingStorePixelRatio ||
        1;
      const ratio = dpr / bsr;
      const viewport = page.getViewport({ scale: pdfScale.value });
      canvas.width = viewport.width * ratio;
      canvas.height = viewport.height * ratio;
      // canvas.style.width = '100%';
      // canvas.style.height = '100%';
      ctx.setTransform(ratio, 0, 0, ratio, 0, 0);
      const renderContext = {
        canvasContext: ctx,
        viewport: viewport,
      };
      page.render(renderContext);
      if (num < pdfPages.value) {
        renderPage(num + 1);
      }
    });
  };
  watch(
    () => [props.file, props.type],
    () => {
      pdfUrl.value = props.file;
      loadFile(props.file);
    },
    {},
  );
</script>
<style lang="less" scoped>
  .project-display {
    position: relative;
    display: flex;
    margin: auto;
    flex-direction: column;
    padding-bottom: 15px;
    height: 100%;
    gap: 15px;
  }
</style>
