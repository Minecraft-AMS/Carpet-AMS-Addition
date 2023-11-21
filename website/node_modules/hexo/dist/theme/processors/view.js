"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.view = void 0;
const hexo_util_1 = require("hexo-util");
function process(file) {
    const { path } = file.params;
    if (file.type === 'delete') {
        file.box.removeView(path);
        return;
    }
    return file.read().then(result => {
        file.box.setView(path, result);
    });
}
const pattern = new hexo_util_1.Pattern('layout/*path');
exports.view = {
    pattern,
    process
};
//# sourceMappingURL=view.js.map