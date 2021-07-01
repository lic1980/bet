<template>
    <div>
        <div>
            <el-button type="primary" size="small" @click="openNewAgentDiag()" :disabled="startExchange">新增代理</el-button>
        </div>
        <div>
            <div>代理列表：</div> 
            <div>
                <el-table :data="agents" style="width: 100%">
                    <el-table-column prop="tel" label="电话">
                    </el-table-column>   
                    <el-table-column prop="address" label="地址">
                    </el-table-column>   
                </el-table>
            </div>
        </div>
        <el-dialog
            title="新增代理"
            :visible.sync="newAgentDialogVisible"
            width="350px"
            :before-close="handleClose">
            <div>
                <el-form>
                 <el-form-item label="代理电话：" label-width="100px">
                    <el-input v-model="agent.tel" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="newAgentDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="addAgent()" >确 定</el-button>
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
            
            newAgentDiagVisible: false,
        }
    },
    methods: {
        openNewAgentDiag(){
        },
        addAgent() {
            axios
                .post('http://localhost:8080/api/v1/security-resources/agents', this.agent, {headers: {'Content-Type': 'application/json'}})
                .then(
                    (response) => {
                            this.$message("添加代理成功");
                            this.agents.push(response.data);
                            this.newAgentDiagVisible = false;
                        }
                )
                .catch(function (error) { 
                    this.$message.error("添加代理失败，检查ID是否已存在");
                    console.log(error)
                });
        }
    },
    mounted () {
        let adminId = sessionStorage.getItem(global.ADMIN_ID_KEY);
        axios
            .get('http://localhost:8080/api/v1/admins/' + adminId +'/agents')
            .then(response => (this.agents = response.data.content))
            .catch(function (error) { 
                console.log(error);
            });

    }
}
</script>

<style>

</style>