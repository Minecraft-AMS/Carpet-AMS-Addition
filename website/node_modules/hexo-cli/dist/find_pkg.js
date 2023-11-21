"use strict";
const path_1 = require("path");
const hexo_fs_1 = require("hexo-fs");
function findPkg(cwd, args = {}) {
    if (args.cwd) {
        cwd = (0, path_1.resolve)(cwd, args.cwd);
    }
    return checkPkg(cwd);
}
function checkPkg(path) {
    const pkgPath = (0, path_1.join)(path, 'package.json');
    return (0, hexo_fs_1.readFile)(pkgPath).then(content => {
        const json = JSON.parse(content);
        if (typeof json.hexo === 'object')
            return path;
    }).catch(err => {
        if (err && err.code === 'ENOENT') {
            const parent = (0, path_1.dirname)(path);
            if (parent === path)
                return;
            return checkPkg(parent);
        }
        throw err;
    });
}
module.exports = findPkg;
//# sourceMappingURL=find_pkg.js.map