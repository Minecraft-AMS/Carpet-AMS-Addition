(() => {
  const root = document.documentElement;
  const storageKey = 'carpet-ams-preferences';
  const stored = JSON.parse(localStorage.getItem(storageKey) || '{}');

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

  const initialTheme = stored.theme || (window.matchMedia('(prefers-color-scheme: light)').matches ? 'light' : 'dark');
  const initialLanguage = stored.language || (window.location.pathname.startsWith('/en_us/') ? 'en' : 'zh-CN');
  applyTheme(initialTheme);
  applyLanguage(initialLanguage);

  document.querySelectorAll('[data-set-theme]').forEach((button) => button.addEventListener('click', () => {
    stored.theme = button.dataset.setTheme;
    localStorage.setItem(storageKey, JSON.stringify(stored));
    applyTheme(stored.theme);
  }));
  document.querySelectorAll('[data-set-language]').forEach((button) => button.addEventListener('click', () => {
    stored.language = button.dataset.setLanguage;
    localStorage.setItem(storageKey, JSON.stringify(stored));
    const currentPath = window.location.pathname.replace(/\/$/, '') || '/';
    const targetPath = (button.dataset.languageTarget || '').replace(/\/$/, '') || '/';
    if (targetPath !== currentPath) {
      window.location.assign(button.dataset.languageTarget);
      return;
    }
    applyLanguage(stored.language);
  }));

  const normalize = (value) => value.trim().toLocaleLowerCase();
  const setupDocumentMetadata = () => {
    const content = document.querySelector('.post-content');
    if (!content) return;
    content.querySelectorAll('h2, h3').forEach((heading) => {
      const note = heading.nextElementSibling;
      if (note?.tagName !== 'BLOCKQUOTE') return;
      const version = note.textContent.trim().match(/^(?:版本：|Version:)\s*(Minecraft .+)$/)?.[1];
      if (!version) return;
      const badge = document.createElement('span');
      badge.className = 'document-version-badge';
      badge.textContent = version;
      const title = document.createElement('span');
      title.className = 'document-heading-title';
      [...heading.childNodes].filter((node) => node.nodeType === Node.TEXT_NODE).forEach((node) => title.append(node));
      heading.classList.add('document-heading');
      heading.append(title, badge);
      note.remove();
    });
    content.querySelectorAll('blockquote').forEach((note) => {
      const paragraph = note.querySelector(':scope > p:only-child');
      if (!paragraph || !/^(?:移植自：|Ported from:)/.test(paragraph.textContent.trim())) return;
      paragraph.classList.add('document-source');
      note.replaceWith(paragraph);
    });
  };
  const setupListSearch = () => {
    const input = document.querySelector('[data-list-search]');
    if (!input) return;
    const results = document.querySelector('[data-list-search-results]');
    const empty = document.querySelector('[data-list-search-empty]');
    const pagination = document.querySelector('[data-list-pagination]');
    const catalogElement = document.querySelector('#post-search-catalog');
    const catalog = catalogElement ? JSON.parse(catalogElement.textContent || '[]') : [];
    const initialMarkup = results.innerHTML;
    const renderPost = (post) => {
      const item = document.createElement('a');
      const title = document.createElement('span');
      const date = document.createElement('time');
      item.className = 'post-item';
      item.href = post.path;
      date.className = 'post-item-date';
      date.dateTime = post.date;
      date.textContent = post.date;
      title.className = 'post-item-title';
      title.dataset.titleCn = post.title;
      title.dataset.titleEn = post.titleEn;
      title.textContent = root.lang === 'en' ? post.titleEn : post.title;
      item.append(date, title);
      return item;
    };
    input.addEventListener('input', () => {
      const query = normalize(input.value);
      if (!query) {
        results.innerHTML = initialMarkup;
        empty.hidden = true;
        if (pagination) pagination.hidden = false;
        return;
      }
      const matches = catalog.filter((post) => normalize(post.searchText).includes(query));
      results.replaceChildren(...matches.map(renderPost));
      empty.hidden = matches.length > 0;
      if (pagination) pagination.hidden = true;
    });
  };

  const setupDocumentSearch = () => {
    const input = document.querySelector('[data-document-search]');
    const content = document.querySelector('[data-searchable-content]');
    if (!input || !content) return;
    const headings = [...content.querySelectorAll('h2, h3')];
    if (!headings.length) return;
    const level = Math.min(...headings.map((heading) => Number(heading.tagName.slice(1))));
    const sectionHeadings = headings.filter((heading) => Number(heading.tagName.slice(1)) === level);
    const sections = sectionHeadings.map((heading, index) => {
      const section = document.createElement('section');
      section.className = 'search-section';
      content.insertBefore(section, heading);
      const nextHeading = sectionHeadings[index + 1];
      for (let node = heading; node && node !== nextHeading;) {
        const nextNode = node.nextSibling;
        section.append(node);
        node = nextNode;
      }
      return section;
    });
    const empty = document.querySelector('[data-document-search-empty]');
    input.addEventListener('input', () => {
      const query = normalize(input.value);
      const matches = sections.filter((section) => {
        const matched = !query || normalize(section.textContent).includes(query);
        section.hidden = !matched;
        return matched;
      });
      empty.hidden = matches.length > 0;
      document.dispatchEvent(new Event('document-search-updated'));
    });
  };

  const setupPageToc = () => {
    const content = document.querySelector('.post-content');
    const toc = document.querySelector('[data-page-toc]');
    const links = document.querySelector('[data-page-toc-links]');
    const layout = document.querySelector('[data-article-layout]');
    if (!content || !toc || !links || !layout) return;

    const headings = [...content.querySelectorAll('h2, h3')];
    const forceToc = layout.hasAttribute('data-force-page-toc');
    const pageTocEnabled = layout.hasAttribute('data-page-toc-enabled');
    if (!pageTocEnabled) return;
    if (!forceToc && headings.length < 4) return;

    headings.forEach((heading, index) => {
      if (!heading.id) heading.id = `section-${index + 1}`;
      const link = document.createElement('a');
      link.href = `#${heading.id}`;
      link.textContent = heading.querySelector('.headerlink')?.getAttribute('title') || heading.textContent.trim();
      link.className = `toc-level-${heading.tagName.slice(1)}`;
      link.dataset.tocTarget = heading.id;
      links.append(link);
    });

    toc.hidden = false;
    layout.classList.add('has-page-toc');
    const tocLinks = [...links.querySelectorAll('a')];
    const visibleHeadings = () => headings.filter((heading) => !heading.closest('.search-section')?.hidden);
    const sync = () => {
      const available = visibleHeadings();
      tocLinks.forEach((link) => {
        const heading = document.getElementById(link.dataset.tocTarget);
        link.hidden = !available.includes(heading);
      });
      if (!available.length) return;
      let active = available[0];
      available.forEach((heading) => {
        if (heading.getBoundingClientRect().top <= 150) active = heading;
      });
      tocLinks.forEach((link) => link.classList.toggle('is-active', link.dataset.tocTarget === active.id));
    };
    let scheduled = false;
    const scheduleSync = () => {
      if (scheduled) return;
      scheduled = true;
      requestAnimationFrame(() => {
        scheduled = false;
        sync();
      });
    };
    window.addEventListener('scroll', scheduleSync, { passive: true });
    window.addEventListener('resize', scheduleSync);
    document.addEventListener('document-search-updated', scheduleSync);
    scheduleSync();
  };

  setupListSearch();
  setupDocumentMetadata();
  setupDocumentSearch();
  setupPageToc();

})();
