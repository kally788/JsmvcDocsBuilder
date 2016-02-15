JsmvcDocsBuilder 概述
=====
该工具可以把 JsMvc 开发的项目，根据注释生成 API 文档。
需要依赖 java jdk-7 环境

### 使用方式：
1. 配置
打开配配置文件  config.conf
修改 sourceDir 要生成API文档的项目目录
修改 outputDir 生成后的文档后放置目录
修改 docsName 您的API文档名称
其它的配置可以使用默认值

2. 生成文档
执行 startup.bat
根据提示输入选项或者一路按回车即可

3. 如果在linux下打包，需要自己写一个sh脚本。可参考startup.bat脚本

### 代码注释规范

只会生成 /** */ 这样包含起来的注释
 
 @type 类型，值：class object function attr，表明为是一个类、一个对象、一个方法、一个属性

 @desc 描述信息，可以是一行或者多行
 
 @example 创建一个例子，例子代码可以用 <code></code>包含起来
 
 @name 名称
 
 @param 参数，值 aaa:bbb 说明。aaa为参数名称，bbb为参数类型
 
 @returns 返回值，值  类型 说明
 
 @value 指明一个属性值的类型
 
 @modification 指明修饰符，值：pro pub sta，表明为是一个保护方法、公共方法、静态方法
 
 您可以参考  <https://github.com/kally788/jsmvc/tree/master/$jsmvc$> 中的注释格式
