export interface Distance {
  top: number;
  bottom: number;
  left: number;
  right: number;
  down?: number;
}

export interface Config extends Distance {
  pattern: number;
  header: number;
  footer: number;
  left: number;
  headerW: number;
  footerW: number;
}
