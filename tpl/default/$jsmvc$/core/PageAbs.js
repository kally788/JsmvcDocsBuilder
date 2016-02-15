/*!
 *       888    d8888b       d8b   d8b    888   888    d8888b
 *       888 d88          d88   888   88b 888   888 d88      88b
 *       888    d8888b    888   888   888 888   888 888
 * d88   88b          88b 888   888   888 d88   88b d88      88b
 * d8888888b    d8888b    888   888   888    d8b       d8888b
 *
 * JsMvc v1.0
 * http://jsmvc.cn/code
 *
 * Copyright (c) 2016 JsMvc
 * Released under the MIT license
 * http://jsmvc.cn/license
 */

$jsmvc$.core.PageAbs = function (className) {

    //模块类名
    this.className = className;
    //错误消息提示
    var errMsg = {
        "err1":"Stage must be '<HTML Element> or <JQuery Object>'",
        "err2":"Display must be '<HTML Element> or <JQuery Object> or <HTML Template Route String>'"
    };
    //舞台引用
    var stage;
    //HTML显示对象
    var display;
    var self = this;

    //检查是否为JQ对象
    var isJQuery = function(element){
        return window.jQuery && element instanceof window.jQuery;
    }

    //检查一个对象是否为HTML节点元素或者JQ对象
    var isElement = function(element){
        if(window.HTMLElement && element instanceof window.HTMLElement){
            return true;
        }
        if(window.Element && element instanceof window.Element){
            return true;
        }
        if(window.jQuery && element instanceof window.jQuery){
            return true;
        }
        if(!window.Element){
            try{
                //兼容IE6-7
                element.removeChild(element.appendChild(document.createElement("TEST")));
                return element.nodeType === 1;
            }catch (e){
                return false;
            }
        }
        return false;
    }

    //检查当前页面处于显示状态
    var isActive = function(matchStage){
        if(isElement(display)){
            var active = isJQuery(display)?(display.parent().length?true:false):(display.parentNode?true:false);
            if(active && matchStage){
                if(!stage){
                    return false;
                }
                if(isJQuery(stage)){
                    if(isJQuery(display)){
                        if($.contains(stage.get(stage.length-1),display.get(0))){
                            return active;
                        }
                    }else if($.contains(stage.get(stage.length-1),display)){
                        return active;
                    }
                }else{
                    if(isJQuery(display)){
                        if(display.parent().get(0) === stage){
                            return active;
                        }
                    }else if(display.parentNode === stage){
                        return active;
                    }
                }
                return false;
            }
            return active;
        }else{
            return false;
        }
    }

    //--------------------------------------------------------------------------------------------------
    //                  保护方法
    //--------------------------------------------------------------------------------------------------

    /**
     * 设置舞台
     * @param element|null element 一个HTML节点元素或者JQ对象
     */
    var setStage = function(element){
        if(!isElement(element)){
            alert(errMsg.err1);
            throw errMsg.err1;
        }
        if(stage && stage === element){
            return element;
        }
        return stage = element;
    }

    /**
     * 获取舞台
     * @returns element
     */
    var getStage = function(){
        return stage;
    }

    /**
     * 删除舞台同时移出已添加的显示对象
     * @returns {boolean}
     */
    var delStage = function(){
        if(stage){
            if(isActive(true)){
                isJQuery(display)?display.remove():display.parentNode.removeChild(display);
            }
            stage = undefined;
            return true;
        }
        return false;
    }

    /**
     * 设置显示对象，如果已经存在显示对象，那么新的显示对象会替换掉旧的，并在原位置显示
     * @param string|element|JQuery element 可以是一个element节点、JQ对象或者是HTML模版路径
     */
    var setDisplay = function(element){
        if(!element || (typeof element != "string") && !isElement(element)){
            alert(errMsg.err2);
            throw errMsg.err2;
        }
        if(display && element === display){
            return element;
        }
        var oldElement;
        if(isActive(false)){
            oldElement = display;
        }
        //创建新的HTML内容
        if(typeof element != "string"){
            display = element;
        }else{
            var tpl = getTemplate(element);
            display = document.createElement("DIV");
            display.innerHTML = tpl.replace( /^[\s\uFEFF\xA0]+|[\s\uFEFF\xA0]+$/g, "");
            if(display.childNodes.length == 1){
                display = display.childNodes[0];
            }
        }
        //替换到已存在的HTML相同的位置上
        if(oldElement){
            if(isJQuery(oldElement)){
                oldElement.after(display);
                oldElement.remove();
            }else{
                if(isJQuery(display)){
                    display.insertAfter(oldElement);
                }else{
                    oldElement.parentNode.insertBefore(display, oldElement);
                }
                oldElement.parentNode.removeChild(oldElement);
            }
        }
        return display;
    }

    /**
     * 获取显示对象
     * @returns HTML Element | JQuery
     */
    var getDisplay = function(){
        return display;
    }

    /**
     * 删除显示对象
     * @returns boolean
     */
    var delDisplay = function(){
        if(display){
            if(isActive(false)){
                isJQuery(display)?display.remove():display.parentNode.removeChild(display);
            }
            display = undefined;
            return true;
        }
        return false
    }

    /**
     * 获取HTML模版
     * @param templateName
     * @returns string
     */
    var getTemplate = this.getTemplate;
    delete this.getTemplate;

    /**
     * 订阅广播
     * @param string evtName  事件名称
     * @param function cb 回调方法，第一个参数为事件名，第二个参数为事件带回来的数据对象
     * @returns number 事件ID
     */
    var attachNotice = this.attachNotice;
    delete this.attachNotice;

    /**
     * 取消广播订阅
     * @param string evtName  要移除的事件名
     * @param number evtId  事件ID，添加事件时获得
     * @returns boolean
     */
    var removeNotice = this.removeNotice;
    delete this.removeNotice;

    //--------------------------------------------------------------------------------------------------
    //                  公共方法
    //--------------------------------------------------------------------------------------------------

    /**
     * 把显示对象添加到舞台上（舞台原有的所有元素会被移除）
     * @returns {boolean}
     */
    this.showPage = function(){
        if(!stage || !display || isActive(true)){
            return false;
        }
        if(isJQuery(stage)) {
            stage.children().detach();
            stage.append(display);
        }else{
            while(stage.hasChildNodes())
            {
                stage.removeChild(stage.firstChild);
            }
            if(isJQuery(display)){
                for(var i=0;i<display.length;i++){
                    stage.appendChild(display.get(i))
                }
            }else{
                stage.appendChild(display);
            }
        }
        return true;
    };

    /**
     * 检查当前页面处于显示状态
     * @param boolean matchStage 是否匹配当前的舞台，当true时，显示对象必须是添加至当前的舞台上，默认false
     * @returns {boolean}
     */
    this.isActive = isActive;

    //--------------------------------------------------------------------------------------------------
    //                  事件
    //--------------------------------------------------------------------------------------------------

    /**
     * 模块创建。当模块被创建时触发，不可显式调用
     * @param parent {*} 父类链中需要提供保护访问的属性或函数集合
     * @returns {*} 提供给子类保护访问的属性或函数集合
     */
    this.onCreate = function(parent){
        return {
            setStage:setStage,
            getStage:getStage,
            delStage:delStage,
            setDisplay:setDisplay,
            getDisplay:getDisplay,
            delDisplay:delDisplay,
            getTemplate:getTemplate,
            attachNotice:attachNotice,
            removeNotice:removeNotice
        };
    };

    /**
     * 模块销毁。通过 $jsmvc$.facade.delPage 删除模块时触发，可显式调用
     */
    this.onDestroy = function(){
        self.onDestroy = undefined;
        $jsmvc$.facade.delPage(className);
        delDisplay();
    };

    //--------------------------------------------------------------------------------------------------
    //                  属性
    //--------------------------------------------------------------------------------------------------

    /**
     * 公开访问函数
     * @type {}
     */
    this.supers = {
        showPage:self.showPage,
        isActive:self.isActive,
        onDestroy:self.onDestroy
    };
}
