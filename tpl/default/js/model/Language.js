/**
 * Created by lizhi on 14-7-18.
 * 语言包
 */
js.model.Language = function () {

    /**
     * --------------------------------------------------------------------------------------------
     * 语言包定义，可定义多级对象，一级结构中不可直接定义语言内容
     * --------------------------------------------------------------------------------------------
     */

    //包结构
    var lang = {
        desc:{},
        word:{},
        title:{}
    };

    lang.desc["not_doc"]          = "没有%?%类的文档！~";
    lang.desc["not_code"]         = "<!-- 没有示例代码 -->";

    lang.word["related"]          = "相关";
    lang.word["desc"]             = "说明";
    lang.word["type"]             = "类型";
    lang.word["obj"]              = "对象";
    lang.word["evt"]              = "事件";
    lang.word["func"]             = "方法";
    lang.word["attr"]             = "属性";
    lang.word["notice"]          = "广播";
    lang.word["pub"]              = "[ 公共 ]";
    lang.word["pro"]              = "[ 保护 ]";
    lang.word["sta"]              = "[ 静态 ]";
    lang.word["param"]              = "参数";
    lang.word["ret"]              = "返回";

    lang.title["0"]              = "摘要：";
    lang.title["1"]              = "示例%?%：";
    lang.title["2"]              = "成员：";
    lang.title["3"]              = "继承：";
    lang.title["4"]              = "事件概述：";
    lang.title["5"]              = "方法概述：";
    lang.title["6"]              = "使用示例%?%：";

    /**
     * 获取语言包字符串
     * @param string model 语言包模块，格式 "a.b.c"（根据您定义的包结构命名）
     * @param string id    语言ID号
     * @param array  rep   需要替换的字符串数组列表 [可选]
     * @returns {*}        返回null时，表示找不到该条记录，可能是模块错误或者是ID错误
     */
    this.getLang = function(model, id, rep){
        var route = model.split(".");
        var ret = lang;
        for(var i=0;i<route.length;i++){
            ret = ret[route[i]];
            if(!ret){
                return null;
            }
        }
        ret = ret["" + id];
        if(!ret){
           return null;
        }
        ret = ret.split("%?%");
        var text = ret[0];
        for(var i=0;typeof rep == "object" && i<ret.length-1;i++){
            if(rep[i] !== undefined){
                text += rep[i];
            }else{
                text += "%null%";
            }
            text += ret[i+1];
        }
        return text;
    }
}