(() => {
  const normalize = value => value.trim().toLocaleLowerCase();
  const setupListSearch = () => {
    const input = document.querySelector('[data-list-search]');
    const results = document.querySelector('[data-list-search-results]');
    const empty = document.querySelector('[data-list-search-empty]');
    const errorMessage = document.querySelector('[data-list-search-error]');
    if (!input || !results || !empty || !errorMessage) return;
    const pagination = document.querySelector('[data-list-pagination]');
    const initialItems = [...results.children];
    let catalogPromise;
    let revision = 0;
    const loadCatalog = () => {
      if (!catalogPromise) {
        catalogPromise = fetch(input.dataset.searchIndex).then(response => {
          if (!response.ok) throw new Error(`Search index request failed: ${response.status}`);
          return response.json();
        }).then(catalog => {
          if (!Array.isArray(catalog) || catalog.some(post => typeof post.searchText !== 'string'
            || typeof post.title !== 'string' || typeof post.titleEn !== 'string'
            || typeof post.path !== 'string' || typeof post.date !== 'string')) {
            throw new Error('Search index has an invalid format');
          }
          return catalog;
        }).catch(error => {
          catalogPromise = null;
          throw error;
        });
      }
      return catalogPromise;
    };
    const renderPost = post => {
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
      title.textContent = document.documentElement.lang === 'en' ? post.titleEn : post.title;
      item.append(date, title);
      return item;
    };
    input.addEventListener('input', async () => {
      const request = ++revision;
      const query = normalize(input.value);
      errorMessage.hidden = true;
      empty.hidden = true;
      if (!query) {
        for (const item of initialItems) {
          const title = item.querySelector('.post-item-title');
          title.textContent = title.dataset[document.documentElement.lang === 'en' ? 'titleEn' : 'titleCn'];
        }
        results.replaceChildren(...initialItems);
        if (pagination) pagination.hidden = false;
        results.removeAttribute('aria-busy');
        return;
      }
      results.setAttribute('aria-busy', 'true');
      try {
        const catalog = await loadCatalog();
        if (request !== revision) return;
        const matches = catalog.filter(post => post.searchText.includes(query));
        results.replaceChildren(...matches.map(renderPost));
        empty.hidden = matches.length > 0;
        if (pagination) pagination.hidden = true;
      } catch (error) {
        if (request !== revision) return;
        errorMessage.hidden = false;
        console.error('Changelog search is unavailable.', error);
      } finally {
        if (request === revision) results.removeAttribute('aria-busy');
      }
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
      return { element: section, text: normalize(section.textContent) };
    });
    const empty = document.querySelector('[data-document-search-empty]');
    input.addEventListener('input', () => {
      const query = normalize(input.value);
      const matches = sections.filter((section) => {
        const matched = !query || section.text.includes(query);
        section.element.hidden = !matched;
        return matched;
      });
      empty.hidden = matches.length > 0;
      document.dispatchEvent(new Event('document-search-updated'));
    });
  };


  const setup = () => { setupListSearch(); setupDocumentSearch(); };
  document.addEventListener('site-page-loaded', setup);
  setup();
})();
