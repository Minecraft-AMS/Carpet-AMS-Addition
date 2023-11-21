import type Hexo from '../../hexo';
/**
* Image tag
*
* Syntax:
*   {% img [class names] /path/to/image [width] [height] [title text [alt text]] %}
*/
declare const _default: (ctx: Hexo) => (args: string[]) => string;
export = _default;
