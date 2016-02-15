/**
 * Created by zhujili on 2015/11/5.
 */

js.page.Menu = function () {
    var self = this;
    var html;
    var protected;

    this.onCreate = function(p){
        protected = p;
        html = p.setDisplay($(js.tpl.MenuTpl()));
        p.setStage($jsmvc$.facade.reqPage("js.page.Layout").getLeft());
        var ls = html.find("*[id]");
        var len = ls.length;
        for (var i = 0; i < len; i++) {
            html[ls[i].id] = $(ls[i]);
        }
        var modeData = $jsmvc$.facade.reqModel("js.model.MenuList");
        if(modeData.getVersion()){
            html.children("p:eq(0)").html(modeData.getName()+" - Version "+modeData.getVersion());
        }else{
            html.children("p:eq(0)").html(modeData.getName());
        }

        //创建菜单
        var list = modeData.getList();
        var ul = html.children("ul");
        ul.empty();
        if(!list || typeof list != "object" || list.length == 0){
            $jsmvc$.facade.reqPage("js.page.Cont").showPage();
        }else{
            for(var i=0;i<list.length;i++){
                var item = html.tpl.clone();
                var data = list[i];
                var span = item.children("span");
                span.html(data.name);
                if(!data.list || typeof data.list != "object" || data.list.length < 1){
                    item.children("ul").remove();
                    item.removeClass("file");
                    item.addClass("js");
                    span.attr("id", data.name);
                    FastClick.attach(span.get(0));
                    span.on("click",(function(className){return function(){select(className, $(this));}})(data.name));
                    if(i==0){
                        span.trigger("click");
                    }
                }else{
                    item.removeClass("js");
                    item.addClass("file");
                    var childUL = item.find("ul:eq(0)");
                    var childLI = childUL.children("li:eq(0)");
                    childUL.empty();
                    for(var j=0;j<data.list.length;j++){
                        var childLIItem = childLI.clone();
                        var childSpan = childLIItem.children("span");
                        childSpan.html(data.list[j]);
                        childSpan.attr("id", data.list[j]);
                        FastClick.attach(childSpan.get(0));
                        childSpan.on("click",(function(className){return function(){select(className, $(this));}})(data.name+"."+data.list[j]));
                        childUL.append(childLIItem);
                        if(i==0 && j==0){
                            childSpan.trigger("click");
                        }
                    }
                    if(i!=0){
                        childUL.hide();
                        childUL.attr("hide",true);
                    }
                    FastClick.attach(span.get(0));
                    span.on("click",function(){
                        html.css({width:"auto"});
                        html.removeClass("menuBlock");
                        html.addClass("menuInline");

                        var ul = $(this).next("ul");
                        if(ul.attr("hide")=="true"){
                            ul.show();
                            ul.attr("hide",false);
                        }else{
                            ul.hide();
                            ul.attr("hide",true);
                        }

                        html.css({width:html.innerWidth()+"px"});
                        html.removeClass("menuInline");
                        html.addClass("menuBlock");
                    });
                }
                ul.append(item);
            }
        }
    }

    var select = function(className, obj){
        html.find(".opt").removeClass("opt");
        obj.parent().addClass("opt");
        $jsmvc$.facade.reqPage("js.page.Cont").showPage(className);
    }


    this.showPage = function(){
        self.supers.showPage();

        html.css({width:html.innerWidth()+"px"});
        html.removeClass("menuInline");
        html.addClass("menuBlock");
    }
}