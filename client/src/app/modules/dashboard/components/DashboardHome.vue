<template>
  <app-card class="dashboard-home">
    <div slot="title">
      <q-icon name="home"></q-icon>
      Home
    </div>
    <div slot="content">
      <h5>Bem vindo ao Sistema de vacinação</h5>
    </div>
  </app-card>
</template>

<script type="text/javascript">
  import AppCard from 'genesis/components/card/AppCard.vue'

  export default {
    components: {
      AppCard
    },
    name: 'dashboard-home'
  }
</script>

<style lang="stylus" rel="stylesheet/stylus" scoped>
  .dashboard-home
    font-size 14px
    .jumbotron
      padding 10px
      border-radius 3px
    small
      font-style italic
</style>
