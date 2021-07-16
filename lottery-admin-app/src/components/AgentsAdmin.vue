<template>
    <div>
        <div>
            <el-button  size="mini" @click="openNewAgentDiag()" >新增代理</el-button>
        </div>
        <div>
            <div>代理列表：</div> 
            <div>
                <el-table :data="agents" style="width: 100%">
                    <el-table-column prop="tel" label="电话">
                    </el-table-column>   
                    <el-table-column prop="address" label="地址">
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button
                            size="mini"
                            @click="resetAgentPassword(scope.row)">重设密码</el-button>
                        </template>
                    </el-table-column> 
                </el-table>
            </div>
        </div>
        <el-dialog
            title="新增代理"
            :visible.sync="newAgentDialogVisible"
            width="350px"
            >
            <div>
                <el-form>
                 <el-form-item label="代理电话：" label-width="100px">
                    <el-input v-model="agent.tel" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="newAgentDialogVisible = false" size="mini">取 消</el-button>
                <el-button type="primary" size="mini" @click="addAgent()" >确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data () {
        return {
            agents: [],
            agent: {},
            
            newAgentDialogVisible: false,
        }
    },
    methods: {
        openNewAgentDiag(){
           this.newAgentDialogVisible = true 
        },
        resetAgentPassword(agent) {
            agent.newPlainPassword = agent.tel
            let adminId = sessionStorage.getItem(global.ADMIN_ID_KEY);
            axios
                .patch('http://' + this.BASE_URL + '/api/v1/admins/' + adminId +'/agents/' + agent.id, agent, {headers: {'Content-Type': 'application/json'}})
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
        addAgent() {
            this.agent.plainPassword = this.agent.tel
            let adminId = sessionStorage.getItem(global.ADMIN_ID_KEY);
            axios
                .post('http://' + this.BASE_URL + '/api/v1/admins/' + adminId +'/agents', this.agent, {headers: {'Content-Type': 'application/json'}})
                .then(
                    (response) => {
                            this.$message("添加代理成功");
                            this.agents.push(response.data);
                            this.newAgentDialogVisible = false;
                        }
                )
                .catch(error => { 
                    this.$message.error("添加代理失败，检查ID是否已存在");
                    console.log(error)
                });
        }
    },
    mounted () {
        let adminId = sessionStorage.getItem(global.ADMIN_ID_KEY);
        axios
            .get('http://' + this.BASE_URL + '/api/v1/admins/' + adminId +'/agents?page=1&size=500')
            .then(response => (this.agents = response.data.content))
            .catch(function (error) { 
                console.log(error);
            });

    }
}
</script>

<style>

</style>