"use strict";
const camel_case_1 = require("camel-case");
function getter(key) {
    return function () {
        return this[key];
    };
}
function setter(key) {
    return function (value) {
        this[key] = value;
    };
}
function toCamelCase(str) {
    let prefixLength = -1;
    while (str[++prefixLength] === '_')
        ;
    if (!prefixLength) {
        return (0, camel_case_1.camelCase)(str);
    }
    return str.substring(0, prefixLength) + (0, camel_case_1.camelCase)(str.substring(prefixLength));
}
function camelCaseKeys(obj) {
    if (typeof obj !== 'object')
        throw new TypeError('obj must be an object!');
    const keys = Object.keys(obj);
    const result = {};
    for (const oldKey of keys) {
        const newKey = toCamelCase(oldKey);
        result[newKey] = obj[oldKey];
        if (newKey !== oldKey) {
            Object.defineProperty(result, oldKey, {
                get: getter(newKey),
                set: setter(newKey),
                configurable: true,
                enumerable: true
            });
        }
    }
    return result;
}
module.exports = camelCaseKeys;
//# sourceMappingURL=camel_case_keys.js.map