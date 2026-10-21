'use strict';

const { createHash } = require('node:crypto');
const { readFileSync } = require('node:fs');
const path = require('node:path');
const scriptVersions = new Map();
const { stripHTML } = require('hexo-util');
const { normalizeConfig, pageView } = require('./lib/config');
let settings;

hexo.extend.filter.register('before_generate', () => {
  settings = normalizeConfig(hexo.theme.config);
  scriptVersions.clear();
});

hexo.extend.helper.register('theme_view', function(page) {
  return pageView(settings || normalizeConfig(hexo.theme.config), page);
});

// 根据脚本内容生成版本号，避免再次访问时混用旧脚本与新页面结构。
hexo.extend.helper.register('theme_script', function(name) {
  if (!scriptVersions.has(name)) {
    const source = readFileSync(path.join(hexo.theme.base, 'source/js', name));
    scriptVersions.set(name, createHash('sha256').update(source).digest('hex').slice(0, 12));
  }
  const url = hexo.extend.helper.get('url_for').call(this, 'js/' + name);
  return `${url}?v=${scriptVersions.get(name)}`;
});

hexo.extend.generator.register('post-search', function(locals) {
  if (!settings.search.posts) return [];
  const context = { config: hexo.config, page: {} };
  const date = hexo.extend.helper.get('date').bind(context);
  const url = hexo.extend.helper.get('url_for').bind(context);
  const posts = locals.posts.sort('date', 'desc').map(post => ({
    date: date(post.date, 'YYYY-MM-DD'),
    path: url(post.path),
    title: post.title,
    titleEn: post.title_en || post.title,
    searchText: [post.title, post.title_en, stripHTML(post.content || '')].filter(Boolean).join(' ').replace(/\s+/g, ' ').toLocaleLowerCase(),
  }));
  return { path: 'post-search.json', data: JSON.stringify(posts) };
});
