"use strict";
const path_1 = require("path");
const hexo_fs_1 = require("hexo-fs");
class Scaffold {
    constructor(context) {
        this.context = context;
        this.scaffoldDir = context.scaffold_dir;
        this.defaults = {
            normal: [
                '---',
                'layout: {{ layout }}',
                'title: {{ title }}',
                'date: {{ date }}',
                'tags:',
                '---'
            ].join('\n')
        };
    }
    _listDir() {
        const { scaffoldDir } = this;
        return (0, hexo_fs_1.exists)(scaffoldDir).then(exist => {
            if (!exist)
                return [];
            return (0, hexo_fs_1.listDir)(scaffoldDir, {
                ignorePattern: /^_|\/_/
            });
        }).map(item => ({
            name: item.substring(0, item.length - (0, path_1.extname)(item).length),
            path: (0, path_1.join)(scaffoldDir, item)
        }));
    }
    _getScaffold(name) {
        return this._listDir().then(list => list.find(item => item.name === name));
    }
    get(name, callback) {
        return this._getScaffold(name).then(item => {
            if (item) {
                return (0, hexo_fs_1.readFile)(item.path);
            }
            return this.defaults[name];
        }).asCallback(callback);
    }
    set(name, content, callback) {
        const { scaffoldDir } = this;
        return this._getScaffold(name).then(item => {
            let path = item ? item.path : (0, path_1.join)(scaffoldDir, name);
            if (!(0, path_1.extname)(path))
                path += '.md';
            return (0, hexo_fs_1.writeFile)(path, content);
        }).asCallback(callback);
    }
    remove(name, callback) {
        return this._getScaffold(name).then(item => {
            if (!item)
                return;
            return (0, hexo_fs_1.unlink)(item.path);
        }).asCallback(callback);
    }
}
module.exports = Scaffold;
//# sourceMappingURL=scaffold.js.map