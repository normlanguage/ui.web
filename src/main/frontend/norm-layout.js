class NormLayout extends HTMLElement {
  static get observedAttributes() {
    return ['data-norm-layout', 'data-norm-columns', 'data-norm-breakpoints', 'data-norm-column-width', 'data-norm-gap'];
  }

  connectedCallback() {
    this.resize = new ResizeObserver(() => this.schedule());
    this.childrenChanged = new MutationObserver(() => {
      this.observeChildren();
      this.schedule();
    });
    this.observation();
    this.schedule();
  }

  disconnectedCallback() {
    this.resize?.disconnect();
    this.childrenChanged?.disconnect();
    cancelAnimationFrame(this.frame);
    this.frame = null;
  }

  attributeChangedCallback() {
    this.observation();
    this.schedule();
  }

  observation() {
    if (!this.resize) return;
    this.resize.disconnect();
    this.childrenChanged.disconnect();
    if (!this.isConnected) return;
    const kind = this.getAttribute('data-norm-layout');
    if (kind !== 'responsive-grid' && kind !== 'masonry') return;
    this.childrenChanged.observe(this, { childList: true, subtree: true, attributes: true, attributeFilter: ['data-norm-span'] });
    this.observeChildren();
  }

  observeChildren() {
    this.resize.disconnect();
    this.resize.observe(this);
    for (const child of this.children) this.resize.observe(child);
  }

  schedule() {
    if (!this.isConnected || this.frame != null) return;
    const kind = this.getAttribute('data-norm-layout');
    if (kind !== 'responsive-grid' && kind !== 'masonry') return;
    this.frame = requestAnimationFrame(() => {
      this.frame = null;
      this.arrange();
    });
  }

  arrange() {
    const kind = this.getAttribute('data-norm-layout');
    if (kind === 'responsive-grid') {
      let columns = Number(this.getAttribute('data-norm-columns'));
      for (const [width, count] of JSON.parse(this.getAttribute('data-norm-breakpoints') || '[]')) {
        if (this.clientWidth >= width) columns = count;
      }
      this.style.setProperty('--norm-columns', columns);
      for (const child of this.children) {
        const span = Number(child.getAttribute('data-norm-span') || 1);
        child.style.gridColumn = `span ${Math.min(span, columns)}`;
      }
    }
    if (kind !== 'masonry') return;
    const minimum = Number(this.getAttribute('data-norm-column-width'));
    const gap = Number(this.getAttribute('data-norm-gap'));
    const count = Math.max(1, Math.floor((this.clientWidth + gap) / (minimum + gap)));
    const width = Math.max(0, (this.clientWidth - (count - 1) * gap) / count);
    const heights = Array(count).fill(0);
    for (const child of this.children) {
      const column = heights.indexOf(Math.min(...heights));
      child.style.position = 'absolute';
      child.style.width = `${width}px`;
      child.style.left = `${column * (width + gap)}px`;
      child.style.top = `${heights[column]}px`;
      heights[column] += child.getBoundingClientRect().height + gap;
    }
    this.style.height = `${Math.max(0, Math.max(...heights) - gap)}px`;
  }
}
customElements.define('norm-layout', NormLayout);
