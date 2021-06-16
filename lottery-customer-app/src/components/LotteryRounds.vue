<template>
    <div>
        <div>
            <div>账户余额：{{ customer.deposit }}</div>
        </div>
        <div>
            <div style="line-height: 36px;">
                <div >
                    彩票名称：{{ lottery.name }}
                </div>
                <div style="overflow:hidden" v-for="(lotteryRound, itemIndex) in lotteryRounds" v-bind:key="itemIndex">
                    <div></div>
                    <div style="float:left;">
                        <a :href="'/lottery-round?lotteryRoundId=' + lotteryRound.id ">
                        {{ lotteryRound.title }}
                        </a>
                        
                        <span style="margin-left:10px;" v-for="(lotteryRoundOption, itemIndex) in lotteryRoundOptionsByLotteryRoundId.get(lotteryRound.id)" v-bind:key="itemIndex">
                             <template v-if="itemIndex < 3">
                                    {{lotteryRoundOption.optionText}} {{lotteryRoundOption.odds}}
                             </template>
                        </span>
                    </div>
                </div>

            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    data () {
        return {
            customer: { "id": "", "deposit": 0 },
            lotteryRounds: [],
            lotteryRoundOptionsByLotteryRoundId: new Map(),
            lottery:{},
        }
    },
    methods: {
        
    },
    mounted () {
        let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
        let lotteryId = this.$route.query.lotteryId;
        axios
            .get('http://localhost:8080/api/v1/customers/' + cusId)
            .then(response => (this.customer = response.data))
            .catch(function (error) { 
                console.log(error);
            });
        if (lotteryId != null){
            axios
                .get('http://localhost:8080/api/v1/lotteries/' + lotteryId + '/rounds')
                .then(
                    response => {
                        this.lotteryRounds = response.data.content
                        for (let i=0; i < this.lotteryRounds.length; i++) {
                            let lotteryRound = this.lotteryRounds[i]
                            axios
                                .get('http://localhost:8080/api/v1/lotteries/rounds/' + lotteryRound.id + '/options')
                                .then(response => {
                                    let tmp = new Map();
                                    tmp.set(lotteryRound.id, response.data)
                                    this.lotteryRoundOptionsByLotteryRoundId.set(lotteryRound.id, response.data)
                                })
                                .catch(function (error) {
                                    console.log(error);
                            })
                        }
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
            axios
                .get('http://localhost:8080/api/v1/lotteries/' + lotteryId )
                .then(response => {this.lottery = response.data})
                .catch(function (error) { 
                    console.log(error);
            });
        } else {
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds')
                .then(response => {
                    this.lotteryRounds = response.data.content
                        for (let i=0; i < this.lotteryRounds.length; i++) {
                            let lotteryRound = this.lotteryRounds[i]
                            axios
                                .get('http://localhost:8080/api/v1/lotteries/rounds/' + lotteryRound.id + '/options')
                                .then(response => {
                                    let tmp = new Map();
                                    tmp.set(lotteryRound.id, response.data)
                                    this.lotteryRoundOptionsByLotteryRoundId= tmp
                                })
                                .catch(function (error) {
                                    console.log(error);
                            })
                        }
                })
                .catch(function (error) {
                    console.log(error);
                })
        }
        
        
    }
}
</script>

<style>

</style>