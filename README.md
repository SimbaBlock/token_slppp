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
   		"utxo":[
   			{
   				"address":"1GpSaVNQUaMeikRSY1RAKQvJh5jbbU8wAx",
   				"addresspos":0,
   				"n":1,
   				"txid":"33be6018671fa59515a234a4852sad9e8352d166d578f90a5e001fa2deeefbad6bcc51",
   				"value":"0.000135"
   			}
   		]
   	 },
   	 "msg":""
    }
    
#查询用户TokenUTXO:
    
    URL:/rest/Api/getTokenUtxo
    传参：
    	 address		string
    
    返回结果：
    {
       	"code":200,
       	"data":{
       		"utxo":[
       			{
       				"address":"1GpSaVNQUaMeikRSY1RAKQvJh5jbbU8wAx",
       				"addresspos":0,
       				"n":1,
       				"txid":"33be6018671fa59515a234a4852sad9e8352d166d578f90a5e001fa2deeefbad6bcc51",
       				"value":"0.000135"
       			}
       		]
       	 },
       	 "msg":""
     }

#查询地址历史交易记录:

    URL:/rest/Api/getTokenHistory
    传参：
	    address		string

    返回结果：   （status (0：发行，1：增发，2：交易，3：销毁)）  （type(2,打出去的钱 1,收到的钱)）
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








