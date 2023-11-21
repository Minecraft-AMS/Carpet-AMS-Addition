'use strict';

const { format } = require('util');

function pagination(base, posts, options = {}) {
  if (typeof base !== 'string') throw new TypeError('base must be a string!');
  if (!posts) throw new TypeError('posts is required!');

  if (base && !base.endsWith('/')) base += '/';

  const { length } = posts;

  const { format: _format, layout, data, perPage } = Object.assign({
    format: 'page/%d/',
    layout: ['archive', 'index'],
    data: {},
    perPage: 10
  }, options);

  const total = perPage ? Math.ceil(length / perPage) : 1;
  const result = [];
  const urlCache = new Map();

  function formatURL(i) {
    if (urlCache.has(i)) return urlCache.get(i);

    const url = i > 1 ? base + format(_format, i) : base;
    urlCache.set(i, url);

    return url;
  }

  function makeData(i) {
    const data = {
      base,
      total,
      current: i,
      current_url: formatURL(i),
      posts: perPage ? posts.slice(perPage * (i - 1), perPage * i) : posts,
      prev: 0,
      prev_link: '',
      next: 0,
      next_link: ''
    };

    if (i > 1) {
      data.prev = i - 1;
      data.prev_link = formatURL(data.prev);
    }

    if (i < total) {
      data.next = i + 1;
      data.next_link = formatURL(data.next);
    }

    return data;
  }

  if (perPage) {
    for (let i = 1; i <= total; i++) {
      result.push({
        path: formatURL(i),
        layout,
        data: Object.assign(makeData(i), data)
      });
    }
  } else {
    result.push({
      path: base,
      layout,
      data: Object.assign(makeData(1), data)
    });
  }

  return result;
}

module.exports = pagination;
