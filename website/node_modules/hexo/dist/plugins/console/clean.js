"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const bluebird_1 = __importDefault(require("bluebird"));
const hexo_fs_1 = require("hexo-fs");
function cleanConsole() {
    return bluebird_1.default.all([
        deleteDatabase(this),
        deletePublicDir(this),
        this.execFilter('after_clean', null, { context: this })
    ]);
}
function deleteDatabase(ctx) {
    const dbPath = ctx.database.options.path;
    return (0, hexo_fs_1.exists)(dbPath).then(exist => {
        if (!exist)
            return;
        return (0, hexo_fs_1.unlink)(dbPath).then(() => {
            ctx.log.info('Deleted database.');
        });
    });
}
function deletePublicDir(ctx) {
    const publicDir = ctx.public_dir;
    return (0, hexo_fs_1.exists)(publicDir).then(exist => {
        if (!exist)
            return;
        return (0, hexo_fs_1.rmdir)(publicDir).then(() => {
            ctx.log.info('Deleted public folder.');
        });
    });
}
module.exports = cleanConsole;
//# sourceMappingURL=clean.js.map