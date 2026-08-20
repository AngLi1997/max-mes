export const DESK_KEY = 'bmos-desk';
export const DESK_NAME = 'BMOS';
// 菜单icon高清图片
import auditImg from '../../src/assets/img/auditImg.png';
import bimsImg from '../../src/assets/img/bimsImg.png';
import bsmsImg from '../../src/assets/img/bsmsImg.png';
import dcImg from '../../src/assets/img/dcImg.png';
import defaultImg from '../../src/assets/img/defaultImg.png';
import emsImg from '../../src/assets/img/emsImg.png';
import limsImg from '../../src/assets/img/limsImg.png';
import lismsImg from '../../src/assets/img/lismsImg.png';
import mesImg from '../../src/assets/img/mesImg.png';
import platformImg from '../../src/assets/img/platformImg.png';
import wmsImg from '../../src/assets/img/wmsImg.png';

export const DEFAULT_IMG = defaultImg;

export interface MenuKey {
  [key: number]: {
    key: string;
    title: string;
    icon: string;
    smallIcon: string;
    img: string;
  };
}

export const MENU_KEY: MenuKey = {
  100: {
    key: '/app/bmos-platform/',
    title: 'BM-PMP',
    icon: 'Platform',
    smallIcon: 'IconPlatform',
    img: platformImg,
  },
  111: {
    key: '/app/bmos-audit/',
    title: 'BM-ATM',
    icon: 'Audit',
    smallIcon: 'IconAudit',
    img: auditImg,
  },
  120: {
    key: '/app/bmos-mes/',
    title: 'BM-MES',
    icon: 'MES',
    smallIcon: 'IconMes',
    img: mesImg,
  },
  130: {
    key: '/app/bmos-lims/',
    title: 'BM-LIMS',
    icon: 'Platform',
    smallIcon: 'IconPlatform',
    img: limsImg,
  },
  140: {
    key: '/app/bmos-qms/',
    title: 'BM-QMS',
    icon: 'Platform',
    smallIcon: 'IconPlatform',
    img: platformImg,
  },
  150: {
    key: '/app/bmos-wms/',
    title: 'BM-WMS',
    icon: 'Platform',
    smallIcon: 'IconWms',
    img: wmsImg,
  },
  160: {
    key: '/app/bmos-ems/',
    title: 'BM-EMS',
    icon: 'Platform',
    smallIcon: 'IconEms',
    img: emsImg,
  },
  170: {
    key: '/app/bmos-bsms/',
    title: 'BM-BSMS',
    icon: 'Platform',
    smallIcon: 'IconBsms',
    img: bsmsImg,
  },
  180: {
    key: '/app/bmos-bims/',
    title: 'BM-BIMS',
    icon: 'Platform',
    smallIcon: 'IconBims',
    img: bimsImg,
  },
  200: {
    key: '/app/bmos-dc/',
    title: 'BM-DC',
    icon: 'Platform',
    smallIcon: 'IconBims',
    img: dcImg,
  },
  210: {
    key: '/app/bmos-lisms/',
    title: 'BM-LISMS',
    icon: 'Platform',
    smallIcon: 'IconLisms',
    img: lismsImg,
  },
  220: {
    key: '/app/bmos-el/',
    title: 'BM-EL',
    icon: 'Platform',
    smallIcon: 'IconPlatform',
    img: platformImg,
  },
};
