<template>
    <div>
        <ul>
            <li><router-link :to="{ path: '/admin'}">我的主页</router-link></li>
            <li><router-link :to="{ path: '/lottery-rounds'}">我要投注</router-link></li>
            <li><router-link :to="{ path: '/bids'}">我收投注</router-link></li>
        </ul>
    </div>
</template>

<script>
export default {

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