<template>
    <div>
        <div>
            <div>
                <el-button
                            size="mini"
                            @click="openNewLottoryDiag()">增加彩票
                </el-button>
            </div>
        </div>
        <div>
            <div>彩票列表：</div> 
            <div>
                <el-table :data="lotteries" style="width: 100%">
                    <el-table-column prop="id" label="ID">
                    </el-table-column>   
                    <el-table-column prop="name" label="名字">
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button
                            size="mini"
                            @click="openLottoryDiag(scope.row)">查看详情</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <el-dialog
            title="彩票详情"
            :visible.sync="lottoryDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lottery.name" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="lottoryDialogVisible = false">取 消</el-button>
                <el-button @click="updateLottery()">修 改</el-button>
            </div>
            <hr style="border-top:1px dotted #DCDFE6;">
            <div>
                <el-form>
                 <el-form-item label="项目列表：" label-width="100px">
                    <el-button type="small" @click="openNewLottoryItemDialog(lottery.id)">增加项目</el-button>
                    <el-table :data="lotteryItems" style="width: 100%">
                        <el-table-column prop="name" label="名字">
                        </el-table-column>
                        <el-table-column prop="order" label="序号">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                size="mini"
                                @click="openLottoryItemDiag(scope.row)">查看详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                 </el-form-item>
                </el-form>
            </div>
        </el-dialog>

        <el-dialog
            title="增加彩票"
            :visible.sync="newLottoryDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lottery.name" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="newLottoryDialogVisible = false">取 消</el-button>
                <el-button @click="newLottory()">确 定</el-button>
            </div>
        </el-dialog>

        <el-dialog
            title="增加彩票项目"
            :visible.sync="newLottoryItemDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lotteryItem.name" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="序号：" label-width="100px">
                    <el-input-number v-model="lotteryItem.order" size="small" :min="-50" :step="1" :max="50"></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="newLottoryItemDialogVisible = false">取 消</el-button>
                <el-button  @click="newLottoryItem()" type="primary" >确 定</el-button>
            </span>
        </el-dialog>

       <el-dialog
            title="彩票项目详情"
            :visible.sync="lottoryItemDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lotteryItem.name" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="序号：" label-width="100px">
                    <el-input-number v-model="lotteryItem.order" size="small" :min="1" :step="1" :max="50"></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="lottoryItemDialogVisible = false">取 消</el-button>
                <el-button @click="updateLotteryItem()">修 改</el-button>
            </div>
            <hr style="border-top:1px dotted #DCDFE6;">
            <div>
                <el-form>
                 <el-form-item label="选项列表：" label-width="100px">
                    <el-button type="small" @click="openNewLottoryItemOptionDialog(lotteryItem.id)">增加选项</el-button>
                    <el-table :data="lotteryItemOptions" style="width: 100%">
                        <el-table-column prop="text" label="文本">
                        </el-table-column>
                        <el-table-column prop="order" label="序号">
                        </el-table-column>
                        <el-table-column label="操作">
                            <template slot-scope="scope">
                                <el-button
                                size="mini"
                                @click="openLottoryItemOptionDiag(scope.row)">查看详情</el-button>
                            </template>
                        </el-table-column>
                    </el-table>
                 </el-form-item>
                 
                </el-form>
            </div>
            
        </el-dialog>
        <el-dialog
            title="增加彩票选项"
            :visible.sync="newLottoryItemOptionDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="内容：" label-width="100px">
                    <el-input v-model="lotteryItemOption.text" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="序号：" label-width="100px">
                    <el-input-number v-model="lotteryItemOption.order" size="small" :min="-50" :step="1" :max="50"></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="newLottoryItemOptionDialogVisible = false">取 消</el-button>
                <el-button  @click="newLottoryItemOption()" type="primary" >确 定</el-button>
            </span>
        </el-dialog>

        <el-dialog
            title="彩票选项详情"
            :visible.sync="lottoryItemOptionDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="lotteryItemOption.text" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div>
                <el-form>
                 <el-form-item label="序号：" label-width="100px">
                    <el-input-number v-model="lotteryItemOption.order" size="small" :min="1" :step="1" :max="50"></el-input-number>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="lottoryItemOptionDialogVisible = false">取 消</el-button>
                <el-button @click="updateLotteryItemOption()">修 改</el-button>
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
            lottery: {},
            lotteryItems: [],
            lotteryItem: {},
            lotteryItemOptions: [],
            lotteryItemOption: {},
            lottoryDialogVisible: false,
            lottoryItemDialogVisible: false,
            lottoryItemOptionDialogVisible: false,
            newLottoryItemDialogVisible: false,
            newLottoryItemOptionDialogVisible: false,
            newLottoryDialogVisible: false,
        }
    },
    methods: {
        updateLotteryItemOption: function() {
            const loading = this.$loading({
                lock: true,
                text: '处理中...',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            axios
                .put('http://' + this.BASE_URL + '/api/v1/admins/123/lotteries/123/items/123/options/'+ this.lotteryItemOption.id, this.lotteryItemOption, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        loading.close()
                        this.$message("修改选项成功");
                        this.lottoryItemOptionDialogVisible = false
                    }
                )
                .catch(error => { 
                    loading.close()
                    console.log(error);
                    this.$message.error("修改选项失败");
                });
        },
        updateLotteryItem: function() {
            const loading = this.$loading({
                lock: true,
                text: '处理中...',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            axios
                .put('http://' + this.BASE_URL + '/api/v1/admins/123/lotteries/123/items/' + this.lotteryItem.id, this.lotteryItem, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        loading.close()
                        this.$message("修改项目成功");
                        this.lottoryItemDialogVisible = false;
                    }
                )
                .catch(error =>  { 
                    loading.close()
                    console.log(error);
                    this.$message.error("修改项目失败");
                });
        },
        updateLottery: function() {
            const loading = this.$loading({
                lock: true,
                text: '处理中...',
                spinner: 'el-icon-loading',
                background: 'rgba(0, 0, 0, 0.7)'
            });
            axios
                .put('http://' + this.BASE_URL + '/api/v1/admins/123/lotteries/'+ this.lottery.id, this.lottery, {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        loading.close()
                        this.$message("修改彩票成功");
                        this.lottoryDialogVisible = false;
                    }
                )
                .catch(error =>  { 
                    loading.close()
                    console.log(error);
                    this.$message.error("修改彩票失败");
                });
        },
        openNewLottoryDiag: function() {
            this.newLottoryDialogVisible = true;
        },
        openNewLottoryItemDialog: function(lotteryId) {
            this.lotteryItem = {
                "lottery": {"id": lotteryId}
            },
            this.newLottoryItemDialogVisible = true;
        },
        openNewLottoryItemOptionDialog: function(lotteryItemId) {
            this.lotteryItemOption = {
                "lotteryItem": {"id": lotteryItemId}
            },
            this.newLottoryItemOptionDialogVisible = true;
        },
        newLottory: function() {
            axios
                .post('http://' + this.BASE_URL + '/api/v1/admins/123/lotteries/', this.lottery, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("增加彩票成功");
                        let lottery = response.data
                        this.lotteries.push(lottery)
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
            this.newLottoryDialogVisible = false;
        },
        newLottoryItem: function() {
            axios
                .post('http://' + this.BASE_URL + '/api/v1/admins/123/lotteries/'+ this.lotteryItem.lottery.id + '/items', this.lotteryItem, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("增加项目成功");
                        let item = response.data
                        this.lotteryItems.push(item);
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
            this.newLottoryItemDialogVisible = false;
        },
        newLottoryItemOption: function() {
            axios
                .post('http://' + this.BASE_URL + '/api/v1/admins/123/lotteries/123/items/' + this.lotteryItem.id + '/options', this.lotteryItemOption, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("增加选项成功");
                        let option = response.data
                        this.lotteryItemOptions.push(option);
                    }
                )
                .catch(function (error) {
                    console.log(error);
                });
            this.newLottoryItemOptionDialogVisible = false;
        },
        openLottoryDiag: function (row) {
            this.lottery = row;
            this.lottoryDialogVisible = true;
            axios
                .get('http://' + this.BASE_URL + '/api/v1/lotteries/' + row.id + '/lottery-items')
                .then(response => {
                        this.lotteryItems = response.data;
                    })
                .catch(function (error) { 
                    console.log(error);
            });
        },
        openLottoryItemDiag: function (row) {
            this.lotteryItem = row;
            this.lottoryItemDialogVisible = true;
            axios
                .get('http://' + this.BASE_URL + '/api/v1/lotteries/' + row.lottery.id + '/items/' + row.id + '/options')
                .then(response => {
                        this.lotteryItemOptions = response.data;
                    })
                .catch(function (error) { 
                    console.log(error);
            });
        },
        openLottoryItemOptionDiag: function (row) {
            this.lotteryItemOption = row;
            this.lottoryItemOptionDialogVisible = true;
        }
    },
    mounted () {
        axios
            .get('http://' + this.BASE_URL + '/api/v1/lotteries?page=1&size=20')
            .then(response => {
                    this.lotteries = response.data.content;
                })
            .catch(function (error) { 
                console.log(error);
            });

    }
}
</script>

<style>

</style>