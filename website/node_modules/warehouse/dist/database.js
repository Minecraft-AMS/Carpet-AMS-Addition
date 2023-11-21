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
const jsonstream_1 = require("./lib/jsonstream");
const bluebird_1 = __importDefault(require("bluebird"));
const graceful_fs_1 = require("graceful-fs");
const stream_1 = require("stream");
const model_1 = __importDefault(require("./model"));
const schema_1 = __importDefault(require("./schema"));
const schematype_1 = __importDefault(require("./schematype"));
const error_1 = __importDefault(require("./error"));
const hexo_log_1 = require("hexo-log");
const log = (0, hexo_log_1.logger)();
const pkg = require('../package.json');
const { open } = graceful_fs_1.promises;
const pipelineAsync = bluebird_1.default.promisify(stream_1.pipeline);
let _writev;
if (typeof graceful_fs_1.writev === 'function') {
    _writev = (handle, buffers) => handle.writev(buffers);
}
else {
    _writev = (handle, buffers) => __awaiter(void 0, void 0, void 0, function* () {
        for (const buffer of buffers)
            yield handle.write(buffer);
    });
}
function exportAsync(database, path) {
    return __awaiter(this, void 0, void 0, function* () {
        const handle = yield open(path, 'w');
        try {
            // Start body & Meta & Start models
            yield handle.write(`{"meta":${JSON.stringify({
                version: database.options.version,
                warehouse: pkg.version
            })},"models":{`);
            const models = database._models;
            const keys = Object.keys(models);
            const { length } = keys;
            // models body
            for (let i = 0; i < length; i++) {
                const key = keys[i];
                if (!models[key])
                    continue;
                const buffers = [];
                if (i)
                    buffers.push(Buffer.from(',', 'ascii'));
                buffers.push(Buffer.from(`"${key}":`));
                buffers.push(Buffer.from(models[key]._export()));
                yield _writev(handle, buffers);
            }
            // End models
            yield handle.write('}}');
        }
        catch (e) {
            log.error(e);
            if (e instanceof RangeError && e.message.includes('Invalid string length')) {
                // NOTE:  Currently, we can't deal with anything about this issue.
                //        If do not `catch` the exception after the process will not work (e.g: `after_generate` filter.)
                //        A side-effect of this workaround is the `db.json` will not generate.
                log.warn('see: https://github.com/nodejs/node/issues/35973');
            }
            else {
                throw e;
            }
        }
        finally {
            yield handle.close();
        }
    });
}
class Database {
    /**
     * Database constructor.
     *
     * @param {object} [options]
     *   @param {number} [options.version=0] Database version
     *   @param {string} [options.path] Database path
     *   @param {function} [options.onUpgrade] Triggered when the database is upgraded
     *   @param {function} [options.onDowngrade] Triggered when the database is downgraded
     */
    constructor(options) {
        this.options = Object.assign({ version: 0, 
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            onUpgrade() { },
            // eslint-disable-next-line @typescript-eslint/no-empty-function
            onDowngrade() { } }, options);
        this._models = {};
        class _Model extends model_1.default {
        }
        this.Model = _Model;
        _Model.prototype._database = this;
    }
    /**
     * Creates a new model.
     *
     * @param {string} name
     * @param {Schema|object} [schema]
     * @return {Model}
     */
    model(name, schema) {
        if (this._models[name]) {
            return this._models[name];
        }
        this._models[name] = new this.Model(name, schema);
        const model = this._models[name];
        return model;
    }
    /**
     * Loads database.
     *
     * @param {function} [callback]
     * @return {Promise}
     */
    load(callback) {
        const { path, onUpgrade, onDowngrade, version: newVersion } = this.options;
        if (!path)
            throw new error_1.default('options.path is required');
        let oldVersion = 0;
        const getMetaCallBack = data => {
            if (data.meta && data.meta.version) {
                oldVersion = data.meta.version;
            }
        };
        // data event arg0 wrap key/value pair.
        const parseStream = (0, jsonstream_1.parse)('models.$*');
        parseStream.once('header', getMetaCallBack);
        parseStream.once('footer', getMetaCallBack);
        parseStream.on('data', data => {
            this.model(data.key)._import(data.value);
        });
        const rs = (0, graceful_fs_1.createReadStream)(path, 'utf8');
        return pipelineAsync(rs, parseStream).then(() => {
            if (newVersion > oldVersion) {
                return onUpgrade(oldVersion, newVersion);
            }
            else if (newVersion < oldVersion) {
                return onDowngrade(oldVersion, newVersion);
            }
        }).asCallback(callback);
    }
    /**
     * Saves database.
     *
     * @param {function} [callback]
     * @return {Promise}
     */
    save(callback) {
        const { path } = this.options;
        if (!path)
            throw new error_1.default('options.path is required');
        return bluebird_1.default.resolve(exportAsync(this, path)).asCallback(callback);
    }
    toJSON() {
        const models = Object.keys(this._models)
            .reduce((obj, key) => {
            const value = this._models[key];
            if (value != null)
                obj[key] = value;
            return obj;
        }, {});
        return {
            meta: {
                version: this.options.version,
                warehouse: pkg.version
            }, models
        };
    }
}
Database.Schema = schema_1.default;
Database.SchemaType = schematype_1.default;
Database.prototype.Schema = schema_1.default;
Database.prototype.SchemaType = schematype_1.default;
Database.version = pkg.version;
exports.default = Database;
//# sourceMappingURL=database.js.map