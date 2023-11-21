"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
module.exports = (ctx) => {
    const { highlight } = ctx.extend;
    highlight.register('highlight.js', require('./highlight'));
    highlight.register('prismjs', require('./prism'));
};
//# sourceMappingURL=index.js.map