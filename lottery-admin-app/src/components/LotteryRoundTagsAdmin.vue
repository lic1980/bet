<template>
    <div>
        <div>
            <div>
                <el-button type="small" @click="newlottoryRoundTagDialogVisible = true">增加标签</el-button>
            </div>
        </div>
        <div>
            <div>标签列表：</div> 
            <div>
                <el-table :data="tags" style="width: 100%">
                    <el-table-column prop="id" label="ID">
                    </el-table-column>   
                    <el-table-column prop="text" label="名字">
                    </el-table-column>
                    <el-table-column label="操作">
                        <template slot-scope="scope">
                            <el-button
                            size="mini"
                            @click="deleteTag(scope.row)">删除</el-button>
                        </template>
                    </el-table-column>
                </el-table>
            </div>
        </div>
        <el-dialog
            title="新增标签"
            :visible.sync="newlottoryRoundTagDialogVisible"
            width="550px">
            <div>
                <el-form>
                 <el-form-item label="名字：" label-width="100px">
                    <el-input v-model="tagText" size="small" ></el-input>
                 </el-form-item>
                </el-form>
            </div>
            <div style="text-align: center;" class="dialog-footer">
                <el-button @click="newlottoryRoundTagDialogVisible = false">取 消</el-button>
                <el-button @click="addTag()">增 加</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import axios from 'axios'
export default {
    data () {
        return {
            tags: [],
            tagText: "",
            newlottoryRoundTagDialogVisible: false
        }
    },
    methods: {
        addTag: function() {
            let tag = {
                text: this.tagText,
            };
            axios
                .post('http://localhost:8080/api/v1/admins/123/lotteries/rounds/tags', tag, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        this.$message("增加标签成功");
                        this.tags.push(response.data);
                        this.newlottoryRoundTagDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
        deleteTag : function(row) {
            axios
                .delete('http://localhost:8080/api/v1/admins/123/lotteries/rounds/tags/' + row.id,  {headers: {'Content-Type': 'application/json'}})
                .then(
                    () => {
                        this.$message("删除标签成功");
                        let tmp = [];
                        for(var i = 0; i < this.tags.length; i++) {
                            if (this.tags[i].id != row.id){
                                tmp.push(this.tags[i]);
                            }
                        }
                        this.tags = tmp;
                        this.newlottoryRoundTagDialogVisible = false;
                    }
                )
                .catch(function (error) { 
                    console.log(error);
                });
        },
    },
    mounted () {
        axios
            .get('http://localhost:8080/api/v1/lotteries/rounds/tags?page=1&size=20')
            .then(response => {
                    this.tags = response.data.content;
                })
            .catch(function (error) { 
                console.log(error);
            });

    }
}
</script>

<style>

</style>