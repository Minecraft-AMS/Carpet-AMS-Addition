import type Hexo from '../../hexo';
/**
* Blockquote tag
*
* Syntax:
*   {% blockquote [author[, source]] [link] [source_link_title] %}
*   Quote string
*   {% endblockquote %}
*/
declare const _default: (ctx: Hexo) => (args: string[], content: string) => string;
export = _default;
