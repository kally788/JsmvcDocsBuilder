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

$jsmvc$.core.ControAbs = function (className) {

    //模块类名
    this.className = className;

    //--------------------------------------------------------------------------------------------------
    //                  事件
    //--------------------------------------------------------------------------------------------------

    /**
     * 模块创建。当模块被创建时触发，不可显式调用
     * @param parent {*} 父类链中需要提供保护访问的属性或函数集合
     * @returns {*} 提供给子类保护访问的属性或函数集合
     */
    this.onCreate = function(parent){
        return null;
    };

}