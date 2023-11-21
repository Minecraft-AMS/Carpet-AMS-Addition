import { Pattern } from 'hexo-util';
import type { _File } from '../../box';
import type Hexo from '../../hexo';
declare const _default: (ctx: Hexo) => {
    pattern: Pattern;
    process: (file: _File) => any;
};
export = _default;
