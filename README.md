# learn-vysor
android

# install app
adb forward tcp:8091 tcp:5000  (adb forward local remote)

Brower: http://localhost:8091/app

sourceDir=com.xinxin.vysor-xxxx/base.apk

adb shell "export CLASSPATH=/data/app/${datadir};exec app_process /system/bin com.xinxin.vysor.core.Main '$main'"

adb forward tcp:8090 tcp:53518

# see result
Brower: http://localhost:8090/screenshot.jpg

