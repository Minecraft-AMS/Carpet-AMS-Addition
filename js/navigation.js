(() => {
  const root = document.documentElement;
  let request = null;
  let renderedUrl = window.location.href;
  let scrollTimer = null;
  let pageAnimation = null;
  const animatePage = async (element, entering, signal) => {
    if (!element?.animate || signal.aborted) return;
    const animation = element.animate(entering
      ? [{ opacity: 0, transform: 'translateY(10px)' }, { opacity: 1, transform: 'translateY(0)' }]
      : [{ opacity: 1, transform: 'translateY(0)' }, { opacity: 0, transform: 'translateY(-6px)' }], {
      duration: entering ? 240 : 110,
      easing: entering ? 'cubic-bezier(.2,.7,.2,1)' : 'ease-out',
      fill: 'both',
    });
    pageAnimation = animation;
    const cancel = () => animation.cancel();
    signal.addEventListener('abort', cancel, { once: true });
    try {
      await animation.finished;
    } catch {
      // 连续导航时取消上一次动画，让最新请求接管。
    } finally {
      signal.removeEventListener('abort', cancel);
      if (pageAnimation === animation) pageAnimation = null;
      animation.cancel();
    }
  };
  const saveScroll = () => {
    clearTimeout(scrollTimer);
    scrollTimer = null;
    if (window.location.href !== renderedUrl) return;
    history.replaceState({ ...history.state, siteScroll: [window.scrollX, window.scrollY] }, '', renderedUrl);
  };
  history.scrollRestoration = 'manual';
  saveScroll();
  window.addEventListener('scroll', () => {
    clearTimeout(scrollTimer);
    if (request) return;
    // 平滑滚动期间不逐帧写 history，避免触发浏览器导航限流。
    scrollTimer = setTimeout(saveScroll, 180);
  }, { passive: true });
  window.addEventListener('pagehide', saveScroll);

  const navigate = async (target, { pop = false, scroll } = {}) => {
    const url = new URL(target, window.location.href);
    clearTimeout(scrollTimer);
    request?.abort();
    pageAnimation?.cancel();
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
      await animatePage(current.querySelector('.main-wrapper'), false, controller.signal);
      if (controller.signal.aborted) return;
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
      await animatePage(content.querySelector('.main-wrapper'), true, controller.signal);
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
      if (url.hash || link.getAttribute('href')?.startsWith('#')) {
        saveScroll();
        return;
      }
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
