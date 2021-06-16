<template>
    <div>
        <div>
            <div>
                <el-button
                            size="mini"
                            @click="openNewLottoryRoundDialog()">增加彩票实例
                </el-button>
            </div>
        </div>
        
        <div>
            <div>彩票实例列表：</div>
            <el-tabs v-model="activeName" @tab-click="handleSwitchLotteryRoundStatus">
                <el-tab-pane label="进行中实例" name="ongoing">
                    <el-table :data="lottoryRounds" style="width: 100%">
                        <el-table-column prop="id" label="ID">
                        </el-table-column>   
                        <el-table-column prop="title" label="名字">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                size="mini"
                                @click="openLottoryRoundDiag(scope.row)">查看详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="待开奖实例" name="closed">
                    <el-table :data="lottoryRounds" style="width: 100%">
                        <el-table-column prop="id" label="ID">
                        </el-table-column>   
                        <el-table-column prop="title" label="名字">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                size="mini"
                                @click="openLottoryRoundResultDiag(scope.row)">设置结果</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                </el-tab-pane>
                <el-tab-pane label="已开奖实例" name="published">已开奖实例</el-tab-pane>
            </el-tabs>
            <div>
                
            </div>
        </div>
        <el-dialog
            title="彩票实例详情"
            :visible.sync="lottoryRoundDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lotteryRound.title" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="参数：" label-width="100px">
                    <el-input v-model="lotteryRound.parameters" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="描述：" label-width="100px">
                    <el-input v-model="lotteryRound.description" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                <el-form-item label="开始时间：" label-width="100px">
                    <el-date-picker
                    v-model="lotteryRound.openingTime"
                    type="datetime"
                    placeholder="Select date and time"
                    :picker-options="pickerOptions">
                    </el-date-picker>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="截止时间：" label-width="100px">
                    <el-date-picker
                    v-model="lotteryRound.cutOffTime"
                    type="datetime"
                    placeholder="Select date and time"
                    :picker-options="pickerOptions">
                    </el-date-picker>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="lottoryRoundDialogVisible = false" size="mini">取 消</el-button>
                <el-button @click="updateLotteryRound()" size="mini">修 改</el-button>
            </div>
            <hr style="border-top:1px dotted #DCDFE6;">
            <div>
                 <el-form>
                     <el-form-item label="标签：" label-width="100px">
                        <el-select v-model="lotteryRoundTagIdsSelect" multiple placeholder="Select">
                            <el-option
                            v-for="item in lotteryRoundTags"
                            :key="item.id"
                            :label="item.text"
                            :value="item.id">
                            </el-option>
                        </el-select>
                     </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="updateLotteryRoundTags()" size="mini">修改标签</el-button>
            </div>
            <hr style="border-top:1px dotted #DCDFE6;">
            <div>
                <el-button type="small" @click="openNewLottoryRoundOptionDialog()">增加赔率</el-button>
                <el-form>
                 <el-form-item label="选项列表：" label-width="100px">
                    <el-table :data="lotteryRoundOptions" style="width: 100%">
                        <el-table-column prop="option.item.name" label="选项">
                        </el-table-column>
                        <el-table-column prop="optionText" label="名字">
                        </el-table-column>
                        <el-table-column prop="odds" label="赔率">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                size="mini"
                                @click="openLottoryRoundOptinDiag(scope.row)">更新赔率</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                 </el-form-item>
                </el-form>
            </div>
        </el-dialog>
        
        <el-dialog
            title="增加彩票赔率"
            :visible.sync="newLottoryRoundOptionDialogVisible"
            width="550px">
            <div>
                 <el-form>
                     <el-form-item label="选项：" label-width="100px">
                    <template>
                    <el-select v-model="lotteryItemOptionId" clearable placeholder="Select" size="mini">
                        <el-option
                        v-for="item in lotteryItemOptions"
                        :key="item.id"
                        :label="item.text"
                        :value="item.id">
                        </el-option>
                    </el-select>
                    </template>
                     </el-form-item>
                 </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="赔率：" label-width="100px">
                    <el-input v-model="odds" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="newLottoryRoundOptionDialogVisible = false" size="mini">取 消</el-button>
                <el-button @click="addLotteryRoundOption()" size="mini">修 改</el-button>
            </div>
        </el-dialog>
        <el-dialog
            title="修改彩票赔率"
            :visible.sync="lottoryRoundOptionDialogVisible"
            width="550px">
            
            <div>
                <el-form>
                 <el-form-item label="赔率：" label-width="100px">
                    <el-input v-model="lotteryRoundOption.odds" size="small"></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="lottoryRoundOptionDialogVisible = false" size="mini">取 消</el-button>
                <el-button @click="updateLotteryRoundOption()" size="mini">修 改</el-button>
            </div>
        </el-dialog>

        <el-dialog
            title="增加彩票实例"
            :visible.sync="newLottoryRoundDialogVisible"
            width="550px">
            
            <div>
                <el-form>
                  <el-form-item label="彩票类型：" label-width="100px">  
                    <el-select v-model="lotteryRound.lottery.id" placeholder="Select">
                        <el-option
                        v-for="item in lotteries"
                        :key="item.id"
                        :label="item.name"
                        :value="item.id">
                        </el-option>
                    </el-select>
                  </el-form-item>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lotteryRound.title" size="small"></el-input>
                 </el-form-item>
                 <el-form-item label="描述：" label-width="100px">
                    <el-input v-model="lotteryRound.description" size="small"></el-input>
                 </el-form-item>
                 <el-form-item label="参数：" label-width="100px">
                    <el-input v-model="lotteryRound.parameters" size="small"></el-input>
                 </el-form-item>
                 <el-form-item label="开始时间：" label-width="100px">
                    <el-date-picker
                    v-model="lotteryRound.openingTime"
                    type="datetime"
                    placeholder="Select date and time"
                    :picker-options="pickerOptions">
                    </el-date-picker>
                 </el-form-item>
                 <el-form-item label="截止时间：" label-width="100px">
                    <el-date-picker
                    v-model="lotteryRound.cutOffTime"
                    type="datetime"
                    placeholder="Select date and time"
                    :picker-options="pickerOptions">
                    </el-date-picker>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="newLottoryRoundDialogVisible = false" size="mini">取 消</el-button>
                <el-button @click="addLotteryRound()" size="mini">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog
            title="设置获奖"
            :visible.sync="lottoryRoundResultsDialogVisible"
            width="550px">
            
            <div>
                <span>彩票： </span><span> {{ lotteryRound.lottery.name }}</span>
            </div>
            <div>
                <span>实例： </span><span> {{ lotteryRound.title }}</span>
            </div>
            <div>
                <span>截至： </span><span> {{ lotteryRound.cutOffTime }}</span>
            </div>
            <div>
                <span>结果： </span>
                <span> 
                    <el-select v-model="lottoryRoundOptionIds" multiple placeholder="请选择" size="small">
                        <el-option-group
                        v-for="lotteryItem in lotteryItems"
                        :key="lotteryItem.id"
                        :label="lotteryItem.name">
                        <el-option
                            v-for="roundOption in lotteryRoundOptionsInLotteryItemId.get(lotteryItem.id)"
                            :key="roundOption.option.id"
                            :label="roundOption.optionText"
                            :value="roundOption.option.id">
                        </el-option>
                        </el-option-group>
                    </el-select>
                </span>
            </div>
            <div style="text-align: center;margin-top:10px;" class="dialog-footer">
                <el-button @click="lottoryRoundResultsDialogVisible = false" size="mini">取 消</el-button>
                <el-button @click="addLotteryRoundResult()" size="mini">确 定</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data () {
        return {
            lotteries: [],
            lottoryRounds: [],
            lotteryRound:{
                lottery:{
                    id:"",
                }
            },
            lotteryRoundOptions:[],
            lotteryRoundOption:{},
            lotteryItemOptions:[],
            lotteryItemOptionId: "",
            lotteryRoundTags:[],
            lotteryRoundTagIdsSelect:[],
            lottoryRoundOptionIds:[],
            odds:0,
            lottoryRoundDialogVisible: false,
            newLottoryRoundDialogVisible:false,
            lottoryRoundOptionDialogVisible:false,
            newLottoryRoundOptionDialogVisible:false,
            lottoryRoundResultsDialogVisible:false,
            lotteryRoundOptionsInLotteryItemId: new Map(),
            lotteryItems: [],
            pickerOptions: {
                shortcuts: [{
                    text: 'Today',
                    onClick(picker) {
                    picker.$emit('pick', new Date());
                    }
                }, {
                    text: 'Yesterday',
                    onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24);
                    picker.$emit('pick', date);
                    }
                }, {
                    text: 'A week ago',
                    onClick(picker) {
                    const date = new Date();
                    date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
                    picker.$emit('pick', date);
                    }
                }]
            },
        }
    },
    methods: {
        reloadRound: function(status) {
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds?status=' + status + '&page=1&size=50')
                .then(response => {
                        this.lottoryRounds = response.data.content;
                    })
                .catch(function (error) { 
                    console.log(error);
                });
        },
        handleSwitchLotteryRoundStatus: function(tab)  {
            this.reloadRound(tab.name);
        },
        updateLotteryRoundOption:function() {
            axios
                .put('http://localhost:8080/api/v1/admins/123/lotteries/123/rounds/'+ this.lotteryRound.id + "/options/" + this.lotteryRoundOption.id, this.lotteryRoundOption, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        this.$message("修改彩票赔率成功");
                        this.lottoryRoundOptionDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        addLotteryRoundOption:function() {
            let data = {
                odds:this.odds,
                option: {id: this.lotteryItemOptionId},
                round: {id: this.lotteryRound.id}
            }
            axios
                .post('http://localhost:8080/api/v1/admins/123/lotteries/123/rounds/'+ this.lotteryRound.id + "/options", data, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("增加彩票赔率成功");
                        this.newLottoryRoundOptionDialogVisible = false;
                        this.lotteryRoundOptions.push(response.data);
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        addLotteryRound: function() {
            axios
                .post('http://localhost:8080/api/v1/admins/123/lotteries/rounds', this.lotteryRound, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("增加彩票实例成功");
                        let round = response.data
                        this.lottoryRounds.push(round)
                        this.newLottoryRoundDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        addLotteryRoundResult: function() {
            let data=[];
            for(let i=0; i< this.lottoryRoundOptionIds.length; i++){
                data.push(
                    {
                        id: this.lottoryRoundOptionIds[i],
                    }
                )
            }
            axios
                .post('http://localhost:8080/api/v1/admins/123/lotteries/' + this.lotteryRound.lottery.id + '/rounds/' + this.lotteryRound.id + '/results', data, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        this.$message("增加彩票结果成功");
                        this.lottoryRoundResultsDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        updateLotteryRoundTags : function() {
            let tags = [];
            for (let i =0; i < this.lotteryRoundTagIdsSelect.length; i++){
                let tag = {id: this.lotteryRoundTagIdsSelect[i]}
                tags.push(tag)
            }
            axios
                .post('http://localhost:8080/api/v1/admins/123/lotteries/rounds/' + this.lotteryRound.id + '/tags', tags, {headers: {'Content-Type': 'application/json'}})
                .then(response => {
                    this.lotteryRoundTags = response.data.content;
                    this.$message("修改彩票实例标签成功");
                })
                .catch(function (error) { 
                    console.log(error);
                });
        },
        updateLotteryRound: function() {
            axios
                .put('http://localhost:8080/api/v1/admins/123/lotteries/rounds/'+ this.lotteryRound.id, this.lotteryRound, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        this.$message("修改彩票实例成功");
                        this.lottoryRoundDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        openNewLottoryRoundDialog: function() {
            this.newLottoryRoundDialogVisible = true;
            axios
                .get('http://localhost:8080/api/v1/lotteries')
                .then(response => {
                        this.lotteries = response.data.content;
                    })
                .catch(function (error) { 
                    console.log(error);
            });
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds/tags')
                .then(response => {
                        this.lotteryRoundTags = response.data.content;
                    })
                .catch(function (error) { 
                    console.log(error);
            });
        },
        openNewLottoryRoundOptionDialog: function() {
            this.newLottoryRoundOptionDialogVisible = true;
            axios
                .get('http://localhost:8080/api/v1/lotteries/' + this.lotteryRound.lottery.id + '/options')
                .then(response => {
                    let lotteryItemOptions = response.data;
                    axios
                        .get('http://localhost:8080/api/v1/lotteries/rounds/' + this.lotteryRound.id + '/options')
                        .then(
                            response => {
                                let lotteryRoundOptions = response.data;
                                for (let i=0;i<lotteryItemOptions.length;i++){
                                    let lotteryItemOption = lotteryItemOptions[i];
                                    let found = false;
                                    for (var j=0; j< lotteryRoundOptions.length;j++){
                                        let lotteryRoundOption = lotteryRoundOptions[j];
                                        if (lotteryItemOption.id == lotteryRoundOption.option.id) {
                                            found = true;
                                            break;
                                        }
                                    }
                                    if(!found) {
                                            this.lotteryItemOptions.push(lotteryItemOption)
                                        }
                                }
                                //console.log("########" +  this.lotteryItemOptions.length);
                            })
                })
                .catch(function (error) { 
                    console.log(error);
            });
        },
        openLottoryRoundDiag: function (row) {
            this.lotteryRound = row;
            this.lottoryRoundDialogVisible = true;
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds/tags')
                .then(response => {
                        this.lotteryRoundTags = response.data.content;
                    })
                .catch(function (error) { 
                    console.log(error);
            });
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds/' + row.id + '/tags')
                .then(response => {
                        let tags = response.data.content;
                        for (var i = 0 ; i < tags.length; i++) {
                            this.lotteryRoundTagIdsSelect.push(tags[i].id)
                        }
                    })
                .catch(function (error) { 
                    console.log(error);
            });
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds/' + row.id + '/options')
                .then(response => {
                        this.lotteryRoundOptions = response.data;
     
                    })
                .catch(function (error) { 
                    console.log(error);
            });
            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds/' + row.id)
                .then(response => {
                        this.lotteryRound = response.data;
                    })
                .catch(function (error) { 
                    console.log(error);
            });
        },
        openLottoryRoundOptinDiag: function (row) {
            this.lottoryRoundOptionDialogVisible = true;
            this.lotteryRoundOption = row;
        },
        openLottoryRoundResultDiag: function (row) {
            this.lottoryRoundResultsDialogVisible = true;
            this.lotteryRound = row;

            axios
                .get('http://localhost:8080/api/v1/lotteries/rounds/' + row.id + '/options')
                .then(response => {
                        this.lotteryRoundOptions = response.data;
                        for (let i=0; i < this.lotteryRoundOptions.length; i ++) {
                            let lotteryRoundOption = this.lotteryRoundOptions[i];
                            let lotteryItemOption = lotteryRoundOption.option;
                            let lotteryItem = lotteryItemOption.item;
                            if (this.lotteryRoundOptionsInLotteryItemId.has(lotteryItem.id)) {
                                let lotteryRoundOptions = this.lotteryRoundOptionsInLotteryItemId.get(lotteryItem.id)
                                lotteryRoundOptions.push(lotteryRoundOption);
                                this.lotteryRoundOptionsInLotteryItemId.set(lotteryItem.id, lotteryRoundOptions);
                            } else {
                                this.lotteryItems.push(lotteryItem);
                                this.lotteryRoundOptionsInLotteryItemId.set(lotteryItem.id, [lotteryRoundOption]);
                            }
                        }
                    })
                .catch(function (error) { 
                    console.log(error);
            });
        }
    },
    mounted () {
        axios
            .get('http://localhost:8080/api/v1/lotteries/rounds?page=1&size=20')
            .then(response => {
                    this.lottoryRounds = response.data.content;
                })
            .catch(function (error) { 
                console.log(error);
            });
        axios
            .get('http://localhost:8080/api/v1/lotteries/rounds/tags?page=1&size=20')
            .then(response => {
                    this.lotteryRoundTags = response.data.content;
                })
            .catch(function (error) { 
                console.log(error);
            });
    }
}
</script>

<style>

</style>