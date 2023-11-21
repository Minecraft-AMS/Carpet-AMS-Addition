"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const common_1 = require("./common");
const bluebird_1 = __importDefault(require("bluebird"));
const hexo_front_matter_1 = require("hexo-front-matter");
const path_1 = require("path");
const hexo_util_1 = require("hexo-util");
const picocolors_1 = require("picocolors");
function processPage(ctx, file) {
    const Page = ctx.model('Page');
    const { path } = file;
    const doc = Page.findOne({ source: path });
    const { config } = ctx;
    const { timezone: timezoneCfg } = config;
    const updated_option = config.updated_option;
    if (file.type === 'skip' && doc) {
        return;
    }
    if (file.type === 'delete') {
        if (doc) {
            return doc.remove();
        }
        return;
    }
    return bluebird_1.default.all([
        file.stat(),
        file.read()
    ]).spread((stats, content) => {
        const data = (0, hexo_front_matter_1.parse)(content);
        const output = ctx.render.getOutput(path);
        data.source = path;
        data.raw = content;
        data.date = (0, common_1.toDate)(data.date);
        if (data.date) {
            if (timezoneCfg)
                data.date = (0, common_1.timezone)(data.date, timezoneCfg);
        }
        else {
            data.date = stats.ctime;
        }
        data.updated = (0, common_1.toDate)(data.updated);
        if (data.updated) {
            if (timezoneCfg)
                data.updated = (0, common_1.timezone)(data.updated, timezoneCfg);
        }
        else if (updated_option === 'date') {
            data.updated = data.date;
        }
        else if (updated_option === 'empty') {
            data.updated = undefined;
        }
        else {
            data.updated = stats.mtime;
        }
        if (data.permalink) {
            data.path = data.permalink;
            data.permalink = undefined;
            if (data.path.endsWith('/')) {
                data.path += 'index';
            }
            if (!(0, path_1.extname)(data.path)) {
                data.path += `.${output}`;
            }
        }
        else {
            data.path = `${path.substring(0, path.length - (0, path_1.extname)(path).length)}.${output}`;
        }
        if (!data.layout && output !== 'html' && output !== 'htm') {
            data.layout = false;
        }
        if (doc) {
            if (file.type !== 'update') {
                ctx.log.warn(`Trying to "create" ${(0, picocolors_1.magenta)(file.path)}, but the file already exists!`);
            }
            return doc.replace(data);
        }
        return Page.insert(data);
    });
}
function processAsset(ctx, file) {
    const id = (0, path_1.relative)(ctx.base_dir, file.source).replace(/\\/g, '/');
    const Asset = ctx.model('Asset');
    const doc = Asset.findById(id);
    if (file.type === 'delete') {
        if (doc) {
            return doc.remove();
        }
        return;
    }
    return Asset.save({
        _id: id,
        path: file.path,
        modified: file.type !== 'skip',
        renderable: file.params.renderable
    });
}
module.exports = (ctx) => {
    return {
        pattern: new hexo_util_1.Pattern(path => {
            if ((0, common_1.isExcludedFile)(path, ctx.config))
                return;
            return {
                renderable: ctx.render.isRenderable(path) && !(0, common_1.isMatch)(path, ctx.config.skip_render)
            };
        }),
        process: function assetProcessor(file) {
            if (file.params.renderable) {
                return processPage(ctx, file);
            }
            return processAsset(ctx, file);
        }
    };
};
//# sourceMappingURL=asset.js.map