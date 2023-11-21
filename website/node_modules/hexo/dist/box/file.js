"use strict";
const hexo_fs_1 = require("hexo-fs");
class File {
    constructor({ source, path, params, type }) {
        this.source = source;
        this.path = path;
        this.params = params;
        this.type = type;
    }
    read(options) {
        return (0, hexo_fs_1.readFile)(this.source, options);
    }
    readSync(options) {
        return (0, hexo_fs_1.readFileSync)(this.source, options);
    }
    stat() {
        return (0, hexo_fs_1.stat)(this.source);
    }
    statSync() {
        return (0, hexo_fs_1.statSync)(this.source);
    }
}
File.TYPE_CREATE = 'create';
File.TYPE_UPDATE = 'update';
File.TYPE_SKIP = 'skip';
File.TYPE_DELETE = 'delete';
module.exports = File;
//# sourceMappingURL=file.js.map