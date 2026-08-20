declare global {
  type KEY = string | number;
}
interface ImportMeta {
  readonly glob: (pattern: string, options?: { eager?: boolean }) => Record<string, { default: string }>;
}
