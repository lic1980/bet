<template>
    <div>
    </div>
</template>

<script>
import axios from 'axios'
import '../common/const.js'

export default {
    data () {
        return {
            customer: {},
        }
    },
    methods : {
    },
    mounted () {
        let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
        axios
            .get('http://localhost:8080/api/v1/customers/' + cusId)
            .then(response => (this.customer = response.data))
            .catch(function (error) { 
                console.log(error);
            });
        this.reloadBids("initiator");
    },
}
</script>


<style>

</style>