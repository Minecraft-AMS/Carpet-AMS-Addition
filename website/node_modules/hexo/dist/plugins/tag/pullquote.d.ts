import type Hexo from '../../hexo';
/**
* Pullquote tag
*
* Syntax:
*   {% pullquote [class] %}
*   Quote string
*   {% endpullquote %}
*/
declare const _default: (ctx: Hexo) => (args: string[], content: string) => string;
export = _default;
