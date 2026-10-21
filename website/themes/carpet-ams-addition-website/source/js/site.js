(() => {
  const root = document.documentElement;
  const storageKey = 'carpet-ams-preferences';
  const stored = window.sitePreferences;
  const savePreferences = () => {
    try {
      localStorage.setItem(storageKey, JSON.stringify(stored));
    } catch (error) {
      console.warn('Site preferences could not be saved for the next visit.', error);
    }
  };

  const applyTheme = (theme) => {
    root.dataset.theme = theme;
    document.querySelectorAll('[data-set-theme]').forEach((button) => {
      const active = button.dataset.setTheme === theme;
      button.classList.toggle('is-active', active);
      button.setAttribute('aria-pressed', String(active));
    });
  };

  const applyLanguage = (language) => {
    root.lang = language;
    document.querySelectorAll('[data-i18n-cn]').forEach((element) => {
      element.textContent = element.dataset[language === 'en' ? 'i18nEn' : 'i18nCn'];
    });
    document.querySelectorAll('[data-i18n-placeholder-cn]').forEach((element) => {
      element.placeholder = element.dataset[language === 'en' ? 'i18nPlaceholderEn' : 'i18nPlaceholderCn'];
    });
    document.querySelectorAll('[data-title-cn]').forEach((element) => {
      element.textContent = element.dataset[language === 'en' ? 'titleEn' : 'titleCn'];
    });
    document.querySelectorAll('[data-url-cn]').forEach((element) => {
      element.href = element.dataset[language === 'en' ? 'urlEn' : 'urlCn'];
    });
    document.querySelectorAll('[data-set-language]').forEach((button) => {
      const active = button.dataset.setLanguage === language;
      button.classList.toggle('is-active', active);
      button.setAttribute('aria-pressed', String(active));
    });
  };

  const setup = () => {
    const initialLanguage = root.dataset.pageLanguage || (['zh-CN', 'en'].includes(stored.language)
      ? stored.language : root.dataset.defaultLanguage || 'zh-CN');
    applyTheme(root.dataset.theme);
    applyLanguage(initialLanguage);
  };
  document.addEventListener('site-page-loaded', setup);
  setup();

  // 使用事件委托处理新页面中的控件，避免切页后重复注册监听器。
  document.addEventListener('click', (event) => {
    const themeButton = event.target.closest('[data-set-theme]');
    if (themeButton) {
      stored.theme = themeButton.dataset.setTheme;
      savePreferences();
      applyTheme(stored.theme);
      return;
    }
    const button = event.target.closest('[data-set-language]');
    if (!button) return;
    stored.language = button.dataset.setLanguage;
    savePreferences();
    const currentPath = window.location.pathname.replace(/\/$/, '') || '/';
    const targetPath = (button.dataset.languageTarget || '').replace(/\/$/, '') || '/';
    if (targetPath !== currentPath) {
      const navigation = new CustomEvent('site-navigation', {
        cancelable: true,
        detail: { url: button.dataset.languageTarget },
      });
      if (document.dispatchEvent(navigation)) window.location.assign(button.dataset.languageTarget);
      return;
    }
    applyLanguage(stored.language);
  });
})();
