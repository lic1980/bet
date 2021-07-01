<template>
    <div>
        <div>
        </div>
        <div>
            <div>投注列表：</div> 
            <div>
                <el-tabs v-model="activeName" @tab-click="handleClick">
                    <el-tab-pane label="待处理投注" name="first">
                        <el-table :data="bids" style="width: 100%">
                            <el-table-column prop="id" label="编号">
                            </el-table-column>
                            <el-table-column prop="option.round.title" label="名字">
                            </el-table-column>
                            <el-table-column  label="投注项">
                                <template slot-scope="scope">  
                                    目标：{{ scope.row.option.optionText }}<br/>
                                    费用：{{ scope.row.fee }}<br/>
                                    投注赔率：{{ scope.row.odds }}
                                    实时赔率：{{ scope.row.option.odds }} 
                                </template>
                            </el-table-column>
                            <el-table-column prop="initiator.tel" label="投注人">
                            </el-table-column>
                            <el-table-column prop="recipient.tel" label="收注人">
                            </el-table-column>
                            <el-table-column label="状态">
                                <template slot-scope="scope">
             
                                    <span v-if="scope.row.status == 'NEW'">初始</span>
                                    <span v-if="scope.row.status == 'ACCEPTED'">接收</span>
                                    <span v-if="scope.row.status == 'CANCEL'">撤销</span>
                                    <span v-if="scope.row.status == 'SETTLED'">开奖</span>
                                    <span v-if="scope.row.status == 'REJECTED'">拒绝</span>
                                </template>
                            </el-table-column>
                            <el-table-column label="状态">
                                <template slot-scope="scope">
                                    <span v-if="scope.row.result == null">未知</span>
                                    <span v-if="scope.row.result == 'PENDING'">未知</span>
                                    <span v-if="scope.row.result == 'WIN'">赢</span>
                                    <span v-if="scope.row.result == 'LOSE'">输</span>
                                </template>
                            </el-table-column>
                            <el-table-column prop="createTime" label="投注时间">
                            </el-table-column>
                            
                        </el-table>
                        
                    </el-tab-pane>
                    <el-tab-pane label="历史投注" name="second">
                        <el-table :data="bids" style="width: 100%">
                            <el-table-column prop="id" label="编号">
                            </el-table-column>
                            <el-table-column  label="投注项">
                                <template slot-scope="scope">  
                                    <li v-for="(option, index) in scope.row.options" v-bind:key="index">
                                        目标：{{ option.option.optionText }}<br/>
                                        费用：{{ option.fee }}<br/>
                                        赔率：{{ option.odds }}
                                    </li>  
                                </template>
                            </el-table-column>
                            
                            <el-table-column prop="customer.tel" label="投注人">
                            </el-table-column>
                            <el-table-column prop="createTime" label="投注时间">
                            </el-table-column>
                            <el-table-column prop="bidTime" label="完成时间">
                            </el-table-column>
                        </el-table>
                    </el-tab-pane>
                </el-tabs>
            </div>
        </div>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data () {
        return {
            activeName: 'first',
            agent: {},
            dialogVisible: false,
            imageUrl: "",
            bids: [],
            bidId: "",
            status: ""
        };
    },
    methods: {
      handleClick(tab) {
        this.status = "active";
        if (tab.name != "first") {
            this.status = "history";
        } 
        this.loadBids(this.status);
      },
      loadBids(status) {
        axios
            .get('http://localhost:8080/api/v1/agents/'+this.agent.id+'/bids?status=' + status + '&page=1&size=10')
            .then(response => {
                    this.bids = response.data.content;
                })
            .catch(function (error) { 
                console.log(error);
            });
      },
      acceptBid(row) {
          this.bidId = row.id;
          let data= {status: 'BIDDEN'};
          axios
            .patch('http://localhost:8080/api/v1/agents/'+this.agent.id+'/bids/' + row.id, data, {headers: {'Content-Type': 'application/json'}})
            .then(
                () =>  this.loadBids(this.status)
            )
            .catch(function (error) { 
                console.log(error);
            });
          
      },
      refuseBid(row) {
          this.bidId = row.id;
          let data= {
              status: 'REJECTED',
              message: "实时赔率已大幅变化"
            };
          axios
            .patch('http://localhost:8080/api/v1/agents/'+this.agent.id+'/bids/' + row.id, data, {headers: {'Content-Type': 'application/json'}})
            .then(
                () =>  this.loadBids(this.status)
            )
            .catch(function (error) { 
                console.log(error);
            });
      },
      beforeAvatarUpload(){
      }
    },
    mounted () {
        let agentId = sessionStorage.getItem(global.AGENT_ID_KEY);
        axios
            .get('http://localhost:8080/api/v1/agents/' + agentId)
            .then(response => (this.agent = response.data))
            .catch(function (error) { 
                console.log(error);
            });
        axios
            .get('http://localhost:8080/api/v1/agents/'+agentId+'/bids?status=active&page=1&size=10')
            .then(response => {
                    this.bids = response.data.content;
                })
            .catch(function (error) { 
                console.log(error);
            });
    }
}
</script>

<style>

</style>