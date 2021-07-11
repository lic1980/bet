<template>
    <div>
        <el-form :model="customerForm" :rules="rules" label-width="80px" >
            <el-form-item label="电话" prop="tel" required>
                <el-input v-model="customerForm.tel" maxlength="11"></el-input>
            </el-form-item>
            <el-form-item label="密码" prop="password" required>
                <el-input v-model="customerForm.password" show-password></el-input>
            </el-form-item>
            <el-form-item  >
                <el-button type="primary" @click="login()">登录</el-button>
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
            customerForm: {
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
                "tel": this.customerForm.tel,
                "plainPassword": this.customerForm.password,
                "agent.id": null,
            };
            axios
                .post('http://' + this.BASE_URL + '/api/v1/security-resources/customers/sessions', data, {headers: {'Content-Type': 'application/json'}})
                .then(
                    response => {
                        sessionStorage.setItem(global.CUSTOMER_ID_KEY, response.data.id);
                        this.$router.push({path: '/admin'});
                        }
                )
                .catch(error => { 
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