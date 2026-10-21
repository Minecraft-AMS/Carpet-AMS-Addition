(() => {
  const root = document.documentElement;
  let request = null;
  let renderedUrl = window.location.href;
  let scrollFrame = null;
  const saveScroll = () => {
    history.replaceState({ ...history.state, siteScroll: [window.scrollX, window.scrollY] }, '', renderedUrl);
  };
  history.scrollRestoration = 'manual';
  saveScroll();
  window.addEventListener('scroll', () => {
    if (request || scrollFrame !== null) return;
    scrollFrame = requestAnimationFrame(() => {
      scrollFrame = null;
      if (!request && window.location.href === renderedUrl) saveScroll();
    });
  }, { passive: true });

  const navigate = async (target, { pop = false, scroll } = {}) => {
    const url = new URL(target, window.location.href);
    request?.abort();
    const controller = new AbortController();
    request = controller;
    root.setAttribute('aria-busy', 'true');
    try {
      // 立即请求新页面；加载内容时保留画布，使动画继续播放。
      const response = await fetch(url.href, { signal: controller.signal });
      if (!response.ok || !response.headers.get('content-type')?.includes('text/html')) {
        throw new Error(`Page request failed: ${response.status}`);
      }
      const nextUrl = new URL(response.url || url.href);
      nextUrl.hash = url.hash;
      if (nextUrl.origin !== window.location.origin) throw new Error('Page redirected outside this site');
      const next = new DOMParser().parseFromString(await response.text(), 'text/html');
      if (controller.signal.aborted) return;
      const content = next.querySelector('.body-container');
      const current = document.querySelector('.body-container');
      const title = next.querySelector('title');
      // 包含独立脚本的页面使用普通跳转，确保脚本正常执行。
      if (!content || !current || !title || content.querySelector('script')) {
        throw new Error('Page does not use the shared site layout');
      }
      if (!pop) {
        saveScroll();
        history.pushState({ siteScroll: [0, 0] }, '', nextUrl.href);
      } else if (window.location.href !== nextUrl.href) {
        history.replaceState(history.state, '', nextUrl.href);
      }
      document.dispatchEvent(new Event('site-page-leaving'));
      root.dataset.pageLanguage = next.documentElement.dataset.pageLanguage || '';
      document.querySelector('title').replaceWith(title);
      current.replaceWith(content);
      renderedUrl = nextUrl.href;
      document.dispatchEvent(new Event('site-page-loaded'));
      const anchor = nextUrl.hash && document.getElementById(decodeURIComponent(nextUrl.hash.slice(1)));
      const top = scroll?.[1] ?? (anchor ? anchor.getBoundingClientRect().top + window.scrollY : 0);
      window.scrollTo({ left: scroll?.[0] || 0, top, behavior: 'instant' });
      const main = content.querySelector('main');
      main?.setAttribute('tabindex', '-1');
      main?.focus({ preventScroll: true });
    } catch (error) {
      if (controller.signal.aborted) return;
      // 局部加载失败或页面结构不兼容时，回退到浏览器的普通跳转。
      console.warn('Loading the page through normal navigation.', error);
      window.location.assign(url.href);
    } finally {
      if (request === controller) {
        request = null;
        root.removeAttribute('aria-busy');
      }
    }
  };

  const eligible = url => url.origin === window.location.origin
    && /^https?:$/.test(url.protocol)
    && !/\.[^/]+$/.test(url.pathname.replace(/\/index\.html$/, '/'));
  document.addEventListener('click', event => {
    if (event.defaultPrevented || event.button !== 0 || event.ctrlKey || event.metaKey || event.shiftKey || event.altKey) return;
    const link = event.target.closest('a[href]');
    if (!link || link.hasAttribute('download') || (link.target && link.target !== '_self') || link.rel?.split(/\s+/).includes('external')) return;
    const url = new URL(link.href, window.location.href);
    if (!eligible(url)) return;
    if (url.pathname === window.location.pathname && url.search === window.location.search) {
      if (url.hash || link.getAttribute('href')?.startsWith('#')) return;
      event.preventDefault();
      return;
    }
    event.preventDefault();
    void navigate(url.href);
  });
  document.addEventListener('site-navigation', event => {
    const url = new URL(event.detail.url, window.location.href);
    if (!eligible(url)) return;
    event.preventDefault();
    void navigate(url.href);
  });
  window.addEventListener('popstate', event => {
    const current = new URL(renderedUrl);
    const target = new URL(window.location.href);
    // 原生锚点跳转也会触发此事件；仅锚点变化时保留浏览器的平滑滚动。
    if (current.pathname === target.pathname && current.search === target.search) {
      renderedUrl = window.location.href;
      return;
    }
    void navigate(window.location.href, { pop: true, scroll: event.state?.siteScroll });
  });
})();
