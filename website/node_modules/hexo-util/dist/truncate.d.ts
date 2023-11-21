interface Options {
    length?: number;
    omission?: string;
    separator?: string;
}
declare function truncate(str: string, options?: Options): string;
export = truncate;
