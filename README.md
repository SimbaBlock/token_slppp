通用：
		
	错误码：
	{"code":"400","data":"","msg":"请求数据格式不正确"}
	{"code":"500","data":"","msg":"服务器异常"}
	{"code":"4000001","data":"","msg":"这个token不存在"}
	{"code":"4000002","data":"","msg":"当前地址钱不够"}
	{"code":"4000003","data":"","msg":"当前token不存在"}
	{"code":"4000004","data":"","msg":"无增发权限"}
	{"code":"4000005","data":"","msg":"地址没有认证"}
	{"code":"4000006","data":"","msg":"认证地址重复"}
	{"code":"4000007","data":"","msg":"数值非法符号或太大"}
	{"code":"4000008","data":"","msg":"用户充值失败"}
	{"code":"4000009","data":"","msg":"重复增发"}
	{"code":"4000010","data":"","msg":"系统地址utxo不足"}
	{"code":"4000012","data":"","msg":"签名错误"}
	{"code":"4000013","data":"","msg":"上链失败"}


#上链:  

    URL:/rest/Api/broadcastTx
    传参:
    {
    	"hex": "0200000002aa8f24d4548a90ec3871a00e80049a27f46405"
    }

    返回结果：
    {
    	"code": 200,
    	"msg": "",
    	"data": {
    		"txid": "sadjlaslkjdalskjdalksjdaksldasdas"
    	}
    }

#用户kyc:

    URL:/rest/Api/kyc
    传参：
    {
	    "address": "18RnWjAjSjYwVC7juVypnsUAmvNGMJ72vH",
	    "name": "利斯海",
	    "ID_number": "321312312312312312312312312" 
    }
    
    返回结果：
    {
    	"code": 200,
    	"msg": "",
    	"data":""
    }


#查询用户tonken:

    URL:/rest/Api/queryToken
    传参：
	    tokenId		string
	    address		string

    返回结果：
    {
    	"code": 200,
    	"msg": "",
    	"data": {
    		"token": 23423423423423,
    		"precition": 8
    	}
    }

#查询用户UTXO:

    URL:/rest/Api/getUtxo
    传参：
	    address		string

    返回结果：
    {
        "code":200,
        "data":{
            "utxo":"[[\"31e547eece313dc444a5c533abeb45398999ba8ca00d7ae310fd42e80b23b883\",0,\"7.998562\"],[\"4a9d49191d710db535cdb61b0496393d7e59d7c347e119975c30f40f93f40ba5\",1,\"2.6e-05\"]]"
        },
        "msg":""
    }
    
#查询用户TokenUTXO:
    
    URL:/rest/Api/getUtxo
    传参：
    	 address		string
    
    返回结果：
    {
        "code": 200,
        "data": {
            "utxo": "[[\"01c2b75e7e35ffb41965dfd458115fc2234afd232e5ac00c93d784c9829a87d8\",0,\"2.8e-05\",\"76a9140e406c10d0315942e442946661a0931ce7181fab88ac6a06534c502b2b0001010453454e44207ee7a38340fd4fa5a14c9f5f3dc47f1e68a9534af5b17d43ba92dc0cdadda2b20800005af3107a4000\"],[\"1b310416fcef9757ebddbde68310e26b6500b9871b45004aa93fdb1f786ca419\",0,\"5.3e-05\",\"76a9140e406c10d0315942e442946661a0931ce7181fab88ac006a04534c500001010747454e45534953045553445423546574686572204c74642e20555320646f6c6c6172206261636b656420746f6b656e734168747470733a2f2f7465746865722e746f2f77702d636f6e74656e742f75706c6f6164732f323031362f30362f546574686572576869746550617065722e70646620db4451f11eda33950670aaf59e704da90117ff7057283b032cfaec77793139160108010208002386f26fc10000\"]]"
        },
        "msg": ""
    }

#查询地址历史交易记录:

    URL:/rest/Api/getTokenHistory
    传参：
	    address		string

    返回结果：   （status (0：发行，1：增发，2：交易)）  （type(2,打出去的钱 1,收到的钱)）
   {
   	"code":200,
   	"data":{
   		"total":"23",
   		"history":[
   			{
   				"fromAddress":"602c8763be74d22f96c43d3f3f965171892df641",
   				"status":1,
   				"time":1588654538,
   				"toAddress":"c079c08dd91583a5a48786f3b9da08893b3687ca",
   				"token":100000000000000,
   				"type":1
   			},
   			{
   				"fromAddress":"602c8763be74d22f96c43d3f3f965171892df641",
   				"status":1,
   				"time":1588654538,
   				"toAddress":"c079c08dd91583a5a48786f3b9da08893b3687ca",
   				"token":1000000000000000,
   				"type":1
   			},
   			{
   				"fromAddress":"602c8763be74d22f96c43d3f3f965171892df641",
   				"status":1,
   				"time":1588654538,
   				"toAddress":"c079c08dd91583a5a48786f3b9da08893b3687ca",
   				"token":100000000000000,
   				"type":1
   			}
   		],
   		"page":"3"
   	},
   	"msg":""
   }








