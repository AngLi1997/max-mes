<script lang="tsx">
  import type { EChartsOption } from 'echarts';
  import echarts from '@/plugins/echarts';
  import { debounce } from '@bmos/utils';
  import 'echarts-wordcloud';
  import { computed, PropType, ref, unref, watch, onMounted, onBeforeUnmount, onActivated } from 'vue';

  export default defineComponent({
    name: 'Echarts',
    props: {
      options: {
        type: Object as PropType<EChartsOption>,
        required: true,
      },
      width: {
        type: [Number, String],
        default: '',
      },
      height: {
        type: [Number, String],
        default: '500px',
      },
    },
    emits: ['clickChart'],
    setup(props, { emit }) {
      const elRef = ref<any>();
      let echartsRef: echarts.ECharts | undefined = undefined;
      const contentEl = ref<Element>();
      const styles = computed<any>(() => {
        const width = typeof props.width == 'string' ? props.width : `${props.width}px`;
        const height = typeof props.height == 'string' ? props.height : `${props.height}px`;
        return {
          width,
          height,
        };
      });

      const initChart = () => {
        if (!echartsRef) {
          echartsRef = echarts.init(unref(elRef));
        }
        echartsRef.setOption(props.options);
      };

      const resizeHandler = debounce(() => {
        if (!echartsRef) {
          return;
        }
        echartsRef.resize();
      }, 100);

      watch(
        () => props.options,
        () => {
          if (!echartsRef) {
            return;
          }
          echartsRef.setOption(props.options);
        },
      );

      const contentResizeHandler = async (e: Event) => {
        if ((e as TransitionEvent).propertyName === 'width') {
          resizeHandler();
        }
      };

      onMounted(() => {
        initChart();

        window.addEventListener('resize', resizeHandler);
        contentEl.value = document.getElementsByClassName(`lisms-layout-content`)[0];
        unref(contentEl) && (unref(contentEl) as Element).addEventListener('transitionend', contentResizeHandler);

        // 监听点击事件
        if (echartsRef) {
          echartsRef.on('click', params => {
            emit('clickChart', params);
          });
        }
      });

      onBeforeUnmount(() => {
        window.removeEventListener('resize', resizeHandler);
        unref(contentEl) && (unref(contentEl) as Element).removeEventListener('transitionend', contentResizeHandler);
      });

      onActivated(() => {
        if (echartsRef) {
          echartsRef.resize();
        }
      });

      return () => <div ref={elRef} class='lisms-echarts' style={styles.value} />;
    },
  });
</script>

<style lang="less" scoped>
  .lisms-echarts {
    flex: 1;
    display: flex;
    align-items: center;
  }
</style>
