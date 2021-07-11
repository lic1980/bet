import Vue from 'vue'
import ElementUI from 'element-ui';
import VueRouter from 'vue-router'
import 'element-ui/lib/theme-chalk/index.css';
import App from './App.vue'
import routers from './router/index.js'
import axios from 'axios'

Vue.config.productionTip = false

Vue.use(ElementUI);
Vue.use(VueRouter)

// enable session in backend
axios.defaults.withCredentials = true
// set backend address from env
const BASE_URL = document.querySelector('body').getAttribute('backend-host')
Vue.prototype.BASE_URL = (BASE_URL ==null? "localhost:8080":BASE_URL)

const router = new VueRouter({
  mode: 'history',
  routes: routers
 })

new Vue({
  router,
  render: h => h(App),
}).$mount('#app')
