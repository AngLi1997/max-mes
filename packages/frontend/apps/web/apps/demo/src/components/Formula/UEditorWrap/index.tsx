import { withInstall } from '../../../utils';
import _VueUeditorWrap from './VueUeditorWrap';


const VueUeditorWrap = withInstall<typeof _VueUeditorWrap>(_VueUeditorWrap);

export default _VueUeditorWrap;
export { VueUeditorWrap };
export type { ModeType } from './type';
