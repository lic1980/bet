<template>
    <div>
        <div>
            <div>账户余额：{{ customer.deposit }}</div>
        </div>
        <div>
            <div style="line-height: 36px;">
                <div >
                    <el-table :data="bids" style="width: 100%">
                        <el-table-column prop="option.round.title" label="标题"></el-table-column>
                        <el-table-column  label="投注项">
                            <template slot-scope="scope">  
                                目标：{{ scope.row.option.optionText }}<br/>
                                费用：{{ scope.row.fee }}<br/>
                                赔率：{{ scope.row.odds }}
                            </template>
                        </el-table-column>
                        <el-table-column prop="createTime" label="投注时间">
                        </el-table-column>
                        <el-table-column label="动作">
                            <template slot-scope="scope">
                                <el-button
                                size="mini"
                                @click="acceptBid(scope.row)">收注</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
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
            bids: [],
        }
    },
    methods: {
        reloadCustomer: function(cusId) {
            axios
            .get('http://' + this.BASE_URL + '/api/v1/customers/' + cusId)
            .then(response => (this.customer = response.data))
            .catch(function (error) { 
                console.log(error);
            });
        },
        acceptBid: function(bid)  {
            let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
            let data = {
                status: "ACCEPTED",
            }
            axios
                .patch('http://' + this.BASE_URL + '/api/v1/customers/'+cusId+'/bids/' + bid.id, data)
                .then(
                    () => {
                        let bids = []
                        this.$message("收注成功");
                        for (let i =0; i < this.bids.length; i++) {
                           if (bid.id != this.bids[i].id) {
                               bids.push(this.bids[i])
                           }
                        }
                        this.bids = bids
                        this.reloadCustomer(cusId)
                    })
                .catch(error => { 
                    if (error.response.status == 400) {
                        this.$message.error("已被别人接收");
                    } else {
                        this.$message.error("服务器错误");
                    }
                    console.log(error);
                });
        },
    },
    mounted () {
        let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
        this.reloadCustomer(cusId);
        axios
            .get('http://' + this.BASE_URL + '/api/v1/customers/'+ cusId +'/customers/bids?page=1&size=20')
            .then(
                response => {
                        this.bids = response.data.content;
                    }
                )
            .catch(function (error) { 
                console.log(error);
        });
    }
}
</script>

<style>

</style>