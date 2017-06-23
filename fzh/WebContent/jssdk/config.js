	var jump_url = "http://mp.weixin.qq.com/s?__biz=MjM5MjM3OTY1Mw==&mid=202150296&idx=1&sn=a4513b734621f4d2e0fb3bbcc1e2bb0b#rd";
		
	/**获取配置*/
	function getConfig(){
		$.post("http://fzh2014.duapp.com/fzh/getparam?jsoncallback=?",
			{appid: "wxdd5dc1ca73d8986d", url: window.location.href.split('#')[0], method: "ticket"},
			function(data){
				setConfig(data);
			}
		,"jsonp");
	}
	/**配置JS-SDK*/
	function setConfig(config){
		wx.config({
		  debug: false,
		  appId: config.appId,
		  timestamp: config.timestamp,
		  nonceStr: config.nonceStr,
		  signature: config.signature,
		  jsApiList: [
			'checkJsApi',
			'onMenuShareTimeline',
			'onMenuShareAppMessage',
			'onMenuShareQQ',
			'onMenuShareWeibo',
			'hideMenuItems',
			'showMenuItems',
			'hideAllNonBaseMenuItem',
			'showAllNonBaseMenuItem',
			'translateVoice',
			'startRecord',
			'stopRecord',
			'onRecordEnd',
			'playVoice',
			'pauseVoice',
			'stopVoice',
			'uploadVoice',
			'downloadVoice',
			'chooseImage',
			'previewImage',
			'uploadImage',
			'downloadImage',
			'getNetworkType',
			'openLocation',
			'getLocation',
			'hideOptionMenu',
			'showOptionMenu',
			'closeWindow',
			'scanQRCode',
			'chooseWXPay',
			'openProductSpecificView',
			'addCard',
			'chooseCard',
			'openCard'
		  ]
		});
		//默认分享设置
		wx.ready(function(){
			wx_share();
		});
	}
	
	window.shareData = {
       "imgUrl": "http://fzh2014.duapp.com/uploadimages/wally.jpg",
       "sendFriendLink": window.location.href,
       "tTitle": 'fzhwx开发者-新闻阅读',
       "tContent": "这里每天都有最新的新闻资讯！"
    };
	/**分享*/
	function share(id, title){
		window.shareData.tTitle = title;
		window.shareData.tContent = "我要第一个分享给你好看的文章 - fzhwx开发者";
		window.shareData.sendFriendLink = "http://zhihui2014.sinaapp.com/page/artinfo.jsp?id="+id;
		wx_share();
		alert('请点右上角分享给朋友或朋友圈！');
	}
	
	/**自定义分享*/	
	function wx_share(){
	  // 2.1 监听“分享给朋友”，按钮点击、自定义分享内容及分享结果接口
	  wx.onMenuShareAppMessage({
		  title: window.shareData.tTitle,
		  desc: window.shareData.tContent,
		  link: window.shareData.sendFriendLink,
		  imgUrl: window.shareData.imgUrl,
		  trigger: function (res) {
			//alert('用户点击发送给朋友');
		  },
		  success: function (res) {
			//alert('已分享');
			location.href = jump_url;
		  },
		  cancel: function (res) {
			//alert('已取消');
			location.href = jump_url;
		  },
		  fail: function (res) {
			//alert(JSON.stringify(res));
		  }
	  });

	  // 2.2 监听“分享到朋友圈”按钮点击、自定义分享内容及分享结果接口
	  wx.onMenuShareTimeline({
		  title: window.shareData.tTitle,
		  desc: window.shareData.tContent,
		  link: window.shareData.sendFriendLink,
		  imgUrl: window.shareData.imgUrl,
		  trigger: function (res) {
			//alert('用户点击分享到朋友圈');
		  },
		  success: function (res) {
			//alert('已分享');
		  },
		  complete: function (res) {
			location.href = jump_url;
		  },
		  fail: function (res) {
			//alert(JSON.stringify(res));
		  }
	  });
	  // 2.3 监听“分享到QQ”按钮点击、自定义分享内容及分享结果接口
	  wx.onMenuShareQQ({
		  title: window.shareData.tTitle,
		  desc: window.shareData.tContent,
		  link: window.shareData.sendFriendLink,
		  imgUrl: window.shareData.imgUrl,
		  trigger: function (res) {
			//alert('用户点击分享到QQ');
		  },
		  complete: function (res) {
			//alert(JSON.stringify(res));
		  },
		  success: function (res) {
			//alert('已分享');
		  },
		  cancel: function (res) {
			//alert('已取消');
		  },
		  fail: function (res) {
			//alert(JSON.stringify(res));
		  }
	   });
	}