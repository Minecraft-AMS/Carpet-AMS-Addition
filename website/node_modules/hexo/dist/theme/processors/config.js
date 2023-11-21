"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.config = void 0;
const hexo_util_1 = require("hexo-util");
function process(file) {
    if (file.type === 'delete') {
        file.box.config = {};
        return;
    }
    return file.render().then(result => {
        file.box.config = result;
        this.log.debug('Theme config loaded.');
    }).catch(err => {
        this.log.error('Theme config load failed.');
        throw err;
    });
}
const pattern = new hexo_util_1.Pattern(/^_config\.\w+$/);
exports.config = {
    pattern,
    process
};
//# sourceMappingURL=config.js.map