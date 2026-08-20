import { reactive, ref } from 'vue';

export const useTable = () => {
  const tableRef = ref();

  const tableProps = reactive({
    pagination: true,
    type: 'selection',
    // data: [
    //   {
    //     name: '赵云',
    //     school: '武汉市阳逻妇幼保健学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '孔明',
    //     school: '武汉市阳逻卧龙学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '刘备',
    //     school: '武汉市阳逻编织学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '赵云',
    //     school: '武汉市阳逻妇幼保健学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '孔明',
    //     school: '武汉市阳逻卧龙学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '刘备222',
    //     school: '武汉市阳逻编织学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '赵云2',
    //     school: '武汉市阳逻妇幼保健学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '孔明2',
    //     school: '武汉市阳逻卧龙学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '刘备1',
    //     school: '武汉市阳逻编织学院',
    //     major: '计算机科学与技术专业'
    //   },
    //   {
    //     name: '赵云1',
    //     school: '武汉市阳dfgd逻妇幼保健学院',
    //     major: '计算机gdg科学与技术专业'
    //   },
    //   {
    //     name: '孔明',
    //     school: '武汉市阳dfgd逻卧龙学院',
    //     major: '计算机科sdf学与技术专业'
    //   },
    //   {
    //     name: '刘备444',
    //     school: '武汉市阳逻编织学院',
    //     major: '计算机科学sdasdad与技术专业'
    //   }
    // ],
    dataRequest: async(params) => {
      return await new Promise((resolve) => {
        setTimeout(() => {
          resolve({
            data: {
              pageNum: 1,
              total: 100,
              list: [
                {
                  id: 1,
                  name: '赵云',
                  school: '1',
                  major: '计算机科学与技术专业'
                },
                {
                  id: 2,
                  name: '孔明',
                  school: '2',
                  major: '计机'
                },
                {
                  id: 3,
                  name: '刘备',
                  school: '3',
                  major: '计算机科学与技术专业'
                }
              ]
            }
          });
        }, 1000);
      });
    },
    border: false,
    tableColProps: [
      {
        prop: 'name',
        label: '姓名',
        width: 100
      },
      {
        prop: 'school',
        label: '学校',
        thProps: {
          align: 'left'
        },
        width: 100,
        showSelect: () => {
          return true;
        },
        componentProps: (row, test) => {
          return {
            options: [
              {
                label: '接生学院',
                value: '1'
              },
              {
                label: '妇科圣学院',
                value: '2'
              },
              {
                label: '妇炎洁学院',
                value: '3'
              }
            ],
            fieldNames: { label: 'label', value: 'value' }
          };
        }
      },
      {
        prop: 'major',
        label: '专业',
        width: 100,
        showInput: () => {
          return true;
        }
      },
      {
        prop: 'major2',
        label: '专业',
        width: 100,
        customRender: ({ row }) => {
          return <span style={{
            color: 'red'
          }}>customRender: {row.major}</span>;
        }
      },
      {
        prop: 'ACTION',
        label: '操作',
        width: 100,
        actions: ({ row, tableInstance }) => {
          return [
            {
              label: '编辑',
              onClick: () => {
                console.log('编辑', row);
                console.log('tableInstance', tableInstance);
              }
            },
            {
              label: '删除',
              ifShow: () => row.name === '刘备',
              onClick: () => {
                console.log('删除', row);
              }
            }
          ];
        }
      }
    ],
    selectionProps: (row) => {
      return {
        disabled: row.name === '刘备'
      };
    }
  });

  const selectionChange = (selectedRows) => {
    console.log('selectedRows', selectedRows);
  };

  return {
    tableRef,
    tableProps,
    selectionChange
  };
};
