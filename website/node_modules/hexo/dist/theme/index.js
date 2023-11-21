"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const path_1 = require("path");
const box_1 = __importDefault(require("../box"));
const view_1 = __importDefault(require("./view"));
const hexo_i18n_1 = __importDefault(require("hexo-i18n"));
const config_1 = require("./processors/config");
const i18n_1 = require("./processors/i18n");
const source_1 = require("./processors/source");
const view_2 = require("./processors/view");
class Theme extends box_1.default {
    constructor(ctx, options) {
        super(ctx, ctx.theme_dir, options);
        this.config = {};
        this.views = {};
        this.processors = [
            config_1.config,
            i18n_1.i18n,
            source_1.source,
            view_2.view
        ];
        let languages = ctx.config.language;
        if (!Array.isArray(languages))
            languages = [languages];
        languages.push('default');
        this.i18n = new hexo_i18n_1.default({
            languages: [...new Set(languages.filter(Boolean))]
        });
        class _View extends view_1.default {
        }
        this.View = _View;
        _View.prototype._theme = this;
        _View.prototype._render = ctx.render;
        _View.prototype._helper = ctx.extend.helper;
    }
    getView(path) {
        // Replace backslashes on Windows
        path = path.replace(/\\/g, '/');
        const ext = (0, path_1.extname)(path);
        const name = path.substring(0, path.length - ext.length);
        const views = this.views[name];
        if (!views)
            return;
        if (ext) {
            return views[ext];
        }
        return views[Object.keys(views)[0]];
    }
    setView(path, data) {
        const ext = (0, path_1.extname)(path);
        const name = path.substring(0, path.length - ext.length);
        this.views[name] = this.views[name] || {};
        const views = this.views[name];
        views[ext] = new this.View(path, data);
    }
    removeView(path) {
        const ext = (0, path_1.extname)(path);
        const name = path.substring(0, path.length - ext.length);
        const views = this.views[name];
        if (!views)
            return;
        views[ext] = undefined;
    }
}
module.exports = Theme;
//# sourceMappingURL=index.js.map