/**
 * Created by zhujili on 2016/1/9.
 */
js.tpl.ContTpl = function () {
    return '<div class="cont">\
        <span id="toTop" style="display: inline;"></span>\
        <h1 id="notDoc"></h1>\
        <div id="classMain">\
            <h1 id="class_name"><!--类名--></h1>\
            <div id="class_desc">\
                <h2><!--摘要--></h2>\
                <p><!--类描述--></p>\
            </div>\
            <div id="class_example">\
                <h2 id="class_example_t"><!--示例--></h2>\
                <p id="class_example_desc"><!--示例说明--></p>\
                <pre class="brush: jscript;" id="class_example_code"><!--类使用示例--></pre>\
            </div>\
            <div id="class_member">\
                <h2><!--成员--></h2>\
                <table cellspacing="0" cellpadding="3" border="1" id="class_member_func">\
                    <tr><th width="30%"><!--公共方法--></th><th width="70%"><!--说明--></th></tr>\
                    <tr>\
                        <td><a href="#" title=""><!--方法名称--></a></td>\
                        <td><!--方法概要--></td>\
                    </tr>\
                </table>\
                <table cellspacing="0" cellpadding="3" border="1" id="class_member_attr">\
                    <tr><th width="30%"><!--公共属性--></th><th width="10%"><!--类型--></th><th width="60%"><!--说明--></th></tr>\
                    <tr id="">\
                        <td><!--属性名称--></td>\
                        <td><!--属性类型--></td>\
                        <td><!--属性说明--></td>\
                    </tr>\
                </table>\
            </div>\
            <div id="class_exte">\
                <h2><!--继承--></h2>\
                <a href="#"><!--继承类名称--></a>\
            </div>\
            <hr />\
        </div>\
\
        <!-- 方法模版 -->\
\
        <div id="funcMain">\
            <h3 id="func_name"><!--方法名称--></h3>\
            <div id="func_desc">\
                <h4><!--方法摘要--></h4>\
                <p><!--方法描述--></p>\
            </div>\
            <div id="func_io">\
                <table cellspacing="0" cellpadding="3" border="1" id="func_io_param">\
                    <tr><th width="30%"><!--参数--></th><th width="10%"><!--类型--></th><th width="60%"><!--说明--></th></tr>\
                    <tr>\
                        <td><!--参数名称--></td>\
                        <td><!--参数类型--></td>\
                        <td><!--参数说明--></td>\
                    </tr>\
                </table>\
                <table cellspacing="0" cellpadding="3" border="1" id="func_io_ret">\
                    <tr><th width="30%"><!--返回--></th><th width="70%"><!--说明--></th></tr>\
                    <tr>\
                        <td><!--返回类型--></td>\
                        <td><!--返回描述--></td>\
                    </tr>\
                </table>\
            </div>\
            <div id="func_example">\
                <h4><!--示例--></h4>\
                <p id="func_example_desc"><!--示例说明--></p>\
                <pre class="brush: jscript;"><!--方法使用示例--></pre>\
            </div>\
            <hr />\
        </div>\
        <div class="jsmvcDocs">@2016 <span>JsMvc.cn - by <a href="http://jsmvc.cn/JsmvcDocsBuilder/" target="_blank">JsmvcDocsBuilder</a></span></div>\
    </div>\
    ';
}