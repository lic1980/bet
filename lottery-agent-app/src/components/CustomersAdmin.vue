<template>
    <div>
        <div>
            <el-button size="small" @click="openNewCustomerDiag()" :disabled="startExchange">新增客户</el-button>
        </div>
        <div>
            <div>客户列表：</div> 
            <div>
                <el-table :data="customers" style="width: 100%">
                    <el-table-column prop="tel" label="客户电话">
                    </el-table-column>   
                    <el-table-column prop="deposit" label="余额">
                    </el-table-column>   
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button
                            size="mini"
                            @click="openExchangeDiag(scope.row)">充值/取值</el-button>
                            <el-button
                            size="mini"
                            @click="resetAgentPassword(scope.row)">重设密码</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <el-dialog
            title="修改账户"
            :visible.sync="newExchangeDialogVisible"
            width="350px"
            :before-close="handleClose">
            <div>
                <el-form>
                 <el-form-item label="客户电话：" label-width="100px">
                    <el-input v-model="customer.tel" size="small" :disabled="true"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="客户现值：" label-width="100px">
                    <el-input v-model="customer.deposit" size="small" :disabled="true"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="账户变化：" label-width="100px">
                    <el-input-number v-model="amount" size="small" :precision="2" :step="1" :min="-5000" :max="5000"></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="newExchangeDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="submitExchange()" :disabled="startExchange">确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog
            title="新增账户"
            :visible.sync="newCustomerDialogVisible"
            width="350px"
            :before-close="handleClose">
            <div>
                <el-form>
                 <el-form-item label="客户电话：" label-width="100px">
                    <el-input v-model="customer.tel" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="客户账户：" label-width="100px">
                    <el-input-number v-model="customer.deposit" size="small" :precision="2" :step="1" :min="0" :max="5000"></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="newCustomerDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addCustomer()" :disabled="startExchange">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data () {
        return {
            amount: 0,
            agent: {},
            newExchangeDialogVisible: false,
            newCustomerDialogVisible:false,
            customer: {
                agent: {
                    id: ""
                },
                deposit: 0,
            },
            customers: [],
            startExchange: false,
        };
    },
    methods: {
        openAccessPicDiag(row) {
            row.id
        },
        handleClose(){
            this.newExchangeDialogVisible = true;
            this.customer = {};
            this.amount = 1;
        },
        openNewCustomerDiag() {
            this.newCustomerDialogVisible = true;
        },
        openExchangeDiag(row) {
            this.amount = 1;
            this.customer = row;
            this.newExchangeDialogVisible = true;
        },
        addCustomer() {
            let agentId = sessionStorage.getItem(global.AGENT_ID_KEY);
            this.customer.agent.id = agentId;
            axios
                .post('http://' + this.BASE_URL + '/api/v1/agents/' + agentId + '/customers', this.customer, {headers: {'Content-Type': 'application/json'}})
                .then(
                    (response) => {
                            this.$message("添加客户成功");
                            this.customers.push(response.data);
                            this.newCustomerDialogVisible = false;
                        }
                )
                .catch(function (error) { 
                    this.$message.error("添加客户失败，检查ID是否已存在");
                    console.log(error)
                });
        },
        resetAgentPassword(customer) {
            customer.newPlainPassword = customer.tel
            let agentId = sessionStorage.getItem(global.AGENT_ID_KEY);
            axios
                .patch('http://' + this.BASE_URL + '/api/v1/agents/' + agentId + '/customers/' + customer.id, customer, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                            this.$message("重设密码成功");
                        }
                )
                .catch(error => { 
                    this.$message.error("重设密码失败");
                    console.log(error)
                });
        },
        submitExchange() {
            if (this.amount == 0){
                this.$message("充值成功");
                return;
            }
            if (this.customer.deposit + this.amount < 0 ) {
                this.$message.error('账户小于0')
                return;
            }
            let agentId = sessionStorage.getItem(global.AGENT_ID_KEY);
            this.startDeposit = true;
            let data = {
                "agent": {"id": agentId},
                "customer": {"id": this.customer.id},
                "reference": this.agent.id,
                "amount": this.amount
            };
            axios
                .post('http://' + this.BASE_URL + '/api/v1/agents/' + this.agent.id + '/customers/' +this.customer.id+ '/exchanges', data, {headers: {'Content-Type': 'application/json'}})
                .then(response => {
                    let deposit = response.data;
                    for (let cus of this.customers) {
                        if (cus.id == this.customer.id) {
                           cus.deposit =  cus.deposit + deposit.amount;
                        } 
                    }
                    this.startExchange = false;
                    if (this.amount > 0){
                        this.$message("充值成功");
                        return;
                    }
                    if (this.amount < 0){
                        this.$message("取值成功");
                        return;
                    }
                })
                .catch(error => { 
                     this.$message.error("操作失败");
                    console.log(error);
                    this.startExchange = false;
                });
            this.newExchangeDialogVisible = false;
        },

    },
    mounted () {
        let agentId = sessionStorage.getItem(global.AGENT_ID_KEY);
        axios
            .get('http://' + this.BASE_URL + '/api/v1/agents/' + agentId)
            .then(response => (this.agent = response.data))
            .catch(function (error) { 
                this.$message.error("访问代理");
                console.log(error);
            });
        axios
            .get('http://' + this.BASE_URL + '/api/v1/agents/'+ agentId + '/customers?page=1&size=10')
            .then(response => {
                    this.customers = response.data.content;
                })
            .catch(function (error) { 
                this.$message.error("访问代理客户");
                console.log(error);
            });
    }
}
</script>

<style>

</style>