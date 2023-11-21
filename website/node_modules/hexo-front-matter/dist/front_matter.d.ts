import yaml from 'js-yaml';
declare function split(str: string): {
    data: string;
    content: string;
    separator: string;
    prefixSeparator: boolean;
} | {
    content: string;
    data?: undefined;
    separator?: undefined;
    prefixSeparator?: undefined;
};
declare function parse(str: string, options?: yaml.LoadOptions): any;
declare function escapeYAML(str: string): string;
interface Options {
    mode?: 'json' | '';
    prefixSeparator?: boolean;
    separator?: string;
}
declare function stringify(obj: any, options?: Options): any;
export { parse, split, escapeYAML as escape, stringify };
