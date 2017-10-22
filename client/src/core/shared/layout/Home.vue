<template>
  <common-card>
    <div slot="title">
      Início
    </div>
    <div slot="content">
      <q-icon name="home"></q-icon>
    </div>
  </common-card>
</template>

<script type="text/javascript">
  import { QIcon } from 'quasar-framework'
  import CommonCard from 'src/core/shared/common/Card.vue'

  export default {
    components: {
      CommonCard, QIcon
    },
    name: 'dashboard-home'
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
</style>
