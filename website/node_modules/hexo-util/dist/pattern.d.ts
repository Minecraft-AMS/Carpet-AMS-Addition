declare class Pattern {
    match: (str: string) => any;
    constructor(rule: Pattern | ((str: string) => any) | RegExp | string);
    test(str: string): boolean;
}
export = Pattern;
