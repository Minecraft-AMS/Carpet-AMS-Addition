"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const warehouse_1 = __importDefault(require("warehouse"));
module.exports = (ctx) => {
    const PostTag = new warehouse_1.default.Schema({
        post_id: { type: warehouse_1.default.Schema.Types.CUID, ref: 'Post' },
        tag_id: { type: warehouse_1.default.Schema.Types.CUID, ref: 'Tag' }
    });
    return PostTag;
};
//# sourceMappingURL=post_tag.js.map