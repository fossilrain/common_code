/*
需要先引入jQuery插件
为一指定元素添加滤镜
	参数：元素的id
*/
function Filter(){
	this.filter_id="";
	this.target_id="";
	this.__css="";
}
/*diy参数说明 为一json格式数据
	{
		position:"",//定位[fixed/absolute 默认为fixed]
		background_color:"",//背景 默认为gray
		filter:""//滤镜透明度 默认为0.3 请用小数
	}*/
Filter.prototype.addFilter=function(id,diy){
	this.target_id=id;
	var target=document.getElementById(id);
	var offset_left=target.offsetLeft;
	var offset_top=target.offsetTop;
	var offset_height=target.offsetHeight;
	var offset_width=target.offsetWidth;
	
	while(target=target.offsetParent){
		offset_left += target.offsetLeft;
		offset_top += target.offsetTop;
	};

	this.__css={"position":(diy != undefined && diy.position != undefined)?(diy.position):"fixed",
				"z-index":"30001",
				"left":offset_left,
				"top":offset_top,
				"height":offset_height,
				"width":offset_width,
				"filter":"alpha(opacity="+eval(((diy != undefined && diy.filter != undefined)?(diy.filter):"0.3")*100)+")",
				"-moz-opacity":(diy != undefined && diy.filter != undefined)?(diy.filter):"0.3",
				"opacity":(diy != undefined && diy.filter != undefined)?(diy.filter):"0.3",
				"background-color":(diy != undefined && diy.background_color != undefined)?(diy.background_color):"gray"
			};
	if(document.getElementById(ID_PREFIX+id) == null ||　document.getElementById(ID_PREFIX+id) == undefined){
		var sb=new StringBuffer();
		sb.append('<div id=\"');
		sb.append(ID_PREFIX+id);
		sb.append('\"></div>');
		jQuery("body").append(sb.toString());
		this.filter_id=ID_PREFIX+id;
		jQuery("#"+ID_PREFIX+id).css(this.__css);
	}else if(jQuery.trim(jQuery("#"+ID_PREFIX+id).html()) != ""){//页面中存在相同id的元素时使用冲突标记CONFLICT_MARK生成id
		if(document.getElementById(CONFLICT_MARK+id) == null ||　document.getElementById(CONFLICT_MARK+id) == undefined){
			var sb=new StringBuffer();
			sb.append('<div id=\"');
			sb.append(CONFLICT_MARK+id);
			sb.append('\"></div>');
			jQuery("body").append(sb.toString());
			this.filter_id=CONFLICT_MARK+id;
			jQuery("#"+CONFLICT_MARK+id).css(this.__css);
		}
	}
};
/*
function generateDiv(prefix,id){
	var sb=new StringBuffer();
	sb.append('<div id=\"');
	sb.append(prefix+id);
	sb.append('\"></div>');
	return sb.toString();
}*/
/*隐藏滤镜*/
Filter.prototype.hideFilter=function(){
	if(document.getElementById(this.filter_id) != null &&　document.getElementById(this.filter_id) != undefined){
		jQuery("#"+this.filter_id).css({"display":"none"});
	}
};
/*删除滤镜*/
Filter.prototype.delFilter=function(){
	if(document.getElementById(this.filter_id) != null &&　document.getElementById(this.filter_id) != undefined){
		jQuery("#"+this.filter_id).remove();
	}
};
/*显示滤镜【需要先用addFilter方法生成滤镜】*/
Filter.prototype.showFilter=function(){
	if(document.getElementById(this.filter_id) != null &&　document.getElementById(this.filter_id) != undefined){
		jQuery("#"+this.filter_id).css({"display":"block"});
	}
};
/*刷新滤镜*/
Filter.prototype.refreshFilter=function(){
	if(document.getElementById(this.filter_id) != null &&　document.getElementById(this.filter_id) != undefined){
		var target=document.getElementById(this.target_id);
		var offset_left=target.offsetLeft;
		var offset_top=target.offsetTop;
		var offset_height=target.offsetHeight;
		var offset_width=target.offsetWidth;
		
		while(target=target.offsetParent){
			offset_left += target.offsetLeft;
			offset_top += target.offsetTop;
		}
		jQuery("#"+this.filter_id).css(this.__css);
	}
};