<!-- 左右列表组件 -->
<script lang="tsx">
  import { DataRequestFn, BMPageComponent } from '@bmos/components';

  export default defineComponent({
    name: 'DubRowTable',
    props: {
      leftTitle: {
        type: String,
        default: '左边列表',
      },
      rightTitle: {
        type: String,
        default: '右边列表',
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

      return () => (
        <>
          <div class='dubRowTable'>
            <div class='itemTable'>
              <BMPageComponent
                ref={leftRef}
                titles={[props.leftTitle]}
                rowKeys={['id']}
                hideRightTree={true}
                {...props.leftTableProps}>
                {{
                  tableHeaderToolbar0: () => slots.leftHeaderToolbar?.(),
                }}
              </BMPageComponent>
            </div>
            <div class='itemTable'>
              <BMPageComponent
                ref={rightRef}
                titles={[props.rightTitle]}
                rowKeys={['id']}
                hideRightTree={true}
                {...props.rightTableProps}>
                {{
                  tableHeaderToolbar0: () => slots.rightHeaderToolbar?.(),
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
    align-items: center;
    height: 100%;
    .itemTable {
      background-color: white;
      width: 49.5%;
      height: 100%;
      padding-bottom: 8px;
    }
  }
</style>
