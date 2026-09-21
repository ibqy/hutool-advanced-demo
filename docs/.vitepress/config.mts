import { defineConfig } from 'vitepress'

export default defineConfig({
  lang: 'zh-CN',
  title: 'Hutool 高级用法',
  description: '13 大模块生产场景示例：HTTP、缓存、Cron、JWT、AOP、Excel、树结构',
  base: '/hutool-advanced-demo/',
  lastUpdated: true,
  markdown: {
    config(md) {
      const defaultLink =
        md.renderer.rules.link_open ||
        ((tokens, idx, options, _env, self) => self.renderToken(tokens, idx, options))
      md.renderer.rules.link_open = (tokens, idx, options, env, self) => {
        const href = tokens[idx].attrGet('href')
        if (href && href.startsWith('../')) {
          const rel = href.replace(/^(\.\.\/)+/, '')
          const kind = /\.[A-Za-z]+$/.test(rel) ? 'blob' : 'tree'
          tokens[idx].attrSet('href', `https://github.com/ibqy/hutool-advanced-demo/${kind}/main/${rel}`)
        }
        return defaultLink(tokens, idx, options, env, self)
      }
    }
  },
  themeConfig: {
    nav: [
      { text: '首页', link: '/' },
      { text: 'GitHub', link: 'https://github.com/ibqy/hutool-advanced-demo' }
    ],
    sidebar: [
      {
        text: '教学文档',
        items: [
          { text: '01 · HTTP 客户端', link: '/01-http-client' },
          { text: '02 · 缓存策略', link: '/02-cache-strategy' },
          { text: '03 · Cron 定时', link: '/03-cron-deep-dive' },
          { text: '04 · JWT 鉴权', link: '/04-jwt-guide' },
          { text: '05 · AOP 代理', link: '/05-aop-proxy' },
          { text: '06 · Excel 操作', link: '/06-excel-ops' },
          { text: '07 · 树结构', link: '/07-tree-builder' }
        ]
      }
    ],
    socialLinks: [
      { icon: 'github', link: 'https://github.com/ibqy/hutool-advanced-demo' }
    ],
    search: { provider: 'local' },
    outline: { level: [2, 3], label: '本页目录' },
    docFooter: { prev: '上一篇', next: '下一篇' },
    lastUpdated: { text: '最后更新于' },
    darkModeSwitchLabel: '外观',
    lightModeSwitchTitle: '切换到浅色模式',
    darkModeSwitchTitle: '切换到深色模式',
    sidebarMenuLabel: '文档',
    returnToTopLabel: '回到顶部',
    footer: {
      message: '个人教学项目 · 代码可跑 · 注释记录设计取舍',
      copyright: 'Copyright © 2026 ibqy'
    }
  }
})
