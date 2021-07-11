<template>
    <div>
    <div><el-button size="small" @click="openPasswordDialog()">修改密码</el-button></div>
  
    <div>
        <div style="overflow:hidden;height:40px;">
                <div style="float:left;width:60px;">电话：</div>
                <div style="float:left;">{{ agent.tel}}</div>
            </div>
            <div  style="overflow:hidden;height:40px;">
                <div style="float:left;width:60px;">地址：</div>
                <div style="float:left;">{{ agent.address}}</div>
            </div>
            <div  style="overflow:hidden;height:40px;">
                <div style="float:left;width:60px;">微信：</div>
                <div style="float:left;">{{ agent.weixin}}</div>
            </div>
        </div>
        <div>
            <el-dialog
                title="修改密码"
                :visible.sync="passwordDialogVisible"
                width="550px">
                <el-form>
                    <el-form-item label="旧密码" required>
                        <el-input v-model="agent.plainPassword" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" required>
                        <el-input v-model="agent.newPlainPassword" show-password></el-input>
                    </el-form-item>
                    <el-form-item label="新密码" required>
                        <el-input v-model="agent.newPlainPassword2" show-password></el-input>
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
export default {
    data () {
        return {
            agent: {},
            passwordDialogVisible:false
        }
    },
    methods: {
        openPasswordDialog () {
            this.passwordDialogVisible = true;
        },
        changePassword  () {
            if(this.newPlainPassword != this.newPlainPassword2) {
                this.$message({ message: "两次输入密码不一致", type: 'error'});
                return
            }
            axios
                .patch('http://' + this.BASE_URL + '/api/v1/security-resources/agents', this.agent)
                .then(
                    response => {
                        this.agent = response.data
                        this.$message("密码修改成功");
                        this.passwordDialogVisible=false;
                        }
                    )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        handleClose(){
            this.dialogVisible = true;
            this.customer = {};
            this.amount = 1;
        },
        openDepositDiag(row) {
            this.amount = 1;
            this.customer = row;
            this.dialogVisible = true;
        },
        submitDeposit() {
            this.startDeposit = true;
            let data = {
                "customer": {"id": this.customer.id},
                "agent": {"id": this.agent.id},
                "amount": this.amount
            };
            axios
                .post('http://' + this.BASE_URL + '/api/v1/agents/'+this.agent.id+'/customers/' +this.customer.id+ '/deposits', data, {headers: {'Content-Type': 'application/json'}})
                .then(response => {
                    let deposit = response.data;
                    for (let cus of this.customers) {
                        if (cus.id == this.customer.id) {
                           cus.deposit =  cus.deposit + deposit.amount;
                        } 
                    }

                    this.startDeposit = false;
                })
                .catch(error => { 
                    console.log(error);
                    this.startDeposit = false;
                });
            this.dialogVisible = false;
        }
    },
    mounted () {
        let agentId = sessionStorage.getItem(global.AGENT_ID_KEY);
        axios
            .get('http://' + this.BASE_URL + '/api/v1/agents/' + agentId)
            .then(response => (this.agent = response.data))
            .catch(function (error) { 
                console.log(error);
            });

    }
}
</script>

<style>

</style>