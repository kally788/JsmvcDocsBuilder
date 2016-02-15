/**
 * Created by zhujili on 2015/11/5.
 */

js.page.Layout = function () {
    var self = this;
    var left;
    var right;
    var protected;
    this.onCreate = function(p){
        protected = p;
        p.setStage(document.body);
        var html = protected.setDisplay($(js.tpl.LayoutTpl()));
        var ls = html.find("*[id]");
        var len = ls.length;
        for (var i = 0; i < len; i++) {
            html[ls[i].id] = $(ls[i]);
        }
        left = html.left;
        right = html.right;
    }

    this.getLeft = function(){
        return left;
    }

    this.getRight = function(){
        return right;
    }

    var changeWindows = function(){
        var win = $(window);
        var width = win.width();
        var height = win.height();
        left.css({height:height+"px",width:width*0.2+"px"});
        right.css({height:height+"px"});
        $jsmvc$.facade.sendBroadcast($jsmvc$.facade.WINDOW_RESIZE, {width: width, height: height});
    }

    this.showPage = function(){
        self.supers.showPage();
        $(window).off("resize",changeWindows).on("resize",changeWindows).trigger("resize");
    }

}