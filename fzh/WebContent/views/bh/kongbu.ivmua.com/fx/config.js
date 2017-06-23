	
	/**获取配置*/
	function getConfig(){
		$.post("http://fzh2014.duapp.com/fzh/getparam?jsoncallback=?",
			{appid: window.shareData.appid, url: window.location.href.split('#')[0], method: "ticket"},
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
		  jsApiList: window.shareData.jsApiList
		});
		wx.ready(function(){
			wx_share();
		});
	}
	
	window.shareData = {
	   "appid": "",
	   "param": "",
	   "jumpUrl": "",
	   "jsApiList": "",
       "imgUrl": "http://fzh2014.duapp.com/uploadimages/wally.jpg",
       "sendFriendLink": window.location.href,
       "tTitle": "",
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
			location.href = window.shareData.jumpUrl;
		  },
		  cancel: function (res) {
			  alert("亲，这么好的东西怎么能不分享给好朋友呢！");
		  },
		  fail: function (res) {
			  alert("分享失败，可能是网络问题，一会儿再试试？");
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
			location.href = window.shareData.jumpUrl;
		  },
		  cancel: function (res) {
			  alert("亲，这么好的东西怎么能不分享给好朋友呢！");
		  },
		  fail: function (res) {
			  alert("分享失败，可能是网络问题，一会儿再试试？");
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
		  },
		  success: function (res) {
			location.href = window.shareData.jumpUrl;
		  },
		  cancel: function (res) {
			//alert('已取消');
		  },
		  fail: function (res) {
			//alert(JSON.stringify(res));
		  }
	   });
	  //隐藏菜单
	  wx.hideMenuItems({
	      menuList: [
	        'menuItem:openWithQQBrowser', // 在QQ浏览器中打开
	        'menuItem:openWithSafari', // 在Safari中打开
	        'menuItem:copyUrl' // 复制链接
	      ]
	  });
	}