"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const path_1 = require("path");
const bluebird_1 = __importDefault(require("bluebird"));
const hexo_fs_1 = require("hexo-fs");
const getExtname = (str) => {
    if (typeof str !== 'string')
        return '';
    const ext = (0, path_1.extname)(str);
    return ext.startsWith('.') ? ext.slice(1) : ext;
};
const toString = (result, options) => {
    if (!Object.prototype.hasOwnProperty.call(options, 'toString') || typeof result === 'string')
        return result;
    if (typeof options.toString === 'function') {
        return options.toString(result);
    }
    else if (typeof result === 'object') {
        return JSON.stringify(result);
    }
    else if (result.toString) {
        return result.toString();
    }
    return result;
};
class Render {
    constructor(ctx) {
        this.context = ctx;
        this.renderer = ctx.extend.renderer;
    }
    isRenderable(path) {
        return this.renderer.isRenderable(path);
    }
    isRenderableSync(path) {
        return this.renderer.isRenderableSync(path);
    }
    getOutput(path) {
        return this.renderer.getOutput(path);
    }
    getRenderer(ext, sync) {
        return this.renderer.get(ext, sync);
    }
    getRendererSync(ext) {
        return this.getRenderer(ext, true);
    }
    render(data, options, callback) {
        if (!callback && typeof options === 'function') {
            callback = options;
            options = {};
        }
        const ctx = this.context;
        let ext = '';
        let promise;
        if (!data)
            return bluebird_1.default.reject(new TypeError('No input file or string!'));
        if (data.text != null) {
            promise = bluebird_1.default.resolve(data.text);
        }
        else if (!data.path) {
            return bluebird_1.default.reject(new TypeError('No input file or string!'));
        }
        else {
            promise = (0, hexo_fs_1.readFile)(data.path);
        }
        return promise.then(text => {
            data.text = text;
            ext = data.engine || getExtname(data.path);
            if (!ext || !this.isRenderable(ext))
                return text;
            const renderer = this.getRenderer(ext);
            return Reflect.apply(renderer, ctx, [data, options]);
        }).then(result => {
            result = toString(result, data);
            if (data.onRenderEnd) {
                return data.onRenderEnd(result);
            }
            return result;
        }).then(result => {
            const output = this.getOutput(ext) || ext;
            return ctx.execFilter(`after_render:${output}`, result, {
                context: ctx,
                args: [data]
            });
        }).asCallback(callback);
    }
    renderSync(data, options = {}) {
        if (!data)
            throw new TypeError('No input file or string!');
        const ctx = this.context;
        if (data.text == null) {
            if (!data.path)
                throw new TypeError('No input file or string!');
            data.text = (0, hexo_fs_1.readFileSync)(data.path);
        }
        if (data.text == null)
            throw new TypeError('No input file or string!');
        const ext = data.engine || getExtname(data.path);
        let result;
        if (ext && this.isRenderableSync(ext)) {
            const renderer = this.getRendererSync(ext);
            result = Reflect.apply(renderer, ctx, [data, options]);
        }
        else {
            result = data.text;
        }
        const output = this.getOutput(ext) || ext;
        result = toString(result, data);
        if (data.onRenderEnd) {
            result = data.onRenderEnd(result);
        }
        return ctx.execFilterSync(`after_render:${output}`, result, {
            context: ctx,
            args: [data]
        });
    }
}
module.exports = Render;
//# sourceMappingURL=render.js.map