interface Options {
    separator?: string;
    transform?: number;
}
declare function slugize(str: string, options?: Options): string;
export = slugize;
