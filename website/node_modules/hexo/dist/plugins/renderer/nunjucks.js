"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const nunjucks_1 = __importDefault(require("nunjucks"));
const hexo_fs_1 = require("hexo-fs");
const path_1 = require("path");
function toArray(value) {
    if (Array.isArray(value)) {
        // Return if given value is an Array
        return value;
    }
    else if (typeof value.toArray === 'function') {
        return value.toArray();
    }
    else if (value instanceof Map) {
        const arr = [];
        value.forEach(v => arr.push(v));
        return arr;
    }
    else if (value instanceof Set || typeof value === 'string') {
        return [...value];
    }
    else if (typeof value === 'object' && value instanceof Object && Boolean(value)) {
        return Object.values(value);
    }
    return [];
}
function safeJsonStringify(json, spacer = undefined) {
    if (typeof json !== 'undefined' && json !== null) {
        return JSON.stringify(json, null, spacer);
    }
    return '""';
}
const nunjucksCfg = {
    autoescape: false,
    throwOnUndefined: false,
    trimBlocks: false,
    lstripBlocks: false
};
const nunjucksAddFilter = (env) => {
    env.addFilter('toarray', toArray);
    env.addFilter('safedump', safeJsonStringify);
};
function njkCompile(data) {
    let env;
    if (data.path) {
        env = nunjucks_1.default.configure((0, path_1.dirname)(data.path), nunjucksCfg);
    }
    else {
        env = nunjucks_1.default.configure(nunjucksCfg);
    }
    nunjucksAddFilter(env);
    const text = 'text' in data ? data.text : (0, hexo_fs_1.readFileSync)(data.path);
    return nunjucks_1.default.compile(text, env, data.path);
}
function njkRenderer(data, locals) {
    return njkCompile(data).render(locals);
}
njkRenderer.compile = (data) => {
    // Need a closure to keep the compiled template.
    return locals => njkCompile(data).render(locals);
};
module.exports = njkRenderer;
//# sourceMappingURL=nunjucks.js.map