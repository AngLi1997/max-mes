// useDragTable.ts

import { Recordable } from '@bmos/components';
import { ref } from 'vue';

/**
 * @method 表格行拖拽
 * @param dataSource 数据源
 */
export function useDragTable<T extends Recordable>(dataSource: Ref<T[]>) {
  /**拖拽起始行 */
  const sourceRecord = ref<Partial<T>>({});
  /**拖拽目标行 */
  const targetRecord = ref<Partial<T>>({});
  /**拖拽起始索引 */
  let oldIndex: number | null = null;
  /**拖拽目标索引 */
  let newIndex: number | null = null;

  /**
   * @method 自定义拖拽行
   * @param record 当前行数据
   * @param index 当前行索引
   */
  function customRow(record: T, index: number) {
    return {
      style: {
        cursor: 'pointer',
      },
      // 鼠标移入
      onMouseenter: (event: MouseEvent) => {
        // 兼容IE
        const ev = event || window.event;
        const target = ev.target as HTMLElement;
        target.draggable = true;
      },
      // 开始拖拽
      onDragstart: (event: Event) => {
        // 兼容IE
        const ev = event || window.event;
        ev.stopPropagation();
        // 得到源目标数据
        sourceRecord.value = record;
        oldIndex = index;
      },
      // 拖动元素经过的元素
      onDragover: (event: DragEvent) => {
        // 兼容 IE
        const ev = event || window.event;
        // 阻止默认行为
        ev.preventDefault();
        ev.dataTransfer!.dropEffect = 'move'; // 可以去掉拖动时那个＋号
        newIndex = index;
      },
      // 鼠标松开
      onDrop: (event: Event) => {
        // 兼容IE
        const ev = event || window.event;
        // 阻止冒泡
        ev.stopPropagation();
        // 得到目标数据
        targetRecord.value = record;
        // 将源数据插入目标数据前面
        newIndex = index;

        if (newIndex === oldIndex) return;
        dataSource.value.splice(oldIndex!, 1);
        dataSource.value.splice(newIndex, 0, sourceRecord.value);
      },
    };
  }

  return {
    sourceRecord,
    targetRecord,
    oldIndex,
    newIndex,
    customRow,
  };
}
