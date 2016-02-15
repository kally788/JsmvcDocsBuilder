/**
 * Created by zhujili on 2015/11/5.
 */
$jsmvc$.run.addFacade(function(){
    this.WINDOW_RESIZE = "window_resize";
    this.startup = function(){
        $jsmvc$.facade.reqPage("js.page.Layout").showPage();
        $jsmvc$.facade.reqPage("js.page.Menu").showPage();
    }
});