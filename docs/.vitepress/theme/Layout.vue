<script setup>
import DefaultTheme from 'vitepress/theme'

const { Layout } = DefaultTheme
</script>

<template>
  <Layout>
    <template #home-features-before>
      <section class="sig">
        <div class="sig-card">
          <span class="sig-label">MODULES · PRODUCTION SCENES</span>
          <h3 class="sig-title">不是背 API，是解生产问题</h3>
          <div class="hu-grid">
            <div class="hu-tile"><b>HTTP 客户端</b><span>超时、重试与生产级配置</span></div>
            <div class="hu-tile"><b>缓存策略</b><span>FIFO / LRU / LFU / Timed 选型</span></div>
            <div class="hu-tile"><b>Cron 定时</b><span>表达式细节，对照 Spring 调度</span></div>
            <div class="hu-tile"><b>JWT 鉴权</b><span>签发、校验、过期完整链路</span></div>
            <div class="hu-tile"><b>AOP 代理</b><span>ProxyUtil 动态代理看切面本质</span></div>
            <div class="hu-tile"><b>Excel 读写</b><span>大数据量导入导出</span></div>
            <div class="hu-tile hu-hot"><b>树结构</b><span>TreeBuilder 一行组装部门树</span></div>
            <div class="hu-tile"><b>零外部依赖</b><span>mvn exec:java 直接跑</span></div>
          </div>
          <p class="sig-note">每个模块示例都对应一个真实生产场景——先有问题，再谈工具。跑起来看输出，比读十遍文档记得牢。</p>
        </div>
      </section>
    </template>
    <template #home-features-after>
      <div class="sig-more"><a href="https://ibqy.github.io/" target="_blank" rel="noopener">← ibqy.github.io · 探索全部项目</a></div>
    </template>
  </Layout>
</template>
