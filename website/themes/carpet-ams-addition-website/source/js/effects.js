(() => {
  const background = document.querySelector('[data-background-effects]');
  const clicks = document.querySelector('[data-click-effects]');
  const backgroundContext = background?.getContext('2d');
  const clickContext = clicks?.getContext('2d');
  if (!backgroundContext && !clickContext) return;

  const root = document.documentElement;
  const smallScreen = window.matchMedia('(max-width: 760px)');

  const maxBursts = 8;
  const maxBackgroundParticles = 300;
  const particleCount = (value, fallback) => {
    if (value == null || value.trim() === '') return fallback;
    const count = Number(value);
    return Number.isFinite(count) && count >= 0 ? Math.min(Math.floor(count), maxBackgroundParticles) : fallback;
  };
  const desktopParticles = particleCount(background?.dataset.particleCount, 72);
  const mobileParticles = particleCount(background?.dataset.mobileParticleCount, 24);
  const backgroundInterval = 1000 / Math.max(1, particleCount(background?.dataset.fps, 30));
  const burstDuration = Number(clicks?.dataset.duration) || 560;
  const fragmentsPerClick = particleCount(clicks?.dataset.particleCount, 8);
  let width = 0;
  let height = 0;
  let pixelRatio = 0;
  let particles = [];
  let bursts = [];
  let colors = [];
  let frame = null;
  let lastBackgroundFrame = null;
  let pageActive = true;
  let pointerStart = null;

  const readColors = () => {
    const style = getComputedStyle(root);
    colors = ['--accent', '--accent-strong'].map((name) => style.getPropertyValue(name).trim());
  };

  const resize = () => {
    const surface = background || clicks;
    const nextWidth = surface.clientWidth || window.innerWidth;
    const nextHeight = surface.clientHeight || window.innerHeight;
    const scale = Math.min(window.devicePixelRatio || 1, 2);
    const count = backgroundContext ? (smallScreen.matches ? mobileParticles : desktopParticles) : 0;
    if (width === nextWidth && height === nextHeight && pixelRatio === scale && particles.length === count) return;
    // 地址栏收起/展开时不重建画布；真正改变尺寸时保留粒子的运动状态。
    for (const particle of particles) {
      particle.x = particle.x / width * nextWidth;
      particle.y = particle.y / height * nextHeight;
    }
    width = nextWidth;
    height = nextHeight;
    pixelRatio = scale;
    for (const [canvas, context] of [[background, backgroundContext], [clicks, clickContext]]) {
      if (!context) continue;
      canvas.width = Math.round(width * scale);
      canvas.height = Math.round(height * scale);
      context.setTransform(scale, 0, 0, scale, 0, 0);
    }
    particles.length = Math.min(particles.length, count);
    while (particles.length < count) particles.push({
      x: Math.random() * width,
      y: Math.random() * height,
      size: 2 + Math.random() * 4,
      speed: 4 + Math.random() * 9,
      phase: Math.random() * Math.PI * 2,
      age: Math.random() * 10,
      outlined: Math.random() > 0.7,
    });
  };

  const drawBackground = (elapsed) => {
    if (!backgroundContext) return;
    const context = backgroundContext;
    context.clearRect(0, 0, width, height);
    context.fillStyle = colors[0];
    context.strokeStyle = colors[0];
    context.lineWidth = 1;
    for (const particle of particles) {
      particle.age += elapsed;
      particle.y -= particle.speed * elapsed;
      particle.x += Math.sin(particle.age * 0.4 + particle.phase) * elapsed * 3;
      if (particle.y < -12) particle.y = height + 12;
      if (particle.x < -12) particle.x = width + 12;
      if (particle.x > width + 12) particle.x = -12;
      context.globalAlpha = 0.18 + (Math.sin(particle.age + particle.phase) + 1) * 0.1;
      if (particle.outlined) {
        context.strokeRect(particle.x, particle.y, particle.size + 3, particle.size + 3);
      } else {
        context.fillRect(particle.x, particle.y, particle.size, particle.size);
      }
    }
    context.globalAlpha = 1;
  };

  const drawClicks = (now) => {
    if (!clickContext || !bursts.length) return;
    const context = clickContext;
    context.clearRect(0, 0, width, height);
    bursts = bursts.filter((burst) => now - burst.started < burstDuration);
    for (const burst of bursts) {
      const progress = Math.max(0, (now - burst.started) / burstDuration);
      const eased = 1 - Math.pow(1 - progress, 3);
      context.strokeStyle = colors[0];
      context.lineWidth = 1;
      context.globalAlpha = (1 - progress) * 0.35;
      context.beginPath();
      context.arc(burst.x, burst.y, 3 + eased * 23, 0, Math.PI * 2);
      context.stroke();
      for (const fragment of burst.fragments) {
        const distance = eased * fragment.distance;
        const x = burst.x + Math.cos(fragment.angle) * distance;
        const y = burst.y + Math.sin(fragment.angle) * distance + progress * progress * 14;
        const size = fragment.size * (1 - progress * 0.65);
        context.globalAlpha = Math.pow(1 - progress, 1.3) * 0.85;
        context.fillStyle = colors[fragment.color];
        context.fillRect(x - size / 2, y - size / 2, size, size);
      }
    }
    context.globalAlpha = 1;
  };

  const animate = (now) => {
    if (lastBackgroundFrame === null || now - lastBackgroundFrame >= backgroundInterval) {
      const elapsed = lastBackgroundFrame === null ? 0 : Math.min((now - lastBackgroundFrame) / 1000, 0.1);
      drawBackground(elapsed);
      lastBackgroundFrame = now;
    }
    drawClicks(now);
    frame = particles.length || bursts.length ? requestAnimationFrame(animate) : null;
  };

  const start = () => {
    if (frame === null && pageActive && !document.hidden
      && (particles.length || bursts.length)) {
      frame = requestAnimationFrame(animate);
    }
  };

  const stop = () => {
    cancelAnimationFrame(frame);
    frame = null;
    lastBackgroundFrame = null;
    bursts = [];
    pointerStart = null;
    clickContext?.clearRect(0, 0, width, height);
  };

  const syncMotion = () => {
    stop();
    start();
  };

  if (clickContext) {
    window.addEventListener('pointerdown', (event) => {
      pointerStart = event.pointerType === 'mouse' && event.button === 0 && event.isPrimary
        ? { id: event.pointerId, x: event.clientX, y: event.clientY } : null;
    }, { passive: true, capture: true });
    window.addEventListener('pointercancel', () => { pointerStart = null; }, { passive: true, capture: true });
    window.addEventListener('pointerup', (event) => {
      const origin = pointerStart;
      pointerStart = null;
      if (!origin || origin.id !== event.pointerId || event.button !== 0
        || !pageActive || document.hidden || event.defaultPrevented) return;
      if (Math.hypot(event.clientX - origin.x, event.clientY - origin.y) > 6) return;
      bursts.push({
        x: event.clientX,
        y: event.clientY,
        started: performance.now(),
        fragments: Array.from({ length: fragmentsPerClick }, (_, index) => ({
          angle: index * Math.PI * 2 / fragmentsPerClick + (Math.random() - 0.5) * 0.4,
          distance: 22 + Math.random() * 28,
          size: 2 + Math.random() * 3,
          color: index % 2,
        })),
      });
      if (bursts.length > maxBursts) bursts.shift();
      start();
    }, { passive: true, capture: true });
  }

  window.addEventListener('resize', () => { resize(); start(); }, { passive: true });
  window.addEventListener('pagehide', () => { pageActive = false; stop(); });
  window.addEventListener('pageshow', () => { pageActive = true; start(); });
  document.addEventListener('visibilitychange', syncMotion);
  new MutationObserver(readColors).observe(root, { attributes: true, attributeFilter: ['data-theme'] });

  readColors();
  resize();
  start();
})();
