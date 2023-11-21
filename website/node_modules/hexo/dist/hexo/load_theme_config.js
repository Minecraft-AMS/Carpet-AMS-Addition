"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const path_1 = require("path");
const tildify_1 = __importDefault(require("tildify"));
const hexo_fs_1 = require("hexo-fs");
const picocolors_1 = require("picocolors");
const hexo_util_1 = require("hexo-util");
function findConfigPath(path) {
    const { dir, name } = (0, path_1.parse)(path);
    return (0, hexo_fs_1.readdir)(dir).then(files => {
        const item = files.find(item => item.startsWith(name));
        if (item != null)
            return (0, path_1.join)(dir, item);
    });
}
module.exports = (ctx) => {
    if (!ctx.env.init)
        return;
    if (!ctx.config.theme)
        return;
    let configPath = (0, path_1.join)(ctx.base_dir, `_config.${String(ctx.config.theme)}.yml`);
    return (0, hexo_fs_1.exists)(configPath).then(exist => {
        return exist ? configPath : findConfigPath(configPath);
    }).then(path => {
        if (!path)
            return;
        configPath = path;
        return ctx.render.render({ path });
    }).then(config => {
        if (!config || typeof config !== 'object')
            return;
        ctx.log.debug('Second Theme Config loaded: %s', (0, picocolors_1.magenta)((0, tildify_1.default)(configPath)));
        // ctx.config.theme_config should have highest priority
        // If ctx.config.theme_config exists, then merge it with _config.[theme].yml
        // If ctx.config.theme_config doesn't exist, set it to _config.[theme].yml
        ctx.config.theme_config = ctx.config.theme_config
            ? (0, hexo_util_1.deepMerge)(config, ctx.config.theme_config) : config;
    });
};
//# sourceMappingURL=load_theme_config.js.map