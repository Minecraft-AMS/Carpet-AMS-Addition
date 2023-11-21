"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const path_1 = require("path");
const hexo_fs_1 = require("hexo-fs");
const bluebird_1 = __importDefault(require("bluebird"));
const picocolors_1 = require("picocolors");
function loadModuleList(ctx, basedir) {
    const packagePath = (0, path_1.join)(basedir, 'package.json');
    // Make sure package.json exists
    return (0, hexo_fs_1.exists)(packagePath).then(exist => {
        if (!exist)
            return [];
        // Read package.json and find dependencies
        return (0, hexo_fs_1.readFile)(packagePath).then(content => {
            const json = JSON.parse(content);
            const deps = Object.keys(json.dependencies || {});
            const devDeps = Object.keys(json.devDependencies || {});
            return basedir === ctx.base_dir ? deps.concat(devDeps) : deps;
        });
    }).filter(name => {
        // Ignore plugins whose name is not started with "hexo-"
        if (!/^hexo-|^@[^/]+\/hexo-/.test(name))
            return false;
        // Ignore plugin whose name is started with "hexo-theme"
        if (/^hexo-theme-|^@[^/]+\/hexo-theme-/.test(name))
            return false;
        // Ignore typescript definition file that is started with "@types/"
        if (name.startsWith('@types/'))
            return false;
        // Make sure the plugin exists
        const path = ctx.resolvePlugin(name, basedir);
        return (0, hexo_fs_1.exists)(path);
    }).then(modules => {
        return Object.fromEntries(modules.map(name => [name, ctx.resolvePlugin(name, basedir)]));
    });
}
function loadModules(ctx) {
    return bluebird_1.default.map([ctx.base_dir, ctx.theme_dir], basedir => loadModuleList(ctx, basedir))
        .then(([hexoModuleList, themeModuleList]) => {
        return Object.entries(Object.assign(themeModuleList, hexoModuleList));
    })
        .map(([name, path]) => {
        // Load plugins
        return ctx.loadPlugin(path).then(() => {
            ctx.log.debug('Plugin loaded: %s', (0, picocolors_1.magenta)(name));
        }).catch(err => {
            ctx.log.error({ err }, 'Plugin load failed: %s', (0, picocolors_1.magenta)(name));
        });
    });
}
function loadScripts(ctx) {
    const baseDirLength = ctx.base_dir.length;
    return bluebird_1.default.filter([
        ctx.theme_script_dir,
        ctx.script_dir
    ], scriptDir => {
        return scriptDir ? (0, hexo_fs_1.exists)(scriptDir) : false;
    }).map(scriptDir => (0, hexo_fs_1.listDir)(scriptDir).map(name => {
        const path = (0, path_1.join)(scriptDir, name);
        return ctx.loadPlugin(path).then(() => {
            ctx.log.debug('Script loaded: %s', displayPath(path, baseDirLength));
        }).catch(err => {
            ctx.log.error({ err }, 'Script load failed: %s', displayPath(path, baseDirLength));
        });
    }));
}
function displayPath(path, baseDirLength) {
    return (0, picocolors_1.magenta)(path.substring(baseDirLength));
}
module.exports = (ctx) => {
    if (!ctx.env.init || ctx.env.safe)
        return;
    return loadModules(ctx).then(() => loadScripts(ctx));
};
//# sourceMappingURL=load_plugins.js.map