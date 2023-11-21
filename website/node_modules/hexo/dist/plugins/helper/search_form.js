"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const moize_1 = __importDefault(require("moize"));
function searchFormHelper(options = {}) {
    const { config } = this;
    const className = options.class || 'search-form';
    const { text = 'Search', button } = options;
    return `<form action="//google.com/search" method="get" accept-charset="UTF-8" class="${className}"><input type="search" name="q" class="${className}-input"${text ? ` placeholder="${text}"` : ''}>${button ? `<button type="submit" class="${className}-submit">${typeof button === 'string' ? button : text}</button>` : ''}<input type="hidden" name="sitesearch" value="${config.url}"></form>`;
}
module.exports = moize_1.default.deep(searchFormHelper);
//# sourceMappingURL=search_form.js.map