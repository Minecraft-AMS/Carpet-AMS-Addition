'use strict';

const normalizePath = (value) => '/' + String(value || '').replace(/^\/+/, '').replace(/(?:^|\/)index\.html$/, '').replace(/\/+$/, '');

function normalizeConfig(raw = {}) {
  const boolean = (name, value, fallback) => {
    if (value == null) return fallback;
    if (typeof value !== 'boolean') throw new Error(`Theme configuration ${name} must be true or false`);
    return value;
  };
  const integer = (name, value, fallback, min, max) => {
    if (value == null) return fallback;
    if (!Number.isInteger(value) || value < min || value > max) {
      throw new Error(`Theme configuration ${name} must be an integer from ${min} to ${max}`);
    }
    return value;
  };
  const choice = (name, value, fallback, choices) => {
    if (value == null) return fallback;
    if (!choices.includes(value)) throw new Error(`Theme configuration ${name} must be one of: ${choices.join(', ')}`);
    return value;
  };
  const appearance = raw.appearance || {};
  const search = raw.search || {};
  const toc = raw.toc || {};
  const effects = raw.effects || {};
  if (raw.menu != null && !Array.isArray(raw.menu)) throw new Error('Theme configuration menu must be a list');
  const paths = new Set();
  const menu = (raw.menu || []).map((item, index) => {
    if (!item || typeof item.name_cn !== 'string' || !item.name_cn.trim()
      || typeof item.name_en !== 'string' || !item.name_en.trim()
      || typeof item.path !== 'string' || !item.path.startsWith('/')) {
      throw new Error(`Theme configuration menu[${index}] requires name_cn, name_en and an absolute site path`);
    }
    if (item.path_en != null && (typeof item.path_en !== 'string' || !item.path_en.startsWith('/'))) {
      throw new Error(`Theme configuration menu[${index}].path_en must start with /`);
    }
    const path = normalizePath(item.path);
    const pathEn = normalizePath(item.path_en || item.path);
    for (const alias of new Set([path, pathEn])) {
      if (paths.has(alias)) throw new Error(`Theme configuration contains duplicate menu path: ${alias}`);
      paths.add(alias);
    }
    return {
      name_cn: item.name_cn,
      name_en: item.name_en,
      title_cn: item.title_cn || item.name_cn,
      title_en: item.title_en || item.name_en,
      path,
      path_en: pathEn,
      document: boolean(`menu[${index}].document`, item.document, false),
      force_toc: boolean(`menu[${index}].force_toc`, item.force_toc, false),
    };
  });
  const settings = {
    favicon: raw.favicon || 'images/icon.png',
    menu,
    appearance: {
      default_theme: choice('appearance.default_theme', appearance.default_theme, 'system', ['system', 'light', 'dark']),
      default_language: choice('appearance.default_language', appearance.default_language, 'zh-CN', ['zh-CN', 'en']),
    },
    search: {
      posts: boolean('search.posts', search.posts, true),
      documents: boolean('search.documents', search.documents, true),
    },
    toc: {
      enable: boolean('toc.enable', toc.enable, true),
      min_headings: integer('toc.min_headings', toc.min_headings, 4, 1, 100),
      offset: integer('toc.offset', toc.offset, 150, 0, 1000),
    },
    effects: {
      background: boolean('effects.background', effects.background, false),
      background_particles: integer('effects.background_particles', effects.background_particles, 72, 0, 300),
      background_particles_mobile: integer('effects.background_particles_mobile', effects.background_particles_mobile, 24, 0, 300),
      background_fps: integer('effects.background_fps', effects.background_fps, 30, 1, 60),
      clicks: boolean('effects.clicks', effects.clicks, false),
      click_duration: integer('effects.click_duration', effects.click_duration, 560, 100, 2000),
      click_particles: integer('effects.click_particles', effects.click_particles, 8, 1, 32),
    },
    copyright: raw.copyright || null,
    license: raw.license || null,
    footer_links: raw.footer_links || [],

  };
  if (!Array.isArray(settings.footer_links) || settings.footer_links.some(link => !link?.name_cn || !link?.name_en || !link?.url)) {
    throw new Error('Theme configuration footer_links must be a list of name_cn, name_en and url');
  }
  return settings;
}

function pageView(settings, page = {}) {
  const path = normalizePath(page.path);
  const section = settings.menu.find(item => item.path === path || item.path_en === path);
  const pageLanguage = section && section.path !== section.path_en ? (path === section.path_en ? 'en' : 'zh-CN') : '';
  return {
    ...settings,
    section: section && path !== '/' ? section : null,
    isArticle: Boolean(page.content),
    language: pageLanguage || settings.appearance.default_language,
    pageLanguage,
    chineseTarget: section?.path || path,
    englishTarget: section?.path_en || path,
    searchable: Boolean(section?.document && settings.search.documents),
    showToc: Boolean(section?.document && settings.toc.enable),
    forceToc: Boolean(section?.force_toc),
  };
}

module.exports = { normalizeConfig, normalizePath, pageView };
