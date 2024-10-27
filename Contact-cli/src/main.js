import Vue from 'vue'
import App from './App'
import router from './router'
import ElementUI from 'element-ui'//引入核心js组件
import 'element-ui/lib/theme-chalk/index.css';
import axios from "axios";
import './assets/iconfont/iconfont.css';

//引入依赖的样式

Vue.use(ElementUI)
Vue.prototype.$tokenErrorNum = 0
Vue.config.productionTip = false
axios.defaults.baseURL = "http://localhost:8088"
Vue.prototype.$http = axios

/* eslint-disable no-new */
const _self = new Vue({
  el: '#app', router, components: {App}, template: '<App/>'
})
