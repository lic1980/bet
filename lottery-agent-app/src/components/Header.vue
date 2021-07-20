<template>
    <div>
       <ul v-show="currePath != '/'">
            <li><router-link :to="{ path: '/agent'}">投注站管理</router-link></li>
            <li><router-link :to="{ path: '/bid'}">投注管理</router-link></li>
            <li><router-link :to="{ path: '/customers'}">充值管理</router-link></li>
        </ul>
    </div>
</template>

<script>
export default {
    data () {
        return {
            currPath: "/"
        }
    },
    mounted () {
        this.currPath = this.$route.path
    }
}
</script>

<style>
ul {
    list-style: none;
    line-height: 30px;
    margin-left: 5px;
}
li {
    display: block;
    float: left;
    margin-left: 5px;
}
</style>