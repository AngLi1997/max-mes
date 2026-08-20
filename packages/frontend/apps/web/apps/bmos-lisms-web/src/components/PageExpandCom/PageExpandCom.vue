<script lang="tsx">
  import { paginationBig } from '@/utils/paginationConfig';
  import { Key, DataRequestFn, BMPageComponent } from '@bmos/components';
  import { useExpand } from './useExpand';
  import { message } from 'ant-design-vue';

  interface Props {
    tableRowKey: string;
    tableProps: object;
    tableLoadApi: Function;
    expandLoadApi: Function;
    expandProps: object;
    expandFields: Function;
  }

  const PageExpandCom = defineComponent({
    props: {
      tableRowKey: {
        type: String,
        default: 'id',
      },
      tableProps: {
        type: Object,
        default: () => ({}),
      },
      tableLoadApi: {
        type: Function,
        required: true,
      },
      expandLoadApi: {
        type: Function,
        required: true,
      },
      expandProps: {
        type: Object,
        default: () => ({}),
      },
      expandFields: {
        type: Function,
        required: true,
      },
    },

    setup(props: Props, { slots, expose }) {
      const createSlot = (name: string, type: string) => {
        return name.replace(type, '');
      };

      const getSlots = computed(() => {
        const tableSlots: Record<string, any> = {};
        const expendSlots: Record<string, any> = {};
        Object.keys(slots).forEach(key => {
          if (key.startsWith('expand')) {
            expendSlots[createSlot(key, 'expand')] = slots[key];
          } else {
            tableSlots[key] = slots[key];
          }
        });
        return { tableSlots, expendSlots };
      });

      const pageRef = ref<any>(null);

      const expandedRowKeys = ref<any>([]);
      const expandMap = reactive<any>({});

      const expandChange = async (expandedKeys: Key[]) => {
        expandedRowKeys.value = expandedKeys;
        if (expandedKeys.length === 0) return;
        const newKey = expandedKeys[expandedKeys.length - 1];
        if (!expandMap[newKey]) {
          expandMap[newKey] = useExpand();
        } else {
          await expandMap[newKey].fetchData();
        }
      };

      const loadData = async (params: any) => {
        try {
          const datas = {
            ...params,
          };
          const res = await props.tableLoadApi(datas);

          const keys = res?.data?.list?.map((item: any) => item[props.tableRowKey]) || [];

          expandedRowKeys.value?.forEach((key: any) => {
            if (keys.includes(key)) {
              expandMap[key].fetchData();
            }
          });
          return res;
        } catch (error: any) {
          console.log(error);
          error.message && message.error(error.message);
        }
      };

      const setExpandRef = (key: any, ref: any) => {
        expandMap[key].setRef(ref);
      };

      const fetchData = async () => {
        pageRef.value?.fetchData();
      };

      expose({
        getSlots,
        pageRef,
        expandedRowKeys,
        expandMap,
        expandChange,
        loadData,
        fetchData,
      });

      return () => (
        <BMPageComponent
          ref={pageRef}
          rowKeys={[props.tableRowKey]}
          expandedRowsChanges={[expandChange]}
          requests={[loadData as DataRequestFn]}
          paginations={[paginationBig]}
          {...props.tableProps}>
          {{
            ...getSlots.value.tableSlots,
            expandColumnTitle0: () => {},
            expandedRowRender0: ({ record, instance }: any) => {
              return (
                <BMPageComponent
                  ref={el => setExpandRef(record[props.tableRowKey], el)}
                  tableFields={[
                    {
                      default: {
                        ...instance.getQueryFormRef()?.handleFormValues(instance.getQueryFormRef().formModel),
                        ...instance.refProps?.extraParams.value,
                        ...props.expandFields(record),
                      },
                    },
                  ]}
                  isExtraParamsChangeQuerys={[false]}
                  paginations={[paginationBig]}
                  requests={[props.expandLoadApi as DataRequestFn]}
                  {...props.expandProps}>
                  {{
                    ...getSlots.value.expendSlots,
                  }}
                </BMPageComponent>
              );
            },
          }}
        </BMPageComponent>
      );
    },
  });

  export default PageExpandCom;
</script>
