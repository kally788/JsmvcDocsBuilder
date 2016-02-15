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

$jsmvc$.core.ModelAbs = function (className) {

    //模块类名
    this.className = className;
    //储存到模块中的数据对象
    var data;
    var self = this;

    //--------------------------------------------------------------------------------------------------
    //                  保护方法
    //--------------------------------------------------------------------------------------------------

    /**
     * 添加/覆盖数据
     * @param v {*} 任意类型
     */
    var setData = function(v){
        data = v;
    }

    /**
     * 获取数据
     * @returns {*}
     */
    var getData = function(){
        return data;
    }

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
            getData:getData,
            setData:setData
        };
    };

    /**
     * 模块销毁。通过 $jsmvc$.facade.delModel 删除模块时触发，可显式调用
     */
    this.onDestroy = function(){
        self.onDestroy = undefined;
        $jsmvc$.facade.delPage(className);
    };

    //--------------------------------------------------------------------------------------------------
    //                  属性
    //--------------------------------------------------------------------------------------------------

    /**
     * 公开访问函数
     * @type {}
     */
    this.supers = {
        onDestroy:self.onDestroy
    };

}