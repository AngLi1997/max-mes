<!-- 左右列表组件 -->
<script lang="tsx">
  import { DataRequestFn, BMPageComponent, BMTableTitle } from '@bmos/components';
  import { t } from '@bmos/i18n';

  export default defineComponent({
    name: 'DubRowTable',
    props: {
      leftTitle: {
        type: String,
        default: t('左边列表'),
      },
      rightTitle: {
        type: String,
        default: t('右边列表'),
      },
      leftTableProps: {
        type: Object,
        default: () => ({}),
      },
      rightTableProps: {
        type: Object,
        default: () => ({}),
      },
    },
    emits: ['update:open', 'action'],
    setup(props, { emit, slots, expose }) {
      const leftRef = ref();
      const rightRef = ref();

      onMounted(() => {
        // leftRef.value?.setRowKeys();
        // rightRef.value?.setRowKeys();
      });

      expose({
        leftRef,
        rightRef,
      });

      const createSlot = (name: string, type: 'left' | 'right', index: number = 0) => {
        return name.replace(type, '') + index;
      };

      const getSlots = computed(() => {
        const leftSlots: Record<string, any> = {};
        const rightSlots: Record<string, any> = {};
        Object.keys(slots).forEach(key => {
          leftSlots[createSlot(key, 'left')] = slots[key];
          rightSlots[createSlot(key, 'right')] = slots[key];
        });
        return { leftSlots, rightSlots };
      });

      return () => (
        <>
          <div class='dubRowTable'>
            <div class='itemTable'>
              <BMPageComponent
                ref={leftRef}
                rowKeys={['id']}
                hideRightTree={true}
                showHeader={[true]}
                {...props.leftTableProps}>
                {{
                  ...getSlots.value.leftSlots,
                  tableTopHeaderTitle0: () => <BMTableTitle title={props.leftTitle} />,
                  tableHeaderToolbar0: slots.leftHeaderToolbar,
                }}
              </BMPageComponent>
            </div>
            <div class='itemTable'>
              <BMPageComponent
                ref={rightRef}
                rowKeys={['id']}
                hideRightTree={true}
                showHeader={[true]}
                {...props.rightTableProps}>
                {{
                  ...getSlots.value.rightSlots,
                  tableTopHeaderTitle0: () => <BMTableTitle title={props.rightTitle} />,
                  tableHeaderToolbar0: slots.rightHeaderToolbar,
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
  .dubRowTable {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    height: 100%;
    .itemTable {
      background-color: white;
      width: 50%;
      height: 100%;
      padding-bottom: 8px;
    }
  }
</style>
