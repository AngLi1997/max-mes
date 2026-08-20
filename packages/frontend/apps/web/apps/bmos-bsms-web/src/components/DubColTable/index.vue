<!-- 上下树列表组件 -->
<script lang="tsx">
  import { BMPageComponent, BMTableTitle } from '@bmos/components';

  export default defineComponent({
    name: 'DubColTable',
    props: {
      topTitle: {
        type: String,
        default: '',
      },
      bottomTitle: {
        type: String,
        default: '',
      },
      topTableProps: {
        type: Object,
        default: () => ({}),
      },
      bottomTableProps: {
        type: Object,
        default: () => ({}),
      },
    },
    emits: ['update:open', 'action'],
    setup(props, { emit, slots, expose }) {
      const topRef = ref();
      const bottomRef = ref();

      onMounted(() => {
        // topRef.value?.setRowKeys();
        // bottomRef.value?.setRowKeys();
      });

      expose({
        topRef,
        bottomRef,
      });

      const createSlot = (name: string, type: 'top' | 'bottom', index: number = 0) => {
        return name.replace(type, '') + index;
      };

      const getSlots = computed(() => {
        const topSlots: Record<string, any> = {};
        const bottomSlots: Record<string, any> = {};
        Object.keys(slots).forEach(key => {
          topSlots[createSlot(key, 'top')] = slots[key];
          bottomSlots[createSlot(key, 'bottom')] = slots[key];
        });
        return { topSlots, bottomSlots };
      });

      return () => (
        <>
          <div class='dubColTable'>
            <div class='itemTable'>
              <BMPageComponent
                ref={topRef}
                rowKeys={['id']}
                hideRightTree={true}
                showHeader={[true]}
                {...props.topTableProps}>
                {{
                  ...getSlots.value.topSlots,
                  tableTopHeaderTitle0: () => (props.topTitle ? <BMTableTitle title={props.topTitle} /> : null),
                  tableHeaderToolbar0: (data: any) => slots.topHeaderToolbar?.(data),
                }}
              </BMPageComponent>
            </div>
            <div class='itemTable'>
              <BMPageComponent
                ref={bottomRef}
                rowKeys={['id']}
                hideRightTree={false}
                showHeader={[true]}
                {...props.bottomTableProps}>
                {{
                  ...getSlots.value.bottomSlots,
                  tableTopHeaderTitle0: () => (props.bottomTitle ? <BMTableTitle title={props.bottomTitle} /> : null),
                  tableHeaderToolbar0: (data: any) => slots.bottomHeaderToolbar?.(data),
                }}
              </BMPageComponent>
            </div>
          </div>
        </>
      );
    },
  });
</script>

<style lang="less" scoped>
  .dubColTable {
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: flex-start;
    height: 100%;
    .itemTable {
      background-color: white;
      width: 100%;
      height: 50%;
      padding-bottom: 8px;
    }
  }
</style>
