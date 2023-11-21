"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const hexo_fs_1 = require("hexo-fs");
const bluebird_1 = __importDefault(require("bluebird"));
module.exports = (ctx) => {
    if (ctx._dbLoaded)
        return bluebird_1.default.resolve();
    const db = ctx.database;
    const { path } = db.options;
    const { log } = ctx;
    return (0, hexo_fs_1.exists)(path).then(exist => {
        if (!exist)
            return;
        log.debug('Loading database.');
        return db.load();
    }).then(() => {
        ctx._dbLoaded = true;
    }).catch(() => {
        log.error('Database load failed. Deleting database.');
        return (0, hexo_fs_1.unlink)(path);
    });
};
//# sourceMappingURL=load_database.js.map