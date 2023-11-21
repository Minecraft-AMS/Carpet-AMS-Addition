"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const picocolors_1 = require("picocolors");
const text_table_1 = __importDefault(require("text-table"));
const common_1 = require("./common");
function listCategory() {
    const categories = this.model('Category');
    const data = categories.sort({ name: 1 }).map(cate => [cate.name, String(cate.length)]);
    // Table header
    const header = ['Name', 'Posts'].map(str => (0, picocolors_1.underline)(str));
    data.unshift(header);
    const t = (0, text_table_1.default)(data, {
        align: ['l', 'r'],
        stringLength: common_1.stringLength
    });
    console.log(t);
    if (data.length === 1)
        console.log('No categories.');
}
module.exports = listCategory;
//# sourceMappingURL=category.js.map