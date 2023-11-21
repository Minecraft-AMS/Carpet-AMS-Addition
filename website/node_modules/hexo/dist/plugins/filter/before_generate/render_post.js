"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const bluebird_1 = __importDefault(require("bluebird"));
function renderPostFilter() {
    const renderPosts = model => {
        const posts = model.toArray().filter(post => post.content == null);
        return bluebird_1.default.map(posts, (post) => {
            post.content = post._content;
            return this.post.render(post.full_source, post).then(() => post.save());
        });
    };
    return bluebird_1.default.all([
        renderPosts(this.model('Post')),
        renderPosts(this.model('Page'))
    ]);
}
module.exports = renderPostFilter;
//# sourceMappingURL=render_post.js.map