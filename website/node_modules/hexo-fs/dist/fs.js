"use strict";
var __awaiter = (this && this.__awaiter) || function (thisArg, _arguments, P, generator) {
    function adopt(value) { return value instanceof P ? value : new P(function (resolve) { resolve(value); }); }
    return new (P || (P = Promise))(function (resolve, reject) {
        function fulfilled(value) { try { step(generator.next(value)); } catch (e) { reject(e); } }
        function rejected(value) { try { step(generator["throw"](value)); } catch (e) { reject(e); } }
        function step(result) { result.done ? resolve(result.value) : adopt(result.value).then(fulfilled, rejected); }
        step((generator = generator.apply(thisArg, _arguments || [])).next());
    });
};
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
Object.defineProperty(exports, "__esModule", { value: true });
exports.mkdirSync = exports.mkdir = exports.linkSync = exports.link = exports.fsyncSync = exports.fsync = exports.createWriteStream = exports.createReadStream = exports.closeSync = exports.close = exports.lchownSync = exports.lchown = exports.fchownSync = exports.fchown = exports.chownSync = exports.chown = exports.lchmodSync = exports.lchmod = exports.fchmodSync = exports.fchmod = exports.chmodSync = exports.chmod = exports.accessSync = exports.access = exports.ensureWriteStreamSync = exports.ensureWriteStream = exports.ensurePathSync = exports.ensurePath = exports.watch = exports.rmdirSync = exports.rmdir = exports.emptyDirSync = exports.emptyDir = exports.readFileSync = exports.readFile = exports.escapeFileContent = exports.escapeBOM = exports.escapeEOL = exports.listDirSync = exports.listDir = exports.copyDir = exports.copyFile = exports.appendFileSync = exports.appendFile = exports.writeFileSync = exports.writeFile = exports.mkdirsSync = exports.mkdirs = exports.existsSync = exports.exists = void 0;
exports.WriteStream = exports.ReadStream = exports.Stats = exports.writeSync = exports.write = exports.unwatchFile = exports.watchFile = exports.futimesSync = exports.futimes = exports.utimesSync = exports.utimes = exports.unlinkSync = exports.unlink = exports.ftruncateSync = exports.ftruncate = exports.truncateSync = exports.truncate = exports.lstatSync = exports.lstat = exports.fstatSync = exports.fstat = exports.statSync = exports.stat = exports.renameSync = exports.rename = exports.realpathSync = exports.realpath = exports.readlinkSync = exports.readlink = exports.readdirSync = exports.readdir = exports.readSync = exports.read = exports.symlinkSync = exports.symlink = exports.openSync = exports.open = void 0;
const chokidar_1 = __importDefault(require("chokidar"));
const bluebird_1 = __importDefault(require("bluebird"));
const path_1 = require("path");
const hexo_util_1 = require("hexo-util");
const graceful_fs_1 = __importDefault(require("graceful-fs"));
const fsPromises = graceful_fs_1.default.promises;
const rEOL = /\r\n/g;
function exists(path) {
    if (!path)
        throw new TypeError('path is required!');
    const promise = fsPromises.access(path).then(() => true, error => {
        if (error.code !== 'ENOENT')
            throw error;
        return false;
    });
    return bluebird_1.default.resolve(promise);
}
exports.exists = exists;
function existsSync(path) {
    if (!path)
        throw new TypeError('path is required!');
    try {
        graceful_fs_1.default.accessSync(path);
    }
    catch (err) {
        if (err.code !== 'ENOENT')
            throw err;
        return false;
    }
    return true;
}
exports.existsSync = existsSync;
function mkdirs(path) {
    if (!path)
        throw new TypeError('path is required!');
    return bluebird_1.default.resolve(fsPromises.mkdir(path, { recursive: true }));
}
exports.mkdirs = mkdirs;
function mkdirsSync(path) {
    if (!path)
        throw new TypeError('path is required!');
    graceful_fs_1.default.mkdirSync(path, { recursive: true });
}
exports.mkdirsSync = mkdirsSync;
function checkParent(path) {
    return bluebird_1.default.resolve(fsPromises.mkdir((0, path_1.dirname)(path), { recursive: true }));
}
function writeFile(path, data, options) {
    if (!path)
        throw new TypeError('path is required!');
    if (!data)
        data = '';
    return checkParent(path)
        .then(() => fsPromises.writeFile(path, data, options));
}
exports.writeFile = writeFile;
function writeFileSync(path, data, options) {
    if (!path)
        throw new TypeError('path is required!');
    graceful_fs_1.default.mkdirSync((0, path_1.dirname)(path), { recursive: true });
    graceful_fs_1.default.writeFileSync(path, data, options);
}
exports.writeFileSync = writeFileSync;
function appendFile(path, data, options) {
    if (!path)
        throw new TypeError('path is required!');
    return checkParent(path)
        .then(() => fsPromises.appendFile(path, data, options));
}
exports.appendFile = appendFile;
function appendFileSync(path, data, options) {
    if (!path)
        throw new TypeError('path is required!');
    graceful_fs_1.default.mkdirSync((0, path_1.dirname)(path), { recursive: true });
    graceful_fs_1.default.appendFileSync(path, data, options);
}
exports.appendFileSync = appendFileSync;
function copyFile(src, dest, flags) {
    if (!src)
        throw new TypeError('src is required!');
    if (!dest)
        throw new TypeError('dest is required!');
    return checkParent(dest)
        .then(() => fsPromises.copyFile(src, dest, flags));
}
exports.copyFile = copyFile;
const trueFn = () => true;
function ignoreHiddenFiles(ignore) {
    if (!ignore)
        return trueFn;
    return ({ name }) => !name.startsWith('.');
}
function ignoreFilesRegex(regex) {
    if (!regex)
        return trueFn;
    return ({ name }) => !regex.test(name);
}
function ignoreExcludeFiles(arr, parent) {
    if (!arr || !arr.length)
        return trueFn;
    const set = new Set(arr);
    return ({ name }) => !set.has((0, path_1.join)(parent, name));
}
function _readAndFilterDir(path, options = {}) {
    return __awaiter(this, void 0, void 0, function* () {
        const { ignoreHidden = true, ignorePattern } = options;
        return (yield fsPromises.readdir(path, Object.assign(Object.assign({}, options), { withFileTypes: true })))
            .filter(ignoreHiddenFiles(ignoreHidden))
            .filter(ignoreFilesRegex(ignorePattern));
    });
}
function _readAndFilterDirSync(path, options) {
    const { ignoreHidden = true, ignorePattern } = options;
    return graceful_fs_1.default.readdirSync(path, Object.assign(Object.assign({}, options), { withFileTypes: true }))
        .filter(ignoreHiddenFiles(ignoreHidden))
        .filter(ignoreFilesRegex(ignorePattern));
}
function _copyDirWalker(src, dest, results, parent, options) {
    return __awaiter(this, void 0, void 0, function* () {
        return bluebird_1.default.map(_readAndFilterDir(src, options), item => {
            const childSrc = (0, path_1.join)(src, item.name);
            const childDest = (0, path_1.join)(dest, item.name);
            const currentPath = (0, path_1.join)(parent, item.name);
            if (item.isDirectory()) {
                return _copyDirWalker(childSrc, childDest, results, currentPath, options);
            }
            results.push(currentPath);
            return copyFile(childSrc, childDest, 0);
        });
    });
}
function copyDir(src, dest, options = {}) {
    if (!src)
        throw new TypeError('src is required!');
    if (!dest)
        throw new TypeError('dest is required!');
    const results = [];
    return checkParent(dest)
        .then(() => _copyDirWalker(src, dest, results, '', options))
        .return(results);
}
exports.copyDir = copyDir;
function _listDirWalker(path, results, parent, options) {
    return __awaiter(this, void 0, void 0, function* () {
        const promises = [];
        for (const item of yield _readAndFilterDir(path, options)) {
            const currentPath = (0, path_1.join)(parent, item.name);
            if (item.isDirectory()) {
                promises.push(_listDirWalker((0, path_1.join)(path, item.name), results, currentPath, options));
            }
            else {
                results.push(currentPath);
            }
        }
        yield bluebird_1.default.all(promises);
    });
}
function listDir(path, options = {}) {
    if (!path)
        throw new TypeError('path is required!');
    const results = [];
    return bluebird_1.default.resolve(_listDirWalker(path, results, '', options))
        .return(results);
}
exports.listDir = listDir;
function _listDirSyncWalker(path, results, parent, options) {
    for (const item of _readAndFilterDirSync(path, options)) {
        const currentPath = (0, path_1.join)(parent, item.name);
        if (item.isDirectory()) {
            _listDirSyncWalker((0, path_1.join)(path, item.name), results, currentPath, options);
        }
        else {
            results.push(currentPath);
        }
    }
}
function listDirSync(path, options = {}) {
    if (!path)
        throw new TypeError('path is required!');
    const results = [];
    _listDirSyncWalker(path, results, '', options);
    return results;
}
exports.listDirSync = listDirSync;
function escapeEOL(str) {
    return str.replace(rEOL, '\n');
}
exports.escapeEOL = escapeEOL;
function escapeBOM(str) {
    return str.charCodeAt(0) === 0xFEFF ? str.substring(1) : str;
}
exports.escapeBOM = escapeBOM;
function escapeFileContent(content) {
    return escapeBOM(escapeEOL(content));
}
exports.escapeFileContent = escapeFileContent;
function _readFile(path, options = {}) {
    return __awaiter(this, void 0, void 0, function* () {
        if (!Object.prototype.hasOwnProperty.call(options, 'encoding'))
            options.encoding = 'utf8';
        const content = yield fsPromises.readFile(path, options);
        if (options.escape == null || options.escape) {
            return escapeFileContent(content);
        }
        return content;
    });
}
function readFile(path, options) {
    if (!path)
        throw new TypeError('path is required!');
    return bluebird_1.default.resolve(_readFile(path, options));
}
exports.readFile = readFile;
function readFileSync(path, options = {}) {
    if (!path)
        throw new TypeError('path is required!');
    if (!Object.prototype.hasOwnProperty.call(options, 'encoding'))
        options.encoding = 'utf8';
    const content = graceful_fs_1.default.readFileSync(path, options);
    if (options.escape == null || options.escape) {
        return escapeFileContent(content);
    }
    return content;
}
exports.readFileSync = readFileSync;
function _emptyDir(path, parent, options) {
    return __awaiter(this, void 0, void 0, function* () {
        const entries = (yield _readAndFilterDir(path, options)).filter(ignoreExcludeFiles(options.exclude, parent));
        const results = [];
        yield bluebird_1.default.map(entries, (item) => {
            const fullPath = (0, path_1.join)(path, item.name);
            const currentPath = (0, path_1.join)(parent, item.name);
            if (item.isDirectory()) {
                return _emptyDir(fullPath, currentPath, options).then(files => {
                    if (!files.length) {
                        return fsPromises.rmdir(fullPath);
                    }
                    results.push(...files);
                });
            }
            results.push(currentPath);
            return fsPromises.unlink(fullPath);
        });
        return results;
    });
}
function emptyDir(path, options = {}) {
    if (!path)
        throw new TypeError('path is required!');
    return bluebird_1.default.resolve(_emptyDir(path, '', options));
}
exports.emptyDir = emptyDir;
function _emptyDirSync(path, options, parent) {
    const entries = _readAndFilterDirSync(path, options)
        .filter(ignoreExcludeFiles(options.exclude, parent));
    const results = [];
    for (const item of entries) {
        const childPath = (0, path_1.join)(path, item.name);
        const currentPath = (0, path_1.join)(parent, item.name);
        if (item.isDirectory()) {
            const removed = _emptyDirSync(childPath, options, currentPath);
            if (!graceful_fs_1.default.readdirSync(childPath).length) {
                rmdirSync(childPath);
            }
            results.push(...removed);
        }
        else {
            graceful_fs_1.default.unlinkSync(childPath);
            results.push(currentPath);
        }
    }
    return results;
}
function emptyDirSync(path, options = {}) {
    if (!path)
        throw new TypeError('path is required!');
    return _emptyDirSync(path, options, '');
}
exports.emptyDirSync = emptyDirSync;
function _rmdir(path) {
    return __awaiter(this, void 0, void 0, function* () {
        const files = fsPromises.readdir(path, { withFileTypes: true });
        yield bluebird_1.default.map(files, (item) => {
            const childPath = (0, path_1.join)(path, item.name);
            return item.isDirectory() ? _rmdir(childPath) : fsPromises.unlink(childPath);
        });
        return fsPromises.rmdir(path);
    });
}
function rmdir(path) {
    if (!path)
        throw new TypeError('path is required!');
    return bluebird_1.default.resolve(_rmdir(path));
}
exports.rmdir = rmdir;
function _rmdirSync(path) {
    const files = graceful_fs_1.default.readdirSync(path, { withFileTypes: true });
    for (let i = 0, len = files.length; i < len; i++) {
        const item = files[i];
        const childPath = (0, path_1.join)(path, item.name);
        if (item.isDirectory()) {
            _rmdirSync(childPath);
        }
        else {
            graceful_fs_1.default.unlinkSync(childPath);
        }
    }
    graceful_fs_1.default.rmdirSync(path);
}
function rmdirSync(path) {
    if (!path)
        throw new TypeError('path is required!');
    _rmdirSync(path);
}
exports.rmdirSync = rmdirSync;
function watch(path, options) {
    if (!path)
        throw new TypeError('path is required!');
    const watcher = chokidar_1.default.watch(path, options);
    return new bluebird_1.default((resolve, reject) => {
        watcher.on('ready', resolve);
        watcher.on('error', reject);
    }).thenReturn(watcher);
}
exports.watch = watch;
function _findUnusedPath(path, files) {
    const ext = (0, path_1.extname)(path);
    const base = (0, path_1.basename)(path, ext);
    const regex = new RegExp(`^${(0, hexo_util_1.escapeRegExp)(base)}(?:-(\\d+))?${(0, hexo_util_1.escapeRegExp)(ext)}$`);
    let num = -1;
    for (let i = 0, len = files.length; i < len; i++) {
        const item = files[i];
        const match = item.match(regex);
        if (match == null)
            continue;
        const matchNum = match[1] ? parseInt(match[1], 10) : 0;
        if (matchNum > num) {
            num = matchNum;
        }
    }
    return (0, path_1.join)((0, path_1.dirname)(path), `${base}-${num + 1}${ext}`);
}
function _ensurePath(path) {
    return __awaiter(this, void 0, void 0, function* () {
        if (!(yield exists(path)))
            return path;
        const files = yield fsPromises.readdir((0, path_1.dirname)(path));
        return _findUnusedPath(path, files);
    });
}
function ensurePath(path) {
    if (!path)
        throw new TypeError('path is required!');
    return bluebird_1.default.resolve(_ensurePath(path));
}
exports.ensurePath = ensurePath;
function ensurePathSync(path) {
    if (!path)
        throw new TypeError('path is required!');
    if (!graceful_fs_1.default.existsSync(path))
        return path;
    const files = graceful_fs_1.default.readdirSync((0, path_1.dirname)(path));
    return _findUnusedPath(path, files);
}
exports.ensurePathSync = ensurePathSync;
function ensureWriteStream(path, options) {
    if (!path)
        throw new TypeError('path is required!');
    return checkParent(path)
        .then(() => graceful_fs_1.default.createWriteStream(path, options));
}
exports.ensureWriteStream = ensureWriteStream;
function ensureWriteStreamSync(path, options) {
    if (!path)
        throw new TypeError('path is required!');
    graceful_fs_1.default.mkdirSync((0, path_1.dirname)(path), { recursive: true });
    return graceful_fs_1.default.createWriteStream(path, options);
}
exports.ensureWriteStreamSync = ensureWriteStreamSync;
// access
['F_OK', 'R_OK', 'W_OK', 'X_OK'].forEach(key => {
    Object.defineProperty(exports, key, {
        enumerable: true,
        value: graceful_fs_1.default.constants[key],
        writable: false
    });
});
exports.access = bluebird_1.default.promisify(graceful_fs_1.default.access);
exports.accessSync = graceful_fs_1.default.accessSync;
// chmod
exports.chmod = bluebird_1.default.promisify(graceful_fs_1.default.chmod);
exports.chmodSync = graceful_fs_1.default.chmodSync;
exports.fchmod = bluebird_1.default.promisify(graceful_fs_1.default.fchmod);
exports.fchmodSync = graceful_fs_1.default.fchmodSync;
exports.lchmod = bluebird_1.default.promisify(graceful_fs_1.default.lchmod);
exports.lchmodSync = graceful_fs_1.default.lchmodSync;
// chown
exports.chown = bluebird_1.default.promisify(graceful_fs_1.default.chown);
exports.chownSync = graceful_fs_1.default.chownSync;
exports.fchown = bluebird_1.default.promisify(graceful_fs_1.default.fchown);
exports.fchownSync = graceful_fs_1.default.fchownSync;
exports.lchown = bluebird_1.default.promisify(graceful_fs_1.default.lchown);
exports.lchownSync = graceful_fs_1.default.lchownSync;
// close
exports.close = bluebird_1.default.promisify(graceful_fs_1.default.close);
exports.closeSync = graceful_fs_1.default.closeSync;
// createStream
exports.createReadStream = graceful_fs_1.default.createReadStream;
exports.createWriteStream = graceful_fs_1.default.createWriteStream;
// fsync
exports.fsync = bluebird_1.default.promisify(graceful_fs_1.default.fsync);
exports.fsyncSync = graceful_fs_1.default.fsyncSync;
// link
exports.link = bluebird_1.default.promisify(graceful_fs_1.default.link);
exports.linkSync = graceful_fs_1.default.linkSync;
// mkdir
exports.mkdir = bluebird_1.default.promisify(graceful_fs_1.default.mkdir);
exports.mkdirSync = graceful_fs_1.default.mkdirSync;
// open
exports.open = bluebird_1.default.promisify(graceful_fs_1.default.open);
exports.openSync = graceful_fs_1.default.openSync;
// symlink
exports.symlink = bluebird_1.default.promisify(graceful_fs_1.default.symlink);
exports.symlinkSync = graceful_fs_1.default.symlinkSync;
// read
exports.read = bluebird_1.default.promisify(graceful_fs_1.default.read);
exports.readSync = graceful_fs_1.default.readSync;
// readdir
exports.readdir = bluebird_1.default.promisify(graceful_fs_1.default.readdir);
exports.readdirSync = graceful_fs_1.default.readdirSync;
// readlink
exports.readlink = bluebird_1.default.promisify(graceful_fs_1.default.readlink);
exports.readlinkSync = graceful_fs_1.default.readlinkSync;
// realpath
exports.realpath = bluebird_1.default.promisify(graceful_fs_1.default.realpath);
exports.realpathSync = graceful_fs_1.default.realpathSync;
// rename
exports.rename = bluebird_1.default.promisify(graceful_fs_1.default.rename);
exports.renameSync = graceful_fs_1.default.renameSync;
// stat
exports.stat = bluebird_1.default.promisify(graceful_fs_1.default.stat);
exports.statSync = graceful_fs_1.default.statSync;
exports.fstat = bluebird_1.default.promisify(graceful_fs_1.default.fstat);
exports.fstatSync = graceful_fs_1.default.fstatSync;
exports.lstat = bluebird_1.default.promisify(graceful_fs_1.default.lstat);
exports.lstatSync = graceful_fs_1.default.lstatSync;
// truncate
exports.truncate = bluebird_1.default.promisify(graceful_fs_1.default.truncate);
exports.truncateSync = graceful_fs_1.default.truncateSync;
exports.ftruncate = bluebird_1.default.promisify(graceful_fs_1.default.ftruncate);
exports.ftruncateSync = graceful_fs_1.default.ftruncateSync;
// unlink
exports.unlink = bluebird_1.default.promisify(graceful_fs_1.default.unlink);
exports.unlinkSync = graceful_fs_1.default.unlinkSync;
// utimes
exports.utimes = bluebird_1.default.promisify(graceful_fs_1.default.utimes);
exports.utimesSync = graceful_fs_1.default.utimesSync;
exports.futimes = bluebird_1.default.promisify(graceful_fs_1.default.futimes);
exports.futimesSync = graceful_fs_1.default.futimesSync;
// watch
exports.watchFile = graceful_fs_1.default.watchFile;
exports.unwatchFile = graceful_fs_1.default.unwatchFile;
// write
exports.write = bluebird_1.default.promisify(graceful_fs_1.default.write);
exports.writeSync = graceful_fs_1.default.writeSync;
// Static classes
exports.Stats = graceful_fs_1.default.Stats;
exports.ReadStream = graceful_fs_1.default.ReadStream;
exports.WriteStream = graceful_fs_1.default.WriteStream;
//# sourceMappingURL=fs.js.map