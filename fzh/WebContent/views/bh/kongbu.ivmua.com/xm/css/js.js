// JavaScript Document

function convert(nOption){
	if(txt.value=='' || txt.value.length==1){
		alert("要输入正确的姓名哦~");
		txt.value='';
		txt.focus();
		return false;
	}
	name = name.replace("name", txt.value);
	dianjsq=''; 
	dian();
	var diandian="分析中";
	function dian(){
		diandian+=".";
		$("#tijiao").val(diandian);
		dianjsq = setTimeout(dian,270);
		if($("#tijiao").val()=="分析中..."){
			diandian="分析中";
		}
	}
	
	//滚动
	ggdd=0;
	gdjsq='';
	function gd(){
		ggdd+=1;
		$(".jieguobankuai").scrollTop(ggdd);
		gdjsq=setTimeout(gd,70);
		if(ggdd>=(gao-115)){
			$(".jieguobankuai").scrollTop(0);
			clearTimeout(gdjsq);
		}
	}
	
	setTimeout(function(){
		clearTimeout(dianjsq);
		$(".ym1").fadeOut(20,function(){
			window.scrollTo(0,0);
			$(".ym2").fadeIn(50);
			//页面出现后
			gao=$("#gundong").height();
			share();//分享
			setTimeout(function(){
				gd();
			},1000);
		});
	},2500);
	
	if(txt.value.length==2){
		$("#jian_xing").text(txt.value.substr(0,1));
		$("#jian_min1").text(txt.value.substr(1,1));
		$("#jian_min2,#fan_min2,#wuxing_min3,.bihuabox").fadeOut(0);
	}
	if(txt.value.length==3){
		$("#jian_xing").text(txt.value.substr(0,1));
		$("#jian_min1").text(txt.value.substr(1,1));
		$("#jian_min2").text(txt.value.substr(2,1));
	}
	if(txt.value.length==4){
		$("#jian_xing").text(txt.value.substr(0,2));
		$("#jian_min1").text(txt.value.substr(2,1));
		$("#jian_min2").text(txt.value.substr(3,1));
	}
		
	//下面开始计算笔的画数		
	bihuas();
}

var zongping=Array(
'',
'',
'天地开泰万事成，身体康安亦富荣。否泰名誉兼享福，一生无忧乐绵长。（大吉）',
'进取富贵又如意，智达明敏扬名威。名利寿福皆此得，前途光茫好鸿图。（大吉）',
'福禄寿长阴阳和，心身健全是英豪。名利双收富荣达，乃是世上福德人。（大吉）',
'安稳余庆福禄开，盛大幸福天赐来。内含衰兆应谨慎，注意品行永免灾。（吉）',
'刚毅果断除万难，独立权威志气安。内外和好兼柔性，温和养德耀吉星。（吉）',
'意志坚刚善恶明，富进取心求和平。忍耐克己如心意，前难后成可安然。（吉）',
'挽回家运矣春光，顺调发展财辉煌。温和笃实阴阳合，稳健顺调得人望。（大吉）',
'智略超群博学多，善处事务亦忍和。功业成就得富荣，艺才相身乐千钟。（大吉）',
'福寿拱照德望高，财子寿全又温和。慈祥好善可恭敬，富贵繁荣得富荣。（大吉）',
'贵人得助天乙扶，为人之上有财富。众望所归事业成，不可贪色保安宁。（大吉）',
'突破万难权威高，刚性固执如英豪。须事谨慎守和平，可得大功奏业成。（吉）',
'明月光照乐依依，俟如梅花待放时。兴家立业名利全，各自独立有权威。（大吉）',
'旭日东升势壮富，贯彻大业万事胜。终至荣达功名显，猛虎添翼势声强。（大吉）',
'家门余庆福无强，子孙繁荣富贵强。白手成家立大业，四方财源皆广进。（大吉）',
'资性英敏有奇能，怪癖不和害前程。修身涵养与仁和，奏功获得大运图。（吉）',
'智勇得志意气新，建立声誉事业兴。终到富贵福禄奏，为人领导德望高。（大吉）',
'温和平安好艺才，努力前途福将来。文笔奇才仁德高，守贞勤俭耐刻苦。（吉）',
'权威显达得众望，忠实热诚运极旺。大德奏功无难事，终得富荣乐千钟。（吉）',
'意志薄弱无威望，长于技艺得长风。名可得而利难获，贯彻努力成大功。（吉）',
'富贵荣华实可当，光明荣达好儿郎。家门隆昌福万千，世代子孙个个贤。（大吉）',
'德望高大名誉振，才谋健全财源进。富贵荣华福禄至，前途洋洋得意真。（大吉）',
'新生泰运顺行舟，排除万难总无忧。成功繁荣四海明，荣华富贵好前途。（吉）',
'开花结子衣食足，大业奏功可庆祝。子孙繁荣多快乐，一家圆满庆有余。（大吉）',
'有德且智德望高，堪为顾问得仁和。名利双收天赋富，威望荣达世间夸。（大吉）',
'先见机明察佳期，意志坚固好运时。功名利达福禄全，一世荣隆乐绵绵。（大吉）',
'寒雪青松性刚强，一度祸难过灾殃。将此发达利亨通，晚景繁荣福无强。（吉）',
'先苦后甘费心思，难关渡过福人扶。终逢兴隆乐余庆，恰如甘蔗好尾甘。（吉）',
'富贵荣达得显宁，子孙繁茂福绵绵。一身平安富益寿，福禄双全享千钟。（大吉）',
'富贵长寿逢吉祥，家运隆昌喜气扬。福禄繁荣兼富贵，万事通达实贵重。（吉）',
'利路享通万事成，和畅逍达四海明。家运隆盛招富贵，万商云集得繁荣。（大吉）',
'兴家立业意志强，智虑周密名望扬。志操坚固信用重，一生艺才献机能。（大吉）',
'志高力微乏实行，妄谋无计事难成。晚年安宁静逸祥，享得天赋增吉相。（吉）',
'还原复始重临福，相等一数再开泰。家门隆昌万事成，名扬四海庆余祥。（大吉）'
);

var shiyesz = Array(
'事业会遇到坎坷，由于努力奋斗的结果，社会地位大大提高，即使没有获得很大的成功，也有值得满足的幸运来临。',
'早年辛勤收获不是很大，中年可一展抱负，会有贵人相助，事半功倍！',
'在事业上，易走弯路，不过后期收获会很大。急事缓办，凡事欲速则不达，宜先思考计划后再行动，可避免很多不必要的过失！'
);

var jiatingsz = Array(
'春暖花开，家庭圆满、和睦，夫妻恩爱、温馨。',
'会有与家人意见不一的情况，但能及时理性地解决。夫妻感情很和谐，子女乖巧。',
'同心力稍欠，有很强的能力去化解，最终事顺人心。'
);

var jiankangsz = Array(
'抵抗力超常人，很强，一生中不会有大的疾病，身心健康。',
'表面乐观，内心时有不安，但影响不大，身体健康不会有大病。',
'有些操心的事，应注重精神修养，培养耐力及定力。'
);

var xinggesz = Array(
'认真、负责，有领袖人物的气质，说话圆融，很有爱心，对於财务及家庭问题，有着独到的见解，是一个才艺洋溢的人。',
'容易亲近，温和沉着，有雅量，对人有同情心，荣誉心稍强。其内心有刚义之肠，却不显现于外表。易结交真情朋友。',
'活动力强，生性好动。有智慧、理性。执行力强，对权誉有比较高的追求，善于思考。应注意勿过爱权财。'
);

function jieguopingjia(){
	//总的评价
	zpwz="";
	zi="";
	if(zhuyubihua<zongping.length){
		zi=zongping[zhuyubihua];
		for(zis=0;zis<zi.length;zis++){
			zpwz+="<span class='tianzi'>"+zi.charAt(zis)+"</span>";
		}
		$("#shuaiji").html(zpwz);
		}else {
			//$("#shuaiji").text(zongping[zongping.length-1]);
			zi=zongping[zongping.length-1]
			for(zis=0;zis<zi.length;zis++){
				zpwz+="<span class='tianzi'>"+zi.charAt(zis)+"</span>";
			}
			$("#shuaiji").html(zpwz);
			}
	
	//其他评
	if(wuxings>6){
		$("#shiye").text(shiyesz[0]);
		$("#jiating").text(jiatingsz[0]);
		$("#jiankang").text(jiankangsz[0]);
		$("#xingge").text(xinggesz[0]);
		}else if(wuxings>3){
			$("#shiye").text(shiyesz[1]);
			$("#jiating").text(jiatingsz[1]);
			$("#jiankang").text(jiankangsz[1]);
			$("#xingge").text(xinggesz[1]);
		}else if(wuxings>=0){
			$("#shiye").text(shiyesz[2]);
			$("#jiating").text(jiatingsz[2]);
			$("#jiankang").text(jiankangsz[2]);
			$("#xingge").text(xinggesz[2]);
		}
		fxfx();
	}
//分享
$(".ym1").height($(".ym2").height());
$(".fxts").height($(".ym2").height());
$(".fxpy").click(function(){
	window.scrollTo(0,0);
	$(".fxts").fadeIn(20);
})
$(".fxts").click(function(){
	$(this).fadeOut(10);
})
