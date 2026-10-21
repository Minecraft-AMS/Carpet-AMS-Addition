// 在加载样式和首次绘制前应用保存的主题，直接打开页面时也同样处理。
(() => {
  const root = document.documentElement;
  const storageKey = 'carpet-ams-preferences';
  const readPreferences = () => {
    try {
      const value = JSON.parse(localStorage.getItem(storageKey) || '{}');
      return value && typeof value === 'object' && !Array.isArray(value) ? value : {};
    } catch (error) {
      console.warn('Site preferences could not be read; using defaults.', error);
      return {};
    }
  };
  const stored = readPreferences();
  window.sitePreferences = stored;
  const themePreference = ['light', 'dark'].includes(stored.theme) ? stored.theme : root.dataset.defaultTheme;
  const initialTheme = ['light', 'dark'].includes(themePreference) ? themePreference
    : (window.matchMedia('(prefers-color-scheme: light)').matches ? 'light' : 'dark');
  root.dataset.theme = initialTheme;
})();
