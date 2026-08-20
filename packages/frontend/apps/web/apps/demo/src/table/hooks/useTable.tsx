import { EllipsisOutlined } from '@ant-design/icons-vue';
import type { TableColumn } from '@bmos/components';
import { BMEllipsis } from '@bmos/components';
import { debounce } from '@bmos/utils';
import { Tag } from 'ant-design-vue';
import { Ref, h } from 'vue';

export type UseTableParams = {
  open: Ref<boolean>;
};

export const useTable = ({ open }: UseTableParams) => {
  const names = ['王路飞asdasdasdasdasdasdasdasdasdasdasdads', '王大蛇', '李白', '刺客伍六七'];
  const fetchStatusMapData = (keyword = '') => {
    const data = [
      {
        label: '已售罄',
        value: 0,
      },
      {
        label: '热卖中',
        value: 1,
      },
    ].filter(n => n.label.includes(keyword));
    return new Promise(resolve => {
      setTimeout(() => {
        resolve(data);
      }, 2000);
    });
  };
  const getClothesByGender = (gender: number) => {
    if (gender === 1) {
      // 男
      return [
        {
          label: '西装',
          value: 1,
        },
        {
          label: '领带',
          value: 0,
        },
      ];
    } else if (gender === 0) {
      //女
      return [
        {
          label: '裙子',
          value: 1,
        },
        {
          label: '包包',
          value: 0,
        },
      ];
    }
    return [];
  };
  const tableData = Array.from({ length: 30 }).map((_, i) => {
    const gender = ~~(Math.random() * 2);
    return {
      id: i + 1,
      date: new Date().toLocaleString(),
      name: names[~~(Math.random() * 4)],
      clothes: getClothesByGender(gender)[~~(Math.random() * 2)].label,
      price: ~~(Math.random() * 1000),
      gender,
      status: ~~(Math.random() * 2),
    };
  });

  const openModal = (record: any) => {
    console.log('openModal', record);
    open.value = true;
  }
  const columns: TableColumn[] = [
    {
      title: '姓姓名名',
      align: 'center',
      dataIndex: 'name',
      fixed: 'left',
      sorter: true,
      width: 300,
      resizable: true,
      formItemProps: {
        defaultValue: '李白',
        // colProps: {
        //   span: 12,
        // },
      },
    },
    {
      title: '性别',
      align: 'center',
      dataIndex: 'gender',
      width: 100,
      resizable: true,
      formItemProps: {
        component: 'Select',
        componentProps: ({ formInstance, formModel }) => ({
          options: [
            {
              label: '男',
              value: 1,
            },
            {
              label: '女',
              value: 0,
            },
          ],
          onChange() {
            // 根据当前选择的性别，更新衣服可选项
            formInstance?.updateSchema({
              field: 'clothes',
              componentProps: {
                options: getClothesByGender(formModel.gender),
              },
            });
            formModel['clothes'] = undefined;
          },
        }),
      },
      customRender: ({ record }) => ['女', '男'][record.gender],
    },
    {
      title: '衣服',
      align: 'center',
      hideInSearch: true,
      dataIndex: 'clothes',
      formItemProps: {
        component: 'Select',
      },
    },
    {
      title: '价格',
      align: 'center',
      hideInSearch: true,
      dataIndex: 'price',
      formItemProps: {
        component: 'Select',
      },
      customRender: ({ record }) => `${record.price}元`,
    },
    {
      title: '状态',
      align: 'center',
      hideInSearch: true,
      dataIndex: 'status',
      formItemProps: {
        component: 'Select',
        componentProps: ({ formInstance, schema }) => ({
          showSearch: true,
          filterOption: false,
          request: () => {
            return fetchStatusMapData();
          },
          onSearch: debounce(async (keyword: string | undefined) => {
            schema.loading = true;
            const newSchema = {
              field: 'status',
              componentProps: {
                options: [] as any,
              },
            };
            formInstance?.updateSchema([newSchema]);
            console.log('onSearch keyword', keyword);
            const result = await fetchStatusMapData(keyword).finally(
              () => (schema.loading = false),
            );
            newSchema.componentProps.options = result;
            formInstance?.updateSchema([newSchema]);
          }, 500),
          onChange(value: string) {
            console.log('onChange', value);
          },
        }),
      },
      customRender: ({ record }) => (
        <Tag color={record.status == 1 ? 'red' : 'default'}>
          {['已售罄', '热卖中'][record.status]}
        </Tag>
      ),
    },
    // {
    //   title: '操作',
    //   align: 'center',
    //   key: 'ACTION',
    //   actions: (params, action) => [
    //     {
    //       label: '编辑',
    //       onClick: ({ record }) => {
    //         console.log('编辑', params, action);
    //         console.log('编辑', record);
    //       },
    //     },
    //     {
    //       label: '删除',
    //       onClick: ({ record }) => {
    //         console.log('删除', record);
    //       },
    //     },
    //   ],
    // },
    {
      title: '操作',
      align: 'left',
      fixed: 'right',
      hideInSearch: true,
      width: 290,
      resizable: true,
      customRender: ({ record }) => (
        <div class='bmos-action-list'>
          <a-button
            style='max-width: 100px; min-width: 40px'
            type='link'
            onClick={() => openModal(record)}>
            <BMEllipsis tooltip={true}>
              {{
                default: () => '编辑编辑编辑编辑编辑编辑编辑编辑',
                title: () => '编辑编辑编辑编辑编辑编辑编辑编辑',
              }}
            </BMEllipsis>
          </a-button>
          <a-button type='link' onClick={() => console.log(record)}>
            <BMEllipsis style='max-width: 100px; min-width: 40px'>
              编
            </BMEllipsis>
          </a-button>
          <a-button type='link' onClick={() => console.log(record)}>
            <BMEllipsis style='max-width: 100px; min-width: 40px'>
              查看
            </BMEllipsis>
          </a-button>
          <a-dropdown trigger="['hover', 'click']">
            {{
              default: () => (
                <a-button type='link' icon={h(EllipsisOutlined)}></a-button>
              ),
              overlay: () => (
                <a-menu>
                  <a-menu-item key='1'>
                    <a href='#'>1st menu item</a>
                  </a-menu-item>
                  <a-menu-item key='2'>
                    <a href='#'>2nd menu item</a>
                  </a-menu-item>
                  <a-menu-item key='3'>
                    <a href='#'>3rd menu item</a>
                  </a-menu-item>
                </a-menu>
              ),
            }}
          </a-dropdown>
        </div>
      ),
    },
  ];

  return {
    tableData,
    columns,
  };
};
