<template>
  <div class="sky">
    <div id="stars"></div>
    <div id="stars2"></div>
    <div id="stars3"></div

    <div class="card-wrapper">

      <q-card class="card-form">
        <q-card-media>
          <!--suppress HtmlUnknownTarget -->
          <img src="statics/logo/logo_fameta.jpg" alt="logo"/>
        </q-card-media>

        <q-card-separator></q-card-separator>

        <q-card-title>
          <p class="text-right"><i>{{ title }}</i></p>
        </q-card-title>

        <q-card-separator></q-card-separator>

        <q-card-main class="form">
          <field-text v-model="user" v-bind="{label: 'Login'}"></field-text>
          <field-text v-model="password" v-bind="{label: 'Senha', type: 'password'}"></field-text>
          <div class="field has-100">
            <hr class="light">
          </div>
          <div class="field has-100">
            <q-btn big class="full-width" color="primary" @click="attempt">Entrar</q-btn>
          </div>
          <div class="field has-100">
            <small>Apenas pressione Entrar</small>
          </div>
        </q-card-main>

      </q-card>

    </div>

  </div>
</template>

<script type="text/javascript">
  import { QCard, QCardMedia, QCardSeparator, QCardTitle, QCardMain, QBtn } from 'quasar-framework'
  import 'src/app/components/fields'
  import { login } from 'src/app/modules/auth/services'

  export default {
    name: 'auth-login',
    components: {
      QCard, QCardMedia, QCardSeparator, QCardTitle, QCardMain, QBtn
    },
    data: () => ({
      title: 'Painel de Controle',
      user: '',
      password: ''
    }),
    methods: {
      attempt () {
        const success = response => {
          this.$router.push({name: 'dashboard.home'})
        }
        const fail = error => {
          console.log('~> error', error)
        }
        login(this.user, this.password, success, fail)
      }
    }
  }
</script>

<style lang="stylus" rel="stylesheet/stylus">
  .sky
    .card-wrapper
      width 50vw
      height 50vh
      // overflow auto
    .card-form
      max-width 380px
      max
      margin 1em auto
      background #ffffff
      .q-card-media
        background #F7F7F7
    //     & > img
    //       width auto
    //       margin 20px auto
    //       max-height 100px
    //   .q-card-main
    //     small
    //       color #ababab
    //       font-style italic
    //   p
    //     font-family play
    //   .q-form-login
    //     max-width 500px
</style>

<style lang="sass">
  // http://codepen.io/saransh/pen/BKJun
  // n is number of stars required
  // http://codepen.io/saransh/pen/BKJun
  // n is number of stars required
  @function multiple-box-shadow ($n)
    $value: '#{random(2000)}px #{random(2000)}px #FFF'
    @for $i from 2 through $n
      $value: '#{$value} , #{random(2000)}px #{random(2000)}px #FFF'
    @return unquote($value)

  $shadows-small:  multiple-box-shadow(700)
  $shadows-medium: multiple-box-shadow(200)
  $shadows-big:    multiple-box-shadow(100)

  $shadows-small:  multiple-box-shadow(700)
  $shadows-medium: multiple-box-shadow(200)
  $shadows-big:    multiple-box-shadow(100)

  .sky
    background: radial-gradient(ellipse at bottom, #1b2735 0%, #090a0f 100%)
    height: 100%
    width: 100%
    position: absolute
    overflow: hidden

    #stars
      width: 1px
      height: 1px
      background: transparent
      box-shadow: $shadows-small
      animation: animationStars 25s linear infinite

      &:after
        content: " "
        position: absolute
        top: 2000px
        width: 1px
        height: 1px
        background: transparent
        box-shadow: $shadows-small

    #stars2
      width: 2px
      height: 2px
      background: transparent
      box-shadow: $shadows-medium
      animation: animationStars 50s linear infinite

      &:after
        content: " "
        position: absolute
        top: 2000px
        width: 2px
        height: 2px
        background: transparent
        box-shadow: $shadows-medium

    #stars3
      width: 3px
      height: 3px
      background: transparent
      box-shadow: $shadows-big
      animation: animationStars 75s linear infinite

      &:after
        content: " "
        position: absolute
        top: 2000px
        width: 3px
        height: 3px
        background: transparent
        box-shadow: $shadows-big

    #title
      color: #FFF
      text-align: right
      font-family: lato, sans-serif
      font-weight: 300
      font-size: 50px
      letter-spacing: 10px

      margin-top: 30%
      padding-left: 10px

      span
        /*text-transform: uppercase*/
        background: -webkit-linear-gradient(white, #38495a)
        -webkit-background-clip: text
        -webkit-text-fill-color: transparent

    @keyframes animationStars
      from
        transform: translateY(0px)
      to
        transform: translateY(-2000px)
</style>
