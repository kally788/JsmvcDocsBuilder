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

$jsmvc$.core.FacadeAbs = function (template) {

    var self = this;
    //模块容器
    var model = {},page = {},contro = {};
    //观察者列表
    var observer = {};
    //广播ID
    var noticeID = 0;
    //错误消息提示
    var errMsg = {
        "err1":"Can't find JS class # ",
        "err2":"Can't find HTML template # ",
        "err3":"Error:Loop extends # "
    }

    //创建继承链
    var createExtends = function(classFunc, className, type, chain, topClassName){
        chain[className] = true;
        var abs,absP,absM,absC,obj,retProt,getProt;
        //创建超类
        absP = $jsmvc$.core.PageAbs;
        absM = $jsmvc$.core.ModelAbs;
        absC = $jsmvc$.core.ControAbs;
        switch (type){
            case "page":
                abs = absP;
                break;
            case "model":
                abs = absM;
                break;
            case "contro":
                abs = absC;
                break;
        }
        if(classFunc.parentClass && typeof classFunc.parentClass == "string"){
            var pClass = findClass(classFunc.parentClass);
            if(chain[classFunc.parentClass]){
                alert(errMsg.err3+classFunc.parentClass);
                throw errMsg.err3+classFunc.parentClass;
            }
            getProt = createExtends(pClass, classFunc.parentClass, type, chain, topClassName);
            if(pClass === absP || pClass === absM || pClass === absC){
                obj = new pClass(topClassName);
            }else{
                pClass.prototype.constructor = pClass;
                obj = new pClass();
            }
        }else if(abs && classFunc !== abs){
            //注册到框架中的3大模块默认继承超类
            obj = new abs(topClassName);
        }
        if(!getProt){
            getProt = {};
        }
        if(obj){
            if(obj.onCreate && typeof obj.onCreate == "function"){
                retProt = obj.onCreate(getProt);
                delete obj.onCreate;
            }
            classFunc.prototype = obj;
        }
        var nextProt = {};
        for(var i in getProt){
            nextProt[i] = getProt[i];
        }
        if(retProt && typeof retProt == "object"){
            for(var i in retProt){
                nextProt[i] = retProt[i];
            }
        }
        return nextProt;
    }

    //检查类的继承链是否存在错误
    var inspectExtends = function(classFunc){
        if(classFunc.parentClass && typeof classFunc.parentClass == "string"){
            inspectExtends(findClass(classFunc.parentClass))
        }
    }

    //根据类路径，查找出相关的类
    var findClass = function(className){
        var classFunc;
        if(className && typeof className == "string"){
            var routeList = className.split(".");
            var classFunc = window[routeList[0]];
            if(classFunc){
                for(var i=1;i<routeList.length;i++){
                    classFunc = classFunc[routeList[i]];
                    if(!classFunc){
                        break;
                    }
                }
            }
        }
        if(!classFunc || typeof classFunc != "function"){
            alert(errMsg.err1+className);
            throw errMsg.err1+className;
        }
        return classFunc;
    }

    //获取HTML模版
    var getTemplate = function(templateName){
        if(!template || !template[templateName]){
            alert(errMsg.err2 + templateName);
            throw errMsg.err2 + templateName;
        }
        return template[templateName];
    }

    //订阅广播
    var attachNotice = function(evtName, cb){
        if (typeof cb != "function" && typeof cb != "string") {
            return -1;
        }
        if(!observer[evtName]){
            observer[evtName] = {};
        }
        observer[evtName][++noticeID] = cb;
        return noticeID;
    }

    //取消广播订阅
    var removeNotice = function(evtName, evtId){
        if(observer[evtName] && observer[evtName][evtId]){
            delete observer[evtName][evtId];
            return true;
        }
        return false;
    }

    //给PAGE抽象类添加顶级函数
    $jsmvc$.core.PageAbs.prototype.getTemplate = getTemplate;
    $jsmvc$.core.PageAbs.prototype.attachNotice = attachNotice;
    $jsmvc$.core.PageAbs.prototype.removeNotice = removeNotice;

    //发布广播
    var sendBroadcast = function(evtName, data){
        if(!observer[evtName]){
            return -1;
        }
        var count = 0;
        for(var i in observer[evtName]){
            var func = observer[evtName][i];
            if(typeof func == "function"){
                func(evtName, data);
            }else{
                newInstance(func, "contro", evtName, data);
            }
            count ++;
        }
        return count;
    }

    //注册MODEL模块
    var reqModel = function(className){
        if(model[className]){
            return model[className];
        }
        return model[className] = newInstance(className,"model");
    }

    //注册PAGE模块
    var reqPage = function(className){
        if(page[className]){
            return page[className];
        }
        return page[className] = newInstance(className,"page");
    }

    //注册CONTRO模块
    var reqContro = function(className, evtName){
        if(contro[className]){
            return;
        }
        inspectExtends(findClass(className));
        contro[className] = {evtName:evtName, evtId:attachNotice(evtName, className)};
    }

    //移除MODEL模块
    var delModel = function(className){
        findClass(className);
        var obj = model[className];
        if(obj){
            delete model[className];
            if(typeof obj.onDestroy == "function"){
                obj.onDestroy();
            }
        }
    }

    //移除PAGE模块
    var delPage = function(className){
        findClass(className);
        var obj = page[className];
        if(obj){
            delete page[className];
            if(typeof obj.onDestroy == "function"){
                obj.onDestroy();
            }
        }
    }

    //移除CONTRO模块
    var delContro = function(className){
        findClass(className);
        if(contro[className]){
            removeNotice(contro[className].evtName, contro[className].evtId);
            delete contro[className];
        }
    }

    //新建一个实例
    var newInstance = function(className, type){
        var mClass = findClass(className);
        var getProt = createExtends(mClass, className, type ? type : null, {}, className);
        mClass.prototype.constructor = mClass;
        var obj = type == "contro"?new mClass(arguments[2],arguments[3]):new mClass();
        if(obj.onCreate && typeof obj.onCreate == "function"){
            obj.onCreate(getProt);
            delete obj.onCreate;
        }
        return obj;
    }

    //--------------------------------------------------------------------------------------------------
    //                  公共方法
    //--------------------------------------------------------------------------------------------------

    /**
     * 发布广播
     * @param string evtName  要派发的事件名
     * @param object data  派发的数据对象
     * @returns number 派发次数
     */
    this.sendBroadcast = sendBroadcast;

    /**
     * 注册MODEL模块并返回实例（单例）
     * @param string className 类命全路径，如：a.b.c
     * @returns object
     */
    this.reqModel = reqModel;

    /**
     * 注册PAGE模块并返回实例（单例）
     * @param string className 类命全路径，如：a.b.c
     * @returns object
     */
    this.reqPage = reqPage;

    /**
     * 注册CONTRO模块并进行事件监听
     * @param string className 类命全路径，如：a.b.c
     * @param string evtName   事件名称
     * @returns void
     */
    this.reqContro = reqContro;

    /**
     * 移除MODEL模块
     * @param string className 类命全路径，如：a.b.c
     * @returns void
     */
    this.delModel = delModel;

    /**
     * 移除PAGE模块
     * @param string className 类命全路径，如：a.b.c
     * @returns void
     */
    this.delPage = delPage;

    /**
     * 移除CONTRO模块并删除对应事件侦听
     * @param string className 类命全路径，如：a.b.c
     * @returns void
     */
    this.delContro = delContro;

    /**
     * 新建一个实例
     * @param className
     * @param type
     * @returns {mClass}
     */
    this.newInstance = newInstance;

    /**
     * 启动函数，需要在子类中重写该方法
     */
    this.startup = function(){
        //启动业务
    }

    //--------------------------------------------------------------------------------------------------
    //                  属性
    //--------------------------------------------------------------------------------------------------

    /**
     * 公开访问函数
     * @type {}
     */
    this.supers = {
        reqModel:reqModel,
        reqPage:reqPage,
        reqContro:reqContro,
        delModel:delModel,
        delPage:delPage,
        delContro:delContro,
        newInstance:newInstance,
        sendBroadcast:sendBroadcast,
        startup:self.startup
    }
}