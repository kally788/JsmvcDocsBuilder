@echo off
echo "�������������������ʱ��Ĭ�ϲ��������ļ��е�ֵ"
echo ".............................................."
echo "�������ĵ����Ʋ����س����磺JsMvc API �������ַ���"
set /p name=
echo "��������Ҫ����ע���ĵ�����Ŀ�汾�Ų����س����磺1.0 �������ַ���"
set /p projVer=
echo "��������Ҫ����ע���ĵ�����ĿĿ¼�����س����磺d:/input/"
set /p inputDir=
echo "������ע���ĵ����ɺ�ķ���Ŀ¼�����س����磺d:/output/"
set /p outputDir=
java.exe -jar JsmvcDocs-v1.0.jar "%name%" "%projVer%" "%inputDir%" "%outputDir%"
echo ������ɣ��밴������˳�...
pause>nul