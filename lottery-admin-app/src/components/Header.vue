<template>
    <div>
        <ul>
            <li><router-link :to="{ path: '/agents'}">投注站管理</router-link></li>
            <li><router-link :to="{ path: '/lotteries'}">彩票管理</router-link></li>
            <li><router-link :to="{ path: '/lottery-rounds'}">彩票实例管理</router-link></li>
            <li><router-link :to="{ path: '/tags'}">彩票标签管理</router-link></li>
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