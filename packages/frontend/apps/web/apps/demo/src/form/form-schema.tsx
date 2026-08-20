import { UploadOutlined } from '@ant-design/icons-vue';
import type { FormSchema } from '@bmos/components';
import { Button, RadioGroup } from 'ant-design-vue';

const provincesOptions = [
  {
    id: 'guangdong',
    label: '广东省',
    value: '1',
    key: '1',
  },
  {
    id: 'jiangsu',
    label: '江苏省',
    value: '2',
    key: '2',
  },
];
const citiesOptionsData = {
  guangdong: [
    {
      label: '珠海市',
      value: '1',
      key: '1',
    },
    {
      label: '深圳市',
      value: '2',
      key: '2',
    },
    {
      label: '广州市',
      value: '3',
      key: '3',
    },
  ],
  jiangsu: [
    {
      label: '南京市',
      value: '1',
      key: '1',
    },
    {
      label: '无锡市',
      value: '2',
      key: '2',
    },
    {
      label: '苏州市',
      value: '3',
      key: '3',
    },
  ],
};

const fetchOptionList = async () => {
  return new Promise(resolve => {
    setTimeout(() => {
      resolve(
        Array.from({ length: 10 }).map((_, i) => ({
          label: `选项${i}`,
          value: `${i}`,
        })),
      );
    }, 3000);
  });
};

export const schemas: FormSchema[] = [
  {
    field: 'field1',
    component: 'Input',
    label: '字段1',
    componentProps: () => {
      return {
        placeholder: '自定义placeholder',
        onChange: (e: any) => {
          console.log(e);
        },
      };
    },
    componentSlots: () => {
      return {
        prefix: () => '前缀',
        suffix: () => '后缀',
      };
    },
  },
  {
    field: 'field2',
    component: 'Input',
    label: '带后缀',
    defaultValue: '111',
    componentProps: {
      onChange: (e: any) => {
        console.log(e);
      },
    },
    suffix: '天',
  },
  {
    field: 'field3',
    component: 'DatePicker',
    label: '字段3',
  },
  {
    field: 'field4',
    component: 'Select',
    label: '字段4',
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
          key: '1',
        },
        {
          label: '选项2',
          value: '2',
          key: '2',
        },
      ],
    },
  },
  {
    field: 'field6',
    component: 'Divider',
    label: '字段6',
    colProps: {
      span: 24,
    },
  },
  {
    field: 'field5',
    component: 'CheckboxGroup',
    label: '字段5',
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  {
    field: 'field7',
    component: 'RadioGroup',
    label: '字段7',
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  {
    field: 'field8',
    component: 'Checkbox',
    label: '字段8',
    componentSlots: 'Check',
  },
  {
    field: 'field9',
    component: 'Switch',
    label: '字段9',
  },
  {
    field: 'field12',
    component: 'FormGroup',
    label: '字段12',
    colProps: {
      span: 24,
    },
    componentProps: {
      style: {
        borderTop: '1px solid #e8e8e8',
      }
    }
  },
  {
    field: 'field10',
    component: 'RadioGroup',
    label: '字段10',
    componentProps: {
      options: [
        {
          label: '选项1',
          value: '1',
        },
        {
          label: '选项2',
          value: '2',
        },
      ],
    },
  },
  {
    field: 'field11',
    component: 'Cascader',
    label: '字段11',
    componentProps: {
      options: [
        {
          value: 'zhejiang',
          label: 'Zhejiang',
          children: [
            {
              value: 'hangzhou',
              label: 'Hangzhou',
              children: [
                {
                  value: 'xihu',
                  label: 'West Lake',
                },
              ],
            },
          ],
        },
        {
          value: 'jiangsu',
          label: 'Jiangsu',
          children: [
            {
              value: 'nanjing',
              label: 'Nanjing',
              children: [
                {
                  value: 'zhonghuamen',
                  label: 'Zhong Hua Men',
                },
              ],
            },
          ],
        },
      ],
    },
  },
  {
    field: 'field30',
    component: 'Select',
    label: '懒加载远程下拉',
    required: true,
    componentProps: {
      request: async (values: any) => {
        return await fetchOptionList();
      },
      onChange: (e: any) => {
        console.log('selected:', e);
      },
    },
    defaultValue: '1',
  },
  {
    field: 'field31',
    component: 'Select',
    label: '下拉本地搜索',
    helpMessage: [
      'Select组件',
      '远程数据源本地搜索',
      '只发起一次请求获取所有选项',
    ],
    componentProps: {
      showSearch: true,
      filterOption: (input: string, option: any) => {
        return option.value.toLowerCase().indexOf(input.toLowerCase()) >= 0;
      },
      request: async (values: any) => {
        return await fetchOptionList();
      },
      onChange: (e: any) => {
        console.log('selected:', e);
      },
    },
    required: true,
    defaultValue: '0',
  },
  {
    field: 'field33',
    component: 'TreeSelect',
    label: '远程下拉树',
    helpMessage: ['TreeSelect组件', '使用接口提供的数据生成选项'],
    required: true,
    componentProps: {
      request: () => {
        return new Promise(resolve => {
          const treeData = Array.from({ length: 5 }).map((_, i) => ({
            title: `选项 ${i}`,
            value: `选项 ${i}`,
            children: Array.from({ length: 3 }).map((_, j) => ({
              title: `选项 ${i}-${j}`,
              value: `选项 ${i}-${j}`,
            })),
          }));
          setTimeout(() => resolve(treeData), 2000);
        });
      },
    },
  },
  {
    field: 'field34',
    component: ({ schema }: any) => {
      const options = schema.componentProps?.requestResult || [];
      return <RadioGroup options={options.slice(0, 2)}></RadioGroup>;
    },
    label: '远程RG',
    helpMessage: ['RadioGroup组件', '使用接口提供的数据生成选项'],
    required: true,
    componentProps: {
      optionType: 'button',
      request: async () => {
        const data = await fetchOptionList();
        return data;
      },
    },
    defaultValue: '0',
  },
  {
    field: 'field35',
    component: 'RadioGroup',
    label: '远程Radio',
    helpMessage: ['RadioGroup组件', '使用接口提供的数据生成选项'],
    required: true,
    componentProps: {
      optionType: 'button',
      request: async () => {
        const data: any = await fetchOptionList();
        return data.slice(0, 2);
      },
    },
    defaultValue: '1',
  },
  {
    field: 'province',
    component: 'Select',
    label: '省份',
    componentProps: ({ formModel, formInstance }: any) => {
      return {
        options: provincesOptions,
        placeholder: '省份与城市联动',
        onChange: (e: any) => {
          // console.log(e)
          // @ts-ignore
          let citiesOptions =
            e == 1
              ? // @ts-ignore
                citiesOptionsData[provincesOptions[0].id]
              : // @ts-ignore
                citiesOptionsData[provincesOptions[1].id];
          // console.log(citiesOptions)
          if (e === undefined) {
            citiesOptions = [];
          }
          formModel.city = undefined; //  reset city value
          const { updateSchema } = formInstance;
          updateSchema({
            field: 'city',
            componentProps: {
              options: citiesOptions,
            },
          });
        },
      };
    },
  },
  {
    field: 'city',
    component: 'Select',
    label: '城市',
    componentProps: {
      options: [], // defalut []
      placeholder: '省份与城市联动',
    },
  },
  {
    field: 'selectA',
    component: 'Select',
    label: '互斥SelectA',
    slot: 'selectA',
    defaultValue: [],
  },
  {
    field: 'selectB',
    component: 'Select',
    label: '互斥SelectB',
    slot: 'selectB',
    defaultValue: [],
  },
  {
    field: 'field20',
    component: 'InputNumber',
    label: '字段20',
    required: true,
  },
  {
    field: 'field21',
    component: 'Slider',
    label: '字段21',
    componentProps: {
      min: 0,
      max: 100,
      range: true,
      marks: {
        20: '20°C',
        60: '60°C',
      },
    },
  },
  {
    field: 'field22',
    component: 'Rate',
    label: '字段22',
    defaultValue: 3,
    componentProps: {
      disabled: false,
      allowHalf: true,
    },
  },
  {
    field: 'field23',
    component: 'Upload',
    label: '字段23',
    componentProps: {
      action: 'https://www.mocky.io/v2/5cc8019d300000980a055e76',
    },
    componentSlots: {
      default: () => (
        <Button>
          <UploadOutlined /> Click to Upload
        </Button>
      ),
    },
  },
];
