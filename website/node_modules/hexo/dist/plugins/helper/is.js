"use strict";
Object.defineProperty(exports, "__esModule", { value: true });
exports.tag = exports.category = exports.month = exports.year = exports.archive = exports.page = exports.post = exports.home_first_page = exports.home = exports.current = void 0;
function isCurrentHelper(path = '/', strict) {
    const currentPath = this.path.replace(/^[^/].*/, '/$&');
    if (strict) {
        if (path.endsWith('/'))
            path += 'index.html';
        path = path.replace(/^[^/].*/, '/$&');
        return currentPath === path;
    }
    path = path.replace(/\/index\.html$/, '/');
    if (path === '/')
        return currentPath === '/index.html';
    path = path.replace(/^[^/].*/, '/$&');
    return currentPath.startsWith(path);
}
exports.current = isCurrentHelper;
function isHomeHelper() {
    return Boolean(this.page.__index);
}
exports.home = isHomeHelper;
function isHomeFirstPageHelper() {
    return Boolean(this.page.__index) && this.page.current === 1;
}
exports.home_first_page = isHomeFirstPageHelper;
function isPostHelper() {
    return Boolean(this.page.__post);
}
exports.post = isPostHelper;
function isPageHelper() {
    return Boolean(this.page.__page);
}
exports.page = isPageHelper;
function isArchiveHelper() {
    return Boolean(this.page.archive);
}
exports.archive = isArchiveHelper;
function isYearHelper(year) {
    const { page } = this;
    if (!page.archive)
        return false;
    if (year) {
        return page.year === year;
    }
    return Boolean(page.year);
}
exports.year = isYearHelper;
function isMonthHelper(year, month) {
    const { page } = this;
    if (!page.archive)
        return false;
    if (year) {
        if (month) {
            return page.year === year && page.month === month;
        }
        return page.month === year;
    }
    return Boolean(page.year && page.month);
}
exports.month = isMonthHelper;
function isCategoryHelper(category) {
    if (category) {
        return this.page.category === category;
    }
    return Boolean(this.page.category);
}
exports.category = isCategoryHelper;
function isTagHelper(tag) {
    if (tag) {
        return this.page.tag === tag;
    }
    return Boolean(this.page.tag);
}
exports.tag = isTagHelper;
//# sourceMappingURL=is.js.map