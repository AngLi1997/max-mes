<template>
  <div class="dynamic-report">
    <div class="dynamic-report-table">
      <BMTable
        ref="tableRef"
        :columns="columns"
        :dataSource="datasetLotReleaseLinkList"
        :pagination="false"
        :search="false"
        :showToolBar="false"
        :scroll="{ x: 400, y: 300 }" />
    </div>
    <Button v-if="!isView" type="link" @click="add">
      <BMIcons
        icon="Add"
        :style="{
          fontSize: '14px',
          width: '14px',
          height: '14px',
          marginRight: '5px',
        }" />
      {{ t('添加批签发引用') }}
    </Button>
  </div>
</template>
<script lang="tsx" setup>
  import { BMTable, Recordable, TableColumn } from '@bmos/components';
  import { Button, Input, message, Select } from 'ant-design-vue';
  import { t } from '@bmos/i18n';
  import { SelectValue } from 'ant-design-vue/es/select';
  import { BMIcons } from '@bmos/icons';
  import { fileStreamDownload, isArray } from '@bmos/utils';
  import { reqLotReleaseListByProcessId, reqLotReleaseTemplateDownloadTemplate } from '@/services';

  defineOptions({
    name: 'LotReleaseLinkTable',
    inheritAttrs: false,
  });
  const datasetLotReleaseLinkList = defineModel<Array<any>>('datasetLotReleaseLinkList', {
    default: [
      {
        key: new Date().getTime(),
      },
    ],
  });

  const props = withDefaults(
    defineProps<{
      isView?: boolean;
      processId?: string;
    }>(),
    {
      isView: false,
      processId: '',
    },
  );

  const tableRef = ref<any>(null);

  const add = () => {
    datasetLotReleaseLinkList.value = [
      ...(isArray(datasetLotReleaseLinkList.value) ? datasetLotReleaseLinkList.value : []),
      {
        key: new Date().getTime(),
      },
    ];
  };

  const updateList = (key: string, value: any, id: number) => {
    datasetLotReleaseLinkList.value = datasetLotReleaseLinkList.value.map((item: any) => {
      if (item.id && item.id === id) {
        return {
          ...item,
          [key]: value,
        };
      }
      if (item.key === id) {
        return {
          ...item,
          [key]: value,
        };
      }
      return item;
    });
  };

  const updateLists = (obj: Array<{ key: string; value: any }>, id: number) => {
    datasetLotReleaseLinkList.value = datasetLotReleaseLinkList.value.map((item: any) => {
      if (item.id && item.id === id) {
        return {
          ...item,
          ...obj.reduce((acc: any, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {}),
        };
      }
      if (item.key === id) {
        return {
          ...item,
          ...obj.reduce((acc: any, cur) => {
            acc[cur.key] = cur.value;
            return acc;
          }, {}),
        };
      }
      return item;
    });
  };

  const lotReleaseOptions = ref<Array<any>>([]);
  const getLotReleaseOptions = async () => {
    try {
      const { data } = await reqLotReleaseListByProcessId(props.processId);
      lotReleaseOptions.value = data?.map((item: any) => ({
        label: item.name,
        value: item.id,
        versionList: item?.list?.map((version: any) => ({
          ...version,
          templateName: item.name,
        })),
      }));
      // 更新每一行的 lotReleaseVersionOptions 选项
      datasetLotReleaseLinkList.value.forEach((item: any) => {
        if (item.lotReleaseTemplateId) {
          const option = lotReleaseOptions.value.find((option: any) => option.value === item.lotReleaseTemplateId);
          if (option) {
            getLotReleaseVersionOptions(item, option);
          }
        }
      });
    } catch (error) {
      //
    }
  };
  const getLotReleaseVersionOptions = async (record: Recordable, option: Recordable) => {
    try {
      updateList('lotReleaseVersionOptions', option.versionList, record.id || record.key);
    } catch (error) {
      //
    }
  };

  const downloadTemplate = async (record: any) => {
    try {
      if (!record.lotReleaseVersion) {
        message.error(t('请选择批签发模板版本'));
        return;
      }
      const lotReleaseVersionId = record.lotReleaseVersionOptions?.find(
        (item: any) => item.version === record.lotReleaseVersion,
      )?.id;
      const res = await reqLotReleaseTemplateDownloadTemplate(lotReleaseVersionId);
      fileStreamDownload(res, `${record.lotReleaseName}.xlsx`);
    } catch (error) {}
  };

  watch(
    () => props.processId,
    val => {
      if (val) {
        getLotReleaseOptions();
      }
    },
  );

  onMounted(() => {
    if (props.processId) {
      getLotReleaseOptions();
    }
  });

  const columns: Ref<TableColumn[]> = ref([
    {
      title: t('批签发模版名称'),
      width: 180,
      dataIndex: 'lotReleaseTemplateId',
      fixed: 'left',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Select
              value={record.lotReleaseTemplateId}
              placeholder={t('批签发模版名称')}
              allowClear
              showSearch
              disabled={props.isView}
              options={lotReleaseOptions.value}
              onChange={(value: SelectValue, option: any) => {
                updateLists(
                  [
                    {
                      key: 'lotReleaseTemplateId',
                      value,
                    },
                    {
                      key: 'lotReleaseVersion',
                      value: undefined,
                    },
                    {
                      key: 'lotReleaseVersionId',
                      value: undefined,
                    },
                    {
                      key: 'lotReleaseName',
                      value: option?.label,
                    },
                  ],
                  record.id || record.key,
                );
                setTimeout(() => {
                  value && getLotReleaseVersionOptions(record, option);
                  if (!value) {
                    updateList('lotReleaseVersionOptions', [], record.id || record.key);
                  }
                }, 100);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('引用数据范围'),
      width: 140,
      dataIndex: 'linkArea',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Input
              value={record.linkArea}
              allowClear
              maxlength={100}
              disabled={props.isView}
              placeholder={t('引用数据范围')}
              onChange={(e: any) => {
                updateList('linkArea', e.target.value, record.id || record.key);
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('批签发版本'),
      width: 140,
      dataIndex: 'lotReleaseVersion',
      customRender: ({ record }) => {
        return (
          <div class='editable-cell'>
            <Select
              value={record.lotReleaseVersion}
              allowClear
              disabled={props.isView}
              placeholder={t('版本')}
              fieldNames={{
                label: 'version',
                value: 'id',
              }}
              options={record.lotReleaseVersionOptions || []}
              onChange={(value: SelectValue, option: any) => {
                updateLists(
                  [
                    {
                      key: 'lotReleaseVersion',
                      value: option?.version,
                    },
                    {
                      key: 'lotReleaseVersionId',
                      value,
                    },
                  ],
                  record.id || record.key,
                );
              }}
            />
          </div>
        );
      },
    },
    {
      title: t('模板'),
      width: 60,
      dataIndex: 'template',
      fixed: 'right',
      customRender: ({ record }) => {
        return (
          <Button
            type='link'
            style={{ padding: '0' }}
            onClick={() => {
              downloadTemplate(record);
            }}>
            {t('下载')}
          </Button>
        );
      },
    },
    {
      title: '',
      width: 60,
      dataIndex: 'delete',
      fixed: 'right',
      customRender: ({ record }) => {
        return props.isView ? null : (
          <BMIcons
            icon='CircleDelete'
            style={{
              fontSize: '16px',
              width: '16px',
              height: '16px',
            }}
            onClick={() => {
              datasetLotReleaseLinkList.value = datasetLotReleaseLinkList.value.filter((item: any) => {
                if (record.key) {
                  return item.key !== record.key;
                }
                return item.id !== record.id;
              });
            }}
          />
        );
      },
    },
  ]);
</script>
