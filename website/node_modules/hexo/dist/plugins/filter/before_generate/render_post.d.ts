import Promise from 'bluebird';
import type Hexo from '../../../hexo';
declare function renderPostFilter(this: Hexo): Promise<[any[], any[]]>;
export = renderPostFilter;
