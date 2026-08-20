import StateTag from '@/components/StateTag/index.vue';
import { archiveAgain, getPlanPageTraceable } from '@/services';
import { usePermissionStore } from '@/stores/permission';
import type { TableColumn } from '@bmos/components';
import { t } from '@bmos/i18n';
import { message } from 'ant-design-vue';
import { saveOperationHistory } from '../../utils';
import { OPERATION } from '../../utils/enum';
import { rootKey } from './useTree';

const StartStatus: Record<string, string> = {
  END: 'success',
  TERMINATION: 'danger',
};
const archiveStatusStatus: Record<string, string> = {
  WAIT_ARCHIVE: 'default',
  ARCHIVE_ING: 'warning',
  ARCHIVE_SUCCESS: 'success',
  ARCHIVE_FAIL: 'danger',
};

export const useColumns = () => {
  const { hasPermission } = usePermissionStore();
  const router = useRouter();
  const currentRecord = ref();
  const previewStatus = ref(false);
  const historyOpen = ref<boolean>(false);
  const file = ref();
  const fileUrl = ref();
  const pageRef = ref();

  const operation = async (record: any, type: number) => {
    switch (type) {
      case 0:
        currentRecord.value = record;
        previewStatus.value = true;
        fileUrl.value = `${document.location.protocol}//${document.location.hostname}:${document.location.port}/${record.archiveFileUrl}`;
        fetch(fileUrl.value)
          .then(response => {
            if (!response.ok) {
              throw new Error('Network response was not ok');
            }
            return response.blob(); // 将响应转换为Blob对象
          })
          .then(blob => {
            // 创建一个用于读取Blob的FileReader对象
            const reader = new FileReader();

            reader.onload = e => {
              // e.target.result 包含了文件的数据
              file.value = e.target?.result;
            };

            reader.onerror = error => {
              console.error('File could not be read!', error);
            };

            // 读取Blob数据
            reader.readAsArrayBuffer(blob);
          })
          .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
          });
        saveOperationHistory(record, OPERATION.V);
        break;
      case 1:
        currentRecord.value = record;
        historyOpen.value = true;
        break;
      case 2:
        try {
          await archiveAgain(record.id);
          message.success(t('操作成功'));
        } catch (error: any) {
          message.error(error.message);
        }
        break;
      default:
        break;
    }
  };

  const requestPage = async (param: any) => {
    const data = { ...param };
    if (data.productIds === rootKey || data.productIds === void 0) {
      delete data.productIds;
    } else {
      if (data.categoryFlag) {
        data.productCategoryId = data.productIds;
        delete data.productIds;
      } else {
        data.productIds = [data.productIds];
      }
    }
    return await getPlanPageTraceable(data);
  };

  const column: TableColumn[] = [
    {
      title: t('产品名称'),
      dataIndex: 'productName',
      fixed: 'left',
      width: 250,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品编码'),
      dataIndex: 'productMergeCode',
      width: 250,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('产品规格'),
      dataIndex: 'productSpecification',
      width: 250,
      resizable: true,
      hideInSearch: true,
    },
    {
      title: t('生产批号'),
      dataIndex: 'batchNo',
      width: 250,
      resizable: true,
      sorter: true,
    },
    {
      title: t('异常数量'),
      dataIndex: 'exceptionCount',
      width: 120,
      resizable: true,
      sorter: true,
      hideInSearch: true,
      customRender: ({ record }) => {
        if (record.exceptionCount == 0) {
          return <div>0</div>;
        } else {
          return (
            <div
              style='color: #2871FF;cursor: pointer;'
              onClick={() => {
                router.push({
                  name: 'exceptionInformation',
                  query: {
                    title: t('生产历史'),
                    productPlanId: record.id,
                  },
                });
              }}>
              {record.exceptionCount}
            </div>
          );
        }
      },
    },
    {
      title: t('工艺名称'),
      dataIndex: 'processName',
      width: 250,
      resizable: true,
    },
    {
      title: t('生产开始时间'),
      dataIndex: 'startTime',
      width: 250,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('生产结束时间'),
      dataIndex: 'endTime',
      width: 250,
      resizable: true,
      hideInSearch: true,
      sorter: true,
    },
    {
      title: t('归档状态'),
      dataIndex: 'archiveStatus',
      width: 150,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => (
        <StateTag type={archiveStatusStatus[record.archiveStatus?.value || 'WAIT_ARCHIVE']}>
          {record.archiveStatus?.label || t('待归档')}
        </StateTag>
      ),
    },
    {
      title: t('状态'),
      dataIndex: 'start',
      width: 250,
      resizable: true,
      hideInSearch: true,
      customRender: ({ record }) => <StateTag type={StartStatus[record.start.value]}>{record.start.label}</StateTag>,
    },
    {
      title: t('操作'),
      align: 'left',
      key: 'ACTION',
      fixed: 'right',
      width: 320,
      actions: ({ record }) => [
        {
          label: t('操作历史'),
          ifShow: hasPermission('120050001000002'),
          onClick: () => {
            operation(record, 1);
          },
        },
        {
          label: t('预览打印'),
          ifShow: hasPermission('120050001000001') && record.archiveStatus?.value == 'ARCHIVE_SUCCESS',
          onClick: () => {
            operation(record, 0);
          },
        },
        {
          label: t('重新归档'),
          ifShow: record.archiveStatus?.value == 'ARCHIVE_FAIL' || record.archiveStatus?.value == 'ARCHIVE_SUCCESS',
          onClick: async () => {
            await operation(record, 2);
            await pageRef.value?.fetchData();
          },
        },
        {
          label: t('修订记录'),
          ifShow: hasPermission('120050001000003'),
          onClick: () => {
            router.push({
              name: 'production-history-revision',
              query: {
                productPlanId: record.id,
              },
            });
          },
        },
      ],
    },
  ];
  return {
    columns: [column],
    requests: [requestPage],
    currentRecord,
    previewStatus,
    historyOpen,
    file,
    fileUrl,
    pageRef,
  };
};
