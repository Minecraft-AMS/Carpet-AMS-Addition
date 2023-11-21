"use strict";
const path_1 = require("path");
const hexo_fs_1 = require("hexo-fs");
function readPkg(path) {
    return (0, hexo_fs_1.exists)(path).then(exist => {
        if (!exist)
            return;
        return (0, hexo_fs_1.readFile)(path).then(content => {
            const pkg = JSON.parse(content);
            if (typeof pkg.hexo !== 'object')
                return;
            return pkg;
        });
    });
}
module.exports = (ctx) => {
    const pkgPath = (0, path_1.join)(ctx.base_dir, 'package.json');
    return readPkg(pkgPath).then(pkg => {
        if (!pkg)
            return;
        ctx.env.init = true;
        if (pkg.hexo.version === ctx.version)
            return;
        pkg.hexo.version = ctx.version;
        ctx.log.debug('Updating package.json');
        return (0, hexo_fs_1.writeFile)(pkgPath, JSON.stringify(pkg, null, '  '));
    });
};
//# sourceMappingURL=update_package.js.map