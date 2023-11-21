interface Options {
    segments?: {
        [key: string]: RegExp | string;
    };
}
declare class Permalink {
    rule: string;
    regex: RegExp;
    params: string[];
    constructor(rule: string, options?: Options);
    test(str: string): boolean;
    parse(str: string): {};
    stringify(data: any): string;
}
export = Permalink;
