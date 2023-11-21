"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.i18n = void 0;
const hexo_util_1 = require("hexo-util");
const path_1 = require("path");
function process(file) {
    const { path } = file.params;
    const ext = (0, path_1.extname)(path);
    const name = path.substring(0, path.length - ext.length);
    const { i18n } = file.box;
    if (file.type === 'delete') {
        i18n.remove(name);
        return;
    }
    return file.render().then(data => {
        if (typeof data !== 'object')
            return;
        i18n.set(name, data);
    });
}
const pattern = new hexo_util_1.Pattern('languages/*path');
exports.i18n = {
    pattern,
    process
};
//# sourceMappingURL=i18n.js.map