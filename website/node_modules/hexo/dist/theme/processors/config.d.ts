import { Pattern } from 'hexo-util';
import type { _File } from '../../box';
declare function process(file: _File): any;
export declare const config: {
    pattern: Pattern;
    process: typeof process;
};
export {};
