(() => {
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

  let dispose = () => {};
  const setup = () => {
    const content = document.querySelector('.post-content');
    const toc = document.querySelector('[data-page-toc]');
    const links = document.querySelector('[data-page-toc-links]');
    const layout = document.querySelector('[data-article-layout]');
    setupDocumentMetadata();
    if (!content || !toc || !links || !layout?.hasAttribute('data-page-toc-enabled')) return;
    const headings = [...content.querySelectorAll('h2, h3')];
    const minimum = Number(layout.dataset.tocMinHeadings) || 4;
    const offset = Number(layout.dataset.tocOffset ?? 150);
    if (!headings.length || (!layout.hasAttribute('data-force-page-toc') && headings.length < minimum)) return;

    const entries = headings.map((heading, index) => {
      if (!heading.id) heading.id = `section-${index + 1}`;
      const link = document.createElement('a');
      link.href = `#${heading.id}`;
      link.textContent = heading.querySelector('.headerlink')?.getAttribute('title') || heading.textContent.trim();
      link.className = `toc-level-${heading.tagName.slice(1)}`;
      links.append(link);
      return { heading, link };
    });
    let visibleEntries = [];
    let active = null;
    let scheduled = null;
    const sync = () => {
      if (!visibleEntries.length) return;
      // 标题按文档顺序排列，通过二分查找定位当前标题，避免逐一测量位置。
      let low = 0;
      let high = visibleEntries.length - 1;
      let index = 0;
      while (low <= high) {
        const middle = Math.floor((low + high) / 2);
        if (visibleEntries[middle].heading.getBoundingClientRect().top <= offset) {
          index = middle;
          low = middle + 1;
        } else {
          high = middle - 1;
        }
      }
      const next = visibleEntries[index].link;
      if (active === next) return;
      active?.classList.remove('is-active');
      active?.removeAttribute('aria-current');
      active = next;
      active.classList.add('is-active');
      active.setAttribute('aria-current', 'location');
    };
    const schedule = () => {
      if (scheduled !== null) return;
      scheduled = requestAnimationFrame(() => { scheduled = null; sync(); });
    };
    const updateVisibility = () => {
      visibleEntries = entries.filter(({ heading, link }) => {
        const visible = !heading.closest('.search-section')?.hidden;
        link.hidden = !visible;
        return visible;
      });
      toc.hidden = !visibleEntries.length;
      layout.classList.toggle('has-page-toc', visibleEntries.length > 0);
      schedule();
    };
    window.addEventListener('scroll', schedule, { passive: true });
    window.addEventListener('resize', schedule, { passive: true });
    document.addEventListener('document-search-updated', updateVisibility);
    dispose = () => {
      window.removeEventListener('scroll', schedule);
      window.removeEventListener('resize', schedule);
      document.removeEventListener('document-search-updated', updateVisibility);
      if (scheduled !== null) cancelAnimationFrame(scheduled);
    };
    updateVisibility();
  };
  document.addEventListener('site-page-leaving', () => { dispose(); dispose = () => {}; });
  document.addEventListener('site-page-loaded', setup);
  setup();
})();
