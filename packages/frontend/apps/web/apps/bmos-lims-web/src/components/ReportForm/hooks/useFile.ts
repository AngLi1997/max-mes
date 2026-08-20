import { loadExcel } from '../js/luckysheet';
import { ReportPropsType } from '../type';
export const useFile = (props: ReportPropsType) => {
  const uploadTemplate = () => {
    const dom = document.createElement('input');
    dom.setAttribute('type', 'file');
    dom.onchange = e => {
      loadExcel(e as InputEvent & { target: HTMLInputElement }, props.edit!);
    };
    dom.click();
    // element.target.blur()
    // document.body.click()
  };
  return {
    uploadTemplate,
  };
};
