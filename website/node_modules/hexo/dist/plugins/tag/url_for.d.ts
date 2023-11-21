import type Hexo from '../../hexo';
/**
 * Url for tag
 *
 * Syntax:
 *   {% url_for text path [relative] %}
 */
declare const _default: (ctx: Hexo) => ([text, path, relative]: [any, any, any]) => string;
export = _default;
