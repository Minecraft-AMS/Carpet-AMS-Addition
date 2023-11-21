import type Hexo from '../hexo';
export interface HighlightOptions {
    lang: string | undefined;
    caption: string | undefined;
    lines_length?: number | undefined;
    firstLineNumber?: string | number;
    language_attr?: boolean | undefined;
    firstLine?: number;
    line_number?: boolean | undefined;
    line_threshold?: number | undefined;
    mark?: number[];
    wrap?: boolean | undefined;
}
interface HighlightExecArgs {
    context?: Hexo;
    args?: [string, HighlightOptions];
}
interface StoreFunction {
    (content: string, options: HighlightOptions): string;
    priority?: number;
}
interface Store {
    [key: string]: StoreFunction;
}
declare class SyntaxHighlight {
    store: Store;
    constructor();
    register(name: string, fn: StoreFunction): void;
    query(name: string): StoreFunction;
    exec(name: string, options: HighlightExecArgs): string;
}
export default SyntaxHighlight;
