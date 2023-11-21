"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const picocolors_1 = require("picocolors");
const text_table_1 = __importDefault(require("text-table"));
const common_1 = require("./common");
function listPage() {
    const Page = this.model('Page');
    const data = Page.sort({ date: 1 }).map(page => {
        const date = page.date.format('YYYY-MM-DD');
        return [(0, picocolors_1.gray)(date), page.title, (0, picocolors_1.magenta)(page.source)];
    });
    // Table header
    const header = ['Date', 'Title', 'Path'].map(str => (0, picocolors_1.underline)(str));
    data.unshift(header);
    const t = (0, text_table_1.default)(data, {
        stringLength: common_1.stringLength
    });
    console.log(t);
    if (data.length === 1)
        console.log('No pages.');
}
module.exports = listPage;
//# sourceMappingURL=page.js.map