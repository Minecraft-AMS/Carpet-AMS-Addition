js-yaml-js-types
================

[![CI](https://github.com/nodeca/js-yaml-js-types/workflows/CI/badge.svg?branch=master)](https://github.com/nodeca/js-yaml-js-types/actions)
[![NPM version](https://img.shields.io/npm/v/js-yaml-js-types.svg)](https://www.npmjs.org/package/js-yaml-js-types)

> js-yaml extra types:
>
> - !!js/regexp /pattern/gim
> - !!js/undefined ''
> - !!js/function 'function () {...}'


Installation
------------

```sh
npm install js-yaml-js-types
```


Usage
-----

```js
const yaml = require('js-yaml');
const unsafe = require('js-yaml-js-types').all;

const schema = yaml.DEFAULT_SCHEMA.extend(unsafe);

const src = `
- !!js/regexp /pattern/gim
- !!js/undefined ''
- !!js/function 'function () { return true }'
`

yaml.load(src, { schema });
```
