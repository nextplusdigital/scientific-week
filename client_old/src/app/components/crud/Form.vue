<template>
  <div>
    <!--[2] {{ $parent.$options.name }}.{{ $options.name }}: {{ scope }}-->
    <common-form ref="form" :scope="scope" :schemas="schemas" :actions="actions" :stylish="stylish"
                 v-model="record" :editable="editable" :debug="debug" :tabs="tabs" :fixed="fixed"
                 @input="onchange"></common-form>
  </div>
</template>

<script type="text/javascript">
  import CommonForm from 'src/app/components/form/Form.vue'
  import Data from 'src/app/components/crud/components/form/data'
  import Hooks from 'src/app/components/crud/components/form/hooks'
  import Methods from 'src/app/components/crud/components/form/methods'
  import Props from 'src/app/components/crud/components/form/props'
  import Watch from 'src/app/components/crud/components/form/watch'

  export default {
    components: {
      CommonForm
    },
    mixins: [Data, Hooks, Methods, Props, Watch],
    name: 'crud-form'
  }
</script>
