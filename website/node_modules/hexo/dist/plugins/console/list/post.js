"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const picocolors_1 = require("picocolors");
const text_table_1 = __importDefault(require("text-table"));
const common_1 = require("./common");
function mapName(item) {
    return item.name;
}
function listPost() {
    const Post = this.model('Post');
    const data = Post.sort({ published: -1, date: 1 }).map(post => {
        const date = post.published ? post.date.format('YYYY-MM-DD') : 'Draft';
        const tags = post.tags.map(mapName);
        const categories = post.categories.map(mapName);
        return [
            (0, picocolors_1.gray)(date),
            post.title,
            (0, picocolors_1.magenta)(post.source),
            categories.join(', '),
            tags.join(', ')
        ];
    });
    // Table header
    const header = ['Date', 'Title', 'Path', 'Category', 'Tags'].map(str => (0, picocolors_1.underline)(str));
    data.unshift(header);
    const t = (0, text_table_1.default)(data, {
        stringLength: common_1.stringLength
    });
    console.log(t);
    if (data.length === 1)
        console.log('No posts.');
}
module.exports = listPost;
//# sourceMappingURL=post.js.map