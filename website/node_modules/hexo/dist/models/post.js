"use strict";
var __importDefault = (this && this.__importDefault) || function (mod) {
    return (mod && mod.__esModule) ? mod : { "default": mod };
};
const warehouse_1 = __importDefault(require("warehouse"));
const moment_1 = __importDefault(require("moment"));
const path_1 = require("path");
const bluebird_1 = __importDefault(require("bluebird"));
const moment_2 = __importDefault(require("./types/moment"));
const hexo_util_1 = require("hexo-util");
function pickID(data) {
    return data._id;
}
function removeEmptyTag(tags) {
    return tags.filter(tag => tag != null && tag !== '').map(tag => `${tag}`);
}
const tagsGetterCache = new hexo_util_1.Cache();
module.exports = (ctx) => {
    const Post = new warehouse_1.default.Schema({
        id: String,
        title: { type: String, default: '' },
        date: {
            type: moment_2.default,
            default: moment_1.default,
            language: ctx.config.languages,
            timezone: ctx.config.timezone
        },
        updated: {
            type: moment_2.default,
            language: ctx.config.languages,
            timezone: ctx.config.timezone
        },
        comments: { type: Boolean, default: true },
        layout: { type: String, default: 'post' },
        _content: { type: String, default: '' },
        source: { type: String, required: true },
        slug: { type: String, required: true },
        photos: [String],
        raw: { type: String, default: '' },
        published: { type: Boolean, default: true },
        content: { type: String },
        excerpt: { type: String },
        more: { type: String }
    });
    Post.virtual('path').get(function () {
        const path = ctx.execFilterSync('post_permalink', this, { context: ctx });
        return typeof path === 'string' ? path : '';
    });
    Post.virtual('permalink').get(function () {
        return hexo_util_1.full_url_for.call(ctx, this.path);
    });
    Post.virtual('full_source').get(function () {
        return (0, path_1.join)(ctx.source_dir, this.source || '');
    });
    Post.virtual('asset_dir').get(function () {
        const src = this.full_source;
        return src.substring(0, src.length - (0, path_1.extname)(src).length) + path_1.sep;
    });
    Post.virtual('tags').get(function () {
        return tagsGetterCache.apply(this._id, () => {
            const PostTag = ctx.model('PostTag');
            const Tag = ctx.model('Tag');
            const ids = PostTag.find({ post_id: this._id }, { lean: true }).map(item => item.tag_id);
            return Tag.find({ _id: { $in: ids } });
        });
    });
    Post.method('notPublished', function () {
        // The same condition as ctx._bindLocals
        return (!ctx.config.future && this.date > Date.now()) || (!ctx._showDrafts() && this.published === false);
    });
    Post.method('setTags', function (tags) {
        if (this.notPublished()) {
            // Ignore tags of draft posts
            // If the post is unpublished then the tag needs to be removed, thus the function cannot be returned early here
            tags = [];
        }
        tagsGetterCache.flush();
        tags = removeEmptyTag(tags);
        const PostTag = ctx.model('PostTag');
        const Tag = ctx.model('Tag');
        const id = this._id;
        const existed = PostTag.find({ post_id: id }, { lean: true }).map(pickID);
        return bluebird_1.default.map(tags, tag => {
            // Find the tag by name
            const data = Tag.findOne({ name: tag }, { lean: true });
            if (data)
                return data;
            // Insert the tag if not exist
            return Tag.insert({ name: tag }).catch(err => {
                // Try to find the tag again. Throw the error if not found
                const data = Tag.findOne({ name: tag }, { lean: true });
                if (data)
                    return data;
                throw err;
            });
        }).map(tag => {
            // Find the reference
            const ref = PostTag.findOne({ post_id: id, tag_id: tag._id }, { lean: true });
            if (ref)
                return ref;
            // Insert the reference if not exist
            return PostTag.insert({
                post_id: id,
                tag_id: tag._id
            });
        }).then(tags => {
            // Remove old tags
            const deleted = existed.filter(item => !tags.map(pickID).includes(item));
            return deleted;
        }).map(tag => PostTag.removeById(tag));
    });
    Post.virtual('categories').get(function () {
        const PostCategory = ctx.model('PostCategory');
        const Category = ctx.model('Category');
        const ids = PostCategory.find({ post_id: this._id }, { lean: true }).map(item => item.category_id);
        return Category.find({ _id: { $in: ids } });
    });
    Post.method('setCategories', function (cats) {
        if (this.notPublished()) {
            cats = [];
        }
        // Remove empty categories, preserving hierarchies
        cats = cats.filter(cat => {
            return Array.isArray(cat) || (cat != null && cat !== '');
        }).map(cat => {
            return Array.isArray(cat) ? removeEmptyTag(cat) : `${cat}`;
        });
        const PostCategory = ctx.model('PostCategory');
        const Category = ctx.model('Category');
        const id = this._id;
        const allIds = [];
        const existed = PostCategory.find({ post_id: id }, { lean: true }).map(pickID);
        const hasHierarchy = cats.filter(Array.isArray).length > 0;
        // Add a hierarchy of categories
        const addHierarchy = (catHierarchy) => {
            const parentIds = [];
            if (!Array.isArray(catHierarchy))
                catHierarchy = [catHierarchy];
            // Don't use "Promise.map". It doesn't run in series.
            // MUST USE "Promise.each".
            return bluebird_1.default.each(catHierarchy, (cat, i) => {
                // Find the category by name
                const data = Category.findOne({
                    name: cat,
                    parent: i ? parentIds[i - 1] : { $exists: false }
                }, { lean: true });
                if (data) {
                    allIds.push(data._id);
                    parentIds.push(data._id);
                    return data;
                }
                // Insert the category if not exist
                const obj = { name: cat };
                if (i)
                    obj.parent = parentIds[i - 1];
                return Category.insert(obj).catch(err => {
                    // Try to find the category again. Throw the error if not found
                    const data = Category.findOne({
                        name: cat,
                        parent: i ? parentIds[i - 1] : { $exists: false }
                    }, { lean: true });
                    if (data)
                        return data;
                    throw err;
                }).then(data => {
                    allIds.push(data._id);
                    parentIds.push(data._id);
                    return data;
                });
            });
        };
        return (hasHierarchy ? bluebird_1.default.each(cats, addHierarchy) : bluebird_1.default.resolve(addHierarchy(cats))).then(() => allIds).map(catId => {
            // Find the reference
            const ref = PostCategory.findOne({ post_id: id, category_id: catId }, { lean: true });
            if (ref)
                return ref;
            // Insert the reference if not exist
            return PostCategory.insert({
                post_id: id,
                category_id: catId
            });
        }).then(postCats => // Remove old categories
         existed.filter(item => !postCats.map(pickID).includes(item))).map(cat => PostCategory.removeById(cat));
    });
    // Remove PostTag references
    Post.pre('remove', data => {
        const PostTag = ctx.model('PostTag');
        return PostTag.remove({ post_id: data._id });
    });
    // Remove PostCategory references
    Post.pre('remove', data => {
        const PostCategory = ctx.model('PostCategory');
        return PostCategory.remove({ post_id: data._id });
    });
    // Remove assets
    Post.pre('remove', data => {
        const PostAsset = ctx.model('PostAsset');
        return PostAsset.remove({ post: data._id });
    });
    return Post;
};
//# sourceMappingURL=post.js.map