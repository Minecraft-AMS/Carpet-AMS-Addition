"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const warehouse_1 = __importDefault(require("warehouse"));
const path_1 = require("path");
const moment_1 = __importDefault(require("./types/moment"));
const moment_2 = __importDefault(require("moment"));
const hexo_util_1 = require("hexo-util");
module.exports = (ctx) => {
    const Page = new warehouse_1.default.Schema({
        title: { type: String, default: '' },
        date: {
            type: moment_1.default,
            default: moment_2.default,
            language: ctx.config.languages,
            timezone: ctx.config.timezone
        },
        updated: {
            type: moment_1.default,
            language: ctx.config.languages,
            timezone: ctx.config.timezone
        },
        comments: { type: Boolean, default: true },
        layout: { type: String, default: 'page' },
        _content: { type: String, default: '' },
        source: { type: String, required: true },
        path: { type: String, required: true },
        raw: { type: String, default: '' },
        content: { type: String },
        excerpt: { type: String },
        more: { type: String }
    });
    Page.virtual('permalink').get(function () {
        return hexo_util_1.full_url_for.call(ctx, this.path);
    });
    Page.virtual('full_source').get(function () {
        return (0, path_1.join)(ctx.source_dir, this.source || '');
    });
    return Page;
};
//# sourceMappingURL=page.js.map