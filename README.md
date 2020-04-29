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

#创建token:

    URL:/rest/Api/issueToken
  
    传参:
    {
	    "short_name": "ABC",
	    "full_name": "ABC is nice",
	    "url": "http://www.baidu.com",
	    "hash": "hashhahahahahahahahahahahahahah",
	    "precition": "8",
	    "quantity": "1000000000000000",
	    "minter_address": "19VvkL2jZctXr7NQEWwuSyXMAWGzMCzFba",
	    "issuer_address": "1LQ5kvQUoE1iat3aVkjRkmj6nwvntkgz1m"
    }
    
    返回结果：
    {
    	"code": 200,
    	"msg": "",
    	"data": {
    		"hex": "sadjlaslkjdalskjdalksjdaksldasdas"
    	}
    }
 
#token增发:   

    URL:/rest/Api/mintToken
  
    传参:
    {
	    "token_id": "ad0ed3f85abccde1785f8190e9c4668ff099174f298085a92a06600dd6ba39a3",
	    "mint_total": "10000000000000000",
	    "minter_address": "1ApUhRDmUKi2oHkS9K9nXQ36uwnPXz1Hei",
	    "issuer_address": "19VvkL2jZctXr7NQEWwuSyXMAWGzMCzFba"
    }

    返回结果：
    {
    	"code": 200,
    	"msg": "",
    	"data": {
    		"hex": "sadjlaslkjdalskjdalksjdaksldasdas"
    	}
    }

#发送:  

    URL:/rest/Api/sendToken
    传参:
    {
	    "token_id": "ad0ed3f85abccde1785f8190e9c4668ff099174f298085a92a06600dd6ba39a3",
	    "transfer_to": [{
		  "account_address": "1HmiHiDqBuumMXpGYdVAWvgi21DzQV91C8",
		  "amount": 1
	  }, {
		    "account_address": "1AN9P63fc93v2wpH261Vyjrioq33W7p79e",
		    "amount": 1
	    }],
	    "transfer_from_address": "19VvkL2jZctXr7NQEWwuSyXMAWGzMCzFba"
    }

    返回结果：
    {
    	"code": 200,
    	"msg": "",
    	"data": {
    		"hex": "sadjlaslkjdalskjdalksjdaksldasdas"
    	}
    }

#上链:  

    URL:/rest/Api/broadcastTx
    传参:
    {
    	"hex": "0200000002aa8f24d4548a90ec3871a00e80049a27f46405",
	    "type": "send"			// mint // issue
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

#查询所有token列表:

    URL:/rest/Api/queryToken
    传参：
	    page		Integer     
	  
    返回结果：
    {
    	"code":200,
    	"data":{
    		"total":"2",
    		"page":"1",
    		"list":[
    			{
    				"initIssueAddress":"12SL22kcHRLEcscSBzD3KdALhWZLaUk25d",
    				"initialTokenMintQuantity":"1000000000",
    				"mintBatonVout":2,
    				"originalAddress":"17vVkTTvyMGjSma1wyTdyofn5iQu6SbHXn",
    				"tokenDecimal":8,
    				"tokenDocumentHash":"122dfqwcq",
    				"tokenDocumentUrl":"asfjeimndew",
    				"tokenName":"youafe",
    				"tokenTicker":"yuio",
    				"transactionType":"GENESIS",
    				"txid":"c368a2bf7ed51fe99f0c0f7609ecabea04ee61b7f09c93271782968dd5cc9463"
    			},
    			{
    				"initIssueAddress":"1Cuk5j8B81pvvCb9BgFnT9JewwDZvNwRN4",
    				"initialTokenMintQuantity":"100000000000000000",
    				"mintBatonVout":2,
    				"originalAddress":"1D1CCx6Gskto3eFC7u7ydbngM8iGwwQRcv",
    				"tokenDecimal":8,
    				"tokenDocumentHash":"hashhahahahahahahahahahahahahah",
    				"tokenDocumentUrl":"http://www.baidu.com",
    				"tokenName":"BTC is nice",
    				"tokenTicker":"BTC",
    				"transactionType":"GENESIS",
    				"txid":"f09ec348b232ce8fc8d7505d150c3960946daf43625233b673454dcc6e463275"
    			}
    		]
    	},
    	"msg":""
    }





