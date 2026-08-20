export type FileArg = File[] | File;

export interface Info {
  name: {
    creator: string;
  };
}

export interface SheetsJson {
  sheets: Array<any>;
  info: Info;
}

export type FileToExcel = (
  files: FileArg,
  callback: (data: SheetsJson['sheets']) => void,
) => Promise<void>;
