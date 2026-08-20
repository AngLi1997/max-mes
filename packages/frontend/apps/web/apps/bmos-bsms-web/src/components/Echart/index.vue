<script lang="tsx">
  import type { EChartsOption } from 'echarts';
  import echarts from '@/plugins/echarts';
  import { debounce } from 'lodash-es';
  import 'echarts-wordcloud';
  import { computed, PropType, ref, unref, watch, onMounted, onBeforeUnmount, onActivated } from 'vue';

  // const prefixCls = getPrefixCls('echart');

  export default defineComponent({
    name: 'Echart',
    props: {
      options: {
        type: Object as PropType<EChartsOption>,
        required: true,
      },
      width: {
        type: String,
        default: '100%',
      },
      height: {
        type: String,
        default: '500px',
      },
    },
    emits: ['clickChart'],
    setup(props, { emit, slots, expose }) {
      const elRef = ref<any>();
      let echartRef: echarts.ECharts | undefined = undefined;
      const contentEl = ref<Element>();
      const styles = computed<any>(() => {
        const width = props.width;
        const height = props.height;
        return {
          width,
          height,
        };
      });

      const initChart = () => {
        if (!echartRef) {
          echartRef = echarts.init(unref(elRef));
        }
        echartRef.setOption(props.options);
      };

      const resizeHandler = debounce(() => {
        if (!echartRef) {
          return;
        }
        echartRef.resize();
      }, 100);

      watch(
        () => props.options,
        () => {
          if (!echartRef) {
            return;
          }
          echartRef.setOption(props.options);
        },
      );

      const contentResizeHandler = async (e: TransitionEvent) => {
        if (e.propertyName === 'width') {
          resizeHandler();
        }
      };

      onMounted(() => {
        initChart();

        window.addEventListener('resize', resizeHandler);
        contentEl.value = document.getElementsByClassName(`bsms-layout-content`)[0];
        unref(contentEl) && (unref(contentEl) as Element).addEventListener('transitionend', contentResizeHandler);

        // 监听点击事件
        if (echartRef) {
          echartRef.on('click', params => {
            emit('clickChart', params);
          });
        }
      });

      onBeforeUnmount(() => {
        window.removeEventListener('resize', resizeHandler);
        unref(contentEl) && (unref(contentEl) as Element).removeEventListener('transitionend', contentResizeHandler);
      });

      onActivated(() => {
        if (echartRef) {
          echartRef.resize();
        }
      });

      return () => <div ref={elRef} class='bsms-echart' style={styles.value} />;
    },
  });
</script>
