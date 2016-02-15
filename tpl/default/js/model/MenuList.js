/**
 * Created by zhujili on 2015/11/7.
 */

js.model.MenuList = function () {
    var protected;
    var docName = {v:"JsMvc API"};
    var docVersion = {v:"1.0"};
    this.onCreate = function(p){
        protected = p;
        p.setData(
//包含在  @cont{ 和 }cont@ 中的数据会被文档生成器替换掉
/*@cont{*/
[{"name":"example","list":[]},{"name":"dir","list":["children"]}]
/*}cont@*/
        );
    }
    //提供外部获取数据的接口
    this.getList = function(){
        return protected.getData();
    }
    this.getName = function(){
        return docName.v;
    }
    this.getVersion = function(){
        return docVersion.v;
    }
}