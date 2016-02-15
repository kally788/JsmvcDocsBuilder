/**
 * Created by zhujili on 2015/11/5.
 */

js.page.Cont = function () {
    var self = this;
    var html;
    var protected;

    this.onCreate = function(p){
        protected = p;
        html = p.setDisplay($(js.tpl.ContTpl()));
        p.setStage($jsmvc$.facade.reqPage("js.page.Layout").getRight());
        var ls = html.find("*[id]");
        var len = ls.length;
        for (var i = 0; i < len; i++) {
            html[ls[i].id] = $(ls[i]);
        }
        html.toTop.hide();
        protected.getStage().scroll(function() {
            if ($(this).scrollTop() > 100) {
                html.toTop.fadeIn();
            } else {
                html.toTop.fadeOut();
            }
        });
        html.toTop.click(function(e) {
            e.preventDefault();
            protected.getStage().animate({
                scrollTop : 0
            }, 400);
        });

    }
    this.showPage = function(className) {
        self.supers.showPage();
        html.classMain.nextAll("div[id^='funcMain']").remove();
        var data = $jsmvc$.facade.reqModel("js.model.ContList").getList()[className];
        var lang = $jsmvc$.facade.reqPage("js.model.Language");
        if(!data){
            html.notDoc.html(lang.getLang("desc","not_doc",[className?className:lang.getLang("word","related")]));
            html.notDoc.show();
            html.classMain.hide();
            protected.getStage().scrollTop(0);
            protected.getStage().trigger("scroll");
            return;
        }
        html.notDoc.hide();
        html.classMain.show();
        //类名
        html.class_name.html(className);
        //摘要
        if(data.desc){
            html.class_desc.show();
            html.class_desc.children("h2").html(lang.getLang("title","0"));
            html.class_desc.children("p").html(data.desc);
        }else{
            html.class_desc.hide();
        }
        //例子
        if(data.example && data.example.length > 0){
            html.class_example.show();
            html.class_example.empty();
            for(var i=0; i<data.example.length;i++){
                var exa = data.example[i];
                var hItem = html.class_example_t.clone();
                hItem.html(lang.getLang("title","1",[data.example.length>1?(i+1):""]));
                html.class_example.append(hItem);
                for(var j=0;j<exa.length;j++){
                    var childExa = exa[j];
                    var pItem = html.class_example_desc.clone();
                    var preItem = html.class_example_code.clone();
                    pItem.html(childExa.desc?childExa.desc:"");
                    html.class_example.append(pItem);
                    if(childExa.code){
                    	preItem.html(childExa.code);
                    	html.class_example.append(preItem);
                    }
                }
            }
        }else{
            html.class_example.hide();
        }
        //成员
        if(data.statement == "object"){
            if(data.func){
                delete data.func.pro;
                delete data.func.sta;
            }
            if(data.attr){
                delete data.attr.pro;
                delete data.attr.sta;
            }
            if(data.evt){
                delete data.evt.pro;
            }
        }
        if(data.func && ((data.func.pub && data.func.pub.length > 0)||(data.func.pro && data.func.pro.length > 0)||(data.func.sta && data.func.sta.length > 0))
            ||data.attr && ((data.attr.pub && data.attr.pub.length > 0)||(data.attr.pro && data.attr.pro.length > 0)||(data.attr.sta && data.attr.sta.length > 0))
            ||data.evt && ((data.evt.pub && data.evt.pub.length > 0)||(data.evt.pro && data.evt.pro.length > 0))||(data.notice && data.notice.length > 0)){
            html.class_member.show();
            html.class_member.children("h2").html(lang.getLang("title","2"));
            html.class_member.children(":gt(0)").remove();
            //方法
            function createFunc(type, evt){
                var func = html.class_member_func.clone();
                var func_item = func.find("tr:eq(1)").clone();
                var tit = evt?lang.getLang("word","evt"):lang.getLang("word","func");
                if(data.statement == "object"){
                    tit = lang.getLang("word","obj")+tit;
                }else{
                    tit += " " +lang.getLang("word",type);
                }
                func.find("tr:eq(0)").children("th:eq(0)").html(tit);
                func.find("tr:eq(0)").children("th:eq(1)").html(lang.getLang("word","desc"));
                func.find("tr:gt(0)").remove();
                for(var i=0;i<data[evt?"evt":"func"][type].length;i++){
                    var func_item_new = func_item.clone();
                    var itemData = data[evt?"evt":"func"][type][i];
                    var funcName = (type=="sta"||data.statement == "object"?className:(type=="pro"?"protected":"this"))+"."+itemData.name+" ( ";
                    for(var j=0;itemData.param && j<itemData.param.length;j++){
                        funcName += (j>0?", ":"")+itemData.param[j].name;
                    }
                    func_item_new.children("td:eq(0)").html("<a href=\"#funcMain_"+itemData.name+"\">"+funcName+" ) </a>");
                    func_item_new.children("td:eq(1)").html(itemData.desc);
                    func.append(func_item_new);
                }
                html.class_member.append(func);
            }
            if(data.func && data.func.pub && data.func.pub.length > 0){
                createFunc("pub", false);
            }
            if(data.func && data.func.pro && data.func.pro.length > 0){
                createFunc("pro", false);
            }
            if(data.func && data.func.sta && data.func.sta.length > 0){
                createFunc("sta", false);
            }
            //事件
            if(data.evt && data.evt.pub && data.evt.pub.length > 0){
                createFunc("pub", true);
            }
            if(data.evt && data.evt.pro && data.evt.pro.length > 0){
                createFunc("pro", true);
            }
            //属性
            function createAttr(type, notice){
                var attr = html.class_member_attr.clone();
                var attr_item = attr.find("tr:eq(1)").clone();
                var tit = notice?lang.getLang("word","notice"):lang.getLang("word","attr");
                if(data.statement == "object"){
                    tit = lang.getLang("word","obj")+tit;
                }else if(!notice){
                    tit += " "+lang.getLang("word",type);
                }
                attr.find("tr:eq(0)").children("th:eq(0)").html(tit);
                attr.find("tr:eq(0)").children("th:eq(1)").html(lang.getLang("word","type"));
                attr.find("tr:eq(0)").children("th:eq(2)").html(lang.getLang("word","desc"));
                attr.find("tr:gt(0)").remove();
                for(var i=0;i<(notice?data.notice.length:data.attr[type].length);i++){
                    var attr_item_new = attr_item.clone();
                    var itemData = notice?data.notice[i]:data.attr[type][i];
                    var funcName = notice?itemData.name:(type=="sta"||data.statement == "object"?className:(type=="pro"?"protected":"this"))+"."+itemData.name;
                    attr_item_new.children("td:eq(0)").html(funcName);
                    attr_item_new.children("td:eq(1)").html(itemData.type);
                    attr_item_new.children("td:eq(2)").html(itemData.desc);
                    attr.append(attr_item_new);
                }
                html.class_member.append(attr);
            }
            if(data.attr && data.attr.pub && data.attr.pub.length > 0){
                createAttr("pub");
            }
            if(data.attr && data.attr.pro && data.attr.pro.length > 0){
                createAttr("pro");
            }
            if(data.attr && data.attr.sta && data.attr.sta.length > 0){
                createAttr("sta");
            }
            //广播
            if(data.notice && data.notice.length > 0){
                createAttr(null, true);
            }
        }else{
            html.class_member.hide();
        }
        //继承exte
        if(data.exte){
            html.class_exte.show();
            html.class_exte.children("h2").html(lang.getLang("title","3"));
            var a = html.class_exte.children("a:eq(0)");
            a.html(data.exte);
            a.off("click").on("click",function(){
                self.showPage(data.exte);
            });
        }else{
            html.class_exte.hide();
        }
        //方法
        function createFuncList(obj,type,isEvt){
            var item = html.funcMain.clone();
            //名称
            if(data.statement == "object"){
                item.find("#func_name").html(obj.name);
            }else{
                item.find("#func_name").html(obj.name+"<span>"+lang.getLang("word",type)+"</span>");
            }
            //描述
            if(obj.desc){
                item.find("#func_desc").children("p").html(obj.desc);
                item.find("#func_desc").children("h4").html(isEvt?lang.getLang("title","4"):lang.getLang("title","5"));
            }else{
                item.find("#func_desc").remove();
            }
            //参数和返回
            if(!obj.param && !obj.ret){
                item.find("#func_io").remove();
            }else{
                var fincIo = item.find("#func_io");
                var funcIoParam = fincIo.find("#func_io_param");
                var funcIoRet = fincIo.find("#func_io_ret");
                if(!obj.param || obj.param.length == 0){
                    funcIoParam.remove();
                }else{
                    funcIoParam.find("th:eq(0)").html(lang.getLang("word","param"));
                    funcIoParam.find("th:eq(1)").html(lang.getLang("word","type"));
                    funcIoParam.find("th:eq(2)").html(lang.getLang("word","desc"));
                    var tr = funcIoParam.find("tr:eq(1)");
                    tr.remove();
                    for(var i=0;i<obj.param.length;i++){
                        var trItem = tr.clone();
                        var p = obj.param[i];
                        trItem.children("td:eq(0)").html(p.name);
                        trItem.children("td:eq(1)").html(p.type);
                        trItem.children("td:eq(2)").html(p.desc ? p.desc : "");
                        funcIoParam.append(trItem);
                    }
                }
                if(!obj.ret){
                    funcIoRet.remove();
                }else{
                    funcIoRet.find("th:eq(0)").html(lang.getLang("word","ret"));
                    funcIoRet.find("th:eq(1)").html(lang.getLang("word","desc"));
                    funcIoRet.find("tr:eq(1)").children("td:eq(0)").html(obj.ret.type);
                    funcIoRet.find("tr:eq(1)").children("td:eq(1)").html(obj.ret.desc ? obj.ret.desc : "");
                }
            }
            //例子
            var example = item.find("#func_example");
            example.remove();
            for(i=0;obj.example && i<obj.example.length;i++){
                var exampleItem = example.clone();
                var exampleObj = obj.example[i];
                exampleItem.children("h4").html(lang.getLang("title","6",[obj.example.length>1?(i+1):""]));
                var p = exampleItem.children("p");
                p.remove();
                var pre = exampleItem.children("pre");
                pre.remove();
                for(var j=0;j<exampleObj.length;j++){
                    var nP = p.clone();
                    var nPre = pre.clone();
                    var nObj = exampleObj[j];
                    nP.html(nObj.desc?nObj.desc:"");
                    exampleItem.append(nP);
                    if(nObj.code){
                    	nPre.html(nObj.code);
                    	exampleItem.append(nPre);
                    }
                }
                if(!obj.param && !obj.ret){
                    item.find("#func_desc").after(exampleItem);
                }else{
                    item.find("#func_io").after(exampleItem);
                }
            }
            item.attr("id","funcMain_"+obj.name);
            html.classMain.after(item);
        }
        //事件
        for(var i=0;data.evt&&data.evt.pro&&i<data.evt.pro.length;i++){
            createFuncList(data.evt.pro[data.evt.pro.length-1-i],"pro",true);
        }
        for(var i=0;data.evt&&data.evt.pub&&i<data.evt.pub.length;i++){
            createFuncList(data.evt.pub[data.evt.pub.length-1-i],"pub",true);
        }
        for(var i=0;data.func&&data.func.sta&&i<data.func.sta.length;i++){
            createFuncList(data.func.sta[data.func.sta.length-1-i],"sta");
        }
        for(var i=0;data.func&&data.func.pro&&i<data.func.pro.length;i++){
            createFuncList(data.func.pro[data.func.pro.length-1-i],"pro");
        }
        for(var i=0;data.func&&data.func.pub&&i<data.func.pub.length;i++){
            createFuncList(data.func.pub[data.func.pub.length-1-i],"pub");
        }

        SyntaxHighlighter.defaults.gutter = false;
        SyntaxHighlighter.highlight();
        protected.getStage().scrollTop(0);
        protected.getStage().trigger("scroll");
    }


}