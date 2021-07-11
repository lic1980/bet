<template>
    <div>
        <el-form :model="agentForm" :rules="rules" label-width="80px" >
            <el-form-item label="电话" prop="tel" required>
                <el-input v-model="agentForm.tel" maxlength="11"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password" required>
                <el-input v-model="agentForm.password" show-password></el-input>
            </el-form-item>
            
            <el-form-item  >
                <el-button type="primary" @click="login()">登录</el-button>
                <!--router-link to="/register">
                    <el-button style="margin-left:7px;" type="primary">注册</el-button>
                </router-link-->
            </el-form-item>
        </el-form>
    </div>
</template>

<script>
import axios from 'axios'
import '../common/const.js'

export default {
    data() {
      return {
            agentForm: {
                tel: "",
                password: ""
            },
            rules: {
                tel: [],
                password: []
            }
        }
    },
    methods: {
        login: function() {
            let data = {
                "tel": this.agentForm.tel,
                "plainPassword": this.agentForm.password
            };
            axios
                .post('http://' + this.BASE_URL + '/api/v1/security-resources/agents/sessions', data, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        sessionStorage.setItem(global.AGENT_ID_KEY, response.data.id);
                        this.$router.push({path: '/customers'});
                        }
                )
                .catch(error=> { 
                    if (error.response.status == 400) {
                        this.$message.error("登陆信息错误");
                    } else {
                        this.$message.error("登陆服务器错误");
                    }
                    console.log(error);
                });
        }
    }
}
</script>

<style>
ul {
    list-style: none;
    line-height: 30px;
    margin-left: 5px;
}
li {
    display: block;
    float: left;
    margin-left: 5px;
}
</style>