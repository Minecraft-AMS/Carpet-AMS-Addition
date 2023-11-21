"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const js_yaml_1 = __importDefault(require("js-yaml"));
const hexo_fs_1 = require("hexo-fs");
const path_1 = require("path");
const bluebird_1 = __importDefault(require("bluebird"));
function configConsole(args) {
    const key = args._[0];
    let value = args._[1];
    if (!key) {
        console.log(this.config);
        return bluebird_1.default.resolve();
    }
    if (!value) {
        value = getProperty(this.config, key);
        if (value)
            console.log(value);
        return bluebird_1.default.resolve();
    }
    const configPath = this.config_path;
    const ext = (0, path_1.extname)(configPath);
    return (0, hexo_fs_1.exists)(configPath).then(exist => {
        if (!exist)
            return {};
        return this.render.render({ path: configPath });
    }).then(config => {
        if (!config)
            config = {};
        setProperty(config, key, castValue(value));
        const result = ext === '.json' ? JSON.stringify(config) : js_yaml_1.default.dump(config);
        return (0, hexo_fs_1.writeFile)(configPath, result);
    });
}
function getProperty(obj, key) {
    const split = key.split('.');
    let result = obj[split[0]];
    for (let i = 1, len = split.length; i < len; i++) {
        result = result[split[i]];
    }
    return result;
}
function setProperty(obj, key, value) {
    const split = key.split('.');
    let cursor = obj;
    const lastKey = split.pop();
    for (let i = 0, len = split.length; i < len; i++) {
        const name = split[i];
        cursor[name] = cursor[name] || {};
        cursor = cursor[name];
    }
    cursor[lastKey] = value;
}
function castValue(value) {
    switch (value) {
        case 'true':
            return true;
        case 'false':
            return false;
        case 'null':
            return null;
        case 'undefined':
            return undefined;
    }
    const num = Number(value);
    if (!isNaN(num))
        return num;
    return value;
}
module.exports = configConsole;
//# sourceMappingURL=config.js.map