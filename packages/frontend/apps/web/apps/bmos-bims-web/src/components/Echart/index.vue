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
        type: [Number, String],
        default: '',
      },
      height: {
        type: [Number, String],
        default: '500px',
      },
    },
    emits: ['clickChart'],
    setup(props, { emit, slots, expose }) {
      console.log('slots', slots);
      const elRef = ref<any>();
      let echartRef: echarts.ECharts | undefined = undefined;
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
        contentEl.value = document.getElementsByClassName(`bims-layout-content`)[0];
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

      return () => <div ref={elRef} class='bims-echart' style={styles.value} />;
    },
  });

  // const props = defineProps({
  //   options: {
  //     type: Object as PropType<EChartsOption>,
  //     required: true,
  //   },
  //   width: {
  //     type: [Number, String],
  //     default: '',
  //   },
  //   height: {
  //     type: [Number, String],
  //     default: '500px',
  //   },
  // });

  // const emit = defineEmits(['clickChart']);

  // const options = computed(() => {
  //   return props.options;
  // });

  // const elRef = ref<any>();

  // const echartRef = ref<echarts.ECharts>();

  // const contentEl = ref<Element>();

  // const styles = computed<any>(() => {
  //   const width = typeof props.width == 'string' ? props.width : `${props.width}px`;
  //   const height = typeof props.height == 'string' ? props.height : `${props.height}px`;

  //   return {
  //     width,
  //     height,
  //   };
  // });

  // const initChart = () => {
  //   if (unref(elRef) && props.options) {
  //     echartRef.value = echarts.init(unref(elRef) as HTMLElement, null, {
  //       renderer: 'canvas',
  //       useDirtyRect: false,
  //     });
  //     echartRef.value?.setOption(unref(options));
  //   }
  // };

  // watch(
  //   () => options.value,
  //   options => {
  //     if (echartRef.value) {
  //       echartRef.value?.setOption(options);
  //     }
  //   },
  //   {
  //     deep: true,
  //   },
  // );

  // const resizeHandler = debounce(() => {
  //   console.log('e', echartRef.value.resize);
  //   if (echartRef.value) {
  //     echartRef.value?.resize();
  //   }
  // }, 100);

  // const contentResizeHandler = async (e: TransitionEvent) => {
  //   if (e.propertyName === 'width') {
  //     resizeHandler();
  //   }
  // };

  // onMounted(() => {
  //   initChart();

  //   window.addEventListener('resize', resizeHandler);
  //   contentEl.value = document.getElementsByClassName(`bims-layout-content`)[0];
  //   unref(contentEl) && (unref(contentEl) as Element).addEventListener('transitionend', contentResizeHandler);

  //   // 监听点击事件
  //   if (echartRef.value) {
  //     echartRef.value.on('click', params => {
  //       emit('clickChart', params);
  //     });
  //   }
  // });

  // onBeforeUnmount(() => {
  //   window.removeEventListener('resize', resizeHandler);
  //   unref(contentEl) && (unref(contentEl) as Element).removeEventListener('transitionend', contentResizeHandler);
  // });

  // onActivated(() => {
  //   if (echartRef.value) {
  //     echartRef.value.resize();
  //   }
  // });
</script>

<!-- <template>
  <div>
    <div ref="elRef" class="bims-echart" :style="styles"></div>
    <div>
      <template v-for="item in $slots" :key="item" #[item[0]]>
        <slot :name="item[0]"></slot>
      </template>
    </div>
  </div>
</template> -->
