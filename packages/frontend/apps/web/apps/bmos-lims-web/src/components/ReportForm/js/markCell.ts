import { Position } from '../type'
import { DragData } from '../type/drag'
import initMark from './mark'
const { markArrOperation } = initMark()


//拖拽的上个节点坐标
let update_r = null
let update_c = null 


/**
* 调用setCellValue设置单元格自定义隐藏值
* @param {Array | Object} position 
* @param {String} check 子表单数据渲染方向
*/
const setCellMarkValue = (position:Position,obj:DragData,check='')=>{
    
    update_r = position.r
    update_c = position.c
    
    luckysheet.setCellValue(position.r, position.c, {
      m:obj.label,
      v: obj.label,
      tv:obj.value  
    }); 
    //每设置一个便记录一次
    markArrOperation.addMarkArr(position)
}

/**
 * 删除标记
 * @param {*} position 
 */
const deleteCellMarkValue = (position:Position[])=>{
    markArrOperation.deleteMarkArr(position)
}


export const cellMarkValue = {
    setCellMarkValue,
    deleteCellMarkValue
}