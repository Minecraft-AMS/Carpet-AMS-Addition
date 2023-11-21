"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const picocolors_1 = require("picocolors");
const text_table_1 = __importDefault(require("text-table"));
const common_1 = require("./common");
function listTag() {
    const Tag = this.model('Tag');
    const data = Tag.sort({ name: 1 }).map(tag => [tag.name, String(tag.length), (0, picocolors_1.magenta)(tag.path)]);
    // Table header
    const header = ['Name', 'Posts', 'Path'].map(str => (0, picocolors_1.underline)(str));
    data.unshift(header);
    const t = (0, text_table_1.default)(data, {
        align: ['l', 'r', 'l'],
        stringLength: common_1.stringLength
    });
    console.log(t);
    if (data.length === 1)
        console.log('No tags.');
}
module.exports = listTag;
//# sourceMappingURL=tag.js.map