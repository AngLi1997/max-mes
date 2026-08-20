import { withInstall } from '../../../utils';
import _VueUeditorWrap from './VueUeditorWrap';

export type { ModeType, UENode, PageBreak } from './type';
export { VueUeditorWrap };

const VueUeditorWrap = withInstall<typeof _VueUeditorWrap>(_VueUeditorWrap);

export default _VueUeditorWrap;
