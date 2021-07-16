<template>
    <div>
        <div>
            <div>账户余额：{{ customer.deposit }}</div>
        </div>
        <div>
            <div style="line-height: 36px;">
                
                <div >
                    彩票名称：{{ lotteryRound.lottery.name }}
                </div>
                <div >
                    {{ lotteryRound.title }}
                </div>
                <div>
                    停止投注时间：
                    <el-date-picker
                        v-model="lotteryRound.cutOffTime"
                        type="datetime"
                        :readonly="true"
                        size="small"
                        placeholder="Select date and time">
                    </el-date-picker>
                </div>
               
                <div style="overflow:hidden" v-for="(optionsItemId, itemIndex) in optionsByItemId.entries()" v-bind:key="itemIndex">
                    <div></div>
                    <div style="float:left;">
                        {{ itemByItemId.get(optionsItemId[0]).name }}： 
                    </div>
                    <div style="float:left;margin:2px;" v-for="(option, optionIndex) of optionsItemId[1]" v-bind:key="optionIndex">
                        <template v-if="roundOptionByOptionId.has(option.id) ">
                        <el-button v-on:click="openBidDialog(roundOptionByOptionId.get(option.id))"  size="small" >
                            <div v-if=" roundOptionByOptionId.get(option.id).odds > 1">
                            {{ roundOptionByOptionId.get(option.id).optionText }}&nbsp;&nbsp;{{ roundOptionByOptionId.get(option.id).odds }}
                            </div>
                        </el-button>
                        </template>
                    </div>
                </div>
            </div>
        </div>
        <el-dialog
            title="投注"
            :visible.sync="bidDialogVisible"
            width="30%"
            >
            <div>{{ lotteryRound.title }}</div>
            <div>{{ lotteryRoundOption.optionText }}</div>
            
            <div>
                <el-form>
                 <el-form-item label="赔率：" label-width="100px">
                    <el-input-number v-model="customerBid.odds" :precision="1" size="small" ></el-input-number>
                 </el-form-item>
                 <el-form-item label="本金：" label-width="100px">
                    <el-input-number v-model="customerBid.fee" :precision="0" size="small" ></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <div slot="footer" class="dialog-footer">
                <el-button @click="bidDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="bid">投注</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'

export default {
    data () {
        return {
            bidDialogVisible:false,
            current: null,
            times: 1,
            customer: { "id": "", "deposit": 0 },
            lotteryRound: {"lottery": {"name":""}},
            lotteryRoundOption: {},
            customerBid: {"odds":0, "fee": 0},
            optionsByItemId: new Map(),
            itemByItemId: new Map(),
            roundOptionByOptionId : new Map(),
        }
    },
    methods: {
        openBidDialog(roundOption) {
            this.lotteryRoundOption = roundOption
            this.bidDialogVisible = true
            this.customerBid.odds = roundOption.odds
        },
        bid: function() {
            let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
            if (this.customerBid.fee < global.FEE_MIN) {
                this.$message("最小投注不能小于" + global.FEE_MIN);
                return;
            }
            if (this.customerBid.fee > this.customer.deposit) {
                this.$message("余额不足");
                return;
            }
            if (this.customerBid.fee > global.FEE_MAX) {
                this.$message("超过最大投注额");
                return;
            }
            if (this.customerBid.odds < 1) {
                this.$message("赔率不能小于1");
                return;
            }
            let data = {
                "customer": {"id": cusId},
                "lotteryRound": {"id": this.lotteryRound.id},
                "option": {"id": this.lotteryRoundOption.id},
                "odds": this.customerBid.odds,
                 "fee":this.customerBid.fee,
            };
            axios
                .post('http://' + this.BASE_URL + '/api/v1/customers/' + cusId +'/bids', data, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("投注成功");
                        this.customer.deposit = this.customer.deposit - response.data.fee;
                        this.bidDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        }
    },
    mounted () {
        let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
        let lotteryRoundId = this.$route.query.lotteryRoundId;
        
        axios
            .get('http://' + this.BASE_URL + '/api/v1/customers/' + cusId)
            .then(response => (this.customer = response.data))
            .catch(function (error) { 
                console.log(error);
            });
        axios
            .get('http://' + this.BASE_URL + '/api/v1/lotteries/rounds/' + lotteryRoundId)
            .then(response => {
                this.lotteryRound = response.data

                axios
                    .get('http://' + this.BASE_URL + '/api/v1/lotteries/' + this.lotteryRound.lottery.id + '/options')
                    .then(response => {
                            let options = response.data;
                            let optionsByitem = [];
                            let optionsByItemId = new Map();
                            let itemByItemId = new Map();
                            for (let i = 0; i < options.length; i ++) {
                                let option = options[i];
                                let item = option.item;
                                if (optionsByItemId.has(item.id)) {
                                    optionsByItemId.get(item.id).push(option);
                                } else {
                                    optionsByitem = [];
                                    optionsByitem.push(option);
                                    optionsByItemId.set(item.id, optionsByitem);

                                    itemByItemId.set(item.id, item);
                                }
                            }
                            this.itemByItemId = itemByItemId
                            this.optionsByItemId = optionsByItemId;
                            
                        })
                    .catch(function (error) { 
                        console.log(error);
                    });

            })
            .catch(function (error) { 
                console.log(error);
            });
        axios
            .get('http://' + this.BASE_URL + '/api/v1/lotteries/rounds/' + lotteryRoundId + "/options")
            .then(response => {
                let options = response.data
                let roundOptionByOptionId = new Map()
                for (var i=0; i< options.length;i++ ) {
                    let option=options[i]
                    roundOptionByOptionId.set(option.option.id, option)
                }
                this.roundOptionByOptionId = roundOptionByOptionId
            })
            .catch(function (error) { 
                console.log(error);
            });
        
    }
}
</script>

<style>
.select{
    background-color: red !important;
    color:white !important;
}
.select:hover{
    background-color: red !important;
    color:white !important;
}
.select:focus{
    background-color: red !important;
    color:white !important;
}
.unselect{
    background-color: white !important;
    border-color: #DCDFE6 !important;
}
.unselect:hover{
    background-color: white !important;
    border-color: #DCDFE6 !important;
    color:black !important;
}
.unselect:focus{
    background-color: white !important;
    border-color: #DCDFE6 !important;
    color:black !important;
}
</style>