<template>
    <div>
        <div>
            <div><el-button size="small" @click="openPasswordDialog()">修改密码</el-button></div>
        </div>
        <div>
            <div>账户余额：{{ customer.deposit }}</div>
        </div>
        <div>
            <div>最近投注：</div> 

            <el-tabs v-model="activeTab"  @tab-click="handleSwitchBids">
                <el-tab-pane label="已投注" name="initiator">
                <el-table :data="bids" style="width: 100%">
                        <el-table-column prop="lotteryRound.title" label="标题"></el-table-column>
                        <el-table-column  label="投注项">
                            <template slot-scope="scope">  
                                <li v-for="(option, index) in scope.row.options" v-bind:key="index">
                                目标：{{ option.option.optionText }}<br/>
                                费用：{{ option.fee }}<br/>
                                赔率：{{ option.odds }}
                                </li>  
                            </template>
                        </el-table-column>
                        <el-table-column prop="createTime" label="投注时间">
                        </el-table-column>
                        <el-table-column label="备注">
                            <template slot-scope="scope">  
                                <span v-if="scope.row.recipient==null">
                                    未接受
                                </span>
                                <span v-if="scope.row.recipient!=null">
                                    已接受
                                </span>
                            </template>
                        </el-table-column>
                </el-table>
                </el-tab-pane>
                <el-tab-pane label="已收注" name="recipient">
                <el-table :data="bids" style="width: 100%">
                        <el-table-column prop="lotteryRound.title" label="标题"></el-table-column>
                        <el-table-column  label="投注项">
                            <template slot-scope="scope">  
                                <li v-for="(option, index) in scope.row.options" v-bind:key="index">
                                目标：{{ option.option.optionText }}<br/>
                                费用：{{ option.fee }}<br/>
                                赔率：{{ option.odds }}
                                </li>  
                            </template>
                        </el-table-column>
                        <el-table-column prop="createTime" label="投注时间">
                        </el-table-column>
                        <el-table-column label="备注">
                            
                        </el-table-column>
                </el-table>
                </el-tab-pane>
                <el-tab-pane label="去收注" name="none">
                <el-table :data="bids" style="width: 100%">
                        <el-table-column prop="lotteryRound.title" label="标题"></el-table-column>
                        <el-table-column  label="投注项">
                            <template slot-scope="scope">  
                                <li v-for="(option, index) in scope.row.options" v-bind:key="index">
                                目标：{{ option.option.optionText }}<br/>
                                费用：{{ option.fee }}<br/>
                                赔率：{{ option.odds }}
                                </li>  
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
                </el-tab-pane>
            </el-tabs>

            
        </div>

        <div>
            <el-dialog
                title="修改密码"
                :visible.sync="passwordDialogVisible"
                width="550px">
                <el-form>
                    <el-form-item label="旧密码" required>
                        <el-input v-model="customer.plainPassword" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" required>
                        <el-input v-model="customer.newPlainPassword" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" required>
                        <el-input v-model="customer.newPlainPassword2" show-password></el-input>
                    </el-form-item>
                </el-form>
                <div style="text-align: center;" class="dialog-footer">
                    <el-button @click="passwordDialogVisible = false" size="mini">取 消</el-button>
                    <el-button @click="changePassword()" size="mini">确 定</el-button>
                </div>
            </el-dialog>
        </div>
    </div>

    
</template>

<script>
import axios from 'axios'
import '../common/const.js'

export default {
    data () {
        return {
            customer: { 
                "id": "", 
                "deposit": 0 , 
                "plainPassword": null, 
                "newPlainPassword": null, 
                },
            bids: [],
            optionsBybidId: new Map(),
            passwordDialogVisible: false,
            activeTab: "initiator",
        }
    },
    methods : {
        reloadCustomer: function(cusId) {
            axios
            .get('http://localhost:8080/api/v1/customers/' + cusId)
            .then(response => (this.customer = response.data))
            .catch(function (error) { 
                console.log(error);
            });
        },
        reloadBids :  function(role) {
            let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
            axios
                .get('http://localhost:8080/api/v1/customers/'+ cusId +'/bids?role=' +role + '&page=1&size=20')
                .then(
                    response => {
                            this.bids = response.data.content;
                        }
                    )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        acceptBid: function(bid)  {
            let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
            let data = {
                recipient: {
                    id: cusId,
                },
                status: "ACCEPTED",
            }
            axios
                .patch('http://localhost:8080/api/v1/customers/'+cusId+'/bids/' + bid.id, data)
                .then(
                    response => {
                        let bids = []
                        this.$message("收注成功");
                        for (let i =0; i < this.bids.length; i++) {
                           if (bid.id != this.bids[i].id) {
                               bids.push(this.bids[i])
                           }
                        }
                        this.bids = bids
                        reloadCustomer(cusId)
                    })
                .catch(function (error) { 
                    console.log(error);
                });
        },
        handleSwitchBids: function(tab)  {
            this.reloadBids(tab.name);
        },
        openPasswordDialog: function() {
            this.passwordDialogVisible = true;
        },
        changePassword : function() {
            if(this.newPlainPassword != this.newPlainPassword2) {
                this.$message({ message: "两次输入密码不一致", type: 'error'});
                return
            }
            axios
                .patch('http://localhost:8080/api/v1/security-resources/customers', this.customer)
                .then(
                    response => {
                        this.customer = response.data
                        this.$message("密码修改成功");
                        this.passwordDialogVisible=false;
                        }
                    )
                .catch(function (error) { 
                    console.log(error);
                });
        },
    },
    mounted () {
        let cusId = sessionStorage.getItem(global.CUSTOMER_ID_KEY);
        reloadCustomer(cusId);
        this.reloadBids("initiator");
    }
}
</script>

<style>

</style>