/**
 * Created by zhujili on 2015/11/7.
 */

js.model.ContList = function () {

    var protected;
    this.onCreate = function(p){
        protected = p;
        p.setData(
//包含在  @cont{ 和 }cont@ 中的数据会被文档生成器替换掉
/*@cont{*/
{"example":{statement:'',desc:'',example:[{desc:'',code:null}],exte:'',func:{pub:[{name:'',desc:'',param:[{name:'',type:'',desc:''}],ret:{type:'',desc:''},example:[]}],pro:[],sta:[]},evt:{pub:[],pro:[]},attr:{pub:[{name:'',desc:'',type:''}],pro:[],sta:[]},notice:[]}}
 /*}cont@*/
        );
    }
    //提供外部获取数据的接口
    this.getList = function(){
        return protected.getData();
    }


}