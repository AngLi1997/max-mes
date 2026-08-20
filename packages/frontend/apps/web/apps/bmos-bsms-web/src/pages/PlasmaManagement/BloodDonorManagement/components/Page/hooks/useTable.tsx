import { usePermissionStore } from '@/stores/permission';
import { usePlasmaStation } from '@/stores/plasmaStation';
import { paginationBig } from '@/utils/paginationConfig';
import { FormProps, TableColumn } from '@bmos/components';
import { useRouter } from 'vue-router';

const { hasPermission } = usePermissionStore();
const { getPlasmaStations } = usePlasmaStation();

export const useTable = () => {
  const pageRef = ref<any>(null);
  const router = useRouter();

  const columnsFirst: TableColumn[] = [
    {
      title: t('献浆者编号'),
      dataIndex: 'no',
      width: 150,
      sorter: true,
      resizable: true,
    },
    {
      title: t('来源单位'),
      dataIndex: 'originOrgCode',
      width: 220,
      resizable: true,
      customRender: ({ record }) => {
        return <span>{record?.originOrgInfo?.originOrg}</span>;
      },
      formItemProps: {
        component: 'Select',
        componentProps: {
          // options: plasmaStations,
          request: getPlasmaStations,
        },
      },
    },
    {
      title: t('献浆者姓名'),
      dataIndex: 'name',
      width: 100,
      resizable: true,
    },
    {
      title: t('身份证号码'),
      dataIndex: 'idCard',
      width: 170,
      resizable: true,
    },
    {
      title: t('性别'),
      dataIndex: 'sex',
      width: 80,
      customRender: ({ record }) => {
        return <span>{record.sex.name}</span>;
      },
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('血型'),
      dataIndex: 'bloodType',
      width: 80,
      customRender: ({ record }) => {
        return <span>{record.bloodType.name}</span>;
      },
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('联系电话'),
      dataIndex: 'tel',
      width: 170,
      resizable: true,
    },
    {
      title: t('身份证地址'),
      dataIndex: 'address',
      width: 170,
      hideInSearch: true,
      resizable: true,
    },
    {
      title: t('操作'),
      key: 'ACTION',
      fixed: 'right',
      width: 80,
      actions: ({ record }) => [
        {
          label: t('详情'),
          ifShow: hasPermission('170040010000002'),
          onClick: () => {
            router.push({
              name: 'plasma-donor-detail',
              params: { id: record.id },
            });
          },
        },
      ],
    },
  ];

  const formFirstProps: Partial<FormProps> = {
    showAdvancedButton: true,
  };

  const paginationFirst = reactive({
    ...paginationBig,
  });

  return {
    pageRef,
    columnsFirst,
    formFirstProps,
    paginationFirst,
  };
};
