<template>
  <q-layout ref="layout" :class="['layout-default', environment]" :view="view">

    <q-toolbar slot="header" class="">
      <slot name="header">
        <!--suppress JSUnresolvedVariable -->
        <q-btn flat @click="$refs.layout.toggleLeft()">
          <q-icon name="menu"></q-icon>
        </q-btn>

        <q-toolbar-title>
          {{ AppName }}
          <div slot="subtitle">{{ AppTitle }}</div>
        </q-toolbar-title>

        <q-btn flat>
          <q-icon name="more_vert"></q-icon>
          <q-popover ref="popover" class="q-popover-menu">
            <q-list item-separator highlight link>
              <!--suppress CommaExpressionJS -->
              <q-item v-for="(menu, index) in menus" :key="index" @click="handlerMenu(menu)" separator>
                <q-item-side :icon="menu.icon"></q-item-side>
                <q-item-main>{{ menu.label }}</q-item-main>
              </q-item>
            </q-list>
          </q-popover>
        </q-btn>
      </slot>
    </q-toolbar>

    <q-scroll-area slot="left" style="width: 100%; height: 100%">
      <div class="q-drawer-logo">
        <!--suppress HtmlUnknownTarget -->
        <img src="statics/logo/big.jpeg" alt="logo" style="width: 200px">
      </div>
      <slot name="left">
        <!--<q-list-header>Left Panel</q-list-header>-->
        <q-side-link v-for="menu in AppMenu" :key="menu" :to="menu.to" :exact="menu.exact" item>
          <q-item-side v-if="menu.left" v-bind="menu.left"></q-item-side>
          <q-item-main :label="menu.label" :sublabel="menu.sublabel"></q-item-main>
          <q-item-side v-if="menu.right" v-bind="menu.right" right></q-item-side>
        </q-side-link>
      </slot>
    </q-scroll-area>

    <slot name="content">
      <router-view :key="$route"></router-view>
    </slot>
  </q-layout>
</template>

<script type="text/javascript">
  import { mapGetters } from 'vuex'

  export default {
    name: 'layout-default',
    data: () => ({
      menus: [
        {
          icon: 'person',
          label: 'Minha Conta'
        },
        {
          icon: 'power_settings_new',
          label: 'Sair'
        }
      ]
    }),
    props: {
      view: {
        default: 'lHh Lpr fff'
      }
    },
    computed: {
      ...mapGetters(['AppTitle', 'AppName', 'AppMenu']),
      environment () {
        // noinspection ES6ModulesDependencies
        return process.env.NODE_ENV
      }
    },
    methods: {
      handlerMenu (menu) {
        // noinspection JSUnresolvedVariable
        this.$refs.popover.close()
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .layout-default
    .q-drawer-logo
      background #F7F7F7
      text-align center
      padding 20px
      border-bottom 1px #ddd solid
    .q-toolbar-title
      font-family play
</style>
