	var imgUrl = "", tTitle = "", collsetId = 0, minTime = new Date().getTime(), type = getParam("type")==null?0:getParam("type");
	
	/**查询文章*/
	function getArt(){
		$.get(
			"http://zhihui2014.sinaapp.com/article?jsoncallback=?",
			{flag : "getArt", id : getParam("id")},
			function(data){
				var imgUrl = "";
				$("h2[class=mg]").html(data.title);
				document.getElementsByTagName("title")[0].innerHTML = data.title;
				$("#pv").html("&nbsp;浏览：" + data.pv);
				$("#cont").html(data.content);
				$("span[class=tf]").html(data.time);
				imgUrl = data.imgurl;
				if(imgUrl.length !=0){
					$("#cont").prepend('<img width="100%" src="' + imgUrl + '"><p>').trigger("create");
					$("#cont").append('<p><a href="javascript:get_More();" data-role="button" data-icon="info">查看更多</a>').trigger("create");
				}
			}
		,"jsonp");
	}
	
	/**获取更多*/
	function get_More(){
		location.href = "http://zhihui2014.sinaapp.com/page/article.htm?type=" + getParam("type");
	}
	
	/**获取URL参数*/
	function getParam(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
		var r = window.location.search.substr(1).match(reg);
		if (r != null)
		return unescape(r[2]);
		return null;
	}
	
	/**设置标题*/
	function setTitle(){
		if(type=="sinaent"){
        	tTitle = "fzhwx开发者-娱乐频道";
    	}else if(type=="sinaworld"){
        	tTitle = "fzhwx开发者-国际频道";
    	}else if(type=="sinatech"){
       	 	tTitle = "fzhwx开发者-科技频道";
    	}else if(type=="sinaedu"){
       	 	tTitle = "fzhwx开发者-教育频道";
    	}else if(type=="sinababy"){
       	 	tTitle = "fzhwx开发者-育儿频道";
    	}else if(type=="sales"){
       	 	tTitle = "fzhwx开发者-电商频道";
    	}else{
    		tTitle = "fzhwx开发者-财经频道";
    	}
    	document.title = tTitle;
		window.shareData.tTitle = tTitle;
	}
	
	/**获取数据*/
	function getData(collsetId){
		$.ajax({
			url : "http://localhost:8080/fzh/article?jsoncallback=?",
			type : "GET",
			data : {
				minTime : minTime, type : type, flag : "getArts"
			},
			dataType : "jsonp",
			success : function(data) {
				$(data).each(function(i){
					var num = $('#num').val();
					if(collsetId==0)num = 0;
					num = parseInt(num) + i + 1;
					var art = this.id + "$" + this.imgurl;
					
					var colls = "<div data-role=\"collapsible\" style=\"color: #449934;\" data-content-theme=\"d\" onclick=\"clkCos(this, '"+ art + "')\">"
						 	  + '<h3>' + num + '. ' + this.title+'</h3>'
						      + '<font id=\"cont' + this.id + '\" size=\"4px\" style=\"line-height: 20pt;\">' + this.content + '</font>'
						      + "<a href=\"javascript:share('"+ this.id + "', '"+ this.title + "')\" data-role=\"button\"  data-icon=\"forward\">分享此文</a></div>";
					$("#c" + collsetId).append(colls).trigger("create");//创建分享按钮
					if (i == data.length - 1) {
						$('#num').val(num);
						minTime = this.time;
					}
				});
				$("#c" + collsetId).collapsibleset("refresh");
				if(collsetId==0){
					var more = '<a href="javascript:getMore();" id="more" data-role="button" data-icon="info">加载更多</a>';
					$("#c").append(more).trigger("create");
				}
			},
			error : function(a, b, c) {
				//alert("error==" + b);
				alert('没有更多了!');
			}
		});
	}
	
	/**滚动定位*/
	function clkCos(obj, art) {
		var arr = art.split("$");
		imgUrl = arr[1];
		$(document).scrollTop(parseInt(obj.offsetTop) - 50);
		var html = $("#cont"+arr[0]+":has(img)");
		if(html.size()==0 && imgUrl.length !=0){
			$("#cont"+arr[0]).prepend('<img width="100%" src="' + imgUrl + '"><p>').trigger("create");
		}
	}

	/**更多数据*/
	function getMore() {
		collsetId++;
		if (collsetId > 4) {
			alert('没有更多了!');
			return;
		}
		getData(collsetId);
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
		if(imgUrl.length !=0)window.shareData.imgUrl = imgUrl;
		window.shareData.tContent = "我要第一个分享给你好看的文章 - fzhwx开发者";
		window.shareData.sendFriendLink = "http://zhihui2014.sinaapp.com/page/artinfo.jsp?id="+id;
		wx_share();
		alert('请点右上角分享给朋友或朋友圈！');
	}
	/*
    document.addEventListener('WeixinJSBridgeReady', function onBridgeReady() {
        WeixinJSBridge.on('menu:share:appmessage', function (argv) {
            WeixinJSBridge.invoke('sendAppMessage', {
                "img_url": window.shareData.imgUrl,
                "img_width": "640",
                "img_height": "640",
                "link": window.shareData.sendFriendLink,
                "desc": window.shareData.tContent,
                "title": window.shareData.tTitle
            }, function (res) {
               //_report('send_msg', res.err_msg);
			   //window.location.href = jump_url;
            })
        });
        WeixinJSBridge.on('menu:share:timeline', function (argv) {
            WeixinJSBridge.invoke('shareTimeline', {
                "img_url": window.shareData.imgUrl,
                "img_width": "640",
                "img_height": "640",
                "link": window.shareData.sendFriendLink,
                "desc": window.shareData.tContent,
                "title": window.shareData.tTitle
            }, function (res) {
                //_report('timeline', res.err_msg);
				//location.href = jump_url;
            });
        });
        WeixinJSBridge.on('menu:share:weibo', function (argv) {
            window.shareData.tTitle = "我要第一个分享给你好看的文章！";
            window.shareData.tContent = "这里每天都有最新的新闻资讯！";
            WeixinJSBridge.invoke('shareWeibo', {
                "content": window.shareData.tContent,
                "url": window.shareData.sendFriendLink,
            }, function (res) {
                //_report('weibo', res.err_msg);
            });
        });
        }, false)
		*/
	
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
		  cancel: function (res) {
			//alert('已取消');
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