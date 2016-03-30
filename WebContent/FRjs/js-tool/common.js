/*
全局常量
*/
//var GLOBAL_VARIABLE_PREFIX="";//声明全局变量时使用的前缀
var ID_PREFIX = "_rf_";//ID前缀
var CONFLICT_MARK = "_rainfossil_";//命名冲突时使用该标记
/*
自定义StringBuffer类 提高字符串连接效率
*/
function StringBuffer () {
  this._strings_ = new Array();
}
StringBuffer.prototype.append = function(str) {
  this._strings_.push(str);
};
StringBuffer.prototype.toString = function() {
  return this._strings_.join("");
};
/*扩展数组的方法 indexOf ie9之前的版本不支持 因此需扩展一下*/
Array.prototype._indexOf=function(_ele){//第一次出现的索引
	for(var i=0,n=this.length;i<n;i++){
		if(this[i] === _ele){
			return i;
		}
	}
	return -1;
};
Array.prototype._lastIndexOf=function(_ele){//最后一次出现的索引
	for(var i=this.length-1;i>-1;i--){
		if(this[i] === _ele){
			return i;
		}
	}
	return -1;
};
Array.prototype._isOnly=function(_ele){//元素是否唯一
	if(this._indexOf(_ele) == this._lastIndexOf(_ele)){
		return true;
	}
	return false;
};
/*克隆js对象（字符串、数字等基本类型无须克隆，直接用=即可）*/
Object.prototype._clone=function(){
	function CloneObj_(){}
	CloneObj_.prototype=this;
	var obj=new CloneObj_();
	for(var o in obj){
		if(typeof(obj[o]) == "object"){
			obj[o]=obj[o]._clone();
		}
	}
	return obj;
}













