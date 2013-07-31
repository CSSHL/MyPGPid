$wshell = new-object -com wscript.shell
$wshell.run("gpg --card-edit")
Start-Sleep 3
$wshell.sendkeys("admin{enter}")
Start-Sleep 1
$wshell.sendkeys("generate{enter}")
Start-Sleep 1
$wshell.sendkeys("n{enter}")
Start-Sleep 1
$wshell.sendkeys("12345678{enter}")
Start-Sleep 1
$wshell.sendkeys("123456{enter}")
Start-Sleep 1
$wshell.sendkeys("0{enter}")
Start-Sleep 1
$wshell.sendkeys("y{enter}")
Start-Sleep 1
$wshell.sendkeys("MyPGPid{enter}")
Start-Sleep 1
$wshell.sendkeys("test@test.org{enter}")
Start-Sleep 1
$wshell.sendkeys("{enter}")
Start-Sleep 1
$wshell.sendkeys("O{enter}")
Start-Sleep 20


$wshell.sendkeys("quit{enter}")
