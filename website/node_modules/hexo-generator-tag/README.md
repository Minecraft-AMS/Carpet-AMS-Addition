# hexo-generator-tag

[![Build Status](https://github.com/hexojs/hexo-generator-tag/workflows/Tester/badge.svg)](https://github.com/hexojs/hexo-generator-tag/actions?query=workflow%3ATester)
[![NPM version](https://badge.fury.io/js/hexo-generator-tag.svg)](https://www.npmjs.com/package/hexo-generator-tag)
[![Coverage Status](https://img.shields.io/coveralls/hexojs/hexo-generator-tag.svg)](https://coveralls.io/r/hexojs/hexo-generator-tag?branch=master)

Tag generator for [Hexo].

## Installation

``` bash
$ npm install hexo-generator-tag --save
```

## Options

``` yaml
tag_generator:
  per_page: 10
  order_by: -date
```

- **per_page**: Posts displayed per page. (0 = disable pagination)
- **order_by**: Posts order. (Order by date descending by default)
- **enable_index_page**: Generate a `/[config.tag_dir]/index.html` page.
  - Support following template layout: `tag-index`, `tag`, `archive`, `index`

## License

MIT

[Hexo]: https://hexo.io/
