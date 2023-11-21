"use strict";
const hexo_util_1 = require("hexo-util");
const path_1 = require("path");
module.exports = (ctx) => ({
    pattern: new hexo_util_1.Pattern('_data/*path'),
    process: function dataProcessor(file) {
        const Data = ctx.model('Data');
        const { path } = file.params;
        const id = path.substring(0, path.length - (0, path_1.extname)(path).length);
        const doc = Data.findById(id);
        if (file.type === 'skip' && doc) {
            return;
        }
        if (file.type === 'delete') {
            if (doc) {
                return doc.remove();
            }
            return;
        }
        return file.render().then(result => {
            if (result == null)
                return;
            return Data.save({
                _id: id,
                data: result
            });
        });
    }
});
//# sourceMappingURL=data.js.map