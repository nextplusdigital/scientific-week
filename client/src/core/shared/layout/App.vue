<template>
  <layout-default></layout-default>
</template>

<script type="text/javascript">
  import { mapActions } from 'vuex'
  import LayoutDefault from './Default.vue'
  import { menu } from 'src/core/helpers/support'

  export default {
    name: 'dashboard',
    components: {
      LayoutDefault
    },
    methods: {
      /**
       * @method changeTitle ({String})
       * @method changeMenu ({Array})
       */
      ...mapActions(['changeTitle', 'changeMenu'])
    },
    created () {
      this.changeTitle('Painel de Controle')
      this.changeMenu(menu)
    }
  }
</script>

<style lang="stylus">
</style>
