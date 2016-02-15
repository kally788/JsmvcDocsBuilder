@echo off
echo "以下输入项如果不输入时，默认采用配置文件中的值"
echo ".............................................."
echo "请输入文档名称并按回车，如：JsMvc API 或任意字符串"
set /p name=
echo "请输入您要生成注释文档的项目版本号并按回车，如：1.0 或任意字符串"
set /p projVer=
echo "请输入您要生成注释文档的项目目录并按回车，如：d:/input/"
set /p inputDir=
echo "请输入注释文档生成后的放置目录并按回车，如：d:/output/"
set /p outputDir=
java.exe -jar JsmvcDocs-v1.0.jar "%name%" "%projVer%" "%inputDir%" "%outputDir%"
echo 生成完成，请按任意键退出...
pause>nul